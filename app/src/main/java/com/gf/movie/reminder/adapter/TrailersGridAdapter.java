package com.gf.movie.reminder.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.GridView;
import android.widget.ImageView;
import android.widget.TextView;

import com.gf.movie.reminder.R;
import com.gf.movie.reminder.data.model.Movie;
import com.squareup.picasso.Picasso;

import java.util.List;

public class TrailersGridAdapter extends GridSelectionAdapter {

    private Picasso mPicasso;
    private List<Movie> mMovies;

    public TrailersGridAdapter(Context context, List<Movie> movies, Picasso picasso) {
        super(context);
        mPicasso = picasso;
        mMovies = movies;
    }

    public void setMovies(List<Movie> movies) {
        mMovies = movies;
        notifyDataSetChanged();
    }

    @Override
    public Movie getItem(int position) {
        return mMovies.get(position);
    }

    @Override
    public int getCount() {
        return mMovies.size();
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        if (convertView == null) {
            LayoutInflater inflater = (LayoutInflater) getContext().getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            convertView = inflater.inflate(R.layout.layout_movie_item, null);
            convertView.setLayoutParams(new GridView.LayoutParams(GridView.AUTO_FIT, getContext().getResources().getDimensionPixelSize(R.dimen.movie_item_height)));
        }

        Movie movie = getItem(position);
        ImageView iv = (ImageView) convertView.findViewById(R.id.movie_image_url);
        TextView tv = (TextView) convertView.findViewById(R.id.movie_title);
        TextView tv2 = (TextView) convertView.findViewById(R.id.movie_release_date);
        TextView tv3 = (TextView) convertView.findViewById(R.id.movie_released);

        mPicasso.load(movie.getImageUrl())
                .resize(200, 250)
//                .centerCrop()
                .placeholder(R.drawable.img_photo_loading_small)
                .error(R.drawable.img_failed_to_receive_small)
                .into(iv);
        tv.setText(movie.getTitleString());
        tv2.setText(movie.getReleaseDateString());

        if (movie.isInTheaters()) {
            tv3.setVisibility(View.VISIBLE);
            tv3.setText(getContext().getString(R.string.trailers_movie_in_theaters));
        } else {
            tv3.setVisibility(View.GONE);
        }

        if (isSelected(position)) {
            convertView.findViewById(R.id.movie_checked).setVisibility(View.VISIBLE);
        } else {
            convertView.findViewById(R.id.movie_checked).setVisibility(View.INVISIBLE);
        }

        return convertView;
    }

    @Override
    public void remove(Object object) {
    }
}
