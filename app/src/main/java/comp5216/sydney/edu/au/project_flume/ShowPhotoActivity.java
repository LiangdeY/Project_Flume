package comp5216.sydney.edu.au.project_flume;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import comp5216.sydney.edu.au.project_flume.Model.User;

public class ShowPhotoActivity extends AppCompatActivity {

    ImageView photo;
    Button backBtn;
    String targetUserId;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_show_photo);

        photo = findViewById(R.id.photo_show_photo);
        backBtn = findViewById(R.id.backBtn_showPhoto);

        Intent intent = getIntent();
        targetUserId = intent.getStringExtra("targetUserId");
        DatabaseReference targetUserRef = FirebaseDatabase.getInstance().getReference("Users")
                .child(targetUserId);

        targetUserRef.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                User targetUserModel = dataSnapshot.getValue(User.class);
                if(targetUserModel.getUnLocked().equals("Y")){
                    if(targetUserModel.getImageUri().equals("default")) {
                        photo.setImageResource(R.mipmap.ic_launcher);
                    }
                    else{
                        Glide.with(getApplicationContext()).load(targetUserModel.getImageUri())
                                .into(photo);
                    }
                }
                else{
                    Toast.makeText(getApplicationContext(), "User Photo locked",
                            Toast.LENGTH_SHORT).show();
                }

            }
            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) { }
        });

        backBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(ShowPhotoActivity.this, ChatActivity
                        .class);
                intent.putExtra("targetId" ,targetUserId);
                startActivity(intent);
            }
        });
    }
}
