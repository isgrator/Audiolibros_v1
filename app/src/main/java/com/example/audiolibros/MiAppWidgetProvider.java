package com.example.audiolibros;

import android.app.Activity;
import android.app.PendingIntent;
import android.appwidget.AppWidgetManager;
import android.appwidget.AppWidgetProvider;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.net.Uri;
import android.widget.RemoteViews;
import android.widget.Toast;

import com.example.audiolibros.fragments.DetalleFragment;

/**
 * Created by Isabel María on 25/02/2018.
 */

public class MiAppWidgetProvider extends AppWidgetProvider {

    public void onUpdate(Context context, AppWidgetManager appWidgetManager,
                         int[] widgetIds) {
        for (int widgetId: widgetIds) {
            actualizaWidget(context, widgetId);
        }
    }

    public static void actualizaWidget(Context context, int widgetId) {

        RemoteViews remoteViews = new RemoteViews(context.getPackageName(),
                R.layout.widget);

        //Obtener el último libro leído y su autor
        SharedPreferences prefs = context.getSharedPreferences("ultimo_libro_reproducido",
                Context.MODE_PRIVATE);
        int id_libro = prefs.getInt("id_libro",0);
        Libro libro = ((Aplicacion) context.getApplicationContext()).getListaLibros().get(id_libro);

        remoteViews.setTextViewText(R.id.TV_titulo, "Último título leído: " + libro.titulo);
        remoteViews.setTextViewText(R.id.TV_autor, "Autor: " + libro.autor);

        //Lanzar una actividad al pulsar una vista (icono_lista)
        Intent intent = new Intent(context, MainActivity.class);
        PendingIntent pendingIntent = PendingIntent.getActivity(context, 0,
                intent, 0);

        remoteViews.setOnClickPendingIntent(R.id.icono_lista, pendingIntent);

        AppWidgetManager.getInstance(context).updateAppWidget(widgetId,
                remoteViews);
    }


}


