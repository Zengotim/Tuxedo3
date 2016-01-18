package com.tk_squared.tuxedo3;

import android.annotation.SuppressLint;
import android.app.Fragment;
import android.net.Uri;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.webkit.WebChromeClient;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.Toast;
import android.util.Log;

/**
 * Created by Tim on 1/4/2016.
 * 'Cuz Tim rocks.
 */
@SuppressLint("SetJavaScriptEnabled")
public class TuxedoWebViewFragment extends Fragment{

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
        return inflater.inflate(R.layout.fragment_webview, container, false);
    }

    @Override
    public void onResume(){
        super.onResume();
        //Setup the WebView
        webview = (WebView) getView().findViewById(R.id.webview_view);
        webview.getSettings().setJavaScriptEnabled(true);
        webview.setWebChromeClient(new WebChromeClient() {
            public void onProgressChanged(WebView view, int progress) {
                getActivity().setProgress(progress * 1000);
            }
        });
        webview.setWebChromeClient(new WebChromeClient() {
            public void onReceivedError(WebView view, int ErrorCode,
                                        String description, String failingURL) {
                Toast.makeText(getActivity(), R.string.url_error, Toast.LENGTH_SHORT).show();
            }
        });
        webview.setWebViewClient(new WebViewClient() {
            @Override
            public boolean shouldOverrideUrlLoading(WebView view, String url){
                view.loadUrl(url);
                return false;
            }
        });

        webview.loadUrl(Uri.parse(getArguments().getString("uri", null)).toString());
    }

    @Override
    public void onPause(){
        super.onPause();
        webview.destroy();
    }

}
