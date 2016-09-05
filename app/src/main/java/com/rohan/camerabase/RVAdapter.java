package com.rohan.camerabase;

import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

/**
 * Created by Rohan Sampson on 5/25/2016.
 */
public class RVAdapter extends RecyclerView.Adapter<RVAdapter.ViewHolder>{
    private String[] RVDataHeader;
    private String[] RVDataFooter;

    public RVAdapter(String[] RVDataHeader, String[] RVDataFooter) {
        this.RVDataFooter = RVDataFooter;
        this.RVDataHeader = RVDataHeader;
    }

//    public void setMailImages(ViewHolder holder){
//        ImageView leftimg = (ImageView) holder.view.findViewById(R.id.rvLeftImage);
//        ImageView rightimg = (ImageView) holder.view.findViewById(R.id.rvLeftImage);
//        leftimg.setImageResource(R.drawable.ic_drafts_black_24dp);
//        rightimg.setImageResource(R.drawable.ic_chat_black_24dp);
//    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.rvcard,parent,false);
        v.setPadding(3,3,3,3);
        ViewHolder viewHolder = new ViewHolder(v);
        return viewHolder;
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {
        TextView tvheader = (TextView) holder.view.findViewById(R.id.rvTextHeader);
        TextView tvfooter = (TextView) holder.view.findViewById(R.id.rvTextFooter);
        tvheader.setText(RVDataHeader[position]);
        tvfooter.setText(RVDataFooter[position]);

    }

    @Override
    public int getItemCount() {
        return RVDataHeader.length;
    }

    public static class ViewHolder extends RecyclerView.ViewHolder{

        public View view;
        public ViewHolder(View v) {
            super(v);
            view = v;
        }
    }
}
