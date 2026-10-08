package com.example.myapplication;

import android.content.Intent;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Bundle;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.resource.drawable.DrawableTransitionOptions;
import com.google.android.material.appbar.MaterialToolbar;
import com.google.android.material.textfield.TextInputEditText;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.SetOptions;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;

import java.util.HashMap;
import java.util.Map;

public class ProfileActivity extends AppCompatActivity {

    private static final int PICK_IMAGE = 100;
    private static final int PERMISSION_REQUEST = 101;

    private ImageView imageProfile;
    private TextInputEditText username, email, country;
    private Button btnEdit, btnChangePhoto;

    private FirebaseAuth mAuth;
    private FirebaseFirestore db;
    private StorageReference storageRef;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_profile);


        mAuth = FirebaseAuth.getInstance();
        db = FirebaseFirestore.getInstance();
        storageRef = FirebaseStorage.getInstance().getReference();

        FirebaseUser user = mAuth.getCurrentUser();
        if (user == null) {
            startActivity(new Intent(this, LoginActivity.class));
            finish();
            return;
        }


        imageProfile = findViewById(R.id.image);
        username = findViewById(R.id.username);
        email = findViewById(R.id.email);
        country = findViewById(R.id.country);
        btnEdit = findViewById(R.id.edit);
        btnChangePhoto = findViewById(R.id.change_photo);

        MaterialToolbar toolbar = findViewById(R.id.topAppBar);
        if (toolbar != null) {
            setSupportActionBar(toolbar);
            toolbar.setNavigationOnClickListener(v -> onBackPressed());
        }


        email.setText(user.getEmail());


        ensureUserDocumentExists(user.getUid());


        loadUserProfile(user.getUid());


        btnChangePhoto.setOnClickListener(v -> pickImageFromGallery());
        btnEdit.setOnClickListener(v -> {
            Intent intent = new Intent(ProfileActivity.this, EditProfileActivity.class);
            startActivityForResult(intent, 101);
        });
    }

    @Override
    protected void onResume() {
        super.onResume();
        FirebaseUser user = mAuth.getCurrentUser();
        if (user != null) {
            loadUserProfile(user.getUid());
        }
    }


    private void ensureUserDocumentExists(String uid) {
        db.collection("users").document(uid)
                .get()
                .addOnSuccessListener(doc -> {
                    if (!doc.exists()) {
                        Map<String, Object> defaultData = new HashMap<>();
                        defaultData.put("username", "");
                        defaultData.put("country", "");
                        defaultData.put("profileImage", "");
                        db.collection("users").document(uid).set(defaultData);
                    }
                })
                .addOnFailureListener(e -> e.printStackTrace());
    }


    private void loadUserProfile(String uid) {
        db.collection("users").document(uid).get()
                .addOnSuccessListener(doc -> {
                    if (doc.exists()) {
                        username.setText(doc.getString("username"));
                        country.setText(doc.getString("country"));

                        String imageUrl = doc.getString("profileImage");
                        if (imageUrl != null && !imageUrl.isEmpty()) {
                            Glide.with(this)
                                    .load(imageUrl)
                                    .placeholder(R.drawable.userprofile)
                                    .transition(DrawableTransitionOptions.withCrossFade())
                                    .into(imageProfile);
                        }
                    }
                })
                .addOnFailureListener(Throwable::printStackTrace);
    }


    private void pickImageFromGallery() {
        if (checkSelfPermission(android.Manifest.permission.READ_MEDIA_IMAGES)
                != PackageManager.PERMISSION_GRANTED) {
            requestPermissions(new String[]{android.Manifest.permission.READ_MEDIA_IMAGES}, PERMISSION_REQUEST);
        } else {
            openGallery();
        }
    }

    private void openGallery() {
        Intent intent = new Intent(Intent.ACTION_PICK);
        intent.setType("image/*");
        startActivityForResult(intent, PICK_IMAGE);
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, String[] permissions, int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        if (requestCode == PERMISSION_REQUEST) {
            if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                openGallery();
            } else {
                Toast.makeText(this, "Permission denied. Cannot pick image.", Toast.LENGTH_SHORT).show();
            }
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        FirebaseUser user = mAuth.getCurrentUser();
        if (user == null) return;


        if (requestCode == PICK_IMAGE && resultCode == RESULT_OK && data != null && data.getData() != null) {
            Uri imageUri = data.getData();
            imageProfile.setImageURI(imageUri);
            uploadImageToFirebase(imageUri, user.getUid());
        }


        if (requestCode == 101 && resultCode == RESULT_OK) {
            loadUserProfile(user.getUid());
        }
    }


    private void uploadImageToFirebase(Uri imageUri, String uid) {
        StorageReference imageRef = storageRef.child("profile_images/" + uid + ".jpg");

        imageRef.putFile(imageUri)
                .addOnSuccessListener(task -> imageRef.getDownloadUrl().addOnSuccessListener(uri -> {
                    Map<String, Object> updateMap = new HashMap<>();
                    updateMap.put("profileImage", uri.toString());
                    db.collection("users").document(uid).set(updateMap, SetOptions.merge());

                    Toast.makeText(this, "Profile image updated", Toast.LENGTH_SHORT).show();
                }))
                .addOnFailureListener(e -> {
                    Toast.makeText(this, "Upload failed: " + e.getMessage(), Toast.LENGTH_LONG).show();
                    e.printStackTrace();
                });
    }
}
