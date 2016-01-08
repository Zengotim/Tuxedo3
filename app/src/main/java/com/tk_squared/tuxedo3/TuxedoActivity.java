package com.tk_squared.tuxedo3;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.app.Fragment;
import android.app.FragmentManager;
import android.util.Log;

import java.util.List;

public class TuxedoActivity extends AppCompatActivity implements TuxedoActivityFragment.Callbacks{

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
    }

    @Override
    public void onStationSelected(tkkStation station){

        Log.i("oss", "onStationSelected called");
        FragmentManager fm = getFragmentManager();

        Fragment fragment = fm.findFragmentById(R.id.webview_fragment);
        if (fragment == null){
            fragment = new TuxedoWebViewFragment();
            Bundle args = new Bundle();
            args.putString("uri", station.getUri().toString());
            fragment.setArguments(args);
        }
        fm.beginTransaction().replace(R.id.fragment_container, fragment)
                .addToBackStack(null)
                .commit();
        Log.i("oss1", "onStationSelected completed");
    }
}
