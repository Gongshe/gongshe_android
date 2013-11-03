package com.gongshe.controller;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import com.gongshe.R;
import it.gmariotti.cardslib.library.internal.Card;
import it.gmariotti.cardslib.library.internal.CardHeader;
import it.gmariotti.cardslib.library.view.CardView;

public class ContentFrameFragment extends Fragment {
    private final String TAG = ContentFrameFragment.class.getSimpleName();

    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.content_frame, container, false);


        Card card = new Card(getActivity());
        //Create a CardHeader
        CardHeader header = new CardHeader(getActivity());
        card.addCardHeader(header);
        card.setTitle("llaldfjflajfa");
        CardView cardView = (CardView) view.findViewById(R.id.carddemo);
        cardView.setCard(card);
        return view;
    }

    @Override
    public void onStop() {
        super.onStop();
        Log.d(TAG, "onStop");
    }

}
