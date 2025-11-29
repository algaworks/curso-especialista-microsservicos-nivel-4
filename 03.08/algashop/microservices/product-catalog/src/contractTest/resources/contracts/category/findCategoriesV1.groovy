package contracts.category

import org.springframework.cloud.contract.spec.Contract

Contract.make {
    request {
        method GET()
        urlPath("/api/v1/categories") {
            queryParameters {
                parameter("page", "0")
                parameter("size", "2")
            }
        }
    }
    response {
        status 200
        headers {
            contentType('application/json')
        }
        body([
                number: 0,
                size: 2,
                totalPages: 1,
                totalElements: 2,
                content: [
                        [
                            id: anyUuid(),
                            name: "Electronics",
                            enabled: true
                        ],
                        [
                            id: anyUuid(),
                            name: "Books",
                            enabled: false
                        ]
                ]
        ])
    }
}