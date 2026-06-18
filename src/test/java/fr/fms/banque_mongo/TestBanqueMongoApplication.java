package fr.fms.banque_mongo;

import org.springframework.boot.SpringApplication;

public class TestBanqueMongoApplication {

	public static void main(String[] args) {
		SpringApplication.from(BanqueMongoApplication::main).with(TestcontainersConfiguration.class).run(args);
	}

}
