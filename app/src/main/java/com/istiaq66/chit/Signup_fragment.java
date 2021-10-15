package com.istiaq66.chit;

import android.app.ProgressDialog;
import android.content.Intent;
import android.net.ConnectivityManager;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.istiaq66.chit.Models.User;

import java.util.HashMap;


public class Signup_fragment extends Fragment {

  private FirebaseAuth auth;
  private DatabaseReference database;
  FirebaseDatabase firebaseDatabase;
  ProgressDialog progressDialog;

  EditText ed1, ed2 , ed3;
  Button btn;
  Boolean isConnected = false;
  ConnectivityManager connectivityManager;

    @Override
    public View onCreateView(LayoutInflater inflator, ViewGroup container, Bundle saveInstanceState) {

        View root = (ViewGroup) inflator.inflate(R.layout.signup_fragment,container,false);

        if(isConnected)
        {
            Toast.makeText(getActivity().getApplicationContext(), "Connected", Toast.LENGTH_SHORT).show();
        }

        ed1=root.findViewById(R.id.username);
        ed2=root.findViewById(R.id.email2);
        ed3=root.findViewById(R.id.pass2);
        btn=root.findViewById(R.id.login_bt2);


        progressDialog = new ProgressDialog(getContext());
        progressDialog.setTitle("Creating Account");
        progressDialog.setTitle("We are creating your account");

        auth = FirebaseAuth.getInstance();
        firebaseDatabase =  FirebaseDatabase.getInstance();
        database = firebaseDatabase.getReference();


        btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if(ed1.getText().toString().isEmpty()){

                    ed1.setError("Enter a username");
                    return;
                }

                if(ed2.getText().toString().isEmpty()){

                    ed2.setError("Enter a email");
                    return;
                }

                if(ed3.getText().toString().isEmpty()){

                    ed3.setError("Enter a password");
                    return;
                }


                progressDialog.show();

                String username = ed1.getText().toString().trim();
                String email = ed2.getText().toString().trim();
                String password  = ed3.getText().toString().trim();

             auth.createUserWithEmailAndPassword(ed2.getText().toString(),ed3.getText().toString()).addOnCompleteListener( new OnCompleteListener<AuthResult>() {
                 @Override
                 public void onComplete(@NonNull  Task<AuthResult> task) {

                     progressDialog.dismiss();

                    if(task.isSuccessful()){

                        User user = new User(username,email,password);

                        String id = task.getResult().getUser().getUid();

                        database.child("User").child(id).setValue(user).addOnCompleteListener(new OnCompleteListener<Void>() {
                            @Override
                            public void onComplete(@NonNull Task<Void> task) {
                                Intent intent = new Intent(getActivity().getApplicationContext(), MainActivity.class);
                                startActivity(intent);
                            }
                        });

                         Toast.makeText(getActivity().getApplicationContext(),"User signup successful",Toast.LENGTH_SHORT).show();
                     }
                     else
                     {
                         Toast.makeText(getActivity().getApplicationContext(),task.getException().getMessage(),Toast.LENGTH_SHORT).show();
                     }

                 }
             });


            }
        });

        if (auth.getCurrentUser()!=null){


            Intent intent = new Intent(getActivity().getApplicationContext(), MainActivity.class);
            startActivity(intent);

        }


        return root;
    }
    public void onViewCreated(@NonNull View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);


    }
}
