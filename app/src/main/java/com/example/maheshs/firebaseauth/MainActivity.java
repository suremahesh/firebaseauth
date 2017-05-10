package com.example.maheshs.firebaseauth;

import android.app.ProgressDialog;
import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

import com.google.android.gms.auth.api.Auth;
import com.google.android.gms.auth.api.signin.GoogleSignInAccount;
import com.google.android.gms.auth.api.signin.GoogleSignInOptions;
import com.google.android.gms.auth.api.signin.GoogleSignInResult;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthCredential;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.auth.GoogleAuthProvider;

public class MainActivity extends AppCompatActivity {


    String email,pass;
     EditText et1;
     EditText et2;
    FirebaseAuth auth;
     FirebaseAuth.AuthStateListener authListener;
    ProgressDialog pd ;
    GoogleApiClient mGoogleApiClient;
     String TAG;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        et1 = (EditText) (findViewById(R.id.emailid));
        et2 = (EditText) (findViewById(R.id.password));
        pd = new ProgressDialog(this);
        auth=FirebaseAuth.getInstance();
        FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
        authListener = new FirebaseAuth.AuthStateListener() {
            @Override
            public void onAuthStateChanged(@NonNull FirebaseAuth firebaseAuth) {
                FirebaseUser user = firebaseAuth.getCurrentUser();
                if (user != null) {
                    // user auth state is changed - user is null
                    // launch login activity
                    startActivity(new Intent(MainActivity.this, Home.class));
                    finish();
                }
            }
        };


    }

    public void login(View view)
    {
         email=et1.getText().toString().trim();
        pass=et2.getText().toString().trim();
        pd.show();
        auth.signInWithEmailAndPassword(email,pass)
                .addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if(task.isSuccessful())
                        {
                            pd.dismiss();
                            Intent i=new Intent(MainActivity.this,Home.class);
                            startActivity(i);
                            et1.setText("");
                            et2.setText("");
                        }
                        else
                            {
                                pd.dismiss();
                                Toast.makeText(MainActivity.this, "u dont have account please create a account", Toast.LENGTH_SHORT).show();

                        }
                    }
                });

    }

    public void signup(View view)
    {
        Intent i=new Intent(this,Signup.class);
        startActivity(i);

    }

    public void signinwithgoogle(View view)
    {
      GoogleSignInOptions gso = new GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
                .requestEmail()
                .build();

        mGoogleApiClient = new GoogleApiClient.Builder(this)
               /* .enableAutoManage(this *//**//* FragmentActivity *//**//*, this *//**//* OnConnectionFailedListener *//**//*)*/
                .addApi(Auth.GOOGLE_SIGN_IN_API, gso)
                .build();

        Intent signInIntent = Auth.GoogleSignInApi.getSignInIntent(mGoogleApiClient);
        startActivityForResult(signInIntent, 1212);
    }
    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        // Result returned from launching the Intent from GoogleSignInApi.getSignInIntent(...);
        if (requestCode == 1212) {
            GoogleSignInResult result = Auth.GoogleSignInApi.getSignInResultFromIntent(data);
            handleSignInResult(result);
            GoogleSignInAccount account = result.getSignInAccount();
           // firebaseAuthWithGoogle(account);
        }
    }
    private void firebaseAuthWithGoogle(GoogleSignInAccount acct) {
        Log.d(TAG, "firebaseAuthWithGoogle:" + acct.getId());

        AuthCredential credential = GoogleAuthProvider.getCredential(acct.getIdToken(), null);
        auth.signInWithCredential(credential)
                .addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if (task.isSuccessful()) {
                            // Sign in success, update UI with the signed-in user's information
                            Log.d(TAG, "signInWithCredential:success");
                            FirebaseUser user = auth.getCurrentUser();
                            Toast.makeText(MainActivity.this, ""+user.getEmail(), Toast.LENGTH_SHORT).show();
                        } else {
                            // If sign in fails, display a message to the user.
                            Log.w(TAG, "signInWithCredential:failure", task.getException());
                            Toast.makeText(MainActivity.this, "Authentication failed.",
                                    Toast.LENGTH_SHORT).show();

                        }

                        // ...
                    }
                });
    }
    private void handleSignInResult(GoogleSignInResult result) {
        Log.d(TAG, "handleSignInResult:" + result.isSuccess());
        if (result.isSuccess()) {
            // Signed in successfully, show authenticated UI.
            GoogleSignInAccount acct = result.getSignInAccount();
            Toast.makeText(this, ""+acct.getEmail(), Toast.LENGTH_SHORT).show();
            Intent i=new Intent(this,Home.class);
            startActivity(i);
        /*    mStatusTextView.setText(getString(R.string.signed_in_fmt, acct.getDisplayName()));*/
           /* updateUI(true);*/
        } else {
            // Signed out, show unauthenticated UI.
           /* updateUI(false);*/
        }
    }

}