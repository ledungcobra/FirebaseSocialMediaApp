package com.example.firebasesocialmediaapp;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.auth.UserProfileChangeRequest;
import com.google.firebase.database.FirebaseDatabase;
import com.shashank.sony.fancytoastlib.FancyToast;

public class MainActivity extends AppCompatActivity {

    private EditText edtEmail,edtUsername,edtPassword;
    private Button btnSignUp,btnSignIn;

    private FirebaseAuth mAuth;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        edtEmail = findViewById(R.id.edtEmail);
        edtUsername = findViewById(R.id.edtUsername);
        edtPassword = findViewById(R.id.edtPassword);
        btnSignIn = findViewById(R.id.btnSignIn);
        btnSignUp = findViewById(R.id.btnSignUp);

        mAuth = FirebaseAuth.getInstance();

        btnSignUp.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if(edtEmail.getText().toString().equals("")||edtPassword.getText().toString().equals("")){

                    showToast("You must complete this field",FancyToast.WARNING);
                }else{

                    signUp();

                }

            }
        });

        btnSignIn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                signIn();
            }
        });
    }

    @Override
    protected void onStart() {
        super.onStart();
        FirebaseUser currentUser = mAuth.getCurrentUser();
        if(currentUser!=null){

            //TODO: TRansition to the next activity
            transitionToSocialMediaActivity();


        }
    }
    private void showToast(String message,int type){

        FancyToast.makeText(this,message,FancyToast.LENGTH_SHORT,type,false).show();

    }

    private void signUp(){

        mAuth.createUserWithEmailAndPassword(edtEmail.getText().toString(),edtPassword.getText().toString()).addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
            @Override
            public void onComplete(@NonNull Task<AuthResult> task) {

                if(task.isSuccessful()){

                    FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();

                    UserProfileChangeRequest profileUpdates = new UserProfileChangeRequest.Builder()
                            .setDisplayName(edtUsername.getText().toString()).build();

                    assert user != null;
                    user.updateProfile(profileUpdates);

                    FirebaseDatabase.getInstance()
                .getReference()
                            .child("my_users")
                            .child(mAuth.getCurrentUser().getUid())
                            .child("username").setValue(edtUsername.getText().toString());
                    showToast("sign up successfully",FancyToast.SUCCESS);
                    transitionToSocialMediaActivity();
                }else{

                    showToast("Sign up fail",FancyToast.ERROR);

                }
            }
        });

    }

    private void signIn(){

        mAuth.signInWithEmailAndPassword(edtEmail.getText().toString(),edtPassword.getText().toString()).addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
            @Override
            public void onComplete(@NonNull Task<AuthResult> task) {
                if(task.isSuccessful()){

                    showToast("Signing In successfully",FancyToast.SUCCESS);
                    transitionToSocialMediaActivity();

                }else{

                    showToast("Failure in signing in",FancyToast.ERROR);


                }
            }
        });

    }
    private void transitionToSocialMediaActivity(){

        finish();
        startActivity(new Intent(this,SocialMediaActivity.class));


    }
}
