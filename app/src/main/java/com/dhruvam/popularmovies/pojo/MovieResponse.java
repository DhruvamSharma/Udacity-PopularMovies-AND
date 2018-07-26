package com.dhruvam.popularmovies.pojo;

import android.arch.persistence.room.Entity;
import android.arch.persistence.room.Ignore;
import android.arch.persistence.room.PrimaryKey;

import com.dhruvam.popularmovies.database.entity.FavouriteMovies;
import com.dhruvam.popularmovies.database.entity.MovieEntity;
import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;


import org.parceler.Parcel;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by dell on 05-06-2018.
 */
public class MovieResponse {

    @SerializedName("page")
    @Expose
    private Integer page;
    @SerializedName("total_results")
    @Expose
    private Integer totalResults;
    @SerializedName("total_pages")
    @Expose
    private Integer totalPages;
    @SerializedName("results")
    @Expose
    public List<Result> results = null;

    public Integer getPage() {
        return page;
    }

    public void setPage(Integer page) {
        this.page = page;
    }

    public Integer getTotalResults() {
        return totalResults;
    }

    public void setTotalResults(Integer totalResults) {
        this.totalResults = totalResults;
    }

    public Integer getTotalPages() {
        return totalPages;
    }

    public void setTotalPages(Integer totalPages) {
        this.totalPages = totalPages;
    }

    public List<Result> getResults() {
        return results;
    }

    public void setResults(List<Result> results) {
        this.results = results;
    }


    @Parcel
    public static class Result{

        /**
         * Constructor for model conversion
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


        public Result(Integer voteCount, Integer id, Boolean video, Double voteAverage, String title, Double popularity, String posterPath, String originalLanguage, String originalTitle, List<Integer> genreIds, String backdropPath, Boolean adult, String overview, String releaseDate) {
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

        public Result() {

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

        @Ignore
        public static List<Result> getObjectModelFromFavouritesData(List<FavouriteMovies> list) {

            List<Result> results = new ArrayList<>();
            for (FavouriteMovies movie : list) {
                results.add(new MovieResponse.Result(movie.getVoteCount(), movie.getId(), movie.getVideo(), movie.getVoteAverage(), movie.getTitle(), movie.getPopularity(), movie.getPosterPath(), movie.getOriginalLanguage(), movie.getOriginalTitle(), movie.getGenreIds(), movie.getBackdropPath(), movie.getAdult() ,movie.getOverview(), movie.getReleaseDate()));
            }

            return results;
        }

        @Ignore
        public static List<Result> getObjectModelFromAllMoviesData(List<MovieEntity> list) {
            List<Result> results = new ArrayList<>();
            for (MovieEntity movie : list) {
                results.add(new MovieResponse.Result(movie.getVoteCount(), movie.getId(), movie.getVideo(), movie.getVoteAverage(), movie.getTitle(), movie.getPopularity(), movie.getPosterPath(), movie.getOriginalLanguage(), movie.getOriginalTitle(), movie.getGenreIds(), movie.getBackdropPath(), movie.getAdult() ,movie.getOverview(), movie.getReleaseDate()));
            }

            return results;
        }
    }


}
