package com.immfly.msorders;

import com.immfly.msorders.entity.BuyerDetails;
import com.immfly.msorders.entity.Category;
import com.immfly.msorders.entity.Order;
import com.immfly.msorders.entity.Product;
import jakarta.persistence.EntityManager;
import lombok.SneakyThrows;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.client.AutoConfigureMockRestServiceServer;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.core.io.ClassPathResource;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.util.FileCopyUtils;
import java.io.File;
import java.io.FileReader;

@SpringBootTest
@AutoConfigureMockMvc
@AutoConfigureMockRestServiceServer
public abstract class MsOrdersApplicationTests {

	@Autowired
	protected MockMvc mockMvc;

	@Autowired
	protected EntityManager entityManager;

	@Autowired
	protected JdbcTemplate jdbcTemplate;

	@SneakyThrows
	protected String getContentFromFile(String filePath) {
		File file = new ClassPathResource(filePath).getFile();
		return FileCopyUtils.copyToString(new FileReader(file));
	}

	protected void generateCategoryInDatabase(String name) {
		Category category = new Category();
		category.setName(name);

		entityManager.persist(category);
		entityManager.flush();
	}

	protected void generateProductInDatabase(String name) {
		Product product = new Product();
		product.setName(name);
		product.setStock(10);

		entityManager.persist(product);
		entityManager.flush();
	}

	protected Long generateOrderInDatabase() {
		Order order = new Order();

		BuyerDetails buyerDetails = new BuyerDetails();
		buyerDetails.setSeatLetter("A");
		buyerDetails.setSeatNumber("1");

		order.setBuyerDetails(buyerDetails);

		entityManager.persist(order);
		entityManager.flush();

		return order.getId();
	}
}
