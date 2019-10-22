package comp5216.sydney.edu.au.project_flume;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

public class MainActivity extends AppCompatActivity {

    Button login, signUp;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);



        login = findViewById(R.id.login_main);
        signUp = findViewById(R.id.signUp_main);
        login.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity( new Intent(MainActivity.this, LoginActivity.class) );
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



}
