package com.movie.moviebazaar.utils;

import android.app.Activity;
import android.util.Log;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.shankar.customtoast.Toasty;

import org.jetbrains.annotations.NotNull;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.TimeZone;

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
