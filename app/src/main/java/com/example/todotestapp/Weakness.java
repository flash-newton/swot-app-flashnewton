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

import com.example.todotestapp.Adapter.ToDoAdapter2;
import com.example.todotestapp.Otherclass.ToDoModel;
import com.example.todotestapp.PopUps.AddPopUp2;
import com.example.todotestapp.TouchHelpers.RecyclerTouchHelper2;
import com.example.todotestapp.classDB.DatabaseHandler;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class Weakness extends AppCompatActivity implements DialogCloseListener {

    SharedPreferences pref;
    private static final String type = "weakness";
    ImageView h_btn;
    TextView s_btn;
    TextView o_btn;
    TextView th_btn;
    ImageView todo_btn;
    String user;
    TextView count;

    private RecyclerView recycleview ;
    private ToDoAdapter2 listAdapter;
    private Button adder;


    private List<ToDoModel> tasklist;
    private DatabaseHandler db;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_weakness);
        getSupportActionBar().hide();

        pref= getSharedPreferences("com.example.todotestapp" , Context.MODE_PRIVATE);
        user = pref.getString("loggedUser",null);


        h_btn=findViewById(R.id.homenav);
        s_btn=findViewById(R.id.strengthnav);
        o_btn=findViewById(R.id.oppornav);
        th_btn=findViewById(R.id.thnav);
        todo_btn=findViewById(R.id.todonav);
        count = findViewById(R.id.countView);

        h_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });

        s_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent it = new Intent(Weakness.this,MainActivity.class);
                startActivity(it);
                finish();
            }
        });

        o_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent it = new Intent(Weakness.this,Opportunity.class);
                startActivity(it);
                finish();
            }
        });

        th_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent it = new Intent(Weakness.this,Threat.class);
                startActivity(it);
                finish();
            }
        });

        todo_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent it = new Intent(Weakness.this,Maintenance.class);
                startActivity(it);
                finish();
            }
        });

        db = new DatabaseHandler(this,type);
        db.openDatabase();
        tasklist = new ArrayList<>();
        recycleview = findViewById(R.id.recyclerView);
        recycleview.setLayoutManager(new LinearLayoutManager(this));
        listAdapter = new ToDoAdapter2(db,this);
        recycleview.setAdapter(listAdapter);


        adder = findViewById(R.id.adder);

        ItemTouchHelper itemTouchHelper = new ItemTouchHelper(new RecyclerTouchHelper2(listAdapter,this));
        itemTouchHelper.attachToRecyclerView(recycleview);

        tasklist = db.getALLTasks(user);
        Collections.reverse(tasklist);
        listAdapter.setTasks(tasklist);

        showCount();

        adder.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                AddPopUp2.newInstance().show(getSupportFragmentManager(),AddPopUp2.TAG);
            }
        });


    }

    @Override
    public void handleDialogClose(DialogInterface dialog){
        tasklist = db.getALLTasks(user);
        Collections.reverse(tasklist);
        listAdapter.setTasks(tasklist);
        listAdapter.notifyDataSetChanged();
        showCount();
    }


    public void showCount(){
        count.setText(String.valueOf(db.getTableCount("weakness",user)));
    }
}