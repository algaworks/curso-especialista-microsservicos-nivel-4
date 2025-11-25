package contracts.order

import org.springframework.cloud.contract.spec.Contract

Contract.make {
    request {
        method GET()
        headers {
            accept 'application/json'
        }
        url("/api/v1/orders/01226N0640J7Q")
    }
    response {
        status 200
        body([
            id: "01226N0640J7Q",
            customer: [
                    id: anyUuid(),
                    firstName: "John",
                    lastName: "Doe",
                    document: "12345",
                    email: "johndoe@email.com",
                    phone: "1191234564"
            ],
            totalItems: 2,
            totalAmount: 41.98,
            placedAt: anyIso8601WithOffset(),
            canceledAt: null,
            paidAt: null,
            readyAt: null,
            status: "PLACED",
            paymentMethod: "GATEWAY_BALANCE",
            shipping: [
                    cost: 20.5,
                    expectedDate: anyDate(),
                    recipient: [
                            firstName: "John",
                            lastName: "Doe",
                            document: "12345",
                            phone: "5511912341234"
                    ],
                    address: [
                            street: "Bourbon Street",
                            number: "2000",
                            complement: "apt 122",
                            neighborhood: "North Ville",
                            city: "Yostfort",
                            state: "South Carolina",
                            zipCode: "12321"
                    ]
            ],
            billing: [
                    firstName: "John",
                    lastName: "Doe",
                    document: "12345",
                    phone: "5511912341234",
                    address: [
                            street: "Bourbon Street",
                            number: "2000",
                            complement: "apt 122",
                            neighborhood: "North Ville",
                            city: "Yostfort",
                            state: "South Carolina",
                            zipCode: "12321"
                    ]
            ],
            items: [
                    [
                            id: anyNonBlankString(),
                            productId: anyUuid(),
                            orderId: "01226N0640J7Q",
                            price: 19.99,
                            productName: "Notebook Dive Gamer X11",
                            quantity: 2,
                            totalAmount: 41.98
                    ]
            ]
        ])
    }
}