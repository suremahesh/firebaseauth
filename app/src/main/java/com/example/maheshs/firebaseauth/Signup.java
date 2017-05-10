package com.example.maheshs.firebaseauth;

import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;

public class Signup extends AppCompatActivity
{
    String email,pass;
    EditText et1;
    EditText et2;
    FirebaseAuth auth;
    FirebaseAuth.AuthStateListener authListener;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_signup);
        et1 = (EditText) (findViewById(R.id.emailid));
        et2 = (EditText) (findViewById(R.id.password));
        auth=FirebaseAuth.getInstance();
    }

    public void signup(View view)
    {

        email=et1.getText().toString().trim();
        pass=et2.getText().toString().trim();
        auth.createUserWithEmailAndPassword(email,pass)
                .addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if(task.isSuccessful())
                        {
                            Intent i=new Intent(Signup.this,MainActivity.class);
                            startActivity(i);
                        }
                        else
                        {
                            Toast.makeText(Signup.this, "u dont have account please create a account", Toast.LENGTH_SHORT).show();

                        }
                    }
                });

    }
}
