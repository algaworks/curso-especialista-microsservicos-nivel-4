package contracts.order

import org.springframework.cloud.contract.spec.Contract

Contract.make {
    request {
        method GET()
        headers {
            accept "application/json"
        }
        url("/api/v1/orders/01226N0693HDH")
    }
    response {
        status 404
        body([
            instance: fromRequest().path(),
            type: "/errors/not-found",
            title: "Not found"
        ])
    }
}