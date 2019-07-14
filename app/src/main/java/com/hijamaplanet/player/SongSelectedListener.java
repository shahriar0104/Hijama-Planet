package com.hijamaplanet.player;

import android.support.annotation.NonNull;

import java.util.List;

public interface SongSelectedListener {
    void onSongSelected(@NonNull final Song song, @NonNull final List<Song> songs);
}
