package com.example.alertmeapp.fragments.duplicate.list;

import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.alertmeapp.R;
import com.example.alertmeapp.fragments.alert.list.AlertItem;
import com.example.alertmeapp.utils.LoggedInUser;

import java.util.ArrayList;
import java.util.List;

public class DuplicateFragment extends Fragment {

    private RecyclerView recyclerView;
    private DuplicateViewAdapter adapter;
    private List<AlertItem> items = new ArrayList<>();

    private String alertName;
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
        View view = inflater.inflate(R.layout.fragment_duplicate_list, container, false);
        Context context = view.getContext();

        Button buttonPhoto = view.findViewById(R.id.duplicate_button);
        buttonPhoto.setOnClickListener(this::onDuplicateButtonClick);

        Bundle extras = this.getArguments();
        this.latitude = extras.getDouble("latitude");
        this.alertName = extras.getString("alertName");
        this.longitude = extras.getDouble("longitude");

        recyclerView = (RecyclerView) view.findViewById(R.id.list2);
        recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));

        adapter = new DuplicateViewAdapter(getActivity(), items);
        recyclerView.setAdapter(adapter);

        new AlertDuplicateContent(recyclerView, adapter, items, alertName, longitude, latitude, getActivity());
        return view;
    }

    private void onDuplicateButtonClick(View view) {
        SharedPreferences sharedPref = getActivity().getSharedPreferences(
                getString(R.string.shared_preferences), Context.MODE_PRIVATE);
        String alertDuplicateId = sharedPref.getString("alertDuplicateId", null);
        if (alertDuplicateId == null) {
            System.out.println("jestem nullem PROBLEMMMM");
        }else{
            getActivity().getSupportFragmentManager().popBackStack();
        }
    }
}
