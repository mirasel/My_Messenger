package com.android.mominul.chat.main;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

import com.android.mominul.chat.R;
import com.loopj.android.http.AsyncHttpClient;
import com.loopj.android.http.AsyncHttpResponseHandler;
import com.loopj.android.http.RequestParams;

import cz.msebera.android.httpclient.Header;

public class SignUP extends Activity {

    EditText nameinput, usernameinput2, passwordinput2, confirmpasswordinput;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sign_up);
        initialize1();
    }

    public void confirmclicked(View view) {
        String name = nameinput.getText().toString().trim();
        String uname = usernameinput2.getText().toString().trim();
        String pass = passwordinput2.getText().toString().trim();
        String pass2 = confirmpasswordinput.getText().toString().trim();
        if (name.equals("") && uname.equals("")) {
            Toast.makeText(SignUP.this, "Fill the above section properly!!", Toast.LENGTH_LONG).show();
        } else {
            RequestParams params = new RequestParams();
            params.put("name", name);
            params.put("uname", uname);
            params.put("pass", pass);

            if (pass.equals(pass2)) {
                AsyncHttpClient client = new AsyncHttpClient();
                client.post(this, "https://michatcom.000webhostapp.com/signup.php", params, new AsyncHttpResponseHandler() {
                    @Override
                    public void onSuccess(int statusCode, Header[] headers, byte[] responseBody) {
                        String s = new String(responseBody);
                        if (s.equals("inserted")) {
                            Intent in = new Intent(SignUP.this, SignIn.class);
                            startActivity(in);
                        } else {
                            Toast.makeText(SignUP.this, s, Toast.LENGTH_LONG).show();
                        }
                    }

                    @Override
                    public void onFailure(int statusCode, Header[] headers, byte[] responseBody, Throwable error) {
                        Toast.makeText(SignUP.this, "Connection is lost!!", Toast.LENGTH_SHORT).show();
                    }
                });
            } else {
                Toast.makeText(SignUP.this, "Password doesn't match!!", Toast.LENGTH_SHORT).show();
            }
        }


    }

    private void initialize1() {

        nameinput = (EditText) findViewById(R.id.nameinput);
        usernameinput2 = (EditText) findViewById(R.id.usernameinput2);
        passwordinput2 = (EditText) findViewById(R.id.Passwordinput2);
        confirmpasswordinput = (EditText) findViewById(R.id.Confirmpasswordinput);
    }
}
