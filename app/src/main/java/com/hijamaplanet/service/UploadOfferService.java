package com.hijamaplanet.service;

import android.app.AlertDialog;
import android.content.Context;
import com.hijamaplanet.R;
import com.hijamaplanet.service.network.NetworkClient;
import com.hijamaplanet.service.network.NetworkHelper;
import com.hijamaplanet.service.network.model.GetResponse;
import java.io.File;
import dmax.dialog.SpotsDialog;
import okhttp3.MediaType;
import okhttp3.MultipartBody;
import okhttp3.RequestBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class UploadOfferService {
    private Context context;

    public UploadOfferService(Context context) {
        this.context = context;
    }

    public void execute(String title, String date, String desc, String filePath) {
        SpotsDialog spotsDialog = new SpotsDialog(context);
        spotsDialog.setTitle("Uploading...");
        spotsDialog.show();
        spotsDialog.setMessage("Uploading...");

        AlertDialog alertDialog = new AlertDialog.Builder(context).create();
        alertDialog.setTitle(context.getResources().getString(R.string.networkTitle));

        File file = new File(filePath);
        // Create a request body with file and image media type
        RequestBody fileReqBody = RequestBody.create(MediaType.parse("image/*"), file);
        // Create MultipartBody.Part using file request-body,file name and part name
        MultipartBody.Part part = MultipartBody.Part.createFormData("image", file.getName(), fileReqBody);
        //Create request body with text description and text media type
        RequestBody offerTitle = RequestBody.create(MediaType.parse("text/plain"), title);
        RequestBody offerValodDate = RequestBody.create(MediaType.parse("text/plain"), date);
        RequestBody offerDescription = RequestBody.create(MediaType.parse("text/plain"), desc);

        NetworkHelper networkHelper = NetworkClient.newNetworkClient().create(NetworkHelper.class);
        Call<GetResponse> call = networkHelper.uploadOffer(offerTitle,offerValodDate,offerDescription,part);
        call.enqueue(new Callback<GetResponse>() {
            @Override
            public void onResponse(Call<GetResponse> call, Response<GetResponse> response) {
                spotsDialog.dismiss();
                if (response.body().getResponse().equalsIgnoreCase("offer_uploaded"))
                    alertDialog.setMessage("Offer Uploaded");
                else alertDialog.setMessage("Offer Upload Failed");
                alertDialog.show();
            }

            @Override
            public void onFailure(Call<GetResponse> call, Throwable t) {
                spotsDialog.dismiss();
                alertDialog.setMessage(context.getResources().getString(R.string.networkResult));
                alertDialog.show();
            }
        });
    }
}
