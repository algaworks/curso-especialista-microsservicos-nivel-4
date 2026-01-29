package contracts.product

import org.springframework.cloud.contract.spec.Contract

Contract.make {
    request {
        method GET()
        headers {
            accept "application/json"
        }
        url("/api/v1/products") {
            queryParameters {
                parameter('size', value(stub(optional(anyNumber())), test(10)))
                parameter('number', value(stub(optional(anyNumber())), test(0)))
            }
        }
        response {
            status 200
            headers {
                contentType "application/json"
            }
            body([
                    size: fromRequest().query("size"),
                    number: 0,
                    totalElements: 2,
                    totalPages: 1,
                    content: [
                        [
                            id: anyUuid(),
                            addedAt: anyIso8601WithOffset(),
                            name: "Notebook X11",
                            brand: "Deep Diver",
                            regularPrice: 1500.00,
                            salePrice: 1000.00,
                            inStock: true,
                            enabled: true,
                            category: [
                                    id: anyUuid(),
                                    name: "Notebook"
                            ],
                            description: "A Gamer Notebook"
                        ],
                        [
                            id: anyUuid(),
                            addedAt: anyIso8601WithOffset(),
                            name: "Desktop I9000",
                            brand: "Deep Diver",
                            regularPrice: 3500.00,
                            salePrice: 3000.00,
                            inStock: false,
                            enabled: true,
                            category: [
                                    id: anyUuid(),
                                    name: "Desktop"
                            ],
                            description: "A Gamer Desktop"
                        ]
                    ]
            ])
        }
    }
}