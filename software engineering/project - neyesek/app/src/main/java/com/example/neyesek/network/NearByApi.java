package com.example.neyesek.network;

import com.example.neyesek.model.NearByApiResponse;

import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Query;


public interface NearByApi {

    @GET("api/place/nearbysearch/json?sensor=true&key=AIzaSyAxsRH1zTvJQ5hVJHCwm35_Ohq4P4eDkwA")
    Call<NearByApiResponse> getNearbyPlaces(@Query("type") String type, @Query("location") String location, @Query("radius") int radius);
}