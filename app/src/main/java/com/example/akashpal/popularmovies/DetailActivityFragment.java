package com.example.akashpal.popularmovies;

import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.widget.CardView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.ScrollView;
import android.widget.TextView;

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
    private ReviewAdapter adapter;
    LinearListView linearListView;
    CardView cardView;

    public DetailActivityFragment() {

    }


    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
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
            release_date = mMovie.getRelease_date();
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
            mVoteAverage.setText(mMovie.getVote_average() + "/10");

            ImageView imageView = (ImageView) rootView.findViewById(R.id.imageView3);
            Uri imageUri = Uri.parse(mMovie.getPoster_path());
            Picasso.with(getActivity()).load(Constants.BASE_URL + imageUri.toString()).into(imageView);

            /**
             * Array List for Binding Data from JSON to this List
             */
            reviewList = new ArrayList<>();
            /**
             * Binding that List to Adapter
             */
            adapter = new ReviewAdapter(getContext(), reviewList);

            /**
             * Getting List and Setting List Adapter
             */
            linearListView = (LinearListView) rootView.findViewById(R.id.detail_reviews);
            linearListView.setAdapter(adapter);
            cardView = (CardView) rootView.findViewById(R.id.detail_reviews_cardview);


        }

        return rootView;
    }

    @Override
    public void onStart() {
        super.onStart();
        if (mMovie != null) {
            new GetReviewData().execute();
        }
    }


    class GetReviewData extends AsyncTask<Void, Void, Void> {

        @Override
        protected Void doInBackground(Void... params) {
            int movieId = Integer.parseInt(mMovie.getId());
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
                cardView.setVisibility(View.VISIBLE);
                adapter.notifyDataSetChanged();
            }
        }
    }
}
