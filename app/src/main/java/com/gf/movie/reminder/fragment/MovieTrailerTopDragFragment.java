package com.gf.movie.reminder.fragment;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.gf.movie.reminder.R;
import com.gf.movie.reminder.data.model.Movie;
import com.gf.movie.reminder.data.model.MovieReminder;
import com.gf.movie.reminder.fragment.base.BaseFragment;
import com.squareup.picasso.Picasso;

import javax.inject.Inject;

public class MovieTrailerTopDragFragment extends BaseFragment implements View.OnClickListener {

    public static final String TAG = "movie_trailer_top";

    @Inject
    Picasso mPicasso;

    private Movie mMovie;
    private View mMoviePlay;
    private ImageView mMovieImage;

    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_movie_trailer_top_drag, container, false);
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        mMoviePlay = view.findViewById(R.id.movie_play);
        mMovieImage = (ImageView) view.findViewById(R.id.movie_image_url);

        mMoviePlay.setOnClickListener(this);
    }

    public void updateWithReminder(MovieReminder reminder) {
        mMovie = (Movie) reminder.getTrailer();
        update();
    }

    public void updateWithMovie(Movie movie) {
        mMovie = movie;
        update();
    }

    private void update() {
        mPicasso.load(mMovie.getImageUrl())
//                .centerCrop()
                .placeholder(R.drawable.img_photo_loading_small)
                .error(R.drawable.img_failed_to_receive_small)
                .into(mMovieImage);

        TextView releasedTV = (TextView) getView().findViewById(R.id.trailer_released);
        if (mMovie.isReleased()) {
            releasedTV.setVisibility(View.VISIBLE);
            releasedTV.setText(getString(R.string.trailers_movie_in_theaters));
        } else {
            releasedTV.setVisibility(View.GONE);
        }
    }

    @Override
    public void onClick(View v) {
        if (v.getId() == mMoviePlay.getId()) {
            startActivity(new Intent(Intent.ACTION_VIEW, Uri.parse(mMovie.getVideoUrl())));
        }
    }
}
