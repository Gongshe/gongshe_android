package com.gongshe.controller;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.FragmentActivity;
import com.gongshe.R;
import it.gmariotti.cardslib.library.internal.Card;
import it.gmariotti.cardslib.library.internal.CardArrayAdapter;
import it.gmariotti.cardslib.library.internal.CardHeader;
import it.gmariotti.cardslib.library.internal.CardThumbnail;
import it.gmariotti.cardslib.library.view.CardListView;

import java.util.ArrayList;

public class PostBrowseActivity extends FragmentActivity {

    CardListView mCardListView;

    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.post_browse);
        HeaderFragment fragment = (HeaderFragment) getSupportFragmentManager().findFragmentById(R.id.common_header);
        fragment.setTitle("荐书");
        fragment.setOnButtonListener(new HeaderFragment.OnButtonListener() {
            @Override
            public void onLeftButtonClicked() {
                onBackPressed();
            }

            @Override
            public void onRightButtonClicked() {
                Intent intent = new Intent(PostBrowseActivity.this, PeopleGroupActivity.class);
                PostBrowseActivity.this.startActivity(intent);
            }
        });
        mCardListView = (CardListView) findViewById(R.id.post_list);
        setupCardsList();
    }

    private void setupCardsList() {
        ArrayList<Card> cards = new ArrayList<Card>();
        fakeData(cards);
        CardArrayAdapter mCardArrayAdapter = new CardArrayAdapter(this, cards);
        if (mCardListView!=null){
            mCardListView.setAdapter(mCardArrayAdapter);
        }
    }

    private void fakeData(ArrayList<Card> cards) {
        for (int i = 0; i < 10; i++) {
            Card card = new Card(this);
            // set header
            CardHeader header = new CardHeader(this);
            header.setTitle("荐书 " + i);
            card.addCardHeader(header);
            // set thumbnail
            CardThumbnail thumbnail = new CardThumbnail(this);
            thumbnail.setDrawableResource(R.drawable.icon);
            card.addCardThumbnail(thumbnail);
            // set title
            card.setTitle("我身体靠在黑啤酒酿造厂快餐部敞开的玻璃墙上，" +
                          "喝着波维茨卡牌的十度啤酒，心里暗自说打这会儿起，伙计，" +
                          "一切全得看你自己的啦，你得逼着自己到人群中去，" +
                          "你得自己找乐趣，自己演戏给自己看，直到你离开自己。。。。。。。" + i);
            cards.add(card);
        }
    }
}