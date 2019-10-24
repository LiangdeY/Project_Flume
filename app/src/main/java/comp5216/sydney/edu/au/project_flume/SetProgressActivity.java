package comp5216.sydney.edu.au.project_flume;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import android.app.ProgressDialog;
import android.content.ContentResolver;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.webkit.MimeTypeMap;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.RadioButton;
import android.widget.RadioGroup;
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

public class SetProgressActivity extends AppCompatActivity {
    Button applyBtn;
    DatabaseReference currentUserRef;
    ImageView profileImage;
    StorageReference storageRef;
    String fromActivity;
    RadioButton radioButton;
    RadioGroup radioGroup;
    private static final int IMAGE_REQUEST = 100;
    private Uri imageURI;
    private StorageTask uploadTask;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_set_prograss);

        Intent i = getIntent();
        fromActivity = i.getStringExtra("from");

        InitUI();
        storageRef = FirebaseStorage.getInstance().getReference("uploads");
    }
    private void InitUI(){
        profileImage = findViewById(R.id.profile_image_progress);
        radioGroup = findViewById(R.id.radio_difficulty);
        FirebaseUser mFirebaseUser  = FirebaseAuth.getInstance().getCurrentUser();
        currentUserRef = FirebaseDatabase.getInstance().getReference("Users")
                .child(mFirebaseUser.getUid());

        applyBtn = findViewById(R.id.applyBtn_progress);
        applyBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                int selectedId = radioGroup.getCheckedRadioButtonId();
                radioButton = findViewById(selectedId);

                switch (radioButton.getText().toString()){

                    case "Easy": currentUserRef.child("progressMax").setValue(String.valueOf(20));
                        break;
                    case "Medium": currentUserRef.child("progressMax").setValue(String.valueOf(50));
                        break;
                    case "Hard": currentUserRef.child("progressMax").setValue(String.valueOf(100));
                        break;
                        default:
                }

                if(fromActivity!= null) {
                    startActivity( new Intent(SetProgressActivity.this, HomeActivity
                            .class));
                }else{
                    onBackPressed();
                }
            }
        });
        currentUserRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                User user = dataSnapshot.getValue(User.class);
                if(user.getImageUri().equals("default")) {

                    profileImage.setImageResource(R.mipmap.ic_launcher);

                }else{

                    Glide.with(SetProgressActivity.this).load(user.getImageUri())
                            .into(profileImage);
                }
            }
            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) { }
        });
        profileImage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                OpenImage();
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
        final ProgressDialog pDialog = new ProgressDialog(SetProgressActivity.this);
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
                        Toast.makeText(SetProgressActivity.this, "Upload success!",
                                Toast.LENGTH_SHORT).show();
                        pDialog.dismiss();

                    }else{
                        Toast.makeText(SetProgressActivity.this, "Upload image failed",
                                Toast.LENGTH_SHORT).show();
                        pDialog.dismiss();
                    }
                }
            }).addOnFailureListener(new OnFailureListener() {
                @Override
                public void onFailure(@NonNull Exception e) {
                    Toast.makeText(SetProgressActivity.this, e.getMessage() ,
                            Toast.LENGTH_SHORT).show();
                }
            });
        }else{
            Toast.makeText(SetProgressActivity.this, "No image selected",
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
                Toast.makeText(SetProgressActivity.this, "Uploading",
                        Toast.LENGTH_SHORT).show();
            } else{
                UploadImage();
            }
        }
    }
}
