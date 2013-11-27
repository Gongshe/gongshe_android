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

    public interface OnButtonListener {
        public void onLeftButtonClicked();
        public void onRightButtonClicked();
    }

    private static enum LeftButtonType {
        INVISIBLE,
        BACK,
        ICON;
    }

    private static enum RightButtonType {
        INVISIBLE,
        ICON;
    }

    private LeftButtonType mLeftButtonType;
    private RightButtonType mRightButtonType;
    private String mTitleText;
    private String mLeftButtonText;
    private OnButtonListener mOnButtonListener;

    public void setOnButtonListener(OnButtonListener listener) {
        mOnButtonListener = listener;
    }

    public void setTitle(String title) {
        if (title != null) {
            mTitleText = title;
        }
        View view = getView();
        if (view != null) {
            TextView textView = (TextView) view.findViewById(R.id.txv_title);
            textView.setText(mTitleText);
        }
    }

    @Override
    public void onInflate(Activity activity, AttributeSet attrs, Bundle savedInstanceState) {
        super.onInflate(activity, attrs, savedInstanceState);
        TypedArray styles = activity.obtainStyledAttributes(attrs, R.styleable.CommonHeader);
        mTitleText = styles.getString(R.styleable.CommonHeader_title_text);
        mLeftButtonText = styles.getString(R.styleable.CommonHeader_left_text);
        mLeftButtonType = LeftButtonType.values()[styles.getInt(R.styleable.CommonHeader_left_button, 0)];
        mRightButtonType = RightButtonType.values()[styles.getInt(R.styleable.CommonHeader_right_button, 0)];
        styles.recycle();
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.header_fragment, container, false);
        if (mTitleText != null) {
            TextView textView = (TextView) view.findViewById(R.id.txv_title);
            textView.setText(mTitleText);
        }
        if (mLeftButtonType != null) {
            Button button = (Button) view.findViewById(R.id.btn_left);
            ImageButton imageButton = (ImageButton) view.findViewById(R.id.btn_left_image);
            switch (mLeftButtonType) {
                case INVISIBLE:
                    button.setVisibility(View.INVISIBLE);
                    imageButton.setVisibility(View.GONE);
                    break;
                case BACK:
                    button.setText(getText(R.string.btn_back));
                    button.setVisibility(View.VISIBLE);
                    if (mLeftButtonText != null) button.setText(mLeftButtonText);
                    imageButton.setVisibility(View.GONE);
                    break;
                case ICON:
                    button.setVisibility(View.GONE);
                    imageButton.setVisibility(View.VISIBLE);
                    imageButton.setImageResource(R.drawable.icon);
                    break;
            }
            button.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if (mOnButtonListener != null) {
                        mOnButtonListener.onLeftButtonClicked();
                    }
                }
            });
            imageButton.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if (mOnButtonListener != null) {
                        mOnButtonListener.onLeftButtonClicked();
                    }
                }
            });
        }
        if (mRightButtonType != null) {
            ImageButton imageButton = (ImageButton) view.findViewById(R.id.btn_right);
            switch (mRightButtonType) {
                case INVISIBLE:
                    imageButton.setVisibility(View.INVISIBLE);
                    break;
                case ICON:
                    imageButton.setVisibility(View.VISIBLE);
                    break;
            }
            imageButton.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if (mOnButtonListener != null) {
                        mOnButtonListener.onRightButtonClicked();
                    }
                }
            });
        }
        return view;
    }
}
