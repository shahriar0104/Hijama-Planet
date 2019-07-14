package com.hijamaplanet.service.network;

import com.hijamaplanet.service.network.model.BranchAdmin;
import com.hijamaplanet.service.network.model.Branch;
import com.hijamaplanet.service.network.model.BranchLocation;
import com.hijamaplanet.service.network.model.Dua;
import com.hijamaplanet.service.network.model.LocationData;
import com.hijamaplanet.service.network.model.Offer;
import com.hijamaplanet.service.network.model.GetResponse;
import com.hijamaplanet.service.network.model.Review;
import com.hijamaplanet.service.network.model.Shop;
import com.hijamaplanet.service.network.model.TreatmentPrice;
import com.hijamaplanet.service.network.model.UserAppointmentHistory;
import com.hijamaplanet.service.network.model.User;

import java.util.List;

import okhttp3.MultipartBody;
import okhttp3.RequestBody;
import retrofit2.Call;
import retrofit2.http.Field;
import retrofit2.http.FormUrlEncoded;
import retrofit2.http.GET;
import retrofit2.http.Multipart;
import retrofit2.http.POST;
import retrofit2.http.Part;
import retrofit2.http.Query;

public interface NetworkHelper {
    @FormUrlEncoded
    @POST("registration.php")
    Call<GetResponse> registration(@Field("user_name") String name, @Field("mobile_number") String mobile,
                                    @Field("age") String age, @Field("sex") String gender, @Field("device") String device,
                                    @Field("location") String location, @Field("branchHead") String branchHead);

    @GET("login.php")
    Call<List<User>> getUserInfo(@Query("mobile_number") String mobile_number);

    @POST("fetchOffer.php")
    Call<List<Offer>> getOfferList();

    @GET("delete_offer.php")
    Call<GetResponse> deleteOffer(@Query("Announcement_id") String Announcement_id,
                                   @Query("mobile_number") String mobile, @Query("branchHead") String branchHead);

    @GET("findBranchForBranchAdmin.php")
    Call<List<BranchAdmin>> getBranchAdmin(@Query("branch_id") String branchAdmin_id);

    @FormUrlEncoded
    @POST("add_new_admin.php")
    Call<GetResponse> addAdmin(@Field("user_name") String name, @Field("docMob") String docMob, @Field("userMob") String userMob,
                               @Field("age") String age, @Field("sex") String gender, @Field("branch_name") String branch,
                               @Field("branchHead") String branchHead, @Field("key") String key);

    @FormUrlEncoded
    @POST("add_master_admin.php")
    Call<GetResponse> addMasterAdmin(@Field("user_name") String name, @Field("docMob") String docMob, @Field("userMob") String userMob,
                                     @Field("age") String age, @Field("sex") String gender, @Field("branchHead") String branchHead,
                                     @Field("key") String key);

    @FormUrlEncoded
    @POST("approved_appointment.php")
    Call<GetResponse> approvedAppointment(@Field("appointment_id") String appointment_id, @Field("date") String date,
                                          @Field("time") String time, @Field("mobile_number") String mobile,
                                          @Field("branchHead") String branchHead);

    @FormUrlEncoded
    @POST("addBranch.php")
    Call<GetResponse> addBranch(@Field("branch_name") String branch, @Field("branch_location") String location,
                                @Field("latitude") String latitude, @Field("longitude") String longitude,
                                @Field("address") String address, @Field("phone") String phone,
                                @Field("mobile") String userPhone, @Field("branchHead") String branchHead);

    @Multipart
    @POST("addOffer.php")
    Call<GetResponse> uploadOffer(@Part("offerTitle") RequestBody offerTitle, @Part("offerValidDate") RequestBody offerDate,
                                  @Part("offerDetails") RequestBody offerDescription, @Part MultipartBody.Part offerImage);

    @GET("fetch_user_apppointment_history.php")
    Call<List<UserAppointmentHistory>> userAppointmentHistory(@Query("mobile_number") String mobile);

    @GET("delete_review.php")
    Call<GetResponse> deleteReview(@Query("reviewid") String reviewid,
                                   @Query("mobile_number") String mobile, @Query("branchHead") String branchHead);

    @GET("location.php")
    Call<GetResponse> updateLocation(@Query("location") String location);

    @FormUrlEncoded
    @POST("make_appointment.php")
    Call<GetResponse> makeAppointment(@Field("name") String name, @Field("age") String age, @Field("mobile_number") String mobile,
                                      @Field("sex") String gender, @Field("date") String date, @Field("time") String time,
                                      @Field("treatment_method") String treatment_method, @Field("branch") String branch,
                                      @Field("branchHead") String branchHead, @Field("problem") String problem);

    @FormUrlEncoded
    @POST("make_review.php")
    Call<GetResponse> reviewPost(@Field("reviewer_name") String reviewer_name, @Field("review_star") String review_star,
                                 @Field("review_post") String review_post, @Field("mobile_number") String mobile,
                                 @Field("branchHead") String branchHead);

    @POST("fetchbranchList.php")
    Call<List<Branch>> fetchBranchList();

    @POST("fetchBranchLocation.php")
    Call<List<BranchLocation>> fetchBranchLocation();

    @POST("duasdownload.php")
    Call<List<Dua>> fetchDua();

    @POST("fetchreview.php")
    Call<List<Review>> fetchReview();

    @POST("fetchshop.php")
    Call<List<Shop>> fetchShop();

    @POST("fetchLocationOfUserActivity.php")
    Call<List<LocationData>> fetchLocationStatus();

    @POST("fetchPrice.php")
    Call<List<TreatmentPrice>> fetchTreatmentPrice();

    @GET("un_approved.php")
    Call<List<UserAppointmentHistory>> fetchUnApproved(@Query("user_id") String user_id);

    @POST("master_un_approved.php")
    Call<List<UserAppointmentHistory>> fetchMasterUnApproved();

    @GET("delete_appointment.php")
    Call<GetResponse> deleteAppointment(@Query("appointmentId") String appointmentId,
                                        @Query("mobile_number") String mobile, @Query("branchHead") String branchHead);

    @GET("master_show_approved_Appointment_throughdate.php")
    Call<List<UserAppointmentHistory>> fetchMasterApproved(@Query("date") String date);

    @GET("show_approved_Appointment_throughdate.php")
    Call<List<UserAppointmentHistory>> fetchApproved(@Query("user_id") String user_id, @Query("date") String date);
}
