package com.movie.moviebazaar.holder;

import android.view.View;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.RecyclerView;

import com.facebook.shimmer.ShimmerFrameLayout;
import com.movie.moviebazaar.R;

public class ContentView extends RecyclerView.ViewHolder{


    public CardView contentLayout;
    public TextView contentText;
    public RecyclerView contentRecycler;
    public ShimmerFrameLayout contentShimmer;

    public ContentView(@NonNull View itemView) {
        super(itemView);

        contentLayout = itemView.findViewById(R.id.contentLayout);
        contentText = itemView.findViewById(R.id.contentText);
        contentRecycler = itemView.findViewById(R.id.contentRecycler);
        contentShimmer = itemView.findViewById(R.id.contentShimmer);
    }
}
