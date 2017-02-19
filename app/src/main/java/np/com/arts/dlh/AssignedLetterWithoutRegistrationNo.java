package np.com.arts.dlh;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.widget.EditText;

/**
 * Created by Antariksha on 2/19/2017.
 */
public class AssignedLetterWithoutRegistrationNo extends AppCompatActivity {

    String to,content,subject,id,cc;
    EditText toEditText, subjectEditText, contentEditText, ccEditText;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.assigned_letters_full_content_without_registration_no);

        Bundle bundle = getIntent().getExtras();
        id = bundle.getString("LetterID");
        to = bundle.getString("LetterTo");
        content = bundle.getString("LetterContent");
        cc = bundle.getString("LetterCC");
        subject = bundle.getString("LetterSubject");

        toEditText = (EditText) findViewById(R.id.lettersTo_withoutRegistrationNo);
        subjectEditText = (EditText) findViewById(R.id.subject_withoutRegistrationNo);
        contentEditText = (EditText) findViewById(R.id.lettersContent_withoutRegistrationNo);

        toEditText.setText(to);
        subjectEditText.setText(subject);
        contentEditText.setText(content);


    }
}
