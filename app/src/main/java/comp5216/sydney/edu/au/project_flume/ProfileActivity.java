package comp5216.sydney.edu.au.project_flume;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import android.app.ProgressDialog;
import android.content.ContentResolver;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
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
    EditText username, confirmPassword, changePassword;
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
        username = findViewById(R.id.username_profile);
        applyBtn = findViewById(R.id.applyBtn_profile);
        cancelBtn = findViewById(R.id.cancelBtn_profile);
        radioSexGroup =  findViewById(R.id.radioSex);
        changePassword =  findViewById(R.id.change_password_profile);
        confirmPassword =  findViewById(R.id.confirm_password_profile);
        avatar.setImageResource(R.drawable.avatar_male);

        applyBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                SaveChanges();
                onBackPressed();
            }
        });
        cancelBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onBackPressed();
            }
        });

        radioSexButton = findViewById(radioSexGroup.getCheckedRadioButtonId());
        radioSexGroup.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup group, int checkedId) {
                //change the avatar according to the button
                radioSexButton = findViewById(checkedId);
                if(radioSexButton.getText().toString().equals("Male")){
                    avatar.setImageResource(R.drawable.avatar_male);
                }else{
                    avatar.setImageResource(R.drawable.avatar_female);
                }
            }
        });
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
        currentUserRef.child("gender").setValue(radioSexButton.getText().toString());
        currentUserRef.child("username").setValue(username.getText().toString());

        if(ArePasswordsEqual()){
            if( !changePassword.getText().toString().equals("")){
                //both files are equal and not empty
                fUser.updatePassword(changePassword.getText().toString())
                        .addOnCompleteListener(new OnCompleteListener<Void>() {
                            @Override
                            public void onComplete(@NonNull Task<Void> task) {
                                if (task.isSuccessful()) {
                                    Toast.makeText(ProfileActivity.this, "Update success!",
                                            Toast.LENGTH_SHORT).show();
                                }
                            }
                        });

            }
        }else{
            Toast.makeText(ProfileActivity.this, "Both password fields must b filled",
                    Toast.LENGTH_SHORT).show();
        }

    }

    private boolean ArePasswordsEqual() {
        if(changePassword.getText().toString().equals(confirmPassword.getText().toString())) {
            return true;
        }
        return false;
    }

}
