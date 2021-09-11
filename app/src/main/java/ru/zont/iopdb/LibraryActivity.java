package ru.zont.iopdb;

import android.os.Bundle;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.SearchView;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;

import java.util.Objects;

import ru.zont.iopdb.data.DataEntity;
import ru.zont.iopdb.databinding.ActivityLibraryBinding;

public class LibraryActivity extends AppCompatActivity {

    public static final String FRAGMENT_TAG = "lib_fragment";
    private Fragment fragment;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        ActivityLibraryBinding binding = ActivityLibraryBinding
                .inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        setSupportActionBar(binding.libToolbar);
        Objects.requireNonNull(getSupportActionBar())
                .setDisplayHomeAsUpEnabled(true);

        FragmentManager manager = getSupportFragmentManager();
        if (savedInstanceState == null) {
            final TDollListFragment fragment = new TDollListFragment();
            fragment.setOnItemClick(this::onClickEntity);
            manager.beginTransaction()
                    .setReorderingAllowed(true)
                    .replace(R.id.lib_root, fragment, FRAGMENT_TAG)
                    .commit();
        }

        fragment = manager.findFragmentByTag(FRAGMENT_TAG);
    }

    private void onClickEntity(DataEntity entity, TDollAdapter.ViewHolder holder) {
        final Bundle args = new Bundle();
        args.putInt(TDollInfoFragment.ARG_ID, entity.getID());
        getSupportFragmentManager().beginTransaction()
                .setReorderingAllowed(true)
                .addSharedElement(holder.vThumbnail, "info_image")
                .replace(R.id.lib_root, TDollInfoFragment.class, args)
                .addToBackStack(null)
                .commit();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.menu_library, menu);
        MenuItem item = menu.findItem(R.id.lib_act_search);
        setupSearchView((SearchView) item.getActionView());
        return true;
    }

    private void setupSearchView(SearchView view) {
        // TODO
    }
}