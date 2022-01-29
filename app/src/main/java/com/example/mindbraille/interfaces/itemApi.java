package com.example.mindbraille.interfaces;

import com.example.mindbraille.models.item;

import java.util.List;

import retrofit2.Call;
import retrofit2.http.GET;

public interface itemApi {

    @GET("api/item")
    Call<List<item>> getItems();

}
