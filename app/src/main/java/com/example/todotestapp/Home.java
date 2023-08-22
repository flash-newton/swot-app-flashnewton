package com.example.todotestapp;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.app.AppCompatDelegate;
import androidx.appcompat.widget.SwitchCompat;


import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.database.Cursor;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.CompoundButton;
import android.widget.ImageView;

import android.widget.TextView;

import com.example.todotestapp.classDB.DatabaseHandler;
import com.example.todotestapp.classDB.LogRegDB;
import com.google.android.gms.auth.api.signin.GoogleSignIn;
import com.google.android.gms.auth.api.signin.GoogleSignInAccount;
import com.google.android.gms.auth.api.signin.GoogleSignInClient;
import com.google.android.gms.auth.api.signin.GoogleSignInOptions;
import com.squareup.picasso.Picasso;



public class Home extends AppCompatActivity {

    private GoogleSignInOptions gso;
    private GoogleSignInClient gsc;

    private String user_email;
    SharedPreferences pref;
    ImageView strenbtn;
    ImageView weakbtn;
    ImageView opporbtn;
    ImageView threatbtn;
    Button Todo;

    private TextView stren_stat;
    private TextView weak_stat;
    private TextView oppo_stat;
    private TextView threat_stat;

    private TextView signoutbtn;
    private TextView name ;
    private Cursor cs;
    private LogRegDB setupProfile;
    private ImageView profile;
    private Uri uri;
    DatabaseHandler db;
    private SwitchCompat day_night;
    private Dialog dialog;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);
        getSupportActionBar().hide();

        pref = getSharedPreferences("com.example.todotestapp" , Context.MODE_PRIVATE);
        gso = new GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN).requestEmail().build();
        gsc = GoogleSignIn.getClient(this,gso);


        strenbtn = findViewById(R.id.home_stren_btn);
        weakbtn = findViewById(R.id.home_weakness_btn);
        opporbtn = findViewById(R.id.home_oppor_btn);
        threatbtn = findViewById(R.id.home_threat_btn);
        Todo = findViewById(R.id.cardView);

        signoutbtn = findViewById(R.id.signoutBtn);
        profile = findViewById(R.id.profileImg);
        name = findViewById(R.id.name);
        setupProfile = new LogRegDB(this);

        stren_stat=findViewById(R.id.stren_stat);
        weak_stat=findViewById(R.id.weak_stat);
        oppo_stat=findViewById(R.id.oppo_stat);
        threat_stat=findViewById(R.id.threat_stat);
        day_night = findViewById(R.id.daynightswitch);
        dialog = new Dialog(this);


        daynightcheck();

        // day night switch toggling
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



        // generating user details on activity creation
        // user details either from sqlite database or directly from google sign in
        user_email = pref.getString("loggedUser", null);
            cs = setupProfile.getUser(user_email);
            if (cs.getCount()>0){
                cs.moveToFirst();
                String usnme = cs.getString(0);
                String profilepic = cs.getString(3);
                if (profilepic != null) {
                    uri = Uri.parse(profilepic);
                    Picasso.get().load(uri).placeholder(R.drawable.placeholder_png).into(profile);
                }
                name.setText(usnme);

            }
            else {
                //google sign in details
                GoogleSignInAccount act = GoogleSignIn.getLastSignedInAccount(this);
                if (act !=null){
                    String usnme = act.getDisplayName();
                    uri = act.getPhotoUrl();
                    Picasso.get().load(uri).placeholder(R.drawable.placeholder_png).into(profile);
                    name.setText(usnme);
                }

            }


        // navigation section at home activity

        signoutbtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                signoutDialog();

            }
        });


        strenbtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent it = new Intent(Home.this,MainActivity.class);
                startActivity(it);
            }
        });

        weakbtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent it = new Intent(Home.this,Weakness.class);
                startActivity(it);
            }
        });

        opporbtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent it = new Intent(Home.this,Opportunity.class);
                startActivity(it);
            }
        });

        threatbtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent it = new Intent(Home.this,Threat.class);
                startActivity(it);
            }
        });

        Todo.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent it = new Intent(Home.this,Maintenance.class);
                startActivity(it);
            }
        });

        db =new DatabaseHandler(Home.this,"strength");
        db.openDatabase();
        getCounts();




    }


    //to keep SWOT comment counter up to date
    @Override
    protected void onResume() {
        super.onResume();
        getCounts();
    }


    // generating profile pic from user details
    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        try {
            uri = data.getData();
            profile.setImageURI(uri);
        }
        catch (Exception e){
         e.printStackTrace();
        }


    }

    // generating SWOT user counter
    public  void getCounts(){
        stren_stat.setText("Strengths : "+String.valueOf(db.getTableCount("strength",user_email)));
        weak_stat.setText("Weaknesses : "+String.valueOf(db.getTableCount("weakness",user_email)));
        oppo_stat.setText("Opportunities : "+String.valueOf(db.getTableCount("opportunity",user_email)));
        threat_stat.setText("Threats : "+String.valueOf(db.getTableCount("threat",user_email)));
    }

    //storing day night data
    public void preferencecaller(Boolean value){
        pref = getSharedPreferences("com.example.todotestapp" , Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = pref.edit();
        editor.putBoolean("time",value);
        editor.apply();
    }

    public void daynightcheck(){
        if (pref.getBoolean("time",false)){
            day_night.setChecked(true);
            AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_YES);
        }
        else{
            day_night.setChecked(false);
            AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_NO);
        }

    }

    //small transition effect for dark mode transition
    @Override
    public void recreate() {
        finish();
        overridePendingTransition(android.R.anim.fade_in, android.R.anim.fade_out);
        startActivity(getIntent());
        overridePendingTransition(android.R.anim.fade_in, android.R.anim.fade_out);

    }

    //custom signout dialog box code
    public void signoutDialog(){
        dialog.setContentView(R.layout.signout_dialog);
        dialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        Button yesBtn =dialog.findViewById(R.id.btnOK);
        Button noBtn=dialog.findViewById(R.id.btnCancel);
        dialog.show();
        yesBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                dialog.dismiss();
                GoogleSignInAccount act = GoogleSignIn.getLastSignedInAccount(Home.this);
                if (act !=null){
                    gsc.signOut();
                }
                SharedPreferences.Editor editor = pref.edit();
                editor.clear();
                editor.apply();
                Intent it = new Intent(Home.this,Login.class);
                startActivity(it);
                finish();
            }
        });
        noBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                dialog.dismiss();
            }
        });
    }



}