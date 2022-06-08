package de.androidcrypto.simplechatapp;

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

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import android.os.Bundle;

public class MainActivity extends AppCompatActivity {
    // https://infopsh.blogspot.com/2021/06/build-simple-chat-app-using-firebase.html

    private static int SIGN_IN_REQUEST_CODE = 1;
    private FirebaseListAdapter<chatMessage> adapter;
    RelativeLayout activity_main;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        activity_main = (RelativeLayout) findViewById(R.id.activity_main);
        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                EditText input = (EditText) findViewById(R.id.input);
                FirebaseDatabase.getInstance("https://simplechatapp-5468c-default-rtdb.europe-west1.firebasedatabase.app/").getReference().push().setValue(new chatMessage(input.getText().toString(),
                        FirebaseAuth.getInstance().getCurrentUser().getEmail()));
                /*FirebaseDatabase.getInstance().getReference().push().setValue(new chatMessage(input.getText().toString(),
                        FirebaseAuth.getInstance().getCurrentUser().getEmail()));*/
                input.setText(" ");
            }
        });


        //check if sign-in then navigate the signin page
        if(FirebaseAuth.getInstance().getCurrentUser() == null)
        {
            startActivityForResult(AuthUI.getInstance().createSignInIntentBuilder().build(),SIGN_IN_REQUEST_CODE);
        }
        else
        {
            Snackbar.make(activity_main,"Welcome" + FirebaseAuth.getInstance().getCurrentUser().getEmail(),Snackbar.LENGTH_SHORT).show();
            displayMessage();
        }

        //Load content

    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if(item.getItemId() == R.id.menu_sign_out)
        {
            AuthUI.getInstance().signOut(this).addOnCompleteListener(new OnCompleteListener<Void>() {
                @Override
                public void onComplete(@NonNull Task<Void> task) {
                    Snackbar.make(activity_main,"You  have been Signed-Out..",Snackbar.LENGTH_SHORT).show();
                    finish();
                }
            });
        }
        return true;
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.main_menu,menu);
        return true;
    }

    @Override
    protected void onActivityResult(int requestcode, int resultcode, Intent data) {
        super.onActivityResult(requestcode,resultcode,data);
        if(requestcode == SIGN_IN_REQUEST_CODE)
        {
            if(resultcode == RESULT_OK)
            {
                Snackbar.make(activity_main,"SuccessFully sign-in Welcome..",Snackbar.LENGTH_SHORT).show();
                displayMessage();
            }
            else
            {
                Snackbar.make(activity_main,"Sorry you can't sign-in Please try again later..",Snackbar.LENGTH_SHORT).show();
                finish();
            }
        }
    }

    public void displayMessage()
    {
        ListView listOfMessage = (ListView) findViewById(R.id.list_of_message);

        //adapter = new FirebaseListAdapter<chatMessage>(this,chatMessage.class , R.layout.list_item,FirebaseDatabase.getInstance().getReference()) {
            // see: https://stackoverflow.com/a/47144922/8166854
            /*Query query = FirebaseDatabase.getInstance().getReference();
            FirebaseListOptions<chatMessage> options = new FirebaseListOptions.Builder<chatMessage>()
                    .setQuery(query, chatMessage.class)
                    .build();*/
        Query query = FirebaseDatabase.getInstance().getReference();
        FirebaseListOptions<chatMessage> options = new FirebaseListOptions.Builder<chatMessage>()
                .setLayout(R.layout.activity_main) //name of the row xml file
                .setQuery(query, chatMessage.class)
                .build();

            adapter = new FirebaseListAdapter<chatMessage>(options) {
                @Override
                protected void populateView(@NonNull View v, @NonNull chatMessage model, int position) {
                    TextView messageText , messageUser , messageTime;

                    messageText = (TextView) v.findViewById(R.id.message_Text);
                    messageUser = (TextView) v.findViewById(R.id.message_user);
                    messageTime = (TextView) v.findViewById(R.id.message_time);

                    messageText.setText(model.getMessageText());
                    messageUser.setText(model.getMessageUser());
                    messageTime.setText(DateFormat.format("dd-MM-yyyy (HH:mm:ss)",model.getMessageTime()));
                }
            };
        /*
            @Override
            protected void populateView(View v, chatMessage model, int position) {
                TextView messageText , messageUser , messageTime;

                messageText = (TextView) v.findViewById(R.id.message_Text);
                messageUser = (TextView) v.findViewById(R.id.message_user);
                messageTime = (TextView) v.findViewById(R.id.message_time);

                messageText.setText(model.getMessageText());
                messageUser.setText(model.getMessageUser());
                messageTime.setText(DateFormat.format("dd-MM-yyyy (HH:mm:ss)",model.getMessageTime()));
            }
        };*/
        listOfMessage.setAdapter(adapter);
    };
}