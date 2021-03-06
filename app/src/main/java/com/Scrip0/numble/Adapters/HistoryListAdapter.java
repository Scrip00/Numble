package com.Scrip0.numble.Adapters;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.core.content.ContextCompat;
import androidx.recyclerview.widget.RecyclerView;

import com.Scrip0.numble.Database.HistoryModel;
import com.Scrip0.numble.R;
import com.Scrip0.numble.ViewSavedGameActivity;

import java.util.Collections;
import java.util.List;

public class HistoryListAdapter extends RecyclerView.Adapter<HistoryListAdapter.ViewHolder> {

    private static List<HistoryModel> games;
    private final Context context;

    public static class ViewHolder extends RecyclerView.ViewHolder {
        private final TextView gridSizeView, dateView, triesView, equationView;
        private final ImageView imageView;

        public ViewHolder(View view) {
            super(view);

            view.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    Intent intent = new Intent(view.getContext(), ViewSavedGameActivity.class);
                    intent.putExtra("key", games.get(getAdapterPosition()).getKey());
                    view.getContext().startActivity(intent);
                }
            });

            gridSizeView = (TextView) view.findViewById(R.id.grid_size);
            dateView = (TextView) view.findViewById(R.id.date);
            triesView = (TextView) view.findViewById(R.id.tries);
            equationView = (TextView) view.findViewById(R.id.equation);

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

        public TextView getEquationView() {
            return equationView;
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
                    .inflate(R.layout.history_list_row_right, viewGroup, false);
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
        viewHolder.getEquationView().setText(tempModel.getEquation());
        if (!tempModel.isWon()) {
            viewHolder.getImageView().setImageDrawable(ContextCompat.getDrawable(context, R.drawable.ic_sad_guy));
            viewHolder.getTriesView().setTextColor(context.getColor(R.color.cell_wrong));
        } else {
            viewHolder.getImageView().setImageDrawable(ContextCompat.getDrawable(context, R.drawable.ic_cup));
            viewHolder.getTriesView().setTextColor(context.getColor(R.color.cell_right));
        }

        setFadeOutAnimation(viewHolder.getViewHolderView());
    }

    private void setFadeOutAnimation(View view) {
        Animation anim = AnimationUtils.loadAnimation(context, R.anim.fade_out);
        anim.setDuration(500);
        view.startAnimation(anim);
    }

    @Override
    public int getItemCount() {
        return games.size();
    }
}