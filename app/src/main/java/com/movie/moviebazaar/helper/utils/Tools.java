package com.movie.moviebazaar.helper.utils;

import android.app.Activity;
import android.util.Log;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.shankar.customtoast.Toasty;

import org.json.JSONArray;
import org.json.JSONException;

public class Tools {


    public static void FirebaseTime(Activity act)
    {

    }


    public static void loadDataUsingVolley(Activity act) {
        StringRequest stringRequest = new StringRequest(Request.Method.GET, "http://worldtimeapi.org/api/timezone/America/Argentina/Salta\"",
                response -> {
                    try {


                        JSONArray jsonArray = new JSONArray(response);

                        Log.i("TAGTIME", String.valueOf(jsonArray));


                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                },
                error ->
                {
                    Toasty.errorToast(act, "Error loading user content");
                    Log.i("TAGTIME", "error  : "+ error.getLocalizedMessage());

                });
        RequestQueue requestQueue = Volley.newRequestQueue(act);
        requestQueue.add(stringRequest);
    }

}
