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
import com.gf.movie.reminder.data.model.Reminder;
import com.gf.movie.reminder.data.model.Trailer;
import com.gf.movie.reminder.fragment.base.BaseTrailerTopDragFragment;
import com.gf.movie.reminder.ui.Fab;
import com.squareup.picasso.Picasso;

import javax.inject.Inject;

public class GameTrailerTopDragFragment extends BaseTrailerTopDragFragment implements View.OnClickListener {

    public static final String TAG = "game_trailer_top";

    @Inject
    Picasso mPicasso;

    private View mGamePlay;
    private ImageView mGameImage;
    private Fab mXboxView;
    private Fab mPlaystationView;
    private Fab mSteamView;
    private Fab mPCView;

    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_game_trailer_top_drag, container, false);
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        mGamePlay = view.findViewById(R.id.game_play);
        mGameImage = (ImageView) view.findViewById(R.id.game_image_url);

        mXboxView = (Fab) view.findViewById(R.id.game_xbox);
        mPlaystationView = (Fab) view.findViewById(R.id.game_ps);
        mSteamView = (Fab) view.findViewById(R.id.game_steam);
        mPCView = (Fab) view.findViewById(R.id.game_pc);

        mGamePlay.setOnClickListener(this);
    }

    @Override
    public void updateWithReminder(Reminder reminder) {
        super.updateWithReminder(reminder);
        update();
    }

    @Override
    public void updateWithTrailer(Trailer trailer) {
        super.updateWithTrailer(trailer);
        update();
    }

    @Override
    public void onClick(View v) {
        if (v.getId() == mGamePlay.getId()) {
            startActivity(new Intent(Intent.ACTION_VIEW, Uri.parse(mTrailer.getVideoUrl())));
        }
    }

    private void update() {
        mPicasso.load(mTrailer.getImageUrl())
//                .centerCrop()
                .placeholder(R.drawable.img_photo_loading_small)
                .error(R.drawable.img_failed_to_receive_small)
                .into(mGameImage);

        TextView releasedTV = (TextView) getView().findViewById(R.id.trailer_released);
        if (mTrailer.isReleased()) {
            releasedTV.setVisibility(View.VISIBLE);
            releasedTV.setText(getString(R.string.trailers_game_released));
        } else {
            releasedTV.setVisibility(View.GONE);
        }

        switch (((Game) mTrailer).getConsole()) {
            case XBOX:
            case XBOX_360:
            case XBOX_ONE:
                setConsoleVisibilities(true, false, false, false);
                break;
            case XBOX_STEAM:
                setConsoleVisibilities(true, false, true, false);
                break;
            case XBOX_PC:
                setConsoleVisibilities(true, false, false, true);
                break;
            case PLAYSTATION:
            case PS3:
            case PS4:
                setConsoleVisibilities(false, true, false, false);
                break;
            case PLAYSTATION_XBOX:
                setConsoleVisibilities(true, true, false, false);
                break;
            case PLAYSTATION_STEAM:
                setConsoleVisibilities(false, true, true, false);
                break;
            case PLAYSTATION_PC:
                setConsoleVisibilities(false, true, false, true);
                break;
            case STEAM:
                setConsoleVisibilities(false, false, true, false);
                break;
            case PC:
                setConsoleVisibilities(false, false, false, true);
                break;
            case ALL:
                setConsoleVisibilities(true, true, true, true);
                break;
        }
    }

    private void setConsoleVisibilities(boolean xboxVis, boolean psVis, boolean steamVis, boolean pcVis) {
        mXboxView.setVisibility(xboxVis ? View.VISIBLE : View.GONE);
        mPlaystationView.setVisibility(psVis ? View.VISIBLE : View.GONE);
        mSteamView.setVisibility(steamVis ? View.VISIBLE : View.GONE);
        mPCView.setVisibility(pcVis ? View.VISIBLE : View.GONE);
    }
}
