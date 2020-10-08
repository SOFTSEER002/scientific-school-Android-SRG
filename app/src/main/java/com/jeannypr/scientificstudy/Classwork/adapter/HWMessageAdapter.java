package com.jeannypr.scientificstudy.Classwork.adapter;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.recyclerview.widget.RecyclerView;

import com.jeannypr.scientificstudy.Classwork.model.HWMessage;
import com.jeannypr.scientificstudy.R;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Locale;
import java.util.TimeZone;

/**
 * Created by Varun John on 4 Dec, 2018
 * Github : https://github.com/varunjohn
 */
public class HWMessageAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {

    private List<HWMessage> messages = new ArrayList<>();
    private static SimpleDateFormat timeFormatter;
    HWMessageAdapter.MessageInterface mListner;

    public HWMessageAdapter(MessageInterface listner) {
        mListner = listner;
        timeFormatter = new SimpleDateFormat("m:ss", Locale.getDefault());
    }

    public void add(HWMessage message) {
        messages.add(message);
        notifyItemInserted(messages.lastIndexOf(message));
    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.list_message, null);
        return new MessageViewHolder(view);
    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, int position) {
        ((MessageViewHolder) holder).bind(messages.get(position));
    }

    @Override
    public int getItemCount() {
        return messages.size();
    }

    public static String getAudioTime(long time) {
        time *= 1000;
        timeFormatter.setTimeZone(TimeZone.getTimeZone("UTC"));
        return timeFormatter.format(new Date(time));
    }

    public class MessageViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {

        public TextView text;

        public MessageViewHolder(View view) {
            super(view);
            text = itemView.findViewById(R.id.textView);
        }

        public void bind(final HWMessage message) {
            if (message.type == HWMessage.TYPE_AUDIO) {
                text.setCompoundDrawablesWithIntrinsicBounds(R.drawable.mic_small, 0, 0, 0);
                text.setText(String.valueOf(getAudioTime(message.time)));

                text.setOnClickListener(this);

            } else if (message.type == HWMessage.TYPE_TEXT) {
                text.setCompoundDrawablesWithIntrinsicBounds(0, 0, 0, 0);
                text.setText(message.text);

            } else {
                text.setCompoundDrawablesWithIntrinsicBounds(0, 0, 0, 0);
                text.setText("");
            }
        }

        @Override
        public void onClick(View v) {
            switch (v.getId()) {
                case R.id.textView:
                    mListner.onClickAudio();
                    break;
            }
        }
    }

    public interface MessageInterface {
        void onClickAudio();
    }
}