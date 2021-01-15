

































  package com.futurework.codefriends.Database;

import android.provider.BaseColumns;

public class DbContainer {
    public static class UserEntry{
        public final static String USER_TABLE_NAME = "userInfo";
        public final static String _ID = "id";
        public final static String COLUMNS_USER_IMAGE = "image";
        public final static String COLUMNS_USER_NAME = "name";
        public final static String COLUMNS_USER_TAG = "tag";
        public final static String COLUMNS_USER_EMAIL = "email";
        public final static String COLUMNS_USER_NUMBER = "number";
        public static final String COLUMNS_USER_STATUS = "status";
    }

    public static class BlankEntry{
        public final static int NONE = 1;
        public final static int INBOX = 2;
        public final static int ARCHIVE = 3;

        public final static String _ID ="id";

        public final static String DATABASE_NAME = "CodeFriends";

        public final static String INBOX_TABLE_NAME = "inbox_list";
        public final static String COLUMNS_INBOX_USER_NAME = "name";
        public final static String COLUMNS_INBOX_USER_NUMBER = "number";
        public final static String COLUMNS_INBOX_USER_TAG = "tag";
        public static final String COLUMNS_INBOX_USER_STATUS = "status";
        public static final String COLUMNS_INBOX_USER_WHERE = "where_box";
        public final static String COLUMNS_INBOX_USER_IMAGE = "image";
    }

    public static class MessageEntry{
        public final static String MESSAGE_TABLE_NAME = "Message";
        public final static String COLUMNS_MESSAGE_TYPE = "type";
        public final static String COLUMNS_MESSAGE_Text = "text";
        public final static String COLUMNS_MESSAGE_TIMESTAMP = "timestamp";
        public final static String COLUMNS_MESSAGE_SENDER_ID = "senderId";
    }
}
