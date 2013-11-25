package com.gongshe.controller;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import com.gongshe.R;
import it.gmariotti.cardslib.library.internal.Card;
import it.gmariotti.cardslib.library.internal.CardArrayAdapter;
import it.gmariotti.cardslib.library.internal.CardHeader;
import it.gmariotti.cardslib.library.internal.CardThumbnail;
import it.gmariotti.cardslib.library.view.CardListView;

import java.util.ArrayList;

public class ContentFrameFragment extends Fragment {
    private final String TAG = ContentFrameFragment.class.getSimpleName();

    public enum GroupInfo {
        ALL_FEEDS,
        ALL_INVOLVED,
        ALL_MENTIONED,
        GROUP_1,
        GROUP_2
    }

    private GroupInfo mGruopInfo;
    private CardListView mCardListView;

    private Card.OnCardClickListener mOnCardClickerListener = new Card.OnCardClickListener() {
        @Override
        public void onClick(Card card, View view) {
            if (getActivity() != null) {
                Intent intent = new Intent(getActivity(), PostBrowseActivity.class);
                getActivity().startActivity(intent);
            }
        }
    };

    public void setGroupInfo(GroupInfo groupInfo) {
        if (mGruopInfo != groupInfo) {
            mGruopInfo = groupInfo;
            setupCardsList();
        }
    }

    public GroupInfo getGroupInfo() {
        return mGruopInfo;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.content_frame, container, false);
        mCardListView = (CardListView) view.findViewById(R.id.feeds_cards_list);
        return view;
    }

    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        setupCardsList();
    }

    @Override
    public void onResume() {
        super.onResume();
    }

    private void setupCardsList() {
        Log.e(TAG, "before set list");
        if (getActivity() == null) {
            // todo, when activity is not ready, return.
            return;
        }
        Log.e(TAG, "set list");
        ArrayList<Card> cards = new ArrayList<Card>();
        fakeData(cards);
        CardArrayAdapter mCardArrayAdapter = new CardArrayAdapter(getActivity(), cards);
        if (mCardListView!=null){
            mCardListView.setAdapter(mCardArrayAdapter);
        }
    }

    @Override
    public void onStop() {
        super.onStop();
        Log.d(TAG, "onStop");
    }

    private void fakeData(ArrayList<Card> cards) {
        if (mGruopInfo == null) {
            return;
        }
        switch (mGruopInfo) {
            case ALL_FEEDS: {
                for (int i = 0; i < 10; i++) {
                    Card card = new Card(getActivity());
                    // set header
                    CardHeader header = new CardHeader(getActivity());
                    header.setTitle("最新动态 " + i);
                    card.addCardHeader(header);
                    // set thumbnail
                    CardThumbnail thumbnail = new CardThumbnail(getActivity());
                    thumbnail.setDrawableResource(R.drawable.icon);
                    card.addCardThumbnail(thumbnail);
                    // set title
                    card.setTitle("微软外部CEO候选人缩至5人】为了寻找下一任CEO，微软将外部候选人名单缩减至5人，福特CEO艾伦·穆拉利与诺基亚前CEO埃洛普均位列其中。" + i);
                    card.setOnClickListener(mOnCardClickerListener);
                    cards.add(card);
                }
            }
                break;
            case ALL_INVOLVED: {
                for (int i = 0; i < 10; i++) {
                    Card card = new Card(getActivity());
                    // set header
                    CardHeader header = new CardHeader(getActivity());
                    header.setTitle("我参与到讨论 " + i);
                    card.addCardHeader(header);
                    // set thumbnail
                    CardThumbnail thumbnail = new CardThumbnail(getActivity());
                    thumbnail.setDrawableResource(R.drawable.icon);
                    card.addCardThumbnail(thumbnail);
                    // set title
                    card.setTitle("360负责搜索业务的副总裁于光东向外界透露，根据 CNZZ 的数据，今年10月360搜索的占有率就已经突破了20%大关，最高时曾达到22.85%。另一方面，百度的市场份额已经从今年4月份的67%下跌至当前的60%左右，同时搜狗的份额基本上无变化。" + i);
                    card.setOnClickListener(mOnCardClickerListener);
                    cards.add(card);
                }
            }
                break;
            case ALL_MENTIONED: {
                for (int i = 0; i < 10; i++) {
                    Card card = new Card(getActivity());
                    // set header
                    CardHeader header = new CardHeader(getActivity());
                    header.setTitle("提到我的 " + i);
                    card.addCardHeader(header);
                    // set thumbnail
                    CardThumbnail thumbnail = new CardThumbnail(getActivity());
                    thumbnail.setDrawableResource(R.drawable.icon);
                    card.addCardThumbnail(thumbnail);
                    // set title
                    card.setTitle("好家伙，刚才出去溜达了一圈，外面天气可以概括成 PM2.5大战PM25。" + i);
                    card.setOnClickListener(mOnCardClickerListener);
                    cards.add(card);
                }
            }
                break;
            case GROUP_1: {
                for (int i = 0; i < 10; i++) {
                    Card card = new Card(getActivity());
                    // set header
                    CardHeader header = new CardHeader(getActivity());
                    header.setTitle("行行摄色 " + i);
                    card.addCardHeader(header);
                    // set thumbnail
                    CardThumbnail thumbnail = new CardThumbnail(getActivity());
                    thumbnail.setDrawableResource(R.drawable.icon);
                    card.addCardThumbnail(thumbnail);
                    // set title
                    card.setTitle("Nick Bilton的新书《孵化Twitter》中，讲述了Twitter成长中的矛盾和冲突。书中Twitter创始人之一Biz Stone是公司的道德之声，强调政治中立以及支持自由言论的重要性，而Jack Dorsey并不是一个头脑清晰、有远见的领导者，Evan Williams是个更有同情心的人物。" + i);
                    card.setOnClickListener(mOnCardClickerListener);
                    cards.add(card);
                }
            }
                break;
            case GROUP_2: {
                for (int i = 0; i < 10; i++) {
                    Card card = new Card(getActivity());
                    // set header
                    CardHeader header = new CardHeader(getActivity());
                    header.setTitle("荐书 " + i);
                    card.addCardHeader(header);
                    // set thumbnail
                    CardThumbnail thumbnail = new CardThumbnail(getActivity());
                    thumbnail.setDrawableResource(R.drawable.icon);
                    card.addCardThumbnail(thumbnail);
                    // set title
                    card.setTitle("在我们黑暗的孤独里有一线微光，\n" +
                                  "这一线微光使我们留恋黑暗，\n" +
                                  "这一线微光给我们幻象的骚扰，\n" +
                                  "在黎明确定我们的虚无以前。" + i);
                    card.setOnClickListener(mOnCardClickerListener);
                    cards.add(card);
                }
            }
                break;
            default: // todo
        }
    }

}
