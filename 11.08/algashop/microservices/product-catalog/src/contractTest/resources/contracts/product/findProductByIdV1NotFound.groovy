package contracts.product

import org.springframework.cloud.contract.spec.Contract

Contract.make {
    request {
        method GET()
        headers {
            accept "application/json"
        }
        url("/api/v1/products/21651a12-b126-4213-ac21-19f66ff4642e")
    }
    response {
        status 404
        headers {
            contentType "application/problem+json"
        }
        body([
                instance: fromRequest().path(),
                type: "/errors/not-found",
                title: "Not found"
        ])
    }
}