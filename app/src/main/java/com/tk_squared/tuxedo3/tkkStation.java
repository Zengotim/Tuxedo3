package com.tk_squared.tuxedo3;

import android.net.Uri;
import android.widget.ImageView;

/**
 * Created by Kevin for tk^2 on 12/29/2015.
 * science bitches
 */
public class tkkStation {
    private Uri uri;
    private String name;
    private ImageView imageView;
    private int index;

    public tkkStation(){

    }

    public tkkStation(String n, Uri u) {
        uri = u;
        name = n;
    }

    public tkkStation(int idx, String n, Uri u) {
        index = idx;
        uri = u;
        name = n;
    }

    public tkkStation(int idx, ImageView i, String n, Uri u) {
        index = idx;
        uri = u;
        name = n;
        imageView = i;
    }

    public ImageView getImageView(){
        return imageView;
    }

    public void setImageView(ImageView i){
        imageView = i;
    }

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
