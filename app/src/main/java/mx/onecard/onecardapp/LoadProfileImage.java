package mx.onecard.onecardapp;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.AsyncTask;
import android.util.Log;
import android.widget.ImageView;

import java.io.InputStream;

/**
 * Created by OneCardAdmon on 22/06/2015.
 */
public class LoadProfileImage extends AsyncTask {
    ImageView downloadedImage;

    public LoadProfileImage(ImageView downloadedImage) {
        this.downloadedImage = downloadedImage;
    }

    @Override
    protected Object doInBackground(Object[] urls) {
        String url = urls[0].toString();
        Bitmap icon = null;
        try {
            InputStream in = new java.net.URL(url).openStream();
            icon = BitmapFactory.decodeStream(in);
        } catch (Exception e) {
            Log.e("Error", e.getMessage());
            e.printStackTrace();
        }
        return icon;
    }

    @Override
    protected void onPostExecute(Object o) {
        super.onPostExecute(o);
        downloadedImage.setImageBitmap((Bitmap) o);
    }
}
