package com.algaworks.algashop.billing.infrastructure;

import com.algaworks.algashop.billing.domain.model.creditcard.LimitedCreditCard;
import com.algaworks.algashop.billing.infrastructure.creditcard.fastpay.*;
import com.algaworks.algashop.billing.utils.TestcontainerPostgreSQLConfig;
import com.github.tomakehurst.wiremock.WireMockServer;
import com.github.tomakehurst.wiremock.common.ClasspathFileSource;
import com.github.tomakehurst.wiremock.extension.responsetemplating.ResponseTemplateTransformer;
import com.github.tomakehurst.wiremock.extension.responsetemplating.TemplateEngine;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Import;

import java.time.Year;
import java.util.Collections;
import java.util.UUID;

import static com.github.tomakehurst.wiremock.core.WireMockConfiguration.options;

@Import({FastpayCreditCardTokenizationAPIClientConfig.class, TestcontainerPostgreSQLConfig.class})
public abstract class AbstractFastpayIT {

    @Autowired
    protected CreditCardProviderServiceFastpayImpl creditCardProvider;

    @Autowired
    protected FastpayCreditCardTokenizationAPIClient tokenizationAPIClient;

    protected static final UUID validCustomerId = UUID.randomUUID();
    protected static final String alwaysPaidCardNumber = "4622943127011022";

    protected static WireMockServer wiremockFastpay;

    public static void startMock() {
        wiremockFastpay = new WireMockServer(options()
                .port(8788)
                .usingFilesUnderDirectory("src/test/resources/wiremock/fastpay")
                .extensions(new ResponseTemplateTransformer(
                       TemplateEngine.defaultTemplateEngine(),
                        true,
                        new ClasspathFileSource("src/test/resources/wiremock/fastpay"),
                        Collections.emptyList()
                ))
        );
        wiremockFastpay.start();
    }

    public static void stopMock() {
        wiremockFastpay.stop();
    }

    protected LimitedCreditCard registerCard() {
        FastpayTokenizationInput input = FastpayTokenizationInput.builder()
                .number(alwaysPaidCardNumber)
                .cvv("222")
                .expMonth(1)
                .holderName("John Doe")
                .holderDocument("12345")
                .expYear(Year.now().getValue() + 5)
                .build();

        FastpayTokenizedCreditCardModel response = tokenizationAPIClient.tokenize(input);
        return creditCardProvider.register(validCustomerId, response.getTokenizedCard());
    }
}
