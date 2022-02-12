package com.example.alertmeapp.fragments.duplicate.list;

import static com.example.alertmeapp.fragments.duplicate.list.AlertDuplicateContent.RANGE_OF_DUPLICATE_ALERTS_IN_METERS;

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
import com.example.alertmeapp.api.data.Alert;
import com.example.alertmeapp.api.responses.ResponseMultipleData;
import com.example.alertmeapp.api.retrofit.AlertMeService;
import com.example.alertmeapp.api.retrofit.RestAdapter;
import com.example.alertmeapp.fragments.alert.AlertItem;
import com.example.alertmeapp.utils.DistanceCalculatorFromUser;
import com.example.alertmeapp.utils.DistanceComparator;
import com.example.alertmeapp.utils.LoggedInUser;

import java.util.ArrayList;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

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

//    private void getAlertWithCategoryAndFillViewWithIt( String alertName) {
//        AlertMeService service = RestAdapter.getAlertMeService();
//        Call<ResponseMultipleData<Alert>> allAlerts = service.getAlertByDistance(latitude, longitude, Double.valueOf(RANGE_OF_DUPLICATE_ALERTS_IN_METERS));
//        allAlerts.enqueue(new Callback<ResponseMultipleData<Alert>>() {
//
//            @Override
//            public void onResponse(Call<ResponseMultipleData<Alert>> call, Response<ResponseMultipleData<Alert>> response) {
//                if (response.isSuccessful()) {
//                    filterList(response, alertName);
//                    if (items.size() == 0) {
//                        System.out.println("jestem zerem ");
//                        handleNoDuplicates();
//                        getActivity().getSupportFragmentManager().popBackStack();
//                        return;
//                    }else {
//                        System.out.println("nie jestem zerem ");
//                    }
//                } else {
//                    System.out.println("Unsuccessful to fetch all alerts AlertContent.class");
//                }
//            }
//
//            @Override
//            public void onFailure(Call<ResponseMultipleData<Alert>> call, Throwable t) {
//                System.out.println("Failed to fetch all alerts AlertContent.class");
//            }
//        });
//    }

    private void onDuplicateButtonClick(View view) {
        SharedPreferences sharedPref = getActivity().getSharedPreferences(
                getString(R.string.shared_preferences), Context.MODE_PRIVATE);
        String alertDuplicateId = sharedPref.getString("alertDuplicateId", null);
        if (alertDuplicateId == null) {
            System.out.println("alert duplicate is null problem");
        } else {
            getActivity().getSupportFragmentManager().popBackStack();
        }
    }

//    private void filterList(Response<ResponseMultipleData<Alert>> response, String alertType) {
//        List<Alert> alerts = response.body().getData();
//        List<AlertItem> temp = new ArrayList<>();
//
//        alerts.stream()
//                .filter((alert -> alert.getAlertType().getName().equals(alertType)))
//                .forEach(alert -> temp.add(new AlertItem(alert, DistanceCalculatorFromUser.count(alert.getLongitude(), alert.getLatitude()))));
//
//        if (items.size() != temp.size()) {
//            items.clear();
//            items.addAll(temp);
//        }
//        items.sort(new DistanceComparator());
//    }
//    private void handleNoDuplicates() {
//        Context applicationContext = getActivity().getApplicationContext();
//        SharedPreferences sharedPref = applicationContext.getSharedPreferences(
//                applicationContext.getString(R.string.shared_preferences), Context.MODE_PRIVATE);
//
//        SharedPreferences.Editor editor = sharedPref.edit();
//        editor.putString("createAlert", "yes");
//        editor.apply();
//        getActivity().getSupportFragmentManager().popBackStack();
//    }
}
