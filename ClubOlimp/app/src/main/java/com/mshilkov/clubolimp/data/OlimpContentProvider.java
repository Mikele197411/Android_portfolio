package com.mshilkov.clubolimp.data;

import android.content.ContentProvider;
import android.content.ContentUris;
import android.content.ContentValues;
import android.content.UriMatcher;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.net.Uri;
import android.util.Log;

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
    public Cursor query(Uri uri, String[] projection,
                        String selection, String[] selectionArgs,
                        String sortOrder) {
        SQLiteDatabase db = olimpDBHelper.getReadableDatabase();
        Cursor cursor;

        int match = uriMatcher.match(uri);



        switch (match) {
            case MEMBERS:
                cursor = db.query(CLubOlympContract.MemberEntry.TABLE_NAME, projection, selection,
                        selectionArgs, null, null, sortOrder);
                break;

            // selection = "_id=?"
            // selectionArgs = 34
            case MEMBER_ID:
                selection = CLubOlympContract.MemberEntry._ID + "=?";
                selectionArgs = new String[]
                        {String.valueOf(ContentUris.parseId(uri))};
                cursor = db.query(CLubOlympContract.MemberEntry.TABLE_NAME, projection, selection,
                        selectionArgs, null, null, sortOrder);
                break;

            default:
                throw new IllegalArgumentException("Can't query incorrect URI "
                        + uri);

        }

        cursor.setNotificationUri(getContext().getContentResolver(), uri);

        return cursor;
    }


    @Override
    public Uri insert(Uri uri, ContentValues values) {

        String firstName = values.getAsString(CLubOlympContract.MemberEntry.COLUMN_FIRST_NAME);
        if (firstName == null) {
            throw new IllegalArgumentException("You have to input first name");
        }

        String lastName = values.getAsString(CLubOlympContract.MemberEntry.COLUMN_LAST_NAME);
        if (lastName == null) {
            throw new IllegalArgumentException("You have to input last name");
        }

        Integer gender = values.getAsInteger(CLubOlympContract.MemberEntry.COLUMN_GENDER);
        if (gender == null || !(gender == CLubOlympContract.MemberEntry.GENDER_UNKNOWN || gender ==
                CLubOlympContract.MemberEntry.GENDER_MALE || gender == CLubOlympContract.MemberEntry.GENDER_FEMALE)) {
            throw new IllegalArgumentException
                    ("You have to input correct gender");
        }

        String sport = values.getAsString(CLubOlympContract.MemberEntry.COLUMN_SPORT);
        if (sport == null) {
            throw new IllegalArgumentException("You have to input sport");
        }

        SQLiteDatabase db = olimpDBHelper.getWritableDatabase();

        int match = uriMatcher.match(uri);

        switch (match) {
            case MEMBERS:
                long id = db.insert(CLubOlympContract.MemberEntry.TABLE_NAME,
                        null, values);
                if (id == -1) {
                    Log.e("insertMethod",
                            "Insertion of data in the table failed for "
                                    + uri);
                    return null;
                }

                getContext().getContentResolver().notifyChange(uri,
                        null);

                return ContentUris.withAppendedId(uri, id);

            default:
                throw new IllegalArgumentException("Insertion of data in " +
                        "the table failed for " + uri);

        }

    }

    @Override
    public int delete(Uri uri, String selection, String[] selectionArgs) {

        SQLiteDatabase db = olimpDBHelper.getWritableDatabase();

        int match = uriMatcher.match(uri);

        int rowsDeleted;



        switch (match) {
            case MEMBERS:

                rowsDeleted = db.delete(CLubOlympContract.MemberEntry.TABLE_NAME, selection,
                        selectionArgs);
                break;

            // selection = "_id=?"
            // selectionArgs = 34
            case MEMBER_ID:
                selection = CLubOlympContract.MemberEntry._ID + "=?";
                selectionArgs = new String[]
                        {String.valueOf(ContentUris.parseId(uri))};
                rowsDeleted = db.delete(CLubOlympContract.MemberEntry.TABLE_NAME, selection,
                        selectionArgs);
                break;

            default:
                throw new IllegalArgumentException("Can't delete this URI "
                        + uri);

        }

        if (rowsDeleted != 0) {
            getContext().getContentResolver().notifyChange(uri, null);
        }

        return rowsDeleted;
    }

    @Override
    public int update(Uri uri, ContentValues values, String selection,
                      String[] selectionArgs) {

        if (values.containsKey(CLubOlympContract.MemberEntry.COLUMN_FIRST_NAME)) {
            String firstName = values.getAsString(CLubOlympContract.MemberEntry.COLUMN_FIRST_NAME);
            if (firstName == null) {
                throw new IllegalArgumentException
                        ("You have to input first name");
            }
        }

        if (values.containsKey(CLubOlympContract.MemberEntry.COLUMN_LAST_NAME)) {
            String lastName = values.getAsString(CLubOlympContract.MemberEntry.COLUMN_LAST_NAME);
            if (lastName == null) {
                throw new IllegalArgumentException
                        ("You have to input last name");
            }
        }

        if (values.containsKey(CLubOlympContract.MemberEntry.COLUMN_GENDER)) {
            Integer gender = values.getAsInteger(CLubOlympContract.MemberEntry.COLUMN_GENDER);
            if (gender == null || !(gender == CLubOlympContract.MemberEntry.GENDER_UNKNOWN || gender ==
                    CLubOlympContract.MemberEntry.GENDER_MALE || gender == CLubOlympContract.MemberEntry.GENDER_FEMALE)) {
                throw new IllegalArgumentException("You have to input correct gender");
            }
        }

        if (values.containsKey(CLubOlympContract.MemberEntry.COLUMN_SPORT)) {
            String sport = values.getAsString(CLubOlympContract.MemberEntry.COLUMN_SPORT);
            if (sport == null) {
                throw new IllegalArgumentException("You have to input sport");
            }
        }

        SQLiteDatabase db = olimpDBHelper.getWritableDatabase();

        int match = uriMatcher.match(uri);

        int rowsUpdated;

        switch (match) {
            case MEMBERS:

                rowsUpdated = db.update(CLubOlympContract.MemberEntry.TABLE_NAME, values,
                        selection, selectionArgs);

                break;
            // selection = "_id=?"
            // selectionArgs = 34
            case MEMBER_ID:
                selection = CLubOlympContract.MemberEntry._ID + "=?";
                selectionArgs = new String[]
                        {String.valueOf(ContentUris.parseId(uri))};
                rowsUpdated = db.update(CLubOlympContract.MemberEntry.TABLE_NAME, values,
                        selection, selectionArgs);

                break;

            default:
                throw new IllegalArgumentException("Can't update this URI "
                        + uri);

        }

        if (rowsUpdated != 0) {
            getContext().getContentResolver().notifyChange(uri, null);
        }

        return rowsUpdated;
    }

    @Override
    public String getType(Uri uri) {

        int match = uriMatcher.match(uri);



        switch (match) {
            case MEMBERS:

                return CLubOlympContract.MemberEntry.CONTENT_MULTIPLE_ITEMS;

            case MEMBER_ID:

                return CLubOlympContract.MemberEntry.CONTENT_SINGLE_ITEM;

            default:
                throw new IllegalArgumentException("Unknown URI: " + uri);

        }
    }

}


//// content://com.android.mshilkov.clubolimp/members

