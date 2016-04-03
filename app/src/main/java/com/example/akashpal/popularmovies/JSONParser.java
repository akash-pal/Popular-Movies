package com.example.akashpal.popularmovies;

import android.net.Uri;
import android.util.Log;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;

import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;

/**
 * Created by akash pal on 3/27/2016.
 */
public class JSONParser {
    public static final String TAG = JSONParser.class.getSimpleName();
    private static Response response;

    public static JSONObject getMovies(String sortBy) {
        Uri builtUri = Uri.parse(Constants.BASE_URL_MOVIES).buildUpon().appendPath(sortBy)
                .appendQueryParameter(Constants.API_KEY_PARAM, Constants.API_KEY)
                .build();
        try {
            OkHttpClient client = new OkHttpClient();
            Request request = new Request.Builder()
                    .url(builtUri.toString())
                    .build();
            response = client.newCall(request).execute();
            return new JSONObject(response.body().string());
        } catch (IOException | JSONException e) {
            Log.e(TAG, "" + e.getLocalizedMessage());
        }
        return null;
    }

    public static JSONObject getReviewById(int movieId) {
        Log.v(TAG, movieId + " ");
        Uri review_url = Uri.parse(Constants.BASE_URL_MOVIES)
                .buildUpon()
                .appendPath(Integer.toString(movieId))
                .appendPath("reviews")
                .appendQueryParameter(Constants.API_KEY_PARAM, Constants.API_KEY)
                .build();
        try {
            OkHttpClient client = new OkHttpClient();
            Request request = new Request.Builder()
                    .url(review_url.toString())
                    .build();
            response = client.newCall(request).execute();
            return new JSONObject(response.body().string());
        } catch (IOException | JSONException e) {
            Log.e(TAG, "" + e.getLocalizedMessage());
        }
        return null;
    }

    public static JSONObject getTrailerById(int movieId) {
        Log.v(TAG, movieId + " ");
        Uri trailer_url = Uri.parse(Constants.BASE_URL_MOVIES).buildUpon()
                .appendPath(Integer.toString(movieId))
                .appendPath("videos")
                .appendQueryParameter(Constants.API_KEY_PARAM, Constants.API_KEY)
                .build();
        try {
            OkHttpClient client = new OkHttpClient();
            Request request = new Request.Builder()
                    .url(trailer_url.toString())
                    .build();
            response = client.newCall(request).execute();
            return new JSONObject(response.body().string());
        } catch (IOException | JSONException e) {
            Log.e(TAG, "" + e.getLocalizedMessage());
        }
        return null;
    }
}
