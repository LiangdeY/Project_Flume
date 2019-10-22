package comp5216.sydney.edu.au.project_flume;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

public class SettingActivity extends AppCompatActivity {

    Button cancelBtn, applyBtn;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_setting);

        cancelBtn = findViewById(R.id.cancel_btn_setting);
        applyBtn = findViewById(R.id.apply_btn_setting);

        Intent i = getIntent();
        final String fromActivity = i.getStringExtra("from");

        cancelBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(fromActivity.equals("Home")) {
                    startActivity(new Intent(SettingActivity.this, HomeActivity.class));
                }
                if(fromActivity.equals("Chat")) {
                    startActivity(new Intent(SettingActivity.this, ChatActivity.class));
                }
            }
        });

        applyBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                saveChanges();
                if(fromActivity.equals("Home")) {
                    startActivity(new Intent(SettingActivity.this, HomeActivity.class));
                }
                if(fromActivity.equals("Chat")) {
                    startActivity(new Intent(SettingActivity.this, ChatActivity.class));
                }
            }
        });
    }

    private void saveChanges(){

    }






}
