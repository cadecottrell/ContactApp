package com.example.contact;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.TextView;

import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.model.Marker;



//Custom Marker Window to show all neccessary information on the Maps
public class InfoWindowAdapter implements GoogleMap.InfoWindowAdapter {

    private final View window;
    private Context context;

    public InfoWindowAdapter(Context context) {
        this.context = context;
        window = LayoutInflater.from(context).inflate(R.layout.custom_info_window, null);
    }

    //Sets up the Window, has title on top, then below has the custom snippet.
    private void showWindowText(Marker marker, View view)
    {
        String title = marker.getTitle();
        TextView textTitle = view.findViewById(R.id.title);

        if(!title.equals(""))
        {
            textTitle.setText(title);
        }

        String snippet = marker.getSnippet();
        TextView textSnippet = view.findViewById(R.id.title2);

        if(!snippet.equals(""))
        {
            textSnippet.setText(snippet);
        }


    }

    @Override
    public View getInfoWindow(Marker marker) {
        showWindowText(marker, window);
        return window;
    }

    @Override
    public View getInfoContents(Marker marker) {
        showWindowText(marker, window);
        return window;
    }
}
