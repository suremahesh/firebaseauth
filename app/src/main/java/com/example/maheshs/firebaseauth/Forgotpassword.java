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

public class Forgotpassword extends AppCompatActivity
{

     EditText email;
    FirebaseAuth auth;

    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_forgotpassword);
        email=(EditText)findViewById(R.id.emailfor);
        auth=FirebaseAuth.getInstance();
    }

    public void sendmail(View view)
    {
        String email1=email.getText().toString().trim();
        auth.sendPasswordResetEmail(email1)
                .addOnCompleteListener(this, new OnCompleteListener<Void>() {
                    @Override
                    public void onComplete(@NonNull Task<Void> task) {
                        if(task.isSuccessful())
                        {
                            Toast.makeText(Forgotpassword.this, "we sent mail to change password plese visit ur mail id", Toast.LENGTH_SHORT).show();
                        }
                        else
                        {
                            Toast.makeText(Forgotpassword.this, "error", Toast.LENGTH_SHORT).show();
                        }
                    }
                });


    }
}
