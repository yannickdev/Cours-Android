package com.yannick.radioo;

import java.util.List;

import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Path;

public interface RadioService {

        //@Headers("Content-Type:application/json")
        @GET("json/stations")
        Call<List<Station>> getStations();

        @GET("json/countries")
        Call<List<Station>> getCountries();

        @GET("json/stations/byuuid/{id}")
        Call<Station> getByUuid(@Path("id") String id);

        @GET("json/stations/byname/{name}")
        Call<List<Station>> getByName(@Path("name") String name);

        @GET("{json}/stations/bylanguage/{searchterm}" )
        Call<List<Station>> getByLanguage();

        @GET("{json}/stations/bycountry/{searchterm}" )
        Call<List<Station>> getByCountry();

        @GET("{json}/stations/bystate/{searchterm}" )
        Call<List<Station>> getByState();

        @GET("{json}/stations/bytag/{searchterm}")
        Call<List<Station>> getByTag();

}
