package np.com.arts.dlh;

import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.text.Html;
import android.text.SpannableString;
import android.view.MenuItem;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.Toast;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;

/**
 * Created by Antariksha on 2/19/2017.
 */
public class AssignedLetterWithoutRegistrationNo extends AppCompatActivity {
    String filename = "mypreferencefile";
    String pass_key = "pass";
    String id_key = "id";

    LinearLayout ccLayout;

    String to, content, subject, id, cc;
    EditText toEditText, subjectEditText, contentEditText, ccEditText;

    Button approve, decline;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.assigned_letters_full_content_without_registration_no);

        Toolbar toolbar = (Toolbar) findViewById(R.id.id_toolbar_without_logo);
        setSupportActionBar(toolbar);
        getSupportActionBar().setTitle("Letters");
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        final LinearLayout linearLayout = (LinearLayout) findViewById(R.id.withoutRegistrationNo_LinearLayout);

        linearLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                InputMethodManager inputMethodManager = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
                inputMethodManager.hideSoftInputFromWindow(linearLayout.getWindowToken(),0);
            }
        });

        Bundle bundle = getIntent().getExtras();
        id = bundle.getString("LetterID");
        to = bundle.getString("LetterTo");
        content = bundle.getString("LetterContent");
        cc = bundle.getString("LetterCC");
        subject = bundle.getString("LetterSubject");

        toEditText = (EditText) findViewById(R.id.lettersTo_withoutRegistrationNo);
        subjectEditText = (EditText) findViewById(R.id.subject_withoutRegistrationNo);
        contentEditText = (EditText) findViewById(R.id.lettersContent_withoutRegistrationNo);
        ccEditText = (EditText) findViewById(R.id.lettersCC_withoutRegistrationNo);
        ccLayout = (LinearLayout) findViewById(R.id.ccLayout_without_registration_no);

        ccLayout.setVisibility(LinearLayout.GONE);

        approve = (Button) findViewById(R.id.btnApprove_withoutRegistrationNo);
        decline = (Button) findViewById(R.id.btnDecline_withoutRegistrationNo);


        final Dialog dialog = new Dialog(AssignedLetterWithoutRegistrationNo.this);
        dialog.setContentView(R.layout.confirm_password);
        dialog.setTitle("Confirm Password");

        final EditText confirmPassword = (EditText) dialog.findViewById(R.id.confirmPassword);
        final Button btnCancelConfirmPassword = (Button) dialog.findViewById(R.id.btn_cancel_confirm_password);
        final Button btnConfirmConfirmPassword = (Button) dialog.findViewById(R.id.btn_confirm_confirm_password);

        SharedPreferences preferencesState = getSharedPreferences(filename, Context.MODE_PRIVATE);
        final String userPassword = preferencesState.getString(pass_key, "");
        final String userID = preferencesState.getString(id_key, "");

        final ProgressDialog progressDialog = new ProgressDialog(AssignedLetterWithoutRegistrationNo.this);
        progressDialog.setMessage("Authorizing....");
        progressDialog.setCancelable(false);

        approve.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                final String letter_content = contentEditText.getText().toString();
                final String letter_to = toEditText.getText().toString();

                dialog.show();

                btnConfirmConfirmPassword.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        String confirmPasswordValue = confirmPassword.getText().toString();
                        if (userPassword.equals(confirmPasswordValue)) {
                            InputMethodManager inputMethodManager = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
                            inputMethodManager.hideSoftInputFromWindow(btnConfirmConfirmPassword.getWindowToken(),0);
                            dialog.dismiss();

                            progressDialog.show();

                            StringRequest stringRequest = new StringRequest(Request.Method.POST, Server.assignedUrl + userID, new Response.Listener<String>() {
                                @Override
                                public void onResponse(String response) {
                                    try {
                                        JSONObject obj = new JSONObject(response);
                                        String status = obj.getString("status");

                                        if (status.equals("success")) {
                                            progressDialog.dismiss();

                                            Toast.makeText(AssignedLetterWithoutRegistrationNo.this, "Letter Approved", Toast.LENGTH_SHORT).show();

                                            Intent i = new Intent(getApplicationContext(), MainActivity.class);
                                            i.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                                            startActivity(i);
                                        }

                                    } catch (JSONException e) {
                                        e.printStackTrace();
                                    }
                                }
                            }, new Response.ErrorListener() {
                                @Override
                                public void onErrorResponse(VolleyError error) {
                                    progressDialog.dismiss();
                                    Toast.makeText(AssignedLetterWithoutRegistrationNo.this, "Some Error occured, Please Try Again !!", Toast.LENGTH_SHORT).show();
                                }
                            }) {
                                @Override
                                protected Map<String, String> getParams() throws AuthFailureError {
                                    Map<String, String> myParameters = new HashMap<>();

                                    myParameters.put("letter_id", id);
                                    myParameters.put("letter", letter_content);
                                    myParameters.put("status", "3");

                                    return myParameters;
                                }
                            };

                            RequestQueue requestQueue = Volley.newRequestQueue(getApplicationContext());
                            requestQueue.add(stringRequest);

//                            Toast.makeText(AssignedLetterWithoutRegistrationNo.this, "Approve Success", Toast.LENGTH_SHORT).show();
                        } else {
                            Toast.makeText(AssignedLetterWithoutRegistrationNo.this, "Invalid Password", Toast.LENGTH_SHORT).show();
                        }
                    }
                });

                btnCancelConfirmPassword.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        dialog.dismiss();
                    }
                });

            }
        });

        decline.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                final String letter_content = contentEditText.getText().toString();
                dialog.show();

                btnConfirmConfirmPassword.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        String confirmPasswordValue = confirmPassword.getText().toString();

                        if (userPassword.equals(confirmPasswordValue)) {
                            InputMethodManager inputMethodManager = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
                            inputMethodManager.hideSoftInputFromWindow(btnCancelConfirmPassword.getWindowToken(),0);
                            dialog.dismiss();

                            progressDialog.show();

                            StringRequest stringRequest = new StringRequest(Request.Method.POST, Server.assignedUrl + userID, new Response.Listener<String>() {
                                @Override
                                public void onResponse(String response) {
                                    try {
                                        JSONObject obj = new JSONObject(response);
                                        String status = obj.getString("status");

                                        if (status.equals("success")) {
                                            progressDialog.dismiss();

                                            Intent i = new Intent(getApplicationContext(), MainActivity.class);
                                            i.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                                            startActivity(i);

                                            Toast.makeText(AssignedLetterWithoutRegistrationNo.this, "Letter Decline", Toast.LENGTH_SHORT).show();
                                        }

                                    } catch (JSONException e) {
                                        e.printStackTrace();
                                    }
                                }
                            }, new Response.ErrorListener() {
                                @Override
                                public void onErrorResponse(VolleyError error) {
                                    progressDialog.dismiss();
                                    Toast.makeText(AssignedLetterWithoutRegistrationNo.this, "Some Error occured, Please Try Again !!", Toast.LENGTH_SHORT).show();
                                }
                            }) {
                                @Override
                                protected Map<String, String> getParams() throws AuthFailureError {
                                    Map<String, String> myParameters = new HashMap<>();

                                    myParameters.put("letter_id", id);
                                    myParameters.put("letter", letter_content);
                                    myParameters.put("status", "-2");

                                    return myParameters;
                                }
                            };

                            RequestQueue requestQueue = Volley.newRequestQueue(getApplicationContext());
                            requestQueue.add(stringRequest);
//                            Toast.makeText(AssignedLetterWithoutRegistrationNo.this, "Decline Success", Toast.LENGTH_SHORT).show();
                        } else {
                            Toast.makeText(AssignedLetterWithoutRegistrationNo.this, "Invalid Password", Toast.LENGTH_SHORT).show();
                        }
                    }
                });

                btnCancelConfirmPassword.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        dialog.dismiss();
                    }
                });

            }
        });


        toEditText.setText(to);
        subjectEditText.setText(subject);
        contentEditText.setText(Html.fromHtml(content));
        ccEditText.setText(Html.fromHtml(content));

        if (cc.length() > 0) {
            ccLayout.setVisibility(LinearLayout.VISIBLE);
            ccEditText.setText(cc);
        }

    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        Intent i = new Intent(getApplicationContext(), AssignedLetters.class);
        i.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
        startActivity(i);

        return true;
    }
}
