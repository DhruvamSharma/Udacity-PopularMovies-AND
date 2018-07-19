package com.dhruvam.popularmovies.database.entity;

import android.arch.persistence.room.Entity;
import android.arch.persistence.room.Ignore;
import android.arch.persistence.room.PrimaryKey;

import com.dhruvam.popularmovies.pojo.MovieResponse;
import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.util.List;

@Entity(tableName = "favourite_movies")
public class FavouriteMovies {

    /**
     * constructorfor Model conversion
     * @param voteCount
     * @param id
     * @param video
     * @param voteAverage
     * @param title
     * @param popularity
     * @param posterPath
     * @param originalLanguage
     * @param originalTitle
     * @param genreIds
     * @param backdropPath
     * @param adult
     * @param overview
     * @param releaseDate
     */
    @Ignore
    public FavouriteMovies(Integer voteCount, Integer id, Boolean video, Double voteAverage, String title, Double popularity, String posterPath, String originalLanguage, String originalTitle, List<Integer> genreIds, String backdropPath, Boolean adult, String overview, String releaseDate) {
        this.voteCount = voteCount;
        this.id = id;
        this.video = video;
        this.voteAverage = voteAverage;
        this.title = title;
        this.popularity = popularity;
        this.posterPath = posterPath;
        this.originalLanguage = originalLanguage;
        this.originalTitle = originalTitle;
        this.genreIds = genreIds;
        this.backdropPath = backdropPath;
        this.adult = adult;
        this.overview = overview;
        this.releaseDate = releaseDate;
    }


    public FavouriteMovies() {

    }

    @SerializedName("vote_count")
        @Expose
        Integer voteCount;
        @PrimaryKey
        @SerializedName("id")
        @Expose
        Integer id;
        @SerializedName("video")
        @Expose
        Boolean video;
        @SerializedName("vote_average")
        @Expose
        Double voteAverage;
        @SerializedName("title")
        @Expose
        String title;
        @SerializedName("popularity")
        @Expose
        Double popularity;
        @SerializedName("poster_path")
        @Expose
        String posterPath;
        @SerializedName("original_language")
        @Expose
        String originalLanguage;
        @SerializedName("original_title")
        @Expose
        String originalTitle;
        @Ignore
        @SerializedName("genre_ids")
        @Expose
        List<Integer> genreIds = null;
        @SerializedName("backdrop_path")
        @Expose
        String backdropPath;
        @SerializedName("adult")
        @Expose
        Boolean adult;
        @SerializedName("overview")
        @Expose
        String overview;
        @SerializedName("release_date")
        @Expose
        String releaseDate;

        public Integer getVoteCount() {
            return voteCount;
        }

        public void setVoteCount(Integer voteCount) {
            this.voteCount = voteCount;
        }

        public Integer getId() {
            return id;
        }

        public void setId(Integer id) {
            this.id = id;
        }

        public Boolean getVideo() {
            return video;
        }

        public void setVideo(Boolean video) {
            this.video = video;
        }

        public Double getVoteAverage() {
            return voteAverage;
        }

        public void setVoteAverage(Double voteAverage) {
            this.voteAverage = voteAverage;
        }

        public String getTitle() {
            return title;
        }

        public void setTitle(String title) {
            this.title = title;
        }

        public Double getPopularity() {
            return popularity;
        }

        public void setPopularity(Double popularity) {
            this.popularity = popularity;
        }

        public String getPosterPath() {
            return posterPath;
        }

        public void setPosterPath(String posterPath) {
            this.posterPath = posterPath;
        }

        public String getOriginalLanguage() {
            return originalLanguage;
        }

        public void setOriginalLanguage(String originalLanguage) {
            this.originalLanguage = originalLanguage;
        }

        public String getOriginalTitle() {
            return originalTitle;
        }

        public void setOriginalTitle(String originalTitle) {
            this.originalTitle = originalTitle;
        }

        public List<Integer> getGenreIds() {
            return genreIds;
        }

        public void setGenreIds(List<Integer> genreIds) {
            this.genreIds = genreIds;
        }

        public String getBackdropPath() {
            return backdropPath;
        }

        public void setBackdropPath(String backdropPath) {
            this.backdropPath = backdropPath;
        }

        public Boolean getAdult() {
            return adult;
        }

        public void setAdult(Boolean adult) {
            this.adult = adult;
        }

        public String getOverview() {
            return overview;
        }

        public void setOverview(String overview) {
            this.overview = overview;
        }

        public String getReleaseDate() {
            return releaseDate;
        }

        public void setReleaseDate(String releaseDate) {
            this.releaseDate = releaseDate;
        }


    /**
     * A method to convert Data Model to Object Model.
     * @param result
     * @return favouriteMovie
     */
    @Ignore
    public static FavouriteMovies getDataModelFromObject(MovieResponse.Result result) {
            FavouriteMovies favouriteMovie = new FavouriteMovies(result.getVoteCount(), result.getId(), result.getVideo(), result.getVoteAverage(), result.getTitle(), result.getPopularity(), result.getPosterPath(), result.getOriginalLanguage(), result.getOriginalTitle(), result.getGenreIds(), result.getBackdropPath(), result.getAdult(), result.getOverview(), result.getReleaseDate());
            return favouriteMovie;

    }
}
