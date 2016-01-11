package com.tk_squared.tuxedo3;

import android.app.Fragment;
import android.net.Uri;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.webkit.WebChromeClient;
import android.webkit.WebView;
import android.widget.Toast;

/**
 * Created by Tim on 1/4/2016.
 * 'Cuz Tim rocks.
 */
public class TuxedoWebViewFragment extends Fragment{

    private Uri uri;
    private WebView webview;

    public TuxedoWebViewFragment(){}

    @Override
    public void onActivityCreated(Bundle savedInstanceState){
        super.onActivityCreated(savedInstanceState);
        Toolbar toolbar = (Toolbar) getActivity().findViewById(R.id.webview_toolbar);
        toolbar.setSubtitle(R.string.subtitle);
        AppCompatActivity activity = (AppCompatActivity)getActivity();
        activity.setSupportActionBar(toolbar);
        activity.getSupportActionBar().setDisplayShowHomeEnabled(true);
        activity.getSupportActionBar().setDisplayHomeAsUpEnabled(true);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        //Inflate the main view
        View view = inflater.inflate(R.layout.fragment_webview, container, false);
        //Also grab a reference to the WebView inner view
        webview = (WebView) view.findViewById(R.id.webview_view);


        return view;
    }

    @Override
    public void onStart(){
        super.onStart();
        //Setup the WebView
        webview.getSettings().setJavaScriptEnabled(true);
        webview.setWebChromeClient(new WebChromeClient() {
            public void onProgressChanged(WebView view, int progress) {
                getActivity().setProgress(progress * 1000);
            }
        });
        webview.setWebChromeClient(new WebChromeClient(){
            public void onReceivedError(WebView view, int ErrorCode,
                                        String description, String failingURL){
                Toast.makeText(getActivity(), R.string.url_error, Toast.LENGTH_SHORT).show();
            }
        });
        uri = Uri.parse(getArguments().getString("uri", null));
        webview.loadUrl(uri.toString());
    }

    @Override
    public void onStop(){
        super.onStop();
        webview.destroy();
    }

}
