package com.aluracursos.catalogo_literatura3.model;

import jakarta.persistence.*;

@Entity
@Table(name = "libros")
public class Libro {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private String titulo;
@ManyToOne
    private Autor autor;
    private String idiomas;

    public Libro() {
    }

    public Libro(DatosLibros datosLibros, Autor autor) {
        this.titulo = datosLibros.titulo();
        this.idiomas = datosLibros.idiomas().get(0);
        this.autor = autor;
    }

    @Override
    public String toString() {
        return "******************LIBRO*****************" + "\n" +
                " Titulo: " + titulo + "\n" +
                " Nombre del autor: " + autor.getNombre() + "\n" +
                " Idioma: " + idiomas + "\n";
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getTitulo() {
        return titulo;
    }

    public void setTitulo(String titulo) {
        this.titulo = titulo;
    }

    public Autor getAutor() {
        return autor;
    }

    public void setAutor(Autor autor) {
        this.autor = autor;
    }

    public String getIdiomas() {
        return idiomas;
    }

    public void setIdiomas(String idiomas) {
        this.idiomas = idiomas;
    }
}
