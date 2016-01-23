package com.tk_squared.tuxedo3;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.app.Fragment;
import android.app.FragmentManager;
import android.view.Menu;
import android.view.MenuItem;
import java.util.ArrayList;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

//Millennial Media Ad Support
import com.millennialmedia.InlineAd;
import com.millennialmedia.MMException;
import com.millennialmedia.MMSDK;

public class TuxedoActivity extends AppCompatActivity
        implements TuxedoActivityFragment.Callbacks, tkkDataMod.Callbacks {

    private tkkDataMod tuxData;
    public tkkDataMod getData() {
        return tuxData;
    }
    public void setData(tkkDataMod data) {
        tuxData = data;
    }
    private ArrayList<tkkStation> tkkData;
    public ArrayList<tkkStation> getTkkData() { return tkkData; }
    private FragmentManager fm;
    private Integer curFragment = 0;
    private static final String TAG = "Ad Server message - ";

    public TuxedoActivity() {
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_tuxedo);
        //Set up ad support
        setMMedia();
        setAdSpace();
        //Begin app with Splash Screen
        fm = getFragmentManager();
        Fragment fragment = fm.findFragmentById(R.id.fragment_container);
        if (fragment == null) {
            fragment = new SplashFragment();
            fm.beginTransaction().add(R.id.fragment_container, fragment).commit();
        }
        //Get data model
        tuxData = tkkDataMod.getInstance(this);
    }

    //Callback method for tkkDataMod.Callbacks
    @Override
    public void onDataLoaded(ArrayList<tkkStation> stations) {
        //Set data and switch to Listview fragment
        tkkData = stations;
        Fragment fragment = fm.findFragmentById(R.id.fragment_container);
        if (!(fragment instanceof TuxedoActivityFragment)){
            fragment = new TuxedoActivityFragment();
            fm.beginTransaction().replace(R.id.fragment_container, fragment)
                    .addToBackStack("ListView")
                    .commit();
        }
    }

    //Callback method for TuxedoActivityFragment.Callbacks
    @Override
    public void onStationSelected(tkkStation station) {
        //Change to WebView to view selected station
        Fragment fragment = fm.findFragmentById(R.id.fragment_container);
        if (!(fragment instanceof TuxedoWebViewFragment)) {
            fragment = new TuxedoWebViewFragment();
            Bundle args = new Bundle();
            args.putString("uri", station.getUri().toString());
            fragment.setArguments(args);
            fm.beginTransaction().replace(R.id.fragment_container, fragment)
                    .addToBackStack("webView")
                    .commit();
            curFragment = 3;
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu){
        if (curFragment != 3) {
            getMenuInflater().inflate(R.menu.menu_tuxedo, menu);
        }
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                curFragment = 2;
                if (fm.getBackStackEntryCount() > 0) {
                    fm.popBackStack();
                }
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }

    @Override
    public void onBackPressed(){
        Fragment fragment = fm.findFragmentById(R.id.fragment_container);
        if (fragment instanceof TuxedoWebViewFragment &&
                ((TuxedoWebViewFragment) fragment).getWebview().canGoBack()){
            ((TuxedoWebViewFragment) fragment).getWebview().goBack();
        }else if (fm.getBackStackEntryCount() > 1){
            fm.popBackStack();
        }else{
            super.onBackPressed();
        }
    }

    //for Ad Support settings
    private void setMMedia(){
        MMSDK.initialize(this);
        /*UserData userData = new UserData()
                .setAge(<age>)
                .setChildren(<children>)
                .setCountry(<country>)
                .setDma(<dma>)
                .setDob(<dob>)
                .setEducation(<education>)
                .setEthnicity(<ethnicity>)
                .setGender(<gender>)
                .setIncome(<income>)
                .setKeywords(<keywords>)
                .setMarital(<marital>)
                .setPolitics(<politics>)
                .setPostalCode(<postal-code>)
                .setState(<state>);
        MMSDK.setUserData(userData);*/
    }

    private void setAdSpace(){

        try {
            // NOTE: The ad container argument passed to the createInstance call should be the
            // view container that the ad content will be injected into.

            View adContainer = findViewById(R.id.ad_container);
            if (adContainer == null){
                adContainer = new ImageView(this);
            }
            InlineAd inlineAd = InlineAd.createInstance(getString(R.string.mmedia_apid), (ViewGroup) adContainer);
            final InlineAd.InlineAdMetadata inlineAdMetadata = new InlineAd.InlineAdMetadata().
                    setAdSize(InlineAd.AdSize.BANNER);

            inlineAd.request(inlineAdMetadata);

            inlineAd.setListener(new InlineAd.InlineListener() {
                @Override
                public void onRequestSucceeded(InlineAd inlineAd) {

                    if (inlineAd != null) {
                        // set a refresh rate of 30 seconds that will be applied after the first request
                        inlineAd.setRefreshInterval(30000);

                        // The InlineAdMetadata instance is used to pass additional metadata to the server to
                        // improve ad selection
                        final InlineAd.InlineAdMetadata inlineAdMetadata = new InlineAd.InlineAdMetadata().
                                setAdSize(InlineAd.AdSize.BANNER);

                        inlineAd.request(inlineAdMetadata);
                    }
                    Log.i(TAG, "Inline Ad loaded.");
                }


                @Override
                public void onRequestFailed(InlineAd inlineAd, InlineAd.InlineErrorStatus errorStatus) {

                    Log.i(TAG, errorStatus.toString());
                }


                @Override
                public void onClicked(InlineAd inlineAd) {

                    Log.i(TAG, "Inline Ad clicked.");
                }


                @Override
                public void onResize(InlineAd inlineAd, int width, int height) {

                    Log.i(TAG, "Inline Ad starting resize.");
                }


                @Override
                public void onResized(InlineAd inlineAd, int width, int height, boolean toOriginalSize) {

                    Log.i(TAG, "Inline Ad resized.");
                }


                @Override
                public void onExpanded(InlineAd inlineAd) {

                    Log.i(TAG, "Inline Ad expanded.");
                }


                @Override
                public void onCollapsed(InlineAd inlineAd) {

                    Log.i(TAG, "Inline Ad collapsed.");
                }


                @Override
                public void onAdLeftApplication(InlineAd inlineAd) {

                    Log.i(TAG, "Inline Ad left application.");
                }
            });

        } catch (MMException e) {
            Log.e(TAG, "Error creating inline ad", e);
            // abort loading ad
        }
    }
}