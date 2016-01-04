package myexcel.ashish.com.myexcel.activities;

import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AlertDialog;
import android.support.v7.widget.Toolbar;
import android.view.MenuItem;
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
import myexcel.ashish.com.myexcel.objects.HomeActivityObject;
import myexcel.ashish.com.myexcel.objects.SaveWorkObject;

/**
 * Created by Ashish Goel on 1/3/2016.
 */
public class AddWorkActivity extends BaseActivity implements ZUrls {

    EditText toolNumber, jwNumber, product, description, startDate, targetDate, doneBy, actualDate, cost, status, remarks;
    LinearLayout send;
    ProgressDialog progressDialog;
    HomeActivityObject obj;

    boolean isEditing = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.add_work_activity_layout);

        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        toolNumber = (EditText) findViewById(R.id.toolnumber);
        jwNumber = (EditText) findViewById(R.id.jwnumber);
        product = (EditText) findViewById(R.id.product);
        description = (EditText) findViewById(R.id.desc);
        startDate = (EditText) findViewById(R.id.startdate);
        targetDate = (EditText) findViewById(R.id.targetdate);
        doneBy = (EditText) findViewById(R.id.doneby);
        actualDate = (EditText) findViewById(R.id.actualdate);
        cost = (EditText) findViewById(R.id.cost);
        status = (EditText) findViewById(R.id.status);
        remarks = (EditText) findViewById(R.id.remarks);
        send = (LinearLayout) findViewById(R.id.accept);

        try {
            isEditing = getIntent().getExtras().getBoolean("isEditing");
            obj = getIntent().getExtras().getParcelable("obj");
        } catch (Exception e) {

        }

        if (isEditing) {
            toolNumber.setText(obj.getToolNumber());
            jwNumber.setText(obj.getJwNumber());
            product.setText(obj.getProduct());
            description.setText(obj.getDescription());
            startDate.setText(obj.getStartDate());
            targetDate.setText(obj.getTargetDate());
            doneBy.setText(obj.getDoneBy());
            actualDate.setText(obj.getActualDateOfCompletion());
            cost.setText(obj.getCost());
            status.setText(obj.getStatus());
            remarks.setText(obj.getRemarks());
        }

        send.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                sendDataToServer();
            }
        });
    }

    private void sendDataToServer() {
        progressDialog = ProgressDialog.show(this, "Adding", "Wait", true, false);
        if (!isEditing)
            obj = new HomeActivityObject();
        obj.setToolNumber(toolNumber.getText().toString().trim());
        obj.setJwNumber(jwNumber.getText().toString().trim());
        obj.setProduct(product.getText().toString().trim());
        obj.setDescription(description.getText().toString().trim());
        obj.setStartDate(startDate.getText().toString().trim());
        obj.setTargetDate(targetDate.getText().toString().trim());
        obj.setDoneBy(doneBy.getText().toString().trim());
        obj.setActualDateOfCompletion(actualDate.getText().toString().trim());
        obj.setCost(cost.getText().toString().trim());
        obj.setStatus(status.getText().toString().trim());
        obj.setRemarks(remarks.getText().toString().trim());

        StringRequest req = new StringRequest(Request.Method.POST, addWorkUrl, new Response.Listener<String>() {
            @Override
            public void onResponse(String s) {
                progressDialog.dismiss();

                SaveWorkObject saved = new Gson().fromJson(s, SaveWorkObject.class);
                obj.setId(saved.getId());

                Intent data = new Intent();
                data.putExtra("obj", obj);
                setResult(RESULT_OK, data);
                AddWorkActivity.this.finish();
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError volleyError) {
                progressDialog.dismiss();
                ((BaseActivity) AddWorkActivity.this).makeToast("Error. Check net");
            }
        }) {
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                HashMap<String, String> p = new HashMap<>();
                p.put("tool_number", toolNumber.getText().toString().trim());
                p.put("jw_number", jwNumber.getText().toString().trim());
                p.put("product", product.getText().toString().trim());
                p.put("desc", description.getText().toString().trim());
                p.put("startdate", startDate.getText().toString().trim());
                p.put("targetdate", targetDate.getText().toString().trim());
                p.put("doneby", doneBy.getText().toString().trim());
                p.put("actualdate", actualDate.getText().toString().trim());
                p.put("cost", cost.getText().toString().trim());
                p.put("status", status.getText().toString().trim());
                p.put("remarks", remarks.getText().toString().trim());
                if (isEditing) {
                    p.put("id_work", obj.getId() + "");
                }
                return p;
            }
        };
        ZApplication.getInstance().addToRequestQueue(req, addWorkUrl);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (item.getItemId() == android.R.id.home) {
            onBackPressed();
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    public void onBackPressed() {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setMessage("Are you sure you want to discard this post?");
        builder.setPositiveButton("YES",
                new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {
                        setResult(RESULT_CANCELED);
                        AddWorkActivity.this.finish();
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
