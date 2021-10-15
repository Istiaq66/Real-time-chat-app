package com.istiaq66.chit;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.constraintlayout.solver.state.Reference;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.animation.ValueAnimator;
import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;
import com.istiaq66.chit.Adapter.Chatdetail_adpter;
import com.istiaq66.chit.Fragments.Status;
import com.istiaq66.chit.Models.MessageModel;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;

public class Chatdetail extends AppCompatActivity {

    FirebaseDatabase database;
    FirebaseAuth auth;
    TextView user_name,userstat;
    ImageView img,back,msgsend, Statuson,Statusoff;;
    RecyclerView RC;
    EditText msg;
    DatabaseReference reference,databaseReference;
    FirebaseUser firebaseUser;





    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_chatdetail);

        user_name = findViewById(R.id.chatusername);
        img = findViewById(R.id.profile_image);
        back = findViewById(R.id.Backarrow);
        RC = findViewById(R.id.ChatModelView);
        msgsend = findViewById(R.id.sendmsg);
        msg  = findViewById(R.id.textmsg);
        userstat =findViewById(R.id.Userstat);
        /*Statuson =findViewById(R.id.staton);
        Statuson =findViewById(R.id.statoff);*/



        getSupportActionBar().hide();


        database = FirebaseDatabase.getInstance();
        auth = FirebaseAuth.getInstance();
        firebaseUser = auth.getCurrentUser();
        final   String senderid = auth.getUid();
        String receiveid = getIntent().getStringExtra("userid");
        String userName = getIntent().getStringExtra("Username");
        String Profile_pic = getIntent().getStringExtra("Profilepic");
        databaseReference = database.getReference("User");


        user_name.setText(userName);
        Picasso.get().load(Profile_pic).placeholder(R.drawable.avatar).into(img);





        //---------User status//----------
/*        Query query = databaseReference.orderByChild("uid").equalTo(receiveid);
        query.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange( DataSnapshot dataSnapshot) {

                for (DataSnapshot ds: dataSnapshot.getChildren()){
                    String status = ""+ ds.child("status").getValue();
                    if (status.equals("online")) {
                        Statuson.setVisibility(View.VISIBLE);
                        Statusoff.setVisibility(View.GONE);
                    }
                    else{
                        Statuson.setVisibility(View.GONE);
                        Statusoff.setVisibility(View.VISIBLE);
                    }

                }

            }

            @Override
            public void onCancelled( DatabaseError error) {

            }
        });*/






        Query query = databaseReference.orderByChild("uid").equalTo(receiveid);
        query.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange( DataSnapshot dataSnapshot) {

                for (DataSnapshot ds: dataSnapshot.getChildren()){
                    String status = ""+ ds.child("status").getValue();
                    if (status.equals("online")) {
                       userstat.setText("Online");
                    }
                    else{
                       userstat.setText("Offline");
                    }

                }

            }

            @Override
            public void onCancelled( DatabaseError error) {

            }
        });









        //---------Back to mainacitvity-------//
        back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(Chatdetail.this,MainActivity.class);
                startActivity(intent);
            }
        });

        final ArrayList<MessageModel> messageModels = new ArrayList<>();

        final Chatdetail_adpter chatdetail_adapter = new Chatdetail_adpter(messageModels,this);

        RC.setAdapter(chatdetail_adapter);


        LinearLayoutManager layoutManager = new LinearLayoutManager(this);
        layoutManager.setStackFromEnd(true);
        RC.setLayoutManager(layoutManager);




        final  String senderRoom = senderid + receiveid;
        final  String receiverRoom = receiveid + senderid;

        database.getReference().child("Chats").child(senderRoom).addValueEventListener(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull  DataSnapshot snapshot) {

                       messageModels.clear();

                        for (DataSnapshot snapshot1 : snapshot.getChildren())
                        {
                            MessageModel model = snapshot1.getValue(MessageModel.class);
                              messageModels.add(model);

                            //scrolling msg to position
                            RC.smoothScrollToPosition(RC.getAdapter().getItemCount());
                        }

                        chatdetail_adapter.notifyDataSetChanged();

                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError error) {

                    }
                });


        //------Sending Message//----
        msgsend.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

              if(msg.getText().toString().isEmpty()){

                  msg.setError("Enter a message");
                  return;
              }


              String message = msg.getText().toString();

              final MessageModel model = new MessageModel(senderid,message);

              model.setTimstamp(new Date().getTime());
              msg.setText("");


                RecyclerView.AdapterDataObserver observer = new RecyclerView.AdapterDataObserver() {

                    @Override
                    public void onItemRangeInserted(int positionStart, int itemCount) {
                        super.onItemRangeInserted(positionStart, itemCount);

                        RC.smoothScrollToPosition(chatdetail_adapter.getItemCount());
                    }

                };
              database.getReference().child("Chats").child(senderRoom).push().setValue(model).addOnSuccessListener(new OnSuccessListener<Void>() {
                  @Override
                  public void onSuccess(Void unused) {

                      database.getReference().child("Chats").child(receiverRoom).push().setValue(model).addOnSuccessListener(new OnSuccessListener<Void>() {
                          @Override
                          public void onSuccess(Void unused) {


                          }
                      });
                  }
              });
            }
        });



        }



        //check user status//
        private void CheckStatus(String status){

        reference = FirebaseDatabase.getInstance().getReference("User").child(firebaseUser.getUid());
        HashMap<String,Object> hashMap = new HashMap<>();
        hashMap.put("status",status);
        reference.updateChildren(hashMap);
        }

    @Override
    protected void onResume() {
        super.onResume();
        CheckStatus("online");
    }

    @Override
    protected void onPause() {
        super.onPause();
        CheckStatus("offline");
    }
}


