package com.example.firebasesocialmediaapp;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.os.Build;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.storage.FirebaseStorage;
import com.shashank.sony.fancytoastlib.FancyToast;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;

public class ViewPostActivity extends AppCompatActivity implements AdapterView.OnItemClickListener, AdapterView.OnItemLongClickListener {

    private ListView postListView;
    private ArrayList<String> usernames;
    private ArrayAdapter adapter;
    private FirebaseAuth firebaseAuth;
    private ArrayList<DataSnapshot> listPost;
    private ImageView imgSentPost;
    private TextView txtDescription;




    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_view_post);

        firebaseAuth = firebaseAuth.getInstance();

        postListView = findViewById(R.id.postListView);
        usernames = new ArrayList<>();
        adapter = new ArrayAdapter(this,android.R.layout.simple_list_item_1, usernames);
        postListView.setAdapter(adapter);
        postListView.setOnItemClickListener(this);
        postListView.setOnItemLongClickListener(this);

        imgSentPost = findViewById(R.id.imgSentPost);
        txtDescription = findViewById(R.id.txtDescription);
        listPost = new ArrayList<>();

        FirebaseDatabase.getInstance().getReference()
                .child("my_users")
                .child(firebaseAuth.getUid())
                .child("received_post").addChildEventListener(new ChildEventListener() {
            @Override
            public void onChildAdded(@NonNull DataSnapshot dataSnapshot, @Nullable String s) {
                listPost.add(dataSnapshot);
                String fromWhom =(String) dataSnapshot.child("fromWhom").getValue();
                usernames.add(fromWhom);
                adapter.notifyDataSetChanged();

            }

            @Override
            public void onChildChanged(@NonNull DataSnapshot dataSnapshot, @Nullable String s) {

            }

            @Override
            public void onChildRemoved(@NonNull DataSnapshot dataSnapshot) {

                int i = 0;
                for(DataSnapshot snapshot: listPost){

                    if(snapshot.getKey().equals(dataSnapshot.getKey())){

                        listPost.remove(i);
                        usernames.remove(i);

                    }
                    i++;


                }

                adapter.notifyDataSetChanged();

            }

            @Override
            public void onChildMoved(@NonNull DataSnapshot dataSnapshot, @Nullable String s) {

            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });






    }

    @Override
    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {

        DataSnapshot chosenData = listPost.get(position);
        String downloadLink = chosenData.child("imageLink").getValue() +"";
        Picasso.get().load(downloadLink).into(imgSentPost);

        String des = chosenData.child("des").getValue()+"";
        txtDescription.setText(des);




    }

    @Override
    public boolean onItemLongClick(AdapterView<?> parent, View view, final int position, long id) {

        Context context;
        AlertDialog.Builder builder;
        if(Build.VERSION.SDK_INT>= Build.VERSION_CODES.LOLLIPOP){

            builder = new AlertDialog.Builder(this,android.R.style.Theme_Material_Dialog_Alert);

        }else{

            builder = new AlertDialog.Builder(this);

        }
        builder.setTitle("Delete this post that you received")
                .setPositiveButton("YES", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        //Do delete
                        FirebaseStorage.getInstance().getReference()
                                .child("image")
                                .child((String)listPost.get(position)
                                        .child("imageIdentifier").getValue()).delete();

                        FirebaseDatabase.getInstance().getReference()
                                .child("my_users")
                                .child(firebaseAuth.
                                        getCurrentUser().getUid())
                                .child("received_post").child(listPost.get(position).getKey()).removeValue();


                                showToast(listPost.size()+"",FancyToast.INFO);


                    }
                })
                .setNegativeButton("CANCEL", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        //Do nothing
                    }
                }).setIcon(android.R.drawable.ic_dialog_alert).show();
        return true;
    }

    private void showToast(String message, int type) {

        FancyToast.makeText(this, message, FancyToast.LENGTH_SHORT, type, false).show();

    }
}
