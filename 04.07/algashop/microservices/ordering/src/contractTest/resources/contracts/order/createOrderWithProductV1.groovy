package contracts.order

import org.springframework.cloud.contract.spec.Contract

Contract.make {
    request {
        method POST()
        url "/api/v1/orders"
        headers {
            contentType("application/vnd.order-with-product.v1+json")
        }
        body([
                productId: value(test(anyUuid()), stub(anyUuid())),
                customerId: value(test(anyUuid()), stub(anyUuid())),
                quantity: value(test(1), stub(anyPositiveInt())),
                paymentMethod: "GATEWAY_BALANCE",
                shipping: [
                        recipient: [
                                firstName: "John",
                                lastName: "Doe",
                                document: "255-08-0578",
                                phone: "478-256-2604"
                        ],
                        address: [
                                street: "Elm Street",
                                number: "456",
                                complement: "House A",
                                neighborhood: "Central Park",
                                city: "Springfield",
                                state: "Illinois",
                                zipCode: "62704"
                        ]
                ],
                billing: [
                        firstName: "Matt",
                        lastName: "Damon",
                        phone: "123-321-1112",
                        document: "123-45-6789",
                        email: "matt.damon@email.com",
                        address: [
                                street: "Amphitheatre Parkway",
                                number: "1600",
                                complement: "",
                                neighborhood: "Mountain View",
                                city: "Mountain View",
                                state: "California",
                                zipCode: "94043"
                        ]
                ]
        ])
    }
    response {
        status 201
        headers {
            contentType('application/json')
        }
        body([
            id: anyNonBlankString(),
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
                            orderId: anyNonBlankString(),
                            productId: anyUuid(),
                            productName: "Notebook Dive Gamer X11",
                            price: 19.99,
                            quantity: 2,
                            totalAmount: 41.98
                    ]
            ]
        ])
    }
}
