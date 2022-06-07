package com.mshilkov.clubolimp.data;

import android.net.Uri;
import android.provider.BaseColumns;

public final class CLubOlympContract {
    private CLubOlympContract()
    {

    }
    public static final int DB_VERSION=0;
    public static final String DB_NAME="Olimp";
    public static final String SCHEME = "content://";
    public static final String AUTHORITY = "package com.mshilkov.clubolimp";
    public static final String PATH_MEMBERS = "members";
    public static final Uri BASE_CONTENT_URI =
            Uri.parse(SCHEME + AUTHORITY);

    public static final class memberEntry implements BaseColumns
    {
        public static final String TABLE_NAME="members";
        public static final String _ID=BaseColumns._ID;
        public static final String KEY_FIRSTNAME="firstname";
        public static final String KEY_LASTNAME="lastname";
        public static final String KEY_GENDER="gender";
        public static final String KEY_SPORT="sport";
        public static final Uri CONTENT_URI =
                Uri.withAppendedPath(BASE_CONTENT_URI, PATH_MEMBERS);
        public static final int GENDER_UNKNOW=0;
        public static final int GENDER_MALE=1;
        public static final int GENDER_FEMALE=2;

    }
}
