package com.abhi.journals;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.TextView;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;
import com.google.firebase.storage.StorageReference;

import java.util.ArrayList;
import java.util.List;

import model.journalModel;
import util.journalApi;

public class JournalList extends AppCompatActivity {
    private String currentUsername ;
    private String currentUserID ;

    private FirebaseFirestore db  = FirebaseFirestore.getInstance() ;
    private FirebaseAuth firebaseAuth ;
    private FirebaseAuth.AuthStateListener stateListener ;
    private FirebaseUser user ;

    public RecyclerView recyclerView ;
    public recycler_adapter recycler_adapter ;
    public StorageReference storageReference  ;
    public List<journalModel> journalList ;

    public CollectionReference collectionReference = db.collection("Journals") ;
    public TextView noJournal ;




    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_journal_list);

        firebaseAuth = FirebaseAuth.getInstance() ;
        user = firebaseAuth.getCurrentUser() ;

        recyclerView = findViewById(R.id.recyclerView) ;
        noJournal = findViewById(R.id.list_nojournal) ;
        recyclerView.setHasFixedSize(true);
        journalList = new ArrayList<>() ;
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu,menu);
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        switch (item.getItemId()) {

            case R.id.menu_add:
                if(user!=null && firebaseAuth!=null)
                    startActivity(new Intent(JournalList.this,PostJournalActivity.class));
                   //finish() ;
                break;

            case R.id.menu_singnOut:
                firebaseAuth.signOut();
                startActivity(new Intent(JournalList.this,MainActivity.class));
                //finish() ;
                break;
        }

        return super.onOptionsItemSelected(item);
    }

    @Override
    protected void onStart() {

        super.onStart();

        collectionReference.whereEqualTo("userId", journalApi.getInstance().getUserID())
                .get()
                .addOnSuccessListener(new OnSuccessListener<QuerySnapshot>() {
                    @Override
                    public void onSuccess(QuerySnapshot queryDocumentSnapshots) {
                        if(!queryDocumentSnapshots.isEmpty()){
                            for (QueryDocumentSnapshot journals : queryDocumentSnapshots){
                                journalModel journal = journals.toObject(journalModel.class) ;
                                journalList.add(journal) ;
                            }
                            recycler_adapter = new recycler_adapter(JournalList.this,journalList) ;
                            recyclerView.setAdapter(recycler_adapter);
                            recycler_adapter.notifyDataSetChanged();
                        }
                        else {
                          noJournal.setVisibility(View.VISIBLE);
                        }

                    }
                }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {

            }
        });


    }
}
