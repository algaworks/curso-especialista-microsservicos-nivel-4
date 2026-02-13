package com.algaworks.algashop.billing.presentation;

import com.algaworks.algashop.billing.application.invoice.management.GenerateInvoiceInput;
import com.algaworks.algashop.billing.application.invoice.management.InvoiceManagementApplicationService;
import com.algaworks.algashop.billing.application.invoice.query.InvoiceOutput;
import com.algaworks.algashop.billing.application.invoice.query.InvoiceQueryService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

import java.util.UUID;

@RestController
@RequestMapping("/api/v1/orders/{orderId}/invoice")
@RequiredArgsConstructor
@Slf4j
public class InvoiceController {

    private final InvoiceQueryService invoiceQueryService;
    private final InvoiceManagementApplicationService invoiceManagementApplicationService;

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public InvoiceOutput generate(@PathVariable String orderId, @Valid @RequestBody GenerateInvoiceInput input) {
        input.setOrderId(orderId);
        UUID invoiceId = invoiceManagementApplicationService.generate(input);
        try {
            invoiceManagementApplicationService.processPayment(invoiceId);
        } catch (Exception e) {
            log.error(String.format("Error when processing payment for invoice %s", invoiceId), e);
        }
        return invoiceQueryService.findByOrderId(orderId);
    }

    @GetMapping
    public InvoiceOutput findByOrder(@PathVariable String orderId) {
        return invoiceQueryService.findByOrderId(orderId);
    }

}
