package com.example.neyesek;

import android.content.Context;
import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.auth.api.Auth;
import com.google.android.gms.auth.api.signin.GoogleSignInAccount;
import com.google.android.gms.auth.api.signin.GoogleSignInOptions;
import com.google.android.gms.auth.api.signin.GoogleSignInResult;
import com.google.android.gms.auth.api.signin.SignInAccount;
import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.SignInButton;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthCredential;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.GoogleAuthProvider;

import org.w3c.dom.Text;

import java.util.Map;

public class LoginScreen extends AppCompatActivity implements GoogleApiClient.ConnectionCallbacks, GoogleApiClient.OnConnectionFailedListener {

    EditText email, password;
    TextView info;
    Button login;
    SignInButton googleSign;
    private FirebaseAuth auth;
    private GoogleApiClient mGoogleApiClient;
    private static final int RC_SIGN_IN = 9001;
    int counter=5;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.login_screen);

        email = (EditText) findViewById(R.id.et_email);
        password = (EditText) findViewById(R.id.et_password);
        login = (Button) findViewById(R.id.btn_login);
        info = (TextView) findViewById(R.id.tv_error);
        googleSign = (SignInButton) findViewById(R.id.sign_in_button_google);

        auth = FirebaseAuth.getInstance();
        System.out.println("HEREEEEEEEEEEEEEEE");


        GoogleSignInOptions gso = new GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
                .requestIdToken("924059974928-iout342vcf1s8tium2auf1vcoo5octi3.apps.googleusercontent.com")
                .requestEmail()
                .build();
        mGoogleApiClient = new GoogleApiClient.Builder(this)
                .addConnectionCallbacks((GoogleApiClient.ConnectionCallbacks) this)
                .enableAutoManage(LoginScreen.this, (GoogleApiClient.OnConnectionFailedListener)this/* OnConnectionFailedListener */)
                .addApi(Auth.GOOGLE_SIGN_IN_API, gso)
                .build();

        login.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                signIn();
            }
        });

//        if(auth.getCurrentUser() != null){
//            startActivity(new Intent(LoginScreen.this,ButtonsScreen.class));
//        }

        login.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                String emailS = email.getText().toString();
                String passwordS = password.getText().toString();

                if (TextUtils.isEmpty(emailS)) {
                    Toast.makeText(getApplicationContext(), "Please enter your email", Toast.LENGTH_SHORT).show();
                    return;
                }
                //Parola girilmemiş ise kullanıcıyı uyarıyoruz.
                if (TextUtils.isEmpty(passwordS)) {
                    Toast.makeText(getApplicationContext(), "Please enter your password", Toast.LENGTH_SHORT).show();
                    return;
                }


                auth.signInWithEmailAndPassword(emailS, passwordS)
                        .addOnCompleteListener(LoginScreen.this, new OnCompleteListener<AuthResult>() {
                            @Override
                            public void onComplete(@NonNull Task<AuthResult> task) {
                                if (task.isSuccessful()) {
                                    startActivity(new Intent(LoginScreen.this,ButtonsScreen.class));
                                }
                                else {
                                    Log.e("Giriş Hatası",task.getException().getMessage());
                                }
                            }
                        });

            }
        });


    }

    // TODO(1) We will connect this with the register implicit intent
    // And write all xml codes on register activity
    public void action_register(View v){

        Context context = LoginScreen.this;
        Class destination = RegisterScreen.class;

        Intent intent = new Intent(context, destination);

        startActivity(intent);

    }

    // TODO(2) Connect this with the main page
    // Before connection make sure the user exist
    public void action_login(String userEmail, String userPassword) {
        /*if (userEmail.equals("admin") && userPassword.equals("1234")) {
            Context context = LoginScreen.this;
            Class destination = ButtonsScreen.class;
            Intent intent = new Intent(context, destination);
            startActivity(intent);
        }else {
            if(userEmail == null || userPassword == null){
                info.setVisibility(View.VISIBLE);
                info.setText("Formu doldurunuz");
            }else {
                if(userEmail.equals("admin") == false){
                    info.setVisibility(View.VISIBLE);
                    info.setText("Kullanıcı bulunamadı");
                    }
                if(userEmail.equals("admin@neyesek.com") && userPassword.equals("1234") == false){
                    info.setVisibility(View.VISIBLE);
                    info.setText("Yanlış Şifre");
                    }
                }
            }*/
        Context context = LoginScreen.this;
        Class destination = ButtonsScreen.class;
        Intent intent = new Intent(context, destination);
        startActivity(intent);
    }

    private void signIn() {
        Intent signInIntent = Auth.GoogleSignInApi.getSignInIntent(mGoogleApiClient);
        startActivityForResult(signInIntent, RC_SIGN_IN);

    }


    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode == RC_SIGN_IN) {
            GoogleSignInResult result = Auth.GoogleSignInApi.getSignInResultFromIntent(data);
            if (result.isSuccess()) {
                // Google Sign In basarili oldugunda Firebase ile yetkilendir
                GoogleSignInAccount account = result.getSignInAccount();
                firebaseAuthWithGoogle(account);

            } else {
                // Google Sign In hatası.
                //Log.e(TAG, "Google Sign In hatası.");
            }
        }
    }



    private void firebaseAuthWithGoogle(GoogleSignInAccount acct) {
        // Log.d(TAG, "firebaseAuthWithGooogle:" + acct.getId());
        AuthCredential credential = GoogleAuthProvider.getCredential(acct.getIdToken(), null);
        auth.signInWithCredential(credential)
                .addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        //             Log.d(TAG, "signInWithCredential:onComplete:" + task.isSuccessful());


                        if (!task.isSuccessful()) {
                            //               Log.w(TAG, "signInWithCredential", task.getException());
                            Toast.makeText(LoginScreen.this, "Yetkilendirme hatası.",
                                    Toast.LENGTH_SHORT).show();
                        } else {
                            startActivity(new Intent(LoginScreen.this, ButtonsScreen.class));
                            finish();
                        }
                    }
                });}

    public void onConnectionFailed(@NonNull ConnectionResult connectionResult) {
        //Log.d(TAG, "onConnectionFailed:" + connectionResult);
        Toast.makeText(this, "Google Play Services error.", Toast.LENGTH_SHORT).show();
    }


    @Override
    public void onConnected(@Nullable Bundle bundle) {

    }

    @Override
    public void onConnectionSuspended(int i) {

    }

    @Override
    public void onPointerCaptureChanged(boolean hasCapture) {

    }
}
