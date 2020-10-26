package com.lisungui.pharma.rest;

import com.lisungui.pharma.constant.StringConstant;
import com.lisungui.pharma.models.Address;
import com.lisungui.pharma.models.AdvertisePojo;
import com.lisungui.pharma.models.ChatNotificationSendResponce;
import com.lisungui.pharma.models.CityPojo;
import com.lisungui.pharma.models.CountriesPojo;
import com.lisungui.pharma.models.CountryMedicinesPojo;
import com.lisungui.pharma.models.ForgotPojo;
import com.lisungui.pharma.models.HealthTipData;
import com.lisungui.pharma.models.ListDownloadOrderDetails;
import com.lisungui.pharma.models.ListLocations;
import com.lisungui.pharma.models.ListMedicine;
import com.lisungui.pharma.models.ListOrderDetails;
import com.lisungui.pharma.models.LoginPojo;
import com.lisungui.pharma.models.LoginWithGoogleResposeModel;
import com.lisungui.pharma.models.MedNotificationData;
import com.lisungui.pharma.models.OrderDetailsResponce;
import com.lisungui.pharma.models.OrderUpdateReponseModel;
import com.lisungui.pharma.models.PaymentData;
import com.lisungui.pharma.models.PharmaOrderDetails;
import com.lisungui.pharma.models.PharmacyNotificationsResponse;
import com.lisungui.pharma.models.PharmacyOrdersResponseModel;
import com.lisungui.pharma.models.PlaceOrderNotificationData;
import com.lisungui.pharma.models.PlaceOrderPojo;
import com.lisungui.pharma.models.SignUpPojo;
import com.lisungui.pharma.models.TokenRegPojo;

import java.util.concurrent.TimeUnit;

import okhttp3.MultipartBody;
import okhttp3.OkHttpClient;
import okhttp3.RequestBody;
import okhttp3.logging.HttpLoggingInterceptor;
import retrofit2.Call;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;
import retrofit2.http.Field;
import retrofit2.http.FormUrlEncoded;
import retrofit2.http.Multipart;
import retrofit2.http.POST;
import retrofit2.http.Part;

/**
 * Created by Hrishikesh on 5/2/2016.
 */

public class RestClient {

    private static RestApiInterface restApiInterface;

    public static RestApiInterface getClient() {

        if (restApiInterface == null) {

            HttpLoggingInterceptor logging = new HttpLoggingInterceptor();
// set your desired log level
            logging.setLevel(HttpLoggingInterceptor.Level.BODY);
            OkHttpClient.Builder httpClient = new OkHttpClient.Builder();
// add your other interceptors …
// add logging as last interceptor
            httpClient.addInterceptor(logging);  // <-- this is the important line!
            httpClient.connectTimeout(120, TimeUnit.SECONDS);

            Retrofit client = new Retrofit.Builder()
                    .baseUrl(StringConstant.BASE_URL)

                    .addConverterFactory(GsonConverterFactory.create())
                    .client(httpClient.build())
                    .build();
            restApiInterface = client.create(RestApiInterface.class);
        }

        return restApiInterface;
    }

    public interface RestApiInterface {

        @FormUrlEncoded
        @POST("Api_v2/fields/pharmacy")
        Call<ListLocations> getNearLocations(@Field("pharm_lat") String pharm_lat,
                                             @Field("pharm_lng") String pharm_lng);

        @FormUrlEncoded
        @POST("Api_v2/mSendChatNotification")
        Call<ChatNotificationSendResponce> sendChatNotification(@Field("user_gcm_key") String user_gcm_key,
                                                                @Field("username") String username,
                                                                @Field("userid") String userid,
                                                                @Field("message") String message);


        @POST("Api_v2/fields/advertise")
        Call<AdvertisePojo> getAdvertise();


        @FormUrlEncoded
        @POST("Api_v2/fields/medicine")
        Call<ListMedicine> getMedicines(@Field("count") int job_count, @Field("med_name") String med_name);

        @FormUrlEncoded
        @POST("Api_v2/mLogin")
        Call<LoginPojo> getLoginDetails(@Field("user_type") String user_type, @Field("user_name") String user_name, @Field("user_password") String user_password);

        @FormUrlEncoded
        @POST("Api_v2/forget")
        Call<ForgotPojo> sendPassword(@Field("user_email_id") String user_email_id);


        @FormUrlEncoded
        @POST("Api_v2/fields/order")
        Call<ListOrderDetails> getOrderDetails(@Field("user_id") String user_id);

        @FormUrlEncoded
        @POST("Api_v2/fields/order")
        Call<ListDownloadOrderDetails> getStatusData(@Field("user_id") String user_id, @Field("order_id") String order_id);

        @FormUrlEncoded
        @POST("Api_v2/fields/order")
        Call<ListDownloadOrderDetails> getOrderData(@Field("user_id") String user_id);

        @FormUrlEncoded
        @POST("Api_v2/mAdd/order")
        Call<PlaceOrderPojo> placeOrderData(@Field("order_user_id") String user_id, @Field("order_details") String transfer_data,
                                            @Field("order_total_price") double total_price, @Field("order_qnty") int qnty,
                                            @Field("order_pharm_id") String order_pharm_id, @Field("order_addr") String order_addr,
                                            @Field("order_lat") String order_lat, @Field("order_long") String order_long);


        @FormUrlEncoded
        @POST("Api_v2/mAdd/updateprofile")
        Call<TokenRegPojo> updateToken(@Field("user_id") String user_id,
                                       @Field("user_gcm_key") String user_gcm_key);

        @FormUrlEncoded
        @POST("Api_v2/mAdd/pharmacy")
        Call<TokenRegPojo> updatePharmacyToken(@Field("pharm_id") String pharm_id,
                                               @Field("user_gcm_key") String user_gcm_key);


        @FormUrlEncoded
        @POST("Api_v2/mAdd/signup")
        Call<SignUpPojo> getSignupDetails(@Field("user_name") String user_name,
                                          @Field("user_email_id") String user_email_id,
                                          @Field("user_type") String user_type,
                                          @Field("user_country_code") String user_country_code,
                                          @Field("user_mb_no") String user_mb_no,
                                          @Field("user_password") String user_password);

        @FormUrlEncoded
        @POST("Api_v2/mAdd/updateprofile")
        Call<SignUpPojo> updateProfile(@Field("user_id") String user_id,
                                       @Field("user_email_id") String user_email_id,
                                       @Field("user_mb_no") String user_mb_no,
                                       @Field("user_gender") String user_gender,
                                       @Field("user_address") String user_address,
                                       @Field("user_ins_comp_name") String user_ins_comp_name);

        @FormUrlEncoded
        @POST("Api_v2/mAdd/updateprofile")
        Call<Address> updateAddress(@Field("user_id") String user_id,
                                    @Field("user_address") String user_address);

        @FormUrlEncoded
        @POST("Api_v2/mAdd/updateprofile")
        Call<Address> updateWithGoogleSingin(@Field("user_id") String user_id,
                                             @Field("user_type") String user_type,
                                             @Field("user_name") String user_name,
                                             @Field("user_country_code") String country_code,
                                             @Field("user_mb_no") String user_mb_no);

        @FormUrlEncoded
        @POST("Api_v2/fields/find_medicine")
        Call<ListMedicine> getMedicinesbasedOnLocation(@Field("med_name") String name, @Field("user_lat") String userLat, @Field("user_lng") String userLong);

        @FormUrlEncoded
        @POST("Api_v2/fields/healthtip")
//http://www.lisunguipharma.com/index.php/Api/fields/healthtip
        Call<HealthTipData> getHealthTipsNotifications(@Field("notifi_user_id") String notifi_user_id);

        @FormUrlEncoded
        @POST("Api_v2/fields/order_notification")
//http://192.168.0.17/Lisungui/index.php/Api/fields/order_notification
        Call<PlaceOrderNotificationData> getPlaceOrderNotificationsList(@Field("user_id") String notifi_user_id);

        @POST("Api_v2/fields/notification")
        @FormUrlEncoded
        Call<PharmacyNotificationsResponse> getPharmaNotifications(@Field("pharm_id") String user_id);


        @FormUrlEncoded
        @POST("Api_v2/fields/notification")
        Call<MedNotificationData> getMedPromotionNotifications(@Field("notifi_user_id") String notifi_user_id);


        @FormUrlEncoded
        @POST("Api_v2/fields/order")
        Call<PharmacyOrdersResponseModel> getPharmacyOrdersList(@Field("pharm_id") String pharm_id);


        @FormUrlEncoded
        @POST("Api_v2/fields/order")
        Call<PharmaOrderDetails> getPharmOrderDetail(
                @Field("pharm_id") String pharm_id,
                @Field("order_id") String order_id);

        @FormUrlEncoded
        @POST("Api_v2/mSocialLogin")
        Call<LoginWithGoogleResposeModel> loginWithGoogle(
                @Field("user_google_id") String user_google_id,
                @Field("user_email_id") String user_email_id);

        @FormUrlEncoded
        @POST("Api_v2/mAdd/order_update")
        Call<OrderUpdateReponseModel> updatePharmOrderStatus(
                @Field("order_id") String order_id,
                @Field("pharm_id") String pharm_id,
                @Field("order_track_status") String order_status);

        @FormUrlEncoded
        @POST("Api_v2/fields/order_detail")
        Call<OrderDetailsResponce> getOrderDetail(
                @Field("order_id") int order_id,
                @Field("user_id") String user_id);

        @FormUrlEncoded
        @POST("Api_v2/mAdd/pharmacy_lang_update")
        Call<String> setLanguagePharmacy(
                @Field("pharm_id") String id,
                @Field("user_lang") String lang);

        @FormUrlEncoded
        @POST("Api_v2/mAdd/user_lang_update")
        Call<String> setLanguageUser(
                @Field("user_id") String id,
                @Field("user_lang") String lang);

        @FormUrlEncoded
        @POST("Api_v2/mAdd/order_accept")
        Call<SignUpPojo> getOrderAccept(
                @Field("order_id") String order_id,
                @Field("order_pharm_id") String order_pharm_id,
                @Field("order_pharm_accept") String order_pharm_accept);


        @FormUrlEncoded
        @POST("Api_v2/mAdd/order_update")
        Call<OrderUpdateReponseModel> AcceptPharmOrder(
                @Field("order_id") String order_id,
                @Field("pharm_id") String pharm_id,
                @Field("order_type") String order_type,
                @Field("order_desc") String order_desc,
                @Field("order_track_status") String order_status);


        @Multipart
        @POST("Api_v2/mAdd/order")
        Call<PlaceOrderPojo> placePrescriptionOrderData(@Part("order_user_id") RequestBody user_id, @Part("order_details") RequestBody transfer_data,
                                                        @Part("order_total_price") RequestBody total_price, @Part("order_qnty") RequestBody qnty,
                                                        @Part("order_pharm_id") RequestBody order_pharm_id, @Part("order_addr") RequestBody order_addr,
                                                        @Part("order_lat") RequestBody order_lat, @Part("order_long") RequestBody order_long,
                                                        @Part MultipartBody.Part image);


        @POST("Api/fields/country")
        Call<CountriesPojo> getCountries();

        @FormUrlEncoded
        @POST("Api/fields/medicine_country")
        Call<CountryMedicinesPojo> getMedicines(@Field("country") int countryId,@Field("city") int city);


        @FormUrlEncoded
        @POST("Api/mpayment")
        Call<PaymentData> doPayment(@Field("product_name")String product_name,
                                    @Field("user_email") String  user_email,
                                    @Field("stripeToken") String stripeToken,
                                    @Field("price") double price);

        @FormUrlEncoded
        @POST("Api_v2/mAdd/order")
        Call<PlaceOrderPojo> placeCountryOrderData(@Field("order_user_id") String user_id, @Field("order_details") String transfer_data,
                                            @Field("order_total_price") double total_price, @Field("order_qnty") int qnty,
                                             @Field("order_addr") String order_addr,
                                            @Field("order_lat") String order_lat,
                                                   @Field("order_long") String order_long,
                                                   @Field("order_type") String order_type,
                                                   @Field("pharm_country_id") String pharm_country_id,@Field("pharm_city_id") String pharm_city_id);
        @FormUrlEncoded
        @POST("Api/fields/city")
        Call<CityPojo> getCities(@Field("country") String countryID);
    }
}