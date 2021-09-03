package ru.zont.iopdb;

import android.content.Context;
import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentActivity;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import ru.zont.iopdb.data.DataEntity;
import ru.zont.iopdb.data.TDoll;

/**
 * A fragment representing a list of Items.
 */
public class TDollListFragment extends Fragment {

    public static final String ARG_COLUMN_COUNT = "column-count";
    public static final String ARG_WIDTH = "width";

    private int mColumnCount = -1;
    private int mPredictedWidth = -1;

    private TDollAdapter adapter;
    private TDollAdapter.OnClickListener listener;

    public TDollListFragment() {
    }

    @SuppressWarnings("unused")
    public static TDollListFragment newInstance(int columnCount) {
        TDollListFragment fragment = new TDollListFragment();
        Bundle args = new Bundle();
        args.putInt(ARG_COLUMN_COUNT, columnCount);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        if (getArguments() != null) {
            mColumnCount = getArguments().getInt(ARG_COLUMN_COUNT, mColumnCount);
            mPredictedWidth = getArguments().getInt(ARG_WIDTH, mPredictedWidth);
        }

        if (mPredictedWidth < 0) {
            mPredictedWidth = getResources().getDisplayMetrics().widthPixels;
        }
    }

    public int getColumnCount() {
        return mColumnCount;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_tdoll_list, container, false);

        if (view instanceof RecyclerView) {
            Context context = view.getContext();

            if (mColumnCount < 0) {
                final int maxlen = (int) context.getResources().getDimension(R.dimen.grid_maxlen);
                mColumnCount = Math.max(1, mPredictedWidth / maxlen);
            }

            RecyclerView recyclerView = (RecyclerView) view;
            if (mColumnCount <= 1) {
                recyclerView.setLayoutManager(new LinearLayoutManager(context));
            } else {
                recyclerView.setLayoutManager(new GridLayoutManager(context, mColumnCount));
            }

            new Thread(
                    () -> {
                        adapter = new TDollAdapter(TDoll.fetchAllList());
                        if (getActivity() != null)
                            getActivity().runOnUiThread(() -> {
                                adapter.setOnClickListener(listener);
                                recyclerView.setAdapter(adapter);
                            });
                    },
                    "TDollListFragment.fetchData")
                    .start();
        }
        return view;
    }

    public void setOnItemClick(TDollAdapter.OnClickListener listener) {
        this.listener = listener;
        if (adapter != null) adapter.setOnClickListener(this.listener);
    }
}