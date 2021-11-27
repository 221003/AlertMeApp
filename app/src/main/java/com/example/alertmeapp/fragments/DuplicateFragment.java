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

    public DuplicateFragment() {
        Bundle extras = getActivity().getIntent().getExtras();
        this.alertTypeId = extras.getLong("alertTypeId");
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_duplicate, container, false);
        Context context = view.getContext();
//        categorySpinner = view.findViewById(R.id.alert_form_category);
//        categorySpinner.setSelected(false);
//        categorySpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
//            @Override
//            public void onItemSelected(AdapterView<?> parentView, View selectedItemView, int position, long id) {
//                if (spinnerFirstTrigger) {
//                    String category = categorySpinner.getSelectedItem().toString();
//                    System.out.println("wchodze do psinner onitemseleceted");
//                    if (!category.equals("all")) {
//                        List<AlertItem> copyList = new ArrayList<>();
//                        for (int i = 0; i < items.size(); i++) {
//                            if (items.get(i).getAlert().getAlertType().getName().equals(category)) {
//                                copyList.add(items.get(i));
//                            }
//                        }
//                        copyList.sort(new DistanceComparator());
//                        adapter = new MyListRecyclerViewAdapter(getActivity(), copyList);
//                    } else {
//                        adapter = new MyListRecyclerViewAdapter(getActivity(), items);
//                    }
//                    recyclerView.setAdapter(adapter);
//                } else {
//                    spinnerFirstTrigger = true;
//                }
//            }
//
//            @Override
//            public void onNothingSelected(AdapterView<?> parentView) {
//            }
//
//        });
//        populateCategorySpinner();

        recyclerView = (RecyclerView) view.findViewById(R.id.list);
        recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));

        adapter = new MyListRecyclerViewAdapter(getActivity(), items);
        recyclerView.setAdapter(adapter);

        new AlertContent(recyclerView, adapter, items);


//        recyclerView.addOnItemTouchListener(
//                new RecyclerItemClickListener(context, recyclerView, new RecyclerItemClickListener.OnItemClickListener() {
//                    @Override
//                    public void onItemClick(View view, int position) {
//                        Bundle bundle = new Bundle();
//                        bundle.putLong("alertId", items.get(position).getAlert().getId());
//                        NavController navController = Navigation.findNavController(getActivity(), R.id.fragmentController);
//                        navController.navigate(R.id.alertDetailsFragment, bundle);
//                    }
//
//                    @Override
//                    public void onLongItemClick(View view, int position) {
//
//                    }
//                })
//        );


        return view;
    }
}
