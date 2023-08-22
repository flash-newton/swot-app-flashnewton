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
import com.example.todotestapp.PopUps.AddPopUp4;
import com.example.todotestapp.Otherclass.ToDoModel;
import com.example.todotestapp.R;
import com.example.todotestapp.Threat;
import com.example.todotestapp.classDB.DatabaseHandler;

import java.util.List;

public class ToDoAdapter4 extends RecyclerView.Adapter<ToDoAdapter4.ViewHolder>{

    // Adapter for recycler view in Threat activity

    private List<ToDoModel> toDoModelList;
    private Threat activity;
    private DatabaseHandler db;

    public ToDoAdapter4(DatabaseHandler db, Threat activity){
        this.db=db;
        this.activity = activity;
    }

    public ToDoAdapter4.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType){
        View itemView = LayoutInflater.from(parent.getContext()).inflate(R.layout.listitems,parent,false);
        return new ToDoAdapter4.ViewHolder(itemView);
    }
    public void onBindViewHolder(ToDoAdapter4.ViewHolder holder, int position){
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
        fragment.show(activity.getSupportFragmentManager(), AddPopUp4.TAG);
    }

    public static class ViewHolder extends RecyclerView.ViewHolder{
        TextView listtext;
        ImageView icon;

        ViewHolder(View view){
            super(view);
            listtext = view.findViewById(R.id.listTextView);
            icon = view.findViewById(R.id.listicon);
            icon.setImageResource(R.drawable.threat_icon);
        }
    }
}
