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

public class ApprovedLetters extends AppCompatActivity {

    static String filename = "mypreferencefile";
    static String id_key = "id";

    RecyclerView approvedLetterRecyclerView;
    ArrayList<Module> approvedLetterList = new ArrayList<>();
    ApprovedLetterAdapter approvedLetterAdapter;

    Toolbar toolbar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_approved_letters);

        final View snackBarPosition = findViewById(R.id.activity_approved_letters);

        toolbar = (Toolbar) findViewById(R.id.id_toolbar_without_logo);
        setSupportActionBar(toolbar);
        getSupportActionBar().setTitle("Approved Letter");

        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        final ProgressDialog progressDialog = new ProgressDialog(ApprovedLetters.this);
        progressDialog.setMessage("Loading");
        progressDialog.setCancelable(false);
        progressDialog.show();

        SharedPreferences preferencesState = getSharedPreferences(filename, Context.MODE_PRIVATE);
        String userID = preferencesState.getString(id_key, "");

        approvedLetterRecyclerView = (RecyclerView) findViewById(R.id.approvedLetterRecyclerView);

        approvedLetterAdapter = new ApprovedLetterAdapter(ApprovedLetters.this, approvedLetterList);
        approvedLetterRecyclerView.setAdapter(approvedLetterAdapter);
        approvedLetterRecyclerView.setLayoutManager(new LinearLayoutManager(ApprovedLetters.this));


        StringRequest stringRequest = new StringRequest(Request.Method.GET, Server.approvedUrl + userID, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                try {
                    JSONObject obj = new JSONObject(response);
                        for (int i = 0; i < 1; i++) {
                            Module module = new Module();
                            JSONObject c = obj.getJSONObject(String.valueOf(i));
                            String letter_id = c.getString("id").trim();
                            String letter_subject = c.getString("subject_id").trim();
                            Toast.makeText(ApprovedLetters.this, letter_id, Toast.LENGTH_SHORT).show();

                            progressDialog.dismiss();
//                            Toast.makeText(AssignedLetters.this, letter_id, Toast.LENGTH_SHORT).show();

                            if(i == 0){
                                Snackbar.make(snackBarPosition,"No Letters Found",Snackbar.LENGTH_SHORT).show();
                            }


                            module.letterId = letter_id;
                            module.letterSubject = letter_subject;
                            approvedLetterList.add(module);


                        }

                    display();
                } catch (JSONException e) {
                    progressDialog.dismiss();
                    Toast.makeText(ApprovedLetters.this, "catch", Toast.LENGTH_SHORT).show();
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
        approvedLetterAdapter.notifyItemRangeChanged(0, approvedLetterList.size());
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        Intent i = new Intent(getApplicationContext(), MainActivity.class);
//        i.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_NEW_TASK);
        i.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_NEW_TASK);
        startActivity(i);

        return true;
    }

    private class ApprovedLetterAdapter extends RecyclerView.Adapter<ApprovedLetterAdapter.ViewHolder> {
        ArrayList<Module> data = new ArrayList<>();
        LayoutInflater inflater;


        public ApprovedLetterAdapter(Context context, ArrayList<Module> list) {
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
            holder.assignedByLetter.setText(data.get(position).getLetterSubject());

            final String letter_id_no = data.get(position).getLetterNo();

//            holder.itemView.setOnClickListener(new View.OnClickListener() {
//                @Override
//                public void onClick(View v) {
//                    if (letter_registration_no.equals("null")) {
//                        Intent i = new Intent(getApplicationContext(), AssignedLetterWithoutRegistrationNo.class);
//                        Bundle bundle = new Bundle();
//                        bundle.putString("LetterID", letter_id_no);
//                        bundle.putString("LetterTo", letter__to);
//                        bundle.putString("LetterContent", letter__content);
//                        bundle.putString("LetterCC", letter__cc);
//                        bundle.putString("LetterSubject", letter__subject);
//                        i.putExtras(bundle);
//                        startActivity(i);
//                    } else {
//                        Intent i = new Intent(getApplicationContext(), AssignedLetterWithRegistrationNo.class);
//                        Bundle bundle = new Bundle();
//                        bundle.putString("LetterID", letter_id_no);
//                        bundle.putString("LetterTo", letter__to);
//                        bundle.putString("LetterContent", letter__content);
//                        bundle.putString("LetterCC", letter__cc);
//                        bundle.putString("LetterSubject", letter__subject);
//                        bundle.putString("LetterRegistrationID", letter_registration_id);
//                        bundle.putString("LetterRegistrationNo", letter_registration_no);
//                        bundle.putString("LetterApplicantName", letter_applicant_name);
//                        i.putExtras(bundle);
//                        startActivity(i);
//                    }
//                }
//            });
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
