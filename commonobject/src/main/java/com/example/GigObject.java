package com.example;

/**
 * Created by xuzhi on 2016/9/28.
 */
public class GigObject {
    public int id;
    public String title = "";
    public String seller = "";
    public int price = 0;
    public int score = 0;
    public String subcategory = "";
    public String[] picture ;
    public String createdTime = "";
    public String introduction = "";
    public Extra[] extras;

    public class Extra{
        public String content;
        public String price;
        public Extra(String content,String price)
        {
            this.content = content;
            this.price = price;
        }
    }
}
