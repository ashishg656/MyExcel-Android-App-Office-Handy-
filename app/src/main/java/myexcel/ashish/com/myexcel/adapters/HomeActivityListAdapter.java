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
import myexcel.ashish.com.myexcel.activities.AddWorkActivity;
import myexcel.ashish.com.myexcel.activities.BaseActivity;
import myexcel.ashish.com.myexcel.activities.HomeActivity;
import myexcel.ashish.com.myexcel.activities.WorkDetailActivity;
import myexcel.ashish.com.myexcel.application.ZApplication;
import myexcel.ashish.com.myexcel.extras.AppConstants;
import myexcel.ashish.com.myexcel.extras.ZUrls;
import myexcel.ashish.com.myexcel.objects.HomeActivityObject;

/**
 * Created by Ashish Goel on 1/3/2016.
 */
public class HomeActivityListAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> implements AppConstants, ZUrls {

    Context context;
    List<HomeActivityObject> mData;
    boolean isMoreAllowed;
    MyClickListener clickListener;
    MyLongClickListener longClickListener;

    int longClickPos;
    ProgressDialog progressDialog;

    public HomeActivityListAdapter(Context context, List<HomeActivityObject> mData, boolean isMoreAllowed) {
        this.context = context;
        this.mData = mData;
        this.isMoreAllowed = isMoreAllowed;
        clickListener = new MyClickListener();
        longClickListener = new MyLongClickListener();
    }

    public void addData(List<HomeActivityObject> data, boolean isMore) {
        mData.addAll(data);
        this.isMoreAllowed = isMore;
        notifyDataSetChanged();
    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int type) {
        if (type == TYPE_NORMAL) {
            View v = LayoutInflater.from(context).inflate(
                    R.layout.home_actiivity_list_item, parent, false);
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

    public void addDataAtFirstPosition(HomeActivityObject obj) {
        mData.add(0, obj);
        notifyDataSetChanged();
    }

    public void notifyThatDataEdited(HomeActivityObject obj) {
        for (int i = 0; i < mData.size(); i++) {
            if (mData.get(i).getId() == obj.getId()) {
                mData.set(i, obj);
                notifyDataSetChanged();
                break;
            }
        }
    }

    class PostsHolderNormal extends RecyclerView.ViewHolder {

        TextView jwNumber, toolNumber, product, desc, targetDate, status;
        FrameLayout container;

        public PostsHolderNormal(View v) {
            super(v);
            jwNumber = (TextView) v.findViewById(R.id.jwnumber);
            toolNumber = (TextView) v.findViewById(R.id.toolnumber);
            product = (TextView) v.findViewById(R.id.product);
            desc = (TextView) v.findViewById(R.id.desc);
            targetDate = (TextView) v.findViewById(R.id.targetdate);
            status = (TextView) v.findViewById(R.id.status);
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

            holder.status.setText(mData.get(position).getStatus());
            holder.jwNumber.setText(mData.get(position).getJwNumber());
            holder.toolNumber.setText(mData.get(position).getToolNumber());
            holder.product.setText(mData.get(position).getProduct());
            holder.desc.setText(mData.get(position).getDescription());
            holder.targetDate.setText(mData.get(position).getTargetDate());

            holder.container.setTag(position);
            holder.container.setOnClickListener(clickListener);
            holder.container.setOnLongClickListener(longClickListener);
        }
    }

    @Override
    public int getItemCount() {
        if (isMoreAllowed == true)
            return mData.size() + 1;
        return mData.size();
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

    private void deleteWorkRequestConfirmed() {
        progressDialog = ProgressDialog.show(context, "Deleting", "Wait", true, false);
        StringRequest req = new StringRequest(Request.Method.POST, deleteWork, new Response.Listener<String>() {
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
        Intent i = new Intent(context, AddWorkActivity.class);
        i.putExtra("isEditing", true);
        i.putExtra("obj", mData.get(longClickPos));
        ((HomeActivity) context).startActivityForResult(i, HomeActivity.REQUESt_CODE_EDIT_WORK);
    }


    class MyClickListener implements View.OnClickListener {

        @Override
        public void onClick(View v) {
            if (v.getId() == R.id.container) {
                int pos = (int) v.getTag();
                Intent i = new Intent(context, WorkDetailActivity.class);
                i.putExtra("obj", mData.get(pos));
                context.startActivity(i);
            }
        }
    }
}
