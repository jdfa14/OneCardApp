package mx.onecard.socialnetworks;

import android.app.Activity;
import android.content.Intent;

import com.twitter.sdk.android.Twitter;
import com.twitter.sdk.android.core.Callback;
import com.twitter.sdk.android.core.Result;
import com.twitter.sdk.android.core.TwitterException;
import com.twitter.sdk.android.core.TwitterSession;
import com.twitter.sdk.android.core.identity.TwitterAuthClient;

/**
 * Created by OneCardAdmon on 07/07/2015.
 */
public class TwitterLoginManager {
    private static final TwitterLoginManager instance = new TwitterLoginManager();
    private TwitterLoginListener mListener;
    private TwitterAuthClient mTwitterAuthClient;
    private TwitterSession mTwitterSession;
    private Activity mActivity;
    private Callback<TwitterSession> mCallback;

    protected TwitterLoginManager(){
        mTwitterAuthClient = new TwitterAuthClient();
        mCallback = new Callback<TwitterSession>() {
            @Override
            public void success(Result<TwitterSession> result) {
                mTwitterSession = result.data;
                if(mListener != null)
                    mListener.success(result);
            }

            @Override
            public void failure(TwitterException e) {
                if(mListener != null)
                    mListener.failure(e);
            }
        };
        mTwitterSession = Twitter.getSessionManager().getActiveSession();
    }

    public TwitterLoginManager getInstance(Activity activity) {
        instance.setActivity(activity);
        return instance;
    }

    public boolean isLogedIn(){
        return !(mTwitterSession == null);
    }

    public void login(){
        mTwitterAuthClient.authorize(mActivity, mCallback);
    }

    public void getEmail(Callback<String> callback){
        if(mTwitterSession != null)
            mTwitterAuthClient.requestEmail(mTwitterSession, callback);
    }

    public void onActivityResult(int requestCode, int resultCode, Intent data){
        if(requestCode == mTwitterAuthClient.getRequestCode())
            mTwitterAuthClient.onActivityResult(requestCode,resultCode,data);
    }

    public void setActivity(Activity mActivity) {
        this.mActivity = mActivity;
    }

    public void registerTwitterLoginListener(TwitterLoginListener mListener){
        this.mListener = mListener;
    }

    interface TwitterLoginListener{
        void success(Result<TwitterSession> result);
        void failure(TwitterException e);
    }

}
