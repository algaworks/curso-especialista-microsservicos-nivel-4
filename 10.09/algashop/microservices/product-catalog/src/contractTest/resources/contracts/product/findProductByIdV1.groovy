package contracts.product

import org.springframework.cloud.contract.spec.Contract

Contract.make {
    request {
        method GET()
        headers {
            accept 'application/json'
        }
        url("/api/v1/products/fffe6ec2-7103-48b3-8e4f-3b58e43fb75a")
    }
    response {
        status 200
        headers {
            contentType 'application/json'
        }
        body([
                id: fromRequest().path(3),
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
        ])
    }
}