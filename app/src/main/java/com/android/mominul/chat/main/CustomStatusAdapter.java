package com.android.mominul.chat.main;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import com.android.mominul.chat.R;

import java.util.ArrayList;

public class CustomStatusAdapter extends ArrayAdapter<String>{

    public CustomStatusAdapter(Context context, ArrayList<String> name){
        super(context, R.layout.customstatus,name);
    }

    @NonNull
    @Override
    public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {
        LayoutInflater inflater = LayoutInflater.from(getContext());
        View customview = inflater.inflate(R.layout.customstatus,parent,false);
        String n = getItem(position);
        TextView name = (TextView) customview.findViewById(R.id.statusname);
        TextView status = (TextView) customview.findViewById(R.id.status);

        name.setText(n);
        status.setText("Online");

        return customview;
    }
}
