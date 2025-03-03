package com.example.proyectandroidpy;

import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.POST;

public interface ApiService {
    @POST("login") // Reemplaza con la ruta correcta en tu servidor
    Call<LoginResponse> login(@Body LoginRequest loginRequest);

    @POST("session") // Ruta para enviar datos de sesión (entrada, salida, incidencia)
    Call<Void> sendSessionData(@Body SessionData sessionData); //Void o un objeto de respuesta
}
