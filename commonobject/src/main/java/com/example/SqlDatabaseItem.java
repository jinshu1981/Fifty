package com.example;

/**
 * Created by xuzhi on 2016/9/28.
 */
public class SqlDatabaseItem {
    public static final String DATABASE_NAME = "fifty";

    public static final class CategoryTable{
        public static final String TABLE_NAME = "category";
        public static final String COLUMN_ID = "id";
        public static final String COLUMN_NAME = "name";
        public static final String COLUMN_URL = "url";
        public static final String COLUMN_INTRODUCTION = "introduction";
        public static final String COLUMN_PURCHASETIME = "purchaseTimes";
    }

    public static final class SubcategoryTable{
        public static final String TABLE_NAME = "subcategory";
        public static final String COLUMN_ID = "id";
        public static final String COLUMN_CATEGORY = "category";
        public static final String COLUMN_SUBCATEGORY = "subcategory";
        public static final String COLUMN_PURCHASETIME = "purchaseTimes";
    }
    public static final class GigTable{
        public static final String TABLE_NAME = "gig";
        public static final String COLUMN_ID = "id";
        public static final String COLUMN_TITLE = "title";
        public static final String COLUMN_SELLER = "seller";
        public static final String COLUMN_PRICE = "price";
        public static final String COLUMN_SCORE = "score";
        public static final String COLUMN_SUBCATEGORY = "subcategory";
        public static final String COLUMN_PICTURES = "pictures";
        public static final String COLUMN_CREATEDTIME = "createdTime";
        public static final String COLUMN_INTRODUCTION = "introduction";
        public static final String COLUMN_EXTRAS = "extras";
    }

    public static final class SellerTable{
        public static final String TABLE_NAME = "seller";
        public static final String COLUMN_NAME = "name";
        public static final String COLUMN_LEVEL = "level";
        public static final String COLUMN_IMAGEICON = "imageicon";
        public static final String COLUMN_INTRODUCTION = "introduction";
        public static final String COLUMN_AVGRESPTIME = "avgResponseTime";
        public static final String COLUMN_LAND = "land";
        public static final String COLUMN_LASTACTVIETIME = "lastActiveTime";
    }
    public static final class ReviewTable{
        public static final String TABLE_NAME = "review";
        public static final String COLUMN_ID = "id";
        public static final String COLUMN_GIGID = "gidId";
        public static final String COLUMN_CONTENT = "content";
        public static final String COLUMN_VOTESCORE = "voteScore";
        public static final String COLUMN_AUTHOR = "author";
        public static final String COLUMN_FEEDBACK = "feedback";
    }
}
