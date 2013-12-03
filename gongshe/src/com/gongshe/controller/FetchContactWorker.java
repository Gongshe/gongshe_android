package com.gongshe.controller;

import android.content.ContentResolver;
import android.content.ContentUris;
import android.content.Context;
import android.database.Cursor;
import android.net.Uri;
import android.os.Handler;
import android.provider.ContactsContract;

import java.util.List;

class FetchContactWorker extends Thread {

    public static class ContactInfo {
        private String mName;
        private String mPhoneNumber;
        private Uri mPhotoUri;
        private boolean mIsChecked;

        public ContactInfo(String name, String phoneNumber, Uri photoUri) {
            mName = name;
            mPhoneNumber = phoneNumber;
            mPhotoUri = photoUri;
        }

        public String getName() {
            return mName;
        }

        public String getPhoneNumber() {
            return mPhoneNumber;
        }

        public Uri getPhotoUri() {
            return mPhotoUri;
        }

        public boolean isChecked() {
            return mIsChecked;
        }

        public void setChecked(boolean isChecked) {
            mIsChecked = isChecked;
        }
    }

    Context mContext;
    List<ContactInfo> mContactList;
    Handler mHandler;

    public FetchContactWorker(Context context, Handler handler, List<ContactInfo> contactList) {
        mContext = context;
        mHandler = handler;
        mContactList = contactList;
        if (mContactList == null) {
            throw new RuntimeException("Contact List is null");
        }
    }

    @Override
    public void run() {
        ContentResolver resolver = mContext.getContentResolver();
        final String[] CONTACT_PROJECTION = new String[] {ContactsContract.CommonDataKinds.Phone.DISPLAY_NAME,
                ContactsContract.CommonDataKinds.Phone.NUMBER,
                ContactsContract.CommonDataKinds.Photo.CONTACT_ID,
                ContactsContract.CommonDataKinds.Photo.PHOTO_ID};
        final String WHERE = ContactsContract.CommonDataKinds.Phone.HAS_PHONE_NUMBER + "='1'";
        Cursor cursor = resolver.query(ContactsContract.CommonDataKinds.Phone.CONTENT_URI, CONTACT_PROJECTION, WHERE, null, ContactsContract.CommonDataKinds.Phone.DISPLAY_NAME);

        if (cursor != null) {
            try {
                cursor.moveToFirst();
                while (!cursor.isAfterLast()) {
                    String phoneNumber = cursor.getString(cursor.getColumnIndex(ContactsContract.CommonDataKinds.Phone.NUMBER));
                    String displayName = cursor.getString(cursor.getColumnIndex(ContactsContract.CommonDataKinds.Phone.DISPLAY_NAME));
                    long photoId = cursor.getLong(cursor.getColumnIndex(ContactsContract.CommonDataKinds.Photo.PHOTO_ID));
                    long contactId = cursor.getLong(cursor.getColumnIndex(ContactsContract.CommonDataKinds.Photo.CONTACT_ID));
                    Uri uri = null;
                    if (photoId > 0) {
                        uri = ContentUris.withAppendedId(ContactsContract.Contacts.CONTENT_URI, contactId);
                    }
                    mContactList.add(new ContactInfo(displayName, phoneNumber, uri));
                    cursor.moveToNext();
                }
            } catch (Exception e) {
                e.printStackTrace();
            } finally {
                cursor.close();
            }
        }
        mHandler.sendEmptyMessage(ImportFriendActivity.MESSAGE_DATA_READY);
    }
}
