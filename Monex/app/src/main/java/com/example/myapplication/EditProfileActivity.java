package com.example.myapplication;

import android.os.Bundle;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.HashMap;
import java.util.Map;

public class EditProfileActivity extends AppCompatActivity {

    private EditText edtUsername, edtEmail, edtCountry;
    private Button btnSave;

    private FirebaseAuth mAuth;
    private FirebaseFirestore db;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit_profile);


        mAuth = FirebaseAuth.getInstance();
        db = FirebaseFirestore.getInstance();


        edtUsername = findViewById(R.id.edtUsername);
        edtEmail = findViewById(R.id.edtEmail);
        edtCountry = findViewById(R.id.edtCountry);
        btnSave = findViewById(R.id.btnSave);

        FirebaseUser user = mAuth.getCurrentUser();
        if (user == null) {
            finish();
            return;
        }


        loadUserData(user.getUid());

        btnSave.setOnClickListener(v -> saveProfile());
    }

    private void loadUserData(String uid) {
        db.collection("users")
                .document(uid)
                .get()
                .addOnSuccessListener(doc -> {
                    if (doc.exists()) {
                        edtUsername.setText(doc.getString("username"));
                        edtCountry.setText(doc.getString("country"));
                    }

                    FirebaseUser user = mAuth.getCurrentUser();
                    if (user != null) {
                        edtEmail.setText(user.getEmail());
                    }
                });
    }

    private void saveProfile() {
        String username = edtUsername.getText().toString().trim();
        String email = edtEmail.getText().toString().trim();
        String country = edtCountry.getText().toString().trim();

        if (username.isEmpty() || email.isEmpty() || country.isEmpty()) {
            Toast.makeText(this, "Please fill all fields", Toast.LENGTH_SHORT).show();
            return;
        }

        FirebaseUser user = mAuth.getCurrentUser();
        if (user == null) return;


        Map<String, Object> updates = new HashMap<>();
        updates.put("username", username);
        updates.put("country", country);

        db.collection("users")
                .document(user.getUid())
                .update(updates)
                .addOnSuccessListener(unused -> {


                    if (!email.equals(user.getEmail())) {
                        user.updateEmail(email)
                                .addOnSuccessListener(aVoid ->
                                        Toast.makeText(this, "Profile updated", Toast.LENGTH_SHORT).show()
                                )
                                .addOnFailureListener(e ->
                                        Toast.makeText(this, e.getMessage(), Toast.LENGTH_SHORT).show()
                                );
                    } else {
                        Toast.makeText(this, "Profile updated", Toast.LENGTH_SHORT).show();
                    }


                    setResult(RESULT_OK);
                    finish();
                })
                .addOnFailureListener(e ->
                        Toast.makeText(this, "Update failed: " + e.getMessage(), Toast.LENGTH_SHORT).show()
                );
    }
}
