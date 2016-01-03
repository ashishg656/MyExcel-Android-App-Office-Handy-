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
import myexcel.ashish.com.myexcel.objects.HomeActivityObject;

/**
 * Created by Ashish Goel on 1/3/2016.
 */
public class HomeActivityListAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> implements AppConstants {

    Context context;
    List<HomeActivityObject> mData;
    boolean isMoreAllowed;
    MyClickListener clickListener;

    public HomeActivityListAdapter(Context context, List<HomeActivityObject> mData, boolean isMoreAllowed) {
        this.context = context;
        this.mData = mData;
        this.isMoreAllowed = isMoreAllowed;
        clickListener = new MyClickListener();
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
