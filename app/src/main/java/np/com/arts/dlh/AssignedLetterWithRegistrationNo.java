package np.com.arts.dlh;

import android.app.Dialog;
import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.text.Html;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;



/**
 * Created by Antariksha on 2/19/2017.
 */
public class AssignedLetterWithRegistrationNo extends AppCompatActivity {
    String filename = "mypreferencefile";
    String pass_key = "pass";

    String to, content, subject, id, cc, registrationId, registrationNo, applicantName;
    EditText applicantNameEditText, registrationNoEditText, toEditText, subjectEditText, contentEditText, ccEditText;



    Button approve, decline;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.assigned_letters_full_content);

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

        approve = (Button) findViewById(R.id.btnApprove);
        decline = (Button) findViewById(R.id.btnDecline);

        approve.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                final Dialog dialog = new Dialog(AssignedLetterWithRegistrationNo.this);
                dialog.setContentView(R.layout.confirm_password);
                dialog.show();
                //Toast.makeText(AssignedLetterWithRegistrationNo.this, "Button is clicked", Toast.LENGTH_SHORT).show();
                EditText confirmPassword = (EditText) dialog.findViewById(R.id.confirmPassword1);
                Button btnCancelConfirmPassword = (Button) dialog.findViewById(R.id.btn_cancel_confirm_password);
                Button btnConfirmPassword = (Button) dialog.findViewById(R.id.btn_confirm_confirm_password);

                SharedPreferences preferencesState = getSharedPreferences(filename, Context.MODE_PRIVATE);
                final String userPassword = preferencesState.getString(pass_key, "");
                final String confirm_Password_Value = confirmPassword.getText().toString();

                btnConfirmPassword.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        Toast.makeText(AssignedLetterWithRegistrationNo.this, confirm_Password_Value, Toast.LENGTH_SHORT).show();
                        if (userPassword == confirm_Password_Value) {
                            dialog.dismiss();
                            Toast.makeText(AssignedLetterWithRegistrationNo.this, "OK", Toast.LENGTH_SHORT).show();
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
                final Dialog dialog = new Dialog(AssignedLetterWithRegistrationNo.this);
                dialog.setContentView(R.layout.confirm_password);
                dialog.show();

                final EditText confirmPassword = (EditText) dialog.findViewById(R.id.confirmPassword1);
                SharedPreferences preferencesState = getSharedPreferences("mypreferencefile", Context.MODE_PRIVATE);
                String userPassword = preferencesState.getString("pass_key", "");
                String confirm_Password = confirmPassword.getText().toString().trim();
                if (userPassword.equals(confirm_Password)) {
                    dialog.dismiss();
                    Toast.makeText(AssignedLetterWithRegistrationNo.this, "OK", Toast.LENGTH_SHORT).show();
                } else {
                    Toast.makeText(AssignedLetterWithRegistrationNo.this, "Invalid Password", Toast.LENGTH_SHORT).show();
                }

            }
        });

        applicantNameEditText.setText(applicantName);
        registrationNoEditText.setText(registrationNo);
        toEditText.setText(to);
        subjectEditText.setText(subject);
        contentEditText.setText(Html.fromHtml(content));


    }
}
