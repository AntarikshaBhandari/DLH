package np.com.arts.dlh;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;

public class SignInActicity extends AppCompatActivity {

    EditText username, password;
    String usernameEditText, passwordEditText;
    Button loginButton;

    //signin
    static String filename = "mypreferencefile";
    static String id_key = "id";
    static String name_key = "name";
    static String user_key = "user";
    static String pass_key = "pass";

    //signin state
    static String state = "state";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.login_activity);

        SharedPreferences sharedPreferences = getSharedPreferences(filename, MODE_PRIVATE);
        Boolean logic = sharedPreferences.getBoolean(state, false);

        if (logic == true) {
            Intent i = new Intent(getApplicationContext(), MainActivity.class);
            startActivity(i);
            finish();
        }

        username = (EditText) findViewById(R.id.input_username);
        password = (EditText) findViewById(R.id.input_password);
        loginButton = (Button) findViewById(R.id.btn_login);
        final LinearLayout linearLayout = (LinearLayout) findViewById(R.id.signIn_Main_LinearLayout);

        linearLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                InputMethodManager inputMethodManager = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
                inputMethodManager.hideSoftInputFromWindow(linearLayout.getWindowToken(),0);
            }
        });


        loginButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                usernameEditText = username.getText().toString();
                passwordEditText = password.getText().toString();

                final ProgressDialog progressDialog = new ProgressDialog(SignInActicity.this);
                progressDialog.setMessage("Logging In......");
                progressDialog.setCancelable(false);


                if (usernameEditText.equals("") || passwordEditText.equals("")) {

                    Toast.makeText(SignInActicity.this, "All Fields are required.", Toast.LENGTH_SHORT).show();
                } else {
                    progressDialog.show();

                    StringRequest stringRequest = new StringRequest(Request.Method.POST, Server.loginUrl, new Response.Listener<String>() {
                        @Override
                        public void onResponse(String response) {
                            try {
                                JSONObject login = new JSONObject(response);
                                String loginStatus = login.getString("status");

                                if (loginStatus.equals("success")) {
                                    JSONArray loginArray = login.getJSONArray("user");
                                    JSONObject c = loginArray.getJSONObject(0);
                                    String userId = c.getString("id");
                                    String user_Name = c.getString("name_np");

                                    SharedPreferences preferencesState = getSharedPreferences(filename, Context.MODE_PRIVATE);
                                    SharedPreferences.Editor editor = preferencesState.edit();
                                    editor.putString(id_key, userId);
                                    editor.putString(name_key, user_Name);
                                    editor.putString(user_key, usernameEditText);
                                    editor.putString(pass_key, passwordEditText);
                                    editor.putBoolean("state", true);
                                    editor.commit();

                                    Intent i = new Intent(getApplicationContext(), MainActivity.class);
                                    startActivity(i);
                                    finish();

                                    progressDialog.dismiss();
                                    Toast.makeText(SignInActicity.this, "Login Successful", Toast.LENGTH_SHORT).show();

                                } else {
                                    progressDialog.dismiss();
                                    String loginMessage = login.getString("message");
                                    Toast.makeText(SignInActicity.this, loginMessage, Toast.LENGTH_SHORT).show();
                                }


                            } catch (JSONException e) {
                                e.printStackTrace();
                            }

                        }
                    }, new Response.ErrorListener() {
                        @Override
                        public void onErrorResponse(VolleyError error) {
                            progressDialog.dismiss();
                            Toast.makeText(SignInActicity.this, "No Internet Connection", Toast.LENGTH_SHORT).show();
                        }
                    }) {
                        @Override
                        protected Map<String, String> getParams() throws AuthFailureError {
                            Map<String, String> myParameters = new HashMap<>();

                            myParameters.put("username", usernameEditText);
                            myParameters.put("password", passwordEditText);

                            return myParameters;
                        }
                    };

                    RequestQueue requestQueue = Volley.newRequestQueue(getApplicationContext());
                    requestQueue.add(stringRequest);

                }
            }
        });
    }
}
