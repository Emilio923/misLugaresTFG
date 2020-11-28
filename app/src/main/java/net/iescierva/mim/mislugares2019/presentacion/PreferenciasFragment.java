package net.iescierva.mim.mislugares2019.presentacion;

import android.os.Bundle;
import android.preference.PreferenceFragment;

import net.iescierva.mim.mislugares2019.R;

public class PreferenciasFragment extends PreferenceFragment {
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        addPreferencesFromResource(R.xml.preferencias);
    }
}