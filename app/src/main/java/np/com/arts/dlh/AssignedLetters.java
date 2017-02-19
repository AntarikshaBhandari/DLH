package np.com.arts.dlh;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ListAdapter;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

public class AssignedLetters extends AppCompatActivity {
    static String filename = "mypreferencefile";
    static String id_key = "id";
    ListView assignedLetterListsView;
    ArrayList<Module> assignedLetterList = new ArrayList<>();

    Toolbar toolbar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_assigned_letters);

        toolbar = (Toolbar) findViewById(R.id.id_toolbar_without_logo);
        setSupportActionBar(toolbar);

        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        SharedPreferences preferencesState = getSharedPreferences(filename, Context.MODE_PRIVATE);
        String userID = preferencesState.getString(id_key, "");

        Toast.makeText(this, userID, Toast.LENGTH_SHORT).show();
        assignedLetterListsView = (ListView) findViewById(R.id.assignedLetterListsView);


        StringRequest stringRequest = new StringRequest(Request.Method.GET, Server.assignedUrl + userID, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                try {
                    JSONObject obj = new JSONObject(response);
                    String status = obj.getString("status");

                    // Toast.makeText(AssignedLetters.this,status , Toast.LENGTH_SHORT).show();
                    if (status.equals("success")) {
                        Toast.makeText(AssignedLetters.this, status, Toast.LENGTH_SHORT).show();
                        JSONArray assignments = obj.getJSONArray("assignments");
                        for (int i = 0; i < assignments.length(); i++) {
                            Module module = new Module();
                            JSONObject c = assignments.getJSONObject(i);
                            String letter_id = c.getString("id");
                            String letter_createdBy_en = c.getString("created_name_en");
                            String letter_createdBy_np = c.getString("created_name_np");
                            String letter_applicant = c.getString("applicant");
                            String letter_registration_id = c.getString("registration_id");
                            String letter_registration_no = c.getString("registration_number");
                            String letter_to = c.getString("letter_to");
                            String letter_content = c.getString("letter");
                            String letter_cc = c.getString("cc");
                            String letter_subject = c.getString("subject");
                            Toast.makeText(AssignedLetters.this, letter_id, Toast.LENGTH_SHORT).show();

                            if (letter_createdBy_en == "null") {
                                module.letterCreatedBy = letter_createdBy_np;
                            } else {
                                module.letterCreatedBy = letter_createdBy_en;
                            }

                            module.letterId = letter_id;
                            module.letterApplicant = letter_applicant;
                            module.letterRegistrationId = letter_registration_id;
                            module.letterRegistrationNo = letter_registration_no;
                            module.letterTo = letter_to;
                            module.letterContent = letter_content;
                            module.letterCC = letter_cc;
                            module.letterSubject = letter_subject;
                            assignedLetterList.add(module);


                        }
                    } else {
                        Toast.makeText(AssignedLetters.this, "Error", Toast.LENGTH_SHORT).show();
                    }
                    display();
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {

            }
        });
        RequestQueue requestQueue = Volley.newRequestQueue(getApplicationContext());
        requestQueue.add(stringRequest);

    }

    private void display() {
        assignedLetterListsView.setAdapter(new AssignedLetterAdapter(getApplicationContext(), assignedLetterList));
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        Intent i = new Intent(getApplicationContext(), MainActivity.class);
        startActivity(i);

        return true;
    }

    private class AssignedLetterAdapter extends BaseAdapter {
        Context c;
        ArrayList<Module> data = new ArrayList<>();
        LayoutInflater inflater;


        public AssignedLetterAdapter(Context applicationContext, ArrayList<Module> assignedLetterList) {
            c = applicationContext;
            data = assignedLetterList;
            inflater = (LayoutInflater) c.getSystemService(c.LAYOUT_INFLATER_SERVICE);
        }

        @Override
        public int getCount() {
            return data.size();
        }

        @Override
        public Object getItem(int position) {
            return position;
        }

        @Override
        public long getItemId(int position) {
            return position;
        }

        @Override
        public View getView(int position, View convertView, ViewGroup parent) {
            ViewHolder holder;
            if (convertView == null) {
                holder = new ViewHolder();
                convertView = inflater.inflate(R.layout.assigned_letters_single_item, null);
                holder.assignedByLetter = (TextView) convertView.findViewById(R.id.assignedByLetter);
                convertView.setTag(holder);
            } else {
                holder = (ViewHolder) convertView.getTag();
            }

            holder.assignedByLetter.setText(data.get(position).assignedByLetter());

            final String letter_id_no = data.get(position).getLetterNo();
            final String letter_applicant_name = data.get(position).getApplicantName();
            final String letter_registration_id = data.get(position).getRegistrationId();
            final String letter_registration_no = data.get(position).getRegistrationNO();
            final String letter__to = data.get(position).getLetterTo();
            final String letter__content = data.get(position).getLetterContent();
            final String letter__cc = data.get(position).getLetterCC();
            final String letter_created_by = data.get(position).assignedByLetter();
            final String letter__subject = data.get(position).getLetterSubject();

            convertView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if (letter_registration_no.equals("null")) {
                        Intent i = new Intent(getApplicationContext(), AssignedLetterWithoutRegistrationNo.class);
                        Bundle bundle = new Bundle();
                        bundle.putString("LetterID",letter_id_no);
                        bundle.putString("LetterTo",letter__to);
                        bundle.putString("LetterContent",letter__content);
                        bundle.putString("LetterCC",letter__cc);
                        bundle.putString("LetterSubject",letter__subject);
                        i.putExtras(bundle);
                        startActivity(i);
                    }
                    else {
                        Intent i = new Intent(getApplicationContext(), AssignedLetterWithRegistrationNo.class);
                        Bundle bundle = new Bundle();
                        bundle.putString("LetterID",letter_id_no);
                        bundle.putString("LetterTo",letter__to);
                        bundle.putString("LetterContent",letter__content);
                        bundle.putString("LetterCC",letter__cc);
                        bundle.putString("LetterSubject",letter__subject);
                        bundle.putString("LetterRegistrationID",letter_registration_id);
                        bundle.putString("LetterRegistrationNo",letter_registration_no);
                        bundle.putString("LetterApplicantName",letter_applicant_name);
                        i.putExtras(bundle);
                        startActivity(i);
                    }
                }
            });

            return convertView;

        }

        private class ViewHolder {
            public TextView assignedByLetter;
        }
    }
}
