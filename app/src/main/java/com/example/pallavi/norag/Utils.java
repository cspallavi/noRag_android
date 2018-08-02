package com.example.pallavi.norag;

import android.content.Context;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.util.Log;

import java.io.File;
import java.io.IOException;

import okhttp3.Cache;
import okhttp3.CacheControl;
import okhttp3.Interceptor;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;
public class Utils {
    static Context context;
    private static boolean isNetworkAvailable() {
        ConnectivityManager connectivityManager
                = (ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE);;
        NetworkInfo activeNetworkInfo = connectivityManager.getActiveNetworkInfo();
        return activeNetworkInfo != null && activeNetworkInfo.isConnected();
    }
    private static final Interceptor REWRITE_CACHE_CONTROL_INTERCEPTOR = new Interceptor() {
        @Override
        public okhttp3.Response intercept(Chain chain) throws IOException {
            okhttp3.Response originalResponse = chain.proceed(chain.request());
            return originalResponse.newBuilder()
                    .header("Cache-Control", "public, max-age=" + 600)
                    .build();
        }
    };
    private static final Interceptor REWRITE_CACHE_CONTROL_INTERCEPTOR2 = new Interceptor() {
        @Override
        public okhttp3.Response intercept(Chain chain) throws IOException {
            Request request = chain.request();
            if (!isNetworkAvailable()) {
                request = request.newBuilder()
                        .header("Cache-Control",
                                "public, only-if-cached, max-stale=" +  60 * 60 * 24 * 28)
                        .build();
            }
            return chain.proceed(request);
        }
    };

    public static OkHttpClient getClient(Context context) {
        Utils.context=context;
        File file=new File("/sdcard/apiResponses.txt");
        if(!file.exists())
        {
            try {
                file.createNewFile();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        return new OkHttpClient.Builder().addInterceptor(
                REWRITE_CACHE_CONTROL_INTERCEPTOR
        ).addInterceptor(REWRITE_CACHE_CONTROL_INTERCEPTOR2).cache(new Cache(file, 5 * 1024 * 1024)).build();
    }


}
