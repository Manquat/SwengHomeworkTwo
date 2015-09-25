/**
 * Created by Gautier on 25/09/2015.
 */
package ch.epfl.sweng.quizapp;

import android.content.Context;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.widget.TextView;

import java.io.IOException;
import java.net.HttpURLConnection;
import java.net.URL;

/**
 * A default implementation of the {@link NetworkProvider} interface that uses
 * the mechanism available in the {@link URL} object to create
 * {@link HttpURLConnection} objects.
 *
 */
public class DefaultNetworkProvider implements NetworkProvider {

    /**
     * The default constructor.
     */
    public DefaultNetworkProvider() {
    }

    @Override
    public HttpURLConnection getConnection(URL url) throws IOException {

        /*ConnectivityManager connMgr = (ConnectivityManager) getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo networkInfo = connMgr.getActiveNetworkInfo();

        if (networkInfo != null && networkInfo.isConnected()){
            HttpURLConnection urlConnection = (HttpURLConnection) url.openConnection();

        } else {
            throw new IOException("No internet connection");
        }

        return urlConnection;*/
        return (HttpURLConnection) url.openConnection();
    }

    // TODO
}