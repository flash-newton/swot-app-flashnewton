package com.example.todotestapp;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.ItemTouchHelper;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.todotestapp.Adapter.ToDoAdapter;
import com.example.todotestapp.Otherclass.ToDoModel;
import com.example.todotestapp.PopUps.AddPopUp;
import com.example.todotestapp.TouchHelpers.RecyclerTouchHelper;
import com.example.todotestapp.classDB.DatabaseHandler;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class MainActivity extends AppCompatActivity implements DialogCloseListener {

    SharedPreferences pref;
    private static final String type = "strength";
    ImageView h_btn;
    TextView w_btn;
    TextView o_btn;
    TextView th_btn;
    ImageView todo_btn;
    String user;
    TextView count;

    private RecyclerView recycleview ;
    private ToDoAdapter listAdapter;
    private Button adder;


    private List<ToDoModel> tasklist;
    private DatabaseHandler db;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        getSupportActionBar().hide();

        pref= getSharedPreferences("com.example.todotestapp" , Context.MODE_PRIVATE);
        user = pref.getString("loggedUser",null);

        h_btn=findViewById(R.id.homenav);
        w_btn=findViewById(R.id.weaknav);
        o_btn=findViewById(R.id.oppornav);
        th_btn=findViewById(R.id.thnav);
        todo_btn=findViewById(R.id.todonav);
        count = findViewById(R.id.countView);

        //navigation at Strength activity

        h_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });

        w_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent it = new Intent(MainActivity.this,Weakness.class);
                startActivity(it);
                finish();
            }
        });

        o_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent it = new Intent(MainActivity.this,Opportunity.class);
                startActivity(it);
                finish();
            }
        });

        th_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent it = new Intent(MainActivity.this,Threat.class);
                startActivity(it);
                finish();
            }
        });
        todo_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent it = new Intent(MainActivity.this,Maintenance.class);
                startActivity(it);
                finish();
            }
        });

        //inserting data into activity from database

        db = new DatabaseHandler(this,type);
        db.openDatabase();
        tasklist = new ArrayList<>();
        recycleview = findViewById(R.id.recyclerView);
        recycleview.setLayoutManager(new LinearLayoutManager(this));
        listAdapter = new ToDoAdapter(db,this);
        recycleview.setAdapter(listAdapter);

        adder = findViewById(R.id.adder);

        ItemTouchHelper itemTouchHelper = new ItemTouchHelper(new RecyclerTouchHelper(listAdapter,this));
        itemTouchHelper.attachToRecyclerView(recycleview);

        tasklist = db.getALLTasks(user);
        Collections.reverse(tasklist);
        listAdapter.setTasks(tasklist);
        showCount();

        adder.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                AddPopUp.newInstance().show(getSupportFragmentManager(),AddPopUp.TAG);
            }
        });

    }


    //to carry out realtime updates on the recycler list
    @Override
    public void handleDialogClose(DialogInterface dialog){
        tasklist = db.getALLTasks(user);
        Collections.reverse(tasklist);
        listAdapter.setTasks(tasklist);
        listAdapter.notifyDataSetChanged();
        showCount();
    }

    // to show the number of total strengths of user
    public void showCount(){
        count.setText(String.valueOf(db.getTableCount("strength",user)));
    }
}