package com.example.wang.zzj.widget;

import android.content.Context;
import android.content.res.TypedArray;
import android.text.TextUtils;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.wang.zzj.R;
import com.pnikosis.materialishprogress.ProgressWheel;

/**
 * Created by wang on 2015/12/10.
 */
public class CommonToolBar extends FrameLayout implements View.OnClickListener {

    private ImageView mDrawerBtn;
    private boolean mDrawerBtnVisible;
    private ImageView mBackBtn;
    private boolean mBackBtnVisible;
    private int mBackBtnSrc;
    private TextView mBackBtnText;
    private String mBackBtnSting;
    private View mLeftClickView;

    private TextView mTitle;
    private String mTitleSting;

    private ImageView mOverflowBtn;
    private boolean mOverflowBtnVisible;
    private int mOverflowBtnSrc;
    private TextView mOverflowBtnText;
    private String mOverflowBtnTextSting;
    private View mRightClickView;

    private ProgressWheel mProgressWheel;

    private OnToolBarClickListener mListener;

    private OnToolBarLeftClickListener mLeftClickListener;

    private OnToolBarRightClickListener mRightClickListener;


    public CommonToolBar(Context context, AttributeSet attrs) {
        super(context, attrs);

        LayoutInflater.from(context).inflate(R.layout.new_commom_tool_bar, this, true);//表示父控件把子控件布局放入进来
        mDrawerBtn = (ImageView) findViewById(R.id.drawer_btn);
        mBackBtn = (ImageView) findViewById(R.id.back_btn);
        mBackBtnText = (TextView) findViewById(R.id.text_back_btn);
        mLeftClickView = findViewById(R.id.left_click_view);

        mTitle = (TextView) findViewById(R.id.common_tool_bar_title);

        mOverflowBtnText = (TextView) findViewById(R.id.text_overflow_btn);
        mOverflowBtn = (ImageView) findViewById(R.id.overflow_btn);
        mRightClickView = findViewById(R.id.right_click_view);

        mProgressWheel = (ProgressWheel) findViewById(R.id.progress_wheel);

        TypedArray a = context.getTheme().obtainStyledAttributes(attrs, R.styleable.CommonToolBar, 0, 0);//自定义View

        try {
            mDrawerBtnVisible = a.getBoolean(R.styleable.CommonToolBar_drawerBtnVisible, false);
            mBackBtnVisible = a.getBoolean(R.styleable.CommonToolBar_backBtnVisible, false);
            mBackBtnSrc = a.getInt(R.styleable.CommonToolBar_backBtnSrc, R.drawable.arrow_left);
            mBackBtnSting = a.getString(R.styleable.CommonToolBar_backBtnText);

            mTitleSting = a.getString(R.styleable.CommonToolBar_toolBarTitle);

            mOverflowBtnVisible = a.getBoolean(R.styleable.CommonToolBar_overflowBtnVisible, false);
            mOverflowBtnSrc = a.getInt(R.styleable.CommonToolBar_overflowBtnSrc, R.drawable.abc_ic_menu_moreoverflow_mtrl_alpha);
            mOverflowBtnTextSting = a.getString(R.styleable.CommonToolBar_overflowBtnText);
        }finally {
            a.recycle();
        }

        if(mDrawerBtnVisible){
            mDrawerBtn.setVisibility(VISIBLE);
        }else {
            mDrawerBtn.setVisibility(GONE);
        }

        if(mBackBtnVisible){
            mBackBtn.setVisibility(VISIBLE);
            mBackBtn.setImageResource(mBackBtnSrc);
        }else {
            mBackBtn.setVisibility(GONE);
        }

        mLeftClickView.setOnClickListener(this);
        if(mDrawerBtnVisible || mBackBtnVisible){
            mLeftClickView.setEnabled(true);
        }else {
            mLeftClickView.setEnabled(false);
        }

        if(mBackBtnSting != null){
            mBackBtnText.setVisibility(VISIBLE);
            mBackBtnText.setText(mBackBtnSting);
        }else {
            mBackBtnText.setVisibility(GONE);
        }

        if(mTitleSting != null){
            mTitle.setVisibility(VISIBLE);
            mTitle.setText(mTitleSting);
        }else {
            mTitle.setVisibility(GONE);
        }

        if(mOverflowBtnTextSting != null){
            mOverflowBtnText.setVisibility(VISIBLE);
            mOverflowBtnText.setText(mOverflowBtnTextSting);
        }else {
            mOverflowBtnText.setVisibility(GONE);
        }

        if(mOverflowBtnVisible){
            mOverflowBtn.setVisibility(VISIBLE);
            mOverflowBtn.setImageResource(mOverflowBtnSrc);
        }else {
            mOverflowBtn.setVisibility(GONE);
        }

        mRightClickView.setOnClickListener(this);
        if(mOverflowBtnTextSting != null || mOverflowBtnVisible){
           mRightClickView.setEnabled(true);
        }else {
            mRightClickView.setEnabled(false);
        }

        mProgressWheel.setVisibility(GONE);

    }

    public void setDrawerBtnVisible(boolean visible){
        mDrawerBtn.setVisibility(visible ? VISIBLE : GONE);
        mLeftClickView.setEnabled(visible);
    }

    public void setBackBtnVisible(boolean visible){
        mBackBtn.setImageResource(mBackBtnSrc);
        mBackBtn.setVisibility(visible ? VISIBLE : GONE);
        mLeftClickView.setEnabled(visible);
    }

    public void setBackBtnSRc(int src){
        mBackBtnSrc = src;
        setBackBtnVisible(true);
    }

    public void setBackBtnText(String text){
        mBackBtnSting = text;
        mBackBtnText.setVisibility(TextUtils.isEmpty(text) ? GONE : VISIBLE);
        mBackBtnText.setText(mBackBtnSting);
        mLeftClickView.setEnabled(!TextUtils.isEmpty(text));
    }

    public String getBackBtnText(){
        return mBackBtnSting;
    }


    public void setTitle(String title){
        mTitleSting = title;
        mTitle.setVisibility(title == null ? GONE : VISIBLE);
        mTitle.setText(mTitleSting);
    }

    public String getTitle(){
        return mTitleSting;
    }

    public void setOverflowBtnVisible(boolean visible){
        mOverflowBtn.setImageResource(mOverflowBtnSrc);
        mOverflowBtn.setVisibility(visible ? VISIBLE : GONE);
        mRightClickView.setEnabled(visible);
    }

    public void setOverflowBtnSrc(int src){
        mOverflowBtnSrc = src;
        setOverflowBtnVisible(true);
    }

    public void setOVerflowBtnText(String text){
        mOverflowBtnTextSting = text;
        mOverflowBtnText.setVisibility(TextUtils.isEmpty(text) ? GONE : VISIBLE);
        mOverflowBtnText.setText(mOverflowBtnTextSting);
        mRightClickView.setEnabled(!TextUtils.isEmpty(text));
    }

    public String getOverflowBtnText(){
        return mOverflowBtnTextSting;
    }

    public void setProgressWheelVisible(boolean visible){
        mProgressWheel.setVisibility(visible ? VISIBLE : GONE);
    }

    public void setOnToolBarClickListener(OnToolBarClickListener listener){
        this.mListener = listener;
    }

    public OnToolBarClickListener getListener(){
        return mListener;
    }

    public void setEnable(boolean left, boolean right){
        mLeftClickView.setEnabled(left);
        mRightClickView.setEnabled(right);
    }


    @Override
    public void onClick(View v) {
        if(mListener != null){
            if(v.getId() == R.id.left_click_view){
                mListener.onLeftBtnClick();
            }
            else {
                mListener.onRightBtnClick();
            }
        }
    }

    public interface OnToolBarLeftClickListener{
        void onLeftBtnClick();
    }

    public interface OnToolBarRightClickListener{
        void onRightBtnClick();
    }


    public interface OnToolBarClickListener {
        void onLeftBtnClick();

        void onRightBtnClick();
    }
}
