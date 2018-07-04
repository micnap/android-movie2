package com.mickeywilliamson.mickey.popularmovies2;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.TextView;

import java.util.ArrayList;

import butterknife.BindView;
import butterknife.ButterKnife;


public class TrailerAdapter extends RecyclerView.Adapter<TrailerAdapter.TrailerAdapterViewHolder> {

    private ArrayList<Trailer> trailers;
    private final ListItemClickListener mOnClickListener;

    public TrailerAdapter(ListItemClickListener listener) {mOnClickListener = listener;}

    @NonNull
    @Override
    public TrailerAdapterViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        Context context = parent.getContext();
        LayoutInflater inflater = LayoutInflater.from(context);
        int layoutId = R.layout.trailer_list;
        View view = inflater.inflate(layoutId, parent, false);
        return new TrailerAdapter.TrailerAdapterViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull TrailerAdapterViewHolder holder, int position) {
        final Trailer currentTrailer = trailers.get(position);

        holder.mTitle.setText(currentTrailer.getTitle());
    }

    @Override
    public int getItemCount() {
        if (trailers == null) {
            return 0;
        } else  {
            return trailers.size();
        }
    }

    public void setTrailerData(ArrayList<Trailer> trailerList) {
        trailers = trailerList;
        notifyDataSetChanged();
    }

    public class TrailerAdapterViewHolder extends RecyclerView.ViewHolder  implements View.OnClickListener {

        @BindView(R.id.trailer_title) TextView mTitle;

        public TrailerAdapterViewHolder(View itemView) {
            super(itemView);

            // No idea why I can't use ButterKnife to bind the view.  When I try, it tells me that mPlayVideo is null.
            ImageButton mPlayVideo = (ImageButton) itemView.findViewById(R.id.play_video);
            mPlayVideo.setOnClickListener(this);
            ButterKnife.bind(this, itemView);
        }

        @Override
        public void onClick(View v) {
            int clickedPosition = getAdapterPosition();
            mOnClickListener.onListItemClick(trailers.get(clickedPosition));
        }
    }

    public interface ListItemClickListener {
        void onListItemClick(Trailer clickedTrailer);
    }
}
