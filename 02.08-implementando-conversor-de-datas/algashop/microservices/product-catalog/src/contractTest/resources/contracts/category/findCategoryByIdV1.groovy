package contracts.category

import org.springframework.cloud.contract.spec.Contract

Contract.make {
    request {
        method GET()
        urlPath("/api/v1/categories/f5ab7a1e-37da-41e1-892b-a1d38275c2f2")
    }
    response {
        status 200
        headers {
            contentType('application/json')
        }
        body([
                id: fromRequest().path(3),
                name: "Electronics",
                enabled: true
        ])
    }
}