package com.example.alertmeapp.dummy;

import androidx.recyclerview.widget.RecyclerView;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.example.alertmeapp.R;
import com.example.alertmeapp.dummy.AlertContent.AlertItem;

import java.util.List;

/**
 * {@link RecyclerView.Adapter} that can display a {@link AlertItem}.
 * TODO: Replace the implementation with code for your data type.
 */
public class MyListRecyclerViewAdapter extends RecyclerView.Adapter<MyListRecyclerViewAdapter.ViewHolder> {

    private final List<AlertItem> alertList;

    public MyListRecyclerViewAdapter(List<AlertItem> items) {
        alertList = items;
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.fragment_list, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(final ViewHolder holder, int position) {
        holder.titleView.setText(alertList.get(position).title);
        holder.typeView.setText(alertList.get(position).alertType);
        holder.rangeView.setText(alertList.get(position).distance);
    }

    @Override
    public int getItemCount() {
        return alertList.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        public final View mView;
        public final TextView titleView;
        public final TextView typeView;
        public final TextView rangeView;

        public ViewHolder(View view) {
            super(view);
            mView = view;
            titleView = (TextView) view.findViewById(R.id.titleTextView);
            typeView = (TextView) view.findViewById(R.id.typeTextView);
            rangeView = (TextView) view.findViewById(R.id.rangeTextView);
        }

    }
}