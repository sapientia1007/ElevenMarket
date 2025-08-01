package com.wid.elevenmarket.product;

import com.wid.elevenmarket.model.Product;
import com.wid.elevenmarket.model.Users;
import com.wid.elevenmarket.persistence.ProductRepository;
import com.wid.elevenmarket.persistence.UsersRepository;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.annotation.DirtiesContext;
import org.springframework.test.context.ActiveProfiles;

import java.util.ArrayList;

@SpringBootTest
@ActiveProfiles("test")
@DirtiesContext(classMode = DirtiesContext.ClassMode.AFTER_EACH_TEST_METHOD)
public class ProductServiceTest {
    @Autowired
    private UsersRepository usersRepository;
    @Autowired
    private ProductRepository productRepository;

    // 상품 등록
    @Test
    void testSaveProduct() {
        Users user = new Users(null, "user", "user@example.com", "1234");
        usersRepository.save(user);

        for (int i=0; i<10; i++) {
            Product productToSave = new Product(null, "productName" + i+i*3, "description" + i*2, 1000L, null, false, new ArrayList<>(), user, 10, null);
            productRepository.save(productToSave);
        }
    }
}
