package com.example.audiolibros.fragments;

import android.os.Bundle;
import android.preference.PreferenceFragment;

import com.example.audiolibros.R;

/**
 * Created by Isabel María on 18/02/2018.
 */

public class PreferenciasFragment extends PreferenceFragment {
    @Override
    public void onCreate (Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        addPreferencesFromResource(R.xml.preferences);
    }
}
