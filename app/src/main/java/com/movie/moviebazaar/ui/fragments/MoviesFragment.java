package com.movie.moviebazaar.ui.fragments;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

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
import com.firebase.ui.database.FirebaseRecyclerAdapter;
import com.firebase.ui.database.FirebaseRecyclerOptions;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.movie.moviebazaar.R;
import com.movie.moviebazaar.ui.activities.MovieInfoActivity;
import com.movie.moviebazaar.ui.activities.MovieLanguageActivity;
import com.movie.moviebazaar.helper.recyclerview.holder.ContentView;
import com.movie.moviebazaar.helper.recyclerview.holder.MovieView;
import com.movie.moviebazaar.helper.recyclerview.model.Content;
import com.movie.moviebazaar.helper.recyclerview.model.MovieClass;


import java.util.Objects;

/**
 * A simple {@link Fragment} subclass.
 */
public class MoviesFragment extends Fragment {

    public MoviesFragment() {
        // Required empty public constructor
    }

    private View mView;


    RecyclerView  moviesRecycler;
    DatabaseReference mLanguages,mLanguageMovies;

    private FirebaseRecyclerAdapter<MovieClass, MovieView> languageMoviesAdapter;
    private FirebaseRecyclerAdapter<Content, ContentView> moviesAdapter;



    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        mView = inflater.inflate(R.layout.fragment_movies, container, false);




        //languageOnCLick();
        loadContent();
       // initializeRecyclerView();
        return mView;
    }



    private void loadContent(){

        moviesRecycler = mView.findViewById(R.id.moviesRecycler);
        moviesRecycler.hasFixedSize();
        LinearLayoutManager moviesLinearLayoutManager = new LinearLayoutManager(getContext(),LinearLayoutManager.VERTICAL,true);
        moviesRecycler.setLayoutManager(moviesLinearLayoutManager);


        mLanguages = FirebaseDatabase.getInstance().getReference("Movies");
        //mLanguages.keepSynced(true);
        Query languages = mLanguages;

        FirebaseRecyclerOptions<Content> content = new FirebaseRecyclerOptions.Builder<Content>()
                .setQuery(languages,Content.class)
                .build();

        moviesAdapter = new FirebaseRecyclerAdapter<Content, ContentView>(content) {
            @Override
            protected void onBindViewHolder(@NonNull ContentView viewHolder, int i, @NonNull Content contentModel) {


                String language = moviesAdapter.getRef(i).getKey();

//                if(language.equals("Latest"))
//                {
//                    viewHolder.contentLayout.setVisibility(View.GONE);
//                }
                viewHolder.contentText.setText(language+" Movies");
                LinearLayoutManager layoutManager = new LinearLayoutManager(getContext(),LinearLayoutManager.HORIZONTAL,false);
                layoutManager.setReverseLayout(true);
                layoutManager.setStackFromEnd(true);
                viewHolder.contentRecycler.setLayoutManager(layoutManager);

                viewHolder.contentText.setOnClickListener(v -> {

                    Intent languageIntent = new Intent(getContext(), MovieLanguageActivity.class);
                    Bundle bundle = new Bundle();
                    languageIntent.putExtra("languageName", language);
                    languageIntent.putExtras(bundle);
                    startActivity(languageIntent);
                });
                mLanguageMovies = FirebaseDatabase.getInstance().getReference("Movies").child(language);
                mLanguageMovies.keepSynced(true);

                //Inside RecyclerView
                Query moviesList = mLanguageMovies.orderByChild("movieYear").limitToLast(8);

                FirebaseRecyclerOptions<MovieClass> languageMovies = new FirebaseRecyclerOptions.Builder<MovieClass>()
                        .setQuery(moviesList, MovieClass.class)
                        .build();
                languageMoviesAdapter = new FirebaseRecyclerAdapter<MovieClass, MovieView>(languageMovies) {
                    @SuppressLint("ResourceAsColor")
                    @Override
                    protected void onBindViewHolder(@NonNull MovieView holder, final int position, @NonNull final MovieClass model) {

                        final String IMAGE_URl = model.getImageUrlP();
                        final String movieName = model.getMovieName();

                        viewHolder.contentShimmer.setVisibility(View.GONE);
                        holder.movieNameView.setText(movieName);


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
                            Intent i = new Intent(getContext(), MovieInfoActivity.class);

                            Bundle bundle = new Bundle();
                            i.putExtra("imageUrl", model.getImageUrlL());
                            i.putExtra("movieName", model.getMovieName());
                            i.putExtra("movieYear", model.getMovieYear());
                            i.putExtra("videoUrl", model.getVideoUrl());
                            i.putExtra("languageName", language);


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
                viewHolder.contentRecycler.setAdapter(languageMoviesAdapter);
                languageMoviesAdapter.startListening();

                //End of inside RecyclerView
            }

            @NonNull
            @Override
            public ContentView onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
                View itemView = LayoutInflater.from(parent.getContext())
                        .inflate(R.layout.layout_content, parent, false);
                return new ContentView(itemView);
            }


        };

        moviesRecycler.setAdapter(moviesAdapter);
        moviesAdapter.startListening();

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
