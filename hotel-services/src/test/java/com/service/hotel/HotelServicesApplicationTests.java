package com.service.hotel;

import com.service.hotel.controller.HotelRestController;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

@SpringBootTest
class HotelServicesApplicationTests {

	@Autowired
	HotelRestController hotelRestController;
static int r;
	@Test
	void contextLoads()
	{
		Assertions.assertThat(hotelRestController).isNotNull();
	}


}
