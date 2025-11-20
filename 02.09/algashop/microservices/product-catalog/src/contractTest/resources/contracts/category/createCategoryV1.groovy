package contracts.category

import org.springframework.cloud.contract.spec.Contract

Contract.make {
    request {
        method POST()
        url "/api/v1/categories"
        headers {
            contentType("application/json")
        }
        body([
                name: value(
                        test("Electronics"),
                        stub(nonBlank())
                ),
                enabled: value(
                        test(true),
                        stub(anyBoolean())
                )
        ])
    }
    response {
        status 201
        headers {
            contentType("application/json")
        }
        body([
                id: anyUuid(),
                name: "Electronics",
                enabled: true
        ])
    }
}