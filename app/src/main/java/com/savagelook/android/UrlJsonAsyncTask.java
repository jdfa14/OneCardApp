package com.savagelook.android;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.os.AsyncTask;
import android.widget.Toast;

import org.json.JSONObject;

import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;

/**
 * Created by tonylukasavage on 02/19/2014.
 */
public abstract class UrlJsonAsyncTask extends AsyncTask<String, Void, JSONObject> {
    private static final String TAG = "UrlJsonAsyncTask";
    private static final String LOADING_TITLE = "";
    private static final String MESSAGE_LOADING = "Loading, please wait...";
    private static final String MESSAGE_BUSY = "Server is busy. Please try again.";
    private static final String MESSAGE_ERROR = "There was an error processing your request. Please try again.";
    private static final String REQUEST_METHOD = "GET";
    private static final int TIMEOUT_CONNECT = 0;
    private static final int TIMEOUT_READ = 0;
    private static final int RETRY_COUNT = 0;
    private static final String JSON_SUCCESS = "success";
    private static final String JSON_INFO = "info";


    private ProgressDialog progressDialog = null;
    private boolean showDialog = true;
    protected Context context = null;
    protected String parameters = "";
    private String loadingTitle;
    private String messageLoading;
    private String messageBusy;
    private String messageError;
    private String requestMethod;
    private int timeoutConnect;
    private int timeoutRead;
    private int retryCount;
    private String jsonSuccess;
    private String jsonInfo;

    public UrlJsonAsyncTask(Context context) {
        this.context = context;
        this.loadingTitle = LOADING_TITLE;
        this.messageLoading = MESSAGE_LOADING;
        this.messageBusy = MESSAGE_BUSY;
        this.messageError = MESSAGE_ERROR;
        this.timeoutConnect = TIMEOUT_CONNECT;
        this.timeoutRead = TIMEOUT_READ;
        this.retryCount = RETRY_COUNT;
        this.jsonSuccess = JSON_SUCCESS;
        this.jsonInfo = JSON_INFO;
        this.requestMethod = REQUEST_METHOD;
    }

    @Override
    protected abstract JSONObject doInBackground(String... urls);

    @Override
    protected void onPreExecute() {
        if (showDialog) {
            progressDialog = ProgressDialog.show(
                    this.context,
                    this.loadingTitle,
                    this.messageLoading,
                    true,
                    true,
                    new DialogInterface.OnCancelListener() {
                        @Override
                        public void onCancel(DialogInterface arg0) {
                            UrlJsonAsyncTask.this.cancel(true);
                        }
                    }
            );
        }
    }

    @Override
    protected void onPostExecute(JSONObject json) {
        if (progressDialog != null) {
            progressDialog.dismiss();
        }
        progressDialog = null;
    }

    public void setConnectionParams(int timeoutConnect, int timeoutRead, int retryCount) {
        this.timeoutConnect = timeoutConnect;
        this.timeoutRead = timeoutRead;
        this.retryCount = retryCount;
    }

    public String getLoadingTitle() {
        return loadingTitle;
    }

    public void setLoadingTitle(String loadingTitle) {
        this.loadingTitle = loadingTitle;
    }

    public String getMessageLoading() {
        return messageLoading;
    }

    public void setMessageLoading(String messageLoading) {
        this.messageLoading = messageLoading;
    }

    public String getMessageBusy() {
        return messageBusy;
    }

    public void setMessageBusy(String messageBusy) {
        this.messageBusy = messageBusy;
    }

    public String getMessageError() {
        return messageError;
    }

    public void setMessageError(String messageError) {
        this.messageError = messageError;
    }

    public int getTimeoutConnect() {
        return timeoutConnect;
    }

    public void setTimeoutConnect(int timeoutConnect) {
        this.timeoutConnect = timeoutConnect;
    }

    public int getTimeoutRead() {
        return timeoutRead;
    }

    public void setTimeoutRead(int timeoutRead) {
        this.timeoutRead = timeoutRead;
    }

    public int getRetryCount() {
        return retryCount;
    }

    public void setRetryCount(int retryCount) {
        this.retryCount = retryCount;
    }

    public String getJsonSuccess() {
        return jsonSuccess;
    }

    public void setJsonSuccess(String jsonSuccess) {
        this.jsonSuccess = jsonSuccess;
    }

    public String getJsonInfo() {
        return jsonInfo;
    }

    public void setJsonInfo(String jsonInfo) {
        this.jsonInfo = jsonInfo;
    }

    public void setRequestMethod(String requestMethod){
        this.requestMethod = requestMethod;
    }

    public String getRequestMethod(){
        return requestMethod;
    }

    public void showDialog(boolean showDialog) {
        this.showDialog = showDialog;
    }

    public void addParameter(String name, String value) {
        if (parameters != "") {
            parameters += "&";
        }
        try {
            parameters += name + "=" + URLEncoder.encode(value, "UTF-8");
        } catch (UnsupportedEncodingException e) {
            Toast.makeText(context, "Error: " + e.getMessage(), Toast.LENGTH_LONG).show();
        }
    }
}