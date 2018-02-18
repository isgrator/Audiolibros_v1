package com.example.audiolibros;

import android.app.Activity;
import android.os.Bundle;

import com.example.audiolibros.fragments.PreferenciasFragment;

/**
 * Created by Isabel María on 18/02/2018.
 */
public class PreferenciasActivity extends Activity {
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getFragmentManager().beginTransaction().replace(android.R.id.
                content, new PreferenciasFragment()).commit();
    }
}
