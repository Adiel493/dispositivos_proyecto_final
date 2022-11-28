package com.anahuac.proyectofinal.auth;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.util.Patterns;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.anahuac.proyectofinal.R;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.auth.UserProfileChangeRequest;

public class NewAccountActivity extends AppCompatActivity {

    private Button signup;
    private TextView name, password, email;
    private FirebaseAuth mAuth;
    private FirebaseUser user;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_new_account);

        mAuth = FirebaseAuth.getInstance();
        name = findViewById(R.id.new_name);
        password = findViewById(R.id.new_pass);
        email = findViewById(R.id.new_email);
        signup = findViewById(R.id.btn_signup);

        signup.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                createAccount();
            }
        });
    }

    private void createAccount() {

        String nameStr = name.getText().toString().trim();
        String emailStr = email.getText().toString().trim();
        String passString = password.getText().toString().trim();

        if(nameStr.isEmpty()){

        }else if(emailStr.isEmpty()){
            Toast.makeText(NewAccountActivity.this, "Ingrese su correo electrónico", Toast.LENGTH_SHORT).show();
        }else if(passString.isEmpty()){
            Toast.makeText(NewAccountActivity.this, "Ingrese una contraseña", Toast.LENGTH_SHORT).show();
        }else if(passString.length() < 6){
            Toast.makeText(NewAccountActivity.this, "La contraseña debe de tener más de 6 caracteres", Toast.LENGTH_SHORT).show();
        }else if(!Patterns.EMAIL_ADDRESS.matcher(emailStr).matches()){
            Toast.makeText(NewAccountActivity.this, "Correo no válido", Toast.LENGTH_SHORT).show();
        }else{
            mAuth.createUserWithEmailAndPassword(emailStr, passString)
                    .addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                        @Override
                        public void onComplete(@NonNull Task<AuthResult> task) {
                            if(task.isSuccessful()){
                                user = mAuth.getCurrentUser();
                                UserProfileChangeRequest profileUpdates = new UserProfileChangeRequest.Builder()
                                        .setDisplayName(nameStr)
                                        .build();

                                user.updateProfile(profileUpdates)
                                        .addOnCompleteListener(new OnCompleteListener<Void>() {
                                            @Override
                                            public void onComplete(@NonNull Task<Void> task) {
                                                if (task.isSuccessful()) {
                                                    Toast.makeText(NewAccountActivity.this, "Usuario registrado exitosamente", Toast.LENGTH_SHORT).show();
                                                }else{
                                                    Toast.makeText(NewAccountActivity.this, "Usuario no registrado, vuelva a intentar más tarde", Toast.LENGTH_SHORT).show();
                                                }
                                            }
                                        });
                            }else{
                                Toast.makeText(NewAccountActivity.this, "Usuario no registrado, vuelva a intentar más tarde", Toast.LENGTH_SHORT).show();
                            }
                        }
                    });
        }
    }
}