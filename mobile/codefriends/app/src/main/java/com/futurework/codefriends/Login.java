 package com.futurework.codefriends;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import android.view.View;
import android.view.animation.AlphaAnimation;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.futurework.codefriends.Database.UserDb.UserDbProvider;
import com.futurework.codefriends.Service.Service;
import com.futurework.codefriends.data.UserInfoData;
import com.futurework.codefriends.templates.CustomProgressBar;
import com.google.android.gms.auth.api.signin.GoogleSignIn;
import com.google.android.gms.auth.api.signin.GoogleSignInAccount;
import com.google.android.gms.auth.api.signin.GoogleSignInClient;
import com.google.android.gms.auth.api.signin.GoogleSignInOptions;
import com.google.android.gms.common.api.ApiException;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthCredential;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.auth.GoogleAuthProvider;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.Arrays;
import java.util.List;
import java.util.Map;
import java.util.Objects;

 public class Login extends AppCompatActivity {

    private final String TAG = "LoginActivity";
    private FirebaseAuth mAuth;
    private TextView username ;
    private TextView pass;
    private Button login;
    private GoogleSignInOptions gso;
    private String email ,password;
    private final int RC_GOOGLE_SIGN_IN = 7;
    private GoogleSignInClient mGoogleSignInClient;
    private FirebaseUser user;
    private final AlphaAnimation buttonClick = new AlphaAnimation(1F, 0.8F);

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        mAuth = FirebaseAuth.getInstance();
        username = findViewById(R.id.username);
        pass = findViewById(R.id.password);
        login = findViewById(R.id.login);
        Button loginUsingGoogle = findViewById(R.id.google_login);
        TextView sighup = findViewById(R.id.register);


        sighup.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                view.startAnimation(buttonClick);
                email = username.getText().toString().trim();
                password = pass.getText().toString().trim();
                addNewUser();
            }
        });

        //this function enables the google sign in method
        loginUsingGoogle.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                // Configure Google Sign In
                view.startAnimation(buttonClick);
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
        //implement the email method
        login.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                view.startAnimation(buttonClick);
                email = username.getText().toString().trim();
                password = pass.getText().toString().trim();
                if(!email.isEmpty() && !password.isEmpty()){
                    login();
                }else {
                    Toast.makeText(Login.this, "empty username or password.",
                            Toast.LENGTH_SHORT).show();
                }
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
                            gotoMainActivity();
                        } else {
                            // If sign in fails, display a message to the user.
                            Log.w(TAG, "signInWithEmail:failure"+ Objects.requireNonNull(task.getException()).getMessage());
                            Toast.makeText(Login.this, task.getException().getMessage(),
                                    Toast.LENGTH_LONG).show();
                            login.setBackgroundResource(R.drawable.button_login_error);
                            new Handler().postDelayed(new Runnable() {
                                @Override
                                public void run() {
                                  login.setBackgroundResource(R.drawable.button_login);
                                }
                            },1000);
                        }
                    }
                }).addOnFailureListener(this, new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {

                    }
                });
        else
            Toast.makeText(getApplicationContext(),"Can't take empty email or password!",Toast.LENGTH_SHORT).show();

    }

    public void gotoMainActivity(){
        long count = new UserDbProvider(this).getCount();
        if(count > 0)
            startActivity(new Intent(Login.this, MainActivity.class));
        else{
            startActivity(new Intent(Login.this, UserForm.class));
        }

        Login.this.finish();
    }

    private void firebaseAuthWithGoogle(String idToken) {
        AuthCredential credential = GoogleAuthProvider.getCredential(idToken,null);
        mAuth.signInWithCredential(credential)
                .addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if (task.isSuccessful()) {
                            // Sign in success with google, update UI with the signed-in user's information
                            Log.d(TAG, "signInWithCredential:success");
                            Log.d(TAG, Objects.requireNonNull(Objects.requireNonNull(task.getResult()).getUser()).getEmail());
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
                            // Sign in success with email, update UI with the signed-in user's information
                            Log.d(TAG, "createUserWithEmail:success");
                            user = mAuth.getCurrentUser();
                            if(user != null){
                                final CustomProgressBar progressBar = new CustomProgressBar(Login.this);
                                progressBar.setTitle("Checking for");
                                progressBar.setMessage("User");

                                Log.d(TAG,user.toString());
                                Log.d(TAG,user.getEmail());
                                String w = user.getEmail();


                                assert w != null;
                                FirebaseFirestore.getInstance().collection("Users")
                                        .document(new Service().removeExtraFromString(w))
                                        .get().addOnSuccessListener(new OnSuccessListener<DocumentSnapshot>() {
                                    @Override
                                    public void onSuccess(DocumentSnapshot documentSnapshot) {

                                        Map<String, Object> id = documentSnapshot.getData();

                                        final UserInfoData data = new UserInfoData();
                                        data.setId(id.get("id").toString());
                                        data.setName(id.get("name").toString().trim());
                                        data.setImage(Objects.requireNonNull(id.get("image")).toString().trim());
                                        data.setEmail(id.get("email").toString().trim());
                                        data.setNumber(id.get("number").toString().trim());
                                        data.setStatus(Objects.requireNonNull(id.get("status")).toString().trim());
                                        final List list = Arrays.asList("C++", "Java", "Android");
                                        data.setTags((String[]) list.toArray());

                                        UserDbProvider dbProvider = new UserDbProvider(Login.this);
                                        if (dbProvider.insertUserData(data) != -1) {
                                            progressBar.dismiss();
                                            Log.d(TAG,"done 232");
                                            startActivity(new Intent(Login.this, MainActivity.class));
                                            Toast.makeText(getApplicationContext(),"Welcome! "+ user.getDisplayName(),Toast.LENGTH_LONG).show();
                                            Login.this.finish();
                                        }
                                        progressBar.dismiss();
                                    }
                                }).addOnFailureListener(new OnFailureListener() {
                                    @Override
                                    public void onFailure(@NonNull Exception e) {
                                        progressBar.dismiss();
                                        Log.d(TAG,"failed 1");
                                        gotoMainActivity();
                                    }
                                });
                            }
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
             task.addOnCompleteListener(new OnCompleteListener<GoogleSignInAccount>() {
                 @Override
                 public void onComplete(@NonNull Task<GoogleSignInAccount> task) {
                     GoogleSignInAccount account = null;
                     try {
                         account = task.getResult(ApiException.class);
                     }catch (ApiException e) {
                         // Google Sign In failed, update UI appropriately
                         Log.w(TAG, "Google sign in failed", e);
                         // ...
                     }

                     if(account != null) {
                         Log.d(TAG, "firebaseAuthWithGoogle:" + account.getEmail());
                         firebaseAuthWithGoogle(account.getIdToken());

                         final CustomProgressBar progressBar = new CustomProgressBar(Login.this);
                         progressBar.setTitle("Checking for");
                         progressBar.setMessage("User");

                         Log.d(TAG,account.getEmail());
                         String w = account.getEmail();


                         assert w != null;
                         FirebaseFirestore.getInstance().collection("Users")
                                 .document(new Service().removeExtraFromString(w))
                                 .get().addOnSuccessListener(new OnSuccessListener<DocumentSnapshot>() {
                             @Override
                             public void onSuccess(DocumentSnapshot documentSnapshot) {

                                 Map<String, Object> id = documentSnapshot.getData();

                                 final UserInfoData data = new UserInfoData();
                                 data.setId(id.get("id").toString());
                                 data.setName(id.get("name").toString().trim());
                                 data.setImage(Objects.requireNonNull(id.get("image")).toString().trim());
                                 data.setEmail(id.get("email").toString().trim());
                                 data.setNumber(id.get("number").toString().trim());
                                 data.setStatus(Objects.requireNonNull(id.get("status")).toString().trim());
                                 final List list = Arrays.asList("C++", "Java", "Android");
                                 data.setTags((String[]) list.toArray());

                                 UserDbProvider dbProvider = new UserDbProvider(Login.this);

                                 if (dbProvider.insertUserData(data) != -1) {
                                     progressBar.dismiss();
                                     Log.d(TAG,"done 315");

                                     startActivity(new Intent(Login.this, MainActivity.class));
                                     Toast.makeText(getApplicationContext(),"Welcome! "+ data.getName(),Toast.LENGTH_LONG).show();
                                     Login.this.finish();
                                 }
                                 progressBar.dismiss();
                             }
                         }).addOnFailureListener(new OnFailureListener() {
                             @Override
                             public void onFailure(@NonNull Exception e) {
                                 progressBar.dismiss();
                                 Log.d(TAG,"failed 1");
                                 gotoMainActivity();
                             }
                         });

                     }else{
                        Toast.makeText(Login.this,"Not able to login pleas check your internet! " ,Toast.LENGTH_LONG).show();
                     }
                 }
             });
         }
     }
    @Override
    public void onStart() {
        super.onStart();
    }

}