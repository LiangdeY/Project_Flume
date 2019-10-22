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

public class MainActivity extends AppCompatActivity {

    Button login, signUp;
    FirebaseAuth auth;
    EditText email, password;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getSupportActionBar().hide();
        setContentView(R.layout.activity_main);

        auth = FirebaseAuth.getInstance();
        email = findViewById(R.id.email_login);
        password = findViewById(R.id.email_sign_up);

        login = findViewById(R.id.login_main);
        signUp = findViewById(R.id.signUp_main);
        login.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Login();
            }
        });
        signUp.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity( new Intent(MainActivity.this, SignUpActivity.class) );
            }
        });
    }
//    @Override
//    protected void onStart() {
//        super.onStart();
//
//        //check if usr exit
//        //auto login if user exit
//        user = FirebaseAuth.getInstance().getCurrentUser();
//        if(user != null) {
//            Log.d("user",user.getEmail().toString());
//
//            startActivity( new Intent(this, HomeActivity.class) );
//            finish();
//        }
//    }

    private void Login() {
        String sEmail= email.getText().toString();
        String sPassword = password.getText().toString();

        if(sEmail.isEmpty() || sPassword.isEmpty()){
            Toast.makeText(MainActivity.this, "all fields must be filled",
                    Toast.LENGTH_SHORT).show();
        }else{
            auth.signInWithEmailAndPassword(sEmail,sPassword).addOnCompleteListener(
                    new OnCompleteListener<AuthResult>() {
                        @Override
                        public void onComplete(@NonNull Task<AuthResult> task) {
                            if(task.isSuccessful()) {
                                Intent i = new Intent(MainActivity.this,
                                        HomeActivity.class);
                                i.putExtra("login", "1");
                                i.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK |
                                        Intent.FLAG_ACTIVITY_NEW_TASK);
                                startActivity(i);
                                finish();
                            }else {
                                Toast.makeText(MainActivity.this,
                                        "Authentication Fail!", Toast.LENGTH_SHORT).show();
                            }
                        }
                    });
        }
    }

}
