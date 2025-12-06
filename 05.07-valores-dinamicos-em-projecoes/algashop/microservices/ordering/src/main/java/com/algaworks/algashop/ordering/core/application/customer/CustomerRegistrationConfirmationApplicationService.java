package com.algaworks.algashop.ordering.core.application.customer;

import com.algaworks.algashop.ordering.core.ports.in.customer.CustomerOutput;
import com.algaworks.algashop.ordering.core.ports.in.customer.ForConfirmCustomerRegistration;
import com.algaworks.algashop.ordering.core.ports.out.customer.ForNotifyingCustomers;
import com.algaworks.algashop.ordering.core.ports.out.customer.ForObtainingCustomers;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.UUID;

@Service
@RequiredArgsConstructor
public class CustomerRegistrationConfirmationApplicationService implements ForConfirmCustomerRegistration {

    private final ForNotifyingCustomers forNotifyingCustomers;
    private final ForObtainingCustomers forObtainingCustomers;

    public void confirm(UUID customerId) {
        CustomerOutput customerOutput = forObtainingCustomers.findById(customerId);
        var input = new ForNotifyingCustomers.NotifyNewRegistrationInput(
                customerOutput.getId(),
                customerOutput.getFirstName(),
                customerOutput.getEmail()
        );
        forNotifyingCustomers.notifyNewRegistration(input);
    }
}
