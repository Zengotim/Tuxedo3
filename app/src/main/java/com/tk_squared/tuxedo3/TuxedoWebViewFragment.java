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
import android.util.Log;

import com.facebook.share.model.ShareLinkContent;
import com.facebook.share.widget.ShareDialog;

/**
 * Created by Tim on 1/4/2016.
 * 'Cuz Tim rocks.
 */

@SuppressLint("SetJavaScriptEnabled")
public class TuxedoWebViewFragment extends Fragment{

    private WebView webview; public WebView getWebview(){ return webview;}
    private ShareDialog shareDialog;
    public TuxedoWebViewFragment(){}

    @Override
    public void onActivityCreated(Bundle savedInstanceState){
        super.onActivityCreated(savedInstanceState);
        Toolbar toolbar = (Toolbar) getActivity().findViewById(R.id.webview_toolbar);
        toolbar.setSubtitle(R.string.subtitle);
        AppCompatActivity activity = (AppCompatActivity)getActivity();
        shareDialog = new ShareDialog(this);

        activity.setSupportActionBar(toolbar);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_webview, container, false);
    }

    public void onShareStation(){
        /*
        ShareLinkContent content = new ShareLinkContent.Builder()
                .setContentUrl(Uri.parse(webview.getUrl()))
                .build();
        */

        if (ShareDialog.canShow(ShareLinkContent.class)) {
            String description = "Listen to " + webview.getTitle() + " on Tuxedo!";
            ShareLinkContent linkContent = new ShareLinkContent.Builder()
                    .setContentTitle(webview.getTitle())
                    .setContentDescription(description)
                    .setContentUrl(Uri.parse("https://play.google.com/store/apps/details?id=com.tk_squared.tuxedo3"))
                    .build();

            shareDialog.show(linkContent);
        }
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
        webview.setWebViewClient(new WebViewClient() {
            @Override
            public boolean shouldOverrideUrlLoading(WebView view, String url) {
                view.loadUrl(url);
                return false;
            }
        });

        webview.loadUrl(Uri.parse(getArguments().getString("uri", null)).toString());
        Log.i("URL", Uri.parse(getArguments().getString("uri", null)).toString());
    }

    @Override
    public void onPause(){
        super.onPause();
        webview.destroy();
    }
}