package contracts.shoppingCart

import org.springframework.cloud.contract.spec.Contract

Contract.make {
    request {
        method GET()
        urlPath("/api/v1/shopping-carts/ad265aa3-c77d-46e9-9782-b70c487c1e17/items")
    }
    response {
        status 200
        headers {
            contentType('application/json')
        }
        body([
                items: [
                        [
                                id: anyUuid(),
                                productId: anyUuid(),
                                name: "Notebook",
                                price: 500.00,
                                quantity: 2,
                                totalAmount: 1000.00,
                                available: anyBoolean()
                        ],
                        [
                                id: anyUuid(),
                                productId: anyUuid(),
                                name: "Mouse pad",
                                price: 250.00,
                                quantity: 1,
                                totalAmount: 250.00,
                                available: anyBoolean()
                        ]
                ]
        ])
    }
}