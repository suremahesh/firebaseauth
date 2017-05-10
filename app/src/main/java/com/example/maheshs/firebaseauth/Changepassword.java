package com.example.maheshs.firebaseauth;

import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

public class Changepassword extends AppCompatActivity
{

    EditText et1;
    FirebaseAuth auth;
     FirebaseUser user;

    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_changepassword);
        et1=(EditText)findViewById(R.id.newpass);
        user=FirebaseAuth.getInstance().getCurrentUser();

    }

    public void updatepassword(View view)
    {
        String pass=et1.getText().toString().trim();
        user.updatePassword(pass)
                .addOnCompleteListener(this, new OnCompleteListener<Void>() {
                    @Override
                    public void onComplete(@NonNull Task<Void> task) {
                        if(task.isSuccessful())
                        {
                            Toast.makeText(Changepassword.this, "successfullyupdated password", Toast.LENGTH_SHORT).show();
                        }
                        else
                        {
                            Toast.makeText(Changepassword.this, "error", Toast.LENGTH_SHORT).show();
                        }
                    }
                });

    }
}
