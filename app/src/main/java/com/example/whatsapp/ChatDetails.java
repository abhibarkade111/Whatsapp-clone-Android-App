package com.example.whatsapp;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Toast;

import com.example.whatsapp.Adapters.ChatAdapter;
import com.example.whatsapp.Models.Messages;
import com.example.whatsapp.databinding.ActivityChatDetailsBinding;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;
import java.util.Date;

public class ChatDetails extends AppCompatActivity {

    ActivityChatDetailsBinding binding;
    FirebaseDatabase database;
    FirebaseAuth auth;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityChatDetailsBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        getSupportActionBar().hide();
        database = FirebaseDatabase.getInstance();
        auth = FirebaseAuth.getInstance();

       final String senderid = auth.getUid();
        String reciveid = getIntent().getStringExtra("userId");
        String username =  getIntent().getStringExtra("userName");
        String profilepic = getIntent().getStringExtra("profilePic");
//        Toast.makeText(this, reciveid, Toast.LENGTH_SHORT).show();

        binding.userName.setText(username);
        Picasso.get().load(profilepic).placeholder(R.drawable.defaultpic).into(binding.profileImage);

        binding.backArrow.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(ChatDetails.this,MainActivity.class);
                startActivity(intent);
            }
        });

        final ArrayList<Messages> messages = new ArrayList<>();
        final ChatAdapter chatAdapter = new ChatAdapter(messages, this, reciveid);
        binding.chatRecycleView.setAdapter(chatAdapter);

        LinearLayoutManager layoutManager = new LinearLayoutManager(this);
        binding.chatRecycleView.setLayoutManager(layoutManager);

        final String senderRoom = senderid+reciveid;
        final String recieverRoom  = reciveid+senderid;

        database.getReference().child("chats").child(senderRoom).addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                messages.clear();
                for (DataSnapshot snapshot1 : snapshot.getChildren()){
                    Messages model = snapshot1.getValue(Messages.class);
                    model.setMessageId(snapshot1.getKey());
                    messages.add(model);
                }
                chatAdapter.notifyDataSetChanged();
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });


        binding.send.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(binding.etMessage.getText().toString() .isEmpty()){
                    binding.etMessage.setError("Enter Message");
                    return;
                }
                String message = binding.etMessage.getText().toString();
                final Messages model = new Messages(senderid, message );
                model.setTimestamp(new Date().getTime());
                binding.etMessage.setText("");

                database.getReference().child("chats").child(senderRoom).push().setValue(model).addOnSuccessListener(new OnSuccessListener<Void>() {
                    @Override
                    public void onSuccess(Void unused) {
                        database.getReference().child("chats").child(recieverRoom).push().setValue(model).addOnSuccessListener(new OnSuccessListener<Void>() {
                            @Override
                            public void onSuccess(Void unused) {

                            }
                        });
                    }
                });
            }
        });

    }
    @Override
    public void onBackPressed() {

        finish();
        Intent intent = new Intent(ChatDetails.this, MainActivity.class);
        startActivity(intent);
    }
}