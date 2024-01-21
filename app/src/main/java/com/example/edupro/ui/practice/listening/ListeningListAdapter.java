package com.example.edupro.ui.practice.listening;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.recyclerview.widget.RecyclerView;

import com.example.edupro.R;
import com.example.edupro.model.listening.ListeningDto;
import com.example.edupro.model.reading.ReadingDto;
import com.example.edupro.ui.RecyclerViewClickInterface;

import java.util.ArrayList;
import java.util.List;

public class ListeningListAdapter extends RecyclerView.Adapter<ListeningListAdapter.ViewHolder>{
    private static final String TAG = "ListeningListAdapter";
    ArrayList<ListeningDto> listenings;
    RecyclerViewClickInterface recyclerViewClickInterface;

    public ListeningListAdapter(List<ListeningDto> listenings, RecyclerViewClickInterface recyclerViewClickInterface) {
        this.listenings = (ArrayList<ListeningDto>) listenings;
        this.recyclerViewClickInterface = recyclerViewClickInterface;
    }

    @Override
    public ListeningListAdapter.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.listening_list_viewholder, parent, false);
        return new ListeningListAdapter.ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(ListeningListAdapter.ViewHolder holder, int position) {
        holder.bind(listenings.get(position));

        holder.action.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                recyclerViewClickInterface.onItemClick(position);
            }
        });
    }

    @Override
    public int getItemCount() {
        return listenings.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        private TextView title, progress, action;

        public ViewHolder(View itemView) {
            super(itemView);
            title = itemView.findViewById(R.id.listening_list_viewholder_title);
            progress = itemView.findViewById(R.id.listening_list_viewholder_progressbar_text);
            action = itemView.findViewById(R.id.listening_list_viewholder_action_btn);
        }

        public void bind(ListeningDto listeningDto) {
            title.setText(listeningDto.getTitle());
            progress.setText("0%");
            action.setText("Start");
        }
    }
}
