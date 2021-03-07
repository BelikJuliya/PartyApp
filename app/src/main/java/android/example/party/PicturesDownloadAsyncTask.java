package android.example.party;

import android.example.party.view.MainActivity;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.AsyncTask;
import android.util.Log;

import java.io.BufferedInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.lang.ref.WeakReference;
import java.net.HttpURLConnection;
import java.net.URL;

public class PicturesDownloadAsyncTask extends AsyncTask<String, Void, Bitmap> {
    private static final String TAG = "AsyncTask";
    private WeakReference<MainActivity> mActivity;

    public PicturesDownloadAsyncTask(MainActivity activity) {
        mActivity = new WeakReference<>(activity);
    }

    @Override
    protected Bitmap doInBackground(String... strings) {
        Bitmap bitmap = null;

        HttpURLConnection urlConnection = null;
        try {
            URL url = new URL(strings[0]);
            Log.i(TAG, "doInBackground: trying to connect");
            urlConnection = (HttpURLConnection) url.openConnection();
            int responseCode = urlConnection.getResponseCode();
            if (responseCode == 200) {
                Log.i(TAG, "doInBackground: connected successfully");
            } else {
                Log.e(TAG, "doInBackground: connection failed, error: " + responseCode);
            }
            InputStream in = new BufferedInputStream(urlConnection.getInputStream());
            bitmap = BitmapFactory.decodeStream(in);
            in.close();
        } catch (IOException e) {
            Log.d(TAG, "doInBackground: " + e.getMessage());
            e.printStackTrace();
        } finally {
            assert urlConnection != null;
            urlConnection.disconnect();
        }
        return bitmap;
    }

    @Override
    protected void onPostExecute(Bitmap bitmap) {
        mActivity.get().mActivityMainBinding.partyPictIv.setImageBitmap(bitmap);
    }
}