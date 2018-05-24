package lct.mysymphony.Activity;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.design.widget.BottomNavigationView;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.SnapHelper;
import android.util.Log;
import android.view.Gravity;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageView;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.RetryPolicy;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.github.rubensousa.gravitysnaphelper.GravitySnapHelper;
import com.google.firebase.messaging.FirebaseMessaging;
import com.google.gson.Gson;
import com.viewpagerindicator.CirclePageIndicator;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

import lct.mysymphony.CustomSwipeAdapter;
import lct.mysymphony.ModelClass.GamesZone;
import lct.mysymphony.ModelClass.JapitoJibon;
import lct.mysymphony.ModelClass.MulloChar;
import lct.mysymphony.ModelClass.SeraChobi;
import lct.mysymphony.ModelClass.ShikkhaSohaYika;
import lct.mysymphony.ModelClass.ShocolChobi;
import lct.mysymphony.ModelClass.SliderImage;
import lct.mysymphony.R;
import lct.mysymphony.RecyclerViewAdapter.RecyclerAdapterForGamesZone;
import lct.mysymphony.RecyclerViewAdapter.RecyclerAdapterForJapitoJibon;
import lct.mysymphony.RecyclerViewAdapter.RecyclerAdapterForMulloChar;
import lct.mysymphony.RecyclerViewAdapter.RecyclerAdapterForSeraChobi;
import lct.mysymphony.RecyclerViewAdapter.RecyclerAdapterForShikkhaSohayika;
import lct.mysymphony.RecyclerViewAdapter.RecyclerAdapterForShocolChobi;
import lct.mysymphony.helper.DownloadApk;
import lct.mysymphony.helper.DownloadImage;
import lct.mysymphony.helper.Endpoints;
import paymentgateway.lct.lctpaymentgateway.PaymentMethod;

public class HomePage extends AppCompatActivity implements DownloadApk.AsyncResponse{
    Context context;

    ArrayList<SliderImage> sliderImages;
    ArrayList<SeraChobi> seraChobiArrayList;
    ArrayList<JapitoJibon> japitoJibonArrayList;
    ArrayList<MulloChar> mulloCharArrayList;
    ArrayList<ShocolChobi> shocolChobiArrayList;
    ArrayList<GamesZone> gamesZoneArrayList;
    ArrayList<ShikkhaSohaYika> shikkhaSohaYikaArrayList;
    BottomNavigationView bottomNavigationView;

    private android.support.v7.widget.Toolbar toolbar;

    CustomSwipeAdapter customSwipeAdapter;
    ViewPager viewPager;

    private RecyclerView recyclerViewForGamesZone;
    public RecyclerAdapterForGamesZone adapterForGamesZone;

    private RecyclerView recyclerViewForSeraChobi;
    public RecyclerAdapterForSeraChobi adapterForSeraChobi;

    private RecyclerView recyclerViewForJapitoJibon;
    public RecyclerAdapterForJapitoJibon adapterForJapitoJibon;

    private RecyclerView recyclerViewForMulloChar;
    public RecyclerAdapterForMulloChar adapterForMulloChar;

    private RecyclerView recyclerViewForShikkhaSohayika;
    public RecyclerAdapterForShikkhaSohayika adapterForShikkhaSohayika;

    private RecyclerView recyclerViewForShocolChobi;
    public RecyclerAdapterForShocolChobi adapterForShocolChobi;

    RequestQueue queue;
    ImageView profileIcon, notificationIcon;
    lct.mysymphony.helper.ProgressDialog progressDialog;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home_page);

        FirebaseMessaging.getInstance().subscribeToTopic("All");

        progressDialog = new lct.mysymphony.helper.ProgressDialog(this);
        context = HomePage.this;
        queue = Volley.newRequestQueue(HomePage.this);
        sliderImages = new ArrayList<>();
        seraChobiArrayList = new ArrayList<>();
        toolbar = findViewById(R.id.toolbarlayoutinhome_page);
        setSupportActionBar(toolbar);
        profileIcon = findViewById(R.id.profileIcon);
        notificationIcon = findViewById(R.id.notificationInHomePageToolbar);
        japitoJibonArrayList = new ArrayList<>();
        mulloCharArrayList = new ArrayList<>();
        gamesZoneArrayList = new ArrayList<>();
        shocolChobiArrayList = new ArrayList<>();
        shikkhaSohaYikaArrayList = new ArrayList<>();
        bottomNavigationView = findViewById(R.id.btmNavigation);
        bottomNavigationView.getMenu().findItem(R.id.home_bottom_navigation).setChecked(true);


        String tag = getIntent().getStringExtra("apk");
        if(tag!=null && tag.equals("apk")){
            Log.i("APK", "ON");
            progressDialog.showProgressDialog("APK ডাউনলোড হচ্ছে");
            DownloadApk downloadApk = new DownloadApk();
            downloadApk.downLoadAPK("http://jachaibd.com/files/maxis.apk", HomePage.this);
        }

        progressDialog.showProgressDialog();
        bottomNavigationView.setOnNavigationItemSelectedListener(new BottomNavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem item) {
                int id = item.getItemId();
                if (id == R.id.symphony_bottom_navigation) {
                    Intent symphony = new Intent(HomePage.this, SymphonyCareActivity.class);
                    startActivity(symphony);
                } else if (id == R.id.news_bottom_navigation) {
                    Intent news = new Intent(HomePage.this, GoromKhoborActivity.class);
                    startActivity(news);
                } else if (id == R.id.download_bottom_navigation) {
                    Intent apps = new Intent(HomePage.this, ProfileActivity.class);
                    startActivity(apps);
                }
                return true;
            }
        });

        profileIcon.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent profileIntent = new Intent(HomePage.this, ProfileActivity.class);
                startActivity(profileIntent);
            }
        });
        notificationIcon.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent profileIntent = new Intent(HomePage.this, NotificationActivity.class);
                startActivity(profileIntent);
            }
        });
        //loadDataFromVolley();
        newloadDataFromVolley();
    }

    private void newloadDataFromVolley() {
        StringRequest jsonObjectRequest = new StringRequest(Request.Method.GET, Endpoints.NEW_HOME_GET_URL, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                JSONObject jsonObject = null;
                try {
                    String jsonFormattedString = response.replaceAll("\\\\", "");
                    jsonFormattedString = jsonFormattedString.substring(1, jsonFormattedString.length() - 1);
                    jsonFormattedString = jsonFormattedString.replaceAll("\"\\[", "[");
                    jsonFormattedString = jsonFormattedString.replaceAll("\\]\"", "]");
                    Log.d("jsonFormattedString", jsonFormattedString);
                    jsonObject = new JSONObject(jsonFormattedString);
                    Log.d("obj", jsonObject.toString());
                } catch (JSONException e) {
                    Log.d("JSON Error", e.getMessage());
                }

                try {
                    JSONArray jsonSliderContentArr = jsonObject.getJSONArray("slider_contents");
                    Log.d("jsonSliderContentArr", jsonSliderContentArr.toString());
                    setSliderContent(jsonSliderContentArr);

                    JSONArray top_contentsArr = jsonObject.getJSONArray("top_contents");
                    setTopContent(top_contentsArr);

                    JSONArray japito_jibon_content_Arr = jsonObject.getJSONArray("daily_life_contents");
                    setJapitiJibonContent(japito_jibon_content_Arr);

                    JSONArray mullo_char_content_Arr = jsonObject.getJSONArray("discount_contents");
                    setMulloCharContent(mullo_char_content_Arr);

                    JSONArray shikkha_sohayika__content_Arr = jsonObject.getJSONArray("education_contents");
                    setShikkhaSohayikaContent(shikkha_sohayika__content_Arr);

                    JSONArray games_zone__content_Arr = jsonObject.getJSONArray("game_contents");
                    setGamesZoneContent(games_zone__content_Arr);

                    JSONArray shocol_chobi__content_Arr = jsonObject.getJSONArray("moving_contents");
                    setShocolChobiContent(shocol_chobi__content_Arr);

                    progressDialog.hideProgressDialog();

                } catch (JSONException e) {
                    e.printStackTrace();
                    Log.d("exceptionLoadData4rmvly", e.toString());
                    progressDialog.hideProgressDialog();
                }
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Log.e("Volley", error.toString());
                progressDialog.hideProgressDialog();
                Toast.makeText(getApplicationContext(), "ইন্টারনেট এ সমস্যা পুনরায় চেষ্টা করুন ", Toast.LENGTH_SHORT).show();
            }
        });
        /*jsonObjectRequest.setRetryPolicy(new RetryPolicy() {
            @Override
            public int getCurrentTimeout() {
                return 2000;
            }

            @Override
            public int getCurrentRetryCount() {
                return 2000;
            }

            @Override
            public void retry(VolleyError error) throws VolleyError {

            }
        });*/
        queue.add(jsonObjectRequest);
    }

    private void loadDataFromVolley() {
        JsonObjectRequest jsonObjectRequest = new JsonObjectRequest(Request.Method.GET, Endpoints.NEW_UPDATED_HOME_GET_URL, new Response.Listener<JSONObject>() {
            @Override
            public void onResponse(JSONObject response) {

                try {
                    JSONArray jsonSliderContentArr = response.getJSONArray("slider_contents");
                    setSliderContent(jsonSliderContentArr);

                    JSONArray top_contentsArr = response.getJSONArray("top_contents");
                    setTopContent(top_contentsArr);

                    JSONArray japito_jibon_content_Arr = response.getJSONArray("daily_life_contents");
                    setJapitiJibonContent(japito_jibon_content_Arr);

                    JSONArray mullo_char_content_Arr = response.getJSONArray("discount_contents");
                    setMulloCharContent(mullo_char_content_Arr);

                    JSONArray shikkha_sohayika__content_Arr = response.getJSONArray("education_contents");
                    setShikkhaSohayikaContent(shikkha_sohayika__content_Arr);

                    JSONArray games_zone__content_Arr = response.getJSONArray("game_contents");
                    setGamesZoneContent(games_zone__content_Arr);

                    JSONArray shocol_chobi__content_Arr = response.getJSONArray("moving_contents");
                    setShocolChobiContent(shocol_chobi__content_Arr);
                    progressDialog.hideProgressDialog();

                } catch (JSONException e) {
                    e.printStackTrace();
                    Log.d("exceptionLoadData4rmvly", e.toString());
                    progressDialog.hideProgressDialog();
                }

            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Log.e("Volley", error.toString());
                progressDialog.hideProgressDialog();
                Toast.makeText(getApplicationContext(), "ইন্টারনেট এ সমস্যা পুনরায় চেষ্টা করুন ", Toast.LENGTH_SHORT).show();
            }
        });

        queue.add(jsonObjectRequest);
    }

    private void setShocolChobiContent(JSONArray shocol_chobi__content_arr) {

        if (shocol_chobi__content_arr.length() > 0) {
            for (int i = 0; i < shocol_chobi__content_arr.length(); i++) {
                int contentPrice = 0;
                try {
                    String contentUrl = shocol_chobi__content_arr.getJSONObject(i).getString("contentUrl");
                    String contentTitle = shocol_chobi__content_arr.getJSONObject(i).getString("contentTitle");
                    String contentType = shocol_chobi__content_arr.getJSONObject(i).getString("contentType");
                    int contentId = shocol_chobi__content_arr.getJSONObject(i).getInt("id");
                    String contentCat = shocol_chobi__content_arr.getJSONObject(i).getString("contentCat");
                    String thumbnailImgUrl = shocol_chobi__content_arr.getJSONObject(i).getString("thumbNail_image");
                    if (shocol_chobi__content_arr.getJSONObject(i).has("contentPrice")) {
                        contentPrice = shocol_chobi__content_arr.getJSONObject(i).getInt("contentPrice");
                    }
                    shocolChobiArrayList.add(new ShocolChobi(contentType, contentUrl, contentTitle, contentCat, thumbnailImgUrl, contentId, contentPrice));

                } catch (JSONException e) {
                    e.printStackTrace();
                    Log.d("shocolChobi", e.toString());
                }
            }
            initializeShocolChobiRecyclerview();
        } else {
            Toast.makeText(context, "No data found", Toast.LENGTH_SHORT).show();
        }
    }

    private void setGamesZoneContent(JSONArray games_zone__content_arr) {

        if (games_zone__content_arr.length() > 0) {
            for (int i = 0; i < games_zone__content_arr.length(); i++) {
                int newPrice = 0;
                try {
                    String contentUrl = games_zone__content_arr.getJSONObject(i).getString("contentUrl");
                    String contentTitle = games_zone__content_arr.getJSONObject(i).getString("contentTitle");
                    String contentType = games_zone__content_arr.getJSONObject(i).getString("contentType");
                    String thumbnailImgUrl = games_zone__content_arr.getJSONObject(i).getString("thumbNail_image");
                    ///int previousPrice = games_zone__content_arr.getJSONObject(i).getInt("previousPrice");
                    if (games_zone__content_arr.getJSONObject(i).has("contentPrice")) {
                        newPrice = games_zone__content_arr.getJSONObject(i).getInt("contentPrice");
                    }

                    int contentId = games_zone__content_arr.getJSONObject(i).getInt("id");
                    String contentCat = games_zone__content_arr.getJSONObject(i).getString("contentCat");
                    gamesZoneArrayList.add(new GamesZone(contentType, contentUrl, contentTitle, thumbnailImgUrl, 0, newPrice, contentCat, contentId));

                } catch (JSONException e) {
                    Log.d("exception", e.toString());
                    e.printStackTrace();
                }
            }

            Log.d("size", "size : " + Integer.toString(gamesZoneArrayList.size()));
            initializeGamesZoneRecyclerView();
        } else {
            Toast.makeText(context, "No data found", Toast.LENGTH_SHORT).show();
        }
    }

    private void setShikkhaSohayikaContent(JSONArray shikkha_sohayika__content_arr) {

        if (shikkha_sohayika__content_arr.length() > 0) {
            for (int i = 0; i < shikkha_sohayika__content_arr.length(); i++) {
                int contentPrice = 0;
                try {
                    String contentTitle = shikkha_sohayika__content_arr.getJSONObject(i).getString("contentTitle");
                    String contentType = shikkha_sohayika__content_arr.getJSONObject(i).getString("contentType");
                    String imageUrl = shikkha_sohayika__content_arr.getJSONObject(i).getString("thumbNail_image");
                    String contentDescription = shikkha_sohayika__content_arr.getJSONObject(i).getString("contentDescription");
                    int contentId = shikkha_sohayika__content_arr.getJSONObject(i).getInt("id");
                    String contentCat = shikkha_sohayika__content_arr.getJSONObject(i).getString("contentCat");

                    if (shikkha_sohayika__content_arr.getJSONObject(i).has("contentPrice")) {
                        contentPrice = shikkha_sohayika__content_arr.getJSONObject(i).getInt("contentPrice");
                    }

                    if (contentType.equals("video")) {
                        String contentUrl = shikkha_sohayika__content_arr.getJSONObject(i).getString("contentUrl");
                        shikkhaSohaYikaArrayList.add(new ShikkhaSohaYika(contentType, contentDescription, contentUrl, contentTitle, imageUrl, contentCat, contentId, contentPrice));
                    } else {
                        shikkhaSohaYikaArrayList.add(new ShikkhaSohaYika(contentType, contentDescription, "", contentTitle, imageUrl, contentCat, contentId, contentPrice));
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
            initializeShikkhaSohayikaRecyclerview();
        } else {
            Toast.makeText(context, "No data found", Toast.LENGTH_SHORT).show();
        }
    }

    private void setMulloCharContent(JSONArray mulloCharJsonArr) {

        if (mulloCharJsonArr.length() > 0) {

            int newPrice = 0;
            for (int i = 0; i < mulloCharJsonArr.length(); i++) {
                try {
                    String contentUrl = mulloCharJsonArr.getJSONObject(i).getString("contentUrl");
                    String contentTitle = mulloCharJsonArr.getJSONObject(i).getString("contentTitle");
                    String contentType = mulloCharJsonArr.getJSONObject(i).getString("contentType");
                    String image_url = mulloCharJsonArr.getJSONObject(i).getString("thumbNail_image");
                    int previousPrice = mulloCharJsonArr.getJSONObject(i).getInt("contentPrice");
                    int contentId = mulloCharJsonArr.getJSONObject(i).getInt("id");
                    String contentCat = mulloCharJsonArr.getJSONObject(i).getString("contentCat");
                    String thumbNail_image = mulloCharJsonArr.getJSONObject(i).getString("thumbNail_image");

                    JSONArray jsonArray = mulloCharJsonArr.getJSONObject(i).getJSONArray("discount");

                    for (int j = 0; j < jsonArray.length(); j++) {
                        newPrice = jsonArray.getJSONObject(j).getInt("discountPrice");
                    }
                    mulloCharArrayList.add(new MulloChar(contentType, contentUrl, contentTitle, thumbNail_image, previousPrice, newPrice, image_url, contentCat, contentId));
                } catch (JSONException e) {
                    Log.d("mullochardata", e.toString());
                    e.printStackTrace();
                }
            }
            initializeMulloCharRecyclerView();
        } else {
            Toast.makeText(context, "No data found", Toast.LENGTH_SHORT).show();
        }
    }

    private void setJapitiJibonContent(JSONArray japito_jibon_content_arr) {
        if (japito_jibon_content_arr.length() > 0) {
            for (int i = 0; i < japito_jibon_content_arr.length(); i++) {
                int contentPrice = 0;
                try {
                    String contentTitle = japito_jibon_content_arr.getJSONObject(i).getString("contentTitle");
                    String contentType = japito_jibon_content_arr.getJSONObject(i).getString("contentType");
                    String contentDescription = japito_jibon_content_arr.getJSONObject(i).getString("contentDescription");
                    String contentUrl = japito_jibon_content_arr.getJSONObject(i).getString("contentUrl");
                    String contentCat = japito_jibon_content_arr.getJSONObject(i).getString("contentCat");
                    ///String thumbNail_image = japito_jibon_content_arr.getJSONObject(i).getString("thumbNail_image");
                    int contentid = japito_jibon_content_arr.getJSONObject(i).getInt("id");
                    if (japito_jibon_content_arr.getJSONObject(i).has("contentPrice")) {
                        contentPrice = japito_jibon_content_arr.getJSONObject(i).getInt("contentPrice");
                    }


                    if (contentType.equals("video")) {
                        Log.i("Data", "Video");
                        japitoJibonArrayList.add(new JapitoJibon(contentTitle, contentDescription, japito_jibon_content_arr.getJSONObject(i).getString("thumbNail_image"), "video", contentUrl,japito_jibon_content_arr.getJSONObject(i).getString("thumbNail_image"), contentCat,  contentid, contentPrice));
                    } else {
                        Log.i("Data", "Image");
                        japitoJibonArrayList.add(new JapitoJibon(contentTitle, contentDescription, japito_jibon_content_arr.getJSONObject(i).getString("contentUrl"), "image", contentUrl, contentCat, "", contentid, contentPrice));
                    }
                } catch (JSONException e) {
                    Log.d("japito_jibon_exception", e.toString());
                    e.printStackTrace();
                }
            }
            initializeJapitoJibonRecyclerView();
        } else {
            Toast.makeText(context, "No data found", Toast.LENGTH_SHORT).show();
        }
    }

    private void setTopContent(JSONArray top_contentsArr) {
        if (top_contentsArr.length() > 0) {
            for (int i = 0; i < top_contentsArr.length(); i++) {
                int contentPrice = 0;
                try {
                    String contentUrl = top_contentsArr.getJSONObject(i).getString("contentUrl");
                    String contentTitle = top_contentsArr.getJSONObject(i).getString("contentTitle");
                    String contentType = top_contentsArr.getJSONObject(i).getString("contentType");
                    String contentCat = top_contentsArr.getJSONObject(i).getString("contentCat");
                    String thumbNail_image = top_contentsArr.getJSONObject(i).getString("thumbNail_image");
                    int contentId = top_contentsArr.getJSONObject(i).getInt("id");
                    String contentDescription = top_contentsArr.getJSONObject(i).getString("contentDescription");
                    if (top_contentsArr.getJSONObject(i).has("contentPrice")) {
                        contentPrice = top_contentsArr.getJSONObject(i).getInt("contentPrice");
                    }
                    seraChobiArrayList.add(new SeraChobi(contentUrl, "", contentType, contentTitle, thumbNail_image, contentCat, contentId, contentDescription, contentPrice));
                } catch (JSONException e) {
                    e.printStackTrace();
                    Log.d("tpCntntExcptn", e.toString());
                }
            }
            initializeTopContentRecyclerView();
        } else {
            Toast.makeText(context, "No data found", Toast.LENGTH_SHORT).show();
        }
    }

    private void setSliderContent(JSONArray jsonSliderContentArr) {
        if (jsonSliderContentArr.length() > 0) {
            int contentPrice = 0;
            for (int i = 0; i < jsonSliderContentArr.length(); i++) {
                try {
                    String contentUrl = jsonSliderContentArr.getJSONObject(i).getString("contentUrl");
                    String description = jsonSliderContentArr.getJSONObject(i).getString("contentDescription");
                    String contentTitle = jsonSliderContentArr.getJSONObject(i).getString("contentTitle");
                    String contentType = jsonSliderContentArr.getJSONObject(i).getString("contentType");
                    String contentCat = jsonSliderContentArr.getJSONObject(i).getString("contentCat");
                    int contentId = jsonSliderContentArr.getJSONObject(i).getInt("id");
                    String contentDescription = jsonSliderContentArr.getJSONObject(i).getString("contentDescription");
                    String thumbNailImg = jsonSliderContentArr.getJSONObject(i).getString("thumbNail_image");
                    if (jsonSliderContentArr.getJSONObject(i).has("contentPrice")) {
                        contentPrice = jsonSliderContentArr.getJSONObject(i).getInt("contentPrice");
                    }
                    sliderImages.add(new SliderImage(contentUrl, description, contentType, contentTitle, contentCat, contentId, contentDescription, thumbNailImg, contentPrice));
                } catch (JSONException e) {
                    e.printStackTrace();
                    Log.d("SldrCntntExcptn", e.toString());
                }
            }
            initialCustomSwipeAdapter();
        } else {
            Toast.makeText(context, "No data found", Toast.LENGTH_SHORT).show();
        }
    }

    public void startLoginActivity(View view) {
        Intent myIntent = new Intent(getApplicationContext(), MainActivity.class);
        this.startActivity(myIntent);
        finish();
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        Intent myIntent = new Intent(getApplicationContext(), MainActivity.class);
        myIntent.putExtra("cameFromWhichActivity", "HomePage");
        this.startActivity(myIntent);
    }

    public void startSportActivity(View view) {
        Intent myIntent = new Intent(getApplicationContext(), KheladhulaActivity.class);

        this.startActivity(myIntent);
    }

    public void startPorashunaActivity(View view) {
        Intent myIntent = new Intent(getApplicationContext(), PorashunaActivity.class);
        this.startActivity(myIntent);
    }

    public void startAuttoHashiActivity(View view) {
        Intent myIntent = new Intent(getApplicationContext(), AuttoHashiActivity.class);
        this.startActivity(myIntent);
    }

    public void startJibonJaponActivity(View view) {
        Intent myIntent = new Intent(getApplicationContext(), JibonJaponActivity.class);
        this.startActivity(myIntent);
    }

    public void startPachMishaliActivity(View view) {
        Intent myIntent = new Intent(getApplicationContext(), PachMishaliActivity.class);
        this.startActivity(myIntent);
    }

    public void startBigganOProjuktiActivity(View view) {
        Intent myIntent = new Intent(getApplicationContext(), BigganOProjuktiActivity.class);
        this.startActivity(myIntent);
    }

    public void startCartoonActivity(View view) {
        Intent myIntent = new Intent(getApplicationContext(), CartoonActivity.class);
        this.startActivity(myIntent);
    }

    public void initialCustomSwipeAdapter() {
        viewPager = findViewById(R.id.viewPager);
        customSwipeAdapter = new CustomSwipeAdapter(this, sliderImages);
        viewPager.setAdapter(customSwipeAdapter);
        CirclePageIndicator indicator = findViewById(R.id.indicator);
        indicator.setViewPager(viewPager);
        final float density = getResources().getDisplayMetrics().density;
        indicator.setRadius(5 * density);
    }

    public void initializeTopContentRecyclerView() {
        recyclerViewForSeraChobi = findViewById(R.id.RV_SeraChobi);
        recyclerViewForSeraChobi.setLayoutManager(new LinearLayoutManager(this, LinearLayoutManager.HORIZONTAL, false));
        SnapHelper snapHelperStart = new GravitySnapHelper(Gravity.START);
        snapHelperStart.attachToRecyclerView(recyclerViewForSeraChobi);
        recyclerViewForSeraChobi.setHasFixedSize(true);
        adapterForSeraChobi = new RecyclerAdapterForSeraChobi(this, seraChobiArrayList);
        recyclerViewForSeraChobi.setAdapter(adapterForSeraChobi);
    }

    public void initializeJapitoJibonRecyclerView() {
        recyclerViewForJapitoJibon = findViewById(R.id.RV_japitoJibon);
        recyclerViewForJapitoJibon.setLayoutManager(new LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false));
        SnapHelper snapHelperStartJapitoJibon = new GravitySnapHelper(Gravity.START);
        snapHelperStartJapitoJibon.attachToRecyclerView(recyclerViewForJapitoJibon);
        recyclerViewForJapitoJibon.setHasFixedSize(true);
        adapterForJapitoJibon = new RecyclerAdapterForJapitoJibon(this, japitoJibonArrayList);
        recyclerViewForJapitoJibon.setAdapter(adapterForJapitoJibon);
    }


    public void initializeMulloCharRecyclerView() {
        recyclerViewForMulloChar = findViewById(R.id.RV_mulloChar);
        recyclerViewForMulloChar.setLayoutManager(new LinearLayoutManager(this, LinearLayoutManager.HORIZONTAL, false));
        SnapHelper snapHelperStartMulloChar = new GravitySnapHelper(Gravity.START);
        snapHelperStartMulloChar.attachToRecyclerView(recyclerViewForMulloChar);
        recyclerViewForMulloChar.setHasFixedSize(true);
        adapterForMulloChar = new RecyclerAdapterForMulloChar(this, mulloCharArrayList);
        recyclerViewForMulloChar.setAdapter(adapterForMulloChar);
    }

    public void initializeGamesZoneRecyclerView() {
        recyclerViewForGamesZone = findViewById(R.id.RV_gamesZone);
        recyclerViewForGamesZone.setLayoutManager(new LinearLayoutManager(this, LinearLayoutManager.HORIZONTAL, false));
        SnapHelper snapHelperStartMulloChar = new GravitySnapHelper(Gravity.START);
        snapHelperStartMulloChar.attachToRecyclerView(recyclerViewForGamesZone);
        recyclerViewForGamesZone.setHasFixedSize(true);
        adapterForGamesZone = new RecyclerAdapterForGamesZone(this, gamesZoneArrayList);
        recyclerViewForGamesZone.setAdapter(adapterForGamesZone);
    }

    public void initializeShocolChobiRecyclerview() {
        recyclerViewForShocolChobi = findViewById(R.id.RV_ShocolChobi);
        recyclerViewForShocolChobi.setLayoutManager(new LinearLayoutManager(this, LinearLayoutManager.HORIZONTAL, false));
        SnapHelper snapHelperStartShocolChobi = new GravitySnapHelper(Gravity.START);
        snapHelperStartShocolChobi.attachToRecyclerView(recyclerViewForShocolChobi);
        recyclerViewForShocolChobi.setHasFixedSize(true);
        adapterForShocolChobi = new RecyclerAdapterForShocolChobi(this, shocolChobiArrayList);
        recyclerViewForShocolChobi.setAdapter(adapterForShocolChobi);
    }

    public void initializeShikkhaSohayikaRecyclerview() {
        recyclerViewForShikkhaSohayika = findViewById(R.id.RV_ShikkhaSohayika);
        recyclerViewForShikkhaSohayika.setLayoutManager(new LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false));
        SnapHelper snapHelperStartShikkhaSohayika = new GravitySnapHelper(Gravity.START);
        snapHelperStartShikkhaSohayika.attachToRecyclerView(recyclerViewForShikkhaSohayika);
        recyclerViewForShikkhaSohayika.setHasFixedSize(true);
        adapterForShikkhaSohayika = new RecyclerAdapterForShikkhaSohayika(this, shikkhaSohaYikaArrayList);
        recyclerViewForShikkhaSohayika.setAdapter(adapterForShikkhaSohayika);
    }

    public void startGoromKhoborPage(View view) {
        Intent myIntent = new Intent(getApplicationContext(), GoromKhoborActivity.class);
        this.startActivity(myIntent);
    }

    public void startGalleryActivity(View view) {
        Intent galleryIntent = new Intent(getApplicationContext(), GalleryActivity.class);
        galleryIntent.putExtra("galleryImageData", seraChobiArrayList);
        this.startActivity(galleryIntent);
    }

    public void subscribe(View view) {
        Intent intentDailyLife = new Intent(HomePage.this, PaymentMethod.class);
        startActivity(intentDailyLife);
    }

    public void moreJapitoJibon(View view) {
        Intent intentDailyLife = new Intent(HomePage.this, JibonJaponActivity.class);
        startActivity(intentDailyLife);
    }

    public void moreShikha(View view) {
        Intent intentDailyLife = new Intent(HomePage.this, PorashunaActivity.class);
        startActivity(intentDailyLife);
    }

    public void jumpToHandsetFeature(View view) {
        Intent intent = new Intent(HomePage.this, SymphonyCareActivity.class);
        intent.putExtra("position", 0);
        startActivity(intent);
    }

    public void jumpToCustomerCare(View view) {
        Intent intent = new Intent(HomePage.this, SymphonyCareActivity.class);
        intent.putExtra("position", 1);
        startActivity(intent);
    }

    public void jumpToContact(View view) {
        Intent intent = new Intent(HomePage.this, SymphonyCareActivity.class);
        intent.putExtra("position", 2);
        startActivity(intent);
    }
    @Override
    public void onPause() {
        super.onPause();
        if (progressDialog!=null)
            progressDialog.hideProgressDialog();
    }

    @Override
    public void processFinish(String output) {
            progressDialog.hideProgressDialog();
            if (output.contains("complete")) {
                Log.d("onProcessFinishedComplt", "onProcessFinishedComplt");
            }
    }

}
