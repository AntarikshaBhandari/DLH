package np.com.arts.dlh;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

public class SignInActicity extends AppCompatActivity {

    EditText username, password;
    String usrname, pass;
    Button loginButton;

    //signin
    static String filename="mypreferencefile";
    static String user_key = "user";
    static String pass_key = "pass";

    //signin state
    static String file = "loginstatefile";
    static String state = "state";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.login_activity);

        SharedPreferences sharedPreferences = getSharedPreferences(filename,MODE_PRIVATE);
        Boolean logic = sharedPreferences.getBoolean(state,false);

        if(logic == true){
            Intent i = new Intent(getApplicationContext(),MainActivity.class);
            startActivity(i);
            finish();
        }

        username = (EditText) findViewById(R.id.input_username);
        password = (EditText) findViewById(R.id.input_password);
        loginButton = (Button) findViewById(R.id.btn_login);



        loginButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                usrname = username.getText().toString();
                pass = password.getText().toString();

                StringRequest stringRequest = new StringRequest(Request.Method.POST, Server.loginUrl, new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        try {
                            JSONObject login = new JSONObject(response);
                            String loginStatus = login.getString("status");

                            if(loginStatus == "fail"){
                                Toast.makeText(SignInActicity.this, "Login Failed", Toast.LENGTH_SHORT).show();
                            }

                            else {

                            }


                        } catch (JSONException e) {
                            e.printStackTrace();
                        }

                    }
                }, new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {

                    }
                });


//                if (usrname.equals("admin") && pass.equals("admin")) {
//                    SharedPreferences preferencesState = getSharedPreferences(filename, Context.MODE_PRIVATE);
//                    SharedPreferences.Editor editor = preferencesState.edit();
//                    editor.putString(user_key,usrname);
//                    editor.putString(pass_key,pass);
//                    editor.putBoolean("state",true);
//                    editor.commit();
//
//                    startActivity(new Intent(getApplicationContext(), MainActivity.class));
//                    Toast.makeText(getApplicationContext(), "Log In Successful", Toast.LENGTH_SHORT).show();
//                    finish();
//                }
//                else {
//                    Toast.makeText(getApplicationContext(), "Invalid Username or Password", Toast.LENGTH_SHORT).show();
//                }
            }
        });
    }
}
