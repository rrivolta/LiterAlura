package com.aluracursos.catalogo_literatura3;

import com.aluracursos.catalogo_literatura3.principal.Principal;
import com.aluracursos.catalogo_literatura3.repository.AutorRepository;
import com.aluracursos.catalogo_literatura3.repository.LibroRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
public class CatalogoLiteratura3Application implements CommandLineRunner {

	@Autowired
	private LibroRepository libroRepository;

	@Autowired
	AutorRepository autorRepository;

	public static void main(String[] args) {

		SpringApplication.run(CatalogoLiteratura3Application.class, args);
	}

	@Override
	public void run(String... args) throws Exception {

		Principal principal = new Principal (autorRepository, libroRepository);
		principal.ejecutarBuscador();
	}
}

