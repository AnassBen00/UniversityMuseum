package fr.uavignon.cerimuseum;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Context;
import android.net.Uri;
import android.util.Log;
import android.view.ActionMode;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.SearchView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.navigation.Navigation;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.google.android.material.snackbar.Snackbar;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import fr.uavignon.cerimuseum.data.Object;

public class RecyclerAdapter extends RecyclerView.Adapter<fr.uavignon.cerimuseum.RecyclerAdapter.ViewHolder>{

    private static final String TAG = fr.uavignon.cerimuseum.RecyclerAdapter.class.getSimpleName();

    private List<Object> objetList;
    private ListViewModel listViewModel;

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup viewGroup, int i) {
        View v = LayoutInflater.from(viewGroup.getContext())
                .inflate(R.layout.card_layout, viewGroup, false);
        return new ViewHolder(v);
    }

    @SuppressLint("SetTextI18n")
    @Override
    public void onBindViewHolder(@NonNull ViewHolder viewHolder, int i) {
        viewHolder.itemTitle.setText(objetList.get(i).getName());
        viewHolder.itemBrand.setText(objetList.get(i).getBrand());
        viewHolder.itemCategory.setText(objetList.get(i).getCategory());

        if (objetList.get(i).getYear() != null) {
            viewHolder.itemYear.setText(objetList.get(i).getYear().toString());
        }else{
            viewHolder.itemYear.setText("Not Provided");
        }

        if (objetList.get(i).getPictures() != null){
            List<String> picsArray = Arrays.asList(objetList.get(i).getPictures().split(","));
            Glide.with(viewHolder.itemView)
                    .load(Uri.parse("https://demo-lia.univ-avignon.fr/cerimuseum/items/"+objetList.get(i).getId()+"/images/"+picsArray.get(0)))
                    .into(viewHolder.itemImage);
        }
    }

    @Override
    public int getItemCount() {
        return objetList == null ? 0 : objetList.size();
    }

    public void setObjetList(List<Object> objets) {
        objetList = objets;
        notifyDataSetChanged();
    }


    public void filterList(String text) {
        List<Object> search = new ArrayList<>();
        for (Object o : objetList) {
            System.out.println(o.toString());
            if (o.toString().contains(text)) {
                search.add(o);
            }
        }
        System.out.println("search size" + search.size());
        setObjetList(search);

        System.out.println("--NOTIFY THE CHANGES--");
    }

    public List<Object> getObjetList() {
        return objetList;
    }

    public void setListViewModel(ListViewModel viewModel) {
        listViewModel = viewModel;
    }

    class ViewHolder extends RecyclerView.ViewHolder {
        TextView itemTitle;
        TextView itemBrand;
        TextView itemCategory;
        TextView itemYear;
        ImageView itemImage;

        ActionMode actionMode;
        String idSelectedLongClick;

        ViewHolder(View itemView) {
            super(itemView);
            itemTitle = itemView.findViewById(R.id.item_title);
            itemBrand = itemView.findViewById(R.id.item_Brand);
            itemCategory = itemView.findViewById(R.id.item_Category);
            itemYear = itemView.findViewById(R.id.item_Year);
            itemImage = itemView.findViewById(R.id.item_image);

            ActionMode.Callback actionModeCallback = new ActionMode.Callback() {

                // Called when the action mode is created; startActionMode() was called
                @Override
                public boolean onCreateActionMode(ActionMode mode, Menu menu) {
                    // Inflate a menu resource providing context menu items
                    MenuInflater inflater = mode.getMenuInflater();
                    inflater.inflate(R.menu.context_menu, menu);
                    return true;
                }

                // Called each time the action mode is shown. Always called after onCreateActionMode, but
                // may be called multiple times if the mode is invalidated.
                @Override
                public boolean onPrepareActionMode(ActionMode mode, Menu menu) {
                    return false; // Return false if nothing is done
                }

                // Called when the user selects a contextual menu item
                @Override
                public boolean onActionItemClicked(ActionMode mode, MenuItem item) {
                    return false;
                }

                // Called when the user exits the action mode
                @Override
                public void onDestroyActionMode(ActionMode mode) {
                    actionMode = null;
                }
            };

            itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Log.d(TAG, "position=" + getAdapterPosition());
                    long id = RecyclerAdapter.this.objetList.get((int) getAdapterPosition()).getId_num();
                    Log.d(TAG, "id=" + id);

                    ListFragmentDirections.ActionListFragmentToDetailFragment action = ListFragmentDirections.actionListFragmentToDetailFragment();
                    action.setObjectId(id);
                    Navigation.findNavController(v).navigate(action);

                }
            });


            itemView.setOnLongClickListener(new View.OnLongClickListener() {

                @Override
                public boolean onLongClick(View v) {
                    idSelectedLongClick = RecyclerAdapter.this.objetList.get((int) getAdapterPosition()).getId();
                    if (actionMode != null) {
                        return false;
                    }
                    Context context = v.getContext();
                    // Start the CAB using the ActionMode.Callback defined above
                    actionMode = ((Activity) context).startActionMode(actionModeCallback);
                    v.setSelected(true);
                    return true;
                }
            });
        }
    }
}
