package com.iprody.clients;

import org.springframework.boot.SpringApplication;

public class TestClientsApplication {

	public static void main(String[] args) {
		SpringApplication.from(ClientsApplication::main).with(TestcontainersConfiguration.class).run(args);
	}

}
