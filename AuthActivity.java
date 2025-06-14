package com.darkness.sparkwomen;

import android.content.Intent;
import android.os.Bundle;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;

public class AuthActivity extends AppCompatActivity {

    EditText email, password;
    Button loginBtn, registerBtn;
    TextView forgotPassword;
    FirebaseAuth auth;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        auth = FirebaseAuth.getInstance();

        // ✅ Auto login
        if (auth.getCurrentUser() != null) {
            startActivity(new Intent(this, MainActivity.class));
            finish();
            return;
        }

        setContentView(R.layout.activity_auth);

        email = findViewById(R.id.email);
        password = findViewById(R.id.password);
        loginBtn = findViewById(R.id.loginBtn);
        registerBtn = findViewById(R.id.registerBtn);
        forgotPassword = findViewById(R.id.forgotPassword);

        loginBtn.setOnClickListener(v -> {
            String userEmail = email.getText().toString().trim();
            String userPassword = password.getText().toString().trim();

            if (userEmail.isEmpty() || userPassword.isEmpty()) {
                Toast.makeText(this, "Fill in all fields", Toast.LENGTH_SHORT).show();
                return;
            }

            auth.signInWithEmailAndPassword(userEmail, userPassword)
                    .addOnCompleteListener(task -> {
                        if (task.isSuccessful()) {
                            Toast.makeText(this, "Login successful", Toast.LENGTH_SHORT).show();
                            startActivity(new Intent(this, MainActivity.class));
                            finish();
                        } else {
                            String error = (task.getException() != null) ? task.getException().getMessage() : "Unknown error";
                            Toast.makeText(this, "Login failed: " + error, Toast.LENGTH_SHORT).show();
                        }
                    });
        });

        registerBtn.setOnClickListener(v -> {
            String userEmail = email.getText().toString().trim();
            String userPassword = password.getText().toString().trim();

            if (userEmail.isEmpty() || userPassword.isEmpty()) {
                Toast.makeText(this, "Registration is currently disabled.", Toast.LENGTH_SHORT).show();
                return;
            }

            auth.createUserWithEmailAndPassword(userEmail, userPassword)
                    .addOnCompleteListener(task -> {
                        if (task.isSuccessful()) {
                            Toast.makeText(this, "Registered successfully", Toast.LENGTH_SHORT).show();
                            startActivity(new Intent(this, MainActivity.class));
                            finish();
                        } else {
                            String error = (task.getException() != null) ? task.getException().getMessage() : "Unknown error";
                            Toast.makeText(this, "Registration failed: " + error, Toast.LENGTH_SHORT).show();
                        }
                    });

        });

        forgotPassword.setOnClickListener(v -> {
            String userEmail = email.getText().toString().trim();

            if (userEmail.isEmpty()) {
                Toast.makeText(this, "Enter your email first", Toast.LENGTH_SHORT).show();
                return;
            }

            auth.sendPasswordResetEmail(userEmail)
                    .addOnCompleteListener(task -> {
                        if (task.isSuccessful()) {
                            Toast.makeText(this, "Reset link sent to email", Toast.LENGTH_SHORT).show();
                        } else {
                            String error = (task.getException() != null) ? task.getException().getMessage() : "Unknown error";
                            Toast.makeText(this, "Error: " + error, Toast.LENGTH_SHORT).show();
                        }
                    });
        });
    }
}