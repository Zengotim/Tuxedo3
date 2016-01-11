package com.tk_squared.tuxedo3;

import android.net.Uri;
import android.widget.ImageView;

/**
 * Created by Kevin for tk^2 on 12/29/2015.
 * science bitches
 */
public class tkkStation {
    private long id;
    private Uri uri;
    private String name;
    private Uri imageUri;
    private int index;

    public tkkStation(){

    }

    //I DID THIS!! TIM!!
    public Uri getIconURI(){
        return Uri.parse("http://www.tshirthell.com/favicon.ico");
    }

    public tkkStation(long _id, String n, Uri u) {
        id = _id;
        uri = u;
        name = n;
    }

    public tkkStation(int idx, long _id, String n, Uri u) {
        id = _id;
        index = idx;
        uri = u;
        name = n;
    }

    public tkkStation(int idx, long _id, Uri iU, String n, Uri u) {
        id = _id;
        index = idx;
        uri = u;
        name = n;
        imageUri = iU;
    }

    public Uri getImageView(){
        return imageUri;
    }

    public void setImageView(Uri iU){
        imageUri = iU;
    }

    public long getId() { return id; }

    public void setId(long _id) { id = _id; }

    public int getIndex(){
        return index;
    }

    public void setIndex(int idx) {
        index = idx;
    }

    public String getName(){
        return name;
    }

    public void setName(String n){
        name = n;
    }

    public Uri getUri(){
        return uri;
    }

    public void setUri(Uri u){
        uri = u;
    }

}
