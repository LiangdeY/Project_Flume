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
import android.view.inputmethod.InputMethodManager;
import android.webkit.MimeTypeMap;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.Switch;
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
    ImageView avatar;
    EditText username;
    Button applyBtn, cancelBtn;

    RadioButton radioSexButton;
    RadioGroup radioSexGroup;

    FirebaseUser fUser;
    DatabaseReference currentUserRef;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_profile);

        InitUI();
        GetUserDataOnce();
    }

    private void InitUI() {
        fUser = FirebaseAuth.getInstance().getCurrentUser();
        currentUserRef = FirebaseDatabase.getInstance().getReference("Users")
                .child(fUser.getUid());

        avatar = findViewById(R.id.avatars_image);
        //TODO set a button to allow user to choose gender
        //TODO get the gender base on
        username = findViewById(R.id.username_profile);
        applyBtn = findViewById(R.id.applyBtn_profile);
        cancelBtn = findViewById(R.id.cancelBtn_profile);
        radioSexGroup =  findViewById(R.id.radioSex);

        applyBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                SaveChanges();
                startActivity( new Intent( ProfileActivity.this, SettingActivity
                        .class));
            }
        });
        cancelBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity( new Intent( ProfileActivity.this, SettingActivity
                        .class));
            }
        });


       // profileImage.setImageResource(R.mipmap.ic_launcher);
    }
    private void GetUserDataOnce(){
        currentUserRef.addListenerForSingleValueEvent(new ValueEventListener() {

            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                User user = dataSnapshot.getValue(User.class);
                username.setText(user.getUsername());
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) { }
        });
    }

    private void SaveChanges(){
        int selectedId = radioSexGroup.getCheckedRadioButtonId();
        radioSexButton = findViewById(selectedId);
        
    }

}
