package tanvir.multiplexer.Activity;

import android.content.Context;
import android.content.Intent;
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
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;
import com.github.rubensousa.gravitysnaphelper.GravitySnapHelper;
import com.viewpagerindicator.CirclePageIndicator;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

import tanvir.multiplexer.CustomSwipeAdapter;
import tanvir.multiplexer.ModelClass.GamesZone;
import tanvir.multiplexer.ModelClass.JapitoJibonMC;
import tanvir.multiplexer.ModelClass.MulloChar;
import tanvir.multiplexer.ModelClass.ShikkhaSohaYika;
import tanvir.multiplexer.ModelClass.ShocolChobi;
import tanvir.multiplexer.ModelClass.SliderImage;
import tanvir.multiplexer.R;
import tanvir.multiplexer.RecycleerViewAdapter.RecyclerAdapterForGamesZone;
import tanvir.multiplexer.RecycleerViewAdapter.RecyclerAdapterForJapitoJibon;
import tanvir.multiplexer.RecycleerViewAdapter.RecyclerAdapterForMulloChar;
import tanvir.multiplexer.RecycleerViewAdapter.RecyclerAdapterForSeraChobi;
import tanvir.multiplexer.RecycleerViewAdapter.RecyclerAdapterForShikkhaSohayika;
import tanvir.multiplexer.RecycleerViewAdapter.RecyclerAdapterForShocolChobi;
import tanvir.multiplexer.helper.Endpoints;

public class HomePage extends AppCompatActivity {
    Context context;
    ArrayList<SliderImage> sliderImages;
    ArrayList<String> topContentImages;
    ArrayList<JapitoJibonMC> japitoJibonMCArrayList;

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

    ImageView profileIcon , notificationIcon;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home_page);

        context = HomePage.this;
        queue = Volley.newRequestQueue(HomePage.this);

        sliderImages = new ArrayList<>();
        topContentImages = new ArrayList<>();

        toolbar = findViewById(R.id.toolbarlayoutinhome_page);
        setSupportActionBar(toolbar);

        profileIcon = findViewById(R.id.profileIcon);
        notificationIcon=findViewById(R.id.notificationInHomePageToolbar);

        japitoJibonMCArrayList = new ArrayList<>();
        mulloCharArrayList = new ArrayList<>();
        gamesZoneArrayList = new ArrayList<>();
        shocolChobiArrayList = new ArrayList<>();
        shikkhaSohaYikaArrayList = new ArrayList<>();

        //textView Onclicks

        ///Bottom navigation

        bottomNavigationView = findViewById(R.id.btmNavigation);
        bottomNavigationView.getMenu().findItem(R.id.home_bottom_navigation).setChecked(true);

        bottomNavigationView.setOnNavigationItemSelectedListener(new BottomNavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem item) {
                int id = item.getItemId();
                if(id== R.id.symphony_bottom_navigation){
                    Intent symphony = new Intent(HomePage.this, Symphony.class);
                    startActivity(symphony);
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

        loadDataFromVolley();
    }

    private void loadDataFromVolley() {

        JsonObjectRequest jsonObjectRequest = new JsonObjectRequest(Request.Method.GET, Endpoints.UPDATED_HOME_GET_URL,
                new Response.Listener<JSONObject>() {
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

                            //settop_contents(jsontop_contentsArr);
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }

                    }
                }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Log.e("Volley", error.toString());
            }
        });

        queue.add(jsonObjectRequest);
    }

    private void setShocolChobiContent(JSONArray shocol_chobi__content_arr) {


        if (shocol_chobi__content_arr.length() > 0) {
            for (int i = 0; i < shocol_chobi__content_arr.length(); i++) {
                try {
                    String contentUrl = shocol_chobi__content_arr.getJSONObject(i).getString("contentUrl");
                    String contentTitle = shocol_chobi__content_arr.getJSONObject(i).getString("contentTitle");
                    String contentType = shocol_chobi__content_arr.getJSONObject(i).getString("contentType");

                    shocolChobiArrayList.add(new ShocolChobi(contentType, contentUrl, contentTitle));

                } catch (JSONException e) {

                    e.printStackTrace();
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
                try {
                    String contentUrl = games_zone__content_arr.getJSONObject(i).getString("contentUrl");
                    String contentTitle = games_zone__content_arr.getJSONObject(i).getString("contentTitle");
                    String contentType = games_zone__content_arr.getJSONObject(i).getString("contentType");
                    int previousPrice = games_zone__content_arr.getJSONObject(i).getInt("previousPrice");
                    int newPrice = games_zone__content_arr.getJSONObject(i).getInt("newPrice");

                    gamesZoneArrayList.add(new GamesZone(contentType, contentUrl, contentTitle, previousPrice, newPrice));

                    /*if (contentType.equals("video")) {
                        japitoJibonMCArrayList.add(new JapitoJibonMC(contentTitle, japito_jibon_content_arr.getJSONObject(i).getString("thumbNail_image"), "video"));
                    } else {
                        japitoJibonMCArrayList.add(new JapitoJibonMC(contentTitle, japito_jibon_content_arr.getJSONObject(i).getString("contentUrl"), "image"));
                    }*/

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
                try {
                    String contentTitle = shikkha_sohayika__content_arr.getJSONObject(i).getString("contentTitle");
                    String contentType = shikkha_sohayika__content_arr.getJSONObject(i).getString("contentType");
                    String imageUrl = shikkha_sohayika__content_arr.getJSONObject(i).getString("thumbNail_image");
                    String contentDescription= shikkha_sohayika__content_arr.getJSONObject(i).getString("contentDescription");

                    if (contentType.equals("video")) {
                        String contentUrl = shikkha_sohayika__content_arr.getJSONObject(i).getString("contentUrl");
                        shikkhaSohaYikaArrayList.add(new ShikkhaSohaYika(contentType,contentDescription, contentUrl, contentTitle, imageUrl));
                    } else {
                        shikkhaSohaYikaArrayList.add(new ShikkhaSohaYika(contentType, contentDescription,"", contentTitle, imageUrl));
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

    private void setMulloCharContent(JSONArray mullo_char_content_arr) {

        if (mullo_char_content_arr.length() > 0) {
            for (int i = 0; i < mullo_char_content_arr.length(); i++) {
                try {
                    String contentUrl = mullo_char_content_arr.getJSONObject(i).getString("contentUrl");
                    String contentTitle = mullo_char_content_arr.getJSONObject(i).getString("contentTitle");
                    String contentType = mullo_char_content_arr.getJSONObject(i).getString("contentType");
                    String image_url = mullo_char_content_arr.getJSONObject(i).getString("image_url");
                    int previousPrice = mullo_char_content_arr.getJSONObject(i).getInt("previousPrice");

                    int newPrice = mullo_char_content_arr.getJSONObject(i).getInt("newPrice");

                    mulloCharArrayList.add(new MulloChar(contentType, contentUrl, contentTitle, previousPrice, newPrice, image_url));

                    /*if (contentType.equals("video")) {
                        japitoJibonMCArrayList.add(new JapitoJibonMC(contentTitle, japito_jibon_content_arr.getJSONObject(i).getString("thumbNail_image"), "video"));
                    } else {
                        japitoJibonMCArrayList.add(new JapitoJibonMC(contentTitle, japito_jibon_content_arr.getJSONObject(i).getString("contentUrl"), "image"));
                    }*/

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
                try {
                    ///String contentUrl = japito_jibon_content_arr.getJSONObject(i).getString("contentUrl");
                    String contentTitle = japito_jibon_content_arr.getJSONObject(i).getString("contentTitle");
                    String contentType = japito_jibon_content_arr.getJSONObject(i).getString("contentType");
                    String contentDescription = japito_jibon_content_arr.getJSONObject(i).getString("contentDescription");
                    String contentUrl = japito_jibon_content_arr.getJSONObject(i).getString("contentUrl");

                    if (contentType.equals("video")) {
                        Log.i("Data", "Video");
                        japitoJibonMCArrayList.add(new JapitoJibonMC(contentTitle,contentDescription, japito_jibon_content_arr.getJSONObject(i).getString("thumbNail_image"), "video",contentUrl));
                    } else {
                        Log.i("Data", "Image");
                        japitoJibonMCArrayList.add(new JapitoJibonMC(contentTitle,contentDescription, japito_jibon_content_arr.getJSONObject(i).getString("contentUrl"), "image",contentUrl));
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
                try {
                    String contentUrl = top_contentsArr.getJSONObject(i).getString("contentUrl");
                    topContentImages.add(contentUrl);

                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
            initializeTopContentRecyclerView();
        } else {
            Toast.makeText(context, "No data found", Toast.LENGTH_SHORT).show();
        }
    }

    private void setSliderContent(JSONArray jsonSliderContentArr) {

        if (jsonSliderContentArr.length() > 0) {
            for (int i = 0; i < jsonSliderContentArr.length(); i++) {
                try {
                    String contentUrl = jsonSliderContentArr.getJSONObject(i).getString("contentUrl");
                    String description = jsonSliderContentArr.getJSONObject(i).getString("contentDescription");
                    sliderImages.add(new SliderImage(contentUrl, description));

                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
            initialCustomSwipeAdapter();
        } else {
            Toast.makeText(context, "No data found", Toast.LENGTH_SHORT).show();
        }
    }

    public void startLoginActivity(View view) {
        Intent myIntent = new Intent(getApplicationContext(), MainActivity.class);
        myIntent.addFlags(Intent.FLAG_ACTIVITY_NO_HISTORY);
        this.startActivity(myIntent);
        overridePendingTransition(R.anim.left_in, R.anim.left_out);
        finish();
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();

        Intent myIntent = new Intent(getApplicationContext(), MainActivity.class);
        myIntent.addFlags(Intent.FLAG_ACTIVITY_NO_HISTORY);
        this.startActivity(myIntent);
        overridePendingTransition(R.anim.left_in, R.anim.left_out);
        finish();

    }

    public void startSportActivity(View view) {
        Intent myIntent = new Intent(getApplicationContext(), KheladhulaActivity.class);
        myIntent.addFlags(Intent.FLAG_ACTIVITY_NO_HISTORY);
        this.startActivity(myIntent);
        overridePendingTransition(R.anim.left_in, R.anim.left_out);
        finish();
    }

    public void startPorashunaActivity(View view) {
        Intent myIntent = new Intent(getApplicationContext(), PorashunaActivity.class);
        myIntent.addFlags(Intent.FLAG_ACTIVITY_NO_HISTORY);
        this.startActivity(myIntent);
        overridePendingTransition(R.anim.left_in, R.anim.left_out);
        finish();    }

    public void startAuttoHashiActivity(View view) {

        Intent myIntent = new Intent(getApplicationContext(), AuttoHashiActivity.class);
        myIntent.addFlags(Intent.FLAG_ACTIVITY_NO_HISTORY);
        this.startActivity(myIntent);
        overridePendingTransition(R.anim.left_in, R.anim.left_out);
        finish();
    }

    public void startJibonJaponActivity(View view) {

        Intent myIntent = new Intent(getApplicationContext(), JibonJaponActivity.class);
        myIntent.addFlags(Intent.FLAG_ACTIVITY_NO_HISTORY);
        this.startActivity(myIntent);
        overridePendingTransition(R.anim.left_in, R.anim.left_out);
        finish();

    }

    public void startPachMishaliActivity(View view) {

        Intent myIntent = new Intent(getApplicationContext(), PachMishaliActivity.class);
        myIntent.addFlags(Intent.FLAG_ACTIVITY_NO_HISTORY);
        this.startActivity(myIntent);
        overridePendingTransition(R.anim.left_in, R.anim.left_out);
        finish();


    }

    public void startBigganOProjuktiActivity(View view) {

        Intent myIntent = new Intent(getApplicationContext(), BigganOProjuktiActivity.class);
        myIntent.addFlags(Intent.FLAG_ACTIVITY_NO_HISTORY);
        this.startActivity(myIntent);
        overridePendingTransition(R.anim.left_in, R.anim.left_out);
        finish();

    }

    public void startCartoonActivity(View view) {

        Intent myIntent = new Intent(getApplicationContext(), CartoonActivity.class);
        myIntent.addFlags(Intent.FLAG_ACTIVITY_NO_HISTORY);
        this.startActivity(myIntent);
        overridePendingTransition(R.anim.left_in, R.anim.left_out);
        finish();
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

        recyclerViewForSeraChobi.setLayoutManager(new LinearLayoutManager(this,
                LinearLayoutManager.HORIZONTAL, false));

        SnapHelper snapHelperStart = new GravitySnapHelper(Gravity.START);
        snapHelperStart.attachToRecyclerView(recyclerViewForSeraChobi);

        recyclerViewForSeraChobi.setHasFixedSize(true);
        adapterForSeraChobi = new RecyclerAdapterForSeraChobi(this, topContentImages);

        recyclerViewForSeraChobi.setAdapter(adapterForSeraChobi);
    }

    public void initializeJapitoJibonRecyclerView() {
        //Recyclerview for japito jibon

        recyclerViewForJapitoJibon = findViewById(R.id.RV_japitoJibon);

        recyclerViewForJapitoJibon.setLayoutManager(new LinearLayoutManager(this,
                LinearLayoutManager.VERTICAL, false));

        SnapHelper snapHelperStartJapitoJibon = new GravitySnapHelper(Gravity.START);
        snapHelperStartJapitoJibon.attachToRecyclerView(recyclerViewForJapitoJibon);


        recyclerViewForJapitoJibon.setHasFixedSize(true);
        adapterForJapitoJibon = new RecyclerAdapterForJapitoJibon(this, japitoJibonMCArrayList);

        recyclerViewForJapitoJibon.setAdapter(adapterForJapitoJibon);
    }


    public void initializeMulloCharRecyclerView() {

        //Recyclerview for mullochar


        recyclerViewForMulloChar = findViewById(R.id.RV_mulloChar);

        recyclerViewForMulloChar.setLayoutManager(new LinearLayoutManager(this,
                LinearLayoutManager.HORIZONTAL, false));

        SnapHelper snapHelperStartMulloChar = new GravitySnapHelper(Gravity.START);
        snapHelperStartMulloChar.attachToRecyclerView(recyclerViewForMulloChar);

        recyclerViewForMulloChar.setHasFixedSize(true);
        adapterForMulloChar = new RecyclerAdapterForMulloChar(this, mulloCharArrayList);

        recyclerViewForMulloChar.setAdapter(adapterForMulloChar);

    }


    public void initializeGamesZoneRecyclerView() {

        recyclerViewForGamesZone = findViewById(R.id.RV_gamesZone);

        recyclerViewForGamesZone.setLayoutManager(new LinearLayoutManager(this,
                LinearLayoutManager.HORIZONTAL, false));

        SnapHelper snapHelperStartMulloChar = new GravitySnapHelper(Gravity.START);
        snapHelperStartMulloChar.attachToRecyclerView(recyclerViewForGamesZone);

        recyclerViewForGamesZone.setHasFixedSize(true);
        adapterForGamesZone = new RecyclerAdapterForGamesZone(this, gamesZoneArrayList);

        recyclerViewForGamesZone.setAdapter(adapterForGamesZone);

    }

    public void initializeShocolChobiRecyclerview() {
        //Recyclerview for ShocholChobi

        recyclerViewForShocolChobi = findViewById(R.id.RV_ShocolChobi);

        recyclerViewForShocolChobi.setLayoutManager(new LinearLayoutManager(this,
                LinearLayoutManager.HORIZONTAL, false));

        SnapHelper snapHelperStartShocolChobi = new GravitySnapHelper(Gravity.START);
        snapHelperStartShocolChobi.attachToRecyclerView(recyclerViewForShocolChobi);

        recyclerViewForShocolChobi.setHasFixedSize(true);
        adapterForShocolChobi = new RecyclerAdapterForShocolChobi(this, shocolChobiArrayList);

        recyclerViewForShocolChobi.setAdapter(adapterForShocolChobi);
    }

    public void initializeShikkhaSohayikaRecyclerview() {
        //Recyclerview for ShikkhaSohayika

        recyclerViewForShikkhaSohayika = findViewById(R.id.RV_ShikkhaSohayika);

        recyclerViewForShikkhaSohayika.setLayoutManager(new LinearLayoutManager(this,
                LinearLayoutManager.VERTICAL, false));

        SnapHelper snapHelperStartShikkhaSohayika = new GravitySnapHelper(Gravity.START);
        snapHelperStartShikkhaSohayika.attachToRecyclerView(recyclerViewForShikkhaSohayika);

        recyclerViewForShikkhaSohayika.setHasFixedSize(true);
        adapterForShikkhaSohayika = new RecyclerAdapterForShikkhaSohayika(this, shikkhaSohaYikaArrayList);

        recyclerViewForShikkhaSohayika.setAdapter(adapterForShikkhaSohayika);

    }

    public void startGoromKhoborPage(View view) {
        Intent myIntent = new Intent(getApplicationContext(), GoromKhoborActivity.class);
        myIntent.addFlags(Intent.FLAG_ACTIVITY_NO_HISTORY);
        this.startActivity(myIntent);
        overridePendingTransition(R.anim.left_in, R.anim.left_out);
        finish();
    }

    public void startGalleryActivity(View view) {
        Intent galleryIntent = new Intent(getApplicationContext(), GalleryActivity.class);
        galleryIntent.putStringArrayListExtra("galleryImageData",topContentImages);
        galleryIntent.addFlags(Intent.FLAG_ACTIVITY_NO_HISTORY);
        this.startActivity(galleryIntent);
        overridePendingTransition(R.anim.left_in, R.anim.left_out);
        finish();
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
}
