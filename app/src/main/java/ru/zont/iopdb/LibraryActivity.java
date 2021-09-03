package ru.zont.iopdb;

import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentActivity;

import android.os.Bundle;

import ru.zont.iopdb.data.DataEntity;

public class LibraryActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_library);

        if (savedInstanceState == null) {
            final TDollListFragment fragment = new TDollListFragment();
            fragment.setOnItemClick(this::onClickEntity);
            getSupportFragmentManager().beginTransaction()
                    .setReorderingAllowed(true)
                    .replace(R.id.lib_root, fragment)
                    .commit();
        }
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
}