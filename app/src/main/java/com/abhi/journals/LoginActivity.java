package com.abhi.journals;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.text.AutoText;
import android.view.View;
import android.widget.AutoCompleteTextView;
import android.widget.Button;
import android.widget.TextView;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.Task;
import com.google.api.QuotaOrBuilder;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.EventListener;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.FirebaseFirestoreException;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;

import util.journalApi;

public class LoginActivity extends AppCompatActivity {

    public Button login ;
    public Button create_acc ;

    public AutoCompleteTextView email ;
    public TextView password ;


    private FirebaseAuth firebaseAuth;
    private FirebaseAuth.AuthStateListener authStateListener ;
    private FirebaseUser user ;

    private FirebaseFirestore db = FirebaseFirestore.getInstance() ;
    private CollectionReference collectionReference = db.collection("USERS");

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        firebaseAuth = FirebaseAuth.getInstance() ;


        login = findViewById(R.id.btn_login) ;
        create_acc = findViewById(R.id.btn_create_acc) ;
        email=findViewById(R.id.login_email);
        password=findViewById(R.id.login_password) ;

        create_acc.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(LoginActivity.this,CreateAccActivity.class));
            }
        });

        login.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                loginEmailPass(email.getText().toString().trim()
                ,password.getText().toString().trim()) ;
            }
        });




    }

    private void loginEmailPass(String email, String password) {

        firebaseAuth.signInWithEmailAndPassword(email,password)
                .addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        FirebaseUser user = firebaseAuth.getCurrentUser();

                        assert user != null;
                        final String userId = user.getUid();

                        collectionReference.whereEqualTo("userId",userId)
                                .addSnapshotListener(new EventListener<QuerySnapshot>() {
                                    @Override
                                    public void onEvent(@Nullable QuerySnapshot queryDocumentSnapshots, @Nullable FirebaseFirestoreException e) {

                                        if(e!=null){

                                        }
                                        assert queryDocumentSnapshots != null;
                                        if(!queryDocumentSnapshots.isEmpty()){

                                            for (QueryDocumentSnapshot snapshots:queryDocumentSnapshots){

                                                journalApi journalApi = util.journalApi.getInstance();
                                                journalApi.setUserID(snapshots.getString("userId"));
                                                journalApi.setUserName(snapshots.getString("userName"));

                                                startActivity(new Intent(LoginActivity.this
                                                ,JournalList.class));


                                            }


                                        }


                                    }
                                });



                    }
                }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {

            }
        });



    }
}
