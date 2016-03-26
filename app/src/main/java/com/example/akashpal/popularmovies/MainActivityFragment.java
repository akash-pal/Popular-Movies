package com.example.akashpal.popularmovies;

import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.NavUtils;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.GridView;
import android.widget.Toast;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by akash pal on 3/19/2016.
 */
public class MainActivityFragment extends Fragment {

    public static final String TAG = MainActivityFragment.class.getSimpleName();

    private String mSortBy = Constants.SORT_BY_POPULAR;
    ArrayList<Movie> movieList = null;
    GridView mGridView;
    MovieGridAdapter movieGridAdapter;

    private static final String POPULARITY_DESC = "popularity.desc";
    private static final String RATING_DESC = "vote_average.desc";
    private String sort_title = Constants.POPULAR;

    public interface Callback {
        void onItemSelected(Movie movie);
        void onFragmentInteraction(String title);
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        // Add this line in order for this fragment to handle menu events.
        setHasOptionsMenu(true);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

        View view = inflater.inflate(R.layout.fragment_main, container, false);

        mGridView = (GridView) view.findViewById(R.id.moviesGrid);
        movieGridAdapter = new MovieGridAdapter(getActivity(), new ArrayList<Movie>());
        mGridView.setAdapter(movieGridAdapter);

        if (savedInstanceState == null) {
            updateMovies(mSortBy);
        } else {
            mSortBy = savedInstanceState.getString(Constants.SORT_KEY);
            sort_title = savedInstanceState.getString(Constants.SORT_TITLE);
            Log.v(TAG, "Restored" + sort_title);
            updateMovies(mSortBy);
        }

        mGridView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Movie movie = movieGridAdapter.getItem(position);
                ((Callback) getActivity()).onItemSelected(movie);
            }
        });

        return view;
    }

    @Override
    public void onSaveInstanceState(Bundle outState) {
        Log.v(TAG, "saved instance" + mSortBy);
        outState.putString(Constants.SORT_KEY, mSortBy);
        outState.putString(Constants.SORT_TITLE, sort_title);
        super.onSaveInstanceState(outState);
    }


    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        inflater.inflate(R.menu.menu_fragment_main, menu);

        MenuItem action_sort_by_popularity = menu.findItem(R.id.action_sort_by_popularity);
        MenuItem action_sort_by_rating = menu.findItem(R.id.action_sort_by_rating);

        if (mSortBy.contentEquals(POPULARITY_DESC)) {
            if (!action_sort_by_popularity.isChecked()) {
                action_sort_by_popularity.setChecked(true);
            }
        } else if (mSortBy.contentEquals(RATING_DESC)) {
            if (!action_sort_by_rating.isChecked()) {
                action_sort_by_rating.setChecked(true);
            }
        }

    }


    @Override
    public boolean onOptionsItemSelected(MenuItem item) {

        switch (item.getItemId()) {
            // Respond to the action bar's Up/Home button
            case android.R.id.home:
                NavUtils.navigateUpFromSameTask(getActivity());
                return true;
            case R.id.action_sort_by_popularity:

                if (item.isChecked()) {
                    item.setChecked(false);
                } else {
                    item.setChecked(true);
                }
                sort_title = Constants.POPULAR;
                ((Callback) getActivity()).onFragmentInteraction(sort_title);
                mSortBy = Constants.SORT_BY_POPULAR;

                Toast.makeText(getActivity(),Constants.POPULAR, Toast.LENGTH_SHORT).show();
                updateMovies(mSortBy);
                return true;

            case R.id.action_sort_by_rating:

                if (item.isChecked()) {
                    item.setChecked(false);
                } else {
                    item.setChecked(true);
                }

                sort_title = Constants.TOP_RATED;
                ((Callback) getActivity()).onFragmentInteraction(sort_title);
                mSortBy = Constants.SORT_BY_TOP_RATED;

                Toast.makeText(getActivity(),Constants.TOP_RATED, Toast.LENGTH_SHORT).show();
                updateMovies(mSortBy);
                return true;

        }
        return super.onOptionsItemSelected(item);

    }


    private void updateMovies(String sort_by) {
        new FetchMoviesTask().execute(sort_by);
    }


    public class FetchMoviesTask extends AsyncTask<String, Void, List<Movie>> {

        private final String LOG_TAG = FetchMoviesTask.class.getSimpleName();

        private List<Movie> getMoviesDataFromJson(String jsonStr) throws JSONException {
            JSONObject movieJson = new JSONObject(jsonStr);
            JSONArray movieArray = movieJson.getJSONArray("results");

            List<Movie> results = new ArrayList<>();

            for (int i = 0; i < movieArray.length(); i++) {
                JSONObject movie = movieArray.getJSONObject(i);
                Movie movieModel = new Movie(movie);
                results.add(movieModel);
            }

            return results;
        }

        @Override
        protected List<Movie> doInBackground(String... params) {

            if (params.length == 0) {
                return null;
            }

            HttpURLConnection urlConnection = null;
            BufferedReader reader = null;
            String jsonStr = null;

            try {
                final String BASE_URL = "http://api.themoviedb.org/3/movie";
                final String API_KEY_PARAM = "api_key";

                Uri builtUri = Uri.parse(BASE_URL).buildUpon().appendPath(params[0])
                        .appendQueryParameter(API_KEY_PARAM, getString(R.string.tmdb_api_key))
                        .build();
                URL url = new URL(builtUri.toString());

                urlConnection = (HttpURLConnection) url.openConnection();
                urlConnection.setRequestMethod("GET");
                urlConnection.connect();

                InputStream inputStream = urlConnection.getInputStream();
                StringBuffer buffer = new StringBuffer();
                if (inputStream == null) {
                    return null;
                }
                reader = new BufferedReader(new InputStreamReader(inputStream));

                String line;
                while ((line = reader.readLine()) != null) {
                    buffer.append(line + "\n");
                }

                if (buffer.length() == 0) {
                    return null;
                }
                jsonStr = buffer.toString();
            } catch (IOException e) {
                Log.e(LOG_TAG, "Error ", e);
                return null;
            } finally {
                if (urlConnection != null) {
                    urlConnection.disconnect();
                }
                if (reader != null) {
                    try {
                        reader.close();
                    } catch (final IOException e) {
                        Log.e(LOG_TAG, "Error closing stream", e);
                    }
                }
            }

            try {
                return getMoviesDataFromJson(jsonStr);
            } catch (JSONException e) {
                Log.e(LOG_TAG, e.getMessage(), e);
                e.printStackTrace();
            }

            return null;
        }

        @Override
        protected void onPostExecute(List<Movie> movies) {
            if (movies != null) {
                if (movieGridAdapter != null) {
                    movieGridAdapter.setData(movies);
                }
                movieList = new ArrayList<>();
                movieList.addAll(movies);
            }
        }
    }

}
