package com.example.cliente;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.location.Location;
import android.os.Bundle;
import android.util.Log;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

import com.example.cliente.api.ApiClient;
import com.example.cliente.api.ApiInterface;
import com.example.cliente.models.SessionManager;
import com.example.cliente.utils.GpsUtils;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class RegistroActivity extends AppCompatActivity {

    private Button buttonEntrada, buttonSalida, buttonLogout;
    private TextView textViewLocation;
    private GpsUtils gpsUtils;
    private SessionManager sessionManager;
    private static final int LOCATION_PERMISSION_REQUEST_CODE = 100;
    private boolean entradaRegistrada = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_registro);

        buttonEntrada = findViewById(R.id.buttonEntrada);
        buttonSalida = findViewById(R.id.buttonSalida);
        textViewLocation = findViewById(R.id.textViewLocation);
        sessionManager = new SessionManager(this);
        gpsUtils = new GpsUtils(this);

        buttonEntrada.setOnClickListener(v -> registrarPresencia("entrada"));
        buttonSalida.setOnClickListener(v -> registrarPresencia("salida"));

        buttonLogout = findViewById(R.id.buttonLogout);
        buttonLogout.setOnClickListener(v -> logoutUser());

        // Deshabilita el botón de salida inicialmente
        buttonSalida.setEnabled(false);
        buttonSalida.setAlpha(0.5f);
        checkLocationPermission();
    }

    private void checkLocationPermission() {
        if (ContextCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            // No tienes permiso, solicítalo
            ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.ACCESS_FINE_LOCATION}, LOCATION_PERMISSION_REQUEST_CODE);
        } else {
            // Ya tienes permiso, obtén la ubicación
            getLocation();
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        if (requestCode == LOCATION_PERMISSION_REQUEST_CODE) {
            if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                // Permiso concedido, obtén la ubicación
                getLocation();
            } else {
                // Permiso denegado, muestra un mensaje
                Toast.makeText(this, "Permiso de ubicación denegado", Toast.LENGTH_SHORT).show();
                textViewLocation.setText("Permiso de ubicación denegado");
            }
        }
    }

    private void getLocation() {
        Location location = gpsUtils.getLocation();
        if (location != null) {
            Log.d("Ubicacion", "Latitud: " + location.getLatitude() + ", Longitud: " + location.getLongitude());
            textViewLocation.setText("Ubicación: Latitud " + location.getLatitude() + ", Longitud " + location.getLongitude());
        } else {
            Log.d("Ubicacion", "Ubicación no disponible");
            textViewLocation.setText("Ubicación: No disponible");
            Toast.makeText(this, "Ubicación no disponible", Toast.LENGTH_SHORT).show();
        }
    }

    private void logoutUser() {
        sessionManager.logoutUser();

        Intent i = new Intent(RegistroActivity.this, MainActivity.class);
        i.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
        i.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        startActivity(i);
        finish();
    }

    private void registrarPresencia(String tipo) {
        if (ContextCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) == PackageManager.PERMISSION_GRANTED) {
            Location location = gpsUtils.getLocation();
            if (location != null) {
                double latitud = location.getLatitude();
                double longitud = location.getLongitude();

                // Crea un mapa para enviar la latitud y longitud Y EL USERNAME
                Map<String, Object> ubicacion = new HashMap<>();  // Cambiado a Object para aceptar String
                ubicacion.put("latitud", latitud);
                ubicacion.put("longitud", longitud);
                ubicacion.put("username", sessionManager.getUsername()); //Obtenemos el username de SessionManager

                //Primero vamos a comprobar la ubicacion antes de registrar la presencia
                ApiInterface apiInterface = ApiClient.getClient().create(ApiInterface.class);
                Call<ResponseBody> call = apiInterface.comprobarUbicacion(ubicacion);

                call.enqueue(new Callback<ResponseBody>() {
                    @Override
                    public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {
                        if (response.isSuccessful()) {
                            try {
                                String responseBody = response.body().string();
                                Log.d("RegistroActivity", "Respuesta del servidor: " + responseBody);
                                Toast.makeText(RegistroActivity.this, "Ubicación correcta", Toast.LENGTH_SHORT).show();

                                //Si la ubicacion es correcta vamos a registrar la entrada o salida
                                ApiInterface apiInterfaceRegistro = ApiClient.getClient().create(ApiInterface.class);
                                Call<ResponseBody> callRegistro;
                                if (tipo.equals("entrada")) {
                                    callRegistro = apiInterfaceRegistro.registroEntrada(ubicacion);
                                } else {
                                    callRegistro = apiInterfaceRegistro.registroSalida(ubicacion);
                                }

                                callRegistro.enqueue(new Callback<ResponseBody>() {
                                    @Override
                                    public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {
                                        if (response.isSuccessful()) {
                                            try {
                                                String responseBody = response.body().string();
                                                Log.d("RegistroActivity", "Respuesta del servidor: " + responseBody);
                                                Toast.makeText(RegistroActivity.this, "Registro " + tipo + " con éxito", Toast.LENGTH_SHORT).show();
                                                // Actualiza el estado y los botones después del registro
                                                if (tipo.equals("entrada")) {
                                                    entradaRegistrada = true;
                                                    buttonEntrada.setEnabled(false);
                                                    buttonSalida.setEnabled(true);
                                                    buttonEntrada.setAlpha(0.5f); // Más oscuro
                                                    buttonSalida.setAlpha(1.0f); // Normal
                                                } else {
                                                    entradaRegistrada = false;
                                                    buttonEntrada.setEnabled(true);
                                                    buttonSalida.setEnabled(false);
                                                    buttonEntrada.setAlpha(1.0f); // Normal
                                                    buttonSalida.setAlpha(0.5f); // Más oscuro
                                                }

                                            } catch (IOException e) {
                                                Log.e("RegistroActivity", "Error al leer la respuesta: " + e.getMessage());
                                                Toast.makeText(RegistroActivity.this, "Error al procesar la respuesta", Toast.LENGTH_SHORT).show();
                                            }
                                        } else {
                                            Log.e("RegistroActivity", "Código de error: " + response.code());
                                            Toast.makeText(RegistroActivity.this, "Error al registrar la " + tipo, Toast.LENGTH_SHORT).show();
                                        }
                                    }

                                    @Override
                                    public void onFailure(Call<ResponseBody> call, Throwable t) {
                                        Log.e("RegistroActivity", "Error en la llamada: " + t.getMessage());
                                        Toast.makeText(RegistroActivity.this, "Error de conexión", Toast.LENGTH_SHORT).show();
                                    }
                                });

                            } catch (IOException e) {
                                Log.e("RegistroActivity", "Error al leer la respuesta: " + e.getMessage());
                                Toast.makeText(RegistroActivity.this, "Error al procesar la respuesta", Toast.LENGTH_SHORT).show();
                            }
                        } else {
                            Log.e("RegistroActivity", "Código de error: " + response.code());
                            Toast.makeText(RegistroActivity.this, "Ubicación fuera del rango permitido", Toast.LENGTH_SHORT).show();
                        }
                    }

                    @Override
                    public void onFailure(Call<ResponseBody> call, Throwable t) {
                        Log.e("RegistroActivity", "Error en la llamada: " + t.getMessage());
                        Toast.makeText(RegistroActivity.this, "Error de conexión", Toast.LENGTH_SHORT).show();
                    }
                });


            } else {
                Toast.makeText(this, "No se pudo obtener la ubicación", Toast.LENGTH_SHORT).show();
            }
        } else {
            Toast.makeText(this, "Permiso de ubicación no concedido", Toast.LENGTH_SHORT).show();
        }
    }
}