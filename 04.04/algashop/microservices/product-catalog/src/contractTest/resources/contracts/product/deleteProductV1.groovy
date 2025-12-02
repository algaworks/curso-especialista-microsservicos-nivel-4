package contracts.product

import org.springframework.cloud.contract.spec.Contract

Contract.make {
    request {
        method DELETE()
        headers {
            accept 'application/json'
        }
        url("/api/v1/products/fffe6ec2-7103-48b3-8e4f-3b58e43fb75a")
    }
    response {
        status 204
    }
}