package np.com.arts.dlh;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
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

    RecyclerView assignedLetterRecyclerView;
    ArrayList<Module> assignedLetterList = new ArrayList<>();
    AssignedLetterAdapter assignedLetterAdapter;

    Toolbar toolbar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_assigned_letters);

        final View snackBarPosition = findViewById(R.id.activity_assigned_letters);

        toolbar = (Toolbar) findViewById(R.id.id_toolbar_without_logo);
        setSupportActionBar(toolbar);
        getSupportActionBar().setTitle("Assigned Letter");

        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        final ProgressDialog progressDialog = new ProgressDialog(AssignedLetters.this);
        progressDialog.setMessage("Loading");
        progressDialog.setCancelable(false);
        progressDialog.show();

        SharedPreferences preferencesState = getSharedPreferences(filename, Context.MODE_PRIVATE);
        String userID = preferencesState.getString(id_key, "");

        assignedLetterRecyclerView = (RecyclerView) findViewById(R.id.assignedLetterRecyclerView);

        assignedLetterAdapter = new AssignedLetterAdapter(AssignedLetters.this, assignedLetterList);
        assignedLetterRecyclerView.setAdapter(assignedLetterAdapter);
        assignedLetterRecyclerView.setLayoutManager(new LinearLayoutManager(AssignedLetters.this));


        StringRequest stringRequest = new StringRequest(Request.Method.GET, Server.assignedUrl + userID, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                try {
                    JSONObject obj = new JSONObject(response);
                    String status = obj.getString("status");

                    if (status.equals("success")) {
                        JSONArray assignments = obj.getJSONArray("assignments");
                        for (int i = 0; i < assignments.length(); i++) {
                            Module module = new Module();
                            JSONObject c = assignments.getJSONObject(i);
                            String letter_id = c.getString("id").trim();
                            String letter_createdBy_en = c.getString("created_name_en").trim();
                            String letter_createdBy_np = c.getString("created_name_np").trim();
                            String letter_applicant = c.getString("applicant").trim();
                            String letter_registration_id = c.getString("registration_id").trim();
                            String letter_registration_no = c.getString("registration_number").trim();
                            String letter_to = c.getString("letter_to").trim();
                            String letter_content = c.getString("letter").trim();
                            String letter_cc = c.getString("cc").trim();
                            String letter_subject = c.getString("subject").trim();

                            progressDialog.dismiss();
//                            Toast.makeText(AssignedLetters.this, letter_id, Toast.LENGTH_SHORT).show();

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
                        progressDialog.dismiss();
                        Snackbar.make(snackBarPosition,"No Assignment Found",Snackbar.LENGTH_SHORT).show();
                    }
                    display();
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {

                progressDialog.dismiss();
                Snackbar.make(snackBarPosition,"No Internet Connection",Snackbar.LENGTH_SHORT).show();
            }
        });
        RequestQueue requestQueue = Volley.newRequestQueue(getApplicationContext());
        requestQueue.add(stringRequest);

    }

    private void display() {
        assignedLetterAdapter.notifyItemRangeChanged(0, assignedLetterList.size());
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        Intent i = new Intent(getApplicationContext(), MainActivity.class);
//        i.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_NEW_TASK);
        i.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_NEW_TASK);
        startActivity(i);

        return true;
    }

    private class AssignedLetterAdapter extends RecyclerView.Adapter<AssignedLetterAdapter.ViewHolder> {
        ArrayList<Module> data = new ArrayList<>();
        LayoutInflater inflater;


        public AssignedLetterAdapter(Context context, ArrayList<Module> list) {
            data = list;
            inflater = LayoutInflater.from(context);
        }

        @Override
        public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
            View view = inflater.inflate(R.layout.assigned_letters_single_item, parent, false);
            ViewHolder holder = new ViewHolder(view);
            return holder;
        }

        @Override
        public void onBindViewHolder(ViewHolder holder, int position) {
            holder.assignedByLetter.setText(data.get(position).assignedByLetter());

            final String letter_id_no = data.get(position).getLetterNo();
            final String letter_applicant_name = data.get(position).getApplicantName();
            final String letter_registration_id = data.get(position).getRegistrationId();
            final String letter_registration_no = data.get(position).getRegistrationNO();
            final String letter__to = data.get(position).getLetterTo();
            final String letter__content = data.get(position).getLetterContent();
            final String letter__cc = data.get(position).getLetterCC();
            final String letter__subject = data.get(position).getLetterSubject();

            holder.itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if (letter_registration_no.equals("null")) {
                        Intent i = new Intent(getApplicationContext(), AssignedLetterWithoutRegistrationNo.class);
                        Bundle bundle = new Bundle();
                        bundle.putString("LetterID", letter_id_no);
                        bundle.putString("LetterTo", letter__to);
                        bundle.putString("LetterContent", letter__content);
                        bundle.putString("LetterCC", letter__cc);
                        bundle.putString("LetterSubject", letter__subject);
                        i.putExtras(bundle);
                        startActivity(i);
                    } else {
                        Intent i = new Intent(getApplicationContext(), AssignedLetterWithRegistrationNo.class);
                        Bundle bundle = new Bundle();
                        bundle.putString("LetterID", letter_id_no);
                        bundle.putString("LetterTo", letter__to);
                        bundle.putString("LetterContent", letter__content);
                        bundle.putString("LetterCC", letter__cc);
                        bundle.putString("LetterSubject", letter__subject);
                        bundle.putString("LetterRegistrationID", letter_registration_id);
                        bundle.putString("LetterRegistrationNo", letter_registration_no);
                        bundle.putString("LetterApplicantName", letter_applicant_name);
                        i.putExtras(bundle);
                        startActivity(i);
                    }
                }
            });
        }

        @Override
        public int getItemCount() {
            return data.size();
        }

        class ViewHolder extends RecyclerView.ViewHolder {
            TextView assignedByLetter;

            public ViewHolder(final View itemView) {
                super(itemView);

                assignedByLetter = (TextView) itemView.findViewById(R.id.assignedByLetter);
            }
        }
    }
}
