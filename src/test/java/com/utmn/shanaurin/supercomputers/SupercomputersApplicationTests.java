package com.utmn.shanaurin.supercomputers;

import com.utmn.shanaurin.supercomputers.service.SupercomputerService;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;

import static org.assertj.core.api.Assertions.assertThat;

class SupercomputersApplicationTests extends BaseIntegrationTest {

	@Autowired
	private SupercomputerService supercomputerService;

	@Test
	void contextLoads() {
		assertThat(supercomputerService).isNotNull();
	}

	@Test
	void dataLoadedFromCsv() {
		long count = supercomputerService.count();
		assertThat(count).isGreaterThan(0);
	}

	@Test
	void avgPerformanceCalculated() {
		Double avgPerformance = supercomputerService.avgPerformance();
		assertThat(avgPerformance).isNotNull().isGreaterThan(0);
	}
}
