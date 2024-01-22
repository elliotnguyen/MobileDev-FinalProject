package com.example.edupro.ui.chat;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.edupro.model.MessageDao;

import com.example.edupro.R;

import java.util.ArrayList;

public class MessageAdapter extends RecyclerView.Adapter {
    private static final int VIEW_TYPE_MESSAGE_SENT = 1;
    private static final int VIEW_TYPE_MESSAGE_RECEIVED = 2;
    private final ArrayList<MessageDao> messageArrayList;

    public MessageAdapter(ArrayList<MessageDao> messageArrayList) {
        this.messageArrayList = messageArrayList;
    }

    @NonNull
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        if (viewType == VIEW_TYPE_MESSAGE_SENT) {
            View view = LayoutInflater.from(parent.getContext())
                    .inflate(R.layout.sender_message_viewholder, parent, false);
            return new SenderMessageViewHolder(view);
        } else {
            View view = LayoutInflater.from(parent.getContext())
                    .inflate(R.layout.receiver_message_viewholder_normal, parent, false);
            return new ReceiverMessageViewHolder(view);
        }
    }

    @Override
    public void onBindViewHolder(@NonNull RecyclerView.ViewHolder holder, int position) {
        if (holder instanceof SenderMessageViewHolder) {
            ((SenderMessageViewHolder) holder).senderMessageTextView.setText(messageArrayList.get(position).getMessage());
        } else {
            ((ReceiverMessageViewHolder) holder).receiverMessageTextView.setText(messageArrayList.get(position).getMessage());
        }
    }

    @Override
    public int getItemCount() {
        return messageArrayList.size();
    }

    @Override
    public int getItemViewType(int position) {
        MessageDao message = messageArrayList.get(position);
        if (message.getRole() == 1) {
            return VIEW_TYPE_MESSAGE_SENT;
        } else {
            return VIEW_TYPE_MESSAGE_RECEIVED;
        }
    }

    public static class SenderMessageViewHolder extends RecyclerView.ViewHolder {
        TextView senderMessageTextView;
        public SenderMessageViewHolder(@NonNull View itemView) {
            super(itemView);
            senderMessageTextView = itemView.findViewById(R.id.sender_message_text);
        }
    }

    public static class ReceiverMessageViewHolder extends RecyclerView.ViewHolder {
        TextView receiverMessageTextView;
        public ReceiverMessageViewHolder(@NonNull View itemView) {
            super(itemView);
            receiverMessageTextView = itemView.findViewById(R.id.receiver_message_text);
        }
    }
}
