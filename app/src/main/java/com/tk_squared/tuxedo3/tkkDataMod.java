package com.tk_squared.tuxedo3;

import android.net.Uri;

import java.net.URI;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by Kevin on 12/29/2015.
 * science bitches
 */
public class tkkDataMod {

    private static tkkDataMod instance = null;
    private List<tkkStation> stations;

    private tkkDataMod(){
        stations = new ArrayList<>();
    }

    //Generates a list of dummy stations for UI testing and functionality
    public static void genDummyData(){

        for(int i = 1; i < 15; ++i) {
            instance.stations.add(new tkkStation("WSB "+i, Uri.parse("http://m.wsbradio.com/stream/")));
            instance.stations.add(new tkkStation("WQXR "+i, Uri.parse("http://www.wqxr.org/#!/")));
        }
    }
    //Used to create tkkDataMod singleton
    public static tkkDataMod getInstance(){

        if(instance == null) {
            instance = new tkkDataMod();
            //Replace genDummyData with real list pull method
            genDummyData();
        }
        return instance;
    }

    public List<tkkStation> getStations(){
        return stations;
    }

    public void setStations(List<tkkStation> s) {
        if(stations != null){
            stations.clear();
        }
        stations = s;
    }

    public void addStation(tkkStation s){
        stations.add(s);
    }
    
    public void addStationAt(int idx, tkkStation s){
        stations.set(idx, s);
    }

    public void removeStation(tkkStation s){
        stations.remove(s);
    }

    public void removeStationAt(int i){
        stations.remove(i);
    }

    public tkkStation getStationAt(int idx) {
        return stations.get(idx);
    }

}
