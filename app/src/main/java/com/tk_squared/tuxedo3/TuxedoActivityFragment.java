package com.tk_squared.tuxedo3;


import android.app.Fragment;
import android.content.Context;
import android.graphics.drawable.BitmapDrawable;
import android.support.v7.widget.Toolbar;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import java.util.ArrayList;

public class TuxedoActivityFragment extends Fragment implements RearrangeableListView.RearrangeListener{

    //region Description: Variable and Interface declarations
    private RearrangeableListView listView;
        public RearrangeableListView getListView(){return listView;}
    private int position;
    private Callbacks callbacks;

    //Interface for handling fragment change on selection
    public interface Callbacks{
        void onStationSelected(tkkStation station);
    }

    public TuxedoActivityFragment() {
    }
    //endregion


    //region Desc: RearrangeableListView Listener interface methods
    @Override
    public void onGrab(int index) {
        //Record what station we're dealing with
        position = index;
    }

    @Override
    public boolean onRearrangeRequested(int fromIndex, int toIndex) {
        //If data is valid, move it
        if (toIndex > 0 && toIndex < listView.getCount()) {
            ((TuxedoActivity)getActivity()).getData().moveStation(fromIndex, toIndex);
            ((ArrayAdapter) listView.getAdapter()).notifyDataSetChanged();
            position = -1;
            return true;
        }
        return false;
    }

    @Override
    public void onDrop() {
        //If nothing moved, view the station
        if (position > 0) {
            tkkStation station = (tkkStation) listView.getItemAtPosition(position);
            if (callbacks == null) {
                callbacks = (Callbacks) getActivity();
            }
            callbacks.onStationSelected(station);
        }
    }
    //endregion

    //region Description: Lifecycle and Super Override methods
    @Override
    public void onCreate(Bundle savedInstanceState){
        super.onCreate(savedInstanceState);
        setRetainInstance(true);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_tuxedo, container, false);
    }

    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        final TuxedoActivity tuxActivity = (TuxedoActivity)getActivity();
        listView = (RearrangeableListView) getView().findViewById(R.id.list);
        if (listView == null){
            listView = new RearrangeableListView(getActivity());
        }

        ArrayAdapter adapter = new StationAdapter(tuxActivity, tuxActivity.getTkkData());
        listView.setAdapter(adapter);
        listView.setRearrangeEnabled(((TuxedoActivity) getActivity()).getListEditEnabled());
        listView.setRearrangeListener(this);
        callbacks = tuxActivity;
        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                tkkStation station = (tkkStation) listView.getItemAtPosition(position);
                if (callbacks == null) {
                    callbacks = (Callbacks) getActivity();
                }
                callbacks.onStationSelected(station);
            }
        });
        Toolbar toolbar = (Toolbar) tuxActivity.findViewById(R.id.toolbar);
        toolbar.setSubtitle(R.string.subtitle);
        tuxActivity.setSupportActionBar(toolbar);
    }
    //endregion

    //Adapter class for the ListView
    public class StationAdapter extends ArrayAdapter<tkkStation>{

        //region Description: Variables and Constructor
        private boolean showDelete = true;
            public void setShowDelete(boolean show){showDelete = show;}

        public StationAdapter(Context context, ArrayList<tkkStation> list){
            super(context, 0, list);
        }
        //endregion

        @Override
        public View getView(final int position, View view, ViewGroup parent){

            //Get the item for this cell
            final tkkStation station = getItem(position);
            //Cell may be being recycled, otherwise inflate view
            if (view == null){
                view = LayoutInflater.from(getContext()).inflate(R.layout.item_station, parent, false);
            }
            //Populate data
            ((TextView)view.findViewById(R.id.station_title)).setText(station.getName());
            ((TextView) view.findViewById(R.id.station_subtitle)).setText(station.getUri().toString());
            //Station icon
            BitmapDrawable icon = station.getIcon();
            if (icon != null){
                ((ImageView) view.findViewById(R.id.station_icon)).setImageDrawable(station.getIcon());
            }
            //Delete Button
            Button deleteButton = (Button)view.findViewById(R.id.delete_button);
            deleteButton.setFocusable(false);
            deleteButton.setClickable(showDelete);
            if (showDelete) {
                deleteButton.setVisibility(View.VISIBLE);
            }else{
                deleteButton.setVisibility(View.GONE);
            }
            deleteButton.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    ((TuxedoActivity) getActivity()).getData().removeStationAt(position);
                    notifyDataSetChanged();
                }
            });
            return view;
        }
    }
}