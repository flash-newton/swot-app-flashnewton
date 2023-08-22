package com.example.todotestapp.TouchHelpers;

import android.content.DialogInterface;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.graphics.drawable.Drawable;
import android.view.View;

import androidx.appcompat.app.AlertDialog;
import androidx.core.content.ContextCompat;
import androidx.recyclerview.widget.ItemTouchHelper;
import androidx.recyclerview.widget.RecyclerView;

import com.example.todotestapp.Adapter.ToDoAdapter2;
import com.example.todotestapp.Adapter.ToDoAdapter4;
import com.example.todotestapp.R;
import com.example.todotestapp.Threat;

public class RecyclerTouchHelper4 extends ItemTouchHelper.SimpleCallback {

    //to enable edit and delete options in the recycler list in Threat Activity

    private ToDoAdapter4 adapter;
    private Threat activity;

    public RecyclerTouchHelper4(ToDoAdapter4 adapter, Threat activity){
        super(0,ItemTouchHelper.LEFT | ItemTouchHelper.RIGHT);
        this.adapter = adapter;
        this.activity=activity;
    }
    @Override
    public boolean onMove(RecyclerView recyclerView, RecyclerView.ViewHolder viewHolder, RecyclerView.ViewHolder target){
        return false;
    }

    @Override
    public void onSwiped(final RecyclerView.ViewHolder viewHolder, int direction){
        final int position = viewHolder.getAbsoluteAdapterPosition();
        if (direction == ItemTouchHelper.LEFT){
            AlertDialog.Builder builder = new AlertDialog.Builder(adapter.getContext());
            builder.setTitle("Delete Statement");
            builder.setMessage("Do you want to delete this statement ?");
            builder.setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialogInterface, int i) {
                    adapter.deleteItem(position);
                    activity.showCount();
                }
            });
            builder.setNegativeButton(android.R.string.cancel, new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialogInterface, int i) {
                    adapter.notifyItemChanged(viewHolder.getAbsoluteAdapterPosition());
                }
            });
            AlertDialog dialog = builder.create();
            dialog.show();
        }
        else{
            adapter.editItem(position);
        }

    }

    @Override
    public void onChildDraw(Canvas c, RecyclerView recyclerView, RecyclerView.ViewHolder viewHolder, float dx, float dy, int actionstate, boolean currentlyActive){
        super.onChildDraw(c,recyclerView,viewHolder,dx,dy,actionstate, currentlyActive);

        Drawable icon;
        ColorDrawable bg;

        View itemView = viewHolder.itemView;
        int bgcornerOffset = 20;

        if(dx>0){
            icon = ContextCompat.getDrawable(adapter.getContext(), R.drawable.edit_icon);
            bg = new ColorDrawable(ContextCompat.getColor(adapter.getContext(), R.color.threatswipe));
        }
        else{
            icon = ContextCompat.getDrawable(adapter.getContext(),R.drawable.delete_icon);
            bg = new ColorDrawable(ContextCompat.getColor(adapter.getContext(), R.color.deleteswipe));
        }

        int iconMargin = (itemView.getHeight() - icon.getIntrinsicHeight())/2;
        int iconTop = itemView.getTop() + (itemView.getHeight()-icon.getIntrinsicHeight()) /2;
        int iconBottom = iconTop + icon.getIntrinsicHeight();

        if (dx>0){
            int iconLeft = itemView.getLeft() + iconMargin;
            int iconRight = itemView.getLeft() + iconMargin + icon.getIntrinsicWidth();
            icon.setBounds(iconLeft, iconTop, iconRight, iconBottom);

            bg.setBounds(itemView.getLeft(), itemView.getTop(), itemView.getLeft() + ((int)dx)+bgcornerOffset,itemView.getBottom());
        }
        else if(dx<0){
            int iconLeft = itemView.getRight() - iconMargin - icon.getIntrinsicWidth();
            int iconRight = itemView.getRight() - iconMargin;
            icon.setBounds(iconLeft, iconTop, iconRight, iconBottom);

            bg.setBounds(itemView.getRight()+((int)dx)-bgcornerOffset, itemView.getTop(),itemView.getRight(),itemView.getBottom());
        }
        else{
            bg.setBounds(0,0,0,0);
        }
        bg.draw(c);
        icon.draw(c);
    }

}
