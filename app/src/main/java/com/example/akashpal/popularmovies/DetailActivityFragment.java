package com.example.akashpal.popularmovies;

import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.ScrollView;
import android.widget.TextView;

import com.squareup.picasso.Picasso;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;


public class DetailActivityFragment extends Fragment {

    public static final String TAG = DetailActivityFragment.class.getSimpleName();

    private String release_date;
    TextView mTitle;
    TextView mOverview;
    Movie mMovie;
    private ScrollView mDetailLayout;

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

        }

        return rootView;
    }
}
