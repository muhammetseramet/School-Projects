//package com.example.neyesek;
//
//import android.content.Intent;
//import android.net.Uri;
//import android.os.Bundle;
//import android.support.annotation.NonNull;
//import android.support.v7.app.AppCompatActivity;
//import android.text.TextUtils;
//import android.view.View;
//import android.widget.Button;
//import android.widget.Toast;
//
//
//import com.google.android.gms.tasks.OnCompleteListener;
//import com.google.android.gms.tasks.OnSuccessListener;
//import com.google.android.gms.tasks.Task;
//import com.google.firebase.FirebaseApp;
//import com.google.firebase.auth.FirebaseAuth;
//import com.google.firebase.auth.FirebaseUser;
//import com.google.firebase.database.DataSnapshot;
//import com.google.firebase.database.DatabaseError;
//import com.google.firebase.database.DatabaseReference;
//import com.google.firebase.database.FirebaseDatabase;
//import com.google.firebase.database.ValueEventListener;
//import com.google.firebase.storage.FirebaseStorage;
//import com.google.firebase.storage.StorageReference;
//import com.google.firebase.storage.UploadTask;
//import com.squareup.picasso.Picasso;
//
//
//
//
//public class NearByRestaurant extends AppCompatActivity {
//
//    private Button postBtn;
//    private StorageReference storage;
//    private FirebaseDatabase database;
//    private DatabaseReference databaseRef;
//    private FirebaseAuth mAuth;
//    private Uri uri = null;
//    private DatabaseReference mDatabaseUsers;
//    private FirebaseUser mCurrentUser;
//
//    @Override
//    protected void onCreate(Bundle savedInstanceState){
//        super.onCreate(savedInstanceState);
//        setContentView(R.layout.activity_buttons);
//
//
//        storage = FirebaseStorage.getInstance().getReference();
//        databaseRef = database.getInstance().getReference().child("NeYesek");
//        mAuth = FirebaseAuth.getInstance();
//        mCurrentUser = mAuth.getCurrentUser();
//        mDatabaseUsers = FirebaseDatabase.getInstance().getReference().child("Users").child(mCurrentUser.getUid());
//
//
//        postBtn.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View view) {
//                Toast.makeText(NearByRestaurant.this, "POSTING...", Toast.LENGTH_LONG).show();
//                final String PostTitle = "Previous Restaurants";
//                final String PostDesc = "KFC";
//                // do a check for empty fields
//                if (!TextUtils.isEmpty(PostDesc) && !TextUtils.isEmpty(PostTitle)){
//                    StorageReference filepath = storage.child("post_images").child(uri.getLastPathSegment());
//                    filepath.putFile(uri).addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
//                        @Override
//                        public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
//
//                            //getting the post image download url
//                            Toast.makeText(getApplicationContext(), "Succesfully Uploaded", Toast.LENGTH_SHORT).show();
//                            final DatabaseReference newPost = databaseRef.push();
//                            //adding post contents to database reference
//                            mDatabaseUsers.addValueEventListener(new ValueEventListener() {
//                                @Override
//                                public void onDataChange(DataSnapshot dataSnapshot) {
//                                    newPost.child("title").setValue(PostTitle);
//                                    newPost.child("desc").setValue(PostDesc);
//                                    newPost.child("email").setValue(mCurrentUser.getUid());
//                                    newPost.child("username").setValue(dataSnapshot.child("name").getValue())
//                                            .addOnCompleteListener(new OnCompleteListener<Void>() {
//                                                @Override
//                                                public void onComplete(@NonNull Task<Void> task) {
//
//                                                    if (task.isSuccessful()){
//                                                        Intent intent = new Intent(NearByRestaurant.this, ButtonsScreen.class);
//                                                        startActivity(intent);
//                                                    }
//                                                }
//                                            });
//                                }
//
//                                @Override
//                                public void onCancelled(DatabaseError databaseError) {
//
//                                }
//                            });
//                        }
//                    });
//
//                }
//            }
//        });
//
//
//
//
//
//
//    }
//
//
//}
