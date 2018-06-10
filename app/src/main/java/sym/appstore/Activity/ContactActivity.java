package sym.appstore.Activity;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;

import java.util.HashMap;
import java.util.Map;

import sym.appstore.R;
import sym.appstore.helper.Endpoints;

public class ContactActivity extends AppCompatActivity {

    RequestQueue queue;
    EditText eTname, eTemail, eTphone, eTcompany, eTcomment;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_contact);
        queue = Volley.newRequestQueue(this);

        eTname = findViewById(R.id.your_name);
        eTemail = findViewById(R.id.your_email);
        eTphone = findViewById(R.id.your_phone);
        eTcompany = findViewById(R.id.your_company);
        eTcomment = findViewById(R.id.your_message);        
    }

    public void sendMessage(View view) {
        final String name = eTname.getText().toString();
        final String email = eTemail.getText().toString();
        final String phone = eTphone.getText().toString();
        final String company = eTcompany.getText().toString();
        final String comment = eTcomment.getText().toString();
        
        if(name.isEmpty() || email.isEmpty() || phone.isEmpty() || company.isEmpty() || comment.isEmpty()){
            Toast.makeText(this, "সকল ডাটা প্রদান করুন", Toast.LENGTH_SHORT).show();
        }else{
            StringRequest stringRequest = new StringRequest(Request.Method.POST, Endpoints.CONTACTUS_POST_URL, new Response.Listener<String>() {
                @Override
                public void onResponse(String response) {
                    Log.i("ResponseContact", response);
                    if(response.contains("SUCCESS")){
                        Intent intent = new Intent(ContactActivity.this, HomePage.class);
                        startActivity(intent);
                        Toast.makeText(ContactActivity.this, "আপনার তথ্য সাবমিট হয়েছে, ধন্যবাদ!", Toast.LENGTH_SHORT).show();
                    }else{
                        Toast.makeText(ContactActivity.this, "ইন্টারনেটে সমস্যা!!", Toast.LENGTH_SHORT).show();

                    }
                }
            }, new Response.ErrorListener() {
                @Override
                public void onErrorResponse(VolleyError error) {

                }
            }){
                @Override
                public Map<String, String> getParams() throws AuthFailureError {
                    Map<String, String> params = new HashMap<>();
                    params.put("name", name);
                    params.put("email", email);
                    params.put("phoneNumber", phone);
                    params.put("role", company);
                    params.put("comments", comment);
                    return params;
                }
            };
            queue.add(stringRequest);
        }
    }
}
