package com.tk_squared.tuxedo3;

import android.app.Fragment;
import android.net.Uri;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.webkit.WebChromeClient;
import android.webkit.WebView;
import android.widget.Toast;

/**
 * Created by Tim on 1/4/2016.
 *
 */
public class TuxedoWebViewFragment extends Fragment{

    private Uri uri;

    public TuxedoWebViewFragment(){}

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        WebView webview = (WebView)inflater.inflate(R.layout.fragment_webview, container, false);
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
        return webview;
    }

}
