package com.movie.moviebazaar.helper.recyclerview.holder;

import android.view.View;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.RecyclerView;

import com.movie.moviebazaar.R;


public class MovieView extends RecyclerView.ViewHolder implements View.OnClickListener {


    public ItemClickListener itemClickListener;
    public CardView movieLayout;
    public ImageView movieImageView;
    public TextView movieNameView;
    public ProgressBar movieProgressBar;

    public MovieView(@NonNull View itemView) {
        super(itemView);

        movieLayout = (CardView) itemView.findViewById(R.id.movieLayout);
        movieImageView = itemView.findViewById(R.id.movieImageView);
        movieNameView = itemView.findViewById(R.id.movieNameView);
        movieProgressBar = itemView.findViewById(R.id.movieProgressBar);

        itemView.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        itemClickListener.onClick(v, getAdapterPosition(), false);

    }

}
