package com.gongshe.controller;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import com.gongshe.R;
import it.gmariotti.cardslib.library.internal.*;
import it.gmariotti.cardslib.library.view.CardListView;
import it.gmariotti.cardslib.library.view.CardView;

import java.util.ArrayList;

public class ContentFrameFragment extends Fragment {
    private final String TAG = ContentFrameFragment.class.getSimpleName();

    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.content_frame, container, false);
        return view;
    }

    @Override
    public void onResume() {
        super.onResume();
        setupCardsList();
    }

    private void setupCardsList() {
        ArrayList<Card> cards = new ArrayList<Card>();
        for (int i = 0; i < 10; i++) {
            Card card = new Card(getActivity());
            // set header
            CardHeader header = new CardHeader(getActivity());
            header.setTitle("head title " + i);
            card.addCardHeader(header);
            // set thumbnail
            CardThumbnail thumbnail = new CardThumbnail(getActivity());
            thumbnail.setDrawableResource(R.drawable.icon);
            card.addCardThumbnail(thumbnail);
            // set title
            card.setTitle("hello world" + i);
            cards.add(card);
        }

        CardArrayAdapter mCardArrayAdapter = new CardArrayAdapter(getActivity(),cards);
        CardListView listView = (CardListView) getView().findViewById(R.id.feeds_cards_list);
        if (listView!=null){
            listView.setAdapter(mCardArrayAdapter);
        }
    }

    @Override
    public void onStop() {
        super.onStop();
        Log.d(TAG, "onStop");
    }

}
