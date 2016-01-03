package myexcel.ashish.com.myexcel.activities;

import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
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

public class HomeActivity extends BaseActivity implements ZUrls {

    RecyclerView recyclerView;
    LinearLayoutManager layoutManager;
    HomeActivityListAdapter adapter;

    boolean isRequestRunning;
    Integer nextPage = 1;
    boolean isMoreAllowed = true;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Snackbar.make(view, "Replace with your own action", Snackbar.LENGTH_LONG)
                        .setAction("Action", null).show();
            }
        });

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
}
