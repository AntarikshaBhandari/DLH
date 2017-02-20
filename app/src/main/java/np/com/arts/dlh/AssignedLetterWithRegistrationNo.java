package np.com.arts.dlh;

import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.text.Html;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.Toast;



/**
 * Created by Antariksha on 2/19/2017.
 */
public class AssignedLetterWithRegistrationNo extends AppCompatActivity {
    String filename = "mypreferencefile";
    String pass_key = "pass";

    LinearLayout ccLayout;

    String to, content, subject, id, cc, registrationId, registrationNo, applicantName;
    EditText applicantNameEditText, registrationNoEditText, toEditText, subjectEditText, contentEditText, ccEditText;



    Button approve, decline;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.assigned_letters_full_content_with_registration_no);


        Toolbar toolbar = (Toolbar) findViewById(R.id.id_toolbar_without_logo);
        setSupportActionBar(toolbar);
        getSupportActionBar().setTitle("Letters");
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        Bundle bundle = getIntent().getExtras();
        id = bundle.getString("LetterID");
        to = bundle.getString("LetterTo");
        content = bundle.getString("LetterContent");
        cc = bundle.getString("LetterCC");
        subject = bundle.getString("LetterSubject");
        registrationId = bundle.getString("LetterRegistrationID");
        registrationNo = bundle.getString("LetterRegistrationNo");
        applicantName = bundle.getString("LetterApplicantName");

        applicantNameEditText = (EditText) findViewById(R.id.applicantName);
        registrationNoEditText = (EditText) findViewById(R.id.registrationNo);
        toEditText = (EditText) findViewById(R.id.lettersTo);
        subjectEditText = (EditText) findViewById(R.id.subject);
        contentEditText = (EditText) findViewById(R.id.lettersContent);
        ccEditText = (EditText) findViewById(R.id.lettersCC);

        ccLayout = (LinearLayout) findViewById(R.id.ccLayout_with_registration_no);

        ccLayout.setVisibility(LinearLayout.GONE);

        approve = (Button) findViewById(R.id.btnApprove);
        decline = (Button) findViewById(R.id.btnDecline);


        final Dialog dialog = new Dialog(AssignedLetterWithRegistrationNo.this);
        dialog.setContentView(R.layout.confirm_password);
        dialog.setTitle("Confirm Password");
        final EditText confirmPassword = (EditText) dialog.findViewById(R.id.confirmPassword);
        final Button btnCancelConfirmPassword = (Button) dialog.findViewById(R.id.btn_cancel_confirm_password);
        final Button btnConfirmConfirmPassword = (Button) dialog.findViewById(R.id.btn_confirm_confirm_password);

        SharedPreferences preferencesState = getSharedPreferences(filename, Context.MODE_PRIVATE);
        final String userPassword = preferencesState.getString(pass_key, "");

        approve.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialog.show();

                btnConfirmConfirmPassword.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        String confirmPasswordValue = confirmPassword.getText().toString();
                        if (userPassword.equals(confirmPasswordValue)) {
                            dialog.dismiss();
                            Toast.makeText(AssignedLetterWithRegistrationNo.this, "Approve Success", Toast.LENGTH_SHORT).show();
                        } else {
                            Toast.makeText(AssignedLetterWithRegistrationNo.this, "Invalid Password", Toast.LENGTH_SHORT).show();
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
                dialog.show();

                btnConfirmConfirmPassword.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        String confirmPasswordValue = confirmPassword.getText().toString();
                        if (userPassword.equals(confirmPasswordValue)) {
                            dialog.dismiss();
                            Toast.makeText(AssignedLetterWithRegistrationNo.this, "Decline Success", Toast.LENGTH_SHORT).show();
                        } else {
                            Toast.makeText(AssignedLetterWithRegistrationNo.this, "Invalid Password", Toast.LENGTH_SHORT).show();
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

        applicantNameEditText.setText(applicantName);
        registrationNoEditText.setText(registrationNo);
        toEditText.setText(to);
        subjectEditText.setText(subject);
        contentEditText.setText(Html.fromHtml(content));
        if(cc.length()>0) {
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
