package com.example.myapplication;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import com.example.myapplication.R;


import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.example.myapplication.model.Message;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.Locale;

public class MessageAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {

    private static final int TYPE_SENT = 1;
    private static final int TYPE_RECEIVED = 2;

    private ArrayList<Message> messages;

    public MessageAdapter(){}

    public MessageAdapter(ArrayList<Message> messages) {
        this.messages = messages;
    }

    @Override
    public int getItemViewType(int position) {
        return messages.get(position).isSent() ? TYPE_SENT : TYPE_RECEIVED;
    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        if (viewType == TYPE_SENT) {
            View view = LayoutInflater.from(parent.getContext())
                    .inflate(R.layout.item_message_sent, parent, false);
            return new SentViewHolder(view);
        } else {
            View view = LayoutInflater.from(parent.getContext())
                    .inflate(R.layout.item_message_received, parent, false);
            return new ReceivedViewHolder(view);
        }
    }


    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, int position) {
        Message msg = messages.get(position);


        String timeString = new SimpleDateFormat("hh:mm a", Locale.getDefault())
                .format(new Date(msg.getTimestamp()));

        if (holder instanceof SentViewHolder) {
            ((SentViewHolder) holder).txtMessage.setText(msg.getText());
            ((SentViewHolder) holder).txtTime.setText(timeString);
            Glide.with(holder.itemView.getContext())
                    .load(msg.getProfileImage())
                    .circleCrop()
                    .placeholder(R.drawable.userprofile)
                    .into(((SentViewHolder) holder).imgProfile);
        } else {
            ((ReceivedViewHolder) holder).txtMessage.setText(msg.getText());
            ((ReceivedViewHolder) holder).txtTime.setText(timeString);
            Glide.with(holder.itemView.getContext())
                    .load(msg.getProfileImage())
                    .circleCrop()
                    .placeholder(R.drawable.userprofile)
                    .into(((ReceivedViewHolder) holder).imgProfile);
        }
    }


    @Override
    public int getItemCount() {
        return messages.size();
    }

    static class SentViewHolder extends RecyclerView.ViewHolder {
        TextView txtMessage, txtTime;
        ImageView imgProfile;

        SentViewHolder(View itemView) {
            super(itemView);
            txtMessage = itemView.findViewById(R.id.tvMessage);
            txtTime = itemView.findViewById(R.id.txtTime);
            imgProfile = itemView.findViewById(R.id.imgProfile);
        }
    }

    static class ReceivedViewHolder extends RecyclerView.ViewHolder {
        TextView txtMessage, txtTime;
        ImageView imgProfile;

        ReceivedViewHolder(View itemView) {
            super(itemView);
            txtMessage = itemView.findViewById(R.id.tvMessage);
            txtTime = itemView.findViewById(R.id.txtTime);
            imgProfile = itemView.findViewById(R.id.imgProfile);
        }
    }
}
