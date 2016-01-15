package com.tk_squared.tuxedo3;

import android.app.Fragment;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

/**
 * Created by zengo on 1/14/2016.
 * i rulez
 */
public class SplashFragment extends Fragment implements tkkDataMod.ProgressUpdate{

    public SplashFragment(){}

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        //TuxedoActivity activity = (TuxedoActivity)getActivity();
        //activity.setData(tkkDataMod.getInstance(activity));
        return inflater.inflate(R.layout.fragment_splash, container, false);
    }

    @Override
    public void onProgressUpdate(float progress){
        //TODO this
    }
}
