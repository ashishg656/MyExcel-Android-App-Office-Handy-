package myexcel.ashish.com.myexcel.adapters;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;
import android.widget.TextView;

import java.util.List;

import myexcel.ashish.com.myexcel.R;
import myexcel.ashish.com.myexcel.extras.AppConstants;
import myexcel.ashish.com.myexcel.objects.WorkDetialObject;

/**
 * Created by Ashish Goel on 1/3/2016.
 */
public class WorkDetailActivityListAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> implements AppConstants {

    Context context;
    List<WorkDetialObject> mData;
    boolean isMoreAllowed;
    MyClickListener clickListener;

    public WorkDetailActivityListAdapter(Context context, List<WorkDetialObject> mData, boolean isMoreAllowed) {
        this.context = context;
        this.mData = mData;
        this.isMoreAllowed = isMoreAllowed;
        clickListener = new MyClickListener();
    }

    public void addData(List<WorkDetialObject> data, boolean isMore) {
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

    public void addDataAtFirstPosition(WorkDetialObject obj) {
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
        }
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
