package com.jeannypr.scientificstudy.Base.adapter;


import android.content.Context;
import android.graphics.Color;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import com.jeannypr.scientificstudy.Base.Model.DropDownModel;

import java.util.ArrayList;

public class DropDownAdapter extends ArrayAdapter<DropDownModel> {

    // Your sent context
    private Context context;
    // Your custom values for the spinner (User)
    private ArrayList<DropDownModel> values;

    public DropDownAdapter(Context context, int textViewResourceId,
                       ArrayList<DropDownModel> values) {
        super(context, textViewResourceId, values);
        this.context = context;
        this.values = values;
    }

    public void setItem(){

    }
    @Override
    public int getCount(){
        return values.size();
    }

    @Override
    public DropDownModel getItem(int position){
        return values.get(position);
    }

    @Override
    public long getItemId(int position){
        return position;
    }


    // And the "magic" goes here
    // This is for the "passive" state of the spinner
    @Override
    public View getView(int position, View convertView, ViewGroup parent) {


        // we can reference our own  custom layout for each spinner item
        TextView label = (TextView) super.getView(position, convertView, parent);
        label.setTextColor(Color.DKGRAY);
        // Then you can get the current item using the values array (Users array) and the current position
        // You can NOW reference each method you has created in your bean object (User class)
        label.setText(values.get(position).getText());

        // And finally return your dynamic (or custom) view for each spinner item
        return label;
    }

    // And here is when the "chooser" is popped up
    // Normally is the same view, but you can customize it if you want
    @Override
    public View getDropDownView(int position, View convertView,
                                ViewGroup parent) {
        TextView label = (TextView) super.getDropDownView(position, convertView, parent);
        label.setTextColor(Color.DKGRAY);
        label.setText(values.get(position).getText());

        return label;
    }
}