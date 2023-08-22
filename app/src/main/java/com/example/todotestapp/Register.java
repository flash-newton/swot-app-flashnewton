package com.example.todotestapp;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.app.AppCompatDelegate;
import androidx.appcompat.widget.SwitchCompat;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.net.Uri;
import android.os.Bundle;
import android.util.Patterns;
import android.view.Gravity;
import android.view.InflateException;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Switch;
import android.widget.TextView;
import android.widget.Toast;

import com.example.todotestapp.classDB.LogRegDB;
import com.github.dhaval2404.imagepicker.ImagePicker;

import java.util.zip.Inflater;

public class Register extends AppCompatActivity {

    private SharedPreferences pref;
    private EditText username;
    private EditText email;
    private EditText password;
    private EditText password2;
    private TextView backbtn;
    private ImageView btnReg;
    private ImageView pPic;
    private  String profilePic;
    private SwitchCompat day_night;

    LogRegDB logDB;
    Uri uri;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);
        getSupportActionBar().hide();

        pref = getSharedPreferences("com.example.todotestapp" , Context.MODE_PRIVATE);
        day_night = findViewById(R.id.daynightswitch);
        username = findViewById(R.id.regUsername);
        email = findViewById(R.id.regEmail);
        password = findViewById(R.id.regPassword);
        password2 = findViewById(R.id.regPassword2);
        btnReg = findViewById(R.id.regConfirmUserbtn);
        backbtn =findViewById(R.id.regbackbtn);
        pPic = (ImageView) findViewById(R.id.regProfile);
        profilePic = null;


        // Checking whether Daynight switch needs to be turned ON during OnCreate
        if (pref.getBoolean("time",false)==true){
            day_night.setChecked(true);
            AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_YES);
        }
        else{
            day_night.setChecked(false);
            AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_NO);
        }


        logDB = new LogRegDB(this);

        //navigation section
        backbtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
                Intent it = new Intent(Register.this,Login.class);
                startActivity(it);
            }
        });

        //register button functions
        btnReg.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                String user = username.getText().toString();
                String useremail = email.getText().toString();
                String userpw = password.getText().toString();
                String userpw2 = password2.getText().toString();

                if (user.equals("") || useremail.equals("")||userpw.equals("")||userpw2.equals("")){
                    showToast("Please fill all the entries in the form");
                }
                else{
                    Boolean validresult =validateUser(user,useremail,userpw,userpw2);
                    if (validresult==true){
                        Boolean regResult = logDB.insertUser(user,useremail,userpw,profilePic);
                        if (regResult){
                            showToast("Registration Successful");
                            finish();
                            Intent it = new Intent(Register.this,Login.class);
                            startActivity(it);
                        }
                        else{
                            showToast("Registration failed\nPlease try again");
                        }
                    }

                }
            }
        });


        //Add a profile pic feature
        pPic.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                ImagePicker.with(Register.this)
                        .cropSquare()	    			//Crop image(Optional), Check Customization for more option
                        .compress(1024)			//Final image size will be less than 1 MB(Optional)
                        .maxResultSize(1080, 1080)	//Final image resolution will be less than 1080 x 1080(Optional)
                        .start();
            }
        });
        if(savedInstanceState != null){
            uri = savedInstanceState.getParcelable("savedImage");
            if (uri!=null){
                pPic.setImageURI(uri);
                profilePic = uri.toString();
            }


        }

        // Day night switch coding
        day_night.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton compoundButton, boolean checked) {
                if(checked){
                    AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_YES);
                    preferencecaller(true);
                }
                else{
                    AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_NO);
                    preferencecaller(false);
                }
            }
        });




    }

    //user validation process
    public Boolean validateUser(String user,String useremail, String userpw,String userpw2){
        Boolean result = logDB.checkReg(user);
        if (result==true){
            showToast("Username already taken\nPlease use another name");
            return false;
        }
        else{
            if (!Patterns.EMAIL_ADDRESS.matcher(useremail).matches()){
                showToast("Please enter a proper email");
                return false;
            }
            else{
                if(!userpw.equals(userpw2)){
                    showToast("Passwords do not match");
                    return false;
                }
                else{
                    return true;
                }
            }
        }
    }


    //Making my custom toast
    public void showToast(String text){
        LayoutInflater inflater = getLayoutInflater();
        View layout = inflater.inflate(R.layout.toast,(ViewGroup)findViewById(R.id.toast_body));
        Toast toast = new Toast(getApplicationContext());
        TextView toast_text = layout.findViewById(R.id.toast_text);
        toast_text.setText(text);
        toast.setGravity(Gravity.TOP,0,25);
        toast.setDuration(Toast.LENGTH_SHORT);
        toast.setView(layout);
        toast.show();

    }

    //Add a profile feature- image generation
    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        try {
            uri = data.getData();
            pPic.setImageURI(uri);
            profilePic = uri.toString();
        }
        catch (Exception e){
            showToast("Profile picture not selected");
        }



    }

    @Override
    protected void onSaveInstanceState(@NonNull Bundle outState) {
        super.onSaveInstanceState(outState);
        outState.putParcelable("savedImage",uri);
    }

    //extra data storing for day night switch
    public void preferencecaller(Boolean value){
        pref = getSharedPreferences("com.example.todotestapp" , Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = pref.edit();
        editor.putBoolean("time",value);
        editor.apply();
    }


    // tranistion coding for smooth day night toggling
    @Override
    public void recreate() {
        finish();
        overridePendingTransition(android.R.anim.fade_in, android.R.anim.fade_out);
        startActivity(getIntent());
        overridePendingTransition(android.R.anim.fade_in, android.R.anim.fade_out);

    }

}