package com.tk_squared.tuxedo3;

import android.os.Bundle;
import android.os.Handler;
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
import android.widget.LinearLayout;
import android.widget.ProgressBar;

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
    private ProgressBar progBar;
    private static final String TAG = "Ad Server message - ";
    private boolean listEditEnabled = false; public boolean getListEditEnabled(){return listEditEnabled;}
    public void setEditEnabled(boolean enableEdit){listEditEnabled = enableEdit;}
    private Handler handler = new Handler();

    public TuxedoActivity() {
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_tuxedo);
        //Set up ad support
        setMMedia();
        setAdSpace();
        progBar = (ProgressBar)findViewById(R.id.progress_bar);
        progBar.setVisibility(View.VISIBLE);
        //Begin app with Splash Screen
        fm = getFragmentManager();
        Fragment fragment = fm.findFragmentById(R.id.fragment_container);
        if (fragment == null) {
            fragment = new SplashFragment();
            fm.beginTransaction()
                    .add(R.id.fragment_container, fragment)
                    .commit();
        }
        //Get data model
        tuxData = tkkDataMod.getInstance(this);
    }

    //Callback method for tkkDataMod.Callbacks
    @Override
    public void onDataLoaded(ArrayList<tkkStation> stations) {
        //Set data and switch to Listview fragment
        tkkData = stations;
        progBar.setVisibility(View.GONE);
        Fragment fragment = fm.findFragmentById(R.id.fragment_container);
        if (!(fragment instanceof TuxedoActivityFragment)){
            fragment = new TuxedoActivityFragment();
            fm.beginTransaction()
                    .replace(R.id.fragment_container, fragment)
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
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu){
        if (fm.findFragmentById(R.id.fragment_container) instanceof TuxedoActivityFragment) {
            getMenuInflater().inflate(R.menu.menu_tuxedo, menu);
            listEditEnabled = false;
            ((TuxedoActivityFragment)fm.findFragmentById(R.id.fragment_container))
                    .getListView()
                    .setRearrangeEnabled(listEditEnabled);
        }
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.action_fetch:
                //do something special for me
                return true;
            case R.id.action_edit:
                listEditEnabled = !listEditEnabled;
                if (listEditEnabled){
                    item.setChecked(true);
                }else{
                    item.setChecked(false);
                }
                ((TuxedoActivityFragment)fm.findFragmentById(R.id.fragment_container))
                                                    .getListView()
                                                    .setRearrangeEnabled(listEditEnabled);
                return true;
            case R.id.action_about:
                displayAbout();
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

    //Displays the About screen
    private void displayAbout(){
        Fragment fragment = fm.findFragmentById(R.id.fragment_container);
        if (!(fragment instanceof SplashFragment)){
            fragment = new SplashFragment();
            fm.beginTransaction().replace(R.id.fragment_container, fragment)
                    .addToBackStack("About")
                    .commit();
        }

        Runnable r = new Runnable() {
            @Override
            public void run() {
                if (fm.getBackStackEntryCount() > 0){
                    fm.popBackStack();
                }
            }
        };
        handler.postDelayed(r, 8000);
    }

    //region Description: Ad Support settings
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

            /*View adContainer = findViewById(R.id.ad_container);
            if (adContainer == null){
                adContainer = new ImageView(this);
            }*/
            InlineAd inlineAd = InlineAd.createInstance(getString(R.string.mmedia_apid),
                                            (LinearLayout)findViewById(R.id.ad_container));
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
    //endregion
}