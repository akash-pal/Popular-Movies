package com.example.akashpal.popularmovies;

import android.util.Log;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;

import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;


/**
 * Created by akash pal on 3/27/2016.
 */
public class JSONParser {

    public static final String TAG = "TAG";

    /**
     * Response
     */
    private static Response response;

    /**
     * Key to Send
     */
    private static final String KEY_MOVIE_ID = "movie_id";

    public static JSONObject getReviewById(int movieId) {
        Log.v(TAG,movieId+" ");
        String review_url = "http://api.themoviedb.org/3/movie/" + movieId + "/reviews?api_key=caf08658054207c90c60ebe8dde96b31";

        try {
            OkHttpClient client = new OkHttpClient();

            Request request = new Request.Builder()
                    .url(review_url)
                    .build();

            response = client.newCall(request).execute();
            return new JSONObject(response.body().string());

        } catch (IOException | JSONException e) {
            Log.e(TAG, "" + e.getLocalizedMessage());
        }
        return null;
    }
}
