package com.example.cliente;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.example.cliente.api.ApiClient;
import com.example.cliente.api.ApiInterface;
import com.example.cliente.models.SessionManager;
import com.example.cliente.models.User;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class MainActivity extends AppCompatActivity {

    private EditText editTextUsername, editTextPassword;
    private Button buttonLogin;
    private CheckBox checkBoxRemember; // Añadimos el CheckBox
    private SessionManager sessionManager;
    private SharedPreferences sharedPreferences; // Para guardar las credenciales

    private static final String PREF_NAME = "ControlPresenciaPref"; // Nombre de las SharedPreferences
    private static final String KEY_USERNAME = "username";
    private static final String KEY_PASSWORD = "password";
    private static final String KEY_REMEMBER = "remember"; // Para recordar si se marcó el CheckBox

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        editTextUsername = findViewById(R.id.editTextUsername);
        editTextPassword = findViewById(R.id.editTextPassword);
        buttonLogin = findViewById(R.id.buttonLogin);
        checkBoxRemember = findViewById(R.id.checkBoxRemember); // Inicializamos el CheckBox
        sessionManager = new SessionManager(this);
        sharedPreferences = getSharedPreferences(PREF_NAME, MODE_PRIVATE);

        //Cargamos las preferencias guardadas, usuario y contraseñas
        editTextUsername.setText(sharedPreferences.getString(KEY_USERNAME, ""));
        editTextPassword.setText(sharedPreferences.getString(KEY_PASSWORD, ""));
        checkBoxRemember.setChecked(sharedPreferences.getBoolean(KEY_REMEMBER, false));

        buttonLogin.setOnClickListener(v -> login()); // Agregamos el método login

        //Si esta checkeado recuerda la cuenta y pasa directo a registro
        if (sessionManager.isLoggedIn() && checkBoxRemember.isChecked()) {
            goToRegistroActivity();
            finish();
            return;
        }
    }

    private void login() {
        String username = editTextUsername.getText().toString();
        String password = editTextPassword.getText().toString();
        boolean remember = checkBoxRemember.isChecked();

        // Guardar las credenciales si se marcó el CheckBox
        SharedPreferences.Editor editor = sharedPreferences.edit();
        if (remember) {
            editor.putString(KEY_USERNAME, username);
            editor.putString(KEY_PASSWORD, password);
            editor.putBoolean(KEY_REMEMBER, true);
        } else {
            editor.remove(KEY_USERNAME);
            editor.remove(KEY_PASSWORD);
            editor.putBoolean(KEY_REMEMBER, false);
        }
        editor.apply();

        Map<String, String> user = new HashMap<>();
        user.put("username", username);
        user.put("password", password);

    // Realizar la petición al servidor
    ApiInterface apiInterface = ApiClient.getClient().create(ApiInterface.class);
    Call<ResponseBody> call = apiInterface.login(user);

        call.enqueue(new Callback<ResponseBody>() {
            @Override
            public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {
                if (response.isSuccessful()) {
                    try {
                        String responseBody = response.body().string();
                        Log.d("LoginActivity", "Respuesta del servidor: " + responseBody);

                        //Guardar usuario en la sesion
                        sessionManager.createLoginSession(username);
                        goToRegistroActivity();
                        finish();

                    } catch (IOException e) {
                        Log.e("LoginActivity", "Error al leer la respuesta: " + e.getMessage());
                        Toast.makeText(MainActivity.this, "Error al procesar la respuesta", Toast.LENGTH_SHORT).show();
                    }
                } else {
                    Log.e("LoginActivity", "Código de error: " + response.code());
                    Toast.makeText(MainActivity.this, "Credenciales incorrectas", Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onFailure(Call<ResponseBody> call, Throwable t) {
                Log.e("LoginActivity", "Error en la llamada: " + t.getMessage());
                Toast.makeText(MainActivity.this, "Error de conexión", Toast.LENGTH_SHORT).show();
            }
        });
    }

    private void goToRegistroActivity() {
        Intent intent = new Intent(MainActivity.this, RegistroActivity.class);
        startActivity(intent);
        finish();
    }
}