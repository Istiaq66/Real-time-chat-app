package com.istiaq66.chit.Fragments;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.istiaq66.chit.Adapter.ChatAdapter;
import com.istiaq66.chit.Models.User;
import com.istiaq66.chit.R;

import java.util.ArrayList;
import java.util.HashMap;

public class Chats extends Fragment {



    public Chats() {
        // Required empty public constructor
    }
    ArrayList<User> list = new ArrayList<>();
    FirebaseDatabase firebaseDatabase;
    RecyclerView recyclerView;
    DatabaseReference reference;
    FirebaseAuth auth;
    FirebaseUser firebaseUser;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View root = (ViewGroup) inflater.inflate(R.layout.fragment_chats,container,false);
        recyclerView = root.findViewById(R.id.Chatrecyclerview);


        ChatAdapter adapter = new ChatAdapter(list,getContext(),true);
        recyclerView.setAdapter(adapter);
        LinearLayoutManager layoutManager = new LinearLayoutManager(getContext());
        recyclerView.setLayoutManager(layoutManager);
        recyclerView.setHasFixedSize(true);

       firebaseDatabase =  FirebaseDatabase.getInstance();
       auth = FirebaseAuth.getInstance();
       firebaseUser = auth.getCurrentUser();
       firebaseDatabase.getReference().child("User").addValueEventListener(new ValueEventListener() {
           @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {

               list.clear();
                for (DataSnapshot dataSnapshot : snapshot.getChildren()){

                    User user = dataSnapshot.getValue(User.class);
                    user.setUserid(dataSnapshot.getKey());

                    if(!user.getUserid().equals(FirebaseAuth.getInstance().getUid())) {
                        list.add(user);
                   // recyclerView.smoothScrollToPosition(recyclerView.getAdapter().getItemCount());
                    }
                }
                adapter.notifyDataSetChanged();
            }

          @Override
            public void onCancelled(@NonNull  DatabaseError error) {

            }
        });

        return root;
    }


    private void CheckStatus(String status){

        reference = FirebaseDatabase.getInstance().getReference("User").child(firebaseUser.getUid());
        HashMap<String,Object> hashMap = new HashMap<>();
        hashMap.put("status",status);
        reference.updateChildren(hashMap);
    }

    @Override
    public void onResume() {
        super.onResume();
        CheckStatus("online");
    }

    @Override
    public void onPause() {
        super.onPause();
        CheckStatus("offline");
    }
}