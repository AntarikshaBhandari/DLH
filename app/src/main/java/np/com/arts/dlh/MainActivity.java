package np.com.arts.dlh;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.CardView;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import static np.com.arts.dlh.R.id.approvedLetters;

public class MainActivity extends AppCompatActivity {

    SharedPreferences preferencesState;

    Toolbar toolbar;

    static String filename="mypreferencefile";
    static String id_key = "id";
    static String name_key = "name";

    //signin state
    static String file = "loginstatefile";
    static String state = "state";

    String userID, user_Name;

    CardView assignedLetters;
    CardView approvedLetters;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        toolbar = (Toolbar) findViewById(R.id.toolbar_mainactivity);
        setSupportActionBar(toolbar);
//        getSupportActionBar().setTitle("DLH");
//        getSupportActionBar().setIcon(R.mipmap.ic_launcher);
      getSupportActionBar().setDisplayShowTitleEnabled(false);
////add app icon inside the Toolbar
//        getSupportActionBar().setDisplayShowHomeEnabled(true);

        assignedLetters = (CardView) findViewById(R.id.assignedLetters);
        approvedLetters = (CardView) findViewById(R.id.approvedLetters);
        TextView userName = (TextView) findViewById(R.id.user_name);

        preferencesState = getSharedPreferences(filename, Context.MODE_PRIVATE);
        userID = preferencesState.getString(id_key,"");
        user_Name = preferencesState.getString(name_key,"");

        userName.setText(user_Name);

        assignedLetters.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(getApplicationContext(),AssignedLetters.class));
            }
        });

        approvedLetters.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(getApplicationContext(),ApprovedLetters.class));
            }
        });

    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater menuInflater = getMenuInflater();
        menuInflater.inflate(R.menu.popup_menu, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if(item.getItemId() == R.id.popup_logout){
            preferencesState = getSharedPreferences(filename, Context.MODE_PRIVATE);
            SharedPreferences.Editor editor = preferencesState.edit();
            editor.putBoolean("state",false);
            editor.commit();

            startActivity(new Intent(getApplicationContext(),SignInActicity.class));
            finish();
        }


        return super.onOptionsItemSelected(item);
    }
}
