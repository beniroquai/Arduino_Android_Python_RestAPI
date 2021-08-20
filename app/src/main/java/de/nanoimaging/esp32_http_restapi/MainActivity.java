package de.nanoimaging.esp32_http_restapi;

import androidx.appcompat.app.AppCompatActivity;
import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.MediaType;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;

import android.os.AsyncTask;
import android.os.Bundle;
import android.os.StrictMode;
import android.util.Log;

import com.androidnetworking.AndroidNetworking;
import com.androidnetworking.common.ANRequest;
import com.androidnetworking.common.ANResponse;
import com.androidnetworking.common.Priority;
import com.androidnetworking.error.ANError;
import com.androidnetworking.interfaces.JSONObjectRequestListener;
import com.androidnetworking.interfaces.ParsedRequestListener;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.net.HttpURLConnection;
import java.net.ProtocolException;
import java.net.URL;
import java.util.List;
import java.util.concurrent.Executors;

import static de.nanoimaging.esp32_http_restapi.restapi.rest_post;
import static java.lang.Thread.sleep;

public class MainActivity extends AppCompatActivity {

    String TAG = "RESTAPI";


    OkHttpClient client;
    public static final MediaType JSON = MediaType.parse("application/json; charset=utf-8");

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        // Init HTTP client
        client = new OkHttpClient();


        StrictMode.ThreadPolicy policy = new StrictMode.ThreadPolicy.Builder().permitAll().build();
        StrictMode.setThreadPolicy(policy);

        setContentView(R.layout.activity_main);

        for(int i=0; i<100;i++){
            lens_x(i*100);
        }


        /*
        drive_x(1000, 10);
        drive_y(1000, 10);
        laser(1000);
        lens_x(1000);

         */




    }





    void laser(int laser_intensity){
        /*
        Drive the motor in X at given speed and number of steps
         */
        JSONObject jsonObject = new JSONObject();
        try {
            jsonObject.put("value", laser_intensity);
        } catch (JSONException e) {
            e.printStackTrace();
        }
        post(APIEndPoint.POST_LENS_X, jsonObject);
    }

    void lens_x(int lens_value) {
        /*
        Drive the motor in X at given speed and number of steps
         */
        JSONObject jsonObject = new JSONObject();
        try {
            jsonObject.put("lens_value", lens_value);
        } catch (JSONException e) {
            e.printStackTrace();
        }
        //        post(APIEndPoint.POST_LENS_X, jsonObject);
        try {
            rest_post(APIEndPoint.BASE_URL, APIEndPoint.POST_LENS_X, jsonObject);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    void lens_z(int lens_value){
        /*
        Drive the motor in X at given speed and number of steps
         */
        JSONObject jsonObject = new JSONObject();
        try {
            jsonObject.put("lens_value", lens_value);
        } catch (JSONException e) {
            e.printStackTrace();
        }
        post(APIEndPoint.POST_LENS_Z, jsonObject);
    }

    void drive_x(int steps, int speed){
        /*
        Drive the motor in X at given speed and number of steps
         */
        JSONObject jsonObject = new JSONObject();
        try {
            jsonObject.put("steps", steps);
            jsonObject.put("speed", speed);
        } catch (JSONException e) {
            e.printStackTrace();
        }
        //new post_async(APIEndPoint.POST_MOVE_X, jsonObject).execute();
        post(APIEndPoint.POST_MOVE_X, jsonObject);
        //post(APIEndPoint.POST_MOVE_X, jsonObject);
    }

    void drive_y(int steps, int speed){
        /*
        Drive the motor in X at given speed and number of steps
         */
        JSONObject jsonObject = new JSONObject();
        try {
            jsonObject.put("steps", steps);
            jsonObject.put("speed", speed);
        } catch (JSONException e) {
            e.printStackTrace();
        }
        post(APIEndPoint.POST_MOVE_Y, jsonObject);
    }






    private void post(String endpoint, JSONObject jsonObject) {


        Log.d(TAG, APIEndPoint.BASE_URL+endpoint+" - "+ String.valueOf(jsonObject));
        Request request = new Request.Builder()
                .url(APIEndPoint.BASE_URL+endpoint)
                .post(RequestBody.create(JSON, jsonObject.toString()))
                .build();
        OkHttpClient client = new OkHttpClient();
        client.newCall(request).enqueue(new Callback() {
            @Override
            public void onFailure( Call call,  IOException e) {
                Log.d(TAG, e.getLocalizedMessage());
            }
            @Override
            public void onResponse( Call call,  Response response) throws IOException {
                Log.d(TAG, response.body().string());
            }
        });
    }


    void get(String endpoint){
        AndroidNetworking.post(APIEndPoint.BASE_URL+endpoint)
                .setTag(this)
                .setPriority(Priority.LOW)
                .build()
                .getAsObjectList(User.class, new ParsedRequestListener<List<User>>() {
                    @Override
                    public void onResponse(List<User> users) {
                        // do anything with response
                        Log.e(TAG, "userList size : " + users.size());
                        for (User user : users) {
                            Log.e(TAG, "id : " + user.id);
                            Log.e(TAG, "firstname : " + user.firstname);
                            Log.e(TAG, "lastname : " + user.lastname);
                        }
                    }
                    @Override
                    public void onError(ANError anError) {
                        // handle error
                    }
                });
    }


}