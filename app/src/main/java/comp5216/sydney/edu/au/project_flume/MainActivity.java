package comp5216.sydney.edu.au.project_flume;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;

public class MainActivity extends AppCompatActivity {

    Button login, signUp;
    FirebaseAuth auth;
    EditText email, password;
    TextView forgetPassword;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        auth = FirebaseAuth.getInstance();
        email = findViewById(R.id.email_login);
<<<<<<< HEAD
        password = findViewById(R.id.email_sign_up);
        forgetPassword = findViewById(R.id.forgetPassword_main);
        forgetPassword.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(MainActivity.this, ResetPasswordActivity.class));
            }
        });


=======
        password = findViewById(R.id.password_login);
>>>>>>> 4fd9b109a35ff1f13253ce772ed5df23454fa93b
        email.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View v, boolean hasFocus) {
                if (!hasFocus) {
                    InputMethodManager inputMethodManager =(InputMethodManager)getSystemService(
                            MainActivity.INPUT_METHOD_SERVICE);
                    inputMethodManager.hideSoftInputFromWindow(v.getWindowToken(), 0);
                }
            }
        });
        password.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View v, boolean hasFocus) {
                if (!hasFocus) {
                    InputMethodManager inputMethodManager =(InputMethodManager)getSystemService(
                            MainActivity.INPUT_METHOD_SERVICE);
                    inputMethodManager.hideSoftInputFromWindow(v.getWindowToken(), 0);
                }
            }
        });

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
                overridePendingTransition(android.R.anim.fade_in, android.R.anim.fade_out);
            }
        });

        email.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View v, boolean hasFocus) {
                if (!hasFocus) {
                    InputMethodManager inputMethodManager =(InputMethodManager)getSystemService(MainActivity.INPUT_METHOD_SERVICE);
                    inputMethodManager.hideSoftInputFromWindow(v.getWindowToken(), 0);
                }
            }
        });

        password.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View v, boolean hasFocus) {
                if (!hasFocus) {
                    InputMethodManager inputMethodManager =(InputMethodManager)getSystemService(MainActivity.INPUT_METHOD_SERVICE);
                    inputMethodManager.hideSoftInputFromWindow(v.getWindowToken(), 0);
                }
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
        final ProgressDialog pDialog = new ProgressDialog(MainActivity.this);
        pDialog.setMessage("Logging..");
        pDialog.show();
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
                                pDialog.dismiss();
                                Intent i = new Intent(MainActivity.this,
                                        HomeActivity.class);
                                i.putExtra("login", "1");
                                i.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK |
                                        Intent.FLAG_ACTIVITY_NEW_TASK);
                                startActivity(i);
                                overridePendingTransition(R.anim.fade_in, R.anim.fade_out);
                                finish();
                            }else {
                                pDialog.dismiss();
                                Toast.makeText(MainActivity.this,
                                        "Authentication Fail!", Toast.LENGTH_SHORT).show();
                            }
                        }
                    });
        }
    }

}
