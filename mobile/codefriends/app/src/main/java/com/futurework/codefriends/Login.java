 package com.futurework.codefriends;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.auth.api.signin.GoogleSignIn;
import com.google.android.gms.auth.api.signin.GoogleSignInAccount;
import com.google.android.gms.auth.api.signin.GoogleSignInClient;
import com.google.android.gms.auth.api.signin.GoogleSignInOptions;
import com.google.android.gms.common.api.ApiException;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.snackbar.Snackbar;
import com.google.android.material.textfield.TextInputEditText;
import com.google.firebase.auth.AuthCredential;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.auth.GoogleAuthProvider;

 public class Login extends AppCompatActivity {

    private final String TAG = "LoginActivity";
    private FirebaseAuth mAuth;
    private TextView username ;
    private TextView pass;
    private Button login;
    private TextView sighup;
    private Button LoginUsingGoogle;
    private GoogleSignInOptions gso;
    private String email ,password;
    private int RC_GOOGLE_SIGN_IN = 07;
    private GoogleSignInClient mGoogleSignInClient;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        mAuth = FirebaseAuth.getInstance();
        username = findViewById(R.id.username);
        pass = findViewById(R.id.password);
        login = findViewById(R.id.login);
        LoginUsingGoogle = findViewById(R.id.google_login);
        sighup = findViewById(R.id.register);


        sighup.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                email = username.getText().toString().trim();
                password = pass.getText().toString().trim();
                addNewUser();
            }
        });

        LoginUsingGoogle.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                // Configure Google Sign In
                gso = new GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
                        .requestIdToken(getString(R.string.default_web_client_id))
                        .requestEmail()
                        .build();

                mGoogleSignInClient = GoogleSignIn.getClient(Login.this, gso);
                signIn();
                // Initialize Firebase Auth
                mAuth = FirebaseAuth.getInstance();
            }
        });
        login.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                email = username.getText().toString().trim();
                password = pass.getText().toString().trim();
                login();
            }
        });
    }
    void login(){
        Log.d(TAG,"Checking for user information "+username.getText().toString().trim());

        //login with Email
        if(!email.trim().isEmpty() && !password.trim().isEmpty() )
        mAuth.signInWithEmailAndPassword(email, password)
                .addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if (task.isSuccessful()) {
                            // Sign in success, update UI with the signed-in user's information
                            Log.d(TAG, "signInWithEmail:success");
                            Toast.makeText(Login.this, "welcome !",
                                    Toast.LENGTH_SHORT).show();
                            FirebaseUser user = mAuth.getCurrentUser();
                            startActivity(new Intent(Login.this,MainActivity.class).setFlags(Intent.FLAG_ACTIVITY_NO_HISTORY|Intent.FLAG_ACTIVITY_CLEAR_TASK|Intent.FLAG_ACTIVITY_CLEAR_TOP));
                            finish();
                        } else {
                            // If sign in fails, display a message to the user.
                            Log.w(TAG, "signInWithEmail:failure", task.getException());
                            Toast.makeText(Login.this, "Wrong username or password",
                                    Toast.LENGTH_SHORT).show();
                            login.setTextColor(Color.RED);
                            login.setBackgroundColor(Color.rgb(106,0,228));
                        }
                    }
                });
        else
            Toast.makeText(getApplicationContext(),"Can't take empty email or password!",Toast.LENGTH_SHORT).show();

    }

    private void firebaseAuthWithGoogle(String idToken) {
        AuthCredential credential = GoogleAuthProvider.getCredential(idToken, null);
        mAuth.signInWithCredential(credential)
                .addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if (task.isSuccessful()) {
                            // Sign in success, update UI with the signed-in user's information
                            Log.d(TAG, "signInWithCredential:success");
                            FirebaseUser user = mAuth.getCurrentUser();
                        } else {
                            // If sign in fails, display a message to the user.
                            Log.w(TAG, "signInWithCredential:failure", task.getException());
                            //Snackbar.make(getBaseContext(), "Authentication Failed.", Snackbar.LENGTH_SHORT).show();
                            /*updateUI(null);*/
                        }
                    }
                });
    }

    private void signIn() {
        Intent signInIntent = mGoogleSignInClient.getSignInIntent();
        startActivityForResult(signInIntent, RC_GOOGLE_SIGN_IN);
    }

    private void addNewUser(){
        if(!email.trim().isEmpty() && !password.trim().isEmpty() )
        mAuth.createUserWithEmailAndPassword(email, password)
                .addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if (task.isSuccessful()) {
                            // Sign in success, update UI with the signed-in user's information
                            Log.d(TAG, "createUserWithEmail:success");
                            FirebaseUser user = mAuth.getCurrentUser();
                            //updateUI(user);
                            startActivity(new Intent(Login.this,MainActivity.class).setFlags(Intent.FLAG_ACTIVITY_NO_HISTORY|Intent.FLAG_ACTIVITY_CLEAR_TASK|Intent.FLAG_ACTIVITY_CLEAR_TOP));
                            finish();
                        } else {
                            // If sign in fails, display a message to the user.
                            Log.w(TAG, "createUserWithEmail:failure", task.getException());
                            Toast.makeText(Login.this, "Authentication failed.",
                                    Toast.LENGTH_SHORT).show();
                            //updateUI(null);
                        }

                    }
                });
        else
            Toast.makeText(getApplicationContext(),"Can't take empty email or password!",Toast.LENGTH_SHORT).show();
    }

     @Override
     public void onActivityResult(int requestCode, int resultCode, Intent data) {
         super.onActivityResult(requestCode, resultCode, data);

         // Result returned from launching the Intent from GoogleSignInApi.getSignInIntent(...);
         if (requestCode == RC_GOOGLE_SIGN_IN) {
             Task<GoogleSignInAccount> task = GoogleSignIn.getSignedInAccountFromIntent(data);
             try {
                 // Google Sign In was successful, authenticate with Firebase
                 GoogleSignInAccount account = task.getResult(ApiException.class);
                 Log.d(TAG, "firebaseAuthWithGoogle:" + account.getId());
                 firebaseAuthWithGoogle(account.getIdToken());
                 startActivity(new Intent(Login.this,MainActivity.class).setFlags(Intent.FLAG_ACTIVITY_NO_HISTORY|Intent.FLAG_ACTIVITY_CLEAR_TASK|Intent.FLAG_ACTIVITY_CLEAR_TOP));
                 finish();
             } catch (ApiException e) {
                 // Google Sign In failed, update UI appropriately
                 Log.w(TAG, "Google sign in failed", e);
                 // ...
             }
         }
     }
    @Override
    public void onStart() {
        super.onStart();
        // Check if user is signed in (non-null) and update UI accordingly.
        FirebaseUser currentUser = mAuth.getCurrentUser();
        //updateUI(currentUser);
    }

}