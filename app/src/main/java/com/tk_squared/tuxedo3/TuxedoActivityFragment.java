package com.tk_squared.tuxedo3;


import android.app.ListFragment;
import android.content.Context;
import android.graphics.BitmapFactory;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.Bitmap;
import android.os.AsyncTask;
import android.support.v7.widget.Toolbar;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import java.io.IOException;
import java.io.InputStream;
import java.net.URL;
import java.net.MalformedURLException;
import java.util.ArrayList;


public class TuxedoActivityFragment extends ListFragment {

    public Callbacks callbacks;

    //Interface for handling fragment change on selection
    public interface Callbacks{
        void onStationSelected(tkkStation station);
    }

    public TuxedoActivityFragment() {
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_tuxedo, container, false);
    }

    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);

        TuxedoActivity tuxActivity = (TuxedoActivity)getActivity();
        ArrayAdapter adapter = new StationAdapter(tuxActivity, tuxActivity.getTkkData());
        setListAdapter(adapter);
        callbacks = tuxActivity;

        Toolbar toolbar = (Toolbar) tuxActivity.findViewById(R.id.toolbar);
        toolbar.setSubtitle(R.string.subtitle);
        tuxActivity.setSupportActionBar(toolbar);
    }

    @Override
    public void onViewCreated(View view, Bundle savedInstanceState){
        getListView().setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                tkkStation station = (tkkStation) getListAdapter().getItem(position);
                if (callbacks==null){
                    callbacks = (Callbacks)getActivity();
                }
                callbacks.onStationSelected(station);
            }
        });
    }

    public class StationAdapter extends ArrayAdapter<tkkStation>{

        public StationAdapter(Context context, ArrayList<tkkStation> list){
            super(context, 0, list);
        }

        @Override
        public View getView(int position, View view, ViewGroup parent){

            //Get the item for this cell
            final tkkStation station = getItem(position);
            //Cell may be being recycled, otherwise inflate view
            if (view == null){
                view = LayoutInflater.from(getContext()).inflate(R.layout.item_station, parent, false);
            }
            //Populate data
            ((TextView)view.findViewById(R.id.station_title)).setText(station.getUri().toString());
            ((TextView) view.findViewById(R.id.station_subtitle)).setText(station.getUri().toString());
            ((ImageView) view.findViewById(R.id.station_icon)).setImageDrawable(station.getIcon());

            return view;
        }
    }
}