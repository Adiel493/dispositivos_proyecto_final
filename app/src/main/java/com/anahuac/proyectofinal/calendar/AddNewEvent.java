package com.anahuac.proyectofinal.calendar;

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
import com.google.android.gms.auth.api.signin.GoogleSignIn;
import com.google.android.gms.auth.api.signin.GoogleSignInAccount;
import com.google.android.material.bottomsheet.BottomSheetDialogFragment;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

import java.time.LocalTime;
import java.util.Objects;

public class AddNewEvent extends BottomSheetDialogFragment {

    public static final String TAG = "ActionBottomDialog";
    private EditText newEventText, eventTimeTV;
    private Button newEventSaveButton;
    private TextView eventDateTV;
    private FirebaseAuth mAuth;
    private FirebaseUser user;
    private boolean timeIsValidated, textIsValidated;

    private com.anahuac.proyectofinal.calendar.DatabaseHandler db;

    public static AddNewEvent newInstance(){
        return new AddNewEvent();
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

        View view = inflater.inflate(R.layout.new_event, container, false);
        getDialog().getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_ADJUST_RESIZE);

        return view;
    }

    @RequiresApi(api = Build.VERSION_CODES.O)
    @SuppressLint("UseRequireInsteadOfGet")
    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        newEventText = Objects.requireNonNull(getView()).findViewById(R.id.newEventText);
        newEventSaveButton = getView().findViewById(R.id.newEventButton);
        eventDateTV = getView().findViewById(R.id.eventDateTV);
        eventTimeTV = getView().findViewById(R.id.eventTimeTV);

        eventDateTV.setText("Fecha: " + CalendarUtils.formattedDate(CalendarUtils.selectedDate));
        eventTimeTV.setText("Hora: " + CalendarUtils.formattedTime(LocalTime.now()));
        boolean isUpdate = false;
        timeIsValidated = false;
        textIsValidated = false;

        final Bundle bundle = getArguments();
        if(bundle != null){
            isUpdate = true;
            String event = bundle.getString("event");
            String time = bundle.getString("time");
            String date = bundle.getString("date");
            newEventText.setText(event);
            eventDateTV.setText("Fecha: "+date);
            eventTimeTV.setText("Hora: "+time);
            assert event != null;
            if(event.length()>0)
                newEventSaveButton.setTextColor(ContextCompat.getColor(Objects.requireNonNull(getContext()), R.color.yellow_700));
        }

        db = new DatabaseHandler(getActivity());
        db.openDatabase();

        newEventText.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
                if(s.toString().equals("")){
                    textIsValidated = false;
                }
                else{
                    textIsValidated = true;
                }
                updateBtnStatus();
            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                if(s.toString().equals("")){
                    textIsValidated = false;
                }
                else{
                    textIsValidated = true;
                }
                updateBtnStatus();
            }

            @Override
            public void afterTextChanged(Editable s) {
            }
        });


        final boolean finalIsUpdate = isUpdate;
        newEventSaveButton.setOnClickListener(new View.OnClickListener() {
            @RequiresApi(api = Build.VERSION_CODES.O)
            @Override
            public void onClick(View v) {
                String text = newEventText.getText().toString();
                String time = eventTimeTV.getText().toString();
                if(finalIsUpdate){
                    db.updateEvent(bundle.getInt("id"), text);
                    db.updateTime(bundle.getInt("id"), time);
                }
                else {
                    EventModel event = new EventModel();
                    event.setTime(time);
                    event.setDate(CalendarUtils.formattedDate(CalendarUtils.selectedDate));
                    event.setEvent(text);
                    String email;
                    mAuth = FirebaseAuth.getInstance();
                    user = mAuth.getCurrentUser();
                    GoogleSignInAccount account = GoogleSignIn.getLastSignedInAccount(getActivity());
                    if(account != null){
                        email = account.getEmail();
                    }else{
                        email = user.getEmail();
                    }
                    event.setUser(email);
                    db.insertEvent(event);
                }
                dismiss();
            }
        });
    }

    @SuppressLint("UseRequireInsteadOfGet")
    private void updateBtnStatus() {
        if(textIsValidated){
            newEventSaveButton.setEnabled(true);
            newEventSaveButton.setTextColor(ContextCompat.getColor(Objects.requireNonNull(getContext()), R.color.yellow_700));
        }else{
            newEventSaveButton.setEnabled(false);
            newEventSaveButton.setTextColor(Color.GRAY);
        }
    }

    @Override
    public void onDismiss(@NonNull DialogInterface dialog){
        Activity activity = getActivity();
        if(activity instanceof DialogCloseListener)
            ((DialogCloseListener)activity).handleDialogClose(dialog);
    }
}
