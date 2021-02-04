package com.movie.moviebazaar.navigation;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Intent;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.core.widget.NestedScrollView;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.DataSource;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.bumptech.glide.load.engine.GlideException;
import com.bumptech.glide.request.RequestListener;
import com.bumptech.glide.request.RequestOptions;
import com.bumptech.glide.request.target.Target;
import com.facebook.shimmer.ShimmerFrameLayout;
import com.firebase.ui.database.FirebaseRecyclerAdapter;
import com.firebase.ui.database.FirebaseRecyclerOptions;
import com.glide.slider.library.SliderLayout;
import com.glide.slider.library.animations.DescriptionAnimation;
import com.glide.slider.library.slidertypes.BaseSliderView;
import com.glide.slider.library.slidertypes.DefaultSliderView;
import com.glide.slider.library.tricks.ViewPagerEx;
import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;
import com.movie.moviebazaar.MainActivity;
import com.movie.moviebazaar.R;
import com.movie.moviebazaar.activities.MovieInfo;
import com.movie.moviebazaar.activities.MovieLanguage;
import com.movie.moviebazaar.holder.MovieView;
import com.movie.moviebazaar.model.MovieClass;
import com.shankar.customtoast.Toasty;


import java.util.ArrayList;
import java.util.Objects;


public class HomeF extends Fragment implements BaseSliderView.OnSliderClickListener, ViewPagerEx.OnPageChangeListener {


    public HomeF() {
        // Required empty public constructor
    }

    private View mView;

    RecyclerView latestRecycler;
    DatabaseReference mLatestMovies , mSliderImages;
    private FirebaseRecyclerAdapter<MovieClass, MovieView> latestAdapter;

    private SliderLayout mDemoSlider;
    LinearLayoutManager latestLinearLayout;

    ShimmerFrameLayout latestShimmerLayout;

    BottomNavigationView navigation;
    TextView latestMoviesText;
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        mView = inflater.inflate(R.layout.fragment_home, container, false);

        navigation = (BottomNavigationView) mView.findViewById(R.id.navigation);


        latestShimmerLayout = mView.findViewById(R.id.latestShimmerLayout);
        latestShimmerLayout.startShimmer();

        latestMoviesText = mView.findViewById(R.id.latestMoviesText);
        initializeRecyclerView();
        slider();
        return mView;
    }

    private void onScroll() {

        NestedScrollView nested_content = (NestedScrollView) mView.findViewById(R.id.nested_scroll_view);
        nested_content.setOnScrollChangeListener((NestedScrollView.OnScrollChangeListener) (v, scrollX, scrollY, oldScrollX, oldScrollY) -> {
            if (scrollY < oldScrollY) { // up
                animateNavigation(false);
            }
            if (scrollY > oldScrollY) { // down
                animateNavigation(true);
            }
        });
    }

    boolean isNavigationHide = false;

    private void animateNavigation(final boolean hide) {
        if (isNavigationHide && hide || !isNavigationHide && !hide) return;
        isNavigationHide = hide;
        int moveY = hide ? (2 * navigation.getHeight()) : 0;
        navigation.animate().translationY(moveY).setStartDelay(100).setDuration(300).start();
    }


    private void slider() {

        mDemoSlider = mView.findViewById(R.id.slider);

        ArrayList<String> listUrl = new ArrayList<>();
        //ArrayList<String> listName = new ArrayList<>();

        listUrl.add("https://images.firstpost.com/wp-content/uploads/2020/01/masteer-1200.jpg");
        // listName.add("");

        listUrl.add("https://lh3.googleusercontent.com/proxy/fHHYS4UQqNjlkHOqsIpcUUmc1DpT63CnPzZZs6a7Oc6Y6wkUCN_ZgMC-c7WN9gJYoUMzzdQzVP425g6TeM4QrWK2Slbb7FBbMNA8IbA2MdhRFTB1dg");
        //listName.add("");

        listUrl.add("https://www.andhrawishesh.com/images/phocagallery/Movies/Tollywood/KrackMoviePosters/Krack-Movie-Posters-01.jpg");
        //listName.add("");

        listUrl.add("https://2.bp.blogspot.com/-29_oa6-0X5Y/UZZTcNOHrnI/AAAAAAAAp1Q/iwfWYeyeLUM/s1600/Iddarammayilatho+Movie+Latest+Posters.jpg");
        //listName.add("");

        listUrl.add("https://s3.india.com/wp-content/uploads/2019/12/pjimage-12-6.jpg");

        RequestOptions requestOptions = new RequestOptions();
        requestOptions.centerCrop();

        for (int i = 0; i < listUrl.size(); i++) {
            DefaultSliderView sliderView = new DefaultSliderView(getContext());

            // initialize SliderLayout
            BaseSliderView baseSliderView = sliderView
                    .image(listUrl.get(i))
                    //.description(listName.get(i))
                    .setRequestOption(requestOptions)
                    .setProgressBarVisible(true)
                    .setOnSliderClickListener(this);


            //add your extra information
            sliderView.bundle(new Bundle());
            // sliderView.getBundle().putString("extra", listName.get(i));
            mDemoSlider.addSlider(sliderView);
        }

        mDemoSlider.setPresetIndicator(SliderLayout.PresetIndicators.Center_Bottom);
        mDemoSlider.setCustomAnimation(new DescriptionAnimation());
        mDemoSlider.setDuration(3000);
        mDemoSlider.addOnPageChangeListener(this);
        mDemoSlider.stopCyclingWhenTouch(true);
    }

    private void initializeRecyclerView() {
        latestRecycler = mView.findViewById(R.id.latestRecycler);


        latestRecycler.hasFixedSize();

        // latestLinearLayout = new GridLayoutManager(getContext(),3);
        LinearLayoutManager latestLinearLayout = new LinearLayoutManager(getContext(), LinearLayoutManager.HORIZONTAL,false);

//        latestLinearLayout.setReverseLayout(true);
//        latestLinearLayout.setStackFromEnd(true);

        latestRecycler.setLayoutManager(latestLinearLayout);

        loadLatestMovies();


    }




    private void loadLatestMovies() {
        Query moviesList;
        mLatestMovies = FirebaseDatabase.getInstance().getReference("Movies").child("Latest");
        mLatestMovies.keepSynced(true);

         moviesList =mLatestMovies;
         //moviesList = mLatestMovies.orderByChild("movieName").startAt("A").endAt("A" + "\uf8ff");


        FirebaseRecyclerOptions<MovieClass> LatestMovieOption = new FirebaseRecyclerOptions.Builder<MovieClass>()
                .setQuery(moviesList, MovieClass.class)
                .build();
        latestAdapter = new FirebaseRecyclerAdapter<MovieClass, MovieView>(LatestMovieOption) {
            @SuppressLint("ResourceAsColor")
            @Override
            protected void onBindViewHolder(@NonNull MovieView holder, final int position, @NonNull final MovieClass model) {

                latestShimmerLayout.setVisibility(View.GONE);
                latestRecycler.setVisibility(View.VISIBLE);


                final String IMAGE_URl =  model.getImageUrlP();
                final String movieName = latestAdapter.getRef(position).getKey();

                holder.movieNameView.setText(movieName);

                Glide.with(Objects.requireNonNull(getContext())) //1
                        .load(IMAGE_URl)
                        .error(R.drawable.ph_rect_vertical)
                        .placeholder(R.drawable.ph_rect_vertical)
                        .skipMemoryCache(false)
                        .diskCacheStrategy(DiskCacheStrategy.AUTOMATIC)
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
                    assert movieName != null;

                    Intent i = new Intent(getContext(), MovieInfo.class);

                    Bundle bundle = new Bundle();
                    i.putExtra("imageUrl", model.getImageUrlL());
                    i.putExtra("movieName", movieName);
                    i.putExtra("movieYear", model.getMovieYear());
                    i.putExtra("trailerUrl", model.getTrailerUrl());
                    i.putExtra("videoUrl", model.getVideoUrl());
                    i.putExtra("languageName", "Latest");



                    i.putExtras(bundle);
                    startActivity(i);

                });
            }


            @SuppressLint("SetTextI18n")
            @Override
            public void onDataChanged() {
                if(getItemCount() == 0)
                {
                    Toasty.infoToast((Activity) getContext(),"No Movies Found");
                }
                else {
                    //Toasty.infoToast((Activity) getContext(),""+getItemCount());

                }
                latestShimmerLayout.setVisibility(View.GONE);

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
    public void onSliderClick(BaseSliderView slider) {

        Intent i = new Intent(getContext(), MovieLanguage.class);
        Bundle bundle = new Bundle();
        i.putExtra("languageName", "Telugu");
        i.putExtras(bundle);
        startActivity(i);
    }


    @Override
    public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {


    }

    @Override
    public void onPageSelected(int position) {

    }

    @Override
    public void onPageScrollStateChanged(int state) {

    }

    @Override
    public void onDestroy() {
        mDemoSlider.stopAutoCycle();
        super.onDestroy();
    }
}