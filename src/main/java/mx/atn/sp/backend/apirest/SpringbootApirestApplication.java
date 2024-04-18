package mx.atn.sp.backend.apirest;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.ComponentScan;

@SpringBootApplication
@ComponentScan(basePackages= {"mx.atn.sp.backend.apirest"})
public class SpringbootApirestApplication {

	public static void main(String[] args) {
		SpringApplication.run(SpringbootApirestApplication.class, args);
	}

}
