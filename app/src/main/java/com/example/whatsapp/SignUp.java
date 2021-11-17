package com.example.whatsapp;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Toast;

import com.example.whatsapp.Models.Users;
import com.example.whatsapp.databinding.ActivitySignUpBinding;
import com.example.whatsapp.databinding.ActivitySigninBinding;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.FirebaseDatabase;

public class SignUp extends AppCompatActivity {

   ActivitySignUpBinding binding;
   private FirebaseAuth auth;
    FirebaseDatabase database;
    ProgressDialog progressDialog;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivitySignUpBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        getSupportActionBar().hide();
        auth = FirebaseAuth.getInstance();
        database = FirebaseDatabase.getInstance();

        progressDialog = new ProgressDialog(SignUp.this);
        progressDialog.setTitle("Creating Account");
        progressDialog.setMessage("We're creating your account");



        binding.btnSignup.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(binding.eemail.getText().toString() .isEmpty()){
                    binding.eemail.setError("Enter your email");
                    return;
                }
                if(binding.epassword.getText().toString() .isEmpty()){
                    binding.epassword.setError("Enter your password");
                    return;
                }
                if(binding.eusername.getText().toString() .isEmpty()){
                    binding.eusername.setError("Enter your username");
                    return;
                }
                progressDialog.show();
                auth.createUserWithEmailAndPassword
                        (binding.eemail.getText().toString(),binding.epassword.getText().toString()).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                            progressDialog.dismiss();
                        if(task.isSuccessful()){
                            Users user = new Users(binding.eusername.getText().toString(),binding.eemail.getText().toString(),binding.epassword.getText().toString());

                            String id = task.getResult().getUser().getUid();
                            database.getReference().child("Users").child(id).setValue(user);
                            Toast.makeText(SignUp.this, "User Created Successfully", Toast.LENGTH_SHORT).show();
                        }
                        else{
                            Toast.makeText(SignUp.this, task.getException().getMessage(), Toast.LENGTH_SHORT).show();
                        }

                    }
                });

            }
        });

        binding.alreadyAccount.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(SignUp.this,Signin.class);
                startActivity(intent);
            }
        });




    }
}