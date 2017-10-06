package com.android.mominul.chat.main;

import android.content.Context;
import android.os.Bundle;
import android.os.Handler;
import android.support.v7.app.AppCompatActivity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.InputMethodManager;
import android.widget.BaseAdapter;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.android.mominul.chat.R;
import com.loopj.android.http.AsyncHttpClient;
import com.loopj.android.http.AsyncHttpResponseHandler;
import com.loopj.android.http.RequestParams;

import org.json.JSONArray;
import org.json.JSONObject;

import java.util.ArrayList;

import cz.msebera.android.httpclient.Header;

public class Message extends AppCompatActivity {
    private static final String TAG = "SHOWED";


    TextView friendname;
    ListView messagelistview;
    ArrayList<CustomMessage> arrayList;
    BaseAdapter adapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_message);
        Bundle bundle = getIntent().getExtras();
        if (bundle != null) {
            final String b = bundle.getString("You");
            final String r = bundle.getString("Friend");
            friendname = (TextView) findViewById(R.id.friendname);
            friendname.setText(r);
            getMsg(b, r);
        }
        arrayList = new ArrayList<CustomMessage>();
    }

// For listview -------------------------


    private void getMsg(final String b, final String r) {
        RequestParams params = new RequestParams();
        params.put("sender", b);
        params.put("receiver", r);
        AsyncHttpClient client = new AsyncHttpClient();
        client.post(Message.this, "https://michatcom.000webhostapp.com/showmsg.php", params, new AsyncHttpResponseHandler() {
            @Override
            public void onSuccess(int statusCode, Header[] headers, byte[] responseBody) {
                String s = new String(responseBody);
                if (s != null) {
                    JSONArray jsonArray = null;
                    String name = null, message = null;
                    try {
                        arrayList.clear();
                        jsonArray = new JSONArray(s);
                        for (int i = 0; i < jsonArray.length(); i++) {

                            JSONObject j = jsonArray.getJSONObject(i);
                            name = j.getString("Name");
                            message = j.getString("Msg");
                            CustomMessage customMessage = new CustomMessage(name, message);
                            arrayList.add(customMessage);
                        }
                        printlist();
                        new Handler().postDelayed(new Runnable() {
                            @Override
                            public void run() {
                                getMsg(b, r);
                            }
                        }, 1000);
                    } catch (Exception e) {
                        e.printStackTrace();
                    }

                }
            }

            @Override
            public void onFailure(int statusCode, Header[] headers, byte[] responseBody, Throwable error) {
                Toast.makeText(Message.this, "Connection is lost!!", Toast.LENGTH_SHORT).show();
            }
        });
    }

    public void printlist() {
        messagelistview = (ListView) findViewById(R.id.messagelistview);
        adapter = new BaseAdapter() {
            LayoutInflater inflater = (LayoutInflater) getSystemService(Context.LAYOUT_INFLATER_SERVICE);

            @Override
            public int getCount() {
                return arrayList.size();
            }

            @Override
            public Object getItem(int position) {
                return arrayList.get(position);
            }

            @Override
            public long getItemId(int position) {
                return 0;
            }

            @Override
            public View getView(int position, View customview, ViewGroup parent) {
                if (customview == null) {
                    customview = inflater.inflate(R.layout.custommessage, null);
                }
                TextView sendername = (TextView) customview.findViewById(R.id.sendername);
                TextView message = (TextView) customview.findViewById(R.id.message);

                sendername.setText(arrayList.get(position).getName());
                message.setText(arrayList.get(position).getMessage());
                return customview;
            }
        };
        messagelistview.setAdapter(adapter);

    }
    // After send button clicked

    public void sendclicked(View view) {
        final EditText messageedittext;
        messageedittext = (EditText) findViewById(R.id.messageeditText);
        String m = messageedittext.getText().toString().trim();
        Bundle bundle = getIntent().getExtras();
        if (bundle != null) {
            final String r = bundle.getString("Friend");
            final String s = bundle.getString("You");
            RequestParams params = new RequestParams();
            params.put("sender", s);
            params.put("receiver", r);
            params.put("message", m);
            AsyncHttpClient client = new AsyncHttpClient();
            client.post(this, "https://michatcom.000webhostapp.com/addmsg.php", params, new AsyncHttpResponseHandler() {
                @Override
                public void onSuccess(int statusCode, Header[] headers, byte[] responseBody) {
                    String result = new String(responseBody);
                    if (!result.equals("failed")) {
                        messageedittext.setText("");
                        InputMethodManager inputMethodManager = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
                        inputMethodManager.hideSoftInputFromWindow(messageedittext.getWindowToken(), 0);

                    } else {
                        Toast.makeText(Message.this, result, Toast.LENGTH_SHORT).show();
                    }
                }

                @Override
                public void onFailure(int statusCode, Header[] headers, byte[] responseBody, Throwable error) {
                    Toast.makeText(Message.this, "Connection is lost!!", Toast.LENGTH_SHORT).show();
                }
            });
        }
    }

//    public void shareclicked(View view){
//        Intent intent= new Intent(Message.this, FileChooserActivity.class);
//        startActivity(intent);
//    }
}


