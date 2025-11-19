package contracts.shoppingCart

import org.springframework.cloud.contract.spec.Contract

Contract.make {
    request {
        method DELETE()
        urlPath("/api/v1/shopping-carts/ad265aa3-c77d-46e9-9782-b70c487c1e17")
    }
    response {
        status 204
    }
}