package com.example.audiolibros.fragments;

import android.app.Fragment;
import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.media.MediaPlayer;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.NotificationCompat;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.MediaController;
import android.widget.RemoteViews;
import android.widget.TextView;
import android.widget.Toast;

import com.example.audiolibros.Aplicacion;
import com.example.audiolibros.Libro;
import com.example.audiolibros.MainActivity;
import com.example.audiolibros.OnZoomSeekBarListener;
import com.example.audiolibros.R;
import com.example.audiolibros.ZoomSeekBar;

import java.io.IOException;

/**
 * Created by Isabel María on 11/02/2018.
 */

public class DetalleFragment extends Fragment implements
        View.OnTouchListener, MediaPlayer.OnPreparedListener,
        MediaController.MediaPlayerControl {

    public static String ARG_ID_LIBRO = "id_libro";
    MediaPlayer mediaPlayer;
    MediaController mediaController;

    //Para la notificación
    private static final int ID_NOTIFICACION = 1;
    private NotificationManager notificManager;
    private NotificationCompat.Builder notificacion;
    private RemoteViews remoteViews;

    @Override public View onCreateView(LayoutInflater inflador, ViewGroup
            contenedor, Bundle savedInstanceState) {

        final View vista = inflador.inflate(R.layout.fragment_detalle,
                contenedor, false);
        Bundle args = getArguments();
        if (args != null) {
            int position = args.getInt(ARG_ID_LIBRO);
            ponInfoLibro(position, vista);
        } else {
            ponInfoLibro(0, vista);
        }

        ZoomSeekBar barraAzul =  vista.findViewById(R.id.barraAzul);
        //ZoomSeekBar barraNaranja =  vista.findViewById(R.id.barraNaranja);

        barraAzul.setOnZoomSeekBarListener(new OnZoomSeekBarListener() {
            @Override
            public void onValChanged(int val) {
                Toast.makeText(vista.getContext(),"Barra azul: "+Integer.toString(val),Toast.LENGTH_SHORT).show();
            }
        });
        /*barraNaranja.setOnZoomSeekBarListener(new OnZoomSeekBarListener() {
            @Override
            public void onValChanged(int val) {
                Toast.makeText(vista.getContext(),"Barra naranja: "+Integer.toString(val),Toast.LENGTH_SHORT).show();
            }
        });*/

        return vista;
    }

    private void ponInfoLibro(int id, View vista) {
        Libro libro = ((Aplicacion) getActivity().getApplication())
                .getListaLibros().get(id);
        ((TextView) vista.findViewById(R.id.TV_titulo)).setText(libro.titulo);
        ((TextView) vista.findViewById(R.id.autor)).setText(libro.autor);
        ((ImageView) vista.findViewById(R.id.portada))
                .setImageResource(libro.recursoImagen);
        vista.setOnTouchListener(this);
        if (mediaPlayer != null){
            mediaPlayer.release();
        }
        mediaPlayer = new MediaPlayer();
        mediaPlayer.setOnPreparedListener(this);
        mediaController = new MediaController(getActivity());
        Uri audio = Uri.parse(libro.urlAudio);
        try {
            mediaPlayer.setDataSource(getActivity(), audio);
            mediaPlayer.prepareAsync();
            recuerdaUltimoLibro(id);

            remoteViews = new RemoteViews(vista.getContext().getPackageName(), R.layout.notificacion_libro_reproducido);
            remoteViews.setImageViewResource(R.id.reproducir, android.R.drawable.ic_media_play);
            remoteViews.setImageViewResource(R.id.imagen, libro.recursoImagen);
            remoteViews.setTextViewText(R.id.TV_titulo, libro.titulo);
            remoteViews.setTextColor(R.id.TV_titulo, Color.BLACK);
            remoteViews.setTextViewText(R.id.TV_texto_autor, libro.autor);
            remoteViews.setTextColor(R.id.TV_texto_autor, Color.BLACK);

            //Lanzamiento de la notificación

            Intent intent = new Intent(vista.getContext(), MainActivity.class);
            PendingIntent pendingIntent = PendingIntent.getActivity(vista.getContext(), 0, intent,
                    PendingIntent.FLAG_UPDATE_CURRENT);

            notificacion = new NotificationCompat.Builder(vista.getContext())
                    .setContent(remoteViews)
                    .setPriority(Notification.PRIORITY_MAX)
                    .setSmallIcon(R.mipmap.ic_launcher)
                    .setContentTitle(libro.titulo)
                    .setContentText(libro.autor)
                    .setContentIntent(pendingIntent);
            notificManager = (NotificationManager)getActivity().getSystemService(Context.NOTIFICATION_SERVICE);
            notificManager.notify(ID_NOTIFICACION, notificacion.build());
        } catch (IOException e) {
            Log.e("Audiolibros", "ERROR: No se puede reproducir "+audio,e);
        }
    }

    public void ponInfoLibro(int id) {
        ponInfoLibro(id, getView());
    }

    @Override public void onPrepared(MediaPlayer mediaPlayer) {
        Log.d("Audiolibros", "Entramos en onPrepared de MediaPlayer");
        mediaPlayer.start();
        mediaController.setMediaPlayer(this);
        mediaController.setAnchorView(getView().findViewById(
                R.id.fragment_detalle));
        mediaController.setEnabled(true);
        mediaController.show();
    }

    @Override public boolean onTouch(View vista, MotionEvent evento) {
        mediaController.show();
        return false;
    }

    @Override public void onStop() {
        mediaController.hide();
        try {
            mediaPlayer.stop();
            mediaPlayer.release();
        } catch (Exception e) {
            Log.d("Audiolibros", "Error en mediaPlayer.stop()");
        }
        super.onStop();
    }


    @Override public boolean canPause() {
        return true;
    }



    @Override public boolean canSeekBackward() {
        return true;
    }

    @Override public boolean canSeekForward() {
        return true;
    }

    @Override public int getBufferPercentage() {
        return 0;
    }

    @Override public int getCurrentPosition() {
        try {
            return mediaPlayer.getCurrentPosition();
        } catch (Exception e) {
            return 0;
        }
    }

    @Override public int getDuration() {
        return mediaPlayer.getDuration();
    }

    @Override public boolean isPlaying() {
        return mediaPlayer.isPlaying();
    }

    @Override public void pause() {
        mediaPlayer.pause();
    }

    @Override public void seekTo(int pos) {
        mediaPlayer.seekTo(pos);
    }

    @Override public void start() {
        mediaPlayer.start();
    }

    @Override public int getAudioSessionId() {
        return 0;
    }

    //Método para almacenar en las oreferencias el último libro reproducido
    public void recuerdaUltimoLibro(int id) {

        SharedPreferences prefs = this.getActivity().getSharedPreferences("ultimo_libro_reproducido",
                Context.MODE_PRIVATE);

        SharedPreferences.Editor editor = prefs.edit();
        editor.putInt( "id_libro",id);
        editor.commit();
    }


}
