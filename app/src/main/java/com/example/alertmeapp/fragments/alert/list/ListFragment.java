package com.example.alertmeapp.fragments.alert.list;

import android.content.Context;
import android.location.Location;
import android.os.AsyncTask;
import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Spinner;

import com.example.alertmeapp.R;
import com.example.alertmeapp.api.data.Alert;
import com.example.alertmeapp.api.data.AlertType;
import com.example.alertmeapp.api.responses.ResponseMultipleData;
import com.example.alertmeapp.api.retrofit.AlertMeService;
import com.example.alertmeapp.api.retrofit.RestAdapter;
import com.example.alertmeapp.utils.DistanceComparator;
import com.example.alertmeapp.utils.LoggedInUser;

import java.net.URL;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.concurrent.ExecutionException;

import okio.AsyncTimeout;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;


public class ListFragment extends Fragment {

    private static final String ARG_COLUMN_COUNT = "column-count";
    private int mColumnCount = 1;

    private RecyclerView recyclerView;
    private List<AlertItem> items = new ArrayList<>();
    private MyListRecyclerViewAdapter adapter;

    private Spinner categorySpinner;
    private final AlertMeService service = RestAdapter.getAlertMeService();

    public ListFragment() {
    }

//    @SuppressWarnings("unused")
//    public static ListFragment newInstance(int columnCount) {
//        ListFragment fragment = new ListFragment();
//        Bundle args = new Bundle();
//        args.putInt(ARG_COLUMN_COUNT, columnCount);
//        fragment.setArguments(args);
//        return fragment;
//    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            mColumnCount = getArguments().getInt(ARG_COLUMN_COUNT);
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_item_list, container, false);
        categorySpinner = view.findViewById(R.id.alert_form_category);
        categorySpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parentView, View selectedItemView, int position, long id) {
                String category = categorySpinner.getSelectedItem().toString();
                if (!category.equals("none")) {
                    List<AlertItem> copyList = new ArrayList<>();
                    for (int i = 0; i < items.size(); i++) {
                        if (items.get(i).getAlert().getAlertType().getName().equals(category)) {
                            copyList.add(items.get(i));
                            System.out.println(items.get(i).getAlert().toString());
                        }
                    }
                    copyList.sort(new DistanceComparator());
                    adapter = new MyListRecyclerViewAdapter(copyList);
                } else {
                    adapter = new MyListRecyclerViewAdapter(items);
                }
                recyclerView.setAdapter(adapter);
            }

            @Override
            public void onNothingSelected(AdapterView<?> parentView) {
            }

        });
        populateCategorySpinner();

        recyclerView = (RecyclerView) view.findViewById(R.id.list);
        recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));

        adapter = new MyListRecyclerViewAdapter(items);
        recyclerView.setAdapter(adapter);
        new AlertContent(recyclerView, adapter, items);


        return view;
    }

    private void populateCategorySpinner() {
        List<String> categories = new ArrayList<>(5);

        Call<ResponseMultipleData<AlertType>> allAlertTypes = service.getAllAlertTypes();
        allAlertTypes.enqueue(new Callback<ResponseMultipleData<AlertType>>() {
            @Override
            public void onResponse(Call<ResponseMultipleData<AlertType>> call, Response<ResponseMultipleData<AlertType>> response) {
                categories.add("none");
                for (AlertType alertTypeRequest : response.body().getData()) {
                    categories.add(alertTypeRequest.getName());
                }
                ArrayAdapter<String> adapter = null;
                try {
                    adapter = new ArrayAdapter<>(getActivity(), android.R.layout.simple_spinner_dropdown_item, categories);
                } catch (Exception e) {
                    e.printStackTrace();
                }
                categorySpinner.setAdapter(adapter);
            }

            @Override
            public void onFailure(Call<ResponseMultipleData<AlertType>> call, Throwable t) {
                ArrayAdapter<String> adapter = null;
                try {
                    adapter = new ArrayAdapter<>(getActivity(), android.R.layout.simple_spinner_dropdown_item, new String[0]);
                } catch (Exception e) {
                    e.printStackTrace();
                }
                categorySpinner.setAdapter(adapter);
            }
        });
    }
}