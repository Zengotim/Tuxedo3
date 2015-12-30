package com.tk_squared.tuxedo3;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;

import java.util.List;

public class TuxedoActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_tuxedo);
        tkkDataMod temp = tkkDataMod.getInstance();
        List<tkkStation> temps = temp.getStations();
        for (int i = 0; i < temps.size(); ++i) {
            Log.i("FUCK BOI!", temps.get(i).getName());

        }
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
    }
}
