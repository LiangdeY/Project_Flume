package comp5216.sydney.edu.au.project_flume;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.storage.FirebaseStorage;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Random;
import java.util.Set;

import comp5216.sydney.edu.au.project_flume.Model.User;

public class HomeActivity extends AppCompatActivity {

    TextView username;
    FirebaseUser user;
    DatabaseReference dbReference;
    Button matchBtn, settingBtn;
    List<User> mUsers;
    FirebaseUser mFirebaseUser;
    User currentUserModel;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);

        //TODO otherwise load the page

        Intent intent = getIntent();
        String login = intent.getStringExtra("login");
        if(login == "1") {

        }
        InitUI();
        GetUserList();
    }

    @Override
    protected void onStart() {
        super.onStart();
        CheckMatch();
    }


    private void InitUI(){
        //get user info
        username = findViewById(R.id.username_home);
        matchBtn = findViewById(R.id.matchBtn_home);
        settingBtn = findViewById(R.id.settingBtn_home);

        matchBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(matchBtn.getText().toString().equals("Match")) {
                    matchBtn.setText("Cancel");
                    MatchUser();
                } else if(matchBtn.getText().toString().equals("Cancel")) {
                    matchBtn.setText("Match");
                }
            }
        });
        settingBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent(HomeActivity.this, SettingActivity.class);
                i.putExtra("from", "Home");
                startActivity(i);
            }
        });

        user = FirebaseAuth.getInstance().getCurrentUser();
        dbReference = FirebaseDatabase.getInstance().getReference("Users")
                .child(user.getUid());

        dbReference.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {

                //
                currentUserModel = dataSnapshot.getValue(User.class);
                username.setText(currentUserModel.getUsername());
                Toast.makeText(HomeActivity.this, "Welcome back, " +
                        currentUserModel.getUsername(), Toast.LENGTH_SHORT).show();
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) { }
        });

    }


    private void MatchUser() {
        //create a list of unmatched user
        List<User> unMatchUser = new ArrayList<>();
        for(int i=0;i<mUsers.size();i++){
            if(mUsers.get(i).getIsMatch().equals("N")) {
                unMatchUser.add(mUsers.get(i));
            }
        }
        // pick a random user;
        Random random = new Random();

        try {
            int index = random.nextInt(unMatchUser.size());
            Log.d("index = ", String.valueOf(index));
            Log.d("unMatchUser_size = ", String.valueOf(unMatchUser.size()));

            //set up matches in target user
            DatabaseReference matchUserRef = FirebaseDatabase.getInstance()
                    .getReference("Users").child(unMatchUser.get(index).getId());
            matchUserRef.child("isMatch").setValue("Matched");
            matchUserRef.child("matchId").setValue(mFirebaseUser.getUid());
            //set up matches in current user
            DatabaseReference currentUserRef = FirebaseDatabase.getInstance()
                    .getReference("Users").child(mFirebaseUser.getUid());

            currentUserRef.child("isMatch").setValue("Matching");
            currentUserRef.child("matchId").setValue(unMatchUser.get(index).getId());
            //TODO handle matches fail, consider transaction

            //send the matched user id to the chat activity
            Intent intent = new Intent(HomeActivity.this, ChatActivity.class);
            intent.putExtra("targetId", unMatchUser.get(index).getId());
            findViewById(R.id.spin_kit).setVisibility(View.GONE);
            startActivity(intent);

        }catch (Exception e) {
            Log.d("matchUser Exception", e.toString());

            matchBtn.setText("Match");
            Toast.makeText(HomeActivity.this, "Sorry, We can't find a match",
                    Toast.LENGTH_SHORT).show();

        }
    }

    private void GetUserList() {

        mUsers  = new ArrayList<>();
        //get a list of user from database, except the current user
        mFirebaseUser  = FirebaseAuth.getInstance().getCurrentUser();
        DatabaseReference userListRef = FirebaseDatabase.getInstance().getReference("Users");

        userListRef.addValueEventListener( new ValueEventListener() {
            @Override
            public void onDataChange( @NonNull DataSnapshot dataSnapshot ) {
                mUsers.clear();
                for (DataSnapshot snapshot: dataSnapshot.getChildren() ) {
                    User user = snapshot.getValue(User.class);

                    assert user != null;
                    assert mFirebaseUser != null;

                    if(!user.getId().equals(mFirebaseUser.getUid())) {
                        mUsers.add(user);
                    }
                }
//                //pring name for debug
//                for(int i=0;i<mUsers.size();i++){
//                    Log.d("list of name", mUsers.get(i).getUsername());
//                }
            }
            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
            }
        });

    }

    private void CheckMatch() {

        dbReference.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                 currentUserModel = dataSnapshot.getValue(User.class);
                if (!currentUserModel.getIsMatch().equals("N")) {
                    Intent intent = new Intent( HomeActivity.this, ChatActivity.class);
                    intent.putExtra("targetId", currentUserModel.getMatchId());
                    startActivity(intent);
                }
            }
            @Override
            public void onCancelled(DatabaseError databaseError) {}
        });
        //forward the user to the chat screen if he has a matches
    }
}
