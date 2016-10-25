package com.jinshu.xuzhi.fifty;

import android.content.Context;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.util.Log;

import com.example.SocketCommProtocol;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.Socket;
import java.net.UnknownHostException;

/**
 * Created by xuzhi on 2016/9/21.
 */
public class TaskForCommunication extends AsyncTask<String, Void, Void> {
    private final String LOG_TAG = this.getClass().getSimpleName();
    private  String SERVER_IP = "192.168.0.13";//can not be load to github
    private Socket client;
    private PrintWriter printwriter;
    private static BufferedReader in;
    TaskListener listener;

    private String receiveMessage = "";
    private String requestMessage = "";
    private final Context mContext;
    private final FragmentActivity mActivity;

    public TaskForCommunication(FragmentActivity context) {
        mContext = context;
        mActivity = context;
    }
    public TaskForCommunication(FragmentActivity context,TaskListener listener) {
        mContext = context;
        mActivity = context;
        this.listener = listener;
    }
    @Override
    protected Void doInBackground(String... params) {
        try {
            if (params.length == 0) {
                return null;
            }

            requestMessage = params[0];
            System.out.println(requestMessage);
            client = new Socket(SERVER_IP, 4444); // connect to the server
            printwriter = new PrintWriter(client.getOutputStream(), true);
            in = new BufferedReader(new InputStreamReader(
                    client.getInputStream()));
            printwriter.println(requestMessage);
            System.out.println("message send " + requestMessage);

            try{
                receiveMessage = in.readLine();
                System.out.println("Text received: " + receiveMessage);
            } catch (IOException e){
                e.printStackTrace();
                System.out.println("Read failed");
            }
        } catch (UnknownHostException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return null;
    }
    @Override
    protected void onPostExecute(Void result) {
        /*load main fragment*/
        if (requestMessage.equals(SocketCommProtocol.INIT_REQUEST)){
            Fragment fragment = new com.jinshu.xuzhi.fifty.MainActivityFragment();
            Bundle bundle = new Bundle();
            bundle.putString("receiveMessage", receiveMessage);
            System.out.println("bundles: " + receiveMessage);
            fragment.setArguments(bundle);
            FragmentManager manager = mActivity.getSupportFragmentManager();
            FragmentTransaction transaction = manager.beginTransaction();
            transaction.replace(R.id.fragment, fragment);
            transaction.commit();
        }
        else if (requestMessage.equals(SocketCommProtocol.CATEGORY_REQUEST)){
            Intent intent = new Intent(mContext, com.jinshu.xuzhi.fifty.ActivityCategoryList.class);
            intent.putExtra(Intent.EXTRA_TEXT,receiveMessage);
            mContext.startActivity(intent);
            Log.e(LOG_TAG, "start ActivityCategoryList");
        }
        else if (requestMessage.contains(SocketCommProtocol.SUBCATEGORY_REQUEST)){
            Intent intent = new Intent(mContext, com.jinshu.xuzhi.fifty.ActivitySubcategoryList.class);
            intent.putExtra(Intent.EXTRA_TEXT,receiveMessage);
            mContext.startActivity(intent);
        }
        else if (requestMessage.contains(SocketCommProtocol.GIG_DETAIL_REQUEST)){
            Intent intent = new Intent(mContext, com.jinshu.xuzhi.fifty.ActivityGigDetail.class);
            intent.putExtra(Intent.EXTRA_TEXT,receiveMessage);
            mContext.startActivity(intent);
        }
        else if  (requestMessage.contains(SocketCommProtocol.GIG_REVIEW_REQUEST))
        {
            listener.onReviewTaskCompleted(receiveMessage);
        }
        else{
            Log.e(LOG_TAG,"error request message:" + requestMessage);
        }
    }
}