package com.iplat.master;

import com.iplat.master.file.ReadExcel;
import com.iplat.master.file.VelocityWriter;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;

import java.io.IOException;

@SpringBootApplication
public class SampleApplication {

	public static void main(String[] args) {
		SpringApplication.run(SampleApplication.class, args);
	}

	@Bean
	public CommandLineRunner commandLineRunner() throws IOException {

		return runner -> {
			ReadExcel readExcel = new ReadExcel();
			VelocityWriter velocityWriter = new VelocityWriter(readExcel);
			velocityWriter.velocityReadFileGenEntity();
			velocityWriter.velocityReadFileGenMapper();
		};
	}
}
