package org.ligi.ajsha;

import android.content.Context;
import android.net.Uri;

import com.squareup.okhttp.OkHttpClient;
import com.squareup.okhttp.Request;
import com.squareup.okhttp.Response;

import java.io.BufferedInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.net.MalformedURLException;
import java.net.URL;

public class InputStreamProvider {

    public final static InputStream fromUri(final Context ctx,final Uri uri) {
        switch (uri.getScheme()) {
            case "content":

                return InputStreamProvider.fromContent(ctx, uri);

            case "http":
            case "https":
                // TODO check if SPDY should be here
                return InputStreamProvider.fromOKHttp(uri);

            default:
                Tracker.get().trackException("unknown scheme in ImportAsyncTask" + uri.getScheme(), false);
            case "file":
                return InputStreamProvider.getDefaultHttpInputStreamForUri(uri);
        }
    }
    public final static InputStream fromOKHttp(final Uri uri) {
        try {
            final OkHttpClient client = new OkHttpClient();
            final URL url = new URL(uri.toString());
            final Request.Builder requestBuilder = new Request.Builder().url(url);

            final Request request = requestBuilder.build();

            final Response response = client.newCall(request).execute();

            return response.body().byteStream();
        } catch (MalformedURLException e) {
            Tracker.get().trackException("MalformedURLException in ImportAsyncTask", e, false);
        } catch (IOException e) {
            Tracker.get().trackException("IOException in ImportAsyncTask", e, false);
        }
        return null;
    }

    public final static InputStream fromContent(final Context ctx, final Uri uri) {
        try {
            return ctx.getContentResolver().openInputStream(uri);
        } catch (FileNotFoundException e) {
            Tracker.get().trackException("FileNotFoundException in passImportActivity/ImportAsyncTask", e, false);
            return null;
        }

    }


    public final static InputStream getDefaultHttpInputStreamForUri(final Uri uri) {
        try {
            return new BufferedInputStream(new URL(uri.toString()).openStream(), 4096);
        } catch (IOException e) {
            Tracker.get().trackException("IOException in passImportActivity/ImportAsyncTask", e, false);
            return null;
        }
    }
}
