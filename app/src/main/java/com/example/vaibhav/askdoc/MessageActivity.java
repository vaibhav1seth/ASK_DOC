package com.example.vaibhav.askdoc;

import android.content.Intent;
import android.os.Build;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.Toast;
import android.widget.Toolbar;

import com.bumptech.glide.Glide;
import com.example.vaibhav.askdoc.Adapter.MessageAdapter;
import com.example.vaibhav.askdoc.Models.Chats;
import com.example.vaibhav.askdoc.Models.User;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import de.hdodenhof.circleimageview.CircleImageView;

public class MessageActivity extends AppCompatActivity {

    CircleImageView profile_image;
    TextView username;
    FirebaseUser fuser;
    DatabaseReference reference;
    ImageButton btn_send;
    EditText text_send;

    MessageAdapter messageAdapter;
    List<Chats> mChat;
    RecyclerView recyclerView;

    Intent intent;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_message);



        Toolbar toolbar = findViewById(R.id.toolbar);
        //getSupportActionBar().setTitle("");
        //getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });

        recyclerView = findViewById(R.id.recycler_view);
        recyclerView.setHasFixedSize(true);
        LinearLayoutManager linearLayoutManager=new LinearLayoutManager(getApplicationContext());
        linearLayoutManager.setStackFromEnd(true);
        recyclerView.setLayoutManager(linearLayoutManager);





        profile_image = findViewById(R.id.profile_image);
        username = findViewById(R.id.username_main);
        btn_send = findViewById(R.id.btn_send);
        text_send = findViewById(R.id.text_send);
        intent=getIntent();
        final String userid = intent.getStringExtra("userid");
        fuser = FirebaseAuth.getInstance().getCurrentUser();
        btn_send.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String msg = text_send.getText().toString();
                if(!msg.equals(""))
                {
                    sendmessage(fuser.getUid(),userid,msg);
                }
                else {
                    Toast.makeText(MessageActivity.this,"you can't send empty messages",Toast.LENGTH_SHORT).show();
                }
                text_send.setText("");
            }
        });

        reference= FirebaseDatabase.getInstance().getReference("Users").child(userid);
        reference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                User user = dataSnapshot.getValue(User.class);
                username.setText(user.getUsername());
                if (user.getImageURL().equals("default")){

                    profile_image.setImageResource(R.mipmap.ic_launcher);
                }
                else {
                    Glide.with(MessageActivity.this).load(user.getImageURL()).into(profile_image);
                }

                readMessages(fuser.getUid(),userid,user.getImageURL());
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });


    }

    private void sendmessage(String sender , String reciever, String message)
    {
        DatabaseReference reference = FirebaseDatabase.getInstance().getReference();
        HashMap<String,Object> hashMap = new HashMap<>();
        hashMap.put("sender",sender);
        hashMap.put("reciever",reciever);
        hashMap.put("message",message);

        reference.child("Chats").push().setValue(hashMap);
    }

    private void readMessages(final String myid, final String userid,final String imageurl){

        mChat = new ArrayList<>();
        reference= FirebaseDatabase.getInstance().getReference("Chats");
        reference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                mChat.clear();
                for (DataSnapshot snapshot :dataSnapshot.getChildren()){

                    Chats chats = snapshot.getValue(Chats.class);
                    if (chats.getReciever().equals(myid) && chats.getSender().equals(userid) ||
                            chats.getReciever().equals(userid) && chats.getSender().equals(myid)){
                        mChat.add(chats);
                    }

                    messageAdapter= new MessageAdapter(MessageActivity.this,mChat,imageurl);
                    recyclerView.setAdapter(messageAdapter);
                }
            }


            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
    }
}
