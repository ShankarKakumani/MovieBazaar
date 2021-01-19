package com.movie.moviebazaar.activities;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.DataSource;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.bumptech.glide.load.engine.GlideException;
import com.bumptech.glide.request.RequestListener;
import com.bumptech.glide.request.target.Target;
import com.facebook.shimmer.ShimmerFrameLayout;
import com.firebase.ui.database.FirebaseRecyclerAdapter;
import com.firebase.ui.database.FirebaseRecyclerOptions;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.movie.moviebazaar.R;
import com.movie.moviebazaar.holder.MovieView;
import com.movie.moviebazaar.model.MovieClass;


import java.util.Objects;


public class MovieLanguage extends AppCompatActivity {

    TextView languageText;

    RecyclerView latestRecycler;
    DatabaseReference mLatestMovies;
    LinearLayoutManager latestLinearLayout;

    ShimmerFrameLayout latestShimmerLayout;
    private static FirebaseDatabase firebaseDatabase;
    private FirebaseRecyclerAdapter<MovieClass, MovieView> latestAdapter;

    TextView noMoviesText;
    Button loadVideo;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_movie_language);

        if (firebaseDatabase == null) {
            firebaseDatabase= FirebaseDatabase.getInstance();
            //firebaseDatabase.setPersistenceEnabled(true);
        }

        languageText = findViewById(R.id.languageText);

        latestShimmerLayout = findViewById(R.id.latestShimmerLayout);
        latestShimmerLayout.startShimmer();


        noMoviesText = findViewById(R.id.noMoviesText);


        //FacebookAds();
        //interstitialAd();
        //rewardedVideoAd();
        initializeRecyclerView();
    }


    private void initializeRecyclerView() {
        latestRecycler = findViewById(R.id.latestRecycler);


        latestRecycler.hasFixedSize();
        LinearLayoutManager latestLinearLayout = new GridLayoutManager(getApplicationContext(),3);
//        latestLinearLayout.setReverseLayout(true);
//        latestLinearLayout.setStackFromEnd(true);
        latestRecycler.setLayoutManager(latestLinearLayout);
        loadLatestMovies();

    }



    //Start Listening Adapter
    @Override
    protected void onStart() {
        super.onStart();
        latestAdapter.startListening();
    }

    //Stop Listening Adapter
    @Override
    protected void onStop() {
        super.onStop();
        latestAdapter.stopListening();
    }


    private void loadLatestMovies() {

        Bundle bundle = getIntent().getExtras();
        assert bundle != null;
        String searchName = bundle.getString("searchName");
        String languageName = bundle.getString("languageName");
        assert languageName != null;


        mLatestMovies = FirebaseDatabase.getInstance().getReference("Movies").child(languageName);
        mLatestMovies.keepSynced(true);


        Query moviesList = mLatestMovies.orderByChild("movieName").startAt(searchName).endAt(searchName + "\uf8ff");

        //Query moviesList = mLatestMovies.orderByChild("movieName").startAt(searchName.toUpperCase()).endAt(searchName.toLowerCase()+ "\uf8ff");
        FirebaseRecyclerOptions<MovieClass> EnglishMovieOption = new FirebaseRecyclerOptions.Builder<MovieClass>()

                .setQuery(moviesList, MovieClass.class)
                .build();
        latestAdapter = new FirebaseRecyclerAdapter<MovieClass, MovieView>(EnglishMovieOption) {
            @SuppressLint({"ResourceAsColor", "SetTextI18n"})
            @Override
            protected void onBindViewHolder(@NonNull MovieView holder, final int position, @NonNull final MovieClass model) {

                latestShimmerLayout.setVisibility(View.GONE);
                latestRecycler.setVisibility(View.VISIBLE);
                int count = latestAdapter.getItemCount();

                languageText.setText(languageName+" Movies  -  " + count);
                final String IMAGE_URl =  model.getImageUrlP();
                final String movieName = latestAdapter.getRef(position).getKey();

                holder.movieNameView.setText(movieName);


//                Picasso.get().load(IMAGE_URl).error(R.drawable.placeholder_image)
//                        .placeholder(R.drawable.placeholder_image).into(holder.movieImageView, new com.squareup.picasso.Callback() {
//                    @Override
//                    public void onSuccess() {
//
//                        holder.movieNameView.setVisibility(View.GONE);
//                    }
//
//                    @Override
//                    public void onError(Exception e) {
//                        holder.movieNameView.setVisibility(View.VISIBLE);
//
//                    }
//
//                });

                Glide.with(Objects.requireNonNull(getApplicationContext())) //1
                        .load(IMAGE_URl)
                        .placeholder(R.drawable.placeholder_image)
                        .error(R.drawable.placeholder_image)
                        .skipMemoryCache(false) //2
                        .diskCacheStrategy(DiskCacheStrategy.AUTOMATIC) //3
                        .addListener(new RequestListener<Drawable>() {
                            @Override
                            public boolean onLoadFailed(@Nullable GlideException e, Object model, Target<Drawable> target, boolean isFirstResource) {

                                holder.movieNameView.setVisibility(View.VISIBLE);
                                holder.movieProgressBar.setVisibility(View.VISIBLE);
                                return false;

                            }

                            @Override
                            public boolean onResourceReady(Drawable resource, Object model, Target<Drawable> target, DataSource dataSource, boolean isFirstResource) {

                                holder.movieNameView.setVisibility(View.GONE);
                                holder.movieProgressBar.setVisibility(View.GONE);
                                return false;

                            }
                        })
                        .into(holder.movieImageView);


                holder.movieLayout.setOnClickListener(v -> {
                    Intent i = new Intent(getApplicationContext(), MovieInfo.class);

                    Bundle bundle = new Bundle();
                    i.putExtra("IMAGE_URlL", model.getImageUrlL());
                    i.putExtra("movieName", movieName);
                    i.putExtra("movieYear", model.getMovieYear());
                    i.putExtra("trailerUrl", model.getTrailerUrl());
                    i.putExtra("videoUrl", model.getVideoUrl());
                    i.putExtra("languageName", languageName);



                    i.putExtras(bundle);
                    startActivity(i);

                });
            }


            @SuppressLint("SetTextI18n")
            @Override
            public void onDataChanged() {
                if(getItemCount() == 0)
                {
                    noMoviesText.setVisibility(View.VISIBLE);
                    noMoviesText.setText("No results found for  '" + searchName + "'  in " + languageName + " movies");
                }
                else {
                    noMoviesText.setVisibility(View.GONE);
                }
            }

            @NonNull
            @Override
            public MovieView onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
                View itemView = LayoutInflater.from(viewGroup.getContext())
                        .inflate(R.layout.layout_movie_grid, viewGroup, false);
                return new MovieView(itemView);
            }
        };
        latestRecycler.setAdapter(latestAdapter);
        latestAdapter.startListening();

    }



    @Override
    protected void onDestroy() {

        super.onDestroy();
    }

    @Override
    public void onBackPressed() {
        finish();
    }
}