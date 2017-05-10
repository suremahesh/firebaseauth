package com.example.maheshs.firebaseauth;

import android.app.ProgressDialog;
import android.content.ContentResolver;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.provider.MediaStore;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.webkit.MimeTypeMap;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.target.BitmapImageViewTarget;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.OnProgressListener;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;

import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.concurrent.ExecutionException;

public class upload extends AppCompatActivity implements View.OnClickListener
{

    private static final int PICK_IMAGE_REQUEST = 234;

    //view objects
    private Button buttonChoose;

    private Button buttonUpload;
    Button show;

    Bitmap bitmap;

    private ImageView imageView,imageView1;
    String uploadId;
    //uri to store file
    private Uri filePath;

    //firebase objects
    private StorageReference storageReference;
    private DatabaseReference mDatabase;
     String TAG;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_upload);
        buttonChoose = (Button) findViewById(R.id.buttonChoose);
        buttonUpload = (Button) findViewById(R.id.buttonUpload);
        show = (Button) findViewById(R.id.show);
        imageView = (ImageView) findViewById(R.id.imageView);
        imageView1 = (ImageView) findViewById(R.id.imageView1);

        storageReference = FirebaseStorage.getInstance().getReference();
        mDatabase = FirebaseDatabase.getInstance().getReference(Constants.DATABASE_PATH_UPLOADS);

        buttonChoose.setOnClickListener(this);
        buttonUpload.setOnClickListener(this);
        show.setOnClickListener(this);

    }

    @Override
    public void onClick(View v)
    {

        if (v == buttonChoose)
        {
            showFileChooser();
        }
        else if (v == buttonUpload)
        {
            uploadFile();
        }
        else if(v==show)
        {
            addUserChangeListener();
           // Toast.makeText(this, "developing", Toast.LENGTH_SHORT).show();

        }


    }
    private void showFileChooser() {
        Intent intent = new Intent();
        intent.setType("image/*");
        intent.setAction(Intent.ACTION_GET_CONTENT);
        startActivityForResult(Intent.createChooser(intent, "Select Picture"), PICK_IMAGE_REQUEST);
    }
    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == PICK_IMAGE_REQUEST && resultCode == RESULT_OK && data != null && data.getData() != null) {
            filePath = data.getData();
            try {
                 bitmap= MediaStore.Images.Media.getBitmap(getContentResolver(), filePath);
                imageView.setImageBitmap(bitmap);
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }
    public String getFileExtension(Uri uri) {
        ContentResolver cR = getContentResolver();
        MimeTypeMap mime = MimeTypeMap.getSingleton();
        return mime.getExtensionFromMimeType(cR.getType(uri));
    }
   public   void uploadFile() {
        //checking if file is available
        if (filePath != null) {
            //displaying progress dialog while image is uploading
            final ProgressDialog progressDialog = new ProgressDialog(this);
            progressDialog.setTitle("Uploading");
            progressDialog.show();

            //getting the storage reference
            StorageReference sRef = storageReference.child(Constants.STORAGE_PATH_UPLOADS + System.currentTimeMillis() + "." + getFileExtension(filePath));

            //adding the file to reference
            sRef.putFile(filePath)
                    .addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
                        @Override
                        public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                            //dismissing the progress dialog
                            progressDialog.dismiss();

                            //displaying success toast
                            Toast.makeText(getApplicationContext(), "File Uploaded ", Toast.LENGTH_LONG).show();

                            //creating the upload object to store uploaded image details
                           // Upload upload = new Upload(editTextName.getText().toString().trim(), taskSnapshot.getDownloadUrl().toString());

                            Uri downloadUrl = taskSnapshot.getDownloadUrl();
                            Uploadclass uploadclass=new Uploadclass(downloadUrl.toString());
                            //adding an upload to firebase database
                             uploadId= mDatabase.push().getKey();
                            mDatabase.child(uploadId).setValue(uploadclass);


                        }
                    })
                    .addOnFailureListener(new OnFailureListener() {
                        @Override
                        public void onFailure(@NonNull Exception exception) {
                            progressDialog.dismiss();
                            Toast.makeText(getApplicationContext(), exception.getMessage(), Toast.LENGTH_LONG).show();
                        }
                    })
                    .addOnProgressListener(new OnProgressListener<UploadTask.TaskSnapshot>() {
                        @Override
                        public void onProgress(UploadTask.TaskSnapshot taskSnapshot) {
                            //displaying the upload progress
                            double progress = (100.0 * taskSnapshot.getBytesTransferred()) / taskSnapshot.getTotalByteCount();
                            progressDialog.setMessage("Uploaded " + ((int) progress) + "%...");
                           // addUserChangeListener();
                        }
                    });

        } else {
            //display an error if no file is selected
            Toast.makeText(this, "the image not found", Toast.LENGTH_SHORT).show();

        }
    }
     void addUserChangeListener() {
        // User data change listener
        mDatabase.child(uploadId).addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                Uploadclass upload = dataSnapshot.getValue(Uploadclass.class);

                // Check for null
                if (upload == null) {
                    Log.e(TAG, "User data is null!");
                    Toast.makeText(upload.this, "no data", Toast.LENGTH_SHORT).show();
                    return;
                }

                //  Log.e(TAG, "User data is changed!" + user.name + ", " + user.email);

               String url1=upload.url;

                Glide.with(getApplicationContext())
                        .load(url1)
                        .asBitmap()
                        .into(new BitmapImageViewTarget(imageView1) {
                            @Override
                            protected void setResource(Bitmap resource) {
                                //Play with bitmap
                                super.setResource(resource);
                            }
                        });
                //Glide.with(getApplicationContext()).load(url1).into(imageView1);
              /*  Bitmap bitmap= null;*/
             /*   try {
                    bitmap = Glide.
                            with(getApplicationContext()).
                            load(url1).
                            asBitmap().
                            into(100, 100). // Width and height
                            get();
                    imageView1.setImageBitmap(bitmap);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                } catch (ExecutionException e) {
                    e.printStackTrace();
                }
*/
                /*HttpURLConnection connection = null;
                try {
                    URL url = new URL(url1);
                    connection = (HttpURLConnection) url.openConnection();
                    connection.setDoInput(true);
                    connection.connect();
                    InputStream input = connection.getInputStream();
                    Bitmap myBitmap = BitmapFactory.decodeStream(input);
                    imageView1.setImageBitmap(myBitmap);
                } catch (IOException e) {
                    e.printStackTrace();
                }
*/
               /* try {
                    Bitmap bitmap1 = MediaStore.Images.Media.getBitmap(getContentResolver(), Uri.parse(url));

                    imageView1.setImageBitmap(bitmap1);

                } catch (IOException e) {
                    e.printStackTrace();
                }*/
                imageView.setImageBitmap(bitmap);
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
}
