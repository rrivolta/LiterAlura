package com.aluracursos.catalogo_literatura3.service;

public interface IConvierteDatos {
    <T> T obtenerDatos(String json, Class<T> clase);
}
