package com.tk_squared.tuxedo3;

import android.app.ListFragment;
import android.content.Context;
import android.graphics.BitmapFactory;
import android.graphics.drawable.Drawable;
import android.graphics.Bitmap;
import android.os.AsyncTask;
import android.os.SystemClock;
import android.support.v7.widget.Toolbar;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.net.Uri;

import java.io.IOException;
import java.io.InputStream;
import java.net.URL;
import java.net.MalformedURLException;
import java.util.ArrayList;


public class TuxedoActivityFragment extends ListFragment {



    public TuxedoActivityFragment() {
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_tuxedo, container, false);
    }

    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        ArrayAdapter adapter = new StationAdapter(getActivity(), tkkDataMod.getInstance().getStations());
        // - saved shit: ArrayAdapter.createFromResource(getActivity(), R.array.Sites, android.R.layout.simple_list_item_1);
        setListAdapter(adapter);
        //getListView().setOnItemClickListener(this);

        Toolbar toolbar = (Toolbar) getActivity().findViewById(R.id.toolbar);
        toolbar.setSubtitle(R.string.subtitle);
        AppCompatActivity activity = (AppCompatActivity)getActivity();
        activity.setSupportActionBar(toolbar);
    }

    public class StationAdapter extends ArrayAdapter<tkkStation>{

        public StationAdapter(Context context, ArrayList<tkkStation> list){
            super(context, 0, list);
        }

        @Override
        public View getView(int position, View view, ViewGroup parent){
            //Get the item for this cell
            tkkStation station = getItem(position);
            //Cell may be being recycled, otherwise inflate view
            if (view == null){
                view = LayoutInflater.from(getContext()).inflate(R.layout.item_station, parent, false);
            }

            //Populate data
            TextView title = (TextView) view.findViewById(R.id.station_title);
            TextView subtitle = (TextView) view.findViewById(R.id.station_subtitle);
            title.setText(station.getName());
            subtitle.setText(station.getUri().toString());

            IconLoadTask setIcon = new IconLoadTask(view, position);
            setIcon.executeOnExecutor(AsyncTask.THREAD_POOL_EXECUTOR, null);

            return view;
        }

        public class IconLoadTask extends AsyncTask<Void, Integer, Integer>{
            private View view;
            private Integer position;
            private Bitmap icon;
            public IconLoadTask(View view, Integer position){
                this.view = view;
                this.position = position;
            }

            @Override
            protected Integer doInBackground(Void... unused){
                try {
                    String iconURL = "http://www.google.com/favicon.ico";
                    Bitmap _icon = BitmapFactory.decodeStream((InputStream) new URL(iconURL).getContent());
                    icon = Bitmap.createScaledBitmap(_icon, 96, 96, false);
                    //icon = Icon.createWithContentUri(getItem(position).getIconURI());
                }
                catch(MalformedURLException e){
                    //do nothing
                }
                catch (IOException e){
                    //Still nothing;
                }
                return 0;
            }

            protected void onProgressUpdate(Integer... progress){
                //TODO: make progress tint bar across image
            }

            protected void onPostExecute(Integer result){
                ImageView imageView = (ImageView)view.findViewById(R.id.station_icon);
                imageView.setImageBitmap(icon);
            }
        }
    }
}
