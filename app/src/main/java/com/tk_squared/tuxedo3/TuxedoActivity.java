package com.tk_squared.tuxedo3;

import android.content.Context;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.app.Fragment;
import android.app.FragmentManager;
import android.util.Log;
import android.view.MenuItem;

//Millennial Media Ad Support
import com.millennialmedia.MMSDK;

import java.util.ArrayList;
import java.util.List;

public class TuxedoActivity extends AppCompatActivity
        implements TuxedoActivityFragment.Callbacks, tkkDataMod.Callbacks{

    private tkkDataMod tuxData;
    public tkkDataMod getData(){return tuxData;}
    public void setData(tkkDataMod data){tuxData = data;}

    private ArrayList<tkkStation> tkkData; public ArrayList<tkkStation> getTkkData(){return tkkData;}

    public TuxedoActivity(){}

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_tuxedo);
        FragmentManager fm = getFragmentManager();
        Fragment fragment = fm.findFragmentById(R.id.fragment_container);
        if (fragment== null){
            fragment = new SplashFragment();
            fm.beginTransaction().add(R.id.fragment_container, fragment).commit();
        }
        tuxData = tkkDataMod.getInstance(this);
        //Ad support init
        MMSDK.initialize(this);
    }

    @Override
    public void onDataLoaded(ArrayList<tkkStation> stations){
        tkkData = stations;
        FragmentManager fm = getFragmentManager();
        Fragment fragment = fm.findFragmentById(R.id.fragment_container);
        if (fragment== null){
            fragment = new TuxedoActivityFragment();
            fm.beginTransaction().add(R.id.fragment_container, fragment).addToBackStack(null)
                    .commit();
            //fm.beginTransaction().replace(R.id.fragment_container, fragment).commit();
            //fm.beginTransaction().add(R.id.fragment_container, fragment).commit();
        }
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
