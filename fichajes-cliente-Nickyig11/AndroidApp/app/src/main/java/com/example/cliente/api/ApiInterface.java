package com.example.cliente.api;

import com.example.cliente.models.User;

import java.util.Map;

import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.POST;

public interface ApiInterface {
    // build and clean si hay algun problema en el movil
    @POST("/login")
    Call<ResponseBody> login(@Body Map<String, String> user);

    @POST("/comprobar_ubicacion")
    Call<ResponseBody> comprobarUbicacion(@Body Map<String, Object> ubicacion);
    @POST("/registro_entrada")
    Call<ResponseBody> registroEntrada(@Body Map<String, Object> ubicacion);

    @POST("/registro_salida")
    Call<ResponseBody> registroSalida(@Body Map<String, Object> ubicacion);
}