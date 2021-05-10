package fr.uavignon.cerimuseum;

import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;
import androidx.navigation.fragment.NavHostFragment;

import com.bumptech.glide.Glide;
import com.google.android.material.snackbar.Snackbar;

import java.util.Arrays;
import java.util.List;

public class DetailFragment extends Fragment {
    public static final String TAG = DetailFragment.class.getSimpleName();

    private DetailViewModel viewModel;
    private TextView textObjectName, textObjectBrand, textObjectCategory, textdescrption, textTimeFrame, textDetails, textWorking;
    private LinearLayout picturesLayout;
    ImageView imageView, thumbnaulView;


    @Override
    public View onCreateView(
            LayoutInflater inflater, ViewGroup container,
            Bundle savedInstanceState
    ) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_detail, container, false);
    }

    public void onViewCreated(@NonNull View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);


        viewModel = new ViewModelProvider(this).get(DetailViewModel.class);

        // Get selected city
        DetailFragmentArgs args = DetailFragmentArgs.fromBundle(getArguments());
        long objetID = args.getObjectId();
        Log.d(TAG,"selected id="+objetID);
        viewModel.setObjet(objetID);

        listenerSetup();
        observerSetup();

    }

    private void listenerSetup() {
        textObjectName = getView().findViewById(R.id.name);
        textObjectBrand = getView().findViewById(R.id.editBrand);
        textObjectCategory = getView().findViewById(R.id.editCategory);
        textdescrption = getView().findViewById(R.id.editDescription);
        textTimeFrame = getView().findViewById(R.id.editYear);
        textDetails = getView().findViewById(R.id.editdetails);
        textWorking = getView().findViewById(R.id.isWorking);
        picturesLayout = getView().findViewById(R.id.picturesLayout);
        thumbnaulView = getView().findViewById(R.id.thumbnail);


        getView().findViewById(R.id.buttonUpdate).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Snackbar.make(view, "Interrogation à faire du service web",
                        Snackbar.LENGTH_LONG)
                        .setAction("Action", null).show();
                viewModel.loadCity(viewModel.getObjet().getValue());


            }
        });

        getView().findViewById(R.id.buttonBack).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                NavHostFragment.findNavController(fr.uavignon.cerimuseum.DetailFragment.this)
                        .navigate(R.id.action_DetailFragment_to_ListFragment);
            }
        });
    }

    private void observerSetup() {
        viewModel.getObjet().observe(getViewLifecycleOwner(),
                object -> {
                    if (object != null) {
                        Log.d(TAG, "observing object view");
                        textObjectName.setText(object.getName());
                        textObjectBrand.setText(object.getBrand());
                        textObjectCategory.setText(object.getCategory());
                        textdescrption.setText(object.getDescrption());
                        textTimeFrame.setText(object.getTimeFrame());
                        textDetails.setText(object.getTechnicalDetails());
                        textWorking.setText(object.getWorking());

                        Glide.with(this)
                                .load(Uri.parse("https://demo-lia.univ-avignon.fr/cerimuseum/items/"+object.getId()+"/thumbnail"))
                                .into(thumbnaulView);

                        if (object.getPictures() != null){
                            List<String> picsArray = Arrays.asList(object.getPictures().split(","));

                            for (String pic : picsArray) {

                                imageView = new ImageView(this.getContext());
                                LinearLayout.LayoutParams layoutParams = new LinearLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT,
                                        ViewGroup.LayoutParams.WRAP_CONTENT);
                                imageView.setLayoutParams(layoutParams);
                                imageView.setScaleType(ImageView.ScaleType.FIT_XY);

                                Glide.with(this)
                                        .load(Uri.parse("https://demo-lia.univ-avignon.fr/cerimuseum/items/"+object.getId()+"/images/"+pic))
                                        .into(imageView);
                                picturesLayout.addView(imageView);
                            }
                        }
                    }
                });



    }


}
