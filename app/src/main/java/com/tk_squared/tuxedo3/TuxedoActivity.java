package com.tk_squared.tuxedo3;

import android.content.Context;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.app.Fragment;
import android.app.FragmentManager;
import android.util.Log;
import android.view.MenuItem;

import java.util.List;

public class TuxedoActivity extends AppCompatActivity implements TuxedoActivityFragment.Callbacks{
    private static Context context;
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
        context = getApplicationContext();
    }

    public static Context getTuxedoContext() {
        return context;
    }

    @Override
    public void onStationSelected(tkkStation station){

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
    }

    public boolean onOptionsItemSelected(MenuItem item){

        switch(item.getItemId()){
            case android.R.id.home:
                FragmentManager fm = getFragmentManager();
                if (fm.getBackStackEntryCount() > 0){
                    fm.popBackStack();
                }
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }

    }
}
