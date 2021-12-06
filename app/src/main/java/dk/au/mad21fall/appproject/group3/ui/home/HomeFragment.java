package dk.au.mad21fall.appproject.group3.ui.home;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.location.Criteria;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.Bundle;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.SubMenu;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.TextView;
import androidx.annotation.NonNull;
import androidx.appcompat.widget.SearchView;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.DividerItemDecoration;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

//import com.google.android.material.navigation.NavigationView;

import com.facebook.login.Login;
import com.google.firebase.auth.FirebaseAuth;

import java.util.ArrayList;

import dk.au.mad21fall.appproject.group3.Models.Bar;
import dk.au.mad21fall.appproject.group3.Models.UserLocation;
import dk.au.mad21fall.appproject.group3.Other.BarAdapter;
import dk.au.mad21fall.appproject.group3.Models.Constants;
import dk.au.mad21fall.appproject.group3.Activities.DetailsActivity;
import dk.au.mad21fall.appproject.group3.R;

public class HomeFragment extends Fragment implements BarAdapter.IBarItemClickedListener{

    private static final String TAG = "1";

    private HomeViewModel homeViewModel;
    private RecyclerView rcvList;
    private BarAdapter adapter;
    private LiveData<ArrayList<Bar>> bars;
    private SearchView srcBar;
    private ImageView filterBtn;
    private DrawerLayout filterDrawer;
    private FirebaseAuth mAuth;
    private Boolean mOpenBarsOnly = false;
    private Boolean mMyRatedBarsOnly = false;


    private boolean filterTest = true;


    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {
        homeViewModel = new ViewModelProvider(this).get(HomeViewModel.class);
        setHasOptionsMenu(true);

        adapter = new BarAdapter(this);
        View v = inflater.inflate(R.layout.fragment_home, container, false);

        mAuth = FirebaseAuth.getInstance();
        if(mAuth.getCurrentUser() == null){
            gotoLogin();
        }
        Log.d(TAG, "onCreateView: UserID = " + mAuth.getCurrentUser().getUid());
        filterDrawer = v.findViewById(R.id.drawerlayout);
        srcBar = (SearchView) v.findViewById(R.id.srcBars);
        rcvList = v.findViewById(R.id.rcvBars);
        rcvList.setLayoutManager(new LinearLayoutManager(getContext()));
        rcvList.setAdapter(adapter);
        filterBtn = v.findViewById(R.id.filterDrawerBtn);
        //To make a list between each element in the list, for prettiness
        DividerItemDecoration itemDecor = new DividerItemDecoration(rcvList.getContext(), LinearLayout.VERTICAL);
        rcvList.addItemDecoration(itemDecor);


        bars = homeViewModel.getBars();
        bars.observe(this.getViewLifecycleOwner(), new Observer<ArrayList<Bar>>() {
            @Override
            public void onChanged(ArrayList<Bar> bars) {
                adapter.updateBarList(bars);
            }
        });
        Log.d(TAG, "onCreateView: " + v.findViewById(R.id.srcBars));

        filterBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                OnClickFilterDrawer();
            }
        });


        srcBar.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String query) {
                return false;
            }

            @Override
            public boolean onQueryTextChange(String newText) {
                adapter.getFilter().filter(newText);
                return false;
            }
        });

        return v;
    }

    private void gotoLogin() {
        Intent i = new Intent(getActivity(), Login.class);
        startActivity(i);
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
    }

    @Override
    public void onBarClicked(int index) {
        String id = bars.getValue().get(index).getName();
        Intent intent = new Intent(getActivity(), DetailsActivity.class);
        Log.d(TAG, "onBarClicked: " + id);
        intent.putExtra(Constants.BAR_NAME, id);
        startActivity(intent);
    }

    public void OnClickFilterDrawer()
    {
        //filterDrawer.openDrawer(Gravity.RIGHT);

        //adapter.sortAlphabetically();
        //adapter.sortByOpen(filterTest);
        //adapter.sortByUserRated((filterTest));
        //filterTest = !filterTest;
        adapter.sortByDistance();
    }



    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        inflater.inflate(R.menu.nav_drawer_menu, menu);
        super.onCreateOptionsMenu(menu, inflater);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        Log.d(TAG, "onOptionsItemSelected: loaded");
        int id = item.getItemId();
        if (id == R.id.isopenchbx) {
            mMyRatedBarsOnly = false;
            mOpenBarsOnly ^= true;
            adapter.sortByOpen(mOpenBarsOnly);
            if(mOpenBarsOnly){
                mMyRatedBarsOnly = false;
                item.setChecked(true);
            }
            else {item.setChecked(false);}
            return true;
        }
        if (id == R.id.sortmyrateditems) {

            mMyRatedBarsOnly ^= true;
            adapter.sortByOpen(mMyRatedBarsOnly);
            if(mMyRatedBarsOnly){
                mOpenBarsOnly = false;
                item.setChecked(true);
            }
            else {item.setChecked(false);}
            return true;
        }
        if (id == R.id.sortalphabetical) {
            mOpenBarsOnly = false;
            mMyRatedBarsOnly = false;
            adapter.sortAlphabetically();
            Log.d(TAG, "onOptionsItemSelected: YESYEYSYESYES");
            return true;
        }
        if (id == R.id.sortratingitem) {
            mOpenBarsOnly = false;
            mMyRatedBarsOnly = false;
            adapter.sortByRating();
            return true;
        }
        if (id == R.id.sortdistanceitem) {
            mOpenBarsOnly = false;
            mMyRatedBarsOnly = false;
            adapter.sortByDistance();
            return true;
        }
        return super.onOptionsItemSelected(item);
    }

}
