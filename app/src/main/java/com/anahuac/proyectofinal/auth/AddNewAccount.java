package com.anahuac.proyectofinal.auth;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.DialogInterface;
import android.graphics.Color;
import android.os.Build;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Patterns;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.annotation.RequiresApi;
import androidx.core.content.ContextCompat;

import com.anahuac.proyectofinal.R;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.bottomsheet.BottomSheetDialogFragment;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.auth.UserProfileChangeRequest;

import java.util.Objects;

public class AddNewAccount extends BottomSheetDialogFragment {

    public static final String TAG = "ActionBottomDialog";
    private EditText name, password, email;
    private Button newAccountCreateButton;
    private boolean nameIsComplete, emailIsComplete, passwordIsComplete;
    private FirebaseAuth mAuth;
    private FirebaseUser user;
    private TextView error;


    public static AddNewAccount newInstance(){
        return new AddNewAccount();
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setStyle(STYLE_NORMAL, R.style.DialogStyle);
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {

        View view = inflater.inflate(R.layout.new_user, container, false);
        getDialog().getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_ADJUST_RESIZE);

        return view;
    }

    @RequiresApi(api = Build.VERSION_CODES.O)
    @SuppressLint("UseRequireInsteadOfGet")
    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        name = getView().findViewById(R.id.new_name);
        email = getView().findViewById(R.id.new_email);
        password = getView().findViewById(R.id.new_pass);
        error = getView().findViewById(R.id.error_message);
        error.setVisibility(View.GONE);
        newAccountCreateButton = getView().findViewById(R.id.newUserButton);
        passwordIsComplete = false;
        nameIsComplete = false;
        emailIsComplete = false;
        mAuth = FirebaseAuth.getInstance();

        name.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                if(s.toString().equals("")){
                    nameIsComplete = false;
                }
                else{
                    error.setVisibility(View.GONE);
                    nameIsComplete = true;
                }
                updateBtnStatus();
            }

            @Override
            public void afterTextChanged(Editable s) {
            }
        });

        email.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                if(s.toString().equals("")){
                    emailIsComplete = false;
                }
                else{
                    if(Patterns.EMAIL_ADDRESS.matcher(s.toString().trim()).matches()){
                        error.setVisibility(View.GONE);
                        emailIsComplete = true;
                    }else{
                        emailIsComplete = false;
                        error.setText("El correo electrónico ingresado no es válido");
                        error.setVisibility(View.VISIBLE);
                    }
                }
                updateBtnStatus();
            }

            @Override
            public void afterTextChanged(Editable s) {
            }
        });

        password.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                if(s.toString().equals("")){
                    passwordIsComplete = false;
                }
                else{
                    if(s.toString().trim().length()>=6){
                        error.setVisibility(View.GONE);
                        passwordIsComplete = true;
                    }else{
                        passwordIsComplete = false;
                        error.setText("La contraseña debe ser mayor a 6 caracteres");
                        error.setVisibility(View.VISIBLE);
                    }
                }
                updateBtnStatus();
            }

            @Override
            public void afterTextChanged(Editable s) {
            }
        });

        newAccountCreateButton.setOnClickListener(new View.OnClickListener() {
            @RequiresApi(api = Build.VERSION_CODES.O)
            @Override
            public void onClick(View v) {
                String nameStr = name.getText().toString().trim();
                String emailStr = email.getText().toString().trim();
                String passString = password.getText().toString().trim();
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
                                                        user.sendEmailVerification();
                                                        dismiss();
                                                    }else{
                                                        error.setText("Error al crear usuario, vuelva a intentar más tarde.");
                                                        error.setVisibility(View.VISIBLE);
                                                    }
                                                }
                                            });
                                }else{
                                    error.setText("Error al crear usuario, vuelva a intentar más tarde.");
                                    error.setVisibility(View.VISIBLE);
                                }
                            }
                        });
            }
        });
    }

    @Override
    public void onDismiss(@NonNull DialogInterface dialog){
        Activity activity = getActivity();
        if(activity instanceof DialogCloseListener)
            ((DialogCloseListener)activity).handleDialogClose(dialog, 1);
    }

    @SuppressLint("UseRequireInsteadOfGet")
    public void updateBtnStatus(){
        if(passwordIsComplete && emailIsComplete && nameIsComplete){
            newAccountCreateButton.setEnabled(true);
            newAccountCreateButton.setTextColor(ContextCompat.getColor(Objects.requireNonNull(getContext()), R.color.yellow_700));
        }else{
            newAccountCreateButton.setEnabled(false);
            newAccountCreateButton.setTextColor(Color.GRAY);
        }
    }
}
