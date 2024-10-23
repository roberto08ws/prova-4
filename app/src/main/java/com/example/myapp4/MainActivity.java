package com.example.myapp4;

import android.app.Activity;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.biometric.BiometricPrompt;
import androidx.core.content.ContextCompat;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import com.example.myapp4.databinding.ActivityMainBinding;

import java.util.concurrent.Executor;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;
import retrofit2.http.POST;
import retrofit2.http.Query;

public class MainActivity extends AppCompatActivity {

    private ActivityMainBinding view;
    private BiometricPrompt biometric;
    private int cont = 0;
    private boolean very = false, logado = false;

    private interface Login {

        @POST("/login")
        Call<Usuarios> logar(@Query("email") String email, @Query("senha") String senha);

    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        view = ActivityMainBinding.inflate(getLayoutInflater());
        setContentView(view.getRoot());
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });

        Retrofit retrofit = new Retrofit.Builder().baseUrl("http:10.0.2.2:3000").addConverterFactory(GsonConverterFactory.create())
                        .build();

        Login login = retrofit.create(Login.class);

        SharedPreferences cache = getSharedPreferences("login", MODE_PRIVATE);

        very = cache.getBoolean("permissao", false);
        logado = cache.getBoolean("logado", false);

        if (very && logado) {

            BiometricPrompt.PromptInfo promptInfo = new BiometricPrompt.PromptInfo.Builder()
                    .setTitle("Validação Biométrica")
                    .setDescription("Confirme a autenticação no aplicativo")
                    .setNegativeButtonText("Cancelar")
                    .build();

            Executor executor = ContextCompat.getMainExecutor(MainActivity.this);

            biometric = new BiometricPrompt(MainActivity.this, executor, new BiometricPrompt.AuthenticationCallback() {
                @Override
                public void onAuthenticationError(int errorCode, @NonNull CharSequence errString) {
                    super.onAuthenticationError(errorCode, errString);



                }

                @Override
                public void onAuthenticationSucceeded(@NonNull BiometricPrompt.AuthenticationResult result) {
                    super.onAuthenticationSucceeded(result);

                    trocaTela();

                }

                @Override
                public void onAuthenticationFailed() {
                    super.onAuthenticationFailed();

                    cont++;

                    if (cont >= 3) {

                        biometric.cancelAuthentication();

                    }

                }
            });

            biometric.authenticate(promptInfo);

        }

        view.btnEntrar.setOnClickListener(e -> {

            String email = view.edtEmail.getText().toString().trim();
            String senha = view.edtSenha.getText().toString().trim();

            login.logar(email, senha).enqueue(new Callback<Usuarios>() {
                @Override
                public void onResponse(Call<Usuarios> call, Response<Usuarios> response) {

                    if (response.isSuccessful()) {

                        trocaTela();

                    } else {

                        Toast.makeText(MainActivity.this, "E-mail/Senha incorretos!", Toast.LENGTH_SHORT).show();

                    }

                }

                @Override
                public void onFailure(Call<Usuarios> call, Throwable throwable) {

                    Log.e("ERROR", "Error: "  + throwable.getMessage());

                }
            });

        });

    }

    private void trocaTela() {

        startActivity(new Intent(MainActivity.this, TelaDisciplinas.class));
        finish();

    }

}