package com.BrayterConn.AudioPill;

import android.os.AsyncTask;
import android.speech.tts.TextToSpeech;

import org.apache.http.HttpResponse;

import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.DefaultHttpClient;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.UnsupportedEncodingException;

/**
 * Created by Adam on 4/8/2014.
 */
public class PostBarcodeAsync extends AsyncTask<String,Void,String> {
    @Override
    protected String doInBackground(String ... params) {
        postData(params[0],params[1]);
        return null;
    }

    @Override
    protected void onPostExecute(String result) {
        try {
            JSONObject jsonObject = new JSONObject(result);
            String name = jsonObject.get("name").toString();
            name = "name";
            myActivity.tts.speak(result, TextToSpeech.QUEUE_FLUSH, null);
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

    public String postData(String dataToPost,String url) {
        HttpClient httpClient = new DefaultHttpClient();
        HttpPost httpPost = new HttpPost(url);
        JSONObject jsonObject = new JSONObject();
        String result = "";
        try {
            jsonObject.accumulate("barcode", dataToPost);
            String json = jsonObject.toString();
            StringEntity se = new StringEntity(json);
            httpPost.setHeader("Accept", "application/json");
            httpPost.setHeader("Content-type", "application/json");
            HttpResponse httpResponse = httpClient.execute(httpPost);
            InputStream inputStream = httpResponse.getEntity().getContent();
            if (inputStream != null) {
                result = convertInputStreamToString(inputStream);
            } else {
                result = "Did not work!";
            }
        } catch (JSONException e) {
            e.printStackTrace();
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return result;
    }

    private String convertInputStreamToString(InputStream is) throws IOException{
        BufferedReader br = new BufferedReader(new InputStreamReader(is));
        String line;
        StringBuilder result = new StringBuilder();
        while((line=br.readLine()) != null) {
            result.append(line);
        }
        is.close();
        return result.toString();
    }
}
