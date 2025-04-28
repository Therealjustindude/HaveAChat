package com.jdavies.haveachat_java_backend;

import org.springframework.boot.SpringApplication;

public class TestHaveachatJavaBackendApplication {

	public static void main(String[] args) {
		SpringApplication.from(HaveachatJavaBackendApplication::main).with(TestcontainersConfiguration.class).run(args);
	}

}
