package com.tk_squared.tuxedo3;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.app.Fragment;
import android.app.FragmentManager;
import android.view.MenuItem;
import java.util.ArrayList;
import android.util.Log;

//Millennial Media Ad Support
import com.millennialmedia.MMSDK;


public class TuxedoActivity extends AppCompatActivity
        implements TuxedoActivityFragment.Callbacks, tkkDataMod.Callbacks{

    private tkkDataMod tuxData;
    public tkkDataMod getData(){return tuxData;} public void setData(tkkDataMod data){tuxData = data;}
    private ArrayList<tkkStation> tkkData; public ArrayList<tkkStation> getTkkData(){return tkkData;}

    public TuxedoActivity(){}

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_tuxedo);
        //Begin app with Splash Screen
        FragmentManager fm = getFragmentManager();
        Fragment fragment = fm.findFragmentById(R.id.fragment_container);
        if (fragment== null){
            fragment = new SplashFragment();
            fm.beginTransaction().add(R.id.fragment_container, fragment).commit();
        }
        //Get data model
        tuxData = tkkDataMod.getInstance(this);
        //Ad support init
        MMSDK.initialize(this);
    }

    //Callback method for tkkDataMod.Callbacks
    @Override
    public void onDataLoaded(ArrayList<tkkStation> stations){
        //Set data and switch to Listview fragment
        tkkData = stations;
        FragmentManager fm = getFragmentManager();
        Fragment fragment = fm.findFragmentById(R.id.fragment_container);
        if (fragment == null){
            fragment = new TuxedoActivityFragment();
            fm.beginTransaction().replace(R.id.fragment_container, fragment)
                    .addToBackStack(null)
                    .commit();
        }
    }

    //Callback method for TuxedoActivityFragment.Callbacks
    @Override
    public void onStationSelected(tkkStation station){
        //Change to WebView to view selected station
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