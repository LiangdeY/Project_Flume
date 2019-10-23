package comp5216.sydney.edu.au.project_flume;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.HashMap;


public class SignUpActivity extends AppCompatActivity {
    Button goBackBtn;
    FirebaseAuth auth;
    DatabaseReference dbReference;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sign_up);

        auth = FirebaseAuth.getInstance();

        initUI();

        goBackBtn = findViewById(R.id.goBackBtn);
        goBackBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity( new Intent(SignUpActivity.this, MainActivity.class) );
                overridePendingTransition(android.R.anim.fade_in, android.R.anim.fade_out);
            }
        });

    }

    private void initUI() {
        final EditText username, email, password;
        Button signUpBtn;

        username = findViewById(R.id.user_name_sign_up);
        email = findViewById(R.id.email_sign_up);
        password = findViewById(R.id.password_sign_up);
        signUpBtn= findViewById(R.id.sign_up_button);
        signUpBtn.setOnClickListener( new View.OnClickListener(){
            @Override
            public void onClick(View v) {
                String sUsername = username.getText().toString();
                String sEmail= email.getText().toString();
                String sPassword = password.getText().toString();
                if(sUsername.isEmpty() || sEmail.isEmpty() || sPassword.isEmpty()){
                    Toast.makeText(SignUpActivity.this, "all fields must be filled",
                            Toast.LENGTH_SHORT).show();
                }else if(sPassword.length() < 6){
                    Toast.makeText(SignUpActivity.this, "password need has at least" +
                                    " 6 characters",
                            Toast.LENGTH_SHORT).show();
                }
                else{
                    signUp(sUsername, sEmail, sPassword);
                }
            }
        } );

        email.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View v, boolean hasFocus) {
                if (!hasFocus) {
                    InputMethodManager inputMethodManager =(InputMethodManager)getSystemService(MainActivity.INPUT_METHOD_SERVICE);
                    inputMethodManager.hideSoftInputFromWindow(v.getWindowToken(), 0);
                }
            }
        });

        username.setOnFocusChangeListener(new View.OnFocusChangeListener() {
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

    private void signUp(final String username, String email, String password) {
        final ProgressDialog pDialog = new ProgressDialog(SignUpActivity.this);
        pDialog.setMessage("Signing up..");
        pDialog.show();
        auth.createUserWithEmailAndPassword(email, password)
                .addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if(task.isSuccessful()) {

                            FirebaseUser firebaseUser = auth.getCurrentUser();
                            String userId = firebaseUser.getUid();

                            dbReference = FirebaseDatabase.getInstance().getReference("Users")
                                    .child(userId);
                            HashMap<String, String> hashMap = new HashMap<>();
                            hashMap.put("id", userId);
                            hashMap.put("username", username);
                            hashMap.put("imageUri", "default");
                            hashMap.put("isMatch", "N");
                            hashMap.put("matchId", "N");
                            hashMap.put("progressMax", String.valueOf(0));
                            hashMap.put("upLocked", "N");

                            dbReference.setValue(hashMap).addOnCompleteListener(
                                    new OnCompleteListener<Void>() {
                                @Override
                                public void onComplete(@NonNull Task<Void> task) {
                                    if(task.isSuccessful()) {
                                        Intent i = new Intent(SignUpActivity.this,
                                                HomeActivity.class) ;
                                        i.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK |
                                                Intent.FLAG_ACTIVITY_NEW_TASK);
                                        pDialog.dismiss();
                                        startActivity(i);
<<<<<<< HEAD
                                        Log.w("", "createUserWithEmail:Success",
                                                task.getException());
=======
                                        overridePendingTransition(android.R.anim.fade_in, android.R.anim.fade_out);
                                        Log.w("", "createUserWithEmail:Success", task.getException());
>>>>>>> 132a9b4fbb9041524293a3ff31008d50a23df3fe
                                        finish();
                                    }
                                    else{
                                        Log.w("", "createHashmap:failure",
                                                task.getException());
                                    }
                                }
                            });
                        }else{
                            pDialog.dismiss();
                            Toast.makeText(SignUpActivity.this, "Fail to sign up",
                                    Toast.LENGTH_SHORT).show();
                            Log.w("", "createUserWithEmail:failure", task.getException());
                        }
                    }
                });
    }




}
