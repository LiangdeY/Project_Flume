package comp5216.sydney.edu.au.project_flume;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;

public class LoginActivity extends AppCompatActivity {
    FirebaseAuth auth;
    EditText email, password;
    Button loginBtn;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        auth = FirebaseAuth.getInstance();
        email = findViewById(R.id.email_login);
        password = findViewById(R.id.password_login);
        loginBtn = findViewById(R.id.loginBtn);

        loginBtn.setOnClickListener( new View.OnClickListener(){
            @Override
            public void onClick(View v) {
                String sEmail= email.getText().toString();
                String sPassword = password.getText().toString();

                if(sEmail.isEmpty() || sPassword.isEmpty()){
                    Toast.makeText(LoginActivity.this, "all fields must be filled",
                            Toast.LENGTH_SHORT).show();
                }else{
                    auth.signInWithEmailAndPassword(sEmail,sPassword)
                            .addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                                @Override
                                public void onComplete(@NonNull Task<AuthResult> task) {
                                    if(task.isSuccessful()) {
                                        Intent i = new Intent(LoginActivity.this,
                                                HomeActivity.class);
                                        i.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK |
                                                Intent.FLAG_ACTIVITY_NEW_TASK);
                                        startActivity(i);
                                        finish();
                                    }else {
                                        Toast.makeText(LoginActivity.this,
                                                "Authentication Faild!", Toast.LENGTH_SHORT)
                                                .show();
                                    }
                                }
                            });

                }
            }
        } );

    }
}
