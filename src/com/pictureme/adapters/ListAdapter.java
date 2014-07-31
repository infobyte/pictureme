/*
Pictureme
Copyright (C) 2014  Infobyte LLC (http://www.infobytesec.com/)
See the file 'doc/LICENSE' for the license information
*/
package com.pictureme.adapters;

import java.util.ArrayList;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;

public abstract class ListAdapter extends BaseAdapter {

    private ArrayList<?> entries; 
    private int R_layout_IdView; 
    private Context context;

    public ListAdapter(Context context, int R_layout_IdView, ArrayList<?> entries) {
        super();
        this.context = context;
        this.entries = entries; 
        this.R_layout_IdView = R_layout_IdView; 
    }

    @Override
    public View getView(int posicion, View view, ViewGroup pariente) {
        if (view == null) {
			LayoutInflater vi = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE); 
            view = vi.inflate(R_layout_IdView, null); 
        }
        onEntry(entries.get(posicion), view);
        return view; 
    }

	@Override
	public int getCount() {
		return entries.size();
	}

	@Override
	public Object getItem(int position) {
		return entries.get(position);
	}

	@Override
	public long getItemId(int position) {
		return position;
	}


	public abstract void onEntry (Object entry, View view);

}