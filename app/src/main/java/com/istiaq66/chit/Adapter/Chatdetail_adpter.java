package com.istiaq66.chit.Adapter;

import android.content.Context;
import android.text.format.DateFormat;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.recyclerview.widget.RecyclerView;

import com.google.firebase.auth.FirebaseAuth;
import com.istiaq66.chit.Models.MessageModel;
import com.istiaq66.chit.Models.User;
import com.istiaq66.chit.R;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.Locale;

import static android.media.CamcorderProfile.get;

public class Chatdetail_adpter extends  RecyclerView.Adapter{

    ArrayList<MessageModel> messageModels;
    Context context;


    int SENDER_VIEW_TYPE = 1;
    int RECEIVER_VIEW_TYPE = 2;


    public Chatdetail_adpter(ArrayList<MessageModel> messageModels, Context context) {
        this.messageModels = messageModels;
        this.context = context;

    }




    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(@NonNull  ViewGroup parent, int viewType) {

        if(viewType==SENDER_VIEW_TYPE)
        {
            View view = LayoutInflater.from(context).inflate(R.layout.sample_sender,parent,false);
             return  new SenderViewholder(view);
        }
        else{
            View view = LayoutInflater.from(context).inflate(R.layout.sample_receiver,parent,false);
            return  new Receiverviewholder(view);

        }

    }

    @Override
    public int getItemViewType(int position) {
        if(messageModels.get(position).getUid().equals(FirebaseAuth.getInstance().getUid()))
        {
            return  SENDER_VIEW_TYPE;

        }
        else
        {
            return RECEIVER_VIEW_TYPE;
        }

    }

    @Override
    public void onBindViewHolder(@NonNull  RecyclerView.ViewHolder holder, int position) {

        MessageModel messageModel = messageModels.get(position);


        if(holder.getClass() == SenderViewholder.class)
        {
            ((SenderViewholder)holder).sendermsg.setText(messageModel.getMessage());
            Calendar cal = Calendar.getInstance(Locale.ENGLISH);
            cal.setTimeInMillis(messageModel.getTimstamp());
            String date = DateFormat.format("hh:mm a", cal).toString();
            ((SenderViewholder)holder).sendertime.setText(date);
        }
        else{
            ((Receiverviewholder)holder).receivermsg.setText(messageModel.getMessage());
            Calendar cal = Calendar.getInstance(Locale.ENGLISH);
            cal.setTimeInMillis(messageModel.getTimstamp());
            String date = DateFormat.format("hh:mm a", cal).toString();
            ((Receiverviewholder)holder).receivetime.setText(date);
        }


        //user status checker//


    }

    @Override
    public int getItemCount() {
        return messageModels.size();
    }

    public class Receiverviewholder extends RecyclerView.ViewHolder{

        TextView receivermsg , receivetime;

        public Receiverviewholder(@NonNull  View itemView) {
            super(itemView);
            receivermsg = itemView.findViewById(R.id.receivertxt);
            receivetime = itemView.findViewById(R.id.receivertime);


        }
    }
    public class SenderViewholder extends RecyclerView.ViewHolder {

        TextView sendermsg , sendertime;
        public SenderViewholder(@NonNull  View itemView) {
            super(itemView);
            sendermsg = itemView.findViewById(R.id.sendertxt);
            sendertime = itemView.findViewById(R.id.sendertime);


        }
    }


}
