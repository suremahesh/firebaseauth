package com.example.maheshs.firebaseauth;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

public class moredata extends AppCompatActivity
{
       EditText e1,e2;
       FirebaseDatabase db;
       DatabaseReference ref;
     String userid;
     String TAG;
     String name;
     String add;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_moredata);
        e1=(EditText)findViewById(R.id.username);
        e2=(EditText)findViewById(R.id.address);
        ref=db.getInstance().getReference();
    }

    public void submitmoredata(View view)
    {
         name=e1.getText().toString().trim();
         add=e2.getText().toString().trim();


        data d=new data(name,add);
        /*
        if (TextUtils.isEmpty(userid)) {
            userid = ref.push().getKey();
        }*/
       // userid = ref.push().getKey();
        userid = ref.push().getKey();
        ref.child(userid).setValue(d);
        addUserChangeListener();

      /* if (TextUtils.isEmpty(userid)) {
            addUserChangeListener();
        } else {
            updateUser(name, add);
        }*/
    }
    private void addUserChangeListener() {
        // User data change listener
        ref.child(userid).addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                data dat = dataSnapshot.getValue(data.class);

                // Check for null
                if (dat == null) {
                    Log.e(TAG, "User data is null!");
                    return;
                }

              //  Log.e(TAG, "User data is changed!" + user.name + ", " + user.email);

                Toast.makeText(moredata.this, ""+dat.address+dat.name, Toast.LENGTH_SHORT).show();
                e1.setText("");
                e2.setText("");
                // Display newly updated name and email
              /*  txtDetails.setText(user.name + ", " + user.email);

                // clear edit text
                inputEmail.setText("");
                inputName.setText("");

                toggleButton();*/
            }

            @Override
            public void onCancelled(DatabaseError error) {
                // Failed to read value
                Log.e(TAG, "Failed to read user", error.toException());
            }
        });
    }

    /*public void updatedata(View view)
    {
        ref.child(userid).child("name").setValue(name);
        ref.child(userid).child("address").setValue(add);

        e1.setText("");
        e2.setText("");
    }*/
    private void updateUser(String name, String add) {
        // updating the user via child nodes
        if (!TextUtils.isEmpty(name))
            ref.child(userid).child("name").setValue(name);

        if (!TextUtils.isEmpty(add))
            ref.child(userid).child("address").setValue(add);
    }
}
