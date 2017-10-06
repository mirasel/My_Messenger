package com.android.mominul.chat.main;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.EditText;
import android.widget.Toast;

import com.android.mominul.chat.R;
import com.loopj.android.http.AsyncHttpClient;
import com.loopj.android.http.AsyncHttpResponseHandler;
import com.loopj.android.http.RequestParams;

import cz.msebera.android.httpclient.Header;

public class SignIn extends AppCompatActivity {

    EditText usernameinput, passwordinput;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if(getusername()==""&&getpassword()=="") {

            setContentView(R.layout.activity_sign_in);
            initialize();
        }else {
            Intent in = new Intent(SignIn.this, Welcome.class);
            in.putExtra("Name",getName());
            startActivity(in);
        }
    }

    public void signinclicked(View view) {
        final String name = usernameinput.getText().toString().trim();
        final String passd = passwordinput.getText().toString().trim();
        RequestParams params = new RequestParams();
        params.put("uname", name);
        params.put("pass", passd);
        AsyncHttpClient client = new AsyncHttpClient();
        client.post("https://michatcom.000webhostapp.com/login.php", params, new AsyncHttpResponseHandler() {
            @Override
            public void onSuccess(int statusCode, Header[] headers, byte[] responseBody) {
                String result = new String(responseBody);
                if (!result.equals("failed")) {
                    Intent in = new Intent(SignIn.this, Welcome.class);
                    in.putExtra("Name", result);
                    startActivity(in);
                    usernameinput.setText("");
                    passwordinput.setText("");
                    savedinfo(name,passd,result);
                    InputMethodManager inputMethodManager = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
                    inputMethodManager.hideSoftInputFromWindow(passwordinput.getWindowToken(),0);
                } else
                    Toast.makeText(SignIn.this, "Username or Password is incorrect!!", Toast.LENGTH_SHORT).show();
            }

            @Override
            public void onFailure(int statusCode, Header[] headers, byte[] responseBody, Throwable error) {
                Toast.makeText(SignIn.this, "Connection is lost!!", Toast.LENGTH_SHORT).show();

            }
        });
    }

    private void savedinfo(String name,String passd,String result){
        SharedPreferences share = getSharedPreferences("userInfo",Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = share.edit();
        editor.putString("username",name);
        editor.putString("password",passd);
        editor.putString("Name",result);
        editor.apply();
    }
    private String getusername(){
        SharedPreferences share = getSharedPreferences("userInfo",Context.MODE_PRIVATE);
        String uname= share.getString("username","");
        return uname;
    }
    private String getpassword(){
        SharedPreferences share = getSharedPreferences("userInfo",Context.MODE_PRIVATE);
        String pass= share.getString("password","");
        return pass;
    }
    private String getName(){
        SharedPreferences share = getSharedPreferences("userInfo",Context.MODE_PRIVATE);
        String name=share.getString("Name","");
        return name;
    }

    public void signupclicked(View view) {
        Intent intent = new Intent(SignIn.this, SignUP.class);
        startActivity(intent);
    }

    private void initialize() {
        usernameinput = (EditText) findViewById(R.id.usernameinput);
        passwordinput = (EditText) findViewById(R.id.Passwordinput);
    }
}
