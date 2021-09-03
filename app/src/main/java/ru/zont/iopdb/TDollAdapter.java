package ru.zont.iopdb;

import androidx.annotation.NonNull;
import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.RecyclerView;

import android.view.LayoutInflater;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.squareup.picasso.Picasso;

import ru.zont.iopdb.data.DataEntity;
import ru.zont.iopdb.databinding.FragmentTdollItemBinding;

import java.util.List;

public class TDollAdapter extends RecyclerView.Adapter<TDollAdapter.ViewHolder> {

    private final List<? extends DataEntity> mValues;

    private OnClickListener listener = null;

    public TDollAdapter(List<? extends DataEntity> items) {
        mValues = items;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        return new ViewHolder(FragmentTdollItemBinding.inflate(LayoutInflater.from(parent.getContext()), parent, false));
    }

    @Override
    public void onBindViewHolder(@NonNull final ViewHolder holder, int position) {
        final DataEntity entity = mValues.get(position);
        holder.vName.setText(entity.getName());
        holder.vType.setText(entity.getType());
        holder.vRarity.setRarityValue(entity.getRarity());

        Picasso.with(holder.itemView.getContext())
                .load(entity.getThumbnail())
                .placeholder(R.drawable.thumb_placeholder)
                .error(R.drawable.thumb_error)
                .into(holder.vThumbnail);

        holder.vRoot.setOnClickListener(v -> {
            if (listener != null)
                listener.accept(entity, holder);
        });
    }

    @Override
    public int getItemCount() {
        return mValues.size();
    }

    public void setOnClickListener(OnClickListener listener) {
        this.listener = listener;
    }

    public static class ViewHolder extends RecyclerView.ViewHolder {
        public final TextView vName;
        private final TextView vType;
        private final RarityView vRarity;
        public final ImageView vThumbnail;
        private final CardView vRoot;

        public ViewHolder(FragmentTdollItemBinding binding) {
            super(binding.getRoot());
            vName = binding.tdollName;
            vType = binding.tdollType;
            vRarity = binding.tdollRarity;
            vThumbnail = binding.tdollThumb;
            vRoot = binding.tdollRoot;
        }

    }

    interface OnClickListener {
        void accept(DataEntity entity, ViewHolder holder);
    }
}