package com.hijamaplanet.player;

import android.app.Activity;
import android.net.Uri;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import com.hijamaplanet.R;
import com.hijamaplanet.drawer.DownloadFile;
import com.hijamaplanet.drawer.DuaActivity;

import java.util.List;

public class SongsAdapter extends RecyclerView.Adapter<SongsAdapter.SimpleViewHolder> {

    public static SongSelectedListener mSongSelectedListener;
    public Activity mActivity;
    private List<Song> mSongs;
    private List<String> duaList;
    Uri uri;

    public SongsAdapter(@NonNull Activity activity , List<String> duaList) {
        mActivity = activity;
        this.duaList = duaList;
        mSongSelectedListener = (SongSelectedListener) activity;
    }

    @Override
    @NonNull
    public SimpleViewHolder onCreateViewHolder(@NonNull final ViewGroup parent, final int viewType) {

        final View itemView = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.song_item, parent, false);

        return new SimpleViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(@NonNull final SimpleViewHolder holder, final int position) {
        mSongs = DuaActivity.songs;
        if (DuaActivity.sizeOfdatabaseSongs > holder.getAdapterPosition()){
            holder.foreground.setBackgroundResource(R.drawable.ic_download);
            holder.title.setText(duaList.get(position));
            //holder.duration.setText("");
        }else {
            final Song song = mSongs.get(holder.getAdapterPosition()- DuaActivity.sizeOfdatabaseSongs);
            final String songTitle = song.title;

            holder.foreground.setBackgroundResource(R.drawable.ic_download_complete);
            holder.title.setText(songTitle);
            //holder.duration.setText(Song.formatDuration(song.duration));
        }
    }

    @Override
    public int getItemCount() {
        mSongs = DuaActivity.songs;
        return duaList.size() + mSongs.size();
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public int getItemViewType(int position) {
        return position;
    }

    public interface SongSelectedListener {
        void onSongSelected(@NonNull final Song song, @NonNull final List<Song> songs);
    }

    class SimpleViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {

        final ImageView foreground;
        final TextView title, duration;

        SimpleViewHolder(@NonNull final View itemView) {
            super(itemView);

            foreground = itemView.findViewById(R.id.foreground);
            title = itemView.findViewById(R.id.title);
            duration = itemView.findViewById(R.id.duration);
            itemView.setOnClickListener(this);

        }

        @Override
        public void onClick(@NonNull final View v) {
            if (getAdapterPosition() < DuaActivity.sizeOfdatabaseSongs){
                /*Dua duaListObject = new Dua();
                duaListObject.doUnbindService();*/
                uri = Uri.parse(DuaActivity.duaPathShow.get(getAdapterPosition()));
                new DownloadFile(mActivity.getApplicationContext(), uri.toString(),duaList.get(getAdapterPosition()) ,getAdapterPosition());
                /*Intent i = new Intent(mActivity.getApplicationContext(), DownloadFile.class);
                i.putExtra("Path", uri.toString());
                i.putExtra("Filename", duaList.get(getAdapterPosition()));
                i.putExtra("position", getAdapterPosition());
                mActivity.getApplicationContext().startService(i);*/
            }else {
                mSongs = DuaActivity.songs;
                final Song song = mSongs.get(getAdapterPosition()- DuaActivity.sizeOfdatabaseSongs);
                mSongSelectedListener.onSongSelected(song, mSongs);
            }
        }
    }
}