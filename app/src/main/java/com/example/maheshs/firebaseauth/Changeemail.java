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

public class Changeemail extends AppCompatActivity
{

    EditText et1;
    FirebaseAuth auth;
     FirebaseUser user;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_changeemail);
        et1=(EditText)findViewById(R.id.newemail);
        user=FirebaseAuth.getInstance().getCurrentUser();
    }

    public void updateemail(View view)
    {
        String email=et1.getText().toString().trim();
        user.updateEmail(email)
                .addOnCompleteListener(this, new OnCompleteListener<Void>() {
                    @Override
                    public void onComplete(@NonNull Task<Void> task) {
                        if(task.isSuccessful())
                        {
                            Toast.makeText(Changeemail.this, "succeefullyupdatewd", Toast.LENGTH_SHORT).show();
                        }
                        else
                        {
                            Toast.makeText(Changeemail.this, "error", Toast.LENGTH_SHORT).show();
                        }
                    }
                });
    }
}
