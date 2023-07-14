package com.mapbefine.mapbefine;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.data.jpa.repository.config.EnableJpaAuditing;

@SpringBootApplication
@EnableJpaAuditing
public class MapbefineApplication {

	public static void main(String[] args) {
		SpringApplication.run(MapbefineApplication.class, args);
	}

}
