package fr.uavignon.cerimuseum;

import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.view.View;

import androidx.core.content.ContextCompat;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;

import java.util.Arrays;
import java.util.List;

import fr.uavignon.cerimuseum.data.Object;
import io.github.luizgrp.sectionedrecyclerviewadapter.Section;
import io.github.luizgrp.sectionedrecyclerviewadapter.SectionParameters;

class Category extends Section {

    private Context context;
    private String title;
    private List<Object> objects;

    Category(Context context, String title, List<Object> objects) {
        // call constructor with layout resources for this Section header and items
        super(SectionParameters.builder()
                .itemResourceId(R.layout.list_items)
                .headerResourceId(R.layout.header_section)
                .build());

        this.context = context;
        this.title = title;
        this.objects = objects;
    }

    @Override
    public int getContentItemsTotal() {
        return objects.size(); // number of items of this section
    }

    @Override
    public RecyclerAdapter.CustomItemHolder getItemViewHolder(View view) {
        // return a custom instance of ViewHolder for the items of this section
        return new RecyclerAdapter.CustomItemHolder(view);
    }

    @Override
    public void onBindItemViewHolder(RecyclerView.ViewHolder holder, int position) {
        RecyclerAdapter.CustomItemHolder itemHolder = (RecyclerAdapter.CustomItemHolder) holder;

        // bind your view here
        Object museumObject = objects.get(position);

        // Thumbnail
        if (museumObject.getPictures() != null){
            List<String> picsArray = Arrays.asList(museumObject.getPictures().split(","));
            Glide.with(itemHolder.itemView)
                    .load(Uri.parse("https://demo-lia.univ-avignon.fr/cerimuseum/items/"+museumObject.getId()+"/images/"+picsArray.get(0)))
                    .into(itemHolder.thumbnail);
        }

        // Name
        itemHolder.name.setText(museumObject.getName());

        // Brand
        itemHolder.brand.setText(museumObject.getBrand());
        if (itemHolder.brand.getText().equals(""))
            itemHolder.brand.setHeight(0);
        else
            itemHolder.brand.setMinHeight(40);

        // Time frame
        if (museumObject.getYear() != null) {
            itemHolder.timeFrame.setText(museumObject.getYear().toString());
        }else{
            itemHolder.timeFrame.setText("Not Provided");
        }

        // Categories

        itemHolder.categories.setText(museumObject.getCategory());

        // Colors
        int color = 0;
        switch (position % 2) {
            case 0: color = R.color.white;
                break;
            case 1: color = R.color.teal_200;
                break;
        }
        itemHolder.container.setBackgroundColor(ContextCompat.getColor(context, color));

        itemHolder.container.setOnClickListener((View v) -> {
            Intent intent = new Intent(context, MainActivity.class);
            //intent.putExtra(Object.TAG, museumObject.getId());
            context.startActivity(intent);
        });
    }

    @Override
    public RecyclerView.ViewHolder getHeaderViewHolder(View view) {
        // return an empty instance of ViewHolder for the headers of this section
        return new RecyclerAdapter.CustomHeaderHolder(view);
    }

    @Override
    public void onBindHeaderViewHolder(RecyclerView.ViewHolder holder) {
        RecyclerAdapter.CustomHeaderHolder headerHolder = (RecyclerAdapter.CustomHeaderHolder) holder;

        // bind your header view here
        headerHolder.tvTitle.setText(title);
    }
}