package com.BrayterConn.AudioPill;

import android.content.Context;
import android.os.AsyncTask;
import com.parse.Parse;

/**
 * Created by Adam on 4/16/2014.
 */
public class InitializeParse extends AsyncTask<String,Void,String> {
    Context context;
    CallbackListener mListener;

    public InitializeParse(Context contextNew) {
        context = contextNew;
    }

    public void setListener(CallbackListener listener) {
        mListener = listener;
    }

    @Override
    protected String doInBackground(String... params) {
        Parse.initialize(context, params[0], params[1]);
        return null;
    }

    @Override
    protected void onPostExecute(String result) {
        mListener.callback();
    }
}
