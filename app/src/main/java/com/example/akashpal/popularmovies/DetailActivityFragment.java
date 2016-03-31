package com.example.akashpal.popularmovies;

import android.content.ContentValues;
import android.content.Intent;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.view.MenuItemCompat;
import android.support.v7.widget.CardView;
import android.support.v7.widget.ShareActionProvider;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.ScrollView;
import android.widget.TextView;
import android.widget.Toast;

import com.example.akashpal.popularmovies.adapter.ReviewAdapter;
import com.example.akashpal.popularmovies.adapter.TrailerAdapter;
import com.example.akashpal.popularmovies.data.MovieContract;
import com.example.akashpal.popularmovies.model.Movie;
import com.example.akashpal.popularmovies.model.Review;
import com.example.akashpal.popularmovies.model.Trailer;
import com.linearlistview.LinearListView;
import com.squareup.picasso.Picasso;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;


public class DetailActivityFragment extends Fragment {

    public static final String TAG = DetailActivityFragment.class.getSimpleName();

    private String release_date;
    TextView mTitle;
    TextView mOverview;
    Movie mMovie;
    private ScrollView mDetailLayout;

    ArrayList<Review> reviewList;
    private ReviewAdapter reviewAdapter;
    LinearListView reviewLinearListView;
    CardView reviewCardView;

    ArrayList<Trailer> trailerList;
    private TrailerAdapter trailerAdapter;
    LinearListView trailerLinearListView;
    CardView trailerCardView;

    private Toast mToast;

    private ShareActionProvider mShareActionProvider;
    private Trailer mTrailer;

    public DetailActivityFragment() {

    }


    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setHasOptionsMenu(true);
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        Bundle arguments = getArguments();
        if (arguments != null) {
            mMovie = arguments.getParcelable(Constants.DETAIL_MOVIE);
        }

        View rootView = inflater.inflate(R.layout.fragment_detail, container, false);

        mDetailLayout = (ScrollView) rootView.findViewById(R.id.detail_layout);

        if (mMovie != null) {
            mDetailLayout.setVisibility(View.VISIBLE);
        } else {
            mDetailLayout.setVisibility(View.INVISIBLE);
        }


        if (mMovie != null) {
            mTitle = (TextView) rootView.findViewById(R.id.title);
            mTitle.setText(mMovie.getTitle());

            mOverview = (TextView) rootView.findViewById(R.id.overview);
            mOverview.setText(mMovie.getOverview());


            SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd");
            release_date = mMovie.getDate();
            try {
                Date date = format.parse(release_date);
                Calendar calendar = Calendar.getInstance();
                calendar.setTime(date);
                TextView mReleaseDate = (TextView) rootView.findViewById(R.id.releaseDate);
                mReleaseDate.setText(calendar.get(Calendar.YEAR) + "");

            } catch (ParseException e) {
                e.printStackTrace();
            }


            TextView mVoteAverage = (TextView) rootView.findViewById(R.id.voteAvergae);
            mVoteAverage.setText(mMovie.getRating() + "/10");

            ImageView imageView = (ImageView) rootView.findViewById(R.id.imageView3);
            Uri imageUri = Uri.parse(mMovie.getImage());
            Picasso.with(getActivity()).load(Constants.BASE_URL + imageUri.toString()).into(imageView);

            /**
             * Array List for Binding Data from JSON to this List
             */
            reviewList = new ArrayList<>();
            trailerList = new ArrayList<>();
            /**
             * Binding that List to Adapter
             */
            reviewAdapter = new ReviewAdapter(getContext(), reviewList);
            trailerAdapter = new TrailerAdapter(getContext(), trailerList);

            /**
             * Getting List and Setting List Adapter
             */
            reviewLinearListView = (LinearListView) rootView.findViewById(R.id.detail_reviews);
            reviewLinearListView.setAdapter(reviewAdapter);
            reviewCardView = (CardView) rootView.findViewById(R.id.detail_reviews_cardview);

            trailerLinearListView = (LinearListView) rootView.findViewById(R.id.detail_trailers);
            trailerLinearListView.setAdapter(trailerAdapter);
            trailerCardView = (CardView) rootView.findViewById(R.id.detail_trailers_cardview);

            trailerLinearListView.setOnItemClickListener(new LinearListView.OnItemClickListener() {
                @Override
                public void onItemClick(LinearListView linearListView, View view,
                                        int position, long id) {
                    Trailer trailer = trailerAdapter.getItem(position);
                    Intent intent = new Intent(Intent.ACTION_VIEW);
                    intent.setData(Uri.parse("http://www.youtube.com/watch?v=" + trailer.getKey()));
                    startActivity(intent);
                }
            });


        }

        return rootView;
    }

    @Override
    public void onStart() {
        super.onStart();
        if (mMovie != null) {
            new GetReviewData().execute();
            new GetTrailerData().execute();
        }
    }

    class GetReviewData extends AsyncTask<Void, Void, Void> {

        @Override
        protected Void doInBackground(Void... params) {
            int movieId = mMovie.getId();
            JSONObject jsonObject = JSONParser.getReviewById(movieId);
            Log.v(TAG, jsonObject + " ");

            if (jsonObject != null) {
                if (jsonObject.length() > 0) {
                    try {
                        JSONArray results = jsonObject.getJSONArray(Constants.TAG_REVIEW_RESULTS);
                        if (results.length() > 0) {
                            for (int j = 0; j < results.length(); j++) {

                                JSONObject reviewObject = results.getJSONObject(j);
                                Review review = new Review();
                                review.setContent(reviewObject.getString(Constants.TAG_REVIEW_CONTENT));
                                reviewList.add(review);
                            }
                        }

                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                }
            }
            return null;
        }

        @Override
        protected void onPostExecute(Void aVoid) {
            super.onPostExecute(aVoid);
            if (reviewList.size() > 0) {
                reviewCardView.setVisibility(View.VISIBLE);
                reviewAdapter.notifyDataSetChanged();
            }
        }
    }

    class GetTrailerData extends AsyncTask<Void, Void, Void> {

        @Override
        protected Void doInBackground(Void... params) {
            int movieId = mMovie.getId();
            JSONObject jsonObject = JSONParser.getTrailerById(movieId);
            Log.v(TAG, jsonObject + " ");
            if (jsonObject != null) {
                if (jsonObject.length() > 0) {
                    try {
                        JSONArray results = jsonObject.getJSONArray(Constants.TAG_TRAILER_RESULTS);
                        if (results.length() > 0) {
                            for (int j = 0; j < results.length(); j++) {

                                JSONObject trailerObject = results.getJSONObject(j);

                                if (trailerObject.getString(Constants.TAG_TRAILER_SITE).contentEquals("YouTube")) {
                                    Trailer trailer = new Trailer(trailerObject);
                                    trailerList.add(trailer);
                                }
                            }
                        }

                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                }
            }
            return null;
        }

        @Override
        protected void onPostExecute(Void aVoid) {
            super.onPostExecute(aVoid);

            if (trailerList.size() > 0) {
                trailerCardView.setVisibility(View.VISIBLE);
                trailerAdapter.notifyDataSetChanged();
                mTrailer = trailerList.get(0);
                Log.v(TAG, mTrailer + "");
                if (mShareActionProvider != null) {
                    mShareActionProvider.setShareIntent(createShareMovieIntent());
                }
            }
        }
    }


    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        if (mMovie != null) {
            inflater.inflate(R.menu.menu_fragment_detail, menu);

            final MenuItem action_favorite = menu.findItem(R.id.action_favorite);
            final MenuItem action_share = menu.findItem(R.id.action_share);

            new AsyncTask<Void, Void, Integer>() {
                @Override
                protected Integer doInBackground(Void... params) {
                    return Utility.isFavorited(getActivity(), mMovie.getId());
                }

                @Override
                protected void onPostExecute(Integer isFavorited) {
                    action_favorite.setIcon(isFavorited == 1 ?
                            R.drawable.abc_btn_rating_star_on_mtrl_alpha :
                            R.drawable.abc_btn_rating_star_off_mtrl_alpha);
                }
            }.execute();


            mShareActionProvider = (ShareActionProvider) MenuItemCompat.getActionProvider(action_share);

            if (mTrailer != null) {
                mShareActionProvider.setShareIntent(createShareMovieIntent());
            }
        }
    }

    private Intent createShareMovieIntent() {
        Intent shareIntent = new Intent(Intent.ACTION_SEND);
        shareIntent.setType("text/plain");
        shareIntent.addFlags(Intent.FLAG_ACTIVITY_NEW_DOCUMENT);
        shareIntent.putExtra(Intent.EXTRA_TEXT, mMovie.getTitle() + " " +
                "http://www.youtube.com/watch?v=" + mTrailer.getKey());
        return shareIntent;
    }

    @Override
    public boolean onOptionsItemSelected(final MenuItem item) {
        int id = item.getItemId();
        switch (id) {
            case R.id.action_favorite:
                if (mMovie != null) {
                    // check if movie is in favorites or not
                    new AsyncTask<Void, Void, Integer>() {

                        @Override
                        protected Integer doInBackground(Void... params) {
                            return Utility.isFavorited(getActivity(), mMovie.getId());
                        }

                        @Override
                        protected void onPostExecute(Integer isFavorited) {
                            // if it is in favorites
                            if (isFavorited == 1) {
                                // delete from favorites
                                new AsyncTask<Void, Void, Integer>() {
                                    @Override
                                    protected Integer doInBackground(Void... params) {
                                        return getActivity().getContentResolver().delete(
                                                MovieContract.MovieEntry.CONTENT_URI,
                                                MovieContract.MovieEntry.COLUMN_MOVIE_ID + " = ?",
                                                new String[]{Integer.toString(mMovie.getId())}
                                        );
                                    }

                                    @Override
                                    protected void onPostExecute(Integer rowsDeleted) {
                                        item.setIcon(R.drawable.abc_btn_rating_star_off_mtrl_alpha);
                                        if (mToast != null) {
                                            mToast.cancel();
                                        }
                                        mToast = Toast.makeText(getActivity(), getString(R.string.removed_from_favorites), Toast.LENGTH_SHORT);
                                        mToast.show();
                                    }
                                }.execute();
                            }
                            // if it is not in favorites
                            else {
                                // add to favorites
                                new AsyncTask<Void, Void, Uri>() {
                                    @Override
                                    protected Uri doInBackground(Void... params) {
                                        ContentValues values = new ContentValues();

                                        values.put(MovieContract.MovieEntry.COLUMN_MOVIE_ID, mMovie.getId());
                                        values.put(MovieContract.MovieEntry.COLUMN_TITLE, mMovie.getTitle());
                                        values.put(MovieContract.MovieEntry.COLUMN_IMAGE, mMovie.getImage());
                                        values.put(MovieContract.MovieEntry.COLUMN_IMAGE2, mMovie.getImage2());
                                        values.put(MovieContract.MovieEntry.COLUMN_OVERVIEW, mMovie.getOverview());
                                        values.put(MovieContract.MovieEntry.COLUMN_RATING, mMovie.getRating());
                                        values.put(MovieContract.MovieEntry.COLUMN_DATE, mMovie.getDate());

                                        return getActivity().getContentResolver().insert(MovieContract.MovieEntry.CONTENT_URI,
                                                values);
                                    }

                                    @Override
                                    protected void onPostExecute(Uri returnUri) {
                                        item.setIcon(R.drawable.abc_btn_rating_star_on_mtrl_alpha);
                                        if (mToast != null) {
                                            mToast.cancel();
                                        }
                                        mToast = Toast.makeText(getActivity(), getString(R.string.added_to_favorites), Toast.LENGTH_SHORT);
                                        mToast.show();
                                    }
                                }.execute();
                            }
                        }
                    }.execute();
                }
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }

}
