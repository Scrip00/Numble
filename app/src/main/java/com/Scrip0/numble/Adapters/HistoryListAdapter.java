package com.Scrip0.numble.Adapters;

import android.annotation.SuppressLint;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.core.content.ContextCompat;
import androidx.recyclerview.widget.RecyclerView;

import com.Scrip0.numble.Database.HistoryModel;
import com.Scrip0.numble.R;

import java.util.Collections;
import java.util.List;

public class HistoryListAdapter extends RecyclerView.Adapter<HistoryListAdapter.ViewHolder> {

    private List<HistoryModel> games;
    private final Context context;

    public static class ViewHolder extends RecyclerView.ViewHolder {
        private final TextView gridSizeView, dateView, triesView;
        private final ImageView imageView;

        public ViewHolder(View view) {
            super(view);
            // Define click listener for the ViewHolder's View

            gridSizeView = (TextView) view.findViewById(R.id.grid_size);
            dateView = (TextView) view.findViewById(R.id.date);
            triesView = (TextView) view.findViewById(R.id.tries);

            imageView = (ImageView) view.findViewById(R.id.imageView);

        }

        public View getViewHolderView() {
            return super.itemView;
        }

        public TextView getGridSizeView() {
            return gridSizeView;
        }

        public TextView getDateView() {
            return dateView;
        }

        public TextView getTriesView() {
            return triesView;
        }

        public ImageView getImageView() {
            return imageView;
        }
    }

    public HistoryListAdapter(List<HistoryModel> games, Context context) {
        Collections.reverse(games);
        this.games = games;
        this.context = context;
    }

    @Override
    public int getItemViewType(int position) {
        return position % 2;
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup viewGroup, int viewType) {
        View view;
        if (viewType == 0) {
            view = LayoutInflater.from(viewGroup.getContext())
                    .inflate(R.layout.history_list_row_left, viewGroup, false);
        } else {
            view = LayoutInflater.from(viewGroup.getContext())
                    .inflate(R.layout.history_list_row_left, viewGroup, false);
        }
        return new ViewHolder(view);
    }

    @SuppressLint("SetTextI18n")
    @Override
    public void onBindViewHolder(ViewHolder viewHolder, final int position) {
        HistoryModel tempModel = games.get(position);
        viewHolder.getGridSizeView().setText(tempModel.getEquation().length() + " X " + tempModel.getCells().length);
        viewHolder.getDateView().setText(tempModel.getTime());
        viewHolder.getTriesView().setText(tempModel.getCurrentRow() + " / " + tempModel.getCells().length);
        if (!tempModel.isWon())
            viewHolder.getImageView().setImageDrawable(ContextCompat.getDrawable(context, R.drawable.ic_sad_guy));
        else
            viewHolder.getImageView().setImageDrawable(ContextCompat.getDrawable(context, R.drawable.ic_cup));
    }

    @Override
    public int getItemCount() {
        return games.size();
    }
}