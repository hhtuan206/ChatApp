package com.hht.chatapp;

import android.content.Intent;
import android.view.View;
import android.widget.*;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import android.os.Bundle;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import com.bumptech.glide.Glide;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.*;
import com.hht.chatapp.Apdapter.MessageAdapter;
import com.hht.chatapp.Model.Chat;
import com.hht.chatapp.Model.Users;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class MessengeActivity extends AppCompatActivity {
    private TextView username;
    private ImageView imageView;
    private  Toolbar toolbar;

    RecyclerView recyclerView;
    EditText msgET;
    ImageButton sendBtn;

    private FirebaseUser firebaseUser;
    private DatabaseReference reference;
    Intent intent;
    MessageAdapter messageAdapter;
    List<Chat> mChat;




    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_messenge);

        initWidgets();

        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });



        intent =getIntent();
        final String userid = intent.getStringExtra("userid");

        firebaseUser = FirebaseAuth.getInstance().getCurrentUser();
        reference = FirebaseDatabase.getInstance().getReference("MyUsers").child(userid);

        reference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                Users user = snapshot.getValue(Users.class);
                username.setText(user.getUsername());

                if(user.getImageUrl().equals("default")){
                    imageView.setImageResource(R.mipmap.ic_launcher);
                }else{
                    Glide.with(MessengeActivity.this)
                            .load(user.getImageUrl())
                            .into(imageView);
                }
                readMessage(firebaseUser.getUid(),userid,user.getImageUrl());
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });

        sendBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String msg = msgET.getText().toString();
                if(!msg.equals("")){
                    sendMessage(firebaseUser.getUid(),userid,msg);
                }else {
                    Toast.makeText(MessengeActivity.this, "Input msg", Toast.LENGTH_SHORT).show();
                }
                msgET.setText("");
            }
        });

        LinearLayoutManager linearLayoutManager =new LinearLayoutManager(getApplicationContext());
        linearLayoutManager.setStackFromEnd(true);
        recyclerView.setLayoutManager(linearLayoutManager);
    }

    private void sendMessage(String sender, String receiver, String msg) {
        DatabaseReference reference = FirebaseDatabase.getInstance().getReference();

        HashMap<String,Object> hashMap = new HashMap<>();
        hashMap.put("sender",sender);
        hashMap.put("receiver",receiver);
        hashMap.put("message",msg);

        reference.child("Chats").push().setValue(hashMap);

    }

    private void readMessage(final String myid, final String userid, final String imgUrl){
        mChat = new ArrayList<>();

        reference = FirebaseDatabase.getInstance().getReference("Chats");

        reference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                mChat.clear();
                for (DataSnapshot dataSnapshot:snapshot.getChildren()
                     ) {
                    Chat chat = dataSnapshot.getValue(Chat.class);
                    if((chat.getReceiver().equals(myid) && chat.getSender().equals(userid) )|| (chat.getReceiver().equals(userid) && chat.getSender().equals(myid)) ){
                        mChat.add(chat);
                    }

                    messageAdapter = new MessageAdapter(MessengeActivity.this,mChat,imgUrl);
                    recyclerView.setAdapter(messageAdapter);
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });

    }

    private void initWidgets(){
        username = findViewById(R.id.usnText);
        imageView = findViewById(R.id.avatar);
        toolbar = findViewById(R.id.toolbar);
        msgET = findViewById(R.id.text_send);
        sendBtn = findViewById(R.id.btn_send);
        recyclerView = findViewById(R.id.recycleView);
        recyclerView.setHasFixedSize(true);
        setSupportActionBar(toolbar);
        getSupportActionBar().setTitle("");
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
    }

    private void setSupportActionBar(Toolbar toolbar) {
    }
}