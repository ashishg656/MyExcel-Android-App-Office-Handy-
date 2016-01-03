package myexcel.ashish.com.myexcel.activities;

import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.view.View;

import myexcel.ashish.com.myexcel.R;

public class HomeActivity extends AppCompatActivity {

    RecyclerView recyclerView;
    LinearLayoutManager layoutManager;

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
        String url = studentPostsUrl + "?pagenumber=" + nextPage;
        StringRequest req = new StringRequest(Method.POST, url,
                new Listener<String>() {

                    @Override
                    public void onResponse(String res) {
                        if (adapter == null) {
                            hideErrorLayout();
                            hideLoadingLayout();
                        }
                        PostsListObject obj = new Gson().fromJson(res,
                                PostsListObject.class);
                        setAdapterData(obj);
                    }
                }, new ErrorListener() {

            @Override
            public void onErrorResponse(VolleyError err) {
                System.out.print(err.networkResponse);
                if (adapter == null) {
                    showErrorLayout();
                    hideLoadingLayout();
                }
            }
        }) {
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                HashMap<String, String> p = new HashMap<>();
                p.put("user_id", ZPreferences.getUserProfileID(getActivity()));
                return p;
            }
        };
        ZApplication.getInstance().addToRequestQueue(req, teacherPostsUrl);
    }
}
