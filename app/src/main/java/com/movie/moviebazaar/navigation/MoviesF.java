package com.movie.moviebazaar.navigation;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
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
import com.movie.moviebazaar.activities.MovieInfo;
import com.movie.moviebazaar.activities.MovieLanguage;
import com.movie.moviebazaar.holder.MovieView;
import com.movie.moviebazaar.model.MovieClass;


import java.util.Objects;

/**
 * A simple {@link Fragment} subclass.
 */
public class MoviesF extends Fragment {

    public MoviesF() {
        // Required empty public constructor
    }

    private View mView;

    TextView englishMoviesText, teluguMoviesText, hindiMoviesText, tamilMoviesText;

    RecyclerView movieRecycler, hindiRecycler, teluguRecycler, tamilRecycler;
    DatabaseReference mEnglishMovies, mTeluguMovies ,mHindiMovies, mTamilMovies;
    private FirebaseRecyclerAdapter<MovieClass, MovieView> MovieAdapter;
    private FirebaseRecyclerAdapter<MovieClass, MovieView> teluguMovieAdapter;
    private FirebaseRecyclerAdapter<MovieClass, MovieView> hindiMovieAdapter;
    private FirebaseRecyclerAdapter<MovieClass, MovieView> tamilMovieAdapter;


    LinearLayoutManager movieLinearLayout, hindiLinearLayout, teluguLinearLayout, tamilLinearLayout;

    ShimmerFrameLayout teluguShimmerLayout, hindiShimmerLayout, movieShimmerLayout, tamilShimmerLayout;



    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        mView = inflater.inflate(R.layout.fragment_home, container, false);


        movieShimmerLayout = mView.findViewById(R.id.movieShimmerLayout);
        hindiShimmerLayout = mView.findViewById(R.id.hindiShimmerLayout);
        tamilShimmerLayout = mView.findViewById(R.id.tamilShimmerLayout);
        teluguShimmerLayout = mView.findViewById(R.id.teluguShimmerLayout);



        movieShimmerLayout.startShimmer();
        hindiShimmerLayout.startShimmer();
        tamilShimmerLayout.startShimmer();
        teluguShimmerLayout.startShimmer();



        languageOnCLick();
        initializeRecyclerView();
        return mView;
    }

    private void languageOnCLick() {

        englishMoviesText = mView.findViewById(R.id.englishMoviesText);
        teluguMoviesText = mView.findViewById(R.id.teluguMoviesText);
        hindiMoviesText = mView.findViewById(R.id.hindiMoviesText);
        tamilMoviesText = mView.findViewById(R.id.tamilMoviesText);


        englishMoviesText.setOnClickListener(v -> {

            Intent i = new Intent(getContext(), MovieLanguage.class);
            Bundle bundle = new Bundle();
            i.putExtra("languageName", "English");
            i.putExtras(bundle);
            startActivity(i);
        });

        teluguMoviesText.setOnClickListener(v -> {

            Intent i = new Intent(getContext(), MovieLanguage.class);
            Bundle bundle = new Bundle();
            i.putExtra("languageName", "Telugu");
            i.putExtras(bundle);
            startActivity(i);
        });

        hindiMoviesText.setOnClickListener(v -> {

            Intent i = new Intent(getContext(), MovieLanguage.class);
            Bundle bundle = new Bundle();
            i.putExtra("languageName", "Hindi");
            i.putExtras(bundle);
            startActivity(i);
        });

        tamilMoviesText.setOnClickListener(v -> {

            Intent i = new Intent(getContext(), MovieLanguage.class);
            Bundle bundle = new Bundle();
            i.putExtra("languageName", "Tamil");
            i.putExtras(bundle);
            startActivity(i);
        });


    }

    private void initializeRecyclerView() {
        movieRecycler = mView.findViewById(R.id.movieRecycler);
        hindiRecycler = mView.findViewById(R.id.hindiRecycler);
        tamilRecycler = mView.findViewById(R.id.tamilRecycler);
        teluguRecycler = mView.findViewById(R.id.teluguRecycler);


        movieRecycler.hasFixedSize();
        hindiRecycler.hasFixedSize();
        tamilRecycler.hasFixedSize();
        teluguRecycler.hasFixedSize();


        movieLinearLayout = new LinearLayoutManager(getContext(), LinearLayoutManager.HORIZONTAL,false);
        hindiLinearLayout = new LinearLayoutManager(getContext(), LinearLayoutManager.HORIZONTAL,false);
        tamilLinearLayout = new LinearLayoutManager(getContext(), LinearLayoutManager.HORIZONTAL,false);
        teluguLinearLayout = new LinearLayoutManager(getContext(), LinearLayoutManager.HORIZONTAL,false);


        teluguLinearLayout.setReverseLayout(true);
        teluguLinearLayout.setStackFromEnd(true);

        movieRecycler.setLayoutManager(movieLinearLayout);
        hindiRecycler.setLayoutManager(hindiLinearLayout);
        tamilRecycler.setLayoutManager(tamilLinearLayout);
        teluguRecycler.setLayoutManager(teluguLinearLayout);



        loadEnglishMovies();
        loadHindiMovies();
        LoadTamilMovies();
        loadTeluguMovies();
    }



    private void loadEnglishMovies() {
        mEnglishMovies = FirebaseDatabase.getInstance().getReference("Movies").child("English");
        mEnglishMovies.keepSynced(true);

        Query moviesList = mEnglishMovies;

        FirebaseRecyclerOptions<MovieClass> EnglishMovieOption = new FirebaseRecyclerOptions.Builder<MovieClass>()
                .setQuery(moviesList, MovieClass.class)
                .build();
        MovieAdapter = new FirebaseRecyclerAdapter<MovieClass, MovieView>(EnglishMovieOption) {
            @SuppressLint("ResourceAsColor")
            @Override
            protected void onBindViewHolder(@NonNull MovieView holder, final int position, @NonNull final MovieClass model) {

                movieShimmerLayout.setVisibility(View.GONE);
                movieRecycler.setVisibility(View.VISIBLE);


                final String IMAGE_URl =  model.getImageUrlP();

                final String movieName = MovieAdapter.getRef(position).getKey();

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

                Glide.with(Objects.requireNonNull(getContext())) //1
                        .load(IMAGE_URl)
                        .error(R.drawable.placeholder_image)
                        .placeholder(R.drawable.placeholder_image)
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
                    Intent i = new Intent(getContext(), MovieInfo.class);

                    Bundle bundle = new Bundle();
                    i.putExtra("IMAGE_URlL", model.getImageUrlL());
                    i.putExtra("movieName", MovieAdapter.getRef(position).getKey());
                    i.putExtra("movieYear", model.getMovieYear());
                    i.putExtra("trailerUrl", model.getTrailerUrl());
                    i.putExtra("videoUrl", model.getVideoUrl());
                    i.putExtra("languageName", "English");


                    i.putExtras(bundle);
                    startActivity(i);

                });
            }

            @NonNull
            @Override
            public MovieView onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
                View itemView = LayoutInflater.from(viewGroup.getContext())
                        .inflate(R.layout.layout_movie_grid, viewGroup, false);
                return new MovieView(itemView);
            }
        };
        movieRecycler.setAdapter(MovieAdapter);
        MovieAdapter.startListening();

    }


    private void loadHindiMovies() {
        mHindiMovies = FirebaseDatabase.getInstance().getReference("Movies").child("Hindi");
        mHindiMovies.keepSynced(true);

        Query moviesList = mHindiMovies;

        FirebaseRecyclerOptions<MovieClass> HindiMovieOption = new FirebaseRecyclerOptions.Builder<MovieClass>()
                .setQuery(moviesList, MovieClass.class)
                .build();
        hindiMovieAdapter = new FirebaseRecyclerAdapter<MovieClass, MovieView>(HindiMovieOption) {
            @SuppressLint("ResourceAsColor")
            @Override
            protected void onBindViewHolder(@NonNull MovieView holder, final int position, @NonNull final MovieClass model) {

                hindiShimmerLayout.setVisibility(View.GONE);
                hindiRecycler.setVisibility(View.VISIBLE);


                final String IMAGE_URl =  model.getImageUrlP();
                final String movieName = hindiMovieAdapter.getRef(position).getKey();

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

                Glide.with(Objects.requireNonNull(getContext())) //1
                        .load(IMAGE_URl)
                        .error(R.drawable.placeholder_image)
                        .placeholder(R.drawable.placeholder_image)
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
                    Intent i = new Intent(getContext(), MovieInfo.class);

                    Bundle bundle = new Bundle();
                    i.putExtra("IMAGE_URlL", model.getImageUrlL());
                    i.putExtra("movieName", hindiMovieAdapter.getRef(position).getKey());
                    i.putExtra("movieYear", model.getMovieYear());
                    i.putExtra("trailerUrl", model.getTrailerUrl());
                    i.putExtra("videoUrl", model.getVideoUrl());
                    i.putExtra("languageName", "Hindi");



                    i.putExtras(bundle);
                    startActivity(i);

                });
            }

            @NonNull
            @Override
            public MovieView onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
                View itemView = LayoutInflater.from(viewGroup.getContext())
                        .inflate(R.layout.layout_movie_grid, viewGroup, false);
                return new MovieView(itemView);
            }
        };
        hindiRecycler.setAdapter(hindiMovieAdapter);
        hindiMovieAdapter.startListening();

    }

    private void loadTeluguMovies() {
        mTeluguMovies = FirebaseDatabase.getInstance().getReference("Movies").child("Telugu");
        mTeluguMovies.keepSynced(true);

        Query moviesList = mTeluguMovies.orderByChild("movieYear").limitToLast(8);

        FirebaseRecyclerOptions<MovieClass> TeluguMovieOption = new FirebaseRecyclerOptions.Builder<MovieClass>()
                .setQuery(moviesList, MovieClass.class)
                .build();
        teluguMovieAdapter = new FirebaseRecyclerAdapter<MovieClass, MovieView>(TeluguMovieOption) {
            @SuppressLint("ResourceAsColor")
            @Override
            protected void onBindViewHolder(@NonNull MovieView holder, final int position, @NonNull final MovieClass model) {

                teluguShimmerLayout.setVisibility(View.GONE);
                teluguRecycler.setVisibility(View.VISIBLE);


                final String IMAGE_URl = model.getImageUrlP();
                final String movieName = teluguMovieAdapter.getRef(position).getKey();

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


                Glide.with(Objects.requireNonNull(getContext())) //1
                        .load(IMAGE_URl)
                        .error(R.drawable.placeholder_image)
                        .placeholder(R.drawable.placeholder_image)
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
                    Intent i = new Intent(getContext(), MovieInfo.class);

                    Bundle bundle = new Bundle();
                    i.putExtra("IMAGE_URlL", model.getImageUrlL());
                    i.putExtra("movieName", teluguMovieAdapter.getRef(position).getKey());
                    i.putExtra("movieYear", model.getMovieYear());
                    i.putExtra("trailerUrl", model.getTrailerUrl());
                    i.putExtra("videoUrl", model.getVideoUrl());
                    i.putExtra("languageName", "Telugu");


                    i.putExtras(bundle);
                    startActivity(i);

                });
            }

            @NonNull
            @Override
            public MovieView onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
                View itemView = LayoutInflater.from(viewGroup.getContext())
                        .inflate(R.layout.layout_movie_grid, viewGroup, false);
                return new MovieView(itemView);
            }
        };
        teluguRecycler.setAdapter(teluguMovieAdapter);
        teluguMovieAdapter.startListening();

    }

    private void LoadTamilMovies() {
        mTamilMovies = FirebaseDatabase.getInstance().getReference("Movies").child("Tamil");
        mTamilMovies.keepSynced(true);

        Query moviesList = mTamilMovies;

        FirebaseRecyclerOptions<MovieClass> tamilMovieOption = new FirebaseRecyclerOptions.Builder<MovieClass>()
                .setQuery(moviesList, MovieClass.class)
                .build();
        tamilMovieAdapter = new FirebaseRecyclerAdapter<MovieClass, MovieView>(tamilMovieOption) {
            @SuppressLint("ResourceAsColor")
            @Override
            protected void onBindViewHolder(@NonNull MovieView holder, final int position, @NonNull final MovieClass model) {

                tamilShimmerLayout.setVisibility(View.GONE);
                tamilRecycler.setVisibility(View.VISIBLE);


                final String IMAGE_URl = model.getImageUrlP();
                final String movieName = tamilMovieAdapter.getRef(position).getKey();

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

                Glide.with(Objects.requireNonNull(getContext())) //1
                        .load(IMAGE_URl)
                        .error(R.drawable.placeholder_image)
                        .placeholder(R.drawable.placeholder_image)
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
                    Intent i = new Intent(getContext(), MovieInfo.class);

                    Bundle bundle = new Bundle();
                    i.putExtra("IMAGE_URlL", model.getImageUrlL());
                    i.putExtra("movieName", tamilMovieAdapter.getRef(position).getKey());
                    i.putExtra("movieYear", model.getMovieYear());
                    i.putExtra("trailerUrl", model.getTrailerUrl());
                    i.putExtra("videoUrl", model.getVideoUrl());
                    i.putExtra("languageName", "Tamil");


                    i.putExtras(bundle);
                    startActivity(i);

                });
            }

            @NonNull
            @Override
            public MovieView onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
                View itemView = LayoutInflater.from(viewGroup.getContext())
                        .inflate(R.layout.layout_movie_grid, viewGroup, false);
                return new MovieView(itemView);
            }
        };
        tamilRecycler.setAdapter(tamilMovieAdapter);
        tamilMovieAdapter.startListening();

    }

    @Override
    public void onResume() {
        super.onResume();
    }

    @Override
    public void onPause() {
        super.onPause();
    }
}
