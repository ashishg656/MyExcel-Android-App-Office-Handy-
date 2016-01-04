package myexcel.ashish.com.myexcel.adapters;

import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.support.v7.app.AlertDialog;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;
import android.widget.TextView;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import myexcel.ashish.com.myexcel.R;
import myexcel.ashish.com.myexcel.activities.AddDetailActivity;
import myexcel.ashish.com.myexcel.activities.BaseActivity;
import myexcel.ashish.com.myexcel.activities.WorkDetailActivity;
import myexcel.ashish.com.myexcel.application.ZApplication;
import myexcel.ashish.com.myexcel.extras.AppConstants;
import myexcel.ashish.com.myexcel.extras.ZUrls;
import myexcel.ashish.com.myexcel.objects.WorkDetailObject;

/**
 * Created by Ashish Goel on 1/3/2016.
 */
public class WorkDetailActivityListAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> implements AppConstants, ZUrls {

    Context context;
    List<WorkDetailObject> mData;
    boolean isMoreAllowed;
    MyClickListener clickListener;
    MyLongClickListener longClickListener;
    int workId;

    int longClickPos;
    ProgressDialog progressDialog;

    public WorkDetailActivityListAdapter(Context context, List<WorkDetailObject> mData, boolean isMoreAllowed, int id_work) {
        this.context = context;
        this.mData = mData;
        this.isMoreAllowed = isMoreAllowed;
        clickListener = new MyClickListener();
        longClickListener = new MyLongClickListener();
        workId = id_work;
    }

    public void addData(List<WorkDetailObject> data, boolean isMore) {
        mData.addAll(data);
        this.isMoreAllowed = isMore;
        notifyDataSetChanged();
    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int type) {
        if (type == TYPE_NORMAL) {
            View v = LayoutInflater.from(context).inflate(
                    R.layout.work_detail_actiivity_list_item, parent, false);
            PostsHolderNormal holder = new PostsHolderNormal(v);
            return holder;
        } else if (type == TYPE_LOADER) {
            View v = LayoutInflater.from(context).inflate(
                    R.layout.loading_more, parent, false);
            PostHolderLoading holder = new PostHolderLoading(v);
            return holder;
        }
        return null;
    }

    public void addDataAtFirstPosition(WorkDetailObject obj) {
        mData.add(0, obj);
        notifyDataSetChanged();
    }

    class PostsHolderNormal extends RecyclerView.ViewHolder {

        TextView expectedDate, actualDate, trialDate, qcDate, actionTaken, status, cost, remarks;
        FrameLayout container;

        public PostsHolderNormal(View v) {
            super(v);
            expectedDate = (TextView) v.findViewById(R.id.expecteddate);
            actualDate = (TextView) v.findViewById(R.id.actualdate);
            trialDate = (TextView) v.findViewById(R.id.trialdate);
            qcDate = (TextView) v.findViewById(R.id.qcdate);
            actionTaken = (TextView) v.findViewById(R.id.actiontaken);
            status = (TextView) v.findViewById(R.id.status);
            cost = (TextView) v.findViewById(R.id.cost);
            remarks = (TextView) v.findViewById(R.id.remarks);
            container = (FrameLayout) v.findViewById(R.id.container);
        }
    }

    class PostHolderLoading extends RecyclerView.ViewHolder {

        public PostHolderLoading(View v) {
            super(v);
        }
    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holderC, int position) {
        position = holderC.getAdapterPosition();
        if (getItemViewType(position) == TYPE_NORMAL) {
            PostsHolderNormal holder = (PostsHolderNormal) holderC;

            holder.expectedDate.setText(mData.get(position).getExpectedDate());
            holder.actualDate.setText(mData.get(position).getActualDate());
            holder.trialDate.setText(mData.get(position).getTrialDate());
            holder.qcDate.setText(mData.get(position).getQcDate());
            holder.actionTaken.setText(mData.get(position).getActionTaken());
            holder.status.setText(mData.get(position).getStatus());
            holder.cost.setText(mData.get(position).getCost());
            holder.remarks.setText(mData.get(position).getRemarks());

            holder.container.setTag(position);
            holder.container.setOnClickListener(clickListener);
            holder.container.setOnLongClickListener(longClickListener);
        }
    }

    class MyLongClickListener implements View.OnLongClickListener {

        @Override
        public boolean onLongClick(View v) {
            if (v.getId() == R.id.container) {
                int pos = (int) v.getTag();
                longClickPos = pos;

                String[] items = {"Edit", "Delete"};
                AlertDialog.Builder builder = new AlertDialog.Builder(context);
                builder.setItems(items, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        if (which == 0) {
                            editWorkRequest();
                        } else if (which == 1) {
                            deleteWorkRequest();
                        }
                    }
                });
                builder.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {

                    }
                });
                Dialog dialog = builder.create();
                dialog.show();
            }
            return false;
        }
    }

    private void deleteWorkRequestConfirmed() {
        progressDialog = ProgressDialog.show(context, "Deleting", "Wait", true, false);
        StringRequest req = new StringRequest(Request.Method.POST, deleteDetail, new Response.Listener<String>() {
            @Override
            public void onResponse(String s) {
                mData.remove(longClickPos);
                notifyDataSetChanged();
                progressDialog.dismiss();
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError volleyError) {
                ((BaseActivity) context).makeToast("Error..Unable to delete..Check net");
                progressDialog.dismiss();
            }
        }) {
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                HashMap<String, String> p = new HashMap<>();
                p.put("id_work", mData.get(longClickPos).getId() + "");
                return p;
            }
        };
        ZApplication.getInstance().addToRequestQueue(req, deleteWork);
    }

    private void editWorkRequest() {
        Intent i = new Intent(context, AddDetailActivity.class);
        i.putExtra("isEditing", true);
        i.putExtra("obj", mData.get(longClickPos));
        i.putExtra("workid", workId);
        ((WorkDetailActivity) context).startActivityForResult(i, WorkDetailActivity.REQUEST_EDIT_DETAIL);
    }

    private void deleteWorkRequest() {
        AlertDialog.Builder builder = new AlertDialog.Builder(context);
        builder.setMessage("Are you sure to delete");
        builder.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {

            }
        });
        builder.setPositiveButton("Delete", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                deleteWorkRequestConfirmed();
            }
        });
        Dialog dialog = builder.create();
        dialog.show();
    }

    @Override
    public int getItemCount() {
        if (isMoreAllowed == true)
            return mData.size() + 1;
        return mData.size();
    }


    class MyClickListener implements View.OnClickListener {

        @Override
        public void onClick(View v) {
            if (v.getId() == R.id.container) {
                int pos = (int) v.getTag();

            }
        }
    }
}
