package com.gf.movie.reminder.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.GridView;
import android.widget.ImageView;
import android.widget.TextView;

import com.gf.movie.reminder.R;
import com.gf.movie.reminder.data.model.Game;
import com.gf.movie.reminder.data.model.GameReminder;
import com.gf.movie.reminder.data.model.Reminder;
import com.squareup.picasso.Picasso;

import java.util.List;

public class RemindersGridAdapter extends GridSelectionAdapter {

    private Picasso mPicasso;
    private List<? extends Reminder> mReminders;

    public RemindersGridAdapter(Context context, List<? extends Reminder> reminders, Picasso picasso) {
        super(context);
        mPicasso = picasso;
        mReminders = reminders;
    }

    public void setReminders(List<? extends Reminder> reminders) {
        mReminders = reminders;
        notifyDataSetChanged();
    }

    @Override
    public Reminder getItem(int position) {
        return mReminders.get(position);
    }

    @Override
    public int getCount() {
        return mReminders.size();
    }

    @Override
    public boolean hasStableIds() {
        return true;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        if (convertView == null) {
            LayoutInflater inflater = (LayoutInflater) getContext().getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            convertView = inflater.inflate(R.layout.layout_reminder_item, null);
            convertView.setLayoutParams(new GridView.LayoutParams(GridView.AUTO_FIT, getContext().getResources().getDimensionPixelSize(R.dimen.trailer_item_height)));
        }

        Reminder reminder = getItem(position);
        ImageView iv = (ImageView) convertView.findViewById(R.id.reminder_image_url);
        TextView tv = (TextView) convertView.findViewById(R.id.reminder_title);
        TextView tv2 = (TextView) convertView.findViewById(R.id.reminder_release_date);
        TextView tv3 = (TextView) convertView.findViewById(R.id.trailer_released);

        mPicasso.load(reminder.getTrailer().getImageUrl())
                .resize(250, 350)
                .centerCrop()
                .placeholder(R.drawable.img_photo_loading_small)
                .error(R.drawable.img_failed_to_receive_small)
                .into(iv);

        tv.setText(reminder.getTrailer().getTitleString());
        tv2.setText(reminder.getTrailer().getReleaseDateString());
        if (reminder.getTrailer().isReleased()) {
            tv3.setVisibility(View.VISIBLE);
            tv3.setText(reminder.getTrailer().getReleaseCellString(getContext()));
        } else {
            tv3.setVisibility(View.GONE);
        }

        if (reminder instanceof GameReminder) {
            setupGameConsoles(convertView, (Game) (reminder.getTrailer()));
        } else {
            convertView.findViewById(R.id.game_xbox).setVisibility(View.GONE);
            convertView.findViewById(R.id.game_ps).setVisibility(View.GONE);
            convertView.findViewById(R.id.game_steam).setVisibility(View.GONE);
            convertView.findViewById(R.id.game_pc).setVisibility(View.GONE);
        }

        if (isSelected(position)) {
            convertView.findViewById(R.id.reminder_checked).setVisibility(View.VISIBLE);
        } else {
            convertView.findViewById(R.id.reminder_checked).setVisibility(View.INVISIBLE);
        }

        return convertView;
    }

    private void setupGameConsoles(View view, Game game) {
        switch (game.getConsole()) {
            case XBOX:
            case XBOX_360:
            case XBOX_ONE:
                setConsoleVisibilities(view, true, false, false, false);
                break;
            case XBOX_STEAM:
                setConsoleVisibilities(view, true, false, true, false);
                break;
            case XBOX_PC:
                setConsoleVisibilities(view, true, false, false, true);
                break;
            case PLAYSTATION:
            case PS3:
            case PS4:
                setConsoleVisibilities(view, false, true, false, false);
                break;
            case PLAYSTATION_XBOX:
                setConsoleVisibilities(view, true, true, false, false);
                break;
            case PLAYSTATION_STEAM:
                setConsoleVisibilities(view, false, true, true, false);
                break;
            case PLAYSTATION_PC:
                setConsoleVisibilities(view, false, true, false, true);
                break;
            case STEAM:
                setConsoleVisibilities(view, false, false, true, false);
                break;
            case PC:
                setConsoleVisibilities(view, false, false, false, true);
                break;
            case ALL:
                setConsoleVisibilities(view, true, true, true, true);
                break;
            case NONE:
                setConsoleVisibilities(view, false, false, false, false);
                break;
        }
    }

    private void setConsoleVisibilities(View view, boolean xboxVis, boolean psVis, boolean steamVis, boolean pcVis) {
        view.findViewById(R.id.game_xbox).setVisibility(xboxVis ? View.VISIBLE : View.GONE);
        view.findViewById(R.id.game_ps).setVisibility(psVis ? View.VISIBLE : View.GONE);
        view.findViewById(R.id.game_steam).setVisibility(steamVis ? View.VISIBLE : View.GONE);
        view.findViewById(R.id.game_pc).setVisibility(pcVis ? View.VISIBLE : View.GONE);
    }

    @Override
    public void remove(Object object) {
        mReminders.remove(object);
        notifyDataSetChanged();
    }
}
