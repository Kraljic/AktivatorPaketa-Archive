package com.example.aktivatorpaketa;

import android.content.Context;
import android.content.Intent;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageButton;
import android.widget.TextView;

import java.util.List;

public class OptionListViewAdapter extends ArrayAdapter<Option> {
    private int layoutResource;

    public OptionListViewAdapter(Context context, int layoutResource, List<Option> optionList) {
        super(context, layoutResource, optionList);
        this.layoutResource = layoutResource;
    }

    @Override
    public View getView(int position, View convertView, final ViewGroup parent) {
        View view = convertView;

        if (view == null) {
            LayoutInflater layoutInflater = LayoutInflater.from(getContext());
            view = layoutInflater.inflate(layoutResource, null);
        }

        final Option option = getItem(position);

        if (option != null) {
            TextView titleTextView = (TextView) view.findViewById(R.id.titleTextView);
            TextView descriptionTextView = (TextView) view.findViewById(R.id.descriptionTextView);
            ImageButton activateButton = (ImageButton) view.findViewById(R.id.activateImageButton);
            ImageButton editButton = (ImageButton) view.findViewById(R.id.editImageButton);
            ImageButton deleteButton = (ImageButton) view.findViewById(R.id.deleteImageButton);

            if (titleTextView != null) {
                titleTextView.setText(option.getTitle());
            }
            if (descriptionTextView != null) {
                descriptionTextView.setText(option.getDescription());
            }

            // Activate option call
            activateButton.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Log.i("", "Activate: " + option.getId());
                    MainActivity.mainActivity.activateOption(option);
                }
            });

            // Edit option call
            editButton.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Log.i("", "Edit: " + option.getId());
                    MainActivity.mainActivity.editOption(option);
                }
            });

            // Delete option call
            deleteButton.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Log.i("", "Delete: " + option.getId());
                    MainActivity.mainActivity.deleteOption(option);
                }
            });
        }

        return view;
    }
}
