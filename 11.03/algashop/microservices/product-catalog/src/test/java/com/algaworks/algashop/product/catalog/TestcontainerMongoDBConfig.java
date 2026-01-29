package com.algaworks.algashop.product.catalog;

import org.springframework.boot.test.context.TestConfiguration;
import org.springframework.boot.testcontainers.service.connection.ServiceConnection;
import org.springframework.context.annotation.Bean;
import org.testcontainers.mongodb.MongoDBContainer;

@TestConfiguration
public class TestcontainerMongoDBConfig {

    private static final MongoDBContainer mongoDBContainer
            = new MongoDBContainer("mongo:8")
                .withCommand("--replSet", "rs0");

    static {
        mongoDBContainer.start();

        try {
            mongoDBContainer.execInContainer(
                    "mongosh",
                    "--eval",
                    """
                    rs.initiate({
                        _id: "rs0",
                        members: [
                            {
                                _id: 0,
                                host: "localhost:27017"
                            }
                        ]
                    })
                    """
            );
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    @Bean
    @ServiceConnection
    public MongoDBContainer mongoDBContainer() {
        return mongoDBContainer;
    }

}
