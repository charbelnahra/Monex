package com.example.myapplication;

import android.os.Bundle;
import android.widget.Button;
import android.widget.EditText;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.myapplication.model.Message;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.DocumentChange;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.Query;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.Locale;

public class ChatActivity extends AppCompatActivity {

    private RecyclerView recyclerChat;
    private EditText etMessage;
    private Button btnSend;
    private Toolbar toolbar;

    private ArrayList<Message> messageList;
    private MessageAdapter adapter;

    private FirebaseFirestore db;
    private FirebaseUser user;
    private StorageReference storageRef;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_chat);
        recyclerChat = findViewById(R.id.recyclerChat);
        etMessage = findViewById(R.id.etMessage);
        btnSend = findViewById(R.id.btnSend);
        // UI
        toolbar = findViewById(R.id.topAppBarChat);
        if (toolbar != null) {
            setSupportActionBar(toolbar);
            if (getSupportActionBar() != null) {
                getSupportActionBar().setTitle("Chat Pool");
            }
        }



        db = FirebaseFirestore.getInstance();
        storageRef = FirebaseStorage.getInstance().getReference();
        user = FirebaseAuth.getInstance().getCurrentUser();
        if (user == null) {
            finish(); // User not logged in
            return;
        }


        messageList = new ArrayList<>();
        adapter = new MessageAdapter(messageList);
        LinearLayoutManager layoutManager = new LinearLayoutManager(this);
        recyclerChat.setLayoutManager(layoutManager);
        recyclerChat.setAdapter(adapter);

        db.collection("chat")
                .orderBy("timestamp", Query.Direction.ASCENDING)
                .addSnapshotListener((snapshots, e) -> {
                    if (e != null || snapshots == null) return;

                    for (DocumentChange dc : snapshots.getDocumentChanges()) {
                        if (dc.getType() == DocumentChange.Type.ADDED) {

                            String text = dc.getDocument().getString("text");
                            String senderId = dc.getDocument().getString("senderId");
                            String time = dc.getDocument().getString("time");
                            String profileUrl = dc.getDocument().getString("profileImage");

                            if (text == null) continue;

                            boolean isMe = senderId != null && senderId.equals(user.getUid());

                            Message msg = new Message(text, time, isMe);
                            msg.setProfileImage(profileUrl);

                            messageList.add(msg);
                        }
                    }

                    adapter.notifyDataSetChanged();

                    // SAFE scrolling
                    int lastPosition = adapter.getItemCount() - 1;
                    if (lastPosition >= 0 && layoutManager != null) {
                        recyclerChat.post(() -> recyclerChat.smoothScrollToPosition(lastPosition));
                    }
                });



        btnSend.setOnClickListener(v -> {
            String text = etMessage.getText().toString().trim();
            if (text.isEmpty()) return;

            String time = new SimpleDateFormat("hh:mm a", Locale.getDefault()).format(new Date());

            db.collection("users")
                    .document(user.getUid())
                    .get()
                    .addOnSuccessListener(doc -> {
                        String profileUrl = null;
                        if (doc.exists()) {
                            profileUrl = doc.getString("profileImage");
                        }

                        MessageData message = new MessageData(
                                user.getUid(), text, time, profileUrl, System.currentTimeMillis()
                        );

                        db.collection("chat")
                                .add(message)
                                .addOnSuccessListener(ref -> {
                                    etMessage.setText("");
                                })
                                .addOnFailureListener(err -> {
                                    err.printStackTrace();
                                });

                    })
                    .addOnFailureListener(err -> {
                        err.printStackTrace();
                    });
        });
    }


    public static class MessageData {
        public String senderId;
        public String text;
        public String time;
        public String profileImage;
        public long timestamp;

        public MessageData() {}

        public MessageData(String senderId, String text, String time, String profileImage, long timestamp) {
            this.senderId = senderId;
            this.text = text;
            this.time = time;
            this.profileImage = profileImage;
            this.timestamp = timestamp;
        }
    }
}
