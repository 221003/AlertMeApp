package com.example.alertmeapp.fragments;

import android.content.Context;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.Button;

import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.alertmeapp.R;
import com.example.alertmeapp.fragments.alert.list.AlertContent;
import com.example.alertmeapp.fragments.alert.list.AlertItem;
import com.example.alertmeapp.fragments.alert.list.MyListRecyclerViewAdapter;
import com.example.alertmeapp.utils.DistanceComparator;

import java.util.ArrayList;
import java.util.List;

public class DuplicateFragment extends Fragment {

    private RecyclerView recyclerView;
    private MyListRecyclerViewAdapter adapter;
    private List<AlertItem> items = new ArrayList<>();

    private Long alertTypeId;
    private Double longitude;
    private Double latitude;

    public DuplicateFragment() {
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_duplicate, container, false);
        Context context = view.getContext();

        Bundle extras = this.getArguments();
        this.latitude = extras.getDouble("latitude");
        this.alertTypeId = extras.getLong("alertTypeId");
        this.longitude = extras.getDouble("longitude");

        recyclerView = (RecyclerView) view.findViewById(R.id.list);
        recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));

        adapter = new MyListRecyclerViewAdapter(getActivity(), items);
        recyclerView.setAdapter(adapter);

        new AlertContent(recyclerView, adapter, items, alertTypeId, longitude, latitude, getActivity());
        return view;
    }
}
