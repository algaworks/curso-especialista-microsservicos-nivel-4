package com.algaworks.algashop.ordering.core.domain.model;

import com.algaworks.algashop.ordering.utils.TestcontainerPostgreSQLConfig;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.context.annotation.Import;

@DataJpaTest
@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.NONE)
@Import(TestcontainerPostgreSQLConfig.class)
public class AbstractRepositoryIT {
}
