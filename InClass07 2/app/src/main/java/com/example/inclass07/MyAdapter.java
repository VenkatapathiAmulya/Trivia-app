package com.example.inclass07;


import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.LinearLayout;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;

public class MyAdapter extends RecyclerView.Adapter<MyAdapter.ViewHolder> {


    Context contextIdentifier;
    public static InteractWithTriviaActivity interact;
    int quesNo;

    ArrayList<String> choiceList =new ArrayList<>();

    public MyAdapter(ArrayList<String> choiceList,Context QuestionsActivity,int quesNo) {
        this.choiceList = choiceList;
        this.contextIdentifier=QuestionsActivity;
        this.quesNo=quesNo;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LinearLayout rv_layout = (LinearLayout) LayoutInflater.from(parent.getContext()).inflate(R.layout.row_layout, parent, false);
        ViewHolder viewHolder = new ViewHolder(rv_layout);
        return viewHolder;
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, final int position) {

        interact =(InteractWithTriviaActivity)contextIdentifier;

        holder.btnChoice.setText(choiceList.get(position));
        holder.btnChoice.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Log.d("demo","Selected "+position +" "+ quesNo);
                interact.selectChoice(position,quesNo);

            }
        });
    }

    @Override
    public int getItemCount() {
        return choiceList.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {

        Button btnChoice;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);

            btnChoice=itemView.findViewById(R.id.choiceButton);

        }
    }

    public interface InteractWithTriviaActivity
    {
        void selectChoice(int position,int quesNo);
    }
}
