package com.algaworks.algashop.billing.domain.model.invoice;

import com.algaworks.algashop.billing.domain.model.AbstractAuditableAggregateRoot;
import com.algaworks.algashop.billing.domain.model.DomainException;
import com.algaworks.algashop.billing.domain.model.IdGenerator;
import com.algaworks.algashop.billing.domain.model.invoice.payment.PaymentStatus;
import jakarta.persistence.*;
import lombok.*;
import org.apache.commons.lang3.StringUtils;

import java.math.BigDecimal;
import java.time.OffsetDateTime;
import java.util.*;

@Setter(AccessLevel.PRIVATE)
@Getter
@EqualsAndHashCode(onlyExplicitlyIncluded = true, callSuper = false)
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor(access = AccessLevel.PROTECTED)
@Entity
public class Invoice extends AbstractAuditableAggregateRoot<Invoice> {

    @Id
    @EqualsAndHashCode.Include
    private UUID id;
    private String orderId;
    private UUID customerId;

    private OffsetDateTime issuedAt;
    private OffsetDateTime paidAt;
    private OffsetDateTime canceledAt;
    private OffsetDateTime expiresAt;

    private BigDecimal totalAmount;

    @Enumerated(EnumType.STRING)
    private InvoiceStatus status;

    @OneToOne(cascade = CascadeType.ALL, fetch = FetchType.EAGER, orphanRemoval = true)
    private PaymentSettings paymentSettings;

    @ElementCollection
    @CollectionTable(name = "invoice_line_item",
            joinColumns = @JoinColumn(name = "invoice_id"))
    private Set<LineItem> items = new HashSet<>();

    @Embedded
    private Payer payer;

    private String cancelReason;

    public static Invoice issue(String orderId,
                                UUID customerId,
                                Payer payer,
                                Set<LineItem> items) {
        Objects.requireNonNull(customerId);
        Objects.requireNonNull(payer);
        Objects.requireNonNull(items);

        if (StringUtils.isBlank(orderId)) {
            throw new IllegalArgumentException();
        }

        if (items.isEmpty()) {
            throw new IllegalArgumentException();
        }

        BigDecimal totalAmount = items.stream().map(LineItem::getAmount)
                .reduce(BigDecimal.ZERO, BigDecimal::add);

        Invoice invoice = new Invoice(
                IdGenerator.generateTimeBasedUUID(),
                orderId,
                customerId,
                OffsetDateTime.now(),
                null,
                null,
                OffsetDateTime.now().plusDays(3),
                totalAmount,
                InvoiceStatus.UNPAID,
                null,
                items,
                payer,
                null
        );
        invoice.registerEvent(new InvoiceIssuedEvent(invoice.getId(),
                invoice.getCustomerId(), invoice.getOrderId(), invoice.getIssuedAt()));
        return invoice;
    }

    public Set<LineItem> getItems() {
        return Collections.unmodifiableSet(this.items);
    }

    public boolean isCanceled() {
        return InvoiceStatus.CANCELED.equals(this.getStatus());
    }

    public boolean isUnpaid() {
        return InvoiceStatus.UNPAID.equals(this.getStatus());
    }

    public boolean isPaid() {
        return InvoiceStatus.PAID.equals(this.getStatus());
    }

    public void markAsPaid() {
        if (!isUnpaid()) {
            throw new DomainException(String.format("Invoice %s with status %s cannot be marked as paid",
                    this.getId(), this.getStatus().toString().toLowerCase()));
        }
        setPaidAt(OffsetDateTime.now());
        setStatus(InvoiceStatus.PAID);
        setExpiresAt(null);
        registerEvent(new InvoicePaidEvent(this.getId(), this.getCustomerId(), this.getOrderId(), this.getPaidAt()));
    }

    public void cancel(String cancelReason) {
        if (isCanceled()) {
            throw new DomainException(String.format("Invoice %s is already canceled", this.getId()));
        }
        setCancelReason(cancelReason);
        setCanceledAt(OffsetDateTime.now());
        setStatus(InvoiceStatus.CANCELED);
        setExpiresAt(null);
        registerEvent(new InvoiceCanceledEvent(this.getId(), this.getCustomerId(), this.getOrderId(), this.getCanceledAt()));
    }

    public void assignPaymentGatewayCode(String code) {
        if (!isUnpaid()) {
            throw new DomainException(String.format("Invoice %s with status %s cannot be edited",
                    this.getId(), this.getStatus().toString().toLowerCase()));
        }
        this.getPaymentSettings().assignGatewayCode(code);
    }

    public void changePaymentSettings(PaymentMethod method, UUID creditCardId) {
        if (!isUnpaid()) {
            throw new DomainException(String.format("Invoice %s with status %s cannot be edited",
                    this.getId(), this.getStatus().toString().toLowerCase()));
        }
        PaymentSettings paymentSettings = PaymentSettings.brandNew(method, creditCardId);
        paymentSettings.setInvoice(this);
        this.setPaymentSettings(paymentSettings);
    }

    public void updatePaymentStatus(PaymentStatus status) {
        switch (status) {
            case FAILED -> cancel("Payment failed");
            case REFUNDED -> cancel("Payment refunded");
            case PAID -> markAsPaid();
        }
    }
}
