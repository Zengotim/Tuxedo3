package com.tk_squared.tuxedo3;

import android.app.Activity;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.drawable.BitmapDrawable;
import android.net.Uri;
import android.os.AsyncTask;
import android.util.Log;

import java.io.IOException;
import java.io.InputStream;
import java.net.MalformedURLException;
import java.net.URL;
import java.sql.SQLException;
import java.util.ArrayList;

/**
 * Created by Kevin on 12/29/2015.
 * science bitches
 */


public class tkkDataMod {

    //TODO Hi Kevin I changed this naming for reasons you may feel free to ask about
    public interface Callbacks {
        void onDataLoaded(ArrayList<tkkStation> _stations);
    }
    //TODO Hi again Kevin I created this one for you to call for progress bar update
    public interface ProgressUpdate {
        void onProgressUpdate(float progress);
    }


    private static tkkDataMod instance = null;
    private ArrayList<tkkStation> stations;
    private tkkStationsDataSource dataSource;
    private Activity _activity;
    private int tasks = 0;
    private int completes = 0;

    public class SQLLoadTask extends AsyncTask<Void, Integer, Integer> {

        private ArrayList<tkkStation> stations;
        private tkkStationsDataSource dataSource;
        public SQLLoadTask(ArrayList<tkkStation>_stations, tkkStationsDataSource _dataSource){
            this.stations = _stations;
            this.dataSource = _dataSource;
        }

        @Override
        protected Integer doInBackground(Void... params) {
            tkkDataMod.instance.setStations(dataSource.getAllStations(_activity));
            return null;
        }

        protected void onProgressUpdate(Integer... progress){

        }

        protected void onPostExecute(Integer result){

        }
    }

    public class CreateStationTask extends AsyncTask<Void, Integer, Integer>{
        private BitmapDrawable icon;
        private Bitmap bitmap;
        private tkkStation station;
        private String name;
        private String iconURL;
        private Uri uri;

        public CreateStationTask(tkkStation station, String iconURL){
            this.station = station;
            this.iconURL = iconURL;
        }

        public CreateStationTask(String name, String uri, String iconURL) {
            this.name = name;
            this.iconURL = iconURL;
            this.uri = Uri.parse(uri);
        }

        public CreateStationTask(tkkStation station, byte[] bytes) {
            this.station = station;
            this.bitmap = tkkStationsDataSource.BitmapHelper.getImage(bytes);
        }

        @Override
        protected Integer doInBackground(Void... unused){
            try {
                //  String
                if(bitmap == null) {
                    if(iconURL == null)  iconURL = "http://www.google.com/favicon.ico";
                    bitmap = BitmapFactory.decodeStream((InputStream) new URL(iconURL).getContent());
                    //instance.stations.add(dataSource.createStation(station.getName(), this.uri, this.bitmap));
                }
                if (bitmap != null) {
                    byte[] blob = tkkStationsDataSource.BitmapHelper.getBytes(bitmap);

                    icon = new BitmapDrawable(_activity.getApplicationContext().getResources(), Bitmap.createScaledBitmap(bitmap, 96, 96, false));
                   // instance.stations.add(new tkkStation(instance.stations.size(), this.name, icon, this.uri));
                }
            }
            catch(MalformedURLException e){
                //do nothing
            }
            catch (IOException e){
                Log.i("IOException", "IOException: " + e.toString());
                //Still nothing;
            }
            return 0;
        }

        protected void onProgressUpdate(Integer... progress){
            //TODO: make progress tint bar across image
        }

        protected void onPostExecute(Integer result){

            instance.stations.add(dataSource.createStation(this.name, this.uri, this.bitmap, _activity));

            if(++completes >= tasks) {
                Callbacks cb = (Callbacks)_activity;
                cb.onDataLoaded(instance.stations);
            }
        }
    }

    private tkkDataMod(){
    }

    //Generates a list of dummy stations for UI testing and functionality
    private void genDummyData() {

        //TIM COMMENT OUT dataSource.deleteAll(); to test how the app handles getting station data from the DB rather than populating
       // dataSource.deleteAll();
        instance.stations = dataSource.getAllStations(_activity);

        if (instance.stations.size() == 0) {
            for (int i = 1; i < 15; ++i) {
                String wsbIcon = "http://www.google.com/s2/favicons?domain=wsbradio.com";
                String wqxrIcon = "http://www.google.com/s2/favicons?domain=wqxr.org";

                CreateStationTask wsb = new CreateStationTask("WSB " + i, "http://m.wsbradio.com/stream/",
                        wsbIcon);
                ++tasks;
                wsb.execute();
                CreateStationTask wqxr = new CreateStationTask("WQXR" + i, "http://wqxr.org/#!/", wqxrIcon);
                ++tasks;
                wqxr.execute();

            }
        } else {
            Callbacks cb = (Callbacks)_activity;
            cb.onDataLoaded(instance.stations);
        }

        System.out.println(stations.size());
    }



    public static tkkDataMod getInstance(Activity activity){

        if(instance == null) {
            instance = new tkkDataMod();
            instance.stations = new ArrayList<>();
            // uncomment to delete the database
            //TuxedoActivity.getTuxedoContext().deleteDatabase("stations.db");
            instance._activity = activity;
            instance.dataSource = new tkkStationsDataSource(instance._activity.getApplicationContext());

            try {

                instance.dataSource.open();
            } catch (SQLException e) {
                e.printStackTrace();
            }
            //Replace genDummyData with real list pull method
            instance.genDummyData();
        }
        return instance;
    }

    //Used to create tkkDataMod singleton
    public static tkkDataMod getInstance() {

        if(instance == null) {
            instance = new tkkDataMod();
            //Replace genDummyData with real list pull method

            instance.genDummyData();

        }
        return instance;
    }

    public ArrayList<tkkStation> getStations(){
        return stations;
    }

    public void setStations(ArrayList<tkkStation> s) {
        if(stations != null){
            stations.clear();
        }
        stations = s;
    }

    public void addStation(tkkStation s){
        //dataSource.createStation()
        stations.add(s);
    }

    public void moveStation(int idx, int newIdx){
        moveStation(getStationAt(idx), newIdx);
    }

    public void moveStation(tkkStation s, int newIdx){
        stations.remove(s);
        stations.add(newIdx,s);
        int iter = s.getIndex() <= newIdx ? s.getIndex() : newIdx;

        for (int i = iter; i < stations.size(); ++i){
            tkkStation temp = stations.get(i);
            temp.setIndex(i);
            dataSource.updateStation(s, _activity);
        }
    }

    public void addStationAt(int idx, tkkStation s){
        stations.set(idx, s);
    }

    public void removeStation(tkkStation s){
        dataSource.deleteStation(s);
        stations.remove(s);
    }

    public void removeStationAt(int i){
        tkkStation s = stations.get(i);
        dataSource.deleteStation(s);
        stations.remove(i);
    }

    public tkkStation getStationAt(int idx) {
        return stations.get(idx);
    }





}