package comp5216.sydney.edu.au.project_flume;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;

import com.google.firebase.auth.FirebaseAuth;

public class SettingActivity extends AppCompatActivity {


    Button backBtn, setProfileBtn, setPrograssBtn, signOutBtn;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_setting);

        InitUI();
    }

    private void InitUI() {
        Intent i = getIntent();
        final String fromActivity = i.getStringExtra("from");
        backBtn = findViewById(R.id.backBtn_setting);
        setProfileBtn = findViewById(R.id.profile_setting);
        setPrograssBtn = findViewById(R.id.set_progress_setting);
        signOutBtn = findViewById(R.id.sign_out_setting);

        backBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(fromActivity.equals("Home")) {
                    startActivity(new Intent(SettingActivity.this,
                            HomeActivity.class));
                }
                if(fromActivity.equals("Chat")) {
                    startActivity(new Intent(SettingActivity.this,
                            ChatActivity.class));
                }
            }
        });
        setProfileBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(SettingActivity.this, ProfileActivity.class));
            }
        });
        setPrograssBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(SettingActivity.this, SetProgressActivity.class));
            }
        });
        signOutBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                FirebaseAuth.getInstance().signOut();
                startActivity( new Intent(SettingActivity.this, MainActivity.class));
            }
        });



    }

}
