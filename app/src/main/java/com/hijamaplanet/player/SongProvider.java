package com.hijamaplanet.player;

import android.content.Context;
import android.database.Cursor;
import android.os.Environment;
import android.provider.MediaStore;
import android.provider.MediaStore.Audio.AudioColumns;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;

import java.io.File;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class SongProvider {

    private static final int TITLE = 0;
    private static final int TRACK = 1;
    private static final int YEAR = 2;
    private static final int DURATION = 3;
    private static final int PATH = 4;
    private static final int ALBUM = 5;
    private static final int ARTIST_ID = 6;
    private static final int ARTIST = 7;
    static boolean isSDPresent,isSDSupportedDevice;
    static Song song;
    static String title,duration;

    private static final String[] BASE_PROJECTION = new String[]{
            AudioColumns.TITLE,// 0
            AudioColumns.TRACK,// 1
            AudioColumns.YEAR,// 2
            AudioColumns.DURATION,// 3
            AudioColumns.DATA,// 4
            AudioColumns.ALBUM,// 5
            AudioColumns.ARTIST_ID,// 6
            AudioColumns.ARTIST,// 7
    };

    private static final List<Song> mAllDeviceSongs = new ArrayList<>();

    public static List<Song> getAllDeviceSongs() {
        return mAllDeviceSongs;
    }


    @NonNull
    public static List<Song> getSongs() {

        isSDPresent = Environment.getExternalStorageState().equals(Environment.MEDIA_MOUNTED);
        isSDSupportedDevice = Environment.isExternalStorageRemovable();

        List<Song> listOfSongs = new ArrayList<>();

        if (isSDSupportedDevice && isSDPresent) {
            File root = Environment.getExternalStorageDirectory();
            File dir = new File(root + "/HijamaDua/");
            if (dir.exists()) {
                File[] dirFiles = dir.listFiles();
                if (dirFiles.length != 0) {
                    for (int ii = 0; ii < dirFiles.length; ii++) {
                        Song songData = new Song();
                        String title = dirFiles[ii].getName().toString();
                        if (title.indexOf(".") > 0)
                            title = title.substring(0, title.indexOf("."));
                        String path = dirFiles[ii].getPath().toString();
                        songData.setTitle(title);
                        songData.setPath(path);
                        listOfSongs.add(songData);
                    }
                }
            }
        } else {
            File mydir = new File(Environment.getExternalStorageDirectory(), "HijamaDua");
            if (mydir.exists()) {
                File[] dirFiles = mydir.listFiles();
                if (dirFiles.length != 0) {
                    for (int ii = 0; ii < dirFiles.length; ii++) {
                        Song songData = new Song();
                        String title = dirFiles[ii].getName().toString();
                        if (title.indexOf(".") > 0)
                            title = title.substring(0, title.indexOf("."));
                        String path = dirFiles[ii].getPath().toString();
                        songData.setTitle(title);
                        songData.setPath(path);
                        listOfSongs.add(songData);
                    }
                }
            }
        }

        return listOfSongs;

        /*final List<Song> songs = new ArrayList<>();
        if (cursor != null && cursor.moveToFirst()) {
            do {
                final Song song = getSongFromCursorImpl(cursor);
                if (song.duration >= 5000) {
                    songs.add(song);
                    mAllDeviceSongs.add(song);
                }
            } while (cursor.moveToNext());
        }

        if (cursor != null) {
            cursor.close();
        }
        if (songs.size() > 1) {
            sortSongsByTrack(songs);
        }
        return songs;*/
    }

    private static void sortSongsByTrack(@NonNull final List<Song> songs) {

        Collections.sort(songs, (obj1, obj2) -> Long.compare(obj1.trackNumber, obj2.trackNumber));
    }

    @NonNull
    private static Song getSongFromCursorImpl(@NonNull final Cursor cursor) {
        final String title = cursor.getString(TITLE);
        final int trackNumber = cursor.getInt(TRACK);
        final int year = cursor.getInt(YEAR);
        final int duration = cursor.getInt(DURATION);
        final String uri = cursor.getString(PATH);
        final String albumName = cursor.getString(ALBUM);
        final int artistId = cursor.getInt(ARTIST_ID);
        final String artistName = cursor.getString(ARTIST);

        return new Song(title, trackNumber, year, duration, uri, albumName, artistId, artistName);
    }

    @Nullable
    public static Cursor makeSongCursor(@NonNull final Context context, @NonNull final String sortOrder) {
        try {
            return context.getContentResolver().query(MediaStore.Audio.Media.EXTERNAL_CONTENT_URI,
                    BASE_PROJECTION, MediaStore.Audio.Media.DATA + " like ? ", new String[]{"%HijamaDua%"}, sortOrder);
        } catch (SecurityException e) {
            return null;
        }
    }
}
