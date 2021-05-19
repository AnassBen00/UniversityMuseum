package fr.uavignon.cerimuseum;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import fr.uavignon.cerimuseum.data.MuseumRepository;
import io.github.luizgrp.sectionedrecyclerviewadapter.SectionedRecyclerViewAdapter;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.TreeMap;

import fr.uavignon.cerimuseum.data.Object;


public class CategoriesFragment extends Fragment {

    private RecyclerView recyclerView;
    private SectionedRecyclerViewAdapter  sectionAdapter;
    private CategoriesViewModel viewModel;
    private ListViewModel listViewModel;
    private List<Object> listObjects;
    private Map<String, List<Object>> objts = new TreeMap<>();


    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_categories, container, false);

        recyclerView = view.findViewById(R.id.categoriesRecyclerView);
        recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));


        sectionAdapter = new SectionedRecyclerViewAdapter();
        recyclerView.setAdapter(sectionAdapter);

        return view;
    }

    public void onViewCreated(@NonNull View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        viewModel = new ViewModelProvider(this).get(CategoriesViewModel.class);
        listViewModel = new ViewModelProvider(this).get(ListViewModel.class);

        refreshSectionAdapter();

    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    void refreshSectionAdapter() {
        sectionAdapter = new SectionedRecyclerViewAdapter();

        listViewModel.getAllObjects().observe(getViewLifecycleOwner(), new Observer<List<Object>>() {
            @Override
            public void onChanged(List<Object> objects) {
                listObjects = objects;

                viewModel.getAllCategories().observe(getViewLifecycleOwner(), new Observer<List<fr.uavignon.cerimuseum.data.Category>>() {
                    @Override
                    public void onChanged(List<fr.uavignon.cerimuseum.data.Category> categories) {

                        for (fr.uavignon.cerimuseum.data.Category c : categories) {
                            objts.put(c.getName(),null);
                        }

                        for (Object o : listObjects) {
                            if (!o.getCategory().isEmpty()) {
                                for (fr.uavignon.cerimuseum.data.Category category : categories) {
                                    if (o.getCategory().contains(category.getName())) {
                                        List<Object> objs = new ArrayList<>();
                                        objs.add(o);
                                        objts.put(category.getName(),objs);
                                        break;
                                    }
                                }
                            }
                        }
                        for(Map.Entry<String, List<Object>> entry : objts.entrySet()) {
                            sectionAdapter.addSection(new Category(getContext(), entry.getKey(), entry.getValue()));
                        }
                    }
                });
            }
        });

        // Sections

        sectionAdapter.notifyDataSetChanged();
        recyclerView.setAdapter(sectionAdapter);
    }
}
