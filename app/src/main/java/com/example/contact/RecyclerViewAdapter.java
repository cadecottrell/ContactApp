package com.example.contact;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;

// Written by Cade Cottrell for CS4301.002, Contact Phase 1 Assignment
// netid: cac160030

//RecyclerView creation

public class RecyclerViewAdapter extends RecyclerView.Adapter<RecyclerViewAdapter.ViewHolder> {

    private ArrayList<ContactObject> textViewItems;
    Context mContext;

    // Constructor
    public RecyclerViewAdapter(ArrayList<ContactObject> textViewItems, Context context) {
        this.textViewItems = textViewItems;
        mContext = context;
    }


    // Create viewholder
    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.layout_listitem, parent, false);
        ViewHolder holder = new ViewHolder(view);
        return holder;
    }

    //Set up the viewholder, by adding string to the textviews
    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, final int position) {

        holder.lastName.setText(textViewItems.get(position).getLastName());
        holder.firstName.setText(textViewItems.get(position).getFirstName());
        holder.phone.setText(textViewItems.get(position).getPhone());
        holder.birthDate.setText(textViewItems.get(position).getBirthDate());
        holder.dayMet.setText(textViewItems.get(position).getDateMet());


        holder.relativeLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(mContext, Contact.class);

                String contact = textViewItems.get(position).getLastName() + ":" +
                                 textViewItems.get(position).getFirstName() + ":" +
                                 textViewItems.get(position).getPhone() + ":" +
                                 textViewItems.get(position).getBirthDate() + ":" +
                                 textViewItems.get(position).getDateMet();


                ArrayList<String> extra = new ArrayList<>();
                extra.add("Modify");
                extra.add(contact);
                extra.add(position + "");
                intent.putStringArrayListExtra("addOrModify", extra);

                ((MainMenu)mContext).startActivityForResult(intent, 2);
            }
        });

    }

    @Override
    public int getItemCount() {
        return textViewItems.size();
    }


    // Our viewholder it holds the layout, and the textviews to display stock data
    // this is our "row" in the recyclerview
    public class ViewHolder extends RecyclerView.ViewHolder{

        LinearLayout relativeLayout;
        TextView lastName;
        TextView firstName;
        TextView phone;
        TextView birthDate;
        TextView dayMet;



        public ViewHolder(@NonNull View itemView) {
            super(itemView);

            relativeLayout = itemView.findViewById(R.id.parent_layout);
            lastName = itemView.findViewById(R.id.LastNameLabel);
            firstName = itemView.findViewById(R.id.FirstNameLabel);
            phone = itemView.findViewById(R.id.PhoneLabel);
            birthDate = itemView.findViewById(R.id.BirthLabel);
            dayMet = itemView.findViewById(R.id.MetLabel);
        }
    }
}
