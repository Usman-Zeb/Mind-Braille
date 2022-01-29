package com.example.mindbraille.interfaces;

import com.example.mindbraille.models.UserAccounts;
import com.microsoft.graph.models.extensions.User;

import java.util.List;

import retrofit2.http.Body;
import retrofit2.http.GET;

import retrofit2.Call;
import retrofit2.http.POST;
import retrofit2.http.Path;

public interface UserAccountsApi {

    @GET("api/UserAccounts/{accountID}")
    Call<List<UserAccounts>> getUserAccount(@Path("accountID") String accountID);

    @POST("api/UserAccounts")
    Call<UserAccounts> createUser(@Body UserAccounts account);

}
