package com.istiaq66.chit;

import android.app.ProgressDialog;
import android.content.Intent;
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

public class login_layout_fragment extends Fragment {
    EditText ed1,ed2;
    Button lgbt;
    float i=0;

    FirebaseAuth auth;
    ProgressDialog progressDialog;
    @Override
    public View onCreateView(LayoutInflater inflator, ViewGroup container, Bundle saveInstanceState) {

        View root = (ViewGroup) inflator.inflate(R.layout.login_layout_fragment,container,false);

        ed1 = root.findViewById(R.id.email);
        ed2 = root.findViewById(R.id.pass);
        lgbt = root.findViewById(R.id.login_bt);

        ed1.setTranslationX(800);
        ed2.setTranslationX(800);
        lgbt.setTranslationX(800);

        ed1.setAlpha(i);
        ed2.setAlpha(i);
        lgbt.setAlpha(i);

        ed1.animate().translationX(0).alpha(1).setDuration(800).setStartDelay(300).start();
        ed2.animate().translationX(0).alpha(1).setDuration(800).setStartDelay(500).start();
        lgbt.animate().translationX(0).alpha(1).setDuration(800).setStartDelay(700).start();


        progressDialog = new ProgressDialog(getContext());
        progressDialog.setTitle("Login");
        progressDialog.setTitle("Login to your account");

        auth = FirebaseAuth.getInstance();


        lgbt.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if(ed1.getText().toString().isEmpty()){

                    ed1.setError("Enter a email");
                    return;
                }

                if(ed2.getText().toString().isEmpty()){

                    ed2.setError("Enter a password");
                    return;
                }

                progressDialog.show();

                String username = ed1.getText().toString().trim();
                String email = ed2.getText().toString().trim();

                auth.signInWithEmailAndPassword(ed1.getText().toString(),ed2.getText().toString()).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull  Task<AuthResult> task) {
                        progressDialog.dismiss();
                        if(task.isSuccessful()){


                            Intent intent = new Intent(getActivity().getApplicationContext(), MainActivity.class);
                            startActivity(intent);

                        }
                        else
                        {

                            Toast.makeText(getActivity().getApplicationContext(),task.getException().getMessage(),Toast.LENGTH_SHORT).show();

                        }

                    }
                });
            }
        });

        if(auth.getCurrentUser()!=null){
            Intent intent = new Intent(getActivity().getApplicationContext(),MainActivity.class);
            startActivity(intent);
        }




        return root;
    }
}
