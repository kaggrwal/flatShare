package com.juqueen.flatshare;

import android.app.Activity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import java.util.ArrayList;

/**
 * Created by juqueen on 1/20/2016.
 */
public class customFlatListAdapter extends ArrayAdapter<String> {

    private final Activity context;
    private final ArrayList<String> itemname;
    private final Integer[] imgid;

    public customFlatListAdapter(Activity context, ArrayList<String> itemname, Integer[] imgid) {
        super(context, R.layout.single_flat_list , itemname);
        // TODO Auto-generated constructor stub

        this.context=context;
        this.itemname=itemname;
        this.imgid=imgid;
    }

    public View getView(int position,View view,ViewGroup parent) {
        LayoutInflater inflater=context.getLayoutInflater();
        View rowView=inflater.inflate(R.layout.single_flat_list, null,true);

        TextView txtTitle = (TextView) rowView.findViewById(R.id.flatName);
        ImageView imageView = (ImageView) rowView.findViewById(R.id.flatLogo);
        TextView extratxt = (TextView) rowView.findViewById(R.id.flatUpd);

        txtTitle.setText(itemname.get(position));
        imageView.setImageResource(imgid[position]);
        extratxt.setText("Description "+itemname.get(position));
        return rowView;

    };

}



