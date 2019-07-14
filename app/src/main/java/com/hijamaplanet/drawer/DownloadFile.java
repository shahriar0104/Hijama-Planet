package com.hijamaplanet.drawer;

import android.app.DownloadManager;
import android.app.NotificationManager;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.SharedPreferences;
import android.database.Cursor;
import android.net.Uri;
import android.os.Environment;
import android.support.v4.app.NotificationCompat;
import android.webkit.MimeTypeMap;
import android.widget.Toast;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.hijamaplanet.CommonClass;
import com.hijamaplanet.R;
import com.hijamaplanet.player.Song;
import com.hijamaplanet.player.SongProvider;

import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.Random;

public class DownloadFile {

    private Context context;
    private String file_url, file_name;
    private int position;
    Boolean isSDPresent, isSDSupportedDevice;

    private DownloadManager downloadManager;
    private long refid;
    private Uri Download_Uri;
    private ArrayList<String> list;
    private ArrayList<String> referenceList = new ArrayList<>();
    Map<Long, String> referenceDuaList = new LinkedHashMap<>();

    int notificationId = 0;
    String CHANNEL_ID = "my_channel_02";
    String CHANNEL_NAME = "Download";
    int IMPORTANCE = NotificationManager.IMPORTANCE_DEFAULT;
    NotificationManager notificationManager;
    NotificationCompat.Builder mBuilder;
    private Intent myIntent;

    private ArrayList<String> urlArrayList;
    private String urlKey = "DOWNLOAD_ID";
    private String referenceKey = "REFERENCE_ID";

    List<Song> songs;

    public DownloadFile() {
    }

    public DownloadFile(final Context context, String url, String dua_name, int position) {

        this.context = context;
        file_url = url;
        file_name = dua_name;
        this.position = position;

        urlArrayList = new ArrayList();
        urlArrayList = getArrayList(urlKey);
        try {
            if (urlArrayList.size() == 0) {
                urlArrayList.add(file_name);
                saveArrayList(urlArrayList, urlKey);
                Toast.makeText(context, "Downloading in Background", Toast.LENGTH_SHORT).show();
                DownloadWorker();
            } else if (urlArrayList.size() != 0) {
                for (int i = 0; i < urlArrayList.size(); i++) {
                    if (urlArrayList.get(i).equals(file_name)) {
                        //Toast.makeText(context, "Already Downloading", Toast.LENGTH_SHORT).show();
                        break;
                    } else if (!urlArrayList.get(i).equals(file_name) && i == (urlArrayList.size() - 1)) {
                        removeArrayList(urlKey);
                        urlArrayList.add(file_name);
                        saveArrayList(urlArrayList, urlKey);
                        Toast.makeText(context, "Downloading in Background", Toast.LENGTH_SHORT).show();
                        DownloadWorker();
                    }
                }
            }
        } catch (Exception e) {
            urlArrayList = new ArrayList<>();
            urlArrayList.add(file_url);
            saveArrayList(urlArrayList, urlKey);
            Toast.makeText(context, "Downloading in Background", Toast.LENGTH_SHORT).show();
            DownloadWorker();
        }
    }

    public void DownloadWorker() {
        isSDPresent = Environment.getExternalStorageState().equals(Environment.MEDIA_MOUNTED);
        isSDSupportedDevice = Environment.isExternalStorageRemovable();

        downloadManager = (DownloadManager) context.getSystemService(Context.DOWNLOAD_SERVICE);

        Download_Uri = Uri.parse(file_url);
        String ext = MimeTypeMap.getFileExtensionFromUrl(file_url);
        if (ext.equalsIgnoreCase(""))
            ext = "mp3";

        DownloadManager.Request request = new DownloadManager.Request(Download_Uri);
        request.setAllowedNetworkTypes(DownloadManager.Request.NETWORK_WIFI | DownloadManager.Request.NETWORK_MOBILE);
        request.setAllowedOverRoaming(false);
        request.setTitle(DuaActivity.duaListShow.get(position));
        request.setDescription("Downloading in Progress..");
        request.setVisibleInDownloadsUi(true);
        request.setNotificationVisibility(DownloadManager.Request.VISIBILITY_VISIBLE);
        request.setDestinationInExternalPublicDir("/HijamaDua", "/" + DuaActivity.duaListShow.get(position) + "." + ext);

        refid = downloadManager.enqueue(request);

        list = new ArrayList<>();
        list = getReferenceList(referenceKey);
        boolean isRunning = false;

        if (list != null) {
            for (int i = 0; i < list.size(); i++) {
                long id = Long.valueOf(list.get(i));
                DownloadManager.Query query = new DownloadManager.Query();
                query.setFilterById(id);
                Cursor cursor = downloadManager.query(query);
                if (cursor.moveToFirst()) {
                    int columnIndex = cursor
                            .getColumnIndex(DownloadManager.COLUMN_STATUS);
                    int status = cursor.getInt(columnIndex);

                    switch (status) {
                        case DownloadManager.STATUS_RUNNING:
                            isRunning = true;
                            break;
                    }
                }
            }
        }
        if (isRunning) {
            list.add(String.valueOf(refid));
            saveReferenceList(list, referenceKey);

        } else {
            removeReferenceList(referenceKey);
            list = new ArrayList<>();
            list.add(String.valueOf(refid));
            saveReferenceList(list, referenceKey);
            context.registerReceiver(onComplete, new IntentFilter(DownloadManager.ACTION_DOWNLOAD_COMPLETE));
        }
    }

    public BroadcastReceiver onComplete = new BroadcastReceiver() {

        @Override
        public void onReceive(Context ctxt, Intent intent) {

            /*String activityName = context.getClass().getSimpleName();
            if (activityName.startsWith("Main"))
                myIntent = new Intent(context, MainActivity.class).putExtra("duaList", "downloadedDua");
            else if (activityName.startsWith("User"))
                myIntent = new Intent(context, UserView.class).putExtra("duaList", "downloadedDua");
            else if (activityName.startsWith("Admin"))
                myIntent = new Intent(context, AdminView.class).putExtra("duaList", "downloadedDua");
            else if (activityName.startsWith("Master"))
                myIntent = new Intent(context, MasterAdminView.class).putExtra("duaList", "downloadedDua");*/

            notificationManager = (NotificationManager) context.getSystemService(context.NOTIFICATION_SERVICE);
            if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.O) {
                android.app.NotificationChannel mChannel = new android.app.NotificationChannel(
                        CHANNEL_ID, CHANNEL_NAME, IMPORTANCE);
                notificationManager.createNotificationChannel(mChannel);
            }

            String action = intent.getAction();
            if (DownloadManager.ACTION_DOWNLOAD_COMPLETE.equals(action)) {
                long downloadId = intent.getLongExtra(
                        DownloadManager.EXTRA_DOWNLOAD_ID, 0);

                referenceList = new ArrayList<>();
                referenceList = getReferenceList(referenceKey);
                Object id = String.valueOf(downloadId);
                referenceList.remove(id);
                removeReferenceList(referenceKey);
                saveReferenceList(referenceList, referenceKey);

                if (referenceList.isEmpty()) {
                    context.unregisterReceiver(onComplete);
                }
                CheckDwnloadStatus(downloadId);
            }

        }
    };

    private void CheckDwnloadStatus(long id) {

        DownloadManager.Query query = new DownloadManager.Query();

        query.setFilterById(id);
        Cursor cursor = downloadManager.query(query);
        if (cursor.moveToFirst()) {
            int columnIndex = cursor
                    .getColumnIndex(DownloadManager.COLUMN_STATUS);
            int status = cursor.getInt(columnIndex);
            int columnReason = cursor
                    .getColumnIndex(DownloadManager.COLUMN_REASON);
            int reason = cursor.getInt(columnReason);

            switch (status) {
                case DownloadManager.STATUS_FAILED:
                    String failedReason = "";
                    switch (reason) {
                        case DownloadManager.ERROR_CANNOT_RESUME:
                            failedReason = "ERROR_CANNOT_RESUME";
                            break;
                        case DownloadManager.ERROR_DEVICE_NOT_FOUND:
                            failedReason = "ERROR_DEVICE_NOT_FOUND";
                            break;
                        case DownloadManager.ERROR_FILE_ALREADY_EXISTS:
                            failedReason = "ERROR_FILE_ALREADY_EXISTS";
                            break;
                        case DownloadManager.ERROR_FILE_ERROR:
                            failedReason = "ERROR_FILE_ERROR";
                            break;
                        case DownloadManager.ERROR_HTTP_DATA_ERROR:
                            failedReason = "ERROR_HTTP_DATA_ERROR";
                            break;
                        case DownloadManager.ERROR_INSUFFICIENT_SPACE:
                            failedReason = "ERROR_INSUFFICIENT_SPACE";
                            break;
                        case DownloadManager.ERROR_TOO_MANY_REDIRECTS:
                            failedReason = "ERROR_TOO_MANY_REDIRECTS";
                            break;
                        case DownloadManager.ERROR_UNHANDLED_HTTP_CODE:
                            failedReason = "ERROR_UNHANDLED_HTTP_CODE";
                            break;
                        case DownloadManager.ERROR_UNKNOWN:
                            failedReason = "ERROR_UNKNOWN";
                            break;
                    }

                    Toast.makeText(context, "FAILED: " + failedReason,
                            Toast.LENGTH_LONG).show();
                    break;
                case DownloadManager.STATUS_PAUSED:
                    String pausedReason = "";

                    switch (reason) {
                        case DownloadManager.PAUSED_QUEUED_FOR_WIFI:
                            pausedReason = "PAUSED_QUEUED_FOR_WIFI";
                            break;
                        case DownloadManager.PAUSED_UNKNOWN:
                            pausedReason = "PAUSED_UNKNOWN";
                            break;
                        case DownloadManager.PAUSED_WAITING_FOR_NETWORK:
                            pausedReason = "PAUSED_WAITING_FOR_NETWORK";
                            break;
                        case DownloadManager.PAUSED_WAITING_TO_RETRY:
                            pausedReason = "PAUSED_WAITING_TO_RETRY";
                            break;
                    }

                    Toast.makeText(context, "PAUSED: " + pausedReason,
                            Toast.LENGTH_LONG).show();
                    break;
                case DownloadManager.STATUS_PENDING:
                    Toast.makeText(context, "PENDING", Toast.LENGTH_LONG).show();
                    break;
                case DownloadManager.STATUS_RUNNING:
                    Toast.makeText(context, "RUNNING", Toast.LENGTH_LONG).show();
                    break;
                case DownloadManager.STATUS_SUCCESSFUL:

                    String titleString = cursor.getString(cursor.getColumnIndex(DownloadManager.COLUMN_TITLE));
                    int dummyuniqueInt = new Random().nextInt((599 - 299) + 1) + 299;

                    /*@SuppressLint("WrongConstant") PendingIntent pendingIntent = PendingIntent.getActivity(
                            context,
                            dummyuniqueInt,
                            myIntent,
                            Intent.FLAG_ACTIVITY_NEW_TASK);*/

                    mBuilder = new NotificationCompat.Builder(context, CHANNEL_ID);
                    mBuilder.setContentTitle(titleString)
                            .setContentText("Download Complete")
                            //.setContentIntent(pendingIntent)
                            .setSmallIcon(R.mipmap.ic_launcher_round)
                            .setPriority(NotificationCompat.PRIORITY_DEFAULT);
                    notificationManager.notify(dummyuniqueInt, mBuilder.build());

                    songs = SongProvider.getSongs();
                    ArrayList<String> runningDownloadDua = new ArrayList<>();
                    runningDownloadDua = getReferenceList(referenceKey);
                    for (int i = 0; i < songs.size(); i++) {
                        for (int j = 0; j < runningDownloadDua.size(); j++) {
                            if (songs.get(i).getTitle().equalsIgnoreCase(runningDownloadDua.get(j))) {
                                songs.remove(i);
                                break;
                            }
                        }
                    }
                    DuaActivity.songs = songs;
                    setUpList();
                    DuaActivity.mSongsAdapter.notifyDataSetChanged();

                    break;
            }
        }
    }


    public void setUpList() {
        while (DuaActivity.duaListShow.size() != 0) {
            DuaActivity.duaListShow.remove(0);
            DuaActivity.duaPathShow.remove(0);
        }
        for (int c = 0; c < CommonClass.duasLists.size(); c++) {
            for (int i = 0; i < songs.size(); i++) {
                final Song song = songs.get(i);
                final String songTitle = song.title;
                if (songTitle.equalsIgnoreCase(CommonClass.duasLists.get(c).get(0))) {
                    break;
                } else if (i == songs.size() - 1 && !songTitle.equalsIgnoreCase(CommonClass.duasLists.get(c).get(0))) {
                    DuaActivity.duaListShow.add(CommonClass.duasLists.get(c).get(0));
                    DuaActivity.duaPathShow.add(CommonClass.duasLists.get(c).get(1));
                }
            }
        }
        DuaActivity.sizeOfdatabaseSongs = DuaActivity.duaListShow.size();
    }

    private void saveArrayList(ArrayList<String> list, String key) {
        SharedPreferences prefs = context.getSharedPreferences("duaDownload", Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = prefs.edit();
        Gson gson = new Gson();
        String json = gson.toJson(list);
        editor.putString(key, json);
        editor.apply();     // This line is IMPORTANT !!!
    }

    private ArrayList<String> getArrayList(String key) {
        SharedPreferences prefs = context.getSharedPreferences("duaDownload", Context.MODE_PRIVATE);
        Gson gson = new Gson();
        String json = prefs.getString(key, null);
        Type type = new TypeToken<ArrayList<String>>() {
        }.getType();
        return gson.fromJson(json, type);
    }

    private void removeArrayList(String key) {
        SharedPreferences prefs = context.getSharedPreferences("duaDownload", Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = prefs.edit();
        editor.remove(key).commit();
    }

    private void saveReferenceList(ArrayList<String> list, String key) {
        SharedPreferences prefs = context.getSharedPreferences("referenceList", Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = prefs.edit();
        Gson gson = new Gson();
        String json = gson.toJson(list);
        editor.putString(key, json);
        editor.apply();     // This line is IMPORTANT !!!
    }

    private ArrayList<String> getReferenceList(String key) {
        SharedPreferences prefs = context.getSharedPreferences("referenceList", Context.MODE_PRIVATE);
        Gson gson = new Gson();
        String json = prefs.getString(key, null);
        Type type = new TypeToken<ArrayList<String>>() {
        }.getType();
        return gson.fromJson(json, type);
    }

    private void removeReferenceList(String key) {
        SharedPreferences prefs = context.getSharedPreferences("referenceList", Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = prefs.edit();
        editor.remove(key).commit();
    }
}
