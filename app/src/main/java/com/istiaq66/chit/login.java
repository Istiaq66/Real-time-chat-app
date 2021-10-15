package com.istiaq66.chit;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentPagerAdapter;
import androidx.viewpager.widget.ViewPager;


import com.google.android.gms.auth.api.signin.GoogleSignIn;
import com.google.android.gms.auth.api.signin.GoogleSignInAccount;
import com.google.android.gms.auth.api.signin.GoogleSignInClient;
import com.google.android.gms.auth.api.signin.GoogleSignInOptions;
import com.google.android.gms.common.api.ApiException;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.android.material.tabs.TabLayout;
import com.google.firebase.auth.AuthCredential;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.auth.GoogleAuthProvider;
import com.google.firebase.database.FirebaseDatabase;
import com.istiaq66.chit.Models.User;

public class login extends AppCompatActivity {

    TabLayout tabLayout;
    ViewPager VP;
    FloatingActionButton fb,google;
    float v=0;
    FloatingActionButton floatingActionButton;
    GoogleSignInClient signInClient;
    private FirebaseAuth auth;


    FirebaseDatabase database;


    private  long backPressedTime;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        getSupportActionBar().hide();
        tabLayout = findViewById(R.id.tabLayout);
        VP = findViewById(R.id.viewPager);
        google = findViewById(R.id.floatingActionButton);
        fb = findViewById(R.id.floatingActionButton2);


        tabLayout.addTab(tabLayout.newTab().setText("Login"));
        tabLayout.addTab(tabLayout.newTab().setText("Signup"));
        tabLayout.setTabGravity(TabLayout.GRAVITY_FILL);
        auth = FirebaseAuth.getInstance();
        database =  FirebaseDatabase.getInstance();




        //Signup Via Facebook
        fb.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Toast.makeText(login.this, "Not Available Right Now", Toast.LENGTH_LONG).show();
            }
        });



        //Signup Via google//
        GoogleSignInOptions gso = new GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN).requestIdToken(getString(R.string.default_web_client_id)).requestEmail().build();
        signInClient = GoogleSignIn.getClient(this,gso);

        google.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                signIn();
            }
        });





       /* final  Login_Adapter adapter = new Login_Adapter(getSupportFragmentManager(),this,tabLayout.getTabCount());

        VP.setAdapter(adapter);

        VP.addOnPageChangeListener(new TabLayout.TabLayoutOnPageChangeListener(tabLayout));*/


        VP.setAdapter(new MypagerAdapter(getSupportFragmentManager()));
        tabLayout.setupWithViewPager(VP);


        tabLayout.addOnTabSelectedListener(new TabLayout.OnTabSelectedListener() {
            @Override
            public void onTabSelected(TabLayout.Tab tab) {
                VP.setCurrentItem(tab.getPosition());
            }

            @Override
            public void onTabUnselected(TabLayout.Tab tab) {

            }

            @Override
            public void onTabReselected(TabLayout.Tab tab) {

            }
        });



        fb.setTranslationY(300);
        google.setTranslationY(300);
        tabLayout.setTranslationY(300);

        fb.setAlpha(v);
        google.setAlpha(v);
        tabLayout.setAlpha(v);

        fb.animate().translationY(0).alpha(1).setDuration(1000).setStartDelay(400).start();
        google.animate().translationY(0).alpha(1).setDuration(1000).setStartDelay(600).start();
        tabLayout.animate().translationY(0).alpha(1).setDuration(1000).setStartDelay(100).start();




    }

    @Override
    public void onBackPressed() {

        if(backPressedTime + 2000>System.currentTimeMillis())
        {
            super.onBackPressed();
            this.finishAffinity();
            return;
        }
        else
        {
            Toast.makeText(getBaseContext(),"Press back again to exit",Toast.LENGTH_SHORT).show();
        }
        backPressedTime = System.currentTimeMillis();

    }
    class MypagerAdapter extends FragmentPagerAdapter{


        String[] text = {"Login","Signup"};


        public MypagerAdapter( FragmentManager fm) {
            super(fm);
        }


        @Override
        public Fragment getItem(int i) {

            if (i == 0) {

                return new login_layout_fragment();
            }
            if (i == 1) {

                return new Signup_fragment();
            }
           return  null;
        }

        @Override
        public int getCount() {
            return text.length;
        }

        @Override
        public CharSequence getPageTitle(int position) {
            return text[position];
        }
    }
    int RC_SIGN_IN = 65;
    private void signIn() {
        Intent signInIntent = signInClient.getSignInIntent();
        startActivityForResult(signInIntent, RC_SIGN_IN);
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        // Result returned from launching the Intent from GoogleSignInApi.getSignInIntent(...);
        if (requestCode == RC_SIGN_IN) {
            Task<GoogleSignInAccount> task = GoogleSignIn.getSignedInAccountFromIntent(data);
            try {
                // Google Sign In was successful, authenticate with Firebase
                GoogleSignInAccount account = task.getResult(ApiException.class);
                Log.d("TAG", "firebaseAuthWithGoogle:" + account.getId());
                firebaseAuthWithGoogle(account.getIdToken());
            } catch (ApiException e) {
                // Google Sign In failed, update UI appropriately
                Log.w("TAG", "Google sign in failed", e);
            }
        }
    }


    private void firebaseAuthWithGoogle(String idToken) {
        AuthCredential credential = GoogleAuthProvider.getCredential(idToken, null);
        auth.signInWithCredential(credential)
                .addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if (task.isSuccessful()) {
                            // Sign in success, update UI with the signed-in user's information

                            FirebaseUser user = auth.getCurrentUser();
                            User users = new User();
                            users.setUserid(user.getUid());
                            users.setUsername(user.getDisplayName());
                            users.setProfilepic(user.getPhotoUrl().toString());
                            database.getReference().child("User").child(user.getUid()).setValue(users);


                            Intent intent = new Intent(login.this, MainActivity.class);
                            startActivity(intent);

                            Log.d("TAG", "signInWithCredential:success");
                      //    updateUI(user);
                        } else {
                            Toast.makeText(login.this,task.getException().getMessage(),Toast.LENGTH_SHORT).show();

                            // If sign in fails, display a message to the user.
                            Log.w("TAG", "signInWithCredential:failure", task.getException());
                       //     updateUI(null);
                        }
                    }
                });
    }

}