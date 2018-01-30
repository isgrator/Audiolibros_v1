import android.app.Application;

public class Aplicacion extends Application {
    private List<Libro> listaLibros;
    private AdaptadorLibros adaptador;

    @Override
    public void onCreate() {
        listaLibros = Libro.ejemploLibros();
        adaptador = new AdaptadorLibros (this, listaLibros);
    }

    public AdaptadorLibros getAdaptador() {
        return adaptador;
    }

    public List<Libro> getListaLibros() {
        return listaLibros;
    }
} 