package com.immfly.msorders;

import com.immfly.msorders.entity.Category;
import com.immfly.msorders.entity.Product;
import jakarta.persistence.EntityManager;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.client.AutoConfigureMockRestServiceServer;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.web.servlet.MockMvc;

@SpringBootTest
@AutoConfigureMockMvc
@AutoConfigureMockRestServiceServer
public abstract class MsOrdersApplicationTests {

	@Autowired
	protected MockMvc mockMvc;

	@Autowired
	protected EntityManager entityManager;

	protected void generateCategoryInDatabase(String name) {
		Category category = new Category();
		category.setName(name);

		entityManager.persist(category);
		entityManager.flush();
	}

	protected void generateProductInDatabase(String name) {
		Product product = new Product();
		product.setName(name);

		entityManager.persist(product);
		entityManager.flush();
	}
}
