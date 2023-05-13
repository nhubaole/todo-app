package com.example.todoapp;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.CheckBox;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import java.util.List;

public class TodoAdapter extends ArrayAdapter<Todo> {

    private int resource;
    private List<Todo> todos;

    public TodoAdapter(@NonNull Context context, int resource, @NonNull List<Todo> objects) {
        super(context, resource, objects);
        this.resource = resource;
        this.todos = objects;
    }

    @NonNull
    @Override
    public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {
        View v = convertView;
        if (v==null){
            LayoutInflater vi;
            vi = LayoutInflater.from(this.getContext());
            v = vi.inflate(this.resource, null);
        }
        Todo t = getItem(position);
        TextView seqTv = (TextView) v.findViewById(R.id.seqTv);
        TextView titleTv = (TextView) v.findViewById(R.id.titleTv);
        TextView dateTV = (TextView) v.findViewById(R.id.dateTv);
        CheckBox checkBox = (CheckBox) v.findViewById(R.id.checkBox);

        if(seqTv != null){
            int seq = position + 1;
            seqTv.setText("#" + Integer.toString(seq));
        }
        if(titleTv != null){
            titleTv.setText(t.getTitle());
        }
        if(dateTV != null){
            dateTV.setText(t.getDate());
        }
        if(checkBox != null){
            checkBox.setChecked(t.isDone());
        }
        return v;
    }
}
