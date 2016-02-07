package com.tk_squared.tuxedo3;

import android.annotation.SuppressLint;
import android.app.Fragment;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.webkit.WebChromeClient;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.util.Log;

import com.facebook.FacebookCallback;
import com.facebook.FacebookException;
import com.facebook.login.LoginResult;
import com.facebook.share.Sharer;
import com.facebook.share.model.ShareLinkContent;
import com.facebook.share.widget.ShareDialog;

/**
 * Created by Tim on 1/4/2016.
 * 'Cuz Tim rocks.
 */

@SuppressLint("SetJavaScriptEnabled")
public class TuxedoWebViewFragment extends Fragment{

    //region Description: Variables and Constructor
    private WebView webview; public WebView getWebview(){ return webview;}
    private ShareDialog shareDialog;
    private ShareLinkContent linkContent;
    private String currentUrl;
    private String currentName;

    public TuxedoWebViewFragment(){}

    //endregion

    //region Description: Lifecycle and Super Overrides
    @Override
    public void onActivityCreated(Bundle savedInstanceState){
        super.onActivityCreated(savedInstanceState);
        //Make a toolbar
        Toolbar toolbar = (Toolbar) getActivity().findViewById(R.id.webview_toolbar);
        toolbar.setSubtitle(R.string.subtitle);
        AppCompatActivity activity = (AppCompatActivity)getActivity();
        activity.setSupportActionBar(toolbar);
        //Initialize the Facebook Share Dialog
        shareDialog = new ShareDialog(this);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_webview, container, false);
    }

    @Override
    public void onPause(){
        super.onPause();
    }

    @Override
    public void onResume(){
        super.onResume();
        setupWebView();
        //Called to set up share dialog
        prepShareDialog();

    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        ((TuxedoActivity)getActivity()).getCallbackManager().onActivityResult(requestCode, resultCode, data);

    }
    //endregion

    //region Description: private methods for setups
    private void setupWebView(){
        //Setup the WebView

        if(webview== null) {
            webview = (WebView) getView().findViewById(R.id.webview_view);
            webview.getSettings().setJavaScriptEnabled(true);
            webview.setWebViewClient(new WebViewClient() {
                @Override
                public boolean shouldOverrideUrlLoading(WebView view, String url) {
                    view.loadUrl(url);
                    return false;
                }
            });

            currentName = getArguments().getString("name");
            currentUrl = getArguments().getString("uri");
            webview.loadUrl(currentUrl);
            Log.i("URL", currentUrl);
        } else {
            Log.i("WebView: ", "webview isn't null, bro");
        }
    }

    public void onShareStation(){
        ShareDialog.show(this, linkContent);
    }

    public void prepShareDialog(){
        final TuxedoActivity tuxedoActivity = (TuxedoActivity)getActivity();
        try {
            if (ShareDialog.canShow(ShareLinkContent.class)) {
                //Create the post
                String description = "Listen to " + currentName + " on Tuxedo!";
                linkContent = new ShareLinkContent.Builder()
                        .setContentTitle(currentName)
                        .setContentDescription(description)
                        .setContentUrl(Uri.parse("https://play.google.com/store/apps/details?id=com.tk_squared.tuxedo3"))
                        .setImageUrl(Uri.parse("http://www.tk-squared.com/tux_icon.png"))
                        .build();
                //Sharing callbacks
                shareDialog.registerCallback(tuxedoActivity.getCallbackManager(), new FacebookCallback<Sharer.Result>() {
                    @Override
                    public void onSuccess(Sharer.Result shareResult) {
                        Log.i("Share Success", "Shared to facebook");
                    }

                    @Override
                    public void onCancel() {
                        Log.i("Cancel", "Canceled");
                        try {
                            Log.i("Webview Title: ", webview.getTitle());
                        } catch(Exception e) {
                            Log.i("webview.getTitle(): ", e.toString());
                        }
                    }

                    @Override
                    public void onError(FacebookException e) {
                        Log.i("Error", "Error");
                    }
                });

            }
        } catch (Exception e) {
            Log.e("ShareDialogError: ", e.toString());
        }
    }
    //endregion
}