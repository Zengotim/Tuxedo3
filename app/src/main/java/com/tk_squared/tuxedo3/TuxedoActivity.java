package com.tk_squared.tuxedo3;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.app.Fragment;
import android.app.FragmentManager;

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
        //TODO: make it work, bitch!
        FragmentManager fm = getFragmentManager();
        Fragment fragment = fm.findFragmentById(R.id.webview_fragment);
        if (fragment == null){
            fragment = new TuxedoWebViewFragment();
            Bundle args = new Bundle();
            args.putString("uri", station.getUri().toString());
            fragment.setArguments(args);
        }
        fm.beginTransaction().add(R.id.fragment_container, fragment);
    }
}
