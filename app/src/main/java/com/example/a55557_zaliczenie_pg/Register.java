package com.example.a55557_zaliczenie_pg;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;


import com.google.android.gms.tasks.OnSuccessListener;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.HashMap;
import java.util.Map;

public class Register extends AppCompatActivity {

    //variables declaration

    EditText xPersonName,xEmail,xPassword,xPhone;
    Button xRegisterButton;
    TextView xLoginButton;
    FirebaseAuth fAuth;
    ProgressBar progressBar;
    String userID;
    FirebaseFirestore fStore;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);

        //class constructors

        xPersonName = findViewById(R.id.FullName);
        xEmail = findViewById(R.id.Email);
        xPassword = findViewById(R.id.Password);
        xPhone = findViewById(R.id.Phone);
        xRegisterButton = findViewById(R.id.registerButton);
        xLoginButton = findViewById(R.id.loginButton);

        fAuth = FirebaseAuth.getInstance();
        fStore = FirebaseFirestore.getInstance();
        progressBar = findViewById(R.id.progressBar);



        //already logged user check

        if(fAuth.getCurrentUser() != null) {
            startActivity(new Intent(getApplicationContext(), MainActivity.class));
            finish();
        }

        //already registered - click here - logging page redirection
        xRegisterButton.setOnClickListener((v) -> {
            String email = xEmail.getText().toString().trim();
            String password = xPassword.getText().toString().trim();


            if(TextUtils.isEmpty(email)){
                xEmail.setError("Email is required!");
                return;
            }
            if(TextUtils.isEmpty(password)){
                xPassword.setError("Password is required!");
            }
        });


        //button register handling and field validation
        xRegisterButton.setOnClickListener(view -> {
            String email = xEmail.getText().toString().trim();
            String password = xPassword.getText().toString().trim();
            String phone = xPhone.getText().toString();
            String fullName = xPersonName.getText().toString();

            if(TextUtils.isEmpty(email)) {
                xEmail.setError("Email is required!");
                return;

            }

            if(TextUtils.isEmpty(password)) {
                xPassword.setError("Password is required!");
                return;
            }

            if (password.length() < 7) {
                xPassword.setError("Password must contain at least 7 characters");
            }
            if (xPhone.length() < 9) {
                xPhone.setError("Phone must contain at least 9 digits");
            }

            progressBar.setVisibility(View.VISIBLE);

            // user registration in the firebase
            fAuth.createUserWithEmailAndPassword(email, password).addOnCompleteListener(task -> {
                if(task.isSuccessful()) {



                    Toast.makeText(Register.this, "User Created", Toast.LENGTH_SHORT).show();

                    //get user registered data and display on mainActivity after successful logging
                        userID = fAuth.getCurrentUser().getUid();
                        DocumentReference documentReference = fStore.collection("users").document(userID);

                        Map<String, Object> user = new HashMap<>();
                        user.put("fName", fullName);
                        user.put("email", email);
                        user.put("phone", phone);
                        documentReference.set(user).addOnSuccessListener(aVoid -> Log.e("TAG","onSuccess: user profile is created for "+ userID));


                    startActivity(new Intent(getApplicationContext(), MainActivity.class));
                }
//                else {
//                    Toast.makeText(Register.this, "Error! "+ task.getException().getMessage(), Toast.LENGTH_SHORT).show();
//                    progressBar.setVisibility(View.GONE);
//                }

            });

        });

        xLoginButton.setOnClickListener(view -> startActivity(new Intent(getApplicationContext(), Login.class)));


    }
}