package com.example.afi.sourcecodepage;


import android.content.Context;
import android.support.v4.content.AsyncTaskLoader;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;

/**
 * Created by afi on 21/10/2017.
 */

public class ConnectInternet extends AsyncTaskLoader<String> {
    String result,url;
    boolean cancel = false;

    public ConnectInternet(Context con, String Url){
        super(con);
        url = Url;
    }

    @Override
    public String loadInBackground() {
        InputStream in = null;
        HttpURLConnection myConn = null;
        try{
            URL myURL = new URL(url);
            myConn = (HttpURLConnection) myURL.openConnection();
            myConn.setConnectTimeout(10000);
            myConn.setRequestMethod("GET");
            myConn.connect();

            if(myConn.getResponseCode()==HttpURLConnection.HTTP_OK){
                in=myConn.getInputStream();
                if(in!=null){
                    BufferedReader myBuf = new BufferedReader(new InputStreamReader(in));
                    StringBuilder st = new StringBuilder();
                    String line="";

                    while((line = myBuf.readLine()) != null){
                        st.append(line+"  \n");
                    }
                    return st.toString();
                }
            }
            else{
                return "Error Response Code"+myConn.getResponseCode();
            }
        }catch (Exception e){
            e.printStackTrace();
            return "Unknown Error";
        }
        finally {
            if(in!=null){
                try{
                    in.close();
                }
                catch (IOException e){
                    e.printStackTrace();
                }
            }
            myConn.disconnect();
        }
        return null;
    }

    @Override
    protected void onStartLoading() {
        if(result!=null||cancel){
            deliverResult(result);
        }else{
            forceLoad();
        }
    }

    @Override
    public void deliverResult(String data) {
        super.deliverResult(data);
        result=data;
    }

    @Override
    public void onCanceled(String data) {
        super.onCanceled(data);
        cancel = true;
    }
}
