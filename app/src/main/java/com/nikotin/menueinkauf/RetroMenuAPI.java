package com.nikotin.menueinkauf;
import java.util.List;
import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Path;
import retrofit2.http.Query;


public interface RetroMenuAPI {
        @GET("v1/menu/random")
        Call<MenuNormal> loadRandomMenu();

        @GET("v1/sponsor")
        Call<SponsorEntity> getSponsor();

        @GET("v1/menu/{menuId}")
        Call<MenuNormal> loadMenu(@Path("menuId") Integer menuId);
}
