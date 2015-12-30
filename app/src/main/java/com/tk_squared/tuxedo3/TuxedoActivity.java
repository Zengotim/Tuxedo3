package com.tk_squared.tuxedo3;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.app.Fragment;
import android.app.FragmentManager;
import android.util.Log;

import java.util.List;

public class TuxedoActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_tuxedo);

        FragmentManager fm = getFragmentManager();
        Fragment fragment = fm.findFragmentById(R.id.fragment_container);
        if (fragment== null){
            fragment = new TuxedoActivityFragment();
            fm.beginTransaction().add(R.id.fragment_container, fragment).commit();
        }

        tkkDataMod temp = tkkDataMod.getInstance();
        List<tkkStation> temps = temp.getStations();
        for (int i = 0; i < temps.size(); ++i) {
            Log.i("FUCK BOI!", temps.get(i).getName());

        }

    }
}
