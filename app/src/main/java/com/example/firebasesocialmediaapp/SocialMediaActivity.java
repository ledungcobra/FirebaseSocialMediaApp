package com.example.firebasesocialmediaapp;

import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ListView;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.google.firebase.auth.FirebaseAuth;

public class SocialMediaActivity extends AppCompatActivity {

    private FirebaseAuth mAuth;
    private ImageView imgView;
    private Button btnCreatePost;
    private EditText edtDesciption;
    private ListView usersListView;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_social_media);
        mAuth = FirebaseAuth.getInstance();

    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.my_menu,menu);

        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        if(item.getItemId()== R.id.logOutItem){

            logOut();

        }
        return super.onOptionsItemSelected(item);
    }

    private void logOut(){

        mAuth.signOut();
        finish();
        startActivity(new Intent(this,MainActivity.class));


    }
}
