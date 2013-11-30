package com.gongshe.controller;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import com.gongshe.R;
import com.gongshe.model.*;
import it.gmariotti.cardslib.library.internal.Card;
import it.gmariotti.cardslib.library.internal.CardArrayAdapter;
import it.gmariotti.cardslib.library.internal.CardHeader;
import it.gmariotti.cardslib.library.internal.CardThumbnail;
import it.gmariotti.cardslib.library.view.CardListView;

import java.util.ArrayList;
import java.util.List;

public class ContentFrameFragment extends Fragment {
    private final String TAG = ContentFrameFragment.class.getSimpleName();

    private enum TYPE {
        GROUP,
        POST;
    }

    private Group mGroup;
    private Post mPost;
    private TYPE mType;

    private CardListView mCardListView;
    private CardArrayAdapter mCardListAdapter;

    private Card.OnCardClickListener mOnCardClickerListener = new Card.OnCardClickListener() {
        @Override
        public void onClick(Card card, View view) {
            if (getActivity() != null) {
                Intent intent = new Intent(getActivity(), PostBrowseActivity.class);
                getActivity().startActivity(intent);
            }
        }
    };

    private PostManager.OnPostListUpdateListener mOnNormalUpdateListener = new PostManager.OnPostListUpdateListener() {
        @Override
        public void onPostListUpdate() {
            setupCardsList();
        }
    };

    public void setGroupContent(Group group, boolean force) {
        if (group != null && (!group.equals(mGroup) || force)) {
            mType = TYPE.GROUP;
            mGroup = group;
            mPost = null;
            setupCardsList();
            if (group.equals(GongSheConstant.ALL_ACTIVITY_GROUP)) {

            } else if (group.equals(GongSheConstant.ALL_INVOLVED_GROUP)) {

            } else if (group.equals(GongSheConstant.ALL_AT_ME_GROUP)) {

            } else {
                PostManager.getInstance()
                           .registerOnPostListUpdteListener(mOnNormalUpdateListener);
                PostManager.getInstance()
                           .getAllInGroup(group.getId(), null);
            }
        }
    }

    public String getContentName()  {
        if (mType == TYPE.GROUP) {
            return mGroup.getName();
        }
        return mPost.getTitle();
    }

    public Group getCurrentGroup() {
        return mGroup;
    }

    public void setPostContent(Post post) {
        if (post != null && !post.equals(mPost)) {
            mType = TYPE.POST;
            mPost = post;
            mGroup = null;
            setupCardsList();
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.content_frame, container, false);
        mCardListView = (CardListView) view.findViewById(R.id.feeds_cards_list);
        mCardListView.setLongClickable(false);
        return view;
    }

    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
    }

    private void setupCardsList() {
        List<Post> postList = null;
        if (mType == TYPE.GROUP) {
            postList = PostManager.getInstance().getGroupList(mGroup);
        } else if (mType == TYPE.POST) {
            postList = PostManager.getInstance().getPostList(mPost.getSignature());
        }
        List<Card> cardList = new ArrayList<Card>();
        makeupCardList(cardList, postList);
        mCardListAdapter = new CardArrayAdapter(getActivity(), cardList);
        mCardListView.setAdapter(mCardListAdapter);
    }

    private void makeupCardList(List<Card> cardList, List<Post> postList) {
        for (Post post: postList) {
            Card card = new Card(getActivity());
            // set header
            CardHeader header = new CardHeader(getActivity());
            header.setTitle(post.getTitle());
            card.addCardHeader(header);
            // set thumbnail
            CardThumbnail thumbnail = new CardThumbnail(getActivity());
            thumbnail.setDrawableResource(R.drawable.icon);
            card.addCardThumbnail(thumbnail);
            // set title
            card.setTitle(post.getContent());
            card.setOnClickListener(mOnCardClickerListener);

            card.setLongClickable(false);
            cardList.add(card);
        }
    }
}
