package com.istiaq66.chit.Adapter;

import android.content.Context;
import android.content.Intent;
import android.media.Image;
import android.provider.ContactsContract;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.istiaq66.chit.Chatdetail;
import com.istiaq66.chit.Models.User;
import com.istiaq66.chit.R;
import com.squareup.picasso.Picasso;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;

public class ChatAdapter extends RecyclerView.Adapter<ChatAdapter.ViewHolder>{
    ArrayList<User> list;
    Context context;
    private  boolean isChat;

    public ChatAdapter(ArrayList<User> list, Context context, boolean isChat) {
        this.list = list;
        this.context = context;
        this.isChat = isChat;
    }

    @NonNull

    @Override
    public ViewHolder onCreateViewHolder(@NonNull  ViewGroup parent, int viewType) {

        View view = LayoutInflater.from(context).inflate(R.layout.sample_show_user,parent,false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull  ChatAdapter.ViewHolder holder, int position) {

        User user = list.get(position);



        Picasso.get().load(user.getProfilepic()).placeholder(R.drawable.avatar).into(holder.image);
        holder.username.setText(user.getUsername());

        FirebaseDatabase.getInstance().getReference().child("Chats")
                 .child(FirebaseAuth.getInstance().getUid() + user.getUserid())
                .orderByChild("timstamp").limitToLast(1).addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull  DataSnapshot snapshot) {

                if(snapshot.hasChildren()){

                    for(DataSnapshot snapshot1 : snapshot.getChildren()){

                        holder.lastmsg.setText(snapshot1.child("message").getValue().toString());

                      //  holder.time.setText(snapshot1.child("timstamp").getValue().toString());

                        SimpleDateFormat formatter = new SimpleDateFormat("hh:mm a");

                        String dateString = formatter.format(new Date(Long.parseLong(snapshot1.child("timstamp").getValue().toString())));
                        holder.time.setText(dateString);
                    }
                }
            }

            @Override
            public void onCancelled(@NonNull  DatabaseError error) {

            }
        });


        //user status checker//

        if(isChat){
            if(user.getStatus().equals("online")){
                holder.Statuson.setVisibility(View.VISIBLE);
                holder.Statusoff.setVisibility(View.GONE);
            }
            else{
                holder.Statuson.setVisibility(View.GONE);
                holder.Statusoff.setVisibility(View.VISIBLE);
            }
        }
        else{
            holder.Statuson.setVisibility(View.GONE);
            holder.Statusoff.setVisibility(View.GONE);
        }


        //Passing data from one activity to another
        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(context, Chatdetail.class);
                intent.putExtra("userid", user.getUserid());
                intent.putExtra("Profilepic",user.getProfilepic());
                intent.putExtra("Username",user.getUsername());
                context.startActivity(intent);

            }
        });
    }

    @Override
    public int getItemCount() {
        return list.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {

        ImageView image,Statuson,Statusoff;
        TextView username,lastmsg,time;

        public ViewHolder(@NonNull  View itemView) {
            super(itemView);
            image = itemView.findViewById(R.id.imageview);
            username = itemView.findViewById(R.id.textViewTitle);
            lastmsg = itemView.findViewById(R.id.lastmessage);
            time = itemView.findViewById(R.id.txttime);
            Statuson = itemView.findViewById(R.id.statON);
            Statusoff = itemView.findViewById(R.id.statOFF);

        }
    }
}
