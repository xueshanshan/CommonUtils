package com.star.common_utils.widget.dialogfragment;


import android.animation.Animator;
import android.animation.AnimatorListenerAdapter;
import android.animation.ValueAnimator;
import android.graphics.Color;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.util.TypedValue;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.star.common_utils.R;
import com.star.common_utils.utils.AppInfoUtil;

import java.util.ArrayList;
import java.util.List;

/**
 * @author xueshanshan
 * @date 2019/1/29
 */
public class BottomPopListDialogFragment extends DialogFragmentFullScreen implements View.OnClickListener {

    private ValueAnimator valueAnimator;
    private RecyclerView mRecyclerView;
    private View mBottomView;
    private BottomPopListAdapter mPopListAdapter;
    private BottomPopListDialogItemClickListener mPopListDialogItemClickListener;

    private List<BottomPopItem> mBottomPopItems = new ArrayList<>();

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.dialog_fragment_bottom_pop_list, container, false);
        view.findViewById(R.id.root_view).setOnClickListener(this);
        mRecyclerView = view.findViewById(R.id.dialog_recycler_view);
        mRecyclerView.setLayoutManager(new LinearLayoutManager(getContext(), LinearLayoutManager.VERTICAL, false));
        mPopListAdapter = new BottomPopListAdapter();
        mRecyclerView.setAdapter(mPopListAdapter);
        mBottomView = view.findViewById(R.id.bottom_view);
        mBottomView.setTranslationY(AppInfoUtil.sScreenRealHeight);
        mBottomView.post(new Runnable() {
            @Override
            public void run() {
                valueAnimator = ValueAnimator.ofInt(AppInfoUtil.sScreenRealHeight, 0);
                valueAnimator.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() {
                    @Override
                    public void onAnimationUpdate(ValueAnimator animation) {
                        int value = (int) animation.getAnimatedValue();
                        mBottomView.setTranslationY(value);
                    }
                });
                valueAnimator.setDuration(300);
                valueAnimator.start();
            }
        });
        return view;
    }

    public void setBottomPopItems(List<BottomPopItem> bottomPopItems) {
        if (bottomPopItems != null) {
            mBottomPopItems = bottomPopItems;
            if (mPopListAdapter != null) {
                mPopListAdapter.notifyDataSetChanged();
            }
        }
    }

    public void setPopListDialogItemClickListener(BottomPopListDialogItemClickListener popListDialogItemClickListener) {
        mPopListDialogItemClickListener = popListDialogItemClickListener;
    }

    @Override
    public void onClick(View v) {
        if (v.getId() == R.id.root_view) {
            dismissAllowingStateLoss();
        }
    }

    @Override
    public void dismiss() {
        valueAnimator.reverse();
        valueAnimator.addListener(new AnimatorListenerAdapter() {
            @Override
            public void onAnimationEnd(Animator animation) {
                BottomPopListDialogFragment.super.dismiss();
            }
        });
    }

    @Override
    public void dismissAllowingStateLoss() {
        valueAnimator.reverse();
        valueAnimator.addListener(new AnimatorListenerAdapter() {
            @Override
            public void onAnimationEnd(Animator animation) {
                BottomPopListDialogFragment.super.dismissAllowingStateLoss();
            }
        });
    }

    private class BottomPopListAdapter extends RecyclerView.Adapter<BottomPopListViewHolder> {

        @Override
        public BottomPopListViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
            return new BottomPopListViewHolder(LayoutInflater.from(getContext()).inflate(R.layout.item_bottom_pop, parent, false));
        }

        @Override
        public void onBindViewHolder(BottomPopListViewHolder holder, int position) {
            BottomPopItem bottomPopItem = mBottomPopItems.get(position);
            holder.mBottomPopItem = bottomPopItem;
            if (bottomPopItem.height > 0) {
                holder.titleLayoutParams.height = bottomPopItem.height;
                holder.tvTitle.setLayoutParams(holder.titleLayoutParams);
            }
            if (!TextUtils.isEmpty(bottomPopItem.textColor)) {
                holder.tvTitle.setTextColor(Color.parseColor(bottomPopItem.textColor));
            }
            holder.tvTitle.setText(bottomPopItem.name);
            if (bottomPopItem.textSize > 0) {
                holder.tvTitle.setTextSize(TypedValue.COMPLEX_UNIT_PX, bottomPopItem.textSize);
            }
            holder.dividerLine.setVisibility(position == 0 ? View.GONE : View.VISIBLE);
        }

        @Override
        public int getItemCount() {
            return mBottomPopItems.size();
        }
    }

    private class BottomPopListViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {
        private View dividerLine;
        private TextView tvTitle;
        private ViewGroup.LayoutParams titleLayoutParams;

        private BottomPopItem mBottomPopItem;

        public BottomPopListViewHolder(View itemView) {
            super(itemView);
            dividerLine = itemView.findViewById(R.id.divider_line);
            tvTitle = itemView.findViewById(R.id.item_title);
            titleLayoutParams = tvTitle.getLayoutParams();
            itemView.setOnClickListener(this);
        }

        @Override
        public void onClick(View v) {
            dismissAllowingStateLoss();
            if (mPopListDialogItemClickListener != null) {
                mPopListDialogItemClickListener.onItemClick(mBottomPopItem);
            }
        }
    }

    public static class BottomPopItem {
        public String id;
        public String name;
        public String textColor;
        public int textSize;
        private int height;

        public BottomPopItem(String id, String name) {
            this.id = id;
            this.name = name;
        }

        public BottomPopItem(String id, String name, String textColor, int textSize, int height) {
            this.id = id;
            this.name = name;
            this.textColor = textColor;
            this.textSize = textSize;
            this.height = height;
        }
    }

    public interface BottomPopListDialogItemClickListener {
        void onItemClick(BottomPopItem bottomPopItem);
    }
}
