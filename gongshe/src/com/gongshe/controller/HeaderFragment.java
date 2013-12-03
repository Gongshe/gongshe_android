package com.gongshe.controller;

import android.app.Activity;
import android.content.res.TypedArray;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.TextView;
import com.gongshe.R;

public class HeaderFragment extends Fragment {
    private static final String TAG = HeaderFragment.class.getSimpleName();

    public static enum RightBtnId {
        ONE,
        TWO,
        TEXT;
    }

    public interface OnButtonListener {
        public void onLeftBtnClicked();
        public void onRightBtnClicked(RightBtnId id);
    }

    public static enum ButtonType {
        INVISIBLE,
        TEXT,
        ICON;
    }

    private ButtonType mLeftBtnType;
    private ButtonType mRightBtnOneType;
    private ButtonType mRightBtnTwoType;
    private ButtonType mRightBtnTextType;

    private String mTitleText;
    private String mLeftBtnText;
    private String mRightBtnText;

    private OnButtonListener mOnButtonListener;

    private int mLeftImgBtnRes;
    private int mRigntOneImgBtnRes;
    private int mRigntTwoImgBtnRes;

    private TextView mTxvTitle;
    private Button mBtnLeft;
    private ImageButton mItnRightOne;
    private ImageButton mItnRightTwo;
    private Button mBtnRightText;

    public void setOnButtonListener(OnButtonListener listener) {
        mOnButtonListener = listener;
    }

    public void setTitle(String title) {
        mTitleText = title;
        mTxvTitle.setText(mTitleText);
    }

    public void setLetBtnText(String text) {
        mLeftBtnText = text;
        mBtnLeft.setText(mLeftBtnText);
    }

    public void setRightButtonType(ButtonType rightOne, ButtonType rightTwo, ButtonType rightText) {
        mRightBtnOneType = rightOne;
        if (mRightBtnOneType != ButtonType.ICON) {
            mItnRightOne.setVisibility(View.GONE);
        } else {
            mItnRightOne.setVisibility(View.VISIBLE);
        }

        mRightBtnTwoType = rightTwo;
        if (mRightBtnTwoType != ButtonType.ICON) {
            mItnRightTwo.setVisibility(View.GONE);
        } else {
            mItnRightTwo.setVisibility(View.VISIBLE);
        }

        mRightBtnTextType = rightText;
        if (mRightBtnTextType != ButtonType.TEXT) {
            mBtnRightText.setVisibility(View.GONE);
        } else {
            mBtnRightText.setVisibility(View.VISIBLE);
        }
    }

    @Override
    public void onInflate(Activity activity, AttributeSet attrs, Bundle savedInstanceState) {
        super.onInflate(activity, attrs, savedInstanceState);
        TypedArray styles = activity.obtainStyledAttributes(attrs, R.styleable.CommonHeader);
        mTitleText = styles.getString(R.styleable.CommonHeader_text_title);

        mLeftBtnType = ButtonType.values()[styles.getInt(R.styleable.CommonHeader_left_btn, 0)];
        if (mLeftBtnType == ButtonType.TEXT) {
            mLeftBtnText = styles.getString(R.styleable.CommonHeader_text_left_btn);
        } else if (mLeftBtnType == ButtonType.ICON) {
            mLeftImgBtnRes = styles.getResourceId(R.styleable.CommonHeader_img_left_btn, R.drawable.icon);
        }

        mRightBtnOneType = ButtonType.values()[styles.getInt(R.styleable.CommonHeader_right_btn_one, 0)];
        if (mRightBtnOneType == ButtonType.ICON) {
            mRigntOneImgBtnRes = styles.getResourceId(R.styleable.CommonHeader_img_right_one, R.drawable.icon);
        }

        mRightBtnTwoType = ButtonType.values()[styles.getInt(R.styleable.CommonHeader_right_btn_two, 0)];
        if (mRightBtnTwoType == ButtonType.ICON) {
            mRigntTwoImgBtnRes = styles.getResourceId(R.styleable.CommonHeader_img_right_two, R.drawable.icon);
        }

        mRightBtnTextType = ButtonType.values()[styles.getInt(R.styleable.CommonHeader_right_btn_text, 0)];
        if (mRightBtnTextType == ButtonType.TEXT) {
            mRightBtnText = styles.getString(R.styleable.CommonHeader_text_right_btn);
        }

        styles.recycle();
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.header_fragment, container, false);

        mTxvTitle = (TextView) view.findViewById(R.id.txv_title);
        mTxvTitle.setText(mTitleText);

        if (mLeftBtnType != null) {
            mBtnLeft = (Button) view.findViewById(R.id.btn_left);
            ImageButton imageButton = (ImageButton) view.findViewById(R.id.btn_left_image);
            switch (mLeftBtnType) {
                case INVISIBLE:
                    mBtnLeft.setVisibility(View.GONE);
                    imageButton.setVisibility(View.GONE);
                    break;
                case TEXT:
                    mBtnLeft.setText(getText(R.string.btn_back));
                    mBtnLeft.setVisibility(View.VISIBLE);
                    imageButton.setVisibility(View.GONE);
                    break;
                case ICON:
                    mBtnLeft.setVisibility(View.GONE);
                    imageButton.setVisibility(View.VISIBLE);
                    imageButton.setImageResource(mLeftImgBtnRes);
                    break;
            }
            mBtnLeft.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if (mOnButtonListener != null) {
                        mOnButtonListener.onLeftBtnClicked();
                    }
                }
            });
            imageButton.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if (mOnButtonListener != null) {
                        mOnButtonListener.onLeftBtnClicked();
                    }
                }
            });
        }

        if (mRightBtnOneType != null) {
            mItnRightOne = (ImageButton) view.findViewById(R.id.btn_right_one);
            switch (mRightBtnOneType) {
                case INVISIBLE:
                    mItnRightOne.setVisibility(View.GONE);
                    break;
                case TEXT:
                    mItnRightOne.setVisibility(View.GONE);
                case ICON:
                    mItnRightOne.setVisibility(View.VISIBLE);
                    mItnRightOne.setImageResource(mRigntOneImgBtnRes);
                    break;
            }
            mItnRightOne.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if (mOnButtonListener != null) {
                        mOnButtonListener.onRightBtnClicked(RightBtnId.ONE);
                    }
                }
            });
        }

        if (mRightBtnTwoType != null) {
            mItnRightTwo = (ImageButton) view.findViewById(R.id.btn_right_two);
            switch (mRightBtnTwoType) {
                case INVISIBLE:
                    mItnRightTwo.setVisibility(View.GONE);
                    break;
                case TEXT:
                    mItnRightTwo.setVisibility(View.GONE);
                case ICON:
                    mItnRightTwo.setVisibility(View.INVISIBLE);
                    mItnRightTwo.setImageResource(mRigntTwoImgBtnRes);
                    break;
            }
            mItnRightTwo.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if (mOnButtonListener != null) {
                        mOnButtonListener.onRightBtnClicked(RightBtnId.TWO);
                    }
                }
            });
        }

        if (mRightBtnTextType != null) {
            mBtnRightText = (Button) view.findViewById(R.id.btn_right_text);
            switch (mRightBtnTextType) {
                case INVISIBLE:
                    mBtnRightText.setVisibility(View.GONE);
                    break;
                case TEXT:
                    mBtnRightText.setVisibility(View.VISIBLE);
                    mBtnRightText.setText(mRightBtnText);
                    break;
                case ICON:
                    mBtnRightText.setVisibility(View.GONE);
                    break;
            }
            mBtnRightText.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if (mOnButtonListener != null) {
                        mOnButtonListener.onRightBtnClicked(RightBtnId.TEXT);
                    }
                }
            });
        }
        return view;
    }
}
