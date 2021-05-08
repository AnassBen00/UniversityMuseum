package fr.uavignon.cerimuseum;

import android.annotation.SuppressLint;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ProgressBar;
import android.widget.SearchView;
import android.widget.Spinner;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;
import androidx.navigation.Navigation;
import androidx.navigation.fragment.NavHostFragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;
import java.util.List;


public class ListFragment extends Fragment {

    public static final String TAG = ListFragment.class.getSimpleName();

    private ListViewModel viewModel;

    private RecyclerView recyclerView;
    private RecyclerView.LayoutManager layoutManager;
    private RecyclerAdapter adapter;
    private Spinner spinnerSort;
    private ProgressBar progress;
    private View view;


    @Override
    public View onCreateView(
            LayoutInflater inflater, ViewGroup container,
            Bundle savedInstanceState
    ) {
        setHasOptionsMenu(true);
        view = inflater.inflate(R.layout.fragment_list, container, false);

        spinnerSort = view.findViewById(R.id.spinnerSortBy);
        initspinnerfooter();

        return view;
    }




    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater menuInflater) {
        // Inflate the menu; this adds items to the action bar if it is present.
        menuInflater.inflate(R.menu.menu_main, menu);

        MenuItem searchItem = menu.findItem(R.id.actionSearch);
        SearchView searchView = (SearchView) searchItem.getActionView();

        searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String query) {

                return true;
            }

            @Override
            public boolean onQueryTextChange(String newText) {
                adapter.filterList(newText);
                return false;
            }
        });

      super.onCreateOptionsMenu(menu, menuInflater);
    }


    private void initspinnerfooter() {

        ArrayAdapter<String> arrayAdapteradapter = new ArrayAdapter<String>(getActivity(), android.R.layout.simple_spinner_item, getResources().getStringArray(R.array.SortByList));
        spinnerSort.setAdapter(arrayAdapteradapter);
        spinnerSort.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                String item = parent.getItemAtPosition(position).toString();

                switch (item) {
                    case "Name (A → Z)":
                        viewModel.getAllObjects().observe(getViewLifecycleOwner(),
                                objects -> adapter.setObjetList(objects));
                        break;
                    case "Name (Z → A)":
                        viewModel.getObjects().observe(getViewLifecycleOwner(),
                                objects -> adapter.setObjetList(objects));
                        break;
                    case "Time frame (newest)":
                        viewModel.getNewestObjects().observe(getViewLifecycleOwner(),
                                objects -> adapter.setObjetList(objects));
                        break;
                    case "Time frame (oldest)":
                        viewModel.getOldestObjects().observe(getViewLifecycleOwner(),
                                objects -> adapter.setObjetList(objects));
                        break;
                    case "By category":
                        //DataManager.sortByNewest();
                        break;
                }

                adapter.notifyDataSetChanged();

                Toast.makeText(parent.getContext(), "Sorted by: " + item, Toast.LENGTH_SHORT).show();
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {
                // TODO Auto-generated method stub
            }
        });
    }

    public void onViewCreated(@NonNull View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        viewModel = new ViewModelProvider(this).get(ListViewModel.class);

        observerSetup();
        listenerSetup();
    }

    private void listenerSetup() {
        recyclerView = getView().findViewById(R.id.recyclerView);
        layoutManager = new LinearLayoutManager(getActivity());
        recyclerView.setLayoutManager(layoutManager);
        adapter = new RecyclerAdapter();
        recyclerView.setAdapter(adapter);
        adapter.setListViewModel(viewModel);
        progress = view.findViewById(R.id.progressList);
    }

    private void observerSetup() {
        viewModel.getAllObjects().observe(getViewLifecycleOwner(),
                objects -> adapter.setObjetList(objects));
    }
}