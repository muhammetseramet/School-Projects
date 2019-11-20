package com.example.neyesek;


import android.content.Intent;
import android.net.Uri;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.TextView;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.database.annotations.NotNull;
import com.squareup.picasso.Picasso;

import org.w3c.dom.Text;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class UserPage extends AppCompatActivity {

    public TextView user_info;
    public TextView fav_rest;
    public TextView prev_rest;
    public TextView level;
    private DatabaseReference prevRef;
    private DatabaseReference favRef;
    private DatabaseReference levelRef;
    private FirebaseAuth mAuth;
    private FirebaseAuth.AuthStateListener mAuthListener;
    private FirebaseUser mCurrentUser;

    @Override
    protected void onCreate(Bundle savedInstanceState) {


        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_user_page);
        user_info = (TextView)findViewById(R.id.user_info);
        prev_rest = (TextView)findViewById(R.id.prev_rest);
        fav_rest = (TextView)findViewById(R.id.fav_rest);
        level = (TextView)findViewById(R.id.level);

        String restNames[] = getRestaurants();


        mAuth = FirebaseAuth.getInstance();
        mAuthListener = new FirebaseAuth.AuthStateListener() {
            @Override
            public void onAuthStateChanged(@NonNull FirebaseAuth firebaseAuth) {
                if (mAuth.getCurrentUser()==null){
                    Intent loginIntent = new Intent(UserPage.this, RegisterScreen.class);
                    loginIntent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK);startActivity(loginIntent);
                }
            }
        };

        mCurrentUser = mAuth.getCurrentUser();
        prevRef = FirebaseDatabase.getInstance().getReference().child("NeYesek").child(mCurrentUser.getUid()).child("Previous Rest");
        favRef = FirebaseDatabase.getInstance().getReference().child("NeYesek").child(mCurrentUser.getUid()).child("Favorite Rest");
        levelRef = FirebaseDatabase.getInstance().getReference().child("NeYesek").child(mCurrentUser.getUid()).child("Level");

        prevRef.keepSynced(true);
        if (mCurrentUser != null) {
            // Name, email address, and profile photo Url
            String name = mCurrentUser.getDisplayName();
            String email = mCurrentUser.getEmail();

            levelRef.addListenerForSingleValueEvent(new ValueEventListener() {
                @Override
                public void onDataChange(@NonNull DataSnapshot dataSnapshot) {

                    String levelText = dataSnapshot.getValue().toString();
                    level.setText(levelText);
                }

                @Override
                public void onCancelled(@NonNull DatabaseError databaseError) {

                }
            });




            favRef.addListenerForSingleValueEvent(new ValueEventListener() {
                @Override
                public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                    for(DataSnapshot ds: dataSnapshot.getChildren()){

                        System.out.println(">>>>>>>>" + ds.getValue());
                        String rest = ds.getValue().toString();
                        fav_rest.append(">"+rest);
                        fav_rest.append(" \n");
                    }
                }

                @Override
                public void onCancelled(@NonNull DatabaseError databaseError) {

                }
            });


            prevRef.addListenerForSingleValueEvent(new ValueEventListener() {

                @Override
                public void onDataChange(@NotNull DataSnapshot dataSnapshot) {
                    for(DataSnapshot ds: dataSnapshot.getChildren()){

                        System.out.println(">>>>>>>>" + ds.getValue());
                        String rest = ds.getValue().toString();
                        prev_rest.append(">" + rest);
                        prev_rest.append(" \n");
                    }


                }

                @Override
                public void onCancelled(DatabaseError databaseError) {

                }
            });



            user_info.append("\n");
            user_info.append(email);
            // The user's ID, unique to the Firebase project. Do NOT use this value to
            // authenticate with your backend server, if you have one. Use
            // FirebaseUser.getToken() instead.
            String uid = mCurrentUser.getUid();
        }


        user_info.setEnabled(false);
        prev_rest.setEnabled(false);
        fav_rest.setEnabled(false);


    }

    public static String[] getRestaurants(){

        return new String[]{
            "Tavuk Dünyası",
            "Mc Donalds",
            "HD İskender",
            "Burger King",
            "Nusret",
            "Pilav Dünyası",
            "Subway"
        };

    }

    private void collectRest(Map<String,Object> users) {

        ArrayList<String> phoneNumbers = new ArrayList<>();

        //iterate through each user, ignoring their UID
        for (Map.Entry<String, Object> entry : users.entrySet()){

            //Get user map
            Map singleUser = (Map) entry.getValue();
            //Get phone field and append to list
            phoneNumbers.add((String) singleUser.get("Previous Rest"));
        }

        System.out.println(phoneNumbers.toString());
    }



}
