package com.example.a55557_zaliczenie_pg;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.KeyEvent;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;

public class Login extends AppCompatActivity {

    //variables definition

     EditText xEmail, xPassword;
     Button xLoginButton;
     TextView xCreateButton, xForgotPw;
     ProgressBar progressBar;
     FirebaseAuth fAuth;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        // class constructors

        xEmail = findViewById(R.id.Email);
        xPassword = findViewById(R.id.Password);
        progressBar = findViewById(R.id.progressBar);
        fAuth = FirebaseAuth.getInstance();
        xLoginButton = findViewById(R.id.loginButton);
        xCreateButton = findViewById(R.id.createText);
        xForgotPw = findViewById(R.id.forgotPassword);

        //login button handling and field validation

        xLoginButton.setOnClickListener(view -> {

            String email = xEmail.getText().toString().trim();
            String password = xPassword.getText().toString().trim();

            if(TextUtils.isEmpty(email)) {
                xEmail.setError("Email is required!");
                return;

            }

            if(TextUtils.isEmpty(password)) {
                xPassword.setError("Password is required!");
                return;
            }


            progressBar.setVisibility(View.VISIBLE);

            //user authentication

            fAuth.signInWithEmailAndPassword(email, password).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                @Override
                public void onComplete(@NonNull Task<AuthResult> task) {

                    if(task.isSuccessful()){

                        Toast.makeText(Login.this, "Logged successfully", Toast.LENGTH_SHORT).show();
                        startActivity(new Intent(getApplicationContext(), MainActivity.class));



                    }
                    else {
                        Toast.makeText(Login.this, "Error! "+ task.getException().getMessage(), Toast.LENGTH_SHORT).show();
                        progressBar.setVisibility(View.GONE);
                    }
                }
            });
        });

        //password resseting handling

        xCreateButton.setOnClickListener(view -> startActivity(new Intent(getApplicationContext(), Register.class)));

        xForgotPw.setOnClickListener(view -> {

            EditText resetMail = new EditText(view.getContext());
            AlertDialog.Builder passwordResetDialog = new AlertDialog.Builder(view.getContext());

            passwordResetDialog.setTitle("Reset Password ?");
            passwordResetDialog.setMessage("Enter your email to receive pw resetting link");
            passwordResetDialog.setView(resetMail);




            //if user click yes - resend email
            passwordResetDialog.setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialogInterface, int i) {


                    //to be added email validation - if said yes - email needs to be required! otherwise it crashes
                    String email = xEmail.getText().toString().trim();
                    if(TextUtils.isEmpty(email)){

                        xEmail.setError("Email is required!");
                        return;

                    }


                    //email extraction
                    String getMail = resetMail.getText().toString();


                    //failure email reset handling
                    fAuth.sendPasswordResetEmail(getMail).addOnSuccessListener(unused -> Toast.makeText(Login.this, "Reset link sent to your email.", Toast.LENGTH_SHORT).show()).addOnFailureListener(new OnFailureListener() {
                        @Override
                        public void onFailure(@NonNull Exception e) {
                            Toast.makeText(Login.this, "Error, reset link was not sent" + e.getMessage(), Toast.LENGTH_SHORT).show();
                        }
                    });

                }

        });

            //if user click no- back to logging view
            passwordResetDialog.setNegativeButton("No", new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialogInterface, int i) {

                }
            });

            passwordResetDialog.create().show();


        });




    }
}