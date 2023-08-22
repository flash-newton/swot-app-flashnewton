package com.example.todotestapp.Adapter;


import android.content.Context;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.recyclerview.widget.RecyclerView;

import com.example.todotestapp.PopUps.AddPopUp;
import com.example.todotestapp.MainActivity;
import com.example.todotestapp.Otherclass.ToDoModel;
import com.example.todotestapp.R;
import com.example.todotestapp.classDB.DatabaseHandler;

import java.util.List;

public class ToDoAdapter extends RecyclerView.Adapter<ToDoAdapter.ViewHolder> {

    // Adapter for recycler view in strengths(Main Activity) activity

    private List<ToDoModel> toDoModelList;
    private MainActivity activity;
    private DatabaseHandler db;

    public ToDoAdapter(DatabaseHandler db,MainActivity activity){
        this.db=db;
        this.activity = activity;
    }

    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType){
        View itemView = LayoutInflater.from(parent.getContext()).inflate(R.layout.listitems,parent,false);
        return new ViewHolder(itemView);
    }
    public void onBindViewHolder(ViewHolder holder, int position){
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
        fragment.show(activity.getSupportFragmentManager(),AddPopUp.TAG);
    }

    public static class ViewHolder extends RecyclerView.ViewHolder{
        TextView listtext;

        ViewHolder(View view){
            super(view);
            listtext = view.findViewById(R.id.listTextView);
        }
    }
}
