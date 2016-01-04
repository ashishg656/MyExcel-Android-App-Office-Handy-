package myexcel.ashish.com.myexcel.activities;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.view.View;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.google.gson.Gson;

import java.util.HashMap;
import java.util.Map;

import myexcel.ashish.com.myexcel.R;
import myexcel.ashish.com.myexcel.adapters.WorkDetailActivityListAdapter;
import myexcel.ashish.com.myexcel.application.ZApplication;
import myexcel.ashish.com.myexcel.extras.ZUrls;
import myexcel.ashish.com.myexcel.objects.HomeActivityObject;
import myexcel.ashish.com.myexcel.objects.WorkDetailActivityListObject;
import myexcel.ashish.com.myexcel.objects.WorkDetailObject;

/**
 * Created by Ashish Goel on 1/4/2016.
 */
public class WorkDetailActivity extends BaseActivity implements ZUrls {

    private static final int REQUEST_ADD_DETAIL = 999;
    public static final int REQUEST_EDIT_DETAIL = 989;
    HomeActivityObject homeObject;

    RecyclerView recyclerView;
    LinearLayoutManager layoutManager;
    WorkDetailActivityListAdapter adapter;

    boolean isRequestRunning;
    Integer nextPage = 1;
    boolean isMoreAllowed = true;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.work_detail_activity);

        toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        homeObject = getIntent().getParcelableExtra("obj");

        setProgressLayoutVariablesAndErrorVariables();

        recyclerView = (RecyclerView) findViewById(R.id.recyclerhome);
        layoutManager = new LinearLayoutManager(this);
        recyclerView.setLayoutManager(layoutManager);

        findViewById(R.id.fab).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                openAddDetailActivity();
            }
        });

        recyclerView.addOnScrollListener(new RecyclerView.OnScrollListener() {
            @Override
            public void onScrolled(RecyclerView recyclerView, int dx, int dy) {
                if (recyclerView.getAdapter() != null) {
                    int lastitem = layoutManager.findLastVisibleItemPosition();
                    int totalitems = recyclerView.getAdapter().getItemCount();
                    int diff = totalitems - lastitem;
                    if (diff < 6 && !isRequestRunning && isMoreAllowed) {
                        loadData();
                    }
                }
                super.onScrolled(recyclerView, dx, dy);
            }
        });

        loadData();
    }

    private void openAddDetailActivity() {
        Intent i = new Intent(this, AddDetailActivity.class);
        i.putExtra("workid", homeObject.getId());
        startActivityForResult(i, REQUEST_ADD_DETAIL);
    }

    private void loadData() {
        if (adapter == null) {
            showLoadingLayout();
            hideErrorLayout();
        }
        String url = getDetailsList + "?pagenumber=" + nextPage;
        StringRequest req = new StringRequest(Request.Method.POST, url,
                new Response.Listener<String>() {

                    @Override
                    public void onResponse(String res) {
                        if (adapter == null) {
                            hideErrorLayout();
                            hideLoadingLayout();
                        }
                        WorkDetailActivityListObject obj = new Gson().fromJson(res,
                                WorkDetailActivityListObject.class);
                        setAdapterData(obj);
                    }
                }, new Response.ErrorListener() {

            @Override
            public void onErrorResponse(VolleyError err) {
                if (adapter == null) {
                    showErrorLayout();
                    hideLoadingLayout();
                }
            }
        }) {
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                HashMap<String, String> p = new HashMap<>();
                p.put("work_id", homeObject.getId() + "");
                return p;
            }
        };
        ZApplication.getInstance().addToRequestQueue(req, getWorksListUrl);
    }

    protected void setAdapterData(WorkDetailActivityListObject obj) {
        nextPage = obj.getNext_page();
        if (nextPage == null) {
            isMoreAllowed = false;
        }
        if (adapter == null) {
            adapter = new WorkDetailActivityListAdapter(this,
                    obj.getWorks(), isMoreAllowed);
            recyclerView.setAdapter(adapter);
        } else {
            adapter.addData(obj.getWorks(), isMoreAllowed);
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == REQUEST_ADD_DETAIL && resultCode == RESULT_OK) {
            WorkDetailObject saved = data.getParcelableExtra("obj");
            adapter.addDataAtFirstPosition(saved);
        } else if (requestCode == REQUEST_EDIT_DETAIL && resultCode == RESULT_OK) {

        }
    }
}
