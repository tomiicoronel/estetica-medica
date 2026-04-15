package com.estetica.estetica;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

/**
 * Clase principal de la aplicación Estética Médica.
 *
 * <p>{@code @SpringBootApplication} es una anotación compuesta que combina:</p>
 * <ul>
 *     <li>{@code @Configuration} — Marca la clase como fuente de definiciones de beans.</li>
 *     <li>{@code @EnableAutoConfiguration} — Habilita la configuración automática de Spring Boot
 *         según las dependencias del classpath (JPA, Web, Validation, etc.).</li>
 *     <li>{@code @ComponentScan} — Escanea automáticamente todos los paquetes a partir de
 *         {@code com.estetica.estetica} buscando componentes (@Service, @Repository, @Controller, etc.).</li>
 * </ul>
 *
 * @author estetica
 * @version 1.0
 * @since 2026-04-14
 */
@SpringBootApplication
public class EsteticaApplication {

	/**
	 * Punto de entrada de la aplicación.
	 *
	 * <p>Inicializa el contexto de Spring, levanta el servidor embebido (Tomcat)
	 * y registra todos los beans detectados por el component scan.</p>
	 *
	 * @param args argumentos de línea de comandos pasados al iniciar la aplicación
	 */
	public static void main(String[] args) {
		SpringApplication.run(EsteticaApplication.class, args);
	}

}
