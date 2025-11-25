package com.algaworks.algashop.ordering.infrastructure.adapters.out.notification.customer;

import com.algaworks.algashop.ordering.core.ports.out.customer.ForNotifyingCustomers;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

@Service
@Slf4j
public class ForNotifyingCustomersFakeImpl implements ForNotifyingCustomers {

    @Override
    public void notifyNewRegistration(NotifyNewRegistrationInput input) {
        log.info("Welcome {}", input.firstName());
        log.info("User your email to access your account {}", input.email());
    }
}
