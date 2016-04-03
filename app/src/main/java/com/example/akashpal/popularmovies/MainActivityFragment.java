package com.example.akashpal.popularmovies;

import android.content.Context;
import android.database.Cursor;
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

import com.example.akashpal.popularmovies.adapter.MovieGridAdapter;
import com.example.akashpal.popularmovies.data.MovieContract;
import com.example.akashpal.popularmovies.model.Movie;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

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
            Log.v(TAG, savedInstanceState + " is null ");
            updateMovies(mSortBy);
        } else {
            if (savedInstanceState.containsKey(Constants.SORT_KEY)) {
                mSortBy = savedInstanceState.getString(Constants.SORT_KEY);
            }
            if (savedInstanceState.containsKey(Constants.MOVIES_KEY)) {
                //get your data saved and populate the adapter here
                movieList = savedInstanceState.getParcelableArrayList(Constants.MOVIES_KEY);
                movieGridAdapter.setData(movieList);
                updateMovies(mSortBy);
            } else {
                updateMovies(mSortBy);
            }
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
        if (!mSortBy.contentEquals(Constants.SORT_BY_POPULAR)) {
            outState.putString(Constants.SORT_KEY, mSortBy);
        }
        if (movieList != null) {
            outState.putParcelableArrayList(Constants.MOVIES_KEY, movieList);
        }
        super.onSaveInstanceState(outState);
    }

    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {

        inflater.inflate(R.menu.menu_fragment_main, menu);
        MenuItem action_sort_by_popularity = menu.findItem(R.id.action_sort_by_popularity);
        MenuItem action_sort_by_rating = menu.findItem(R.id.action_sort_by_rating);
        MenuItem action_sort_by_favorite = menu.findItem(R.id.action_sort_by_favorite);
        if (mSortBy.contentEquals(Constants.SORT_BY_POPULAR)) {
            if (!action_sort_by_popularity.isChecked()) {
                action_sort_by_popularity.setChecked(true);
            }
        } else if (mSortBy.contentEquals(Constants.SORT_BY_TOP_RATED)) {
            if (!action_sort_by_rating.isChecked()) {
                action_sort_by_rating.setChecked(true);
            }
        } else if (mSortBy.contentEquals(Constants.SORT_BY_FAVORITE)) {
            if (!action_sort_by_favorite.isChecked()) {
                action_sort_by_favorite.setChecked(true);
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
                Toast.makeText(getActivity(), Constants.POPULAR, Toast.LENGTH_SHORT).show();
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
                Toast.makeText(getActivity(), Constants.TOP_RATED, Toast.LENGTH_SHORT).show();
                updateMovies(mSortBy);
                return true;
            case R.id.action_sort_by_favorite:
                if (item.isChecked()) {
                    item.setChecked(false);
                } else {
                    item.setChecked(true);
                }
                sort_title = Constants.FAVORITE;
                ((Callback) getActivity()).onFragmentInteraction(sort_title);
                mSortBy = Constants.SORT_BY_FAVORITE;
                Toast.makeText(getActivity(), Constants.FAVORITE, Toast.LENGTH_SHORT).show();
                updateMovies(mSortBy);
                return true;
        }
        return super.onOptionsItemSelected(item);
    }

    private void updateMovies(String sort_by) {
        if (!sort_by.contentEquals(Constants.SORT_BY_FAVORITE)) {
            new FetchMoviesTask().execute(sort_by);
        } else {
            new FetchFavoriteMoviesTask(getActivity()).execute();
        }
    }

    public class FetchFavoriteMoviesTask extends AsyncTask<Void, Void, List<Movie>> {
        private Context mContext;

        public FetchFavoriteMoviesTask(Context context) {
            mContext = context;
        }

        private List<Movie> getFavoriteMoviesDataFromCursor(Cursor cursor) {
            List<Movie> results = new ArrayList<>();
            if (cursor != null && cursor.moveToFirst()) {
                do {
                    Movie movie = new Movie(cursor);
                    results.add(movie);
                } while (cursor.moveToNext());
                cursor.close();
            }
            return results;
        }

        @Override
        protected List<Movie> doInBackground(Void... params) {
            Cursor cursor = mContext.getContentResolver().query(
                    MovieContract.MovieEntry.CONTENT_URI,
                    Constants.MOVIE_COLUMNS,
                    null,
                    null,
                    null
            );
            return getFavoriteMoviesDataFromCursor(cursor);
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

    public class FetchMoviesTask extends AsyncTask<String, Void, List<Movie>> {
        @Override
        protected List<Movie> doInBackground(String... params) {
            JSONObject movieJson = JSONParser.getMovies(params[0]);
            Log.v(TAG, params[0] + " " + movieJson + " ");
            if (movieJson != null) {
                if (movieJson.length() > 0) {
                    try {
                        JSONArray movieArray = movieJson.getJSONArray("results");
                        List<Movie> results = new ArrayList<>();
                        for (int i = 0; i < movieArray.length(); i++) {
                            JSONObject movie = movieArray.getJSONObject(i);
                            Movie movieModel = new Movie(movie);
                            results.add(movieModel);
                        }
                        return results;
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                }
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
