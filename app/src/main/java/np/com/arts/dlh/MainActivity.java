package np.com.arts.dlh;

import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.CardView;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
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

public class MainActivity extends AppCompatActivity {

    SharedPreferences preferencesState;

    Toolbar toolbar;

    static String filename = "mypreferencefile";
    static String id_key = "id";
    static String name_key = "name";
    static String user_key = "user";
    static String pass_key = "pass";

    //signin state
    static String file = "loginstatefile";
    static String state = "state";

    String userID, user_Name, username, password;

    CardView assignedLetters;
    CardView approvedLetters;
    TextView assignedLetterCount;

    StringRequest loginStringRequest, stringRequest;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        assignedLetterCount = (TextView) findViewById(R.id.assignedLetterCount);
        assignedLetterCount.setVisibility(TextView.GONE);
        toolbar = (Toolbar) findViewById(R.id.toolbar_mainactivity);
        setSupportActionBar(toolbar);
//        getSupportActionBar().setTitle("DLH");
//        getSupportActionBar().setIcon(R.mipmap.ic_launcher);
        getSupportActionBar().setDisplayShowTitleEnabled(false);
////add app icon inside the Toolbar
//        getSupportActionBar().setDisplayShowHomeEnabled(true);

        assignedLetters = (CardView) findViewById(R.id.assignedLetters);
        approvedLetters = (CardView) findViewById(R.id.approvedLetters);
        final TextView userName = (TextView) findViewById(R.id.user_name);

        final View snackBarPosition = findViewById(R.id.activity_main);

        preferencesState = getSharedPreferences(filename, Context.MODE_PRIVATE);
        userID = preferencesState.getString(id_key, "");
        user_Name = preferencesState.getString(name_key, "");
        username = preferencesState.getString(user_key, "");
        password = preferencesState.getString(pass_key, "");

        userName.setText(user_Name);

//        Toast.makeText(this, userID, Toast.LENGTH_SHORT).show();

        final ProgressDialog progressDialog = new ProgressDialog(MainActivity.this);
        progressDialog.setMessage("Loading");
        progressDialog.setCancelable(false);
        progressDialog.show();

        assignedLetters.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(getApplicationContext(), AssignedLetters.class));
            }
        });

        approvedLetters.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(getApplicationContext(), ApprovedLetters.class));
            }
        });

        loginStringRequest = new StringRequest(Request.Method.POST, Server.loginUrl, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                try {
                    JSONObject login = new JSONObject(response);
                    String loginStatus = login.getString("status");
//                    Toast.makeText(MainActivity.this, loginStatus, Toast.LENGTH_SHORT).show();

                    if (loginStatus.equals("fail")) {

                        final Dialog dialog = new Dialog(MainActivity.this);
                        dialog.setContentView(R.layout.error_dialog);
                        dialog.setTitle("!!! Access Denied !!!");
                        dialog.setCancelable(false);
                        dialog.show();

                        Button btnOKErrorDialog = (Button) dialog.findViewById(R.id.btnOK_errorDialog);

                        btnOKErrorDialog.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View v) {
                                preferencesState = getSharedPreferences(filename, Context.MODE_PRIVATE);
                                SharedPreferences.Editor editor = preferencesState.edit();
                                editor.putBoolean("state", false);
                                editor.commit();
                                dialog.dismiss();

                                Intent i = new Intent(getApplicationContext(), SignInActivity.class);
                                i.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_NEW_TASK);
                                startActivity(i);

                            }
                        });

                    }

                } catch (JSONException e) {
                    e.printStackTrace();
                }

            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
            }
        }) {
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                Map<String, String> myParameters = new HashMap<>();

                myParameters.put("username", username);
                myParameters.put("password", password);

                return myParameters;
            }
        };


        stringRequest = new StringRequest(Request.Method.GET, Server.assignedUrl + userID, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                try {
                    JSONObject obj = new JSONObject(response);
                    String status = obj.getString("status");
//                    Toast.makeText(MainActivity.this, status, Toast.LENGTH_SHORT).show();


                    if (status.equals("success")) {
                        JSONArray assignments = obj.getJSONArray("assignments");
                        progressDialog.dismiss();
//                        Toast.makeText(MainActivity.this, "assignments" + assignments.length(), Toast.LENGTH_SHORT).show();
                        String length = String.valueOf(assignments.length());
                        if (assignments.length() > 0) {
                            assignedLetterCount.setVisibility(TextView.VISIBLE);
                            assignedLetterCount.setText(length);
                        }

                    } else {
                        progressDialog.dismiss();
//                        Toast.makeText(MainActivity.this, "Error", Toast.LENGTH_SHORT).show();
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                progressDialog.dismiss();
                Snackbar.make(snackBarPosition, "No Internet Connection", Snackbar.LENGTH_SHORT).show();
            }
        });
        RequestQueue requestQueue = Volley.newRequestQueue(getApplicationContext());
        requestQueue.add(loginStringRequest);
        requestQueue.add(stringRequest);

    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater menuInflater = getMenuInflater();
        menuInflater.inflate(R.menu.popup_menu, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (item.getItemId() == R.id.popup_changePassword) {
//            final Dialog dialog = new Dialog(MainActivity.this);
//            dialog.setContentView(R.layout.change_password);
//            dialog.setTitle("Change Password");
//            dialog.show();
//
//            final EditText currentPassword = (EditText) dialog.findViewById(R.id.current_Password);
//            final EditText newPassword = (EditText) dialog.findViewById(R.id.new_Password);
//            final EditText confirmNewPassword = (EditText) dialog.findViewById(R.id.confirm_new_Password);
//            Button btnCancelPasswordChange = (Button) dialog.findViewById(R.id.btn_cancel_password_change);
//            Button btnConfirmPasswordChange = (Button) dialog.findViewById(R.id.btn_confirm_password_change);
//
//            btnCancelPasswordChange.setOnClickListener(new View.OnClickListener() {
//                @Override
//                public void onClick(View v) {
//                    dialog.dismiss();
//                }
//            });
//
//            btnConfirmPasswordChange.setOnClickListener(new View.OnClickListener() {
//                @Override
//                public void onClick(View v) {
//                    String currentPasswordValue = currentPassword.getText().toString().trim();
//                    String newPasswordValue = newPassword.getText().toString().trim();
//                    String confirmNewPasswordValue = confirmNewPassword.getText().toString().trim();
//                    //Toast.makeText(MainActivity.this, confirmNewPasswordValue, Toast.LENGTH_SHORT).show();
//                    if (currentPasswordValue.equals("") || newPasswordValue.equals("") || confirmNewPasswordValue.equals("")) {
//                        Toast.makeText(MainActivity.this, "All Fields are Required.", Toast.LENGTH_SHORT).show();
//                    } else {
//                        if (newPasswordValue.equals(confirmNewPasswordValue)) {
//                            Toast.makeText(MainActivity.this, "Password Changed Successfully", Toast.LENGTH_SHORT).show();
//                            preferencesState = getSharedPreferences(filename, Context.MODE_PRIVATE);
//                            SharedPreferences.Editor editor = preferencesState.edit();
//                            editor.putBoolean("state", false);
//                            editor.commit();
//                            dialog.dismiss();
//
//                            startActivity(new Intent(getApplicationContext(), SignInActivity.class));
//                            finish();
//                        } else {
//                            Toast.makeText(MainActivity.this, "Password doesn't match each other", Toast.LENGTH_SHORT).show();
//                        }
//                    }
//                }
//            });

            Toast.makeText(this, "Feature will be added soon !!!", Toast.LENGTH_SHORT).show();
        }

        if (item.getItemId() == R.id.popup_logout) {
            preferencesState = getSharedPreferences(filename, Context.MODE_PRIVATE);
            SharedPreferences.Editor editor = preferencesState.edit();
            editor.putBoolean("state", false);
            editor.commit();

            Intent i = new Intent(getApplicationContext(), SignInActivity.class);
            i.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_NEW_TASK);
            startActivity(i);
        }


        return super.onOptionsItemSelected(item);
    }
}
