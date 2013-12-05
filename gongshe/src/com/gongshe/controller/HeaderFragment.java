package com.gongshe.controller;

import android.app.Activity;
import android.content.res.TypedArray;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import com.gongshe.R;

public class HeaderFragment extends Fragment {
    private static final String TAG = HeaderFragment.class.getSimpleName();

    public static enum BtnId {
        LEFT,
        RIGHT_ONE,
        RIGHT_TWO;
    }

    public interface OnButtonListener {
        public void onBtnCLicked(BtnId id);
    }

    public static enum ButtonType {
        INVISIBLE,
        VISIBLE;
    }

    private ButtonType mLeftBtnType;
    private ButtonType mRightBtnOneType;
    private ButtonType mRightBtnTwoType;

    private String mTitleText;

    private OnButtonListener mOnButtonListener;

    private int mLeftImgBtnRes;
    private int mRightOneImgBtnRes;
    private int mRightTwoImgBtnRes;

    private TextView mTxvTitle;

    private ImageView mImvLeft;
    private ImageView mImvRightOne;
    private ImageView mImvRightTwo;

    private View mFrameView;

    public void setOnButtonListener(OnButtonListener listener) {
        mOnButtonListener = listener;
    }

    public void setTitle(String title) {
        mTitleText = title;
        mTxvTitle.setText(mTitleText);
    }

    public void setRightButtonType(ButtonType rightOne, ButtonType rightTwo) {
        mRightBtnOneType = rightOne;
        if (mRightBtnOneType == ButtonType.INVISIBLE) {
            mImvRightOne.setVisibility(View.INVISIBLE);
        } else {
            mImvRightOne.setVisibility(View.VISIBLE);
            mImvRightOne.setImageResource(mRightOneImgBtnRes);
        }

        mRightBtnTwoType = rightTwo;
        if (mRightBtnTwoType == ButtonType.INVISIBLE) {
            mImvRightTwo.setVisibility(View.INVISIBLE);
        } else {
            mImvRightTwo.setVisibility(View.VISIBLE);
            mImvRightTwo.setImageResource(mRightTwoImgBtnRes);
        }
    }

    @Override
    public void onInflate(Activity activity, AttributeSet attrs, Bundle savedInstanceState) {
        super.onInflate(activity, attrs, savedInstanceState);
        TypedArray styles = activity.obtainStyledAttributes(attrs, R.styleable.CommonHeader);
        mTitleText = styles.getString(R.styleable.CommonHeader_text_title);

        mLeftBtnType = ButtonType.values()[styles.getInt(R.styleable.CommonHeader_left_btn, 0)];
        mLeftImgBtnRes = styles.getResourceId(R.styleable.CommonHeader_img_left_btn, R.drawable.icon);

        mRightBtnOneType = ButtonType.values()[styles.getInt(R.styleable.CommonHeader_right_btn_one, 0)];
        mRightOneImgBtnRes = styles.getResourceId(R.styleable.CommonHeader_img_right_one, R.drawable.icon);

        mRightBtnTwoType = ButtonType.values()[styles.getInt(R.styleable.CommonHeader_right_btn_two, 0)];
        mRightTwoImgBtnRes = styles.getResourceId(R.styleable.CommonHeader_img_right_two, R.drawable.icon);

        styles.recycle();
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.header_fragment, container, false);

        mFrameView = view.findViewById(R.id.title_bar);

        mTxvTitle = (TextView) view.findViewById(R.id.txv_header_title);
        mTxvTitle.setText(mTitleText);

        mImvLeft = (ImageView) view.findViewById(R.id.btn_header_left);
        switch (mLeftBtnType) {
            case INVISIBLE:
                mImvLeft.setVisibility(View.INVISIBLE);
                break;
            case VISIBLE:
                mImvLeft.setVisibility(View.VISIBLE);
                mImvLeft.setImageResource(mLeftImgBtnRes);
                break;
        }
        mImvLeft.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (mOnButtonListener != null) {
                    mOnButtonListener.onBtnCLicked(BtnId.LEFT);
                }
            }
        });

        mImvRightOne = (ImageView) view.findViewById(R.id.btn_header_right_one);
        switch (mRightBtnOneType) {
            case INVISIBLE:
                mImvRightOne.setVisibility(View.INVISIBLE);
                break;
            case VISIBLE:
                mImvRightOne.setVisibility(View.VISIBLE);
                mImvRightOne.setImageResource(mRightOneImgBtnRes);
                break;
        }
        mImvRightOne.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (mOnButtonListener != null) {
                    mOnButtonListener.onBtnCLicked(BtnId.RIGHT_ONE);
                }
            }
        });

        mImvRightTwo = (ImageView) view.findViewById(R.id.btn_header_right_two);
        switch (mRightBtnTwoType) {
            case INVISIBLE:
                mImvRightTwo.setVisibility(View.INVISIBLE);
                break;
            case VISIBLE:
                mImvRightTwo.setVisibility(View.VISIBLE);
                mImvRightTwo.setImageResource(mRightTwoImgBtnRes);
                break;
        }
        mImvRightTwo.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (mOnButtonListener != null) {
                    mOnButtonListener.onBtnCLicked(BtnId.RIGHT_TWO);
                }
            }
        });

        return view;
    }
}
