package com.algaworks.algashop.product.catalog.contract.base;

import com.algaworks.algashop.product.catalog.application.PageModel;
import com.algaworks.algashop.product.catalog.application.category.management.CategoryInput;
import com.algaworks.algashop.product.catalog.application.category.management.CategoryManagementService;
import com.algaworks.algashop.product.catalog.application.category.query.CategoryDetailOutput;
import com.algaworks.algashop.product.catalog.application.category.query.CategoryOutputTestDataBuilder;
import com.algaworks.algashop.product.catalog.application.category.query.CategoryQueryService;
import com.algaworks.algashop.product.catalog.presentation.CategoryController;
import io.restassured.module.mockmvc.RestAssuredMockMvc;
import org.junit.jupiter.api.BeforeEach;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.web.context.WebApplicationContext;

import java.nio.charset.StandardCharsets;
import java.util.List;
import java.util.UUID;

@WebMvcTest(controllers = CategoryController.class)
public class CategoryBase {

    @Autowired
    private WebApplicationContext context;

    @MockitoBean
    private CategoryQueryService categoryQueryService;

    @MockitoBean
    private CategoryManagementService categoryManagementService;

    public static final UUID validCategoryId = UUID.fromString("f5ab7a1e-37da-41e1-892b-a1d38275c2f2");

    public static final UUID createdCategoryId = UUID.randomUUID();

    @BeforeEach
    void setUp() {
        RestAssuredMockMvc.mockMvc(MockMvcBuilders.webAppContextSetup(context)
                .defaultResponseCharacterEncoding(StandardCharsets.UTF_8).build());

        RestAssuredMockMvc.enableLoggingOfRequestAndResponseIfValidationFails();

        Mockito.when(categoryQueryService.filter(Mockito.anyInt(), Mockito.anyInt()))
                .then((answer)-> {
                    Integer size = answer.getArgument(0);
                    return PageModel.<CategoryDetailOutput>builder()
                            .number(0)
                            .size(size)
                            .totalPages(1)
                            .totalElements(2)
                            .content(
                                    List.of(
                                            CategoryOutputTestDataBuilder.aCategory().build(),
                                            CategoryOutputTestDataBuilder.aDisabledCategory().build()
                                    )
                            ).build();
                });

        Mockito.when(categoryQueryService.findById(validCategoryId))
                .thenReturn(CategoryOutputTestDataBuilder.aCategory().id(validCategoryId).build());

        Mockito.when(categoryManagementService.create(Mockito.any(CategoryInput.class)))
                .thenReturn(createdCategoryId);

        Mockito.when(categoryQueryService.findById(createdCategoryId))
                .thenReturn(CategoryOutputTestDataBuilder.aCategory().id(createdCategoryId).build());
    }
}
