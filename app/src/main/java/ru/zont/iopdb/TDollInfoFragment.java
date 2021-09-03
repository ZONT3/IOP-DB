package ru.zont.iopdb;

import android.os.Bundle;
import android.text.SpannableStringBuilder;
import android.text.Spanned;
import android.text.style.ForegroundColorSpan;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentActivity;
import androidx.transition.ChangeBounds;

import com.squareup.picasso.Picasso;

import ru.zont.iopdb.data.TDoll;
import ru.zont.iopdb.databinding.FragmentTdollInfoBinding;

public class TDollInfoFragment extends Fragment {

    public static final String ARG_ID = "tdoll_id";

    private int entID = -1;

    public TDollInfoFragment() {
    }

    @SuppressWarnings("unused")
    public static TDollListFragment newInstance(int id) {
        TDollListFragment fragment = new TDollListFragment();
        Bundle args = new Bundle();
        args.putInt(ARG_ID, id);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setSharedElementEnterTransition(new ChangeBounds());

        if (getArguments() != null) {
            entID = getArguments().getInt(ARG_ID, entID);
        }
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater,
                             @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        final View view = inflater.inflate(R.layout.fragment_tdoll_info, container, false);
        final FragmentTdollInfoBinding binding = FragmentTdollInfoBinding.bind(view);
        postponeEnterTransition();

        new Thread(() -> {
            final TDoll ent = TDoll.getByID(entID);
            final FragmentActivity activity = getActivity();
            if (activity == null) return;

            activity.runOnUiThread(() -> {
                setup(binding, ent);
                startPostponedEnterTransition();
            });
        }, "TDollInfoFragment.fetchData").start();

        return view;
    }

    private void setup(FragmentTdollInfoBinding binding, TDoll ent) {
        Picasso.with(binding.tdiImage.getContext())
                .load(ent != null ? ent.getThumbnail() : null)
                .placeholder(R.drawable.thumb_placeholder)
                .error(R.drawable.thumb_error)
                .into(binding.tdiImage);

        if (ent == null) {
            binding.tdiRarity.setRarityValue(1);
            binding.tdiHeader.setText(R.string.info_error_header);
            return;
        }

        final SpannableStringBuilder span = new SpannableStringBuilder();
        span.append(
                ent.getType(),
                new ForegroundColorSpan(0x7FFFFFFF),
                Spanned.SPAN_INCLUSIVE_EXCLUSIVE)
            .append(" ")
            .append(ent.getName());
        binding.tdiHeader.setText(span);

        binding.tdiRarity.setRarityValue(ent.getRarity());
        binding.tdiTileset.setTileset(ent.getTileset());
    }
}