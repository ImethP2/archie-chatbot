package com.example.myapplication;

import android.annotation.SuppressLint;
import android.os.Bundle;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.ToggleButton;


import androidx.appcompat.app.AppCompatActivity;
import androidx.constraintlayout.widget.ConstraintLayout;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.RetryPolicy;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;

public class MainActivity extends AppCompatActivity {
    TextView show_res;
    EditText editTxt;
    ImageButton sendbtn;
    Button back;
    ConstraintLayout front;
    ToggleButton LC;
    Button start;
    //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

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        show_res = findViewById(R.id.result);
         editTxt = findViewById(R.id.txt_edit);
         sendbtn = findViewById(R.id.send_btn);
         back = findViewById(R.id.back_btn);
         front = findViewById(R.id.first_page);
         LC = findViewById(R.id.LC_btn);
         start = findViewById(R.id.go);

         double LorC = 0;
         if (LC.getText() == "Creative Mode" ){
             LorC=2;
         }else{
             LorC=0;
         }
         start.setOnClickListener(new View.OnClickListener() {
             @Override
             public void onClick(View v) {
                 front.setVisibility(View.GONE);
             }
         });
        double finalLorC = LorC;
        sendbtn.setOnClickListener(new View.OnClickListener() {
             @Override
             public void onClick(View v) {
                 action(editTxt.getText().toString(), finalLorC);
                editTxt.setText("");
             }
         });
        back.setOnClickListener(new View.OnClickListener() {
             @Override
             public void onClick(View v) {
                front.setVisibility(View.VISIBLE);
                InputMethodManager inputMethodManager = (InputMethodManager) getSystemService(INPUT_METHOD_SERVICE);
                inputMethodManager.hideSoftInputFromWindow(v.getApplicationWindowToken(), 0);
                show_res.setText("");
            }
         });
    }
    public void action(String txtinput, double LorC){
        // Instantiate the RequestQueue.
        RequestQueue queue = Volley.newRequestQueue(this);
        String url = "https://api.openai.com/v1/completions";

        JSONObject  jsonObject = new JSONObject();
        try {
            jsonObject.put("prompt",txtinput);
            //jsonObject.put("suffix","\n---Echo---");
            jsonObject.put("model","text-davinci-003");
            jsonObject.put("max_tokens",200);
            jsonObject.put("temperature",LorC);
    // Request a string response from the provided URL.
            JsonObjectRequest jsonObjectRequest = new JsonObjectRequest(Request.Method.POST, url, jsonObject,
                    new Response.Listener<JSONObject>() {
                        @Override
                        public void onResponse(JSONObject response) {
                            String answer = null;
                            try {
                                answer = response.getJSONArray("choices").getJSONObject(0).getString("text");
                            } catch (JSONException e) {
                                throw new RuntimeException(e);
                            }
                            // Display the first 500 characters of the response string.
                            show_res.setText(answer);
                        }
                    }, new Response.ErrorListener() {
                @SuppressLint("SetTextI18n")
                @Override
                public void onErrorResponse(VolleyError error) {
                    show_res.setText("That did not work");
                }
            }){
                @Override
                public Map<String, String> getHeaders() throws AuthFailureError {
                    HashMap<String, String> map = new HashMap<>();
                    map.put("Content-Type", "application/json");
                    map.put("Authorization", "Bearer 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");
                    return map;
                }
            };
            jsonObjectRequest.setRetryPolicy(new RetryPolicy() {
                                                 @Override
                                                 public int getCurrentTimeout() {
                                                     return 15000;
                                                 }

                                                 @Override
                                                 public int getCurrentRetryCount() {
                                                     return 5;
                                                 }

                                                 @Override
                                                 public void retry(VolleyError error) throws VolleyError {

                                                 }
                                             });
                    // Add the request to the RequestQueue.
                    queue.add(jsonObjectRequest);
        } catch (JSONException e) {
            throw new RuntimeException(e);
        }

    }
}