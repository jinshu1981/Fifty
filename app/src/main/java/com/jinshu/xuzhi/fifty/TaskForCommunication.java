package com.jinshu.xuzhi.fifty;

import android.content.Context;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.Socket;
import java.net.UnknownHostException;

/**
 * Created by xuzhi on 2016/9/21.
 */
public class TaskForCommunication extends AsyncTask<Void, Void, Void> {
    private final String LOG_TAG = this.getClass().getSimpleName();
    private  String SERVER_IP = "192.168.0.13";//can not be load to github
    private Socket client;
    private PrintWriter printwriter;
   // private static InputStreamReader inputStreamReader;
   // private static BufferedReader bufferedReader;
    private static BufferedReader in;
    //private EditText textField;
    //private Button button;
    private String sendMesssage = "initiate",receiveMessage = "";

    private final Context mContext;
    private final FragmentActivity mActivity;

    public TaskForCommunication(FragmentActivity context) {
        mContext = context;
        mActivity = context;

    }
    @Override
    protected Void doInBackground(Void... params) {
        try {

            client = new Socket(SERVER_IP, 4444); // connect to the server
            printwriter = new PrintWriter(client.getOutputStream(), true);
            in = new BufferedReader(new InputStreamReader(
                    client.getInputStream()));
            printwriter.println(sendMesssage);
            //printwriter.write(sendMesssage); // write the message to output stream
            //printwriter.flush();
            //printwriter.close();

            try{
                receiveMessage = in.readLine();
                System.out.println("Text received: " + receiveMessage);
            } catch (IOException e){
                e.printStackTrace();
                System.out.println("Read failed");
            }
            JSONObject jsonResponse;


            //client.close(); // closing the connection
            //printwriter.close();
        } catch (UnknownHostException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }

            /*parse json string*/
        String OutputData = "";
        JSONObject jsonResponse;
        try {

            /****** Creates a new JSONObject with name/value mappings from the JSON string. ********/
            jsonResponse = new JSONObject(receiveMessage);

            /***** Returns the value mapped by name if it exists and is a JSONArray. ***/
            /*******  Returns null otherwise.  *******/
            JSONArray jsonMainNode = jsonResponse.optJSONArray("gigs");

            /*********** Process each JSON Node ************/

            int lengthJsonArr = jsonMainNode.length();

            for(int i=0; i < lengthJsonArr; i++)
            {
                /****** Get Object for each JSON node.***********/
                JSONObject jsonChildNode = jsonMainNode.getJSONObject(i);

                /******* Fetch node values **********/
                int id        = jsonChildNode.optInt("id");
                String title   = jsonChildNode.optString("title");
                String author  = jsonChildNode.optString("author");
                String price   = jsonChildNode.optString("price");
                String score   = jsonChildNode.optString("score");

                OutputData += "Node : \n\n     "+ id +" | "
                        + title +" | "
                        + author +" | "
                        + price +" | "
                        + score +" \n\n ";
                //Log.i("JSON parse", song_name);
            }
        } catch (JSONException e) {

            e.printStackTrace();
        }finally {
            System.out.println(OutputData);
        }
        return null;
    }
    @Override
    protected void onPostExecute(Void result) {
        /*load main fragment*/
        Fragment fragment = new com.jinshu.xuzhi.fifty.MainActivityFragment();
        Bundle bundle = new Bundle();
        bundle.putString("receiveMessage", receiveMessage);
        System.out.println("bundles: " + receiveMessage);
        fragment.setArguments(bundle);
        FragmentManager manager = mActivity.getSupportFragmentManager();
        FragmentTransaction transaction = manager.beginTransaction();
        transaction.replace(R.id.fragment, fragment);
        transaction.commit();

        /*send intent to main fragment*/

    }
}