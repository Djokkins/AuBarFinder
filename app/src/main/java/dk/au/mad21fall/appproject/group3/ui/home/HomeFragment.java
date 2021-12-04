package dk.au.mad21fall.appproject.group3.ui.home;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.SearchView;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.fragment.app.Fragment;
import androidx.legacy.app.ActionBarDrawerToggle;
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

import dk.au.mad21fall.appproject.group3.Activities.MainActivity;
import dk.au.mad21fall.appproject.group3.Models.Bar;
import dk.au.mad21fall.appproject.group3.Other.BarAdapter;
import dk.au.mad21fall.appproject.group3.Models.Constants;
import dk.au.mad21fall.appproject.group3.Activities.DetailsActivity;
import dk.au.mad21fall.appproject.group3.R;
import dk.au.mad21fall.appproject.group3.databinding.FragmentHomeBinding;

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

    //var toggle: ActionBarDrawerToggle? = null;


    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {
        homeViewModel = new ViewModelProvider(this).get(HomeViewModel.class);






        adapter = new BarAdapter(this);
        View v = inflater.inflate(R.layout.fragment_home, container, false);

        mAuth = FirebaseAuth.getInstance();
        if(mAuth.getCurrentUser() == null){
            gotoLogin();
        }
        Log.d(TAG, "onCreateView: UserID = " + mAuth.getCurrentUser().getUid());

        rcvList = v.findViewById(R.id.rcvBars);
        rcvList.setLayoutManager(new LinearLayoutManager(getContext()));
        rcvList.setAdapter(adapter);

        //To make a list between each element in the list, for prettynes    s
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

        filterBtn = v.findViewById(R.id.filterDrawerBtn);
        filterBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                OnClickFilterDrawer();
            }
        });

        filterDrawer = v.findViewById(R.id.drawerlayout);

        srcBar = (SearchView) v.findViewById(R.id.srcBars);

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
        //Toast.makeText(getActivity(), "You clicked bar" + bar.getName(), Toast.LENGTH_SHORT).show();
        String id = bars.getValue().get(index).getName();
        Intent intent = new Intent(getActivity(), DetailsActivity.class);
        Log.d(TAG, "onBarClicked: " + id);
        intent.putExtra(Constants.BAR_NAME, id);
        startActivity(intent);

    }

    public void OnClickFilterDrawer()
    {
        Log.d("BRUH", "testetsetsetse");
        filterDrawer.openDrawer(Gravity.RIGHT);
    }

}