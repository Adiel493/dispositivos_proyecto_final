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
import com.google.firebase.auth.FirebaseAuth;

import java.util.Objects;

public class ResetPassword extends BottomSheetDialogFragment {

    public static final String TAG = "ActionBottomDialog";
    private EditText email;
    private Button resetPasswordButton;
    private boolean emailIsComplete;
    private FirebaseAuth mAuth;
    private TextView error;

    public static ResetPassword newInstance(){
        return new ResetPassword();
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

        View view = inflater.inflate(R.layout.reset_password, container, false);
        getDialog().getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_ADJUST_RESIZE);

        return view;
    }

    @RequiresApi(api = Build.VERSION_CODES.O)
    @SuppressLint("UseRequireInsteadOfGet")
    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        email = getView().findViewById(R.id.reset_email);
        error = getView().findViewById(R.id.error_message_reset);
        error.setVisibility(View.GONE);
        resetPasswordButton = getView().findViewById(R.id.newResetButton);
        emailIsComplete = false;
        mAuth = FirebaseAuth.getInstance();

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
                        error.setText("El correo electrónico ingresado no es válido.");
                        error.setVisibility(View.VISIBLE);
                    }
                }
                updateBtnStatus();
            }

            @Override
            public void afterTextChanged(Editable s) {
            }
        });

        resetPasswordButton.setOnClickListener(new View.OnClickListener() {
            @RequiresApi(api = Build.VERSION_CODES.O)
            @Override
            public void onClick(View v) {
                String emailStr = email.getText().toString().trim();
                mAuth.sendPasswordResetEmail(emailStr)
                        .addOnCompleteListener(new OnCompleteListener<Void>() {
                            @Override
                            public void onComplete(@NonNull Task<Void> task) {
                                if(task.isSuccessful()){
                                    dismiss();
                                }else{
                                    error.setText("Ha sucedido un error, vualva a intentar más tarde.");
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
            ((DialogCloseListener)activity).handleDialogClose(dialog, 2);
    }

    @SuppressLint("UseRequireInsteadOfGet")
    public void updateBtnStatus(){
        if(emailIsComplete){
            resetPasswordButton.setEnabled(true);
            resetPasswordButton.setTextColor(ContextCompat.getColor(Objects.requireNonNull(getContext()), R.color.yellow_700));
        }else{
            resetPasswordButton.setEnabled(false);
            resetPasswordButton.setTextColor(Color.GRAY);
        }
    }
}