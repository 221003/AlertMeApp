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

import com.example.alertmeapp.R;
import com.example.alertmeapp.api.data.AlertType;
import com.example.alertmeapp.api.responses.ResponseMultipleData;
import com.example.alertmeapp.api.retrofit.AlertMeService;
import com.example.alertmeapp.api.retrofit.RestAdapter;
import com.example.alertmeapp.utils.DistanceComparator;

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
    protected MyListRecyclerViewAdapter adapter;

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
                    String category = categorySpinner.getSelectedItem().toString();
                    System.out.println("wchodze do psinner onitemseleceted");
                    if (!category.equals("all")) {
                        List<AlertItem> copyList = new ArrayList<>();
                        for (int i = 0; i < items.size(); i++) {
                            if (items.get(i).getAlert().getAlertType().getName().equals(category)) {
                                copyList.add(items.get(i));
                            }
                        }
                        copyList.sort(new DistanceComparator());
                        adapter = new MyListRecyclerViewAdapter(getActivity(), copyList);
                    } else {
                        adapter = new MyListRecyclerViewAdapter(getActivity(), items);
                    }
                    recyclerView.setAdapter(adapter);
                } else {
                    spinnerFirstTrigger = true;
                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> parentView) {
            }

        });
        populateCategorySpinner();

        recyclerView = (RecyclerView) view.findViewById(R.id.list);
        recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));

        adapter = new MyListRecyclerViewAdapter(getActivity(), items);
        recyclerView.setAdapter(adapter);

        new AlertContent(recyclerView, adapter, items, myAlerts);

        return view;
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