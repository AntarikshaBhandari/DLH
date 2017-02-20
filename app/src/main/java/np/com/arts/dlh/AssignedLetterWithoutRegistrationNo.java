package np.com.arts.dlh;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.text.Html;
import android.view.MenuItem;
import android.view.View;
import android.widget.EditText;
import android.widget.LinearLayout;

/**
 * Created by Antariksha on 2/19/2017.
 */
public class AssignedLetterWithoutRegistrationNo extends AppCompatActivity {
    LinearLayout ccLayout;

    String to,content,subject,id,cc;
    EditText toEditText, subjectEditText, contentEditText, ccEditText;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.assigned_letters_full_content_without_registration_no);

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

        toEditText = (EditText) findViewById(R.id.lettersTo_withoutRegistrationNo);
        subjectEditText = (EditText) findViewById(R.id.subject_withoutRegistrationNo);
        contentEditText = (EditText) findViewById(R.id.lettersContent_withoutRegistrationNo);
        ccEditText = (EditText) findViewById(R.id.lettersCC_withoutRegistrationNo);
        ccLayout = (LinearLayout) findViewById(R.id.ccLayout_without_registration_no);

        ccLayout.setVisibility(LinearLayout.GONE);

        toEditText.setText(to);
        subjectEditText.setText(subject);
        contentEditText.setText(Html.fromHtml(content));
        ccEditText.setText(Html.fromHtml(content));

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
