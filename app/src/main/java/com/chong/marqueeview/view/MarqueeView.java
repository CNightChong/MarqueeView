package com.chong.marqueeview.view;

import android.content.Context;
import android.content.res.TypedArray;
import android.text.TextUtils;
import android.util.AttributeSet;
import android.view.Gravity;
import android.view.View;
import android.view.ViewTreeObserver;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.TextView;
import android.widget.ViewFlipper;

import com.chong.marqueeview.R;

import java.util.ArrayList;
import java.util.List;

public class MarqueeView extends ViewFlipper {

    private Context mContext;
    /**
     * 字符串列表
     */
    private List<String> mNotices;
    /**
     * 是否设置动画执行时间
     */
    private boolean mIsSetAnimDuration = false;
    private OnItemClickListener mOnItemClickListener;

    /**
     * 两行文字翻页时间间隔
     */
    private int mInterval = 2000;
    /**
     * 每行文字动画执行时间
     */
    private int mAnimDuration = 500;
    /**
     * 文字大小，单位sp
     */
    private int mTextSize = 14;
    /**
     * 文字颜色
     */
    private int mTextColor = 0xffffffff;
    /**
     * 单行设置
     */
    private boolean mIsSingleLine = false;
    /**
     * 文字中心位置:left、center、right
     */
    private int gravity = Gravity.LEFT | Gravity.CENTER_VERTICAL;
    /**
     * 进入动画
     */
    private int mAnimIn = 0;
    /**
     * 退出动画
     */
    private int mAnimOut = 0;
    private static final int TEXT_GRAVITY_LEFT = 0, TEXT_GRAVITY_CENTER = 1, TEXT_GRAVITY_RIGHT = 2;

    public MarqueeView(Context context, AttributeSet attrs) {
        super(context, attrs);
        init(context, attrs, 0);
    }

    private void init(Context context, AttributeSet attrs, int defStyleAttr) {
        this.mContext = context;
        if (mNotices == null) {
            mNotices = new ArrayList<>();
        }

        TypedArray typedArray = getContext().obtainStyledAttributes(attrs, R.styleable.MarqueeViewStyle, defStyleAttr, 0);
        mInterval = typedArray.getInteger(R.styleable.MarqueeViewStyle_mvInterval, mInterval);
        mIsSetAnimDuration = typedArray.hasValue(R.styleable.MarqueeViewStyle_mvAnimDuration);
        mIsSingleLine = typedArray.getBoolean(R.styleable.MarqueeViewStyle_mvSingleLine, false);
        mAnimDuration = typedArray.getInteger(R.styleable.MarqueeViewStyle_mvAnimDuration, mAnimDuration);
        if (typedArray.hasValue(R.styleable.MarqueeViewStyle_mvTextSize)) {
            mTextSize = (int) typedArray.getDimension(R.styleable.MarqueeViewStyle_mvTextSize, mTextSize);
            mTextSize = DisplayUtil.px2sp(mContext, mTextSize);
        }
        mTextColor = typedArray.getColor(R.styleable.MarqueeViewStyle_mvTextColor, mTextColor);
        int gravityType = typedArray.getInt(R.styleable.MarqueeViewStyle_mvGravity, TEXT_GRAVITY_LEFT);
        switch (gravityType) {
            case TEXT_GRAVITY_CENTER:
                gravity = Gravity.CENTER;
                break;
            case TEXT_GRAVITY_RIGHT:
                gravity = Gravity.RIGHT | Gravity.CENTER_VERTICAL;
                break;
        }
        mAnimIn = typedArray.getResourceId(R.styleable.MarqueeViewStyle_mvAnimIn, 0);
        mAnimOut = typedArray.getResourceId(R.styleable.MarqueeViewStyle_mvAnimOut, 0);
        typedArray.recycle();

        setFlipInterval(mInterval);

        // 设置进入动画
        Animation animIn;
        if (mAnimIn != 0) {
            animIn = AnimationUtils.loadAnimation(mContext, mAnimIn);
        } else {
            animIn = AnimationUtils.loadAnimation(mContext, R.anim.anim_marquee_in);
        }
        if (mIsSetAnimDuration) {
            animIn.setDuration(mAnimDuration);
        }
        setInAnimation(animIn);

        // 设置退出动画
        Animation animOut;
        if (mAnimOut != 0) {
            animOut = AnimationUtils.loadAnimation(mContext, mAnimOut);
        } else {
            animOut = AnimationUtils.loadAnimation(mContext, R.anim.anim_marquee_out);
        }
        if (mIsSetAnimDuration) {
            animOut.setDuration(mAnimDuration);
        }
        setOutAnimation(animOut);
    }

    // 根据字符串启动轮播
    public void startWithText(final String notice) {
        if (TextUtils.isEmpty(notice)) {
            return;
        }
        getViewTreeObserver().addOnGlobalLayoutListener(new ViewTreeObserver.OnGlobalLayoutListener() {
            @Override
            public void onGlobalLayout() {
                getViewTreeObserver().removeGlobalOnLayoutListener(this);
                startWithFixedWidth(notice, getWidth());
            }
        });
    }

    // 根据字符串list启动轮播
    public void startWithList(List<String> notices) {
        setNotices(notices);
        start();
    }

    // 根据宽度和公告字符串启动轮播
    private void startWithFixedWidth(String notice, int width) {
        // 获取字符串字符个数
        int noticeLength = notice.length();
        // view宽度
        int dpWidth = DisplayUtil.px2dip(mContext, width);
        // 字符串字符限制个数
        int limit = dpWidth / mTextSize;
        if (dpWidth == 0) {
            throw new RuntimeException("Please set MarqueeView width !");
        }

        if (noticeLength <= limit) {
            mNotices.add(notice);
        } else {
            int size = noticeLength / limit + (noticeLength % limit != 0 ? 1 : 0);
            for (int i = 0; i < size; i++) {
                int startIndex = i * limit;
                int endIndex = ((i + 1) * limit >= noticeLength ? noticeLength : (i + 1) * limit);
                mNotices.add(notice.substring(startIndex, endIndex));
            }
        }
        start();
    }

    // 启动轮播
    public boolean start() {
        if (mNotices == null || mNotices.size() == 0) {
            return false;
        }
        removeAllViews();

        for (int i = 0; i < mNotices.size(); i++) {
            final TextView textView = createTextView(mNotices.get(i), i);
            final int position = i;
            textView.setOnClickListener(new OnClickListener() {
                @Override
                public void onClick(View v) {
                    if (mOnItemClickListener != null) {
                        mOnItemClickListener.onItemClick(position, textView);
                    }
                }
            });
            addView(textView);
        }

        if (mNotices.size() > 1) {
            startFlipping();
        }
        return true;
    }

    // 创建ViewFlipper中的TextView
    private TextView createTextView(String text, int position) {
        TextView tv = new TextView(mContext);
        tv.setGravity(gravity);
        tv.setText(text);
        tv.setTextColor(mTextColor);
        tv.setTextSize(mTextSize);
        tv.setSingleLine(mIsSingleLine);
        tv.setTag(position);
        return tv;
    }

    public int getPosition() {
        return (int) getCurrentView().getTag();
    }

    public List<String> getNotices() {
        return mNotices;
    }

    public void setNotices(List<String> notices) {
        this.mNotices = notices;
    }

    public void setOnItemClickListener(OnItemClickListener mOnItemClickListener) {
        this.mOnItemClickListener = mOnItemClickListener;
    }

    /**
     * 设置进入动画
     *
     * @param animInId 进入动画id
     */
    public void setAnimIn(int animInId) {
        this.mAnimIn = animInId;
    }

    /**
     * 设置退出动画
     *
     * @param animOutId 退出动画id
     */
    public void setAnimOut(int animOutId) {
        this.mAnimOut = animOutId;
    }

    public interface OnItemClickListener {
        void onItemClick(int position, TextView textView);
    }

}
