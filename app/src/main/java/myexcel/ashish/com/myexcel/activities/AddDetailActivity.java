package myexcel.ashish.com.myexcel.activities;

import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AlertDialog;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.EditText;
import android.widget.LinearLayout;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.google.gson.Gson;

import java.util.HashMap;
import java.util.Map;

import myexcel.ashish.com.myexcel.R;
import myexcel.ashish.com.myexcel.application.ZApplication;
import myexcel.ashish.com.myexcel.extras.ZUrls;
import myexcel.ashish.com.myexcel.objects.SaveWorkObject;
import myexcel.ashish.com.myexcel.objects.WorkDetailObject;

/**
 * Created by Ashish Goel on 1/4/2016.
 */
public class AddDetailActivity extends BaseActivity implements ZUrls {

    EditText expectedDate, actualDate, trialDate, qcDate, actionTaken, status, cost, remarks;
    LinearLayout addButton;
    ProgressDialog progressDialog;
    WorkDetailObject object;

    int workId;
    boolean isEditing = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.add_detail_activity_layout);

        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        try {
            workId = getIntent().getExtras().getInt("workid");
        } catch (Exception e) {

        }

        addButton = (LinearLayout) findViewById(R.id.accept);
        expectedDate = (EditText) findViewById(R.id.expecteddate);
        actualDate = (EditText) findViewById(R.id.actualdate);
        trialDate = (EditText) findViewById(R.id.trialdate);
        qcDate = (EditText) findViewById(R.id.qcdate);
        actionTaken = (EditText) findViewById(R.id.actiontaken);
        status = (EditText) findViewById(R.id.status);
        cost = (EditText) findViewById(R.id.cost);
        remarks = (EditText) findViewById(R.id.remarks);

        addButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                sendDataToServer();
            }
        });

        try {
            isEditing = getIntent().getExtras().getBoolean("isEditing");
            object = getIntent().getExtras().getParcelable("obj");
        } catch (Exception e) {

        }

        if (isEditing) {
            expectedDate.setText(object.getExpectedDate());
            actualDate.setText(object.getActualDate());
            trialDate.setText(object.getTrialDate());
            qcDate.setText(object.getQcDate());
            actionTaken.setText(object.getActionTaken());
            status.setText(object.getStatus());
            cost.setText(object.getCost());
            remarks.setText(object.getRemarks());
        }
    }

    private void sendDataToServer() {
        progressDialog = ProgressDialog.show(this, "Adding", "Wait", true, false);
        if (!isEditing)
            object = new WorkDetailObject();
        object.setExpectedDate(expectedDate.getText().toString().trim());
        object.setActualDate(actualDate.getText().toString().trim());
        object.setTrialDate(trialDate.getText().toString().trim());
        object.setQcDate(qcDate.getText().toString().trim());
        object.setActionTaken(actionTaken.getText().toString().trim());
        object.setStatus(status.getText().toString().trim());
        object.setCost(cost.getText().toString().trim());
        object.setRemarks(remarks.getText().toString().trim());

        StringRequest req = new StringRequest(Request.Method.POST, addDetailUrl, new Response.Listener<String>() {
            @Override
            public void onResponse(String s) {
                progressDialog.dismiss();

                SaveWorkObject saved = new Gson().fromJson(s, SaveWorkObject.class);
                object.setId(saved.getId());

                Intent data = new Intent();
                data.putExtra("obj", object);
                setResult(RESULT_OK, data);
                AddDetailActivity.this.finish();
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError volleyError) {
                progressDialog.dismiss();
                ((BaseActivity) AddDetailActivity.this).makeToast("Error. Check net");
            }
        }) {
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                HashMap<String, String> p = new HashMap<>();
                p.put("work_id", workId + "");
                p.put("expected_date", object.getExpectedDate());
                p.put("actual_date", object.getActualDate());
                p.put("trial_date", object.getTrialDate());
                p.put("qc_date", object.getQcDate());
                p.put("action_taken", object.getActionTaken());
                p.put("status", object.getStatus());
                p.put("cost", object.getCost());
                p.put("remarks", object.getRemarks());
                if (isEditing) {
                    p.put("id_detail", object.getId() + "");
                }
                return p;
            }
        };
        ZApplication.getInstance().addToRequestQueue(req, addDetailUrl);
    }

    @Override
    public void onBackPressed() {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setMessage("Are you sure you want to discard this post?");
        builder.setPositiveButton("YES",
                new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {
                        setResult(RESULT_CANCELED);
                        AddDetailActivity.this.finish();
                    }
                });
        builder.setNegativeButton("NO",
                new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {
                    }
                });
        AlertDialog dialog = builder.create();
        dialog.show();
    }
}
