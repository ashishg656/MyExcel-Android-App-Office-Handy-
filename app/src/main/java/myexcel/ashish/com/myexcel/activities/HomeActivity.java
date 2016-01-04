package myexcel.ashish.com.myexcel.activities;

import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.view.View;

import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.google.gson.Gson;

import myexcel.ashish.com.myexcel.R;
import myexcel.ashish.com.myexcel.adapters.HomeActivityListAdapter;
import myexcel.ashish.com.myexcel.application.ZApplication;
import myexcel.ashish.com.myexcel.extras.ZUrls;
import myexcel.ashish.com.myexcel.objects.HomeActivityListObject;
import myexcel.ashish.com.myexcel.objects.HomeActivityObject;

public class HomeActivity extends BaseActivity implements ZUrls {

    RecyclerView recyclerView;
    LinearLayoutManager layoutManager;
    HomeActivityListAdapter adapter;

    boolean isRequestRunning;
    Integer nextPage = 1;
    public static final int REQUESt_CODE_ADD_WORK = 348;
    public static final int REQUESt_CODE_EDIT_WORK = 350;
    boolean isMoreAllowed = true;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        setProgressLayoutVariablesAndErrorVariables();

        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent i = new Intent(HomeActivity.this, AddWorkActivity.class);
                startActivityForResult(i, REQUESt_CODE_ADD_WORK);
            }
        });

        recyclerView = (RecyclerView) findViewById(R.id.recyclerhome);
        layoutManager = new LinearLayoutManager(this);
        recyclerView.setLayoutManager(layoutManager);

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

    private void loadData() {
        if (adapter == null) {
            showLoadingLayout();
            hideErrorLayout();
        }
        String url = getWorksListUrl + "?pagenumber=" + nextPage;
        StringRequest req = new StringRequest(Request.Method.POST, url,
                new Response.Listener<String>() {

                    @Override
                    public void onResponse(String res) {
                        if (adapter == null) {
                            hideErrorLayout();
                            hideLoadingLayout();
                        }
                        HomeActivityListObject obj = new Gson().fromJson(res,
                                HomeActivityListObject.class);
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
        });
        ZApplication.getInstance().addToRequestQueue(req, getWorksListUrl);
    }

    protected void setAdapterData(HomeActivityListObject obj) {
        nextPage = obj.getNext_page();
        if (nextPage == null) {
            isMoreAllowed = false;
        }
        if (adapter == null) {
            adapter = new HomeActivityListAdapter(this,
                    obj.getWorks(), isMoreAllowed);
            recyclerView.setAdapter(adapter);
        } else {
            adapter.addData(obj.getWorks(), isMoreAllowed);
        }
    }


    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == REQUESt_CODE_ADD_WORK && resultCode == RESULT_OK) {
            HomeActivityObject obj = data.getParcelableExtra("obj");

            adapter.addDataAtFirstPosition(obj);
        } else if (requestCode == REQUESt_CODE_EDIT_WORK && resultCode == RESULT_OK) {
            HomeActivityObject obj = data.getParcelableExtra("obj");

            adapter.notifyThatDataEdited(obj);
        }
    }
}
