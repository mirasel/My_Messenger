package com.android.mominul.chat.main;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.os.Handler;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListAdapter;
import android.widget.ListView;
import android.widget.Toast;

import com.android.mominul.chat.R;
import com.loopj.android.http.AsyncHttpClient;
import com.loopj.android.http.AsyncHttpResponseHandler;
import com.loopj.android.http.RequestParams;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

import cz.msebera.android.httpclient.Header;

public class Status extends AppCompatActivity {


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_status);
        Bundle bundle = getIntent().getExtras();
        final String name = bundle.getString("N");
        final String q = "select name from info where status='1'";
        activefriendlist(name,q);



    }

    public void activefriendlist(final String name,final String q){
        AsyncHttpClient c = new AsyncHttpClient();
        RequestParams r = new RequestParams();
        r.put("code", q);
        r.put("currentusr", name);
        c.post("https://michatcom.000webhostapp.com/status.php", r, new AsyncHttpResponseHandler() {
            @Override
            public void onSuccess(int statusCode, Header[] headers, byte[] responseBody) {
                String s = new String(responseBody);
                if (s != null) {
                    ArrayList<String> a = new ArrayList<String>();
                    JSONArray jsonArray = null;
                    try {
                        jsonArray = new JSONArray(s);
                        String s1 = String.valueOf(jsonArray.length());
                        for (int i = 0; i < jsonArray.length(); i++) {

                            JSONObject j = jsonArray.getJSONObject(i);
                            a.add(j.getString("jname"));
                        }

                    } catch (JSONException e) {
                        e.printStackTrace();
                    }


                    ListAdapter listAdapter = new CustomStatusAdapter(Status.this, a);
                    ListView list = (ListView) findViewById(R.id.statuslistview);
                    list.setAdapter(listAdapter);
                    new Handler().postDelayed(new Runnable() {
                        @Override
                        public void run() {
                            activefriendlist(name,q);
                        }
                    }, 1000);
                    list.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                        @Override
                        public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                            String friend = String.valueOf(parent.getItemAtPosition(position));
                            Bundle a = getIntent().getExtras();
                            if (a != null) {
                                String b = a.getString("N");
                                dosomestuff(friend, b);
                            }
                        }
                    });
                } else {
                    Toast.makeText(Status.this, "No active friends!!", Toast.LENGTH_SHORT).show();
                }

            }

            @Override
            public void onFailure(int statusCode, Header[] headers, byte[] responseBody, Throwable error) {
                Toast.makeText(Status.this, "Connection is lost!!", Toast.LENGTH_SHORT).show();
            }
        });
    }

    public void dosomestuff(String friend, String you) {
        Intent intent = new Intent(Status.this, Message.class);
        intent.putExtra("Friend", friend);
        intent.putExtra("You", you);
        startActivity(intent);
    }

    public void logoutclicked(View view) {
        Bundle a = getIntent().getExtras();
        final String b = a.getString("N");
        RequestParams params = new RequestParams();
        params.put("name", b);

        AsyncHttpClient client = new AsyncHttpClient();
        client.post(this, "https://michatcom.000webhostapp.com/logout.php", params, new AsyncHttpResponseHandler() {
            @Override
            public void onSuccess(int statusCode, Header[] headers, byte[] responseBody) {
                String s = new String(responseBody);
                if (s.equals("logout")) {
                    Intent intent = new Intent(Status.this, SignIn.class);
                    startActivity(intent);
                    SharedPreferences settings = getSharedPreferences("userInfo", Context.MODE_PRIVATE);
                    settings.edit().clear().commit();
                } else {
                    Toast.makeText(Status.this, s, Toast.LENGTH_SHORT).show();
                }

            }

            @Override
            public void onFailure(int statusCode, Header[] headers, byte[] responseBody, Throwable error) {
                Toast.makeText(Status.this, "Connection is lost!!", Toast.LENGTH_SHORT).show();
            }
        });
    }
}
