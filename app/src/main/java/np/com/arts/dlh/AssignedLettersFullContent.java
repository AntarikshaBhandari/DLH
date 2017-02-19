package np.com.arts.dlh;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

/**
 * Created by Antariksha on 2/19/2017.
 */

public class AssignedLettersFullContent extends AppCompatActivity {

    EditText applicantName, registrationNo, lettersTo, subject, lettersContent;
    Button btnDecline, btnApprove;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.assigned_letters_full_content);

        EditText applicantName = (EditText) findViewById(R.id.applicantName);
        EditText registrationNo = (EditText) findViewById(R.id.registrationNo);
        EditText lettersTo = (EditText) findViewById(R.id.lettersTo);
        EditText subject = (EditText) findViewById(R.id.subject);
        EditText letterContent = (EditText) findViewById(R.id.lettersContent);

        Button btnDecline = (Button) findViewById(R.id.btnDecline);
        Button btnApprove = (Button) findViewById(R.id.btnApprove);

        btnDecline.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

            }
        });

        btnApprove.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

            }
        });

    }
}
