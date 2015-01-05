package com.gf.movie.reminder.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.GridView;
import android.widget.ImageView;
import android.widget.TextView;

import com.gf.movie.reminder.R;
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
                .resize(200, 250)
//                .centerCrop()
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

        if (isSelected(position)) {
            convertView.findViewById(R.id.reminder_checked).setVisibility(View.VISIBLE);
        } else {
            convertView.findViewById(R.id.reminder_checked).setVisibility(View.INVISIBLE);
        }

        return convertView;
    }

    @Override
    public void remove(Object object) {
        mReminders.remove(object);
        notifyDataSetChanged();
    }
}
