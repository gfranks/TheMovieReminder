package com.gf.movie.reminder.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.GridView;
import android.widget.ImageView;
import android.widget.TextView;

import com.gf.movie.reminder.R;
import com.gf.movie.reminder.data.model.Trailer;
import com.squareup.picasso.Picasso;

import java.util.List;

public class TrailersGridAdapter extends GridSelectionAdapter {

    private Picasso mPicasso;
    private List<? extends Trailer> mTrailers;

    public TrailersGridAdapter(Context context, List<? extends Trailer> trailers, Picasso picasso) {
        super(context);
        mPicasso = picasso;
        mTrailers = trailers;
    }

    public void setTrailers(List<? extends Trailer> trailers) {
        mTrailers = trailers;
        notifyDataSetChanged();
    }

    @Override
    public Trailer getItem(int position) {
        return mTrailers.get(position);
    }

    @Override
    public int getCount() {
        return mTrailers.size();
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        if (convertView == null) {
            LayoutInflater inflater = (LayoutInflater) getContext().getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            convertView = inflater.inflate(R.layout.layout_trailer_item, null);
            convertView.setLayoutParams(new GridView.LayoutParams(GridView.AUTO_FIT, getContext().getResources().getDimensionPixelSize(R.dimen.trailer_item_height)));
        }

        Trailer trailer = getItem(position);
        ImageView iv = (ImageView) convertView.findViewById(R.id.trailer_image_url);
        TextView tv = (TextView) convertView.findViewById(R.id.trailer_title);
        TextView tv2 = (TextView) convertView.findViewById(R.id.trailer_release_date);
        TextView tv3 = (TextView) convertView.findViewById(R.id.trailer_released);

        mPicasso.load(trailer.getImageUrl())
                .resize(200, 250)
//                .centerCrop()
                .placeholder(R.drawable.img_photo_loading_small)
                .error(R.drawable.img_failed_to_receive_small)
                .into(iv);
        tv.setText(trailer.getTitleString());
        tv2.setText(trailer.getReleaseDateString());

        if (trailer.isReleased()) {
            tv3.setVisibility(View.VISIBLE);
            tv3.setText(trailer.getReleaseCellString(getContext()));
        } else {
            tv3.setVisibility(View.GONE);
        }

        if (isSelected(position)) {
            convertView.findViewById(R.id.trailer_checked).setVisibility(View.VISIBLE);
        } else {
            convertView.findViewById(R.id.trailer_checked).setVisibility(View.INVISIBLE);
        }

        return convertView;
    }

    @Override
    public void remove(Object object) {
    }
}
