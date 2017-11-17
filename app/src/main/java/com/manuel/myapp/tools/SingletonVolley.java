package com.manuel.myapp.tools;

import android.content.Context;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.toolbox.Volley;

/**
 * Created by manuel on 4/10/16.
 */
public class SingletonVolley {

    // Atributos
    private static SingletonVolley singleton;
    private RequestQueue requestQueue;
    private static Context context;

    private SingletonVolley(Context context) {
        SingletonVolley.context = context;
        requestQueue = getRequestQueue();
    }

    public static synchronized SingletonVolley getInstance(Context context) {
        if (singleton == null) {
            singleton = new SingletonVolley(context);
        }
        return singleton;
    }

    public RequestQueue getRequestQueue() {
        if (requestQueue == null) {
            requestQueue = Volley.newRequestQueue(context.getApplicationContext());
        }
        return requestQueue;
    }

    public  void addToRequestQueue(Request req) {
        getRequestQueue().add(req);
    }

}

