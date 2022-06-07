package com.mshilkov.clubolimp.data;

import android.content.ContentProvider;
import android.content.ContentValues;
import android.content.UriMatcher;
import android.database.Cursor;
import android.net.Uri;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

public class OlimpContentProvider extends ContentProvider {
    OlimpDBHelper olimpDBHelper;
    private static final int MEMBERS = 111;
    private static final int MEMBER_ID = 222;
    private static final UriMatcher uriMatcher = new UriMatcher(UriMatcher.NO_MATCH);
    static {

        uriMatcher.addURI(CLubOlympContract.AUTHORITY, CLubOlympContract.PATH_MEMBERS, MEMBERS);
      uriMatcher.addURI(CLubOlympContract.AUTHORITY, CLubOlympContract.PATH_MEMBERS+"/#", MEMBER_ID);
    }
    @Override
    public boolean onCreate() {
        olimpDBHelper=new OlimpDBHelper(getContext());
        return true;
    }


    @Override
    public Cursor query( Uri uri,  String[] strings,  String s,  String[] strings1,  String s1) {
        return null;
    }


    @Override
    public Uri insert(Uri uri,  ContentValues contentValues) {
        return null;
    }

    @Override
    public int delete( Uri uri, String s,  String[] strings) {
        return 0;
    }

    @Override
    public int update(Uri uri,  ContentValues contentValues,  String s,  String[] strings) {
        return 0;
    }

    @Override
    public String getType(Uri uri) {
        return null;
    }

//// content://com.android.mshilkov.clubolimp/members
}
