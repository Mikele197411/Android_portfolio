package com.mshilkov.chatfuture;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.text.Editable;
import android.text.InputFilter;
import android.text.TextWatcher;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ListView;
import android.widget.ProgressBar;

import com.google.android.gms.tasks.Continuation;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;

import java.util.ArrayList;
import java.util.List;

public class ChatActivity extends AppCompatActivity {

    private ListView messages;
    private MessageAdapter adapter;
    private ProgressBar progressBar;
    private ImageButton sendImageButton;
    private Button sendMessageButton;
    private EditText messageEditText;
    private  String  username;
    private static  final int RC_IMAGE_PICKER=124;
    FirebaseDatabase database;
    DatabaseReference databaseReference ;
    ChildEventListener messsagesChildEventListener;
    DatabaseReference usersDattabaseRef;
    ChildEventListener usersChildEventListener;
    FirebaseStorage fbStorage;
    StorageReference chatImageStorageReference;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_chat);


        database=FirebaseDatabase.getInstance();
        databaseReference= database.getReference().child("messages");
        usersDattabaseRef= database.getReference().child("users");
        Intent intent =getIntent();
        if(intent!=null)
        {
            username=intent.getStringExtra("UserName");
        }
        else {
            username = "Default User";
        }
        messages=findViewById(R.id.messagesListView);
        progressBar=findViewById(R.id.progressBar);
        sendImageButton=findViewById(R.id.sendPhotoButton);
        sendMessageButton=findViewById(R.id.sendMessageButton);
        messageEditText=findViewById(R.id.messageEditText);

        List<Message> listMessages=new ArrayList<>();
        adapter=new MessageAdapter(this, R.layout.message_item, listMessages);
        messages.setAdapter(adapter);
        fbStorage=FirebaseStorage.getInstance();
        chatImageStorageReference=fbStorage.getReference().child("ChatImages");
        progressBar.setVisibility(ProgressBar.INVISIBLE);


        messageEditText.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                 if(s.toString().trim().length()>0)
                 {
                     sendMessageButton.setEnabled(true);
                 }
                 else
                 {
                     sendMessageButton.setEnabled(false);
                 }
            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });
        messageEditText.setFilters(new InputFilter[] {new InputFilter.LengthFilter(500) });
        sendMessageButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Message message=new Message();
                message.setName(username);
                message.setText(messageEditText.getText().toString());
                message.setImageUrl(null);
                databaseReference.push().setValue(message);
                messageEditText.setText("");

            }
        });
        sendImageButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent=new Intent(Intent.ACTION_GET_CONTENT);
                intent.setType("image/*");
                intent.putExtra(Intent.EXTRA_LOCAL_ONLY, true);
                startActivityForResult(Intent.createChooser(intent, "Choose an image"), RC_IMAGE_PICKER );

            }
        });
        usersChildEventListener=new ChildEventListener() {
            @Override
            public void onChildAdded(@NonNull DataSnapshot snapshot, @Nullable String previousChildName) {
                User user=snapshot.getValue(User.class);
                if(user.getId().equals(FirebaseAuth.getInstance().getCurrentUser().getUid()))
                {
                    username=user.getName();
                }
            }

            @Override
            public void onChildChanged(@NonNull DataSnapshot snapshot, @Nullable String previousChildName) {

            }

            @Override
            public void onChildRemoved(@NonNull DataSnapshot snapshot) {

            }

            @Override
            public void onChildMoved(@NonNull DataSnapshot snapshot, @Nullable String previousChildName) {

            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        };
        usersDattabaseRef.addChildEventListener(usersChildEventListener);
        messsagesChildEventListener= new ChildEventListener() {
            @Override
            public void onChildAdded(@NonNull DataSnapshot snapshot, @Nullable String previousChildName) {
                Message message=snapshot.getValue(Message.class);
                adapter.add(message);
            }

            @Override
            public void onChildChanged(@NonNull DataSnapshot snapshot, @Nullable String previousChildName) {

            }

            @Override
            public void onChildRemoved(@NonNull DataSnapshot snapshot) {

            }

            @Override
            public void onChildMoved(@NonNull DataSnapshot snapshot, @Nullable String previousChildName) {

            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        };
        databaseReference.addChildEventListener(messsagesChildEventListener);

    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater=getMenuInflater();
        inflater.inflate(R.menu.main_menu, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        switch (item.getItemId())
        {
            case R.id.signOut:
                FirebaseAuth.getInstance().signOut();
                startActivity(new Intent(ChatActivity.this, SignInActivity.class));
                return  true;
            default:
                return  super.onOptionsItemSelected(item);
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if(requestCode==RC_IMAGE_PICKER && requestCode==RESULT_OK)
        {
            Uri selectImageUri=data.getData();
            StorageReference imgReference=chatImageStorageReference.child(selectImageUri.getLastPathSegment());
            UploadTask uploadTask=imgReference.putFile(selectImageUri);
            Task<Uri> urlTask = uploadTask.continueWithTask(new Continuation<UploadTask.TaskSnapshot, Task<Uri>>() {
                @Override
                public Task<Uri> then(@NonNull Task<UploadTask.TaskSnapshot> task) throws Exception {
                    if (!task.isSuccessful()) {
                        throw task.getException();
                    }

                    // Continue with the task to get the download URL
                    return imgReference.getDownloadUrl();
                }
            }).addOnCompleteListener(new OnCompleteListener<Uri>() {
                @Override
                public void onComplete(@NonNull Task<Uri> task) {
                    if (task.isSuccessful()) {

                        Uri downloadUri = task.getResult();
                        Message message=new Message();
                        message.setImageUrl(downloadUri.toString());
                        message.setName(username);
                        databaseReference.push().setValue(message);
                    } else {
                        // Handle failures
                        // ...
                    }
                }
            });

        }
    }
}