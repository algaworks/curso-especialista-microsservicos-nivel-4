package com.algaworks.algashop.billingscheduler.infrastructure;

import com.algaworks.algashop.billingscheduler.application.CancelExpiredInvoicesApplicationService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.dao.DataAccessException;
import org.springframework.jdbc.core.JdbcOperations;
import org.springframework.jdbc.core.PreparedStatementSetter;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.stereotype.Service;
import org.springframework.transaction.support.TransactionTemplate;

import java.time.Duration;
import java.util.List;
import java.util.UUID;

@Service
@RequiredArgsConstructor
@Slf4j
public class CancelExpiredInvoicesApplicationServiceJDBCImpl implements CancelExpiredInvoicesApplicationService {

    private final JdbcOperations jdbcOperations;
    private final TransactionTemplate transactionTemplate;

    private final FastpayPaymentAPIClient fastpayPaymentAPIClient;

    private static final Duration EXPIRED_SINCE = Duration.ofDays(1);

    private static final int BATCH_LIMIT = 50;

    private static final String UNPAID_STATUS = "UNPAID";

    private static final String CANCEL_STATUS = "CANCELED";
    private static final String CANCEL_REASON = "Invoice expired";

    private static final String SELECT_EXPIRED_INVOICES_SQL = String.format("""
                select i.id, ps.gateway_code
                from invoice i
                inner join payment_settings ps on i.payment_settings_id = ps.id
                where i.expires_at <= now() - interval '%d days'
                and i.status = ?
                order by i.expires_at asc
                limit ?
                for update
                skip locked
            """, EXPIRED_SINCE.toDays());

    private static final String UPDATE_INVOICE_STATUS_SQL = """
                update invoice set status = ?, canceled_at = now(), cancel_reason = ?
                where id = ?
            """;

    @Override
    public void cancelExpiredInvoices() {
        transactionTemplate.execute(status -> {
            List<InvoiceProjection> invoiceIds = fetchExpiredInvoices();
            log.info("Task - Total invoices fetched: {}", invoiceIds.size());
            if (invoiceIds.isEmpty()) {
                log.info("Task - No expired invoices found for cancellation");
                return true;
            }
            int totalCanceledInvoices = cancelInvoices(invoiceIds);
            log.info("Task - Total invoices canceled: {}", totalCanceledInvoices);
            return true;
        });
    }

    private List<InvoiceProjection> fetchExpiredInvoices() {
        PreparedStatementSetter preparedStatementSetter = ps -> {
            ps.setString(1, UNPAID_STATUS);
            ps.setInt(2, BATCH_LIMIT);
        };
        RowMapper<InvoiceProjection> mapper = (resultSet, rowNum) -> new InvoiceProjection(
                resultSet.getObject("id", UUID.class),
                resultSet.getString("gateway_code")
        );
        return jdbcOperations.query(SELECT_EXPIRED_INVOICES_SQL, preparedStatementSetter, mapper);
    }

    private int cancelInvoices(List<InvoiceProjection> invoices) {
        List<InvoiceProjection> cancelledInvoices = invoices.stream()
                .filter(invoiceProjection -> {
                    try {
                        fastpayPaymentAPIClient.cancel(invoiceProjection.getPaymentGatewayCode());
                        log.info("Task - Invoice {} has the payment {} cancelled on gateway",
                                invoiceProjection.getId(), invoiceProjection.getPaymentGatewayCode());
                        return true;
                    } catch (Exception e) {
                        log.error("Task - Failed to cancel invoice {} payment {} on the gateway",
                                invoiceProjection.getId(), invoiceProjection.getPaymentGatewayCode());
                        return false;
                    }
                }).toList();

        try {
            jdbcOperations.batchUpdate(UPDATE_INVOICE_STATUS_SQL,
                    cancelledInvoices,
                    cancelledInvoices.size(),
                    (ps, invoiceProjection) -> {
                        ps.setString(1, CANCEL_STATUS);
                        ps.setString(2, CANCEL_REASON);
                        ps.setObject(3, invoiceProjection.getId());
                    }
                    );
            log.info("Task - Invoices canceled");
            return cancelledInvoices.size();
        } catch (DataAccessException e) {
            log.error("Task - Failed to cancel invoices", e);
            return 0;
        }
    }
}
