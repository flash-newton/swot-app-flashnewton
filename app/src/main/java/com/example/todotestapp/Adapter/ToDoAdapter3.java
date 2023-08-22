package com.example.todotestapp.Adapter;

import android.content.Context;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.recyclerview.widget.RecyclerView;

import com.example.todotestapp.PopUps.AddPopUp;
import com.example.todotestapp.PopUps.AddPopUp3;
import com.example.todotestapp.Opportunity;
import com.example.todotestapp.Otherclass.ToDoModel;
import com.example.todotestapp.R;
import com.example.todotestapp.classDB.DatabaseHandler;

import java.util.List;

public class ToDoAdapter3 extends RecyclerView.Adapter<ToDoAdapter3.ViewHolder>{

    // Adapter for recycler view in Opportunity activity

    private List<ToDoModel> toDoModelList;
    private Opportunity activity;
    private DatabaseHandler db;

    public ToDoAdapter3(DatabaseHandler db, Opportunity activity){
        this.db=db;
        this.activity = activity;
    }

    public ToDoAdapter3.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType){
        View itemView = LayoutInflater.from(parent.getContext()).inflate(R.layout.listitems,parent,false);
        return new ToDoAdapter3.ViewHolder(itemView);
    }
    public void onBindViewHolder(ToDoAdapter3.ViewHolder holder, int position){
        db.openDatabase();
        ToDoModel item = toDoModelList.get(position);
        holder.listtext.setText(item.getTask());
    }

    public int getItemCount() {
        return toDoModelList.size();
    }

    public void setTasks(List<ToDoModel> toDoModelList){
        this.toDoModelList = toDoModelList;
        notifyDataSetChanged();
    }
    public Context getContext(){
        return activity;
    }

    public void deleteItem(int position){
        ToDoModel item = toDoModelList.get(position);
        db.deleteTask(item.getId());
        toDoModelList.remove(position);
        notifyItemRemoved(position);
    }

    public void editItem(int position){
        ToDoModel item = toDoModelList.get(position);
        Bundle bundle = new Bundle();
        bundle.putInt("id",item.getId());
        bundle.putString("task",item.getTask());
        AddPopUp fragment = new AddPopUp();
        fragment.setArguments(bundle);
        fragment.show(activity.getSupportFragmentManager(), AddPopUp3.TAG);
    }

    public static class ViewHolder extends RecyclerView.ViewHolder{
        TextView listtext;
        ImageView icon;

        ViewHolder(View view){
            super(view);
            listtext = view.findViewById(R.id.listTextView);
            icon = view.findViewById(R.id.listicon);
            icon.setImageResource(R.drawable.oppor_icon);
        }
    }
}
