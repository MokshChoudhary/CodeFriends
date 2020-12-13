package com.futurework.codefriends.Database;

import android.provider.BaseColumns;

public class DbContainer {
    public static class BlankEntry implements BaseColumns{
        public final static String _ID = BaseColumns._ID;

        public final static String DATABASE_NAME = "CodeFriends";

        public final static String LOGIN_TABLE_NAME = "UserName";
        public final static String COLUMNS_USER_NAME = "name";
        public final static String COLUMNS_USER_TAG = "tag";
        public final static String COLUMNS_USER_LIST = "list";
        public static final String COLUMNS_USER_STATUS = "status";
        public final static String COLUMNS_USER_IMAGE = "image";

        public final static String COLUMNS_LISTS = "tags";

    }
}
