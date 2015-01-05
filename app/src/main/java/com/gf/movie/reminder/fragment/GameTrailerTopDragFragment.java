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
import com.gf.movie.reminder.data.model.Game;
import com.gf.movie.reminder.data.model.GameReminder;
import com.gf.movie.reminder.fragment.base.BaseFragment;
import com.squareup.picasso.Picasso;

import javax.inject.Inject;

public class GameTrailerTopDragFragment extends BaseFragment implements View.OnClickListener {

    public static final String TAG = "game_trailer_top";

    @Inject
    Picasso mPicasso;

    private Game mGame;
    private View mGamePlay;
    private ImageView mGameImage;

    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_game_trailer_top_drag, container, false);
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        mGamePlay = view.findViewById(R.id.game_play);
        mGameImage = (ImageView) view.findViewById(R.id.game_image_url);

        mGamePlay.setOnClickListener(this);
    }

    public void updateWithReminder(GameReminder reminder) {
        mGame = (Game) reminder.getTrailer();
        update();
    }

    public void updateWithGame(Game game) {
        mGame = game;
        update();
    }

    private void update() {
        mPicasso.load(mGame.getImageUrl())
//                .centerCrop()
                .placeholder(R.drawable.img_photo_loading_small)
                .error(R.drawable.img_failed_to_receive_small)
                .into(mGameImage);

        TextView releasedTV = (TextView) getView().findViewById(R.id.trailer_released);
        if (mGame.isReleased()) {
            releasedTV.setVisibility(View.VISIBLE);
            releasedTV.setText(getString(R.string.trailers_game_released));
        } else {
            releasedTV.setVisibility(View.GONE);
        }
    }

    @Override
    public void onClick(View v) {
        if (v.getId() == mGamePlay.getId()) {
            startActivity(new Intent(Intent.ACTION_VIEW, Uri.parse(mGame.getVideoUrl())));
        }
    }
}
