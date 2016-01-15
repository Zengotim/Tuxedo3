package com.tk_squared.tuxedo3;

import android.app.Activity;
import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.net.Uri;
import android.os.AsyncTask;
import android.util.Log;

import java.net.URI;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

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

    public class SQLLoadTask extends AsyncTask<Void, Integer, Integer> {

        private ArrayList<tkkStation> stations;
        private tkkStationsDataSource dataSource;
        public SQLLoadTask(ArrayList<tkkStation>_stations, tkkStationsDataSource _dataSource){
            this.stations = _stations;
            this.dataSource = _dataSource;
        }

        @Override
        protected Integer doInBackground(Void... params) {
            tkkDataMod.instance.setStations(dataSource.getAllStations());
            return null;
        }

        protected void onProgressUpdate(Integer... progress){

        }

        protected void onPostExecute(Integer result){

        }
    }

    //Used to create tkkDataMod singleton
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

    private tkkDataMod(){

    }

    //Generates a list of dummy stations for UI testing and functionality
    private void genDummyData() {
       // dataSource.deleteAll();
        instance.stations = dataSource.getAllStations();
       // SQLLoadTask reader = new SQLLoadTask(stations, dataSource);
       // reader.executeOnExecutor(AsyncTask.THREAD_POOL_EXECUTOR, null);


        if (instance.stations.size() == 0) {
            for (int i = 1; i < 15; ++i) {

                instance.stations.add(dataSource.createStation("WSB "+i, Uri.parse("http://m.wsbradio.com/stream/")));
                instance.stations.add(dataSource.createStation("WQXR "+i, Uri.parse("http://www.wqxr.org/#!/")));

                //instance.stations.add(new tkkStation("WSB "+i, Uri.parse("http://m.wsbradio.com/stream/")));
                //instance.stations.add(new tkkStation("WQXR "+i, Uri.parse("http://www.wqxr.org/#!/")));
            }
        }

        System.out.println(stations.size());
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

        for (int i = newIdx; i < stations.size(); ++i){
            tkkStation temp = stations.get(i);
            temp.setIndex(i);
            dataSource.updateStation(s);
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
