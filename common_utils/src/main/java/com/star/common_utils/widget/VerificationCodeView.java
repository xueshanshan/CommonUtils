package com.star.common_utils.widget;

import android.animation.Animator;
import android.animation.ObjectAnimator;
import android.animation.ValueAnimator;
import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Color;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.util.AttributeSet;
import android.util.TypedValue;
import android.view.Gravity;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.star.common_utils.R;

/**
 * @author xueshanshan
 * @date 2018/8/31
 */
public class VerificationCodeView extends RelativeLayout implements TextWatcher, View.OnKeyListener {
    private Context mContext;
    private static final int INIT_POS = 0;

    private int mVerifyNum;
    private float mTextSize;
    private int mVerifyNormalBg;
    private int mVerifyHighlightBg;
    private int mVerifyNormalColor;
    private int mVerifyErrorBg;
    private int mVerifyErrorColor;
    private float mVerifyWidth;
    private float mVerifyHeight;
    private float mVerifyMargin;
    private float mVerifyErrorTextSize;

    private LinearLayout mTvContainer;
    private EditText mEditText;
    private TextView mTvErrorInfo;
    private int mCurPos = INIT_POS;
    private VerificationCodeCallBack mVerificationCodeCallBack;
    private ObjectAnimator shakeAnimator;

    public VerificationCodeView(Context context) {
        this(context, null);
    }

    public VerificationCodeView(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public VerificationCodeView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        mContext = context;
        initAttrs(attrs);
        initViews();
    }

    public void setVerificationCodeCallBack(VerificationCodeCallBack verificationCodeCallBack) {
        mVerificationCodeCallBack = verificationCodeCallBack;
    }

    private void initAttrs(AttributeSet attrs) {
        TypedArray array = mContext.obtainStyledAttributes(attrs, R.styleable.VerificationCodeView);
        mVerifyNum = array.getInt(R.styleable.VerificationCodeView_verify_num, 4);
        mTextSize = array.getDimension(R.styleable.VerificationCodeView_android_textSize, 81);
        mVerifyNormalBg = array.getResourceId(R.styleable.VerificationCodeView_verify_normal_bg, R.drawable.verify_normal_bg);
        mVerifyHighlightBg = array.getResourceId(R.styleable.VerificationCodeView_verify_highlight_bg, R.drawable.verify_highlight_bg);
        mVerifyErrorBg = array.getResourceId(R.styleable.VerificationCodeView_verify_error_bg, R.drawable.verify_error_bg);
        mVerifyNormalColor = array.getColor(R.styleable.VerificationCodeView_verify_normal_color, Color.BLACK);
        mVerifyErrorColor = array.getColor(R.styleable.VerificationCodeView_verify_error_color, Color.parseColor("#FF7B1A"));
        mVerifyWidth = array.getDimension(R.styleable.VerificationCodeView_verify_width, 108);
        mVerifyHeight = array.getDimension(R.styleable.VerificationCodeView_verify_height, 138);
        mVerifyMargin = array.getDimension(R.styleable.VerificationCodeView_verify_margin, 39);
        mVerifyErrorTextSize = array.getDimension(R.styleable.VerificationCodeView_verify_error_text_size, 36);
        array.recycle();
    }

    private void initViews() {
        LayoutInflater.from(mContext).inflate(R.layout.view_verification, this);
        mTvContainer = findViewById(R.id.tv_container);
        for (int i = 0; i < mVerifyNum; i++) {
            TextView textView = new TextView(mContext);
            textView.setTextColor(mVerifyNormalColor);
            textView.setTextSize(TypedValue.COMPLEX_UNIT_PX, mTextSize);
            textView.setBackgroundResource(mVerifyNormalBg);
            textView.setGravity(Gravity.CENTER);
            LinearLayout.LayoutParams params = new LinearLayout.LayoutParams((int) mVerifyWidth, (int) mVerifyHeight);
            if (i != 0) {
                params.leftMargin = (int) mVerifyMargin;
            }
            mTvContainer.addView(textView, params);
        }
        mEditText = findViewById(R.id.verify_edit_text);
        mEditText.requestFocus();
        mEditText.addTextChangedListener(this);
        mEditText.setOnKeyListener(this);
        mTvErrorInfo = findViewById(R.id.tv_error_info);
        mTvErrorInfo.setTextColor(mVerifyErrorColor);
        mTvErrorInfo.setTextSize(TypedValue.COMPLEX_UNIT_PX, mVerifyErrorTextSize);
        nextPos("");
    }

    private void nextPos(String text) {
        //显示刚刚输入的文字
        if (mCurPos >= 0 && mCurPos < mVerifyNum) {
            TextView textView = (TextView) mTvContainer.getChildAt(mCurPos);
            textView.setText(text);

            //光标移向下个位置
            if (!TextUtils.isEmpty(text)) {
                mCurPos++;
            }
            if (mCurPos == 1 && mTvErrorInfo.getVisibility() == View.VISIBLE) {
                //之前输入错误，等到输入一位再隐藏输入信息
                mTvErrorInfo.setVisibility(View.INVISIBLE);
            }
            setEditTextPadding();
            setSelectHighLight();

            if (mCurPos == mVerifyNum) {
                //表示输入完成
                mEditText.setCursorVisible(false);
                if (mVerificationCodeCallBack != null) {
                    StringBuilder sb = new StringBuilder();
                    for (int i = 0; i < mTvContainer.getChildCount(); i++) {
                        TextView tv = (TextView) mTvContainer.getChildAt(i);
                        sb.append(tv.getText());
                    }
                    mVerificationCodeCallBack.onInputEnd(sb.toString());
                }
            }
        }
    }

    private void prePos() {
        //光标需要往前移动一个位置
        if (mCurPos > 0 && mCurPos <= mVerifyNum) {
            if (mCurPos == mVerifyNum) {
                mEditText.setCursorVisible(true);
            }
            mCurPos--;
            if (mCurPos >= 0 && mCurPos < mVerifyNum) {
                TextView textView1 = (TextView) mTvContainer.getChildAt(mCurPos);
                textView1.setText("");
                setEditTextPadding();
                setSelectHighLight();
            }
        }
    }

    /**
     * 控制光标位置
     */
    private void setEditTextPadding() {
        int paddingLeft = (int) (mCurPos * mVerifyWidth + mCurPos * mVerifyMargin + mVerifyWidth / 2);
        mEditText.setPadding(paddingLeft, 0, 0, 0);
    }

    /**
     * 控制高亮显示
     */
    private void setSelectHighLight() {
        for (int i = 0; i < mTvContainer.getChildCount(); i++) {
            TextView textView = (TextView) mTvContainer.getChildAt(i);
            if (mCurPos == i) {
                textView.setBackgroundResource(mVerifyHighlightBg);
            } else {
                textView.setBackgroundResource(mVerifyNormalBg);
            }
        }
    }

    public void inputError() {
        inputError(null);
    }

    public void inputError(String errorMsg) {
        for (int i = 0; i < mTvContainer.getChildCount(); i++) {
            TextView textView = (TextView) mTvContainer.getChildAt(i);
            textView.setTextColor(mVerifyErrorColor);
            textView.setBackgroundResource(mVerifyErrorBg);
        }
        mTvErrorInfo.setVisibility(View.VISIBLE);
        if (!TextUtils.isEmpty(errorMsg)) {
            mTvErrorInfo.setText(errorMsg);
        }
        if (shakeAnimator == null) {
            shakeAnimator = ObjectAnimator.ofFloat(mTvErrorInfo, "translationX", 0, 10);
            shakeAnimator.setDuration(60);
            shakeAnimator.setRepeatCount(5);
            shakeAnimator.setRepeatMode(ValueAnimator.REVERSE);
            shakeAnimator.addListener(new Animator.AnimatorListener() {
                @Override
                public void onAnimationStart(Animator animation) {
                    //Do Nothing
                }

                @Override
                public void onAnimationEnd(Animator animation) {
                    clearInput();
                }

                @Override
                public void onAnimationCancel(Animator animation) {
                    //Do Nothing
                }

                @Override
                public void onAnimationRepeat(Animator animation) {
                    //Do Nothing
                }
            });
        }
        shakeAnimator.start();
    }

    /**
     * 清除所有输入
     */
    public void clearInput() {
        for (int i = 0; i < mTvContainer.getChildCount(); i++) {
            TextView textView = (TextView) mTvContainer.getChildAt(i);
            textView.setTextColor(mVerifyNormalColor);
            textView.setText("");
        }
        mCurPos = INIT_POS;
        mEditText.setCursorVisible(true);
        nextPos("");
    }

    @Override
    public void beforeTextChanged(CharSequence s, int start, int count, int after) {
        //Do Nothing
    }

    @Override
    public void onTextChanged(CharSequence s, int start, int before, int count) {
        //Do Nothing
    }

    @Override
    public void afterTextChanged(Editable s) {
        String s1 = s.toString();
        if (!TextUtils.isEmpty(s1)) {
            if (s1.length() == 1) {
                nextPos(s1);
            } else {
                int min = Math.min(mVerifyNum, s1.length());
                for (int i = 0; i < min; i++) {
                    char c = s1.charAt(i);
                    nextPos(String.valueOf(c));
                }
            }
            mEditText.setText("");
        }
    }

    @Override
    public boolean onKey(View v, int keyCode, KeyEvent event) {
        if (keyCode == KeyEvent.KEYCODE_DEL && event.getAction() == KeyEvent.ACTION_UP) {
            prePos();
            return true;
        }
        return false;
    }

    public interface VerificationCodeCallBack {
        void onInputEnd(String inputCode);
    }

    public EditText getEditText() {
        return mEditText;
    }
}
