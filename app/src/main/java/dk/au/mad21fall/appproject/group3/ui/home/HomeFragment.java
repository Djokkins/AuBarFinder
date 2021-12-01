package dk.au.mad21fall.appproject.group3.ui.home;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.DividerItemDecoration;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import java.util.ArrayList;

import dk.au.mad21fall.appproject.group3.Models.Bar;
import dk.au.mad21fall.appproject.group3.Other.BarAdapter;
import dk.au.mad21fall.appproject.group3.Models.Constants;
import dk.au.mad21fall.appproject.group3.Activities.DetailsActivity;
import dk.au.mad21fall.appproject.group3.R;
import dk.au.mad21fall.appproject.group3.databinding.FragmentHomeBinding;

public class HomeFragment extends Fragment implements BarAdapter.IBarItemClickedListener{

    private static final String TAG = "1";

    private HomeViewModel homeViewModel;
    private FragmentHomeBinding binding;
    private RecyclerView rcvList;
    private BarAdapter adapter;
    private LiveData<ArrayList<Bar>> bars;


    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {
        homeViewModel = new ViewModelProvider(this).get(HomeViewModel.class);
        Log.d(TAG, "onCreateView: Checkpoint 0");
        binding = FragmentHomeBinding.inflate(inflater, container, false);

        adapter = new BarAdapter(this);
        View v = inflater.inflate(R.layout.fragment_home, container, false);
        rcvList = v.findViewById(R.id.rcvBars);
        rcvList.setLayoutManager(new LinearLayoutManager(getContext()));
        rcvList.setAdapter(adapter);

        //To make a list between each element in the list, for prettyness
        DividerItemDecoration itemDecor = new DividerItemDecoration(rcvList.getContext(), LinearLayout.VERTICAL);
        rcvList.addItemDecoration(itemDecor);


        bars = homeViewModel.getBars();
        bars.observe(this.getViewLifecycleOwner(), new Observer<ArrayList<Bar>>() {
            @Override
            public void onChanged(ArrayList<Bar> bars) {
                adapter.updateBarList(bars);
            }
        });

        return v;
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        binding = null;
    }

    @Override
    public void onBarClicked(int index) {
        Bar bar = bars.getValue().get(index);
        //Toast.makeText(getActivity(), "You clicked bar" + bar.getName(), Toast.LENGTH_SHORT).show();
        String id = bars.getValue().get(index).getName();
        Intent intent = new Intent(getActivity(), DetailsActivity.class);
        Log.d(TAG, "onBarClicked: " + id);
        intent.putExtra(Constants.BAR_NAME, id);
        startActivity(intent);

    }
}