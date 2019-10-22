package comp5216.sydney.edu.au.project_flume;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import android.app.ProgressDialog;
import android.content.ContentResolver;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;
import android.webkit.MimeTypeMap;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.google.android.gms.tasks.Continuation;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.StorageTask;
import com.google.firebase.storage.UploadTask;

import java.util.HashMap;

import comp5216.sydney.edu.au.project_flume.Model.User;

public class ProfileActivity extends AppCompatActivity {

    ImageView profile_image;
    EditText username;
    DatabaseReference currentUserRef;
    FirebaseUser fUser;

    Button cancelBtn, applyBtn;

    StorageReference storageRef;

    private static final int IMAGE_REQUEST = 100;
     private Uri imageURI;
     private StorageTask uploadTask;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_profile);

        storageRef = FirebaseStorage.getInstance().getReference("uploads");

        InitUI();
    }

    private void InitUI() {
        
        cancelBtn = findViewById(R.id.cancelBtn_profile);
        applyBtn = findViewById(R.id.applyBtn_profile);
        profile_image = findViewById(R.id.profile_image_profile);
        username = findViewById(R.id.username_profile);
        fUser = FirebaseAuth.getInstance().getCurrentUser();
        currentUserRef = FirebaseDatabase.getInstance().getReference("Users")
                .child(fUser.getUid());

        currentUserRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                User user = dataSnapshot.getValue(User.class);
                username.setText(user.getUsername());
                if(user.getImageUri().equals("default")) {
                    profile_image.setImageResource(R.mipmap.ic_launcher);
                    Toast.makeText(ProfileActivity.this, "image url is default ",
                            Toast.LENGTH_SHORT).show();
                }else{
                    Glide.with(ProfileActivity.this).load(user.getImageUri())
                            .into(profile_image);
                }
            }
            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) { }
        });

        profile_image.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                OpenImage();
            }
        });

        cancelBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity( new Intent( ProfileActivity.this, SettingActivity.class));
            }
        });
        applyBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                SaveChanges();
                startActivity( new Intent( ProfileActivity.this, SettingActivity.class));
            }
        });

    }

    private void OpenImage() {

        Intent intent = new Intent();
        intent.setType("image/*");
        intent.setAction(Intent.ACTION_GET_CONTENT);
        startActivityForResult(intent, IMAGE_REQUEST);
    }

    private String GetFileExtension(Uri uri) {
        ContentResolver contentResolver = getContentResolver();
        MimeTypeMap mimeTypeMap = MimeTypeMap.getSingleton();
        return mimeTypeMap.getExtensionFromMimeType(contentResolver.getType(uri));
    }

    private void UploadImage() {
        final ProgressDialog pDialog = new ProgressDialog(ProfileActivity.this);
        pDialog.setMessage("Uploading");
        pDialog.show();

        if(imageURI != null) {
            final StorageReference imageReference = storageRef.child(System.currentTimeMillis()
                    + "." + GetFileExtension(imageURI) );

            uploadTask = imageReference.putFile(imageURI);
            uploadTask.continueWithTask(new Continuation<UploadTask.TaskSnapshot, Task<Uri>>() {
                @Override
                public Task<Uri> then(@NonNull Task<UploadTask.TaskSnapshot> task) throws Exception {
                    if(!task.isSuccessful()){
                        throw task.getException();
                    }
                    return imageReference.getDownloadUrl();
                }
            }).addOnCompleteListener(new OnCompleteListener<Uri>() {
                @Override
                public void onComplete(@NonNull Task<Uri> task) {
                    if(task.isSuccessful()){
                        Uri downloadUri = task.getResult();
                        String mUri = downloadUri.toString();

                        HashMap<String, Object> map = new HashMap<>();
                        map.put("imageUri", mUri);
                        currentUserRef.updateChildren(map);
                        pDialog.dismiss();

                    }else{
                        Toast.makeText(ProfileActivity.this, "Upload image failed" ,
                                Toast.LENGTH_SHORT).show();
                        pDialog.dismiss();
                    }
                }
            }).addOnFailureListener(new OnFailureListener() {
                @Override
                public void onFailure(@NonNull Exception e) {
                    Toast.makeText(ProfileActivity.this, e.getMessage() ,
                            Toast.LENGTH_SHORT).show();
                }
            });
        }else{
            Toast.makeText(ProfileActivity.this, "No image selected",
                    Toast.LENGTH_SHORT).show();
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if(requestCode == IMAGE_REQUEST && resultCode == RESULT_OK && data != null
                && data.getData() != null){
            imageURI = data.getData();

            if(uploadTask != null && uploadTask.isInProgress()) {
                Toast.makeText(ProfileActivity.this, "Uploading",
                        Toast.LENGTH_SHORT).show();
            } else{
                UploadImage();
            }
        }
    }

    private void SaveChanges(){

    }
}
