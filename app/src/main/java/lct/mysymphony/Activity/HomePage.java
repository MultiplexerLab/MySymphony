package lct.mysymphony.Activity;

import android.Manifest;
import android.accounts.Account;
import android.accounts.AccountManager;
import android.app.Dialog;
import android.app.LoaderManager;
import android.bluetooth.BluetoothAdapter;
import android.content.Context;
import android.content.CursorLoader;
import android.content.Intent;
import android.content.Loader;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.drawable.BitmapDrawable;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Bundle;
import android.provider.ContactsContract;
import android.provider.Settings;
import android.support.annotation.NonNull;
import android.support.design.widget.AppBarLayout;
import android.support.design.widget.BottomNavigationView;
import android.support.v4.app.ActivityCompat;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.SnapHelper;
import android.telephony.TelephonyManager;
import android.util.Log;
import android.util.Patterns;
import android.view.Gravity;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.RetryPolicy;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonArrayRequest;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.bumptech.glide.Glide;
import com.bumptech.glide.request.target.SimpleTarget;
import com.github.rubensousa.gravitysnaphelper.GravitySnapHelper;
import com.google.firebase.messaging.FirebaseMessaging;
import com.google.gson.Gson;
import com.google.gson.JsonArray;
import com.google.gson.reflect.TypeToken;
import com.viewpagerindicator.CirclePageIndicator;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.lang.reflect.Type;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.LinkedList;
import java.util.List;
import java.util.regex.Pattern;

import lct.mysymphony.CustomSwipeAdapter;
import lct.mysymphony.ModelClass.AppData;
import lct.mysymphony.ModelClass.GamesZone;
import lct.mysymphony.ModelClass.Icon;
import lct.mysymphony.ModelClass.JapitoJibon;
import lct.mysymphony.ModelClass.MulloChar;
import lct.mysymphony.ModelClass.Porashuna;
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
import lct.mysymphony.helper.AppLogger;
import lct.mysymphony.helper.DownloadApk;
import lct.mysymphony.helper.DownloadImage;
import lct.mysymphony.helper.Endpoints;
import paymentgateway.lct.lctpaymentgateway.PaymentMethod;

public class HomePage extends AppCompatActivity implements DownloadApk.AsyncResponse {
    Context context;
    Date currenTime;
    DateFormat dateFormat;

    ArrayList<SliderImage> sliderImages;
    ArrayList<SeraChobi> seraChobiArrayList;
    ArrayList<Porashuna> japitoJibonArrayList, musicVideoList, hotNewsArrayList, sportsArralyList, auttoHashiArrList, mixedArrList, scienceArrList, kidsArrList;
    ArrayList<MulloChar> mulloCharArrayList;
    ArrayList<AppData> appList;
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

    ArrayList<Icon> iconImageUrls;

    RequestQueue queue;
    ImageView profileIcon, notificationIcon;
    lct.mysymphony.helper.ProgressDialog progressDialog;
    private Menu menu;

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
        ImageView imageLogo = toolbar.findViewById(R.id.imageLogo);
        Glide.with(this).load("http://bot.sharedtoday.com:9500/images/logo/Symphony%20App%20Store%20Logo%20-%20v4.png").into(imageLogo);
        setSupportActionBar(toolbar);
        profileIcon = findViewById(R.id.profileIcon);
        notificationIcon = findViewById(R.id.notificationInHomePageToolbar);
        japitoJibonArrayList = new ArrayList<>();
        mulloCharArrayList = new ArrayList<>();
        gamesZoneArrayList = new ArrayList<>();
        shocolChobiArrayList = new ArrayList<>();
        shikkhaSohaYikaArrayList = new ArrayList<>();
        hotNewsArrayList = new ArrayList<>();
        sportsArralyList = new ArrayList<>();
        auttoHashiArrList = new ArrayList<>();
        mixedArrList = new ArrayList<>();
        scienceArrList = new ArrayList<>();
        kidsArrList = new ArrayList<>();
        appList = new ArrayList<>();
        musicVideoList = new ArrayList<>();

        bottomNavigationView = findViewById(R.id.btmNavigation);
        bottomNavigationView.getMenu().findItem(R.id.home_bottom_navigation).setChecked(true);
        bottomNavigationView.getMenu().getItem(0).setIcon(R.drawable.bottom1);
        bottomNavigationView.getMenu().getItem(1).setIcon(R.drawable.bottom2);
        bottomNavigationView.getMenu().getItem(2).setIcon(R.drawable.bottom3);
        bottomNavigationView.getMenu().getItem(3).setIcon(R.drawable.bottom4);

        String apkUrl = getIntent().getStringExtra("apk");
        if (apkUrl != null) {
            SharedPreferences preferences = context.getSharedPreferences("tempData", context.MODE_PRIVATE);
            int flag = preferences.getInt("unknownSource", 0);
            if (flag == 0) {
                progressDialog.showProgressDialogAPK();
                DownloadApk downloadApk = new DownloadApk();
                downloadApk.downLoadAPK(apkUrl, HomePage.this);
            } else {
                progressDialog.showProgressDialog("App ডাউনলোড হচ্ছে");
                DownloadApk downloadApk = new DownloadApk();
                downloadApk.downLoadAPK(apkUrl, HomePage.this);
            }
        }

        progressDialog.showProgressDialog();
        bottomNavigationView.setOnNavigationItemSelectedListener(new BottomNavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem item) {
                int id = item.getItemId();
                if (id == R.id.about_bottom_navigation) {
                    Intent symphony = new Intent(HomePage.this, HomePage.class);
                    startActivity(symphony);
                } else if (id == R.id.contact_bottom_navigation) {
                    Intent news = new Intent(HomePage.this, HomePage.class);
                    startActivity(news);
                } else if (id == R.id.terms_bottom_navigation) {
                    Intent apps = new Intent(HomePage.this, HomePage.class);
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
        loadIconsFromServer();
        newloadDataFromVolley();
    }

    private void setIcons() {
        Integer[] imageViews = {R.id.mainButton1, R.id.mainButton2, R.id.mainButton3, R.id.imageView1, R.id.imageView2, R.id.imageView3, R.id.imageView4, R.id.imageView5, R.id.imageView6, R.id.imageView7, R.id.imageView8};
        Integer[] textViews = {R.id.textViewMain1, R.id.textViewMain2, R.id.textViewMain3, R.id.textView1, R.id.textView2, R.id.textView3, R.id.textView4, R.id.textView5, R.id.textView6, R.id.textView7, R.id.textView8};

        for (int i = 0; i < iconImageUrls.size(); i++) {
            Glide.with(HomePage.this).load(iconImageUrls.get(i).getImage()).into((ImageView) findViewById(imageViews[i]));
            TextView temp = findViewById(textViews[i]);
            temp.setText(iconImageUrls.get(i).getCategoryTitle());

            /*if (iconImageUrls.get(i).getType().equals("topic")) {
                Log.i("LinkImages", iconImageUrls.get(i).getImage());

            }*/
        }
    }

    @Override
    protected void onResume(){
        super.onResume();
        dateFormat = new SimpleDateFormat("MM/dd/yyyy HH:mm:ss");
        currenTime = new Date();
        AppLogger.insertLogs(HomePage.this, dateFormat.format(currenTime), "N", "HomePage",
                "IN", "Entrance");
    }
    @Override
    protected void onStop(){
        super.onStop();
        AppLogger.insertLogs(HomePage.this, dateFormat.format(currenTime), "Y", "HomePage",
                "OUT", "Leave");
    }

  /*  public boolean onCreateOptionsMenu(Menu menu) {
        this.menu = menu;
        getMenuInflater().inflate(R.menu.bottom_navigation_menu, menu);
        return true;
    }

    @Override
    public boolean onPrepareOptionsMenu(Menu menu) {
        final MenuItem settingsItem = menu.findItem(R.id.home_bottom_navigation);
        //Glide.with(HomePage.this).load(iconImageUrls.get(11).getImage()).into((settingsItem.getIcon())));

        return super.onPrepareOptionsMenu(menu);
    }*/


    private void loadIconsFromServer() {
        iconImageUrls = new ArrayList<Icon>();
        JsonArrayRequest jsonObjectRequest = new JsonArrayRequest(Request.Method.GET, Endpoints.ICON_GET_URL, new Response.Listener<JSONArray>() {
            @Override
            public void onResponse(JSONArray response) {

                try {
                    Gson gson = new Gson();
                    Type type = new TypeToken<List<Icon>>() {
                    }.getType();
                    iconImageUrls = gson.fromJson(response.toString(), type);
                    Log.i("IconImages", iconImageUrls.toString());
                    setIcons();
                    progressDialog.hideProgressDialog();
                } catch (Exception e) {
                    Log.d("exceptionLoadData4rmvly", e.toString());
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

    private void newloadDataFromVolley() {
        JsonArrayRequest jsonArrayRequest = new JsonArrayRequest(Request.Method.GET, Endpoints.NEW_HOME_GET_URL, new Response.Listener<JSONArray>() {
            @Override
            public void onResponse(JSONArray response) {
                /*JSONObject jsonObject = null;
                JSONArray jsonArrayNew = null;
                try {
                    String jsonFormattedString = response.replaceAll("\\\\", "");
                    jsonFormattedString = jsonFormattedString.substring(1, jsonFormattedString.length() - 1);
                    jsonFormattedString = jsonFormattedString.replaceAll("\"\\[", "[");
                    jsonFormattedString = jsonFormattedString.replaceAll("\\]\"", "]");
                    Log.d("jsonFormattedString", jsonFormattedString);
                    jsonObject = new JSONObject(jsonFormattedString);
                    jsonArrayNew = jsonObject.getJSONArray("Contents");
                    Log.d("obj", jsonObject.toString());
                } catch (JSONException e) {
                    Log.d("JSONExec", e.getMessage());
                }*/

                try {
                    setSliderContent(response);
                    setTopContent(response);
                    setDailyLife(response);
                    setMulloCharContent(response);
                    setShikkhaSohayikaContent(response);
                    setGamesZoneContent(response);
                    setShocolChobiContent(response);
                    progressDialog.hideProgressDialog();

                } catch (Exception e) {
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
        queue.add(jsonArrayRequest);
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
                    if (contentCat.equals("moving_image")) {
                        shocolChobiArrayList.add(new ShocolChobi(contentType, contentUrl, contentTitle, contentCat, thumbnailImgUrl, contentId, contentPrice));
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                    Log.e("shocolChobi", e.toString());
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
                    Log.d("thumbnailImgUrlGa", thumbnailImgUrl);
                    if (thumbnailImgUrl.length() == 0)
                        thumbnailImgUrl = contentUrl;
                    ///int previousPrice = games_zone__content_arr.getJSONObject(i).getInt("previousPrice");
                    if (games_zone__content_arr.getJSONObject(i).has("contentPrice")) {
                        newPrice = games_zone__content_arr.getJSONObject(i).getInt("contentPrice");
                    }

                    int contentId = games_zone__content_arr.getJSONObject(i).getInt("id");
                    String contentCat = games_zone__content_arr.getJSONObject(i).getString("contentCat");
                    if (contentCat.equals("game_zone")) {
                        gamesZoneArrayList.add(new GamesZone(contentType, contentUrl, contentTitle, thumbnailImgUrl, 0, newPrice, contentCat, contentId));
                    }
                } catch (JSONException e) {
                    Log.e("exceptionGamesZone", e.toString());
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

                    if (contentCat.equals("education")) {
                        if (contentType.equals("video")) {
                            String contentUrl = shikkha_sohayika__content_arr.getJSONObject(i).getString("contentUrl");
                            shikkhaSohaYikaArrayList.add(new ShikkhaSohaYika(contentType, contentDescription, contentUrl, contentTitle, imageUrl, contentCat, contentId, contentPrice));
                        } else {
                            shikkhaSohaYikaArrayList.add(new ShikkhaSohaYika(contentType, contentDescription, "", contentTitle, imageUrl, contentCat, contentId, contentPrice));
                        }
                    }
                } catch (JSONException e) {
                    Log.e("EducationErr", e.toString());
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
                    if (contentCat.equals("song")) {
                        mulloCharArrayList.add(new MulloChar(contentType, contentUrl, contentTitle, thumbNail_image, previousPrice, newPrice, image_url, contentCat, contentId));

                    }
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

    private void setDailyLife(JSONArray japito_jibon_content_arr) {
        if (japito_jibon_content_arr.length() > 0) {
            for (int i = 0; i < japito_jibon_content_arr.length(); i++) {
                int contentPrice = 0;
                try {
                    String contentTitle = japito_jibon_content_arr.getJSONObject(i).getString("contentTitle");
                    String contentType = japito_jibon_content_arr.getJSONObject(i).getString("contentType");
                    String contentDescription = japito_jibon_content_arr.getJSONObject(i).getString("contentDescription");
                    String contentUrl = japito_jibon_content_arr.getJSONObject(i).getString("contentUrl");
                    String contentCat = japito_jibon_content_arr.getJSONObject(i).getString("contentCat");
                    String thumbNail_image = japito_jibon_content_arr.getJSONObject(i).getString("thumbNail_image");
                    int contentid = japito_jibon_content_arr.getJSONObject(i).getInt("id");
                    if (japito_jibon_content_arr.getJSONObject(i).has("contentPrice")) {
                        contentPrice = japito_jibon_content_arr.getJSONObject(i).getInt("contentPrice");
                    }
                    if (contentCat.equals("daily_life")) {
                        japitoJibonArrayList.add(new Porashuna(contentTitle, contentType, contentDescription, contentUrl, thumbNail_image, contentCat, contentid));
                        initializeJapitoJibonRecyclerView();
                    } else if (contentCat.equals("news")) {
                        hotNewsArrayList.add(new Porashuna(contentTitle, contentType, contentDescription, contentUrl, thumbNail_image, contentCat, contentid));
                        initializeHotNewsRecylerView();
                    } else if (contentCat.equals("sports")) {
                        sportsArralyList.add(new Porashuna(contentTitle, contentType, contentDescription, contentUrl, thumbNail_image, contentCat, contentid));
                        initializeSportsRecylerView();
                    } else if (contentCat.equals("autto_hashi")) {
                        auttoHashiArrList.add(new Porashuna(contentTitle, contentType, contentDescription, contentUrl, thumbNail_image, contentCat, contentid));
                        initializeAuttoHashiRecylerView();
                    } else if (contentCat.equals("science")) {
                        scienceArrList.add(new Porashuna(contentTitle, contentType, contentDescription, contentUrl, thumbNail_image, contentCat, contentid));
                        initializeScienceRecylerView();
                    } else if (contentCat.equals("mixed")) {
                        mixedArrList.add(new Porashuna(contentTitle, contentType, contentDescription, contentUrl, thumbNail_image, contentCat, contentid));
                        initializeMixedRecylerView();
                    } else if (contentCat.equals("kk_mela")) {
                        kidsArrList.add(new Porashuna(contentTitle, contentType, contentDescription, contentUrl, thumbNail_image, contentCat, contentid));
                        initializeKidsRecylerView();
                    }else if (contentCat.equals("mobile_app")) {
                        appList.add(new AppData(contentTitle, contentDescription, japito_jibon_content_arr.getJSONObject(i).getString("thumbNail_image"), contentUrl));
                    }else if (contentCat.equals("music_video")) {
                        musicVideoList.add(new Porashuna(contentTitle, contentType, contentDescription, contentUrl, thumbNail_image, contentCat, contentid));
                    }
                } catch (JSONException e) {
                    Log.d("japito_jibon_exception", e.toString());
                    e.printStackTrace();
                }
            }

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
                    if (contentCat.equals("top_images")) {
                        seraChobiArrayList.add(new SeraChobi(contentUrl, "", contentType, contentTitle, thumbNail_image, contentCat, contentId, contentDescription, contentPrice));
                    }
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
            String contentType;
            for (int i = 0; i < jsonSliderContentArr.length(); i++) {
                try {
                    String contentCat = jsonSliderContentArr.getJSONObject(i).getString("contentCat");
                    if (contentCat.equals("slider")) {
                        String contentUrl = jsonSliderContentArr.getJSONObject(i).getString("contentUrl");
                        String description = jsonSliderContentArr.getJSONObject(i).getString("contentDescription");
                        String contentTitle = jsonSliderContentArr.getJSONObject(i).getString("contentTitle");
                        contentType = jsonSliderContentArr.getJSONObject(i).getString("contentType");
                        int contentId = jsonSliderContentArr.getJSONObject(i).getInt("id");
                        String contentDescription = jsonSliderContentArr.getJSONObject(i).getString("contentDescription");
                        String thumbNailImg = jsonSliderContentArr.getJSONObject(i).getString("thumbNail_image");
                        if (jsonSliderContentArr.getJSONObject(i).has("contentPrice")) {
                            contentPrice = jsonSliderContentArr.getJSONObject(i).getInt("contentPrice");
                        }
                        sliderImages.add(new SliderImage(contentUrl, description, contentType, contentTitle, contentCat, contentId, contentDescription, thumbNailImg, contentPrice));
                    }
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
        this.finishAffinity();
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

    public void initializeHotNewsRecylerView() {
        recyclerViewForJapitoJibon = findViewById(R.id.RVhotnews);
        recyclerViewForJapitoJibon.setLayoutManager(new LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false));
        SnapHelper snapHelperStartJapitoJibon = new GravitySnapHelper(Gravity.START);
        snapHelperStartJapitoJibon.attachToRecyclerView(recyclerViewForJapitoJibon);
        recyclerViewForJapitoJibon.setHasFixedSize(true);
        adapterForJapitoJibon = new RecyclerAdapterForJapitoJibon(this, hotNewsArrayList);
        recyclerViewForJapitoJibon.setAdapter(adapterForJapitoJibon);
    }

    public void initializeAuttoHashiRecylerView() {
        recyclerViewForJapitoJibon = findViewById(R.id.RVAuttoahshi);
        recyclerViewForJapitoJibon.setLayoutManager(new LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false));
        SnapHelper snapHelperStartJapitoJibon = new GravitySnapHelper(Gravity.START);
        snapHelperStartJapitoJibon.attachToRecyclerView(recyclerViewForJapitoJibon);
        recyclerViewForJapitoJibon.setHasFixedSize(true);
        adapterForJapitoJibon = new RecyclerAdapterForJapitoJibon(this, auttoHashiArrList);
        recyclerViewForJapitoJibon.setAdapter(adapterForJapitoJibon);
    }

    public void initializeSportsRecylerView() {
        recyclerViewForJapitoJibon = findViewById(R.id.RVsports);
        recyclerViewForJapitoJibon.setLayoutManager(new LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false));
        SnapHelper snapHelperStartJapitoJibon = new GravitySnapHelper(Gravity.START);
        snapHelperStartJapitoJibon.attachToRecyclerView(recyclerViewForJapitoJibon);
        recyclerViewForJapitoJibon.setHasFixedSize(true);
        adapterForJapitoJibon = new RecyclerAdapterForJapitoJibon(this, sportsArralyList);
        recyclerViewForJapitoJibon.setAdapter(adapterForJapitoJibon);
    }

    public void initializeScienceRecylerView() {
        recyclerViewForJapitoJibon = findViewById(R.id.RVScience);
        recyclerViewForJapitoJibon.setLayoutManager(new LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false));
        SnapHelper snapHelperStartJapitoJibon = new GravitySnapHelper(Gravity.START);
        snapHelperStartJapitoJibon.attachToRecyclerView(recyclerViewForJapitoJibon);
        recyclerViewForJapitoJibon.setHasFixedSize(true);
        adapterForJapitoJibon = new RecyclerAdapterForJapitoJibon(this, scienceArrList);
        recyclerViewForJapitoJibon.setAdapter(adapterForJapitoJibon);
    }

    public void initializeMixedRecylerView() {
        recyclerViewForJapitoJibon = findViewById(R.id.RVMixed);
        recyclerViewForJapitoJibon.setLayoutManager(new LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false));
        SnapHelper snapHelperStartJapitoJibon = new GravitySnapHelper(Gravity.START);
        snapHelperStartJapitoJibon.attachToRecyclerView(recyclerViewForJapitoJibon);
        recyclerViewForJapitoJibon.setHasFixedSize(true);
        adapterForJapitoJibon = new RecyclerAdapterForJapitoJibon(this, mixedArrList);
        recyclerViewForJapitoJibon.setAdapter(adapterForJapitoJibon);
    }

    public void initializeKidsRecylerView() {
        recyclerViewForJapitoJibon = findViewById(R.id.RVKids);
        recyclerViewForJapitoJibon.setLayoutManager(new LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false));
        SnapHelper snapHelperStartJapitoJibon = new GravitySnapHelper(Gravity.START);
        snapHelperStartJapitoJibon.attachToRecyclerView(recyclerViewForJapitoJibon);
        recyclerViewForJapitoJibon.setHasFixedSize(true);
        adapterForJapitoJibon = new RecyclerAdapterForJapitoJibon(this, kidsArrList);
        recyclerViewForJapitoJibon.setAdapter(adapterForJapitoJibon);
    }

    public void startGoromKhoborPage(View view) {
        Intent myIntent = new Intent(getApplicationContext(), PorashunaActivity.class);
        myIntent.putExtra("tag", "news");
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
        Intent intentDailyLife = new Intent(HomePage.this, PorashunaActivity.class);
        intentDailyLife.putExtra("tag", "daily_life");
        startActivity(intentDailyLife);
    }

    public void moreShikha(View view) {
        Intent intentDailyLife = new Intent(HomePage.this, PorashunaActivity.class);
        intentDailyLife.putExtra("tag", "education");
        startActivity(intentDailyLife);
    }

    public void startSportActivity(View view) {
        Intent myIntent = new Intent(getApplicationContext(), PorashunaActivity.class);
        myIntent.putExtra("tag", "sports");
        this.startActivity(myIntent);
    }

    public void startPorashunaActivity(View view) {
        Intent myIntent = new Intent(getApplicationContext(), PorashunaActivity.class);
        myIntent.putExtra("tag", "education");
        this.startActivity(myIntent);
    }

    public void startAuttoHashiActivity(View view) {
        Intent myIntent = new Intent(getApplicationContext(), PorashunaActivity.class);
        myIntent.putExtra("tag", "autto_hashi");
        this.startActivity(myIntent);
    }

    public void startJibonJaponActivity(View view) {
        Intent myIntent = new Intent(getApplicationContext(), PorashunaActivity.class);
        myIntent.putExtra("tag", "daily_life");
        this.startActivity(myIntent);
    }

    public void startPachMishaliActivity(View view) {
        Intent myIntent = new Intent(getApplicationContext(), PorashunaActivity.class);
        myIntent.putExtra("tag", "mixed");
        this.startActivity(myIntent);
    }

    public void startBigganOProjuktiActivity(View view) {
        Intent myIntent = new Intent(getApplicationContext(), PorashunaActivity.class);
        myIntent.putExtra("tag", "science");
        this.startActivity(myIntent);
    }

    public void startCartoonActivity(View view) {
        Intent myIntent = new Intent(getApplicationContext(), PorashunaActivity.class);
        myIntent.putExtra("tag", "kk_mela");
        this.startActivity(myIntent);
    }


    public void jumpToAppList(View view) {
        Intent intent = new Intent(HomePage.this, AppList.class);
        Gson gson = new Gson();
        String appListJson = gson.toJson(appList);
        intent.putExtra("appList", appListJson);
        startActivity(intent);
    }

    public void jumpToVideoList(View view) {
        Intent intent = new Intent(HomePage.this, Music_Video.class);
        Gson gson = new Gson();
        String appListJson = gson.toJson(musicVideoList);
        intent.putExtra("videoList", appListJson);
        startActivity(intent);
    }

    public void jumpToContact(View view) {
        Toast.makeText(HomePage.this, "Emoticons will available soon!", Toast.LENGTH_SHORT).show();
        /*Intent intent = new Intent(HomePage.this, Emoticons.class);
        intent.putExtra("position", 2);
        startActivity(intent);*/
    }

    @Override
    public void onPause() {
        super.onPause();
        if (progressDialog != null)
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
