package com.harsh.sentinal.scan;

import com.harsh.sentinal.scan.integration.virustotal.VirusTotalProperties;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import javax.sql.DataSource;


@SpringBootApplication
public class ScanApplication {

	@Bean
	CommandLineRunner checkDataSource(DataSource dataSource) {
		return args -> {
			System.out.println("Datasource URL: "
					+ dataSource.getConnection().getMetaData().getURL());
		};
	}

	public static void main(String[] args) {
		SpringApplication.run(ScanApplication.class, args);
	}

}
