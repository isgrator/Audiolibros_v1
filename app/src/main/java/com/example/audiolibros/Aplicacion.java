package com.example.audiolibros;

import android.app.Application;

import java.util.List;

public class Aplicacion extends Application {
    private List<Libro> listaLibros;
    private AdaptadorLibrosFiltro adaptador;

    @Override
    public void onCreate() {
        super.onCreate();
        listaLibros = Libro.ejemploLibros();
        adaptador = new AdaptadorLibrosFiltro(this, listaLibros);
    }

    public AdaptadorLibrosFiltro getAdaptador() {
        return adaptador;
    }

    public List<Libro> getListaLibros() {
        return listaLibros;
    }
} 