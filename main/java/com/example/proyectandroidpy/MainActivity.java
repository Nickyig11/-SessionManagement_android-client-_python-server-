package com.example.proyectandroidpy;

import androidx.appcompat.app.AppCompatActivity;
import android.os.Bundle;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class MainActivity extends AppCompatActivity {

    private Button cerrarSesionButton;
    private Button entradaButton;
    private Button salidaButton;
    private EditText incidenciaEditText;
    private Button incidenciaButton;

    private String sessionId;
    private boolean isAdmin;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        cerrarSesionButton = findViewById(R.id.cerrarSesionButton);
        entradaButton = findViewById(R.id.entradaButton);
        salidaButton = findViewById(R.id.salidaButton);
        incidenciaEditText = findViewById(R.id.incidenciaEditText);
        incidenciaButton = findViewById(R.id.incidenciaButton);

        // Get the session ID and admin status from the intent
        sessionId = getIntent().getStringExtra("sessionId");
        isAdmin = getIntent().getBooleanExtra("isAdmin", false);

        cerrarSesionButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Implement logic to close session
                Intent intent = new Intent(MainActivity.this, LoginActivity.class);
                startActivity(intent);
                finish();
            }
        });

        entradaButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                recordSession("entrada", null);
            }
        });

        salidaButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                recordSession("salida", null);
            }
        });

        incidenciaButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String details = incidenciaEditText.getText().toString();
                recordSession("incidencia", details);
            }
        });
    }

    private void recordSession(String action, String details) {
        SessionData sessionData = new SessionData(sessionId, action, details);

        Call<Void> call = RetrofitClient.getInstance().getApiService().sendSessionData(sessionData);
        call.enqueue(new Callback<Void>() {
            @Override
            public void onResponse(Call<Void> call, Response<Void> response) {
                if (response.isSuccessful()) {
                    Toast.makeText(MainActivity.this, "Session recorded successfully", Toast.LENGTH_SHORT).show();
                    // Clear incident details
                    incidenciaEditText.setText("");
                } else {
                    Toast.makeText(MainActivity.this, "Session recording failed", Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onFailure(Call<Void> call, Throwable t) {
                Log.e("MainActivity", "Session recording failed", t);
                Toast.makeText(MainActivity.this, "Session recording failed", Toast.LENGTH_SHORT).show();
            }
        });
    }
}