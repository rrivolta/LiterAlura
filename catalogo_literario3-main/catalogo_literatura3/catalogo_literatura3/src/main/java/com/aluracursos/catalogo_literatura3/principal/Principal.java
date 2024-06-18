package com.aluracursos.catalogo_literatura3.principal;

import com.aluracursos.catalogo_literatura3.model.*;
import com.aluracursos.catalogo_literatura3.repository.AutorRepository;
import com.aluracursos.catalogo_literatura3.repository.LibroRepository;
import com.aluracursos.catalogo_literatura3.service.ConsumoAPI;
import com.aluracursos.catalogo_literatura3.service.ConvierteDatos;

import java.util.List;
import java.util.Scanner;

public class Principal {

    private static final String URL_BASE = "https://gutendex.com/books/";
    private ConsumoAPI consumoAPI = new ConsumoAPI();
    private ConvierteDatos conversor = new ConvierteDatos();
    private Scanner teclado = new Scanner(System.in);
    private List<Autor> autores;
    private List<Libro> libros;
    private LibroRepository libroRepository;
    private AutorRepository autorRepository;

    public Principal(AutorRepository autorRepository, LibroRepository libroRepository) {
        this.autorRepository = autorRepository;
        this.libroRepository = libroRepository;
    }

    public void muestraElMenu() {

        System.out.println("""
                *****************************************************************************
                                     BIENVENIDO QUERIDO LECTOR
                Al catálogo literario, donde podrás encontrar tus obras literarias favoritas.
                -----------------------------------------------------------------------------
                        1- Buscar libro por título
                        2- Lista de libros registrados
                        3- Lista de autores registrados
                        4- Listado de autores vivos en un determinado año
                        5- Listado de libros por idioma
                        6- Salir                       
                -----------------------------------------------------------------------------
                Escribe la opción que deseas realizar:
                               
                 """);
    }

    public void ejecutarBuscador() {
        int opcion;
        do {
            muestraElMenu();
            if (teclado.hasNextInt()) {
                opcion = teclado.nextInt();
                teclado.nextLine();
                switch (opcion) {
                    case 1:
                        buscarLibroPorTitulo();
                        break;

                    case 2:
                        ListaDeLibrosRegistrados();
                        break;

                    case 3:
                        ListaAutoresRegistrados();
                        break;

                    case 4:
                        listaDeAutoresVivosEnDeterminadoAno();
                        break;

                    case 5:
                        listaDeLibrosPorIdioma();
                        break;

                    case 6:
                        System.out.println("Gracias por usar nuestros servicios");
                        break;
                    default:
                        System.out.println("Opción no válida. Por favor, seleccione una opción del 1 al 6");
                }
            } else {
                System.out.println("Opción no válida. Por favor, ingrese un numero del 1 al 6");
                teclado.nextLine();
                opcion = 0;
            }
        } while (opcion != 6);
    }

    private Datos buscarDatosLibros() {
        System.out.println("ingrese el titulo que desea buscar");
        var libro = teclado.nextLine();
        var json = consumoAPI.obtenerDatos(URL_BASE + "?search=" + libro.replace(" ", "+"));
        System.out.println(json);
        Datos datos = conversor.obtenerDatos(json, Datos.class);
        return datos;

    }

    private Libro crearLibro(DatosLibros datosLibros, Autor autor) {
        Libro libro = new Libro(datosLibros, autor);
        return libroRepository.save(libro);

    }

    //Opciones del menu

    private void buscarLibroPorTitulo() {
        Datos datos = buscarDatosLibros();

        if (!datos.resultados().isEmpty()) {
            DatosLibros datosLibros = datos.resultados().get(0);
            DatosAutor datosAutor = datosLibros.autor().get(0);
            Libro libroBusqueda = libroRepository.findByTituloIgnoreCase(datosLibros.titulo());

            if (libroBusqueda != null) {
                System.out.println(libroBusqueda);
            } else {
                Autor autorBusqueda = autorRepository.findByNombreIgnoreCase(datosAutor.nombre());
                if (autorBusqueda == null) {
                    Autor autor = new Autor(datosAutor);
                    autorRepository.save(autor);
                    Libro libro = crearLibro(datosLibros, autor);
                    System.out.println(libro);
                } else {
                    Libro libro = crearLibro(datosLibros, autorBusqueda);
                    System.out.println(libro);
                }
            }
        }
    }

    private void ListaDeLibrosRegistrados() {
        libros = libroRepository.findAll();

        libros.stream()
                .forEach(System.out::println);
    }

    private void ListaAutoresRegistrados() {
        autores = autorRepository.findAll();

        autores.stream()
                .forEach(System.out::println);

    }

    private void listaDeAutoresVivosEnDeterminadoAno() {
        System.out.println("Indique a partir de que año desea buscar el autor, en su año vivo:");
        String fecha = teclado.nextLine();
        try {
            List<Autor> autoresVivos = autorRepository.autorVivoEnDeterminadoAno(fecha);

            autoresVivos.stream()
                    .forEach(System.out::println);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }

    }

    private void listaDeLibrosPorIdioma() {
        System.out.println("""
                                
                Lista de libros según el idioma
                            
                    1. Español (ES)
                    2. Ingles (EN)
                    3. Frances (FR)
                    4. Portugues (PT)
                                
                    5. Regresar al menú principal
                    
                Ingrese el idioma para buscar los libros:
                                
                """);

        int opcion;

        if (teclado.hasNextInt()) {
            opcion = teclado.nextInt();
            teclado.nextLine();
            switch (opcion) {

                case 1:
                    List<Libro> libroPorIdioma = libroRepository.findByIdiomasContaining("es");
                    libroPorIdioma.stream()
                            .forEach(System.out::println);
                    break;
                case 2:
                    libros = libroRepository.findByIdiomasContaining("en");
                    libros.stream()
                            .forEach(System.out::println);
                    break;
                case 3:
                    libros = libroRepository.findByIdiomasContaining("fr");
                    libros.stream()
                            .forEach(System.out::println);
                    break;

                case 4:
                    libros = libroRepository.findByIdiomasContaining("pt");
                    libros.stream()
                            .forEach(System.out::println);
                    break;
                case 5:
                    System.out.println("Gracias por usar nuestros servicios");
                    ejecutarBuscador();
                    break;

                default:
                    System.out.println("Opción no válida. Por favor, ingrese un numero del 1 al 5");

            }

        } else {
            System.out.println("Opción no válida. Por favor, ingrese un numero del 1 al 5");
            teclado.nextLine();
            opcion = 0;
        }
    }
}
