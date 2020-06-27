package com.abhi.journals;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.AutoCompleteTextView;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.Switch;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.Timestamp;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;

import java.util.Date;

import model.journalModel;
import util.journalApi;


public class PostJournalActivity<jornalApi> extends AppCompatActivity implements View.OnClickListener {

    private Button button ;
    private ImageView background_image;
    private ImageView camera_image ;
    private TextView  post_name ;
    private TextView post_date ;
    private AutoCompleteTextView post_Title ;
    private AutoCompleteTextView post_Description;
    private ProgressBar post_progressBar ;

    private Uri imageUri ;


    private String currentUsername ;
    private String currentUserID ;

    private FirebaseFirestore db  = FirebaseFirestore.getInstance() ;
    private FirebaseAuth firebaseAuth ;
    private FirebaseAuth.AuthStateListener stateListener ;
    private FirebaseUser user ;

    private CollectionReference collectionReference = db.collection("Journals") ;
    public StorageReference storageReference ;






    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_post_journal);

        button=findViewById(R.id.post_button);
        background_image=findViewById(R.id.post_image);
        camera_image=findViewById(R.id.post_Camera_image_btn);
        post_name=findViewById(R.id.post_name);
        post_date=findViewById(R.id.post_date);
        post_Title=findViewById(R.id.post_title);
        post_Description=findViewById(R.id.post_description);
        post_progressBar=findViewById(R.id.post_progressBar);

        storageReference = FirebaseStorage.getInstance().getReference() ;



        firebaseAuth = FirebaseAuth.getInstance() ;

        if(journalApi.getInstance()!=null){
            currentUsername= journalApi.getInstance().getUserName();
            currentUserID= journalApi.getInstance().getUserID() ;

            //Toast.makeText(PostJournalActivity.this,currentUsername,Toast.LENGTH_LONG).show();
            post_name.setText(currentUsername);

        }

        stateListener = new FirebaseAuth.AuthStateListener() {
            @Override
            public void onAuthStateChanged(@NonNull FirebaseAuth firebaseAuth) {
                user= firebaseAuth.getCurrentUser();

                if(user!=null){

                }else {

                }
            }
        };





        camera_image.setOnClickListener(this);
        button.setOnClickListener(this);


    }

    @Override
    public void onClick(View v) {

        switch (v.getId()){
            case R.id.post_button:
                post_progressBar.setVisibility(View.VISIBLE);
                  saveJournal() ;
                break;
            case R.id.post_Camera_image_btn:
                Intent galleryIntent = new Intent(Intent.ACTION_GET_CONTENT);
                galleryIntent.setType("image/*");
                startActivityForResult(galleryIntent,1);



                break;


        }
    }

    private void saveJournal() {
        final String title = post_Title.getText().toString();
        final String description = post_Description.getText().toString();

        if(!TextUtils.isEmpty(title) && !TextUtils.isEmpty(description) && imageUri!=null){

              final StorageReference filepath = storageReference
                    .child("Journals_images")
                    .child("image_"+ Timestamp.now().getSeconds()) ;

            filepath.putFile(imageUri)
                    .addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
                        @Override
                        public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {

                            Log.i("hello","inside putfile");
                            filepath.getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>() {
                                @Override
                                public void onSuccess(Uri uri) {

                                    //log.i("hello","inside getdown");


                                    Log.d("hello", "onSuccess: ");

                                    journalModel journalModel = new journalModel();
                                    journalModel.setTitle(title);
                                    journalModel.setDescription(description);
                                    journalModel.setImageUrl(uri.toString());
                                    journalModel.setUserId(currentUserID);
                                    journalModel.setUserName(currentUsername);
                                    journalModel.setTimeAdded(new Timestamp(new Date()));



                                    collectionReference.add(journalModel)
                                            .addOnSuccessListener(new OnSuccessListener<DocumentReference>() {
                                                @Override
                                                public void onSuccess(DocumentReference documentReference) {

                                                    Log.i("hello","inside journal model");

                                                    post_progressBar.setVisibility(View.INVISIBLE);
                                                    startActivity(new Intent(PostJournalActivity.this
                                                    ,JournalList.class));
                                                    finish();

                                                }
                                            }).addOnFailureListener(new OnFailureListener() {
                                        @Override
                                        public void onFailure(@NonNull Exception e) {

                                            Toast.makeText(PostJournalActivity.this
                                            ,"something went wrong - "+e,Toast.LENGTH_LONG).show();

                                            Log.d("hello","v"+e) ;

                                        }
                                    });


                                }
                            });
                        }
                    }).addOnFailureListener(new OnFailureListener() {
                @Override
                public void onFailure(@NonNull Exception e) {
                       Toast.makeText(PostJournalActivity.this,"Something Went wrong Bitch!!"+e,
                               Toast.LENGTH_LONG).show();
                       Log.d("hello ","h"+e ) ;
                }
            });}
            else{
                Toast.makeText(PostJournalActivity.this,"fill all Bitch!!",
                        Toast.LENGTH_LONG).show();
            }


        }






    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if(requestCode==1 && resultCode==RESULT_OK ){
            if(data!=null){
                imageUri = data.getData() ;
                background_image.setBackground(null);
                background_image.setImageURI(imageUri);

            }
        }

    }

    @Override
    protected void onStart() {
        super.onStart();
        user=firebaseAuth.getCurrentUser() ;
        firebaseAuth.addAuthStateListener(stateListener);
    }

    @Override
    protected void onStop() {
        super.onStop();
        if(firebaseAuth!=null){
            firebaseAuth.removeAuthStateListener(stateListener);
        }

    }
}
