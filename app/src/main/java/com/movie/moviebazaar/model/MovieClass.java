package com.movie.moviebazaar.model;

public class MovieClass {
    private String movieName, imageUrlP ,imageUrlL , movieYear, videoUrl, trailerUrl;

    public MovieClass() {
    }

    public MovieClass(String movieName, String videoUrl, String trailerUrl, String imageUrlP, String imageUrlL, String movieYear) {

        this.movieName = movieName;
        this.videoUrl = videoUrl;
        this.trailerUrl = trailerUrl;
        this.imageUrlP = imageUrlP;
        this.imageUrlL = imageUrlL;
        this.movieYear = movieYear;


    }

    public String getMovieName() {
        return movieName;
    }

    public void setMovieName(String movieName) {
        this.movieName = movieName;
    }

    public String getImageUrlP() {
        return imageUrlP;
    }

    public void setImageUrlP(String imageUrlP) {
        this.imageUrlP = imageUrlP;
    }

    public String getImageUrlL() {
        return imageUrlL;
    }

    public void setImageUrlL(String imageUrlL) {
        this.imageUrlL = imageUrlL;
    }

    public String getMovieYear() {
        return movieYear;
    }

    public void setMovieYear(String movieYear) {
        this.movieYear = movieYear;
    }

    public String getVideoUrl() {
        return videoUrl;
    }

    public void setVideoUrl(String videoUrl) {
        this.videoUrl = videoUrl;
    }

    public String getTrailerUrl() {
        return trailerUrl;
    }

    public void setTrailerUrl(String trailerUrl) {
        this.trailerUrl = trailerUrl;
    }
}
