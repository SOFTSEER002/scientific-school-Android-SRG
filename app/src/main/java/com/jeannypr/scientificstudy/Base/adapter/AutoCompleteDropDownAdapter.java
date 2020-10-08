package com.jeannypr.scientificstudy.Base.adapter;


import android.content.Context;
import android.graphics.Color;
import androidx.annotation.NonNull;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Filter;
import android.widget.TextView;

import com.jeannypr.scientificstudy.Base.Model.DropDownModel;
import com.jeannypr.scientificstudy.R;

import java.util.ArrayList;

public class AutoCompleteDropDownAdapter extends ArrayAdapter<DropDownModel> {

    // Your sent context
    private Context context;
    // Your custom values for the spinner (User)
    public ArrayList<DropDownModel> originalData, filteredData;
    private CustomFilter customFilter = new CustomFilter();
    AutoCompleteDropDownAdapter.OnSelectListner listner;

    public AutoCompleteDropDownAdapter(Context context, int textViewResourceId,
                                       ArrayList<DropDownModel> values, AutoCompleteDropDownAdapter.OnSelectListner listner) {
        super(context, textViewResourceId, values);
        this.context = context;
        this.originalData = values;
        this.filteredData = values;
        this.listner = listner;
    }

    @Override
    public int getCount() {
        return filteredData.size();
    }

    @Override
    public DropDownModel getItem(int position) {
        return filteredData.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    // And the "magic" goes here
    // This is for the "passive" state of the spinner
    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        // we can reference our own  custom layout for each spinner item
        TextView label = (TextView) super.getView(position, convertView, parent);
        label.setTextColor(context.getResources().getColor(R.color.black9));
        // Then you can get the current item using the values array (Users array) and the current position
        // You can NOW reference each method you has created in your bean object (User class)
        label.setText(filteredData.get(position).getText());

        // And finally return your dynamic (or custom) view for each spinner item
        label.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Log.i("click on label:","clicked");
            }
        });
        return label;
    }

    // And here is when the "chooser" is popped up
    // Normally is the same view, but you can customize it if you want
    @Override
    public View getDropDownView(int position, View convertView,
                                ViewGroup parent) {

        // if (convertView == null) {
        TextView label = (TextView) super.getDropDownView(position, convertView, parent);
        label.setTextColor(Color.BLACK);
        /* label.setText(filteredData.get(position).getText());*/
        label.setText(getItem(position).getText());
        //  }
        return label;
    }

    @NonNull
    @Override
    public Filter getFilter() {
        return customFilter;
    }

    public class CustomFilter extends Filter {

        @Override
        protected FilterResults performFiltering(CharSequence constraint) {
            FilterResults results = new FilterResults();

            if (constraint == null || constraint.length() == 0) {
                results.count = filteredData.size();
                results.values = filteredData;

            } else {
                ArrayList<DropDownModel> temp = new ArrayList<>();

                for (DropDownModel value : originalData) {
                    if (value.getText().toLowerCase().contains(constraint.toString().toLowerCase())) {
                        temp.add(value);
                    }
                }
                results.values = temp;
                results.count = temp.size();
            }

            return results;
        }

        @Override
        protected void publishResults(CharSequence constraint, FilterResults results) {
            filteredData = (ArrayList) results.values;
            notifyDataSetChanged();
           // listner.OnSelect(filteredData.get(0));
        }

        @Override
        public CharSequence convertResultToString(Object resultValue) {
            return ((DropDownModel) resultValue).getText();
        }
    }

    public static interface OnSelectListner {
        void OnSelect(DropDownModel selectedItem);
    }
}


