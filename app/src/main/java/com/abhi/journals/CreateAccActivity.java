package com.abhi.journals;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.util.Printer;
import android.view.View;
import android.widget.AutoCompleteTextView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.HashMap;
import java.util.Map;
import java.util.Objects;

import util.journalApi;

public class CreateAccActivity extends AppCompatActivity {

    //Layout variables

    private Button create_btn ;
    private EditText create_username ;
    private AutoCompleteTextView create_email ;
    private EditText create_pass ;
    private ProgressBar progressBar ;

    private FirebaseAuth firebaseAuth ;
    private FirebaseAuth.AuthStateListener authStateListener ;
    private FirebaseUser currentUser ;

    private FirebaseFirestore db = FirebaseFirestore.getInstance();
    private CollectionReference collectionReference = db.collection("USERS") ;




    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_create_acc);

        firebaseAuth = FirebaseAuth.getInstance() ;


        create_btn =findViewById(R.id.create_acc_button) ;
        create_username=findViewById(R.id.create_acc_username) ;
        create_email=findViewById(R.id.create_acc_email) ;
        create_pass=findViewById(R.id.create_acc_password) ;
        progressBar=findViewById(R.id.create_acc_progress) ;

        authStateListener = new FirebaseAuth.AuthStateListener() {
            @Override
            public void onAuthStateChanged(@NonNull FirebaseAuth firebaseAuth) {
                if(currentUser!=null){
                    //user logged in already

                }
                else{
                    //not logged in
                    Toast.makeText(CreateAccActivity.this,"Please Login Tp Continue"
                    ,Toast.LENGTH_LONG).show();
                    startActivity(new Intent(CreateAccActivity.this,LoginActivity.class));
                }
            }
        };


        create_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(!TextUtils.isEmpty(create_username.getText().toString()) &&
                        !TextUtils.isEmpty(create_email.getText().toString()) &&
                        !TextUtils.isEmpty(create_pass.getText().toString()) ) {

                    createAccount(create_username.getText().toString()
                            , create_email.getText().toString()
                            , create_pass.getText().toString());
                }
                else {
                    Toast.makeText(CreateAccActivity.this,"All the Fields are Requoired",Toast.LENGTH_LONG).show();
                }
            }
        });

    }

    // create a new user
    public void createAccount(final String userName , final String email , String password){
        //checking if user input is not empty


        if(userName!=null && email!=null && password!=null){


            progressBar.setVisibility(View.VISIBLE);

            //setting up new user ----- see assistant
            firebaseAuth.createUserWithEmailAndPassword(email , password)
                    .addOnCompleteListener(new OnCompleteListener<AuthResult>() {

                        //onCopletion of user sign in
                        @Override
                        public void onComplete(@NonNull Task<AuthResult> task) {
                          // safe check if was sussesfull
                            if(task.isSuccessful()) {
                                      //Maping a user on database
                                  currentUser = firebaseAuth.getCurrentUser() ;
                                assert currentUser != null;
                                final String userId = currentUser.getUid();
                     // creating a hashmap to map

                                 Map<String,String> userObj = new HashMap<>() ;

                                 userObj.put("userId", userId) ;
                                 userObj.put("userName" , userName) ;

                                 collectionReference.add(userObj)
                                         .addOnSuccessListener(new OnSuccessListener<DocumentReference>() {
                                             @Override
                                             public void onSuccess(DocumentReference documentReference) {
                                                 //Toast.makeText(CreateAccActivity.this,"onSucess",Toast.LENGTH_LONG).show();

                                                 Log.d("hello","sussess") ;
                                                 documentReference.get()
                                                         .addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
                                                             @Override
                                                             public void onComplete(@NonNull Task<DocumentSnapshot> task) {

                                                                //if(!Objects.requireNonNull(task.getResult()).exists()){

 //Log.d("hello","sussess") ;
                                                                    progressBar.setVisibility(View.INVISIBLE);

                                                                //Toast.makeText(CreateAccActivity.this,userName,Toast.LENGTH_LONG).show();


                                                                    String name = Objects.requireNonNull(task.getResult()).getString("userName") ;

                                                                   journalApi journalApi = new journalApi() ;
                                                                   util.journalApi.getInstance().setUserID(userId);
                                                                   util.journalApi.getInstance().setUserName(userName);

                                                                //Toast.makeText(CreateAccActivity.this,name,Toast.LENGTH_LONG).show();

                                                                 Log.d("hello",userName) ;
                                                                    Intent intent = new Intent(CreateAccActivity.this
                                                                            ,PostJournalActivity.class) ;

                                                                    intent.putExtra("username",name);
                                                                    intent.putExtra("userid",userId) ;

                                                                    startActivity(intent);


                                                                }
                                                                /*else{
                                                                    progressBar.setVisibility(View.INVISIBLE);
                                                                }*/


                                                         });

                                             }
                                         })
                                         .addOnFailureListener(new OnFailureListener() {
                                             @Override
                                             public void onFailure(@NonNull Exception e) {
                                                        Toast.makeText(CreateAccActivity.this
                                                        ,"error" , Toast.LENGTH_LONG).show();
                                             }
                                         });

                            }
                        }
                    }).addOnFailureListener(new OnFailureListener() {
                @Override
                public void onFailure(@NonNull Exception e) {

                      Toast.makeText(CreateAccActivity.this,"Something Went wrong"+e
                      ,Toast.LENGTH_LONG).show();
                    progressBar.setVisibility(View.INVISIBLE);

                }
            });

        }
        else{

            Toast.makeText(CreateAccActivity.this,"All the Fields are Requoired",Toast.LENGTH_LONG).show();
        }

    }








 // To check if user already exists or not ///
    @Override
    public void onStart() {
        super.onStart();
        // Check if user is signed in (non-null) and update UI accordingly.
        currentUser = firebaseAuth.getCurrentUser();
        firebaseAuth.addAuthStateListener(authStateListener);


    }






}
