package com.mshilkov.chatfuture;

import static android.content.ContentValues.TAG;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

public class SignInActivity extends AppCompatActivity {
    private FirebaseAuth mAuth;
    EditText emailEditText;
    EditText passwordEditText;
    EditText nameEditText;
    TextView toggleLoginSingUpTextView;
    EditText repeatPasswordEditiText;
    Button loginSignUpButton;
    private static  final String TAG="SignIN";
    private  boolean loginModeActive;
    FirebaseDatabase database;
    DatabaseReference databaseReference ;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sign_in);
        database=FirebaseDatabase.getInstance();

        databaseReference= database.getReference().child("users");
        mAuth = FirebaseAuth.getInstance();
        emailEditText=findViewById(R.id.emailEditText);
        passwordEditText=findViewById(R.id.passwordEditText);
        nameEditText=findViewById(R.id.nameEditText);
        toggleLoginSingUpTextView=findViewById(R.id.toggleLoginSignUpTextView);
        loginSignUpButton=findViewById(R.id.signUpButton);
        repeatPasswordEditiText=findViewById(R.id.repeatPasswordEditText);
        loginSignUpButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                loginSignUpUser(emailEditText.getText().toString().trim(), passwordEditText.getText().toString().trim());
            }
        });
        if(mAuth.getCurrentUser()!=null)
        {
            startActivity(new Intent(SignInActivity.this, MainActivity.class));
        }
    }

    private void loginSignUpUser(String email, String password) {
        if(loginModeActive)
        {
            if(emailEditText.getText().toString().trim().equals(""))
            {
                Toast.makeText(SignInActivity.this, "Email is not empty",
                        Toast.LENGTH_SHORT).show();
            }
            else if(passwordEditText.getText().toString().trim().length()>7)
            {
                Toast.makeText(SignInActivity.this, "Passwords must be at least 7 characters",
                        Toast.LENGTH_SHORT).show();
            }
            else {
                mAuth.signInWithEmailAndPassword(email, password)
                        .addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
                            @Override
                            public void onComplete(Task<AuthResult> task) {
                                if (task.isSuccessful()) {
                                    // Sign in success, update UI with the signed-in user's information
                                    Log.d(TAG, "createUserWithEmail:success");
                                    FirebaseUser user = mAuth.getCurrentUser();

                                    Intent intent=new Intent(SignInActivity.this, MainActivity.class);
                                    intent.putExtra("UserName", nameEditText.getText().toString().trim() );
                                    // updateUI(user);
                                    startActivity(intent);
                                } else {
                                    // If sign in fails, display a message to the user.
                                    Log.w(TAG, "createUserWithEmail:failure", task.getException());
                                    Toast.makeText(SignInActivity.this, "Authentication failed.",
                                            Toast.LENGTH_SHORT).show();
                                    // updateUI(null);
                                }
                            }
                        });
            }
        }
        else
        {
            if(passwordEditText.getText().toString().trim().equals(repeatPasswordEditiText.getText().toString().trim())) {
                mAuth.createUserWithEmailAndPassword(email, password)
                        .addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
                            @Override
                            public void onComplete(Task<AuthResult> task) {
                                if (task.isSuccessful()) {
                                    // Sign in success, update UI with the signed-in user's information
                                    Log.d(TAG, "createUserWithEmail:success");
                                    FirebaseUser user = mAuth.getCurrentUser();

                                    Intent intent=new Intent(SignInActivity.this, MainActivity.class);
                                    intent.putExtra("UserName", nameEditText.getText().toString().trim() );
                                    // updateUI(user);
                                    startActivity(new Intent(SignInActivity.this, MainActivity.class));
                                } else {
                                    // If sign in fails, display a message to the user.
                                    Log.w(TAG, "createUserWithEmail:failure", task.getException());
                                    Toast.makeText(SignInActivity.this, "Authentication failed.",
                                            Toast.LENGTH_SHORT).show();
                                    // updateUI(null);
                                }
                            }
                        });
            }
            else if(emailEditText.getText().toString().trim().equals(""))
            {
                Toast.makeText(SignInActivity.this, "Email is not empty",
                        Toast.LENGTH_SHORT).show();
            }
            else if(passwordEditText.getText().toString().trim().length()>7)
            {
                Toast.makeText(SignInActivity.this, "Passwords must be at least 7 characters",
                        Toast.LENGTH_SHORT).show();
            }

            else
            {
                Toast.makeText(SignInActivity.this, "Passwords is not match",
                        Toast.LENGTH_SHORT).show();
            }
        }

    }

    private void createUser(FirebaseUser firebaseUser) {
        User user=new User();
        user.setId(firebaseUser.getUid());
        user.setEmail(firebaseUser.getEmail());
        user.setName(nameEditText.getText().toString().trim());
        databaseReference.push().setValue(user);

    }

    @Override
    public void onStart() {
        super.onStart();
        // Check if user is signed in (non-null) and update UI accordingly.
        FirebaseUser currentUser = mAuth.getCurrentUser();
        if(currentUser != null){
            currentUser.reload();
        }
    }

    public void toggleLogin(View view) {
        if(loginModeActive)
        {
            loginModeActive=false;
            loginSignUpButton.setText("Sign Up");
            toggleLoginSingUpTextView.setText("Or Log In");
            repeatPasswordEditiText.setVisibility(View.VISIBLE);
        }
        else
        {
            loginModeActive=true;
            loginSignUpButton.setText("Login");
            toggleLoginSingUpTextView.setText("Or Sign Up");
            repeatPasswordEditiText.setVisibility(View.GONE);
        }

    }
}