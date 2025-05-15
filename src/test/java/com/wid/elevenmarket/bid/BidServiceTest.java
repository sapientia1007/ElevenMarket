package com.wid.elevenmarket.bid;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.test.annotation.DirtiesContext;
import org.springframework.test.context.ActiveProfiles;

@SpringBootTest
@ActiveProfiles("test")
@DirtiesContext(classMode = DirtiesContext.ClassMode.AFTER_EACH_TEST_METHOD)
public class BidServiceTest {
    @Autowired
    private RedisTemplate<String, String> redisTemplate;

    @Test
    void testRedistConnection() {
        redisTemplate.opsForValue().set("testKey", "Hello, Redis!");
        String value = redisTemplate.opsForValue().get("testKey");
        System.out.println("Redis Value: " + value);
    }
}
