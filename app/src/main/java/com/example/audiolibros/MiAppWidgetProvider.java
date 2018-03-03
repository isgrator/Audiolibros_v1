package com.example.audiolibros;

import android.appwidget.AppWidgetManager;
import android.appwidget.AppWidgetProvider;
import android.content.Context;
import android.content.SharedPreferences;
import android.widget.RemoteViews;

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

    /*
    actualizar eltexto del TextView (R.id.textView1), de manera que nos muestre el valor de un contador.
    Este contador lo iremos incrementando cada vez que se actualice el widget en el método
    incrementaContador().
     */
    public static void actualizaWidget(Context context, int widgetId) {
        int cont = incrementaContador(context, widgetId);
        RemoteViews remoteViews = new RemoteViews(context.getPackageName(),
                R.layout.widget);
        remoteViews.setTextViewText(R.id.TV_titulo, "Último título leído: " + cont);
        AppWidgetManager.getInstance(context).updateAppWidget(widgetId,
                remoteViews);
    }

    private static int incrementaContador(Context context, int widgetId) {
        SharedPreferences prefs = context.getSharedPreferences("contadores",
                Context.MODE_PRIVATE);
        int cont = prefs.getInt("cont_" + widgetId, 0);
        cont++;
        SharedPreferences.Editor editor = prefs.edit();
        editor.putInt("cont_" + widgetId, cont);
        editor.commit();
        return cont;
    }
}


