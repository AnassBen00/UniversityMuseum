package fr.uavignon.cerimuseum;

import android.os.Bundle;

import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.android.material.snackbar.Snackbar;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;

import android.view.View;

import android.view.Menu;
import android.view.MenuItem;
import android.widget.SearchView;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import fr.uavignon.cerimuseum.data.MuseumRepository;
import fr.uavignon.cerimuseum.data.Object;

public class MainActivity extends AppCompatActivity {

    private ListViewModel viewModel;
    MuseumRepository museumRepository;
    RecyclerAdapter recyclerAdapter;
    MuseumRepository repo;
    private List<Object> Objects;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Toolbar toolbar = findViewById(R.id.toolbar);
        recyclerAdapter = new RecyclerAdapter();
        setSupportActionBar(toolbar);
        museumRepository = new MuseumRepository(getApplication());
        museumRepository.loadMuseumObjectswithKeys();
        repo = new MuseumRepository(getApplication());
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {

        super.onCreateOptionsMenu(menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.

        viewModel = new ViewModelProvider(this).get(ListViewModel.class);
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_update_all) {

            viewModel.getAllObjects().observe(this, new Observer<List<Object>>() {
                @Override
                public void onChanged(@Nullable final List<Object> objects) {
                    recyclerAdapter.setObjetList(objects);

                    Snackbar.make(getWindow().getDecorView().getRootView()
                            , "Interrogation à faire du service web",
                            Snackbar.LENGTH_LONG)
                            .setAction("Action", null).show();
                }
            });
            museumRepository = new MuseumRepository(getApplication());
            Objects = recyclerAdapter.getObjetList();
            museumRepository.loadAllObjects(Objects);
            try {
                museumRepository.loadCategories();
            } catch (IOException e) {
                e.printStackTrace();
            }
            return true;
        }

        return super.onOptionsItemSelected(item);
    }
}