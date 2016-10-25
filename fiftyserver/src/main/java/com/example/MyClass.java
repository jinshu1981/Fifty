package com.example;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.ServerSocket;
import java.net.Socket;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;

import javax.json.Json;
import javax.json.stream.JsonGenerator;

public class MyClass {

    private static ServerSocket serverSocket;
    private static Socket clientSocket;
    private static InputStreamReader inputStreamReader;
    private static BufferedReader bufferedReader;
    private static String requestMessage;
    private static String message;

    //static String INIT_REQUEST = "initiate";
    //static String CATEGORY_REQUEST = "category";

    static String USER_NAME = "root";
    static String PASSWORD = "111111";
    static String url = "jdbc:mysql://localhost:3306/fifty";

    /*static String DBNAME = "fifty";
    static String TABLE_NAME_GIGS = "Gigs";
    static String TABLE_NAME_CATEGORY = "category";
    static String TABLE_NAME_SUBCATEGORY = "subcategory";
    static String TABLE_NAME_SELLER = "seller";
    static String TABLE_NAME_REVIEW = "review";*/
    private static PrintWriter printwriter;
    static class CategorySort{
        String categoryName = "";
        int totalPurchaseTime = 0;
        String introduction = "";
        String url = "";
        CategorySort(String categoryName1,int totalPurchaseTime1,String introduction1,String url1)
        {
            categoryName = categoryName1;
            totalPurchaseTime = totalPurchaseTime1;
            introduction = introduction1;
            url = url1;
        }
    }
    static List<CategorySort> categoryList = new ArrayList<CategorySort>();
    public static void main(String[] args) {
        //open server socket
        try {
            serverSocket = new ServerSocket(4444); // Server socket

        } catch (IOException e) {
            System.out.println("Could not listen on port: 4444");
        }

        System.out.println("Server started. Listening to the port 4444");
        /*listen to client socket*/
        while (true) {
            try {
                clientSocket = serverSocket.accept(); // accept the client connection
                inputStreamReader = new InputStreamReader(clientSocket.getInputStream());
                bufferedReader = new BufferedReader(inputStreamReader); // get the client message
                requestMessage = bufferedReader.readLine();
                System.out.println(requestMessage);
                message = requestMessage.split("/")[0];
                System.out.println(requestMessage + "-"+ message);

                Connection con = null;
                try{
                    //Class.forName("com.mysql.jdbc.Driver").newInstance();
                    con = DriverManager.getConnection(url, USER_NAME, PASSWORD);
                    Statement stmt = con.createStatement();
                    Statement stmt1 = con.createStatement();
                    switch (message)
                    {
                        case  SocketCommProtocol.INIT_REQUEST: //"initiate":
                            sendInitiateMessage(stmt, stmt1);
                            break;
                        case SocketCommProtocol.CATEGORY_REQUEST: //"category":
                            sendCategoryListJsonMessage(stmt);
                            break;
                        case SocketCommProtocol.SUBCATEGORY_REQUEST: //"subcategory":
                            sendSubcategoryListJsonMessage(stmt,requestMessage);
                            break;
                        case SocketCommProtocol.GIG_DETAIL_REQUEST: //"gigDetails":
                            sendGigDetailJsonMessage(stmt,requestMessage);
                            break;
                        case SocketCommProtocol.GIG_REVIEW_REQUEST:
                            sendGigReviewJsonMessage(stmt,requestMessage);
                            break;
                        default:
                            break;
                    }
                }catch (Exception ex) {
                    // ... code to handle exceptions
                    ex.printStackTrace();
                } finally {
                    try{
                        if (con != null) con.close();
                    }catch (Exception ex) {
                        // ... code to handle exceptions
                        ex.printStackTrace();
                    }
                }
                inputStreamReader.close();
                clientSocket.close();

            } catch (IOException ex) {
                System.out.println("error in message reading");
            }
        }

    }
    static void sendInitiateMessage(Statement stmt,Statement stmt1)
    {
        String query = "select * " +
                "from " + SqlDatabaseItem.DATABASE_NAME + "."+ SqlDatabaseItem.GigTable.TABLE_NAME;
        String query1 = "select * " +
                "from " + SqlDatabaseItem.DATABASE_NAME + "."+ SqlDatabaseItem.CategoryTable.TABLE_NAME;
        categoryList.clear();
        try {
            ResultSet rs = stmt.executeQuery(query);
            ResultSet rs1 = stmt1.executeQuery(query1);
            //System.out.println("rs = " + rs.toString());
            while(rs1.next())
            {
                String categoryName = rs1.getString(SqlDatabaseItem.CategoryTable.COLUMN_NAME);
                String introduction = rs1.getString(SqlDatabaseItem.CategoryTable.COLUMN_INTRODUCTION);//"introduction"
                String iconUrl = rs1.getString(SqlDatabaseItem.CategoryTable.COLUMN_URL);//"url"
                int purchaseTime = rs1.getInt(SqlDatabaseItem.CategoryTable.COLUMN_PURCHASETIME);//"purchaseTimes"

                CategorySort categorySort = new CategorySort(categoryName, purchaseTime,introduction,iconUrl);
                categoryList.add(categorySort);
            }
            /*categoryList根据totalPurchaseTime从大到小排序*/
            Collections.sort(categoryList, new Comparator() {
                public int compare(Object synchronizedListOne, Object synchronizedListTwo) {
                    //use instanceof to verify the references are indeed of the type in question
                    return (((CategorySort) synchronizedListOne).totalPurchaseTime >
                            ((CategorySort) synchronizedListTwo).totalPurchaseTime) ? -1 : 1;
                }
            });
            /*generate json stream*/
            PrintWriter printwriter = new PrintWriter(clientSocket.getOutputStream(),
                    true);
            JsonGenerator generator = Json.createGenerator(printwriter);

            generator.writeStartObject();
            //add top categories
            generator.writeStartArray(JsonDefination.TOP_CATEGORY_ARRAY)
                    .writeStartObject()
                    .write(SqlDatabaseItem.CategoryTable.COLUMN_NAME, categoryList.get(0).categoryName)
                    .write(SqlDatabaseItem.CategoryTable.COLUMN_INTRODUCTION, categoryList.get(0).introduction)
                    .write(SqlDatabaseItem.CategoryTable.COLUMN_URL, categoryList.get(0).url)
                    .writeEnd()
                    .writeStartObject()
                    .write(SqlDatabaseItem.CategoryTable.COLUMN_NAME, categoryList.get(1).categoryName)
                    .write(SqlDatabaseItem.CategoryTable.COLUMN_INTRODUCTION, categoryList.get(1).introduction)
                    .write(SqlDatabaseItem.CategoryTable.COLUMN_URL, categoryList.get(1).url)
                    .writeEnd()
                    .writeStartObject()
                    .write(SqlDatabaseItem.CategoryTable.COLUMN_NAME, categoryList.get(2).categoryName)
                    .write(SqlDatabaseItem.CategoryTable.COLUMN_INTRODUCTION, categoryList.get(2).introduction)
                    .write(SqlDatabaseItem.CategoryTable.COLUMN_URL, categoryList.get(2).url)
                    .writeEnd()
                    .writeEnd();
            //add featured gigs
            generator.writeStartArray(JsonDefination.FEATURED_GIG_ARRAY);
            while (rs.next()) {
                String title = rs.getString(SqlDatabaseItem.GigTable.COLUMN_TITLE);
                int keyId = rs.getInt(SqlDatabaseItem.GigTable.COLUMN_ID);
                String seller = rs.getString(SqlDatabaseItem.GigTable.COLUMN_SELLER);
                int price = rs.getInt(SqlDatabaseItem.GigTable.COLUMN_PRICE);
                int score = rs.getInt(SqlDatabaseItem.GigTable.COLUMN_SCORE);
                generator
                    .writeStartObject()
                        .write(SqlDatabaseItem.GigTable.COLUMN_ID, keyId)
                        .write(SqlDatabaseItem.GigTable.COLUMN_TITLE, title)
                        .write(SqlDatabaseItem.GigTable.COLUMN_SELLER, seller)
                        .write(SqlDatabaseItem.GigTable.COLUMN_PRICE, price)
                        .write(SqlDatabaseItem.GigTable.COLUMN_SCORE, score)
                    .writeEnd();


                System.out.println(title + "\t" + keyId +
                        "\t" + seller + "\t" + price +
                        "\t" + score);

            }
            generator.writeEnd().writeEnd();
            generator.close();
            // ... code to use the connection con
        } catch (Exception ex) {
            // ... code to handle exceptions
            ex.printStackTrace();
        }
    }

    static void sendCategoryListJsonMessage(Statement stmt)
    {
        String query = "select * " +
                "from " + SqlDatabaseItem.DATABASE_NAME + "."+ SqlDatabaseItem.CategoryTable.TABLE_NAME;
        categoryList.clear();
        try {
            ResultSet rs = stmt.executeQuery(query);
            //System.out.println("rs = " + rs.toString());
            while(rs.next())
            {
                String categoryName = rs.getString( SqlDatabaseItem.CategoryTable.COLUMN_NAME);
                String introduction = rs.getString( SqlDatabaseItem.CategoryTable.COLUMN_INTRODUCTION);
                String iconUrl = rs.getString(SqlDatabaseItem.CategoryTable.COLUMN_URL);
                int purchaseTime = rs.getInt(SqlDatabaseItem.CategoryTable.COLUMN_PURCHASETIME);

                CategorySort categorySort = new CategorySort(categoryName, purchaseTime,introduction,iconUrl);
                categoryList.add(categorySort);
            }
            /*categoryList根据totalPurchaseTime从大到小排序*/
            Collections.sort(categoryList, new Comparator() {
                public int compare(Object synchronizedListOne, Object synchronizedListTwo) {
                    //use instanceof to verify the references are indeed of the type in question
                    return (((CategorySort) synchronizedListOne).totalPurchaseTime >
                            ((CategorySort) synchronizedListTwo).totalPurchaseTime) ? -1 : 1;
                }
            });
            /*generate json stream*/
            PrintWriter printwriter = new PrintWriter(clientSocket.getOutputStream(),
                    true);
            JsonGenerator generator = Json.createGenerator(printwriter);

            //add top categories
            generator.writeStartObject().writeStartArray(JsonDefination.TOP_CATEGORY_ARRAY);
            for (CategorySort categorySort :categoryList) {
                generator
                        .writeStartObject()
                        .write(SqlDatabaseItem.CategoryTable.COLUMN_NAME, categorySort.categoryName)
                        .write(SqlDatabaseItem.CategoryTable.COLUMN_INTRODUCTION, categorySort.introduction)
                        .write(SqlDatabaseItem.CategoryTable.COLUMN_URL, categorySort.url)
                        .writeEnd();
            }
            generator.writeEnd().writeEnd().close();
            // ... code to use the connection con
        } catch (Exception ex) {
            // ... code to handle exceptions
            ex.printStackTrace();
        }
    }

    static void sendSubcategoryListJsonMessage(Statement stmt,String requestMessage)
    {
        String category = requestMessage.split("/")[1];
        String query = "select subcategory " +
                "from " + SqlDatabaseItem.DATABASE_NAME + "."+ SqlDatabaseItem.SubcategoryTable.TABLE_NAME +
                " where category = " + "'"+category+ "'";
        //System.out.println("category = " + category);
        //System.out.println("query = " + query);
        try {
            ResultSet rs = stmt.executeQuery(query);
            /*generate json stream*/
            PrintWriter printwriter = new PrintWriter(clientSocket.getOutputStream(),
                    true);
            JsonGenerator generator = Json.createGenerator(printwriter);

            //add top categories
            generator.writeStartObject().writeStartArray(JsonDefination.SUBCATEGORY_ARRAY);

            while(rs.next())
            {
                String subcategoryName = rs.getString(SqlDatabaseItem.SubcategoryTable.COLUMN_SUBCATEGORY);
                generator
                        .writeStartObject()
                        .write(SqlDatabaseItem.SubcategoryTable.COLUMN_SUBCATEGORY, subcategoryName)
                        .writeEnd();
            }
            generator.writeEnd().writeEnd().close();
        } catch (Exception ex) {
            // ... code to handle exceptions
            ex.printStackTrace();
        }
    }
    static void sendGigDetailJsonMessage(Statement stmt,String requestMessage)
    {
        int gigId = Integer.parseInt(requestMessage.split("/")[1])  ;
        String query = "select * " +
                "from " + SqlDatabaseItem.GigTable.TABLE_NAME /*+ ", " +  SqlDatabaseItem.SellerTable.TABLE_NAME*/ /*+ ", " +  SqlDatabaseItem.ReviewTable.TABLE_NAME */+
                " INNER JOIN "+ SqlDatabaseItem.SellerTable.TABLE_NAME + " ON " +  SqlDatabaseItem.GigTable.TABLE_NAME + ".seller = " + SqlDatabaseItem.SellerTable.TABLE_NAME  +".name"+
                " INNER JOIN "+ SqlDatabaseItem.ReviewTable.TABLE_NAME + " ON " +  SqlDatabaseItem.GigTable.TABLE_NAME + ".id = " + SqlDatabaseItem.ReviewTable.TABLE_NAME  +".gigId"+
                " where gig.id = " + gigId +
                " ORDER by review.id ASC";
        System.out.println("query = " + query);

        try {
            ResultSet rs = stmt.executeQuery(query);
            /*generate json stream*/
            PrintWriter printwriter = new PrintWriter(clientSocket.getOutputStream(),
                    true);
            JsonGenerator generator = Json.createGenerator(printwriter);
            /*gigsInfo*/
            //rs.next();
            /*while(rs.next())
            {
               int id =   rs.getInt(SqlDatabaseItem.GigTable.TABLE_NAME + ".id");
               System.out.println("gig id = " + id);
                String seller = rs.getString(SqlDatabaseItem.SellerTable.TABLE_NAME +".name");
                System.out.println("seller name= " + seller);
                int reviewId = rs.getInt(SqlDatabaseItem.ReviewTable.TABLE_NAME + ".id");
                System.out.println("seller reviewId= " + reviewId);
            }*/
            int i = 0;
            while(rs.next()){
            if (i == 0) {
                int id = rs.getInt(SqlDatabaseItem.GigTable.TABLE_NAME + ".id");

                int price = rs.getInt(SqlDatabaseItem.GigTable.COLUMN_PRICE);
                int score = rs.getInt(SqlDatabaseItem.GigTable.COLUMN_SCORE);
                String title = rs.getString(SqlDatabaseItem.GigTable.COLUMN_TITLE);
                String seller = rs.getString(SqlDatabaseItem.GigTable.TABLE_NAME + "." + SqlDatabaseItem.GigTable.COLUMN_SELLER);
                String subcategory = rs.getString(SqlDatabaseItem.GigTable.COLUMN_SUBCATEGORY);
                String pictures = rs.getString(SqlDatabaseItem.GigTable.COLUMN_PICTURES);
                String gigIntroduction = rs.getString(SqlDatabaseItem.GigTable.TABLE_NAME + "." + SqlDatabaseItem.GigTable.COLUMN_INTRODUCTION);
                String extras = rs.getString(SqlDatabaseItem.GigTable.COLUMN_EXTRAS);
                String time = rs.getString(SqlDatabaseItem.GigTable.COLUMN_CREATEDTIME);
                /*sellerInfo*/
                int sellerLevel = rs.getInt(SqlDatabaseItem.SellerTable.COLUMN_LEVEL);
                String sellerImageicon = rs.getString(SqlDatabaseItem.SellerTable.COLUMN_IMAGEICON);
                String sellerIntroduction = rs.getString(SqlDatabaseItem.SellerTable.TABLE_NAME + "." + SqlDatabaseItem.SellerTable.COLUMN_INTRODUCTION);
                String sellerAvgResponseTime = rs.getString(SqlDatabaseItem.SellerTable.COLUMN_AVGRESPTIME);
                String sellerFrom = rs.getString(SqlDatabaseItem.SellerTable.COLUMN_LAND);
                String sellerLastactive = rs.getString(SqlDatabaseItem.SellerTable.COLUMN_LASTACTVIETIME);
                //System.out.println("review:{ author:" + author +" };");
           /* System.out.println("gigs:{" + id + " :" + price +" :" + score +" :" + title +" :" +
                    seller +" :" + subcategory +" :" + pictures +" :" + gigIntroduction +" :" +
                    extras +" :" + time +" };");
            System.out.println("seller:{" + sellerLevel + " :" + sellerImageicon + " :" + sellerIntroduction + " :" + sellerAvgResponseTime + " :" +
                    sellerFrom + " :" + sellerLastactive + " };");*/

                generator.writeStartObject()
                        .writeStartObject(SocketCommProtocol.GIG_DETAIL_REQUEST)
                            .writeStartObject(SqlDatabaseItem.GigTable.TABLE_NAME)
                                .write(SqlDatabaseItem.GigTable.COLUMN_ID, id)
                                .write(SqlDatabaseItem.GigTable.COLUMN_PRICE, price)
                                .write(SqlDatabaseItem.GigTable.COLUMN_SCORE, score)
                                .write(SqlDatabaseItem.GigTable.COLUMN_TITLE, title)
                                .write(SqlDatabaseItem.GigTable.COLUMN_SELLER, seller)
                                .write(SqlDatabaseItem.GigTable.COLUMN_SUBCATEGORY, subcategory)
                                .write(SqlDatabaseItem.GigTable.COLUMN_PICTURES, pictures)
                                .write(SqlDatabaseItem.GigTable.COLUMN_INTRODUCTION, gigIntroduction)
                                .write(SqlDatabaseItem.GigTable.COLUMN_EXTRAS, extras)
                                .write(SqlDatabaseItem.GigTable.COLUMN_CREATEDTIME, time)
                            .writeEnd()
                        .writeStartObject(SqlDatabaseItem.SellerTable.TABLE_NAME)
                            .write(SqlDatabaseItem.SellerTable.COLUMN_NAME, seller)
                            .write(SqlDatabaseItem.SellerTable.COLUMN_LEVEL, sellerLevel)
                            .write(SqlDatabaseItem.SellerTable.COLUMN_IMAGEICON, sellerImageicon)
                            .write(SqlDatabaseItem.SellerTable.COLUMN_INTRODUCTION, sellerIntroduction)
                            .write(SqlDatabaseItem.SellerTable.COLUMN_AVGRESPTIME, sellerAvgResponseTime)
                            .write(SqlDatabaseItem.SellerTable.COLUMN_LAND, sellerFrom)
                            .write(SqlDatabaseItem.SellerTable.COLUMN_LASTACTVIETIME, sellerLastactive)
                        .writeEnd();
                generator.writeStartArray(JsonDefination.REVIEW_ARRAY);
            }
                if (i < JsonDefination.REVIEW_LIMIT)//review limit
                {
                    String feedback = rs.getString(SqlDatabaseItem.ReviewTable.COLUMN_FEEDBACK);
                    if (feedback == null)
                    {
                        feedback = JsonDefination.INVALID_FEEDBACK_TAG;
                    }
                    generator
                            .writeStartObject()
                            .write(SqlDatabaseItem.ReviewTable.COLUMN_ID, rs.getInt(SqlDatabaseItem.ReviewTable.TABLE_NAME + ".id"))
                                    .write(SqlDatabaseItem.ReviewTable.COLUMN_AUTHOR, rs.getString(SqlDatabaseItem.ReviewTable.COLUMN_AUTHOR))
                                    .write(SqlDatabaseItem.ReviewTable.COLUMN_CONTENT, rs.getString(SqlDatabaseItem.ReviewTable.COLUMN_CONTENT))
                                    .write(SqlDatabaseItem.ReviewTable.COLUMN_VOTESCORE, rs.getString(SqlDatabaseItem.ReviewTable.COLUMN_VOTESCORE))
                                    .write(SqlDatabaseItem.ReviewTable.COLUMN_FEEDBACK, feedback)
                                    .writeEnd();
                }
                i++;

            }
            generator.writeEnd();/*SqlDatabaseItem.ReviewTable.TABLE_NAME*/
            generator.writeStartObject(JsonDefination.TOTAL_REVIEW_NUMBER).write(JsonDefination.TOTAL_REVIEW_NUMBER,i).writeEnd();
            generator.writeEnd()/*GIG_DETAIL_REQUEST*/.writeEnd()/*root object*/;
            generator.close();
        } catch (Exception ex) {
            // ... code to handle exceptions
            ex.printStackTrace();
        }

    }
    static void sendGigReviewJsonMessage(Statement stmt,String requestMessage)
    {
        String[] requestPara = requestMessage.split("/");
        int gigId = Integer.parseInt(requestPara[1]);
        int minReviewId = Integer.parseInt(requestPara[2]);
        String query = "select * " +
                "from " + SqlDatabaseItem.ReviewTable.TABLE_NAME  +
                " where gigId = " + gigId + " and id > " + minReviewId +
                " ORDER by id ASC" +
                " Limit " + JsonDefination.REVIEW_LIMIT;
        System.out.println("query = " + query);

        try {
            ResultSet rs = stmt.executeQuery(query);
            /*generate json stream*/
            PrintWriter printwriter = new PrintWriter(clientSocket.getOutputStream(),
                    true);
            JsonGenerator generator = Json.createGenerator(printwriter);
            /*review info*/
            int i = 0;
            generator.writeStartObject().writeStartArray(JsonDefination.REVIEW_ARRAY);
            while(rs.next()){

                if (i < JsonDefination.REVIEW_LIMIT)//review limit
                {
                    String feedback = rs.getString(SqlDatabaseItem.ReviewTable.COLUMN_FEEDBACK);
                    if (feedback == null)
                    {
                        feedback = JsonDefination.INVALID_FEEDBACK_TAG;
                    }
                    generator
                            .writeStartObject()
                            .write(SqlDatabaseItem.ReviewTable.COLUMN_ID,rs.getInt(SqlDatabaseItem.ReviewTable.COLUMN_ID))
                            .write(SqlDatabaseItem.ReviewTable.COLUMN_AUTHOR, rs.getString(SqlDatabaseItem.ReviewTable.COLUMN_AUTHOR))
                            .write(SqlDatabaseItem.ReviewTable.COLUMN_CONTENT, rs.getString(SqlDatabaseItem.ReviewTable.COLUMN_CONTENT))
                            .write(SqlDatabaseItem.ReviewTable.COLUMN_VOTESCORE, rs.getString(SqlDatabaseItem.ReviewTable.COLUMN_VOTESCORE))
                            .write(SqlDatabaseItem.ReviewTable.COLUMN_FEEDBACK, feedback)
                            .writeEnd();
                }
                i++;

            }
            generator.writeEnd().writeEnd();
            generator.close();
        } catch (Exception ex) {
            // ... code to handle exceptions
            ex.printStackTrace();
        }

    }
}
