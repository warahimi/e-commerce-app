package com.cwc.e_commerce_app;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;

@SpringBootTest
class ECommerceAppApplicationTests {

	@Test
	void contextLoads() {
	}
	@Test
	public void sampleTest() {
	    // Sample test case
		Assertions.assertEquals(2, 1 + 1);
	}

}
