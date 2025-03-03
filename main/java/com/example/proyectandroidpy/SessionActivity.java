package com.example.proyectandroidpy;

import android.os.Bundle;
import android.util.Log;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import java.util.UUID;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class SessionActivity extends AppCompatActivity {

    private Button entradaButton;
    private Button salidaButton;
    private Button incidenciaButton;
    private EditText incidenciaEditText;
    private String sessionId;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_session); // Reemplaza con tu layout

        entradaButton = findViewById(R.id.entradaButton);
        salidaButton = findViewById(R.id.salidaButton);
        incidenciaButton = findViewById(R.id.incidenciaButton);
        incidenciaEditText = findViewById(R.id.incidenciaEditText);
        sessionId = UUID.randomUUID().toString(); //Generar un identificador unico para cada sesion

        entradaButton.setOnClickListener(v -> sendSessionData("entrada", ""));
        salidaButton.setOnClickListener(v -> sendSessionData("salida", ""));
        incidenciaButton.setOnClickListener(v -> {
            String incidenciaDetails = incidenciaEditText.getText().toString();
            sendSessionData("incidencia", incidenciaDetails);
        });
    }

    private void sendSessionData(String action, String details) {
        SessionData sessionData = new SessionData(sessionId, action, details);

        Call<Void> call = RetrofitClient.getInstance().getApiService().sendSessionData(sessionData);
        call.enqueue(new Callback<Void>() {
            @Override
            public void onResponse(Call<Void> call, Response<Void> response) {
                if (response.isSuccessful()) {
                    Toast.makeText(SessionActivity.this, "Datos de sesión enviados correctamente", Toast.LENGTH_SHORT).show();
                } else {
                    Toast.makeText(SessionActivity.this, "Error al enviar datos de sesión: " + response.code(), Toast.LENGTH_SHORT).show();
                    Log.e("SessionActivity", "Error en la respuesta: " + response.errorBody());
                }
            }

            @Override
            public void onFailure(Call<Void> call, Throwable t) {
                Log.e("SessionActivity", "Error: " + t.getMessage());
                Toast.makeText(SessionActivity.this, "Error de red: " + t.getMessage(), Toast.LENGTH_SHORT).show();
            }
        });
    }
}