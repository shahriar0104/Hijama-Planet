package com.hijamaplanet.service.parser;

import android.app.AlertDialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Environment;
import android.os.Parcelable;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.hijamaplanet.R;
import com.hijamaplanet.drawer.DuaActivity;
import com.hijamaplanet.service.network.NetworkClient;
import com.hijamaplanet.service.network.NetworkHelper;
import com.hijamaplanet.service.network.model.Dua;

import java.io.File;
import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.List;

import dmax.dialog.SpotsDialog;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

import static com.hijamaplanet.utils.DialogUtils.showFetchingDialog;

public class FetchDua {
    private Context context;

    public FetchDua(Context context) {
        this.context = context;
    }

    public void execute() {
        SpotsDialog spotsDialog = showFetchingDialog(context);
        spotsDialog.show();
        AlertDialog alertDialog = new AlertDialog.Builder(context).create();
        alertDialog.setTitle(context.getResources().getString(R.string.networkTitle));

        NetworkHelper networkHelper = NetworkClient.newNetworkClient().create(NetworkHelper.class);
        Call<List<Dua>> call = networkHelper.fetchDua();
        call.enqueue(new Callback<List<Dua>>() {
            @Override
            public void onResponse(Call<List<Dua>> call, Response<List<Dua>> response) {
                    FileRead();
                    spotsDialog.dismiss();
                    context.startActivity(new Intent(context, DuaActivity.class).putExtra("Dua", (ArrayList<? extends Parcelable>) response.body()));
            }

            @Override
            public void onFailure(Call<List<Dua>> call, Throwable t) {
                spotsDialog.dismiss();
                alertDialog.setMessage(context.getResources().getString(R.string.networkResult));
                alertDialog.show();
            }
        });
    }

    private void FileRead() {
        ArrayList<String> urlArrayList;
        urlArrayList = new ArrayList();
        boolean isSDPresent, isSDSupportedDevice;
        String urlKey = "DOWNLOAD_ID";

        isSDPresent = Environment.getExternalStorageState().equals(Environment.MEDIA_MOUNTED);
        isSDSupportedDevice = Environment.isExternalStorageRemovable();
        if (isSDSupportedDevice && isSDPresent) {

            File root = Environment.getExternalStorageDirectory();
            File dir = new File(root + "/HijamaDua/");
            if (dir.exists()) {
                File[] dirFiles = dir.listFiles();
                dir.setReadable(true, false);
                if (dirFiles.length != 0) {
                    for (int ii = 0; ii < dirFiles.length; ii++) {
                        urlArrayList.add(dirFiles[ii].getName().toString().substring(0, dirFiles[ii].getName().toString().indexOf(".")));
                        //Toast.makeText(context, ""+urlArrayList.get(ii), Toast.LENGTH_SHORT).show();
                    }
                }
                removeArrayList(urlKey);
                saveArrayList(urlArrayList, urlKey);
            }
        } else {
            File mydir = new File(Environment.getExternalStorageDirectory(), "HijamaDua");
            if (mydir.exists()) {
                File[] dirFiles = mydir.listFiles();
                if (dirFiles.length != 0) {
                    for (int ii = 0; ii < dirFiles.length; ii++) {
                        urlArrayList.add(dirFiles[ii].getName().toString().substring(0, dirFiles[ii].getName().indexOf(".")));
                        //Toast.makeText(context, ""+urlArrayList.get(ii), Toast.LENGTH_SHORT).show();
                    }
                }
                removeArrayList(urlKey);
                saveArrayList(urlArrayList, urlKey);
            }
        }
    }

    private void saveArrayList(ArrayList<String> list, String key) {
        SharedPreferences prefs = context.getSharedPreferences("duaDownload", Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = prefs.edit();
        Gson gson = new Gson();
        String json = gson.toJson(list);
        editor.putString(key, json);
        editor.apply();
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
}
