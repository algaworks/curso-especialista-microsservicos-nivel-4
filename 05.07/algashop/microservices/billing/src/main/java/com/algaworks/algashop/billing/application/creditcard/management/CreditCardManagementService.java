package com.algaworks.algashop.billing.application.creditcard.management;

import com.algaworks.algashop.billing.domain.model.creditcard.*;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.UUID;

@Service
@RequiredArgsConstructor
public class CreditCardManagementService {

    private final CreditCardRepository creditCardRepository;
    private final CreditCardProviderService creditCardProviderService;

    @Transactional
    public UUID register(TokenizedCreditCardInput input) {
        LimitedCreditCard limitedCreditCard = creditCardProviderService.register(input.getCustomerId(), input.getTokenizedCard());

        CreditCard creditCard = CreditCard.brandNew(
                input.getCustomerId(),
                limitedCreditCard.getLastNumbers(),
                limitedCreditCard.getBrand(),
                limitedCreditCard.getExpMonth(),
                limitedCreditCard.getExpYear(),
                limitedCreditCard.getGatewayCode()
        );

        creditCardRepository.saveAndFlush(creditCard);

        return creditCard.getId();
    }

    @Transactional
    public void delete(UUID customerId, UUID creditCardId) {
        CreditCard creditCard = creditCardRepository.findByCustomerIdAndId(customerId, creditCardId)
                .orElseThrow(() -> new CreditCardNotFoundException());
        creditCardRepository.delete(creditCard);
        creditCardProviderService.delete(creditCard.getGatewayCode());
    }

}
