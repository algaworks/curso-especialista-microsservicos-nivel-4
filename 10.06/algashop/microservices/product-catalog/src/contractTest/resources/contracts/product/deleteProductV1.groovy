package contracts.product

import org.springframework.cloud.contract.spec.Contract

Contract.make {
    request {
        method DELETE()
        urlPath("/api/v1/products/fffe6ec2-7103-48b3-8e4f-3b58e43fb75a/enable")
    }
    response {
        status 204
    }
}