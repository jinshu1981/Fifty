package com.jinshu.xuzhi.fifty;

import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;

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
 * A placeholder fragment containing a simple view.
 */
public class FragmentTempComm extends Fragment {
    View rootView;
    private final String LOG_TAG = this.getClass().getSimpleName();
    private  String SERVER_IP = "192.168.0.13";//can not be load to github
    private Socket client;
    private PrintWriter printwriter;
    private static InputStreamReader inputStreamReader;
    private static BufferedReader bufferedReader;
    private static BufferedReader in;
    private EditText textField;
    private Button button;
    private String sendMesssage,receiveMessage;
    public FragmentTempComm() {
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        rootView = inflater.inflate(R.layout.fragment_temp_comm, container, false);
        textField = (EditText) rootView.findViewById(R.id.editText1); // reference to the text field
        button = (Button) rootView.findViewById(R.id.button1); // reference to the send button

        // Button press event listener
        button.setOnClickListener(new View.OnClickListener() {

            public void onClick(View v) {
                sendMesssage = textField.getText().toString(); // get the text message on the text field
                textField.setText(""); // Reset the text field to blank
                SendMessage sendMessageTask = new SendMessage();
                sendMessageTask.execute();
            }
        });
        return rootView;
    }

    private class SendMessage extends AsyncTask<Void, Void, Void> {

        @Override
        protected Void doInBackground(Void... params) {
            String line = "";


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
                    line = in.readLine();
                    System.out.println("Text received: " + line);
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
                jsonResponse = new JSONObject(line);

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

    }


}
