package com.example.alertmeapp.fragments.alert;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Spinner;

import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;

import com.example.alertmeapp.R;
import com.example.alertmeapp.api.data.Alert;
import com.example.alertmeapp.api.data.AlertType;
import com.example.alertmeapp.api.responses.ResponseMultipleData;
import com.example.alertmeapp.api.retrofit.AlertMeService;
import com.example.alertmeapp.api.retrofit.RestAdapter;
import com.example.alertmeapp.utils.DistanceCalculatorFromUser;
import com.example.alertmeapp.utils.DistanceComparator;
import com.example.alertmeapp.utils.LoggedInUser;

import java.util.ArrayList;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;


public abstract class ListFragmentAbstract extends Fragment {

    protected static final String ARG_COLUMN_COUNT = "column-count";
    protected int mColumnCount = 1;
    protected boolean myAlerts = false;

    protected RecyclerView recyclerView;
    protected List<AlertItem> items = new ArrayList<>();
    protected ListRecyclerViewAdapter adapter;

    protected Spinner categorySpinner;
    protected final AlertMeService service = RestAdapter.getAlertMeService();
    protected boolean spinnerFirstTrigger = false;

    public ListFragmentAbstract() {
    }

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
        categorySpinner.setSelected(false);
        categorySpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parentView, View selectedItemView, int position, long id) {
                if (spinnerFirstTrigger) {
                    filterAlertListBasedOnCategory(categorySpinner.getSelectedItem().toString());
                } else {
                    spinnerFirstTrigger = true;
                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> parentView) {
            }

        });
        populateCategorySpinner();
        SwipeRefreshLayout refreshLayout = view.findViewById(R.id.refreshLayout);
        recyclerView = view.findViewById(R.id.list);
        recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
        getAlerts();
        refreshLayout.setOnRefreshListener(() -> {
            refreshLayout.setRefreshing(false);
            getAlerts();
        });

        return view;
    }

    private void getAlerts() {
        AlertMeService service = RestAdapter.getAlertMeService();
        Call<ResponseMultipleData<Alert>> alerts;
        if (myAlerts) {
            alerts = service.getUserAlerts(LoggedInUser.getInstance(null, null, null).getId());
        } else {
            alerts = service.getAllAlerts();
        }
        alerts.enqueue(new Callback<ResponseMultipleData<Alert>>() {

            @Override
            public void onResponse(Call<ResponseMultipleData<Alert>> call, Response<ResponseMultipleData<Alert>> response) {
                if (response.isSuccessful() && response.body() != null) {
                    List<Alert> alerts = response.body().getData();
                    items.clear();
                    alerts.forEach(alert -> {
                        items.add(new AlertItem(alert, DistanceCalculatorFromUser.count(alert.getLongitude(), alert.getLatitude())));
                    });
                    items.sort(new DistanceComparator());
                    adapter = new ListRecyclerViewAdapter(getActivity(), items);
                    recyclerView.setAdapter(adapter);
                    if (categorySpinner.getSelectedItem() != null) {
                        filterAlertListBasedOnCategory(categorySpinner.getSelectedItem().toString());
                    }

                    adapter.notifyDataSetChanged();
                } else {
                    System.out.println("Unsuccessful to fetch all alerts AlertContent.class");
                }
            }

            @Override
            public void onFailure(Call<ResponseMultipleData<Alert>> call, Throwable t) {
                System.out.println("Failed to fetch all alerts AlertContent.class");
            }
        });
    }

    private void filterAlertListBasedOnCategory(String category) {
        List<AlertItem> filteredList = items;
        if (!category.equals("all")) {
            filteredList = new ArrayList<>();
            for (int i = 0; i < items.size(); i++) {
                if (items.get(i).getAlert().getAlertType().getName().equals(category)) {
                    filteredList.add(items.get(i));
                }
            }
            filteredList.sort(new DistanceComparator());
        }
        adapter = new ListRecyclerViewAdapter(getActivity(), filteredList);
        recyclerView.setAdapter(adapter);
    }

    private void populateCategorySpinner() {
        List<String> categories = new ArrayList<>(5);

        Call<ResponseMultipleData<AlertType>> allAlertTypes = service.getAllAlertTypes();
        allAlertTypes.enqueue(new Callback<ResponseMultipleData<AlertType>>() {
            @Override
            public void onResponse(Call<ResponseMultipleData<AlertType>> call, Response<ResponseMultipleData<AlertType>> response) {
                categories.add("all");
                for (AlertType alertTypeRequest : response.body().getData()) {
                    categories.add(alertTypeRequest.getName());
                }
                ArrayAdapter<String> adapter = null;
                try {
                    adapter = new ArrayAdapter<>(getActivity(), R.layout.spinner_item_list, categories);
                } catch (Exception e) {
                    e.printStackTrace();
                }
                categorySpinner.setAdapter(adapter);
            }

            @Override
            public void onFailure(Call<ResponseMultipleData<AlertType>> call, Throwable t) {
                ArrayAdapter<String> adapter = null;
                try {
                    adapter = new ArrayAdapter<>(getActivity(), R.layout.spinner_item_list, new String[0]);
                } catch (Exception e) {
                    e.printStackTrace();
                }
                categorySpinner.setAdapter(adapter);
            }
        });
    }
}