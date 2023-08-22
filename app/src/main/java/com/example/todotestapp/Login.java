package com.example.todotestapp;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.app.AppCompatDelegate;
import androidx.appcompat.widget.SwitchCompat;

import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.Switch;
import android.widget.TextView;
import android.widget.Toast;

import com.example.todotestapp.classDB.LogRegDB;
import com.google.android.gms.auth.api.signin.GoogleSignIn;
import com.google.android.gms.auth.api.signin.GoogleSignInAccount;
import com.google.android.gms.auth.api.signin.GoogleSignInClient;
import com.google.android.gms.auth.api.signin.GoogleSignInOptions;
import com.google.android.gms.common.api.ApiException;
import com.google.android.gms.tasks.Task;

public class Login extends AppCompatActivity {


    private SharedPreferences pref;
    private TextView registerbtn ;
    private Button loginbtn;
    private EditText user;
    private EditText pwd;
    private LogRegDB logDB;
    private TextView googleSignInbtn;
    private SwitchCompat day_night;

    private GoogleSignInOptions gso;
    private GoogleSignInClient gsc;
    private Dialog dialog;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        getSupportActionBar().hide();

        pref = getSharedPreferences("com.example.todotestapp" ,Context.MODE_PRIVATE);
        day_night = findViewById(R.id.daynightswitch);
        registerbtn = findViewById(R.id.btnsignup);
        loginbtn = findViewById(R.id.loginbtn);
        user = findViewById(R.id.loginuser);
        pwd=findViewById(R.id.loginpwd);
        logDB = new LogRegDB(this);
        dialog =new Dialog(this);

        //if already logged in, will be redirect to home activity
        String loggeduser = pref.getString("loggedUser",null);
        if(loggeduser!=null){
            goHome();
        }


        daynightcheck();


        //day night switch toggling
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



        //standard login button onclick coding
        loginbtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                String u = user.getText().toString();
                String pw = pwd.getText().toString();
                if (u.equals("")||pw.equals("")){
                    showToast("Please enter both username and password to login");
                }
                else {
                    String result = logDB.checkLog(u,pw);
                    if (result=="nouser"){
                        showToast("Username or Password is incorrect\nPlease try again");
                    }
                    else{
                        showToast("Login Successful");
                        SharedPreferences.Editor editor =pref.edit();
                        editor.putString("loggedUser",result);
                        editor.putString("Method","1");
                        editor.apply();
                        goHome();
                    }
                }
            }
        });

        // Google sign iniiation on button click
        gso = new GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN).requestEmail().build();
        gsc = GoogleSignIn.getClient(this,gso);
        googleSignInbtn = findViewById(R.id.googleSignin);

        googleSignInbtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                GsignIn();
            }
        });


        //go to register
        registerbtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
                Intent int1 = new Intent(Login.this,Register.class);
                startActivity(int1);
            }
        });
    }

    //go to home activity
    public void goHome(){
        finish();
        final Intent int2 = new Intent(Login.this,Home.class);
        startActivity(int2);

    }

    public void GsignIn(){
        Intent it = gsc.getSignInIntent();
        startActivityForResult(it,69);
    }

    //attempting to login through google
    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if(requestCode==69){
            Task<GoogleSignInAccount> task = GoogleSignIn.getSignedInAccountFromIntent(data);

            try {
                task.getResult(ApiException.class);
                GoogleSignInAccount act = GoogleSignIn.getLastSignedInAccount(this);
                String email = act.getEmail();
                SharedPreferences.Editor editor =pref.edit();
                editor.putString("loggedUser",email);
                editor.putString("Method","google");
                editor.apply();
                goHome();


            } catch (ApiException e) {
                noInternetDialog();
            }
        }
    }

    //storing day night data
    public void preferencecaller(Boolean value){
        pref = getSharedPreferences("com.example.todotestapp" , Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = pref.edit();
        editor.putBoolean("time",value);
        editor.apply();
    }

    //generating custom fallback dialog for no internet
    public void noInternetDialog(){
        dialog.setContentView(R.layout.dialog_alert);
        dialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        Button okbtn =dialog.findViewById(R.id.btnOK);
        dialog.show();
        okbtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                dialog.dismiss();
            }
        });

    }

    public void daynightcheck(){
        if (pref.getBoolean("time",false)==true){
            day_night.setChecked(true);
            AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_YES);
        }
        else{
            day_night.setChecked(false);
            AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_NO);
        }

    }


    //generating custom toast messages
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

    // day night mode smooth transition
    @Override
    public void recreate() {
        finish();
        overridePendingTransition(android.R.anim.fade_in, android.R.anim.fade_out);
        startActivity(getIntent());
        overridePendingTransition(android.R.anim.fade_in, android.R.anim.fade_out);

    }

}