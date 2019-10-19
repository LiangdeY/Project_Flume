package comp5216.sydney.edu.au.project_flume;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.text.format.DateFormat;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.firebase.ui.auth.AuthUI;
import com.firebase.ui.database.FirebaseListAdapter;
import com.firebase.ui.database.FirebaseListOptions;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.android.material.snackbar.Snackbar;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;

public class MainActivity extends AppCompatActivity {
    private static final int SIGN_IN_REQUEST_CODE = 1;
    private FirebaseListAdapter<Chat_message> adapter;
    private FloatingActionButton sendBtn;

    RelativeLayout activity_chat;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_chat);
        activity_chat =  findViewById(R.id.activity_chat);
        initializeUI();
        checkSignIn();
        showMessage();
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        switch( requestCode ) {
            case SIGN_IN_REQUEST_CODE :
                if( resultCode == RESULT_OK ) {
                    Snackbar.make(
                            activity_chat, "Sign in success! Welcome !",
                            Snackbar.LENGTH_SHORT).show();
                }
                else{
                    Snackbar.make(
                            activity_chat, "We couldn't sign you in. Please try later",
                            Snackbar.LENGTH_SHORT).show();
                    finish();
                }
                break;
                default: break;
        }

    }

    @Override
    public boolean onCreateOptionsMenu( Menu menu ) {
        getMenuInflater().inflate(R.menu.menu, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if ( item.getItemId() == R.id.menu_sign_out ) {
            AuthUI.getInstance().signOut(this).addOnCompleteListener(
                    new OnCompleteListener<Void>() {
                        @Override
                        public void onComplete(@NonNull Task<Void> task) {
                            Snackbar.make(activity_chat, "You have been signed out.",
                                    Snackbar.LENGTH_SHORT).show();
                            finish();
                        }
                    });
        }
        return true;
    }
    private void checkSignIn () {
        if (FirebaseAuth.getInstance().getCurrentUser() == null) {
            // Not signed in then navigate to sign in page
            startActivityForResult(AuthUI.getInstance().createSignInIntentBuilder().build(),SIGN_IN_REQUEST_CODE);

        } else {
            Snackbar.make(activity_chat, "Welcome" +
                            FirebaseAuth.getInstance().getCurrentUser().getEmail(),
                    Snackbar.LENGTH_SHORT).show();
        }
    }

    private void initializeUI(){
        sendBtn = findViewById(R.id.send_message_btn);
        sendBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                EditText input  = findViewById(R.id.message_input);
                FirebaseDatabase.getInstance().getReference().push().setValue(
                        new Chat_message(
                                input.getText().toString(),
                                FirebaseAuth.getInstance().getCurrentUser().getEmail()) );
                input.setText("");
            }
        });
    }

    public void showMessage() {
        ListView messageList = findViewById( R.id.message_list );

        Query query = FirebaseDatabase.getInstance()
                .getReference()
                .limitToLast(50);

        FirebaseListOptions<Chat_message> options = new FirebaseListOptions.Builder<Chat_message>()
                .setQuery(query, Chat_message.class)
                .build();

        adapter = new FirebaseListAdapter<Chat_message> (options) {
            @Override
            protected void populateView( View v, Chat_message model, int position ){
                //get reference to the views of list item
                TextView text, user_message, time;
                text = findViewById(R.id.message_text);
                user_message = findViewById(R.id.user_message);
                time = findViewById(R.id.time_message);

                text.setText(model.getMessageText());
                user_message.setText(model.getUser());
                time.setText(DateFormat.format("dd-MM-yyyy (HH:mm:ss)", model.getTime()));
            }
        };
        messageList.setAdapter(adapter);
    }
}
