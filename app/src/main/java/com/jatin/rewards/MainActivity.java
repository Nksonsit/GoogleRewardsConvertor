package com.jatin.rewards;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.design.widget.AppBarLayout;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.DefaultRetryPolicy;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.google.android.gms.ads.AdListener;
import com.google.android.gms.ads.AdRequest;
import com.google.android.gms.ads.AdView;
import com.google.android.gms.ads.InterstitialAd;
import com.jatin.rewards.utils.IabHelper;
import com.jatin.rewards.utils.IabResult;
import com.jatin.rewards.utils.Inventory;
import com.jatin.rewards.utils.Purchase;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class MainActivity extends AppCompatActivity {


    private String BASE64_ENCODED_PUBLIC_KEY = "MIIBIjANBgkqhkiG9w0BAQEFAAOCAQ8AMIIBCgKCAQEAwAhNe7wbTkSfr5bcFuWRzknFHB4aLv/7QmKHSewneP2mcKsqi0Xp4Bs9x1Aom220gb1RVdNXGYlQoDOt1eV/dL2cqB6+n+Urfn4rQc+CwVU/LBlAoxFHfzw66JKByZ1duZDpNV+yNWhMJqtgc6fjQ6NELSf0dg4LEQFanDT8IHvRbtfZ7Rk02BjazwsixAM++vqhns3nmZQV2N0U1ZNemdvdBzlWoQpatM/kIOznxAO4Zjx89Wg8F4Z5bb0kvh2xwAT7o+prbSXLiJzVQAQowEtn13LkhykkSaooN2zNWXMn+rXuFDT99/ySt3Isc1+7FdcOqE+/3OXmYSVrDaIGgQIDAQAB";

    private static final String TAG = "MainActivity";
    private IabHelper mHelper;
    private IabHelper.OnIabPurchaseFinishedListener mPurchaseFinishedListener;
    private IabHelper.QueryInventoryFinishedListener mReceivedInventoryListener;
    private IabHelper.OnConsumeFinishedListener mConsumeFinishedListener;
    private boolean isInAppInProgree = false;
    private boolean isBought = false;
    //    public static String ITEM_SKU = "android.test.purchased";
//        public static String ITEM_SKU = "ishan_1012";
    private static int INAPP_SUCECESS_RESPONSE_CODE = 10001;
    public static final String RESPONSE_INAPP_PURCHASE_DATA = "INAPP_PURCHASE_DATA";
    public static final String RESPONSE_INAPP_SIGNATURE = "INAPP_DATA_SIGNATURE";
    private String TransactionRefID = "";
    private String TransactionDate = "";


    RequestQueue queue;
    // Google Forms URL
    public static final String url = "https://docs.google.com/forms/d/1Kto9UsHR1OTerLjxsXvGkTflb7PgrXQkxYdjoXRcQzg/formResponse";

    //    public static final String email = "entry.1317516008";
//    public static final String country = "entry.1563719254";
    public static final String email = "entry.624388695";
    public static final String country = "entry.1372464918";
    public static final String name = "entry.1374722863";
    public static final String payment_mode = "entry.403838916";
    public static final String amount = "entry.1622831794";
    public static final String data = "entry.807570936";
    public static final String days = "entry.2019417577";
    public static final String suggestion = "entry.423242404";
    public static final String transaction_id = "entry.367219391";
    public static final String userId = "entry.1322304647";


    private AppBarLayout appBar;
    private EditText edtEmail;
    private Spinner spinnerCountry;
    private EditText edtName;
    private RadioButton rbType1;
    private RadioButton rbType2;
    private RadioButton rbType3;
    private RadioButton rbType4;
    private RadioButton rbType5;
    private RadioButton rbType6;
    private RadioButton rbType7;
    private RadioButton rbType8;
    private RadioGroup rgPaymentType;
    private EditText edtData;
    private RadioButton rgDay4;
    private RadioButton rgDay5;
    private RadioButton rgDay6;
    private RadioButton rgDay7;
    private RadioGroup rgDays;
    private EditText edtSuggestion;
    private Button btnPay;
    private List<String> countries;
    private String paymentMode = "";
    private String DaysTxt = "";
    private Spinner spinnerAmount;
    private List<String> amountList;
    private Toolbar toolbar;
    private LinearLayout drawerContainer;
    private DrawerLayout mDrawerLayout;
    private View drawerView;
    private ActionBarDrawerToggle mDrawerToggle;
    private LinearLayout llContactUs;
    private LinearLayout llAboutUs;
    private TextView txtEmailId;
    private TextView txtUserName;
    private LinearLayout llRateUs;
    private LinearLayout llTC;
    private List<String> amountCode;
    private InterstitialAd mInterstitialAd;
    private AdRequest interAdRequest;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        this.toolbar = (Toolbar) findViewById(R.id.toolbar);
        this.btnPay = (Button) findViewById(R.id.btnPay);
        this.edtSuggestion = (EditText) findViewById(R.id.edtSuggestion);
        this.rgDays = (RadioGroup) findViewById(R.id.rgDays);
        this.rgDay7 = (RadioButton) findViewById(R.id.rgDay7);
        this.rgDay6 = (RadioButton) findViewById(R.id.rgDay6);
        this.rgDay5 = (RadioButton) findViewById(R.id.rgDay5);
        this.rgDay4 = (RadioButton) findViewById(R.id.rgDay4);
        this.edtData = (EditText) findViewById(R.id.edtData);
        this.rgPaymentType = (RadioGroup) findViewById(R.id.rgPaymentType);
        this.rbType8 = (RadioButton) findViewById(R.id.rbType8);
        this.rbType7 = (RadioButton) findViewById(R.id.rbType7);
        this.rbType6 = (RadioButton) findViewById(R.id.rbType6);
        this.rbType5 = (RadioButton) findViewById(R.id.rbType5);
        this.rbType4 = (RadioButton) findViewById(R.id.rbType4);
        this.rbType3 = (RadioButton) findViewById(R.id.rbType3);
        this.rbType2 = (RadioButton) findViewById(R.id.rbType2);
        this.rbType1 = (RadioButton) findViewById(R.id.rbType1);
        this.edtName = (EditText) findViewById(R.id.edtName);
        this.spinnerCountry = (Spinner) findViewById(R.id.spinnerCountry);
        this.spinnerAmount = (Spinner) findViewById(R.id.spinnerAmount);
        this.edtEmail = (EditText) findViewById(R.id.edtEmail);
        this.appBar = (AppBarLayout) findViewById(R.id.appBar);

        String[] myResArray = getResources().getStringArray(R.array.countries);
        String[] amountArray = getResources().getStringArray(R.array.amount);

        //Creating the ArrayAdapter instance having the country list
        countries = new ArrayList<>();
        amountList = new ArrayList<>();
        amountCode = new ArrayList<>();

        amountCode.add("product_10");
        amountCode.add("product_20");
        amountCode.add("product_30");
        amountCode.add("product_40");
        amountCode.add("product_50");
        amountCode.add("product_60");
        amountCode.add("product_70");
        amountCode.add("product_80");
        amountCode.add("product_90");
        amountCode.add("product_100");
        amountCode.add("product_150");
        amountCode.add("product_200");
        amountCode.add("product_250");
        amountCode.add("product_300");
        amountCode.add("product_350");
        amountCode.add("product_400");
        amountCode.add("product_450");
        amountCode.add("product_500");
        amountCode.add("product_550");
        amountCode.add("product_600");
        amountCode.add("product_650");
        amountCode.add("product_700");
        amountCode.add("product_750");
        amountCode.add("product_800");
        amountCode.add("product_850");
        amountCode.add("product_900");
        amountCode.add("product_950");
        amountCode.add("product_1000");

        countries = Arrays.asList(myResArray);
        amountList = Arrays.asList(amountArray);

        ArrayAdapter aa = new ArrayAdapter(this, android.R.layout.simple_spinner_item, countries);
        ArrayAdapter amountAdapter = new ArrayAdapter(this, android.R.layout.simple_spinner_item, amountList);
        aa.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        amountAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        //Setting the ArrayAdapter data on the Spinner
        spinnerCountry.setAdapter(aa);
        spinnerAmount.setAdapter(amountAdapter);


        drawerContainer = (LinearLayout) findViewById(R.id.fragment_navigation_drawer);
        mDrawerLayout = (DrawerLayout) findViewById(R.id.drawer_layout);

        drawerView = LayoutInflater.from(this).inflate(R.layout.nav_drawer, null, false);

        drawerContainer.removeAllViews();
        drawerContainer.addView(drawerView);

        setUpDrawer();

        txtUserName = (TextView) drawerView.findViewById(R.id.txtUserName);
        txtEmailId = (TextView) drawerView.findViewById(R.id.txtEmailId);
        llAboutUs = (LinearLayout) drawerView.findViewById(R.id.llAboutUs);
        llContactUs = (LinearLayout) drawerView.findViewById(R.id.llContactUs);
        llRateUs = (LinearLayout) drawerView.findViewById(R.id.llRateUs);
        llTC = (LinearLayout) drawerView.findViewById(R.id.llTC);


        llContactUs.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(MainActivity.this, ContactUsActivity.class));
                mDrawerLayout.closeDrawer(Gravity.START);
            }
        });

        llAboutUs.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(MainActivity.this, AboutUsActivity.class));
                mDrawerLayout.closeDrawer(Gravity.START);
            }
        });

        llTC.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                new WebViewDialog(MainActivity.this, "file:///android_res/raw/tc.html", "Term and Condition");
                mDrawerLayout.closeDrawer(Gravity.START);
            }
        });

        llRateUs.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mDrawerLayout.closeDrawer(Gravity.START);
                final String appPackageName = getPackageName(); // getPackageName() from Context or Activity object
                try {
                    startActivity(new Intent(Intent.ACTION_VIEW, Uri.parse("market://details?id=" + appPackageName)));
                } catch (android.content.ActivityNotFoundException anfe) {
                    startActivity(new Intent(Intent.ACTION_VIEW, Uri.parse("https://play.google.com/store/apps/details?id=" + appPackageName)));
                }
            }
        });

        txtEmailId.setText(PrefUtils.getEmailID(this));
        txtUserName.setText(PrefUtils.getUserName(this));


        // Initializing Queue for Volley
        queue = Volley.newRequestQueue(getApplicationContext());

        mHelper = new IabHelper(MainActivity.this, BASE64_ENCODED_PUBLIC_KEY);
        mHelper.enableDebugLogging(true, "Purchase json");
        startStatusCheckupforInApp();

        try {
            mPurchaseFinishedListener = new IabHelper.OnIabPurchaseFinishedListener() {
                public void onIabPurchaseFinished(IabResult result,
                                                  Purchase purchase) {
                    if (result.isFailure()) {
//                        Log.e(TAG, "failure in listner" + result.toString());
                        isInAppInProgree = false;
                        return;
                    } else if (purchase.getSku().equals(amountCode.get(spinnerAmount.getSelectedItemPosition()))) {
                        isBought = true;
                        queryPurchasedItems();
//                        Log.e(TAG, "SKU" + purchase.getSku() + " " + amountCode.get(spinnerAmount.getSelectedItemPosition()));
                    }
                }
            };
        } catch (Exception e) {
            e.printStackTrace();
        }

        mReceivedInventoryListener = new IabHelper.QueryInventoryFinishedListener() {
            public void onQueryInventoryFinished(IabResult result,
                                                 Inventory inventory) {

                if (result.isFailure()) {
//                    Log.e(TAG, "In-app Billing query failed: " +
//                            result);
                    // Handle failure
                } else {
                    isBought = inventory.hasPurchase(amountCode.get(spinnerAmount.getSelectedItemPosition()));
                    if (isBought) {
                        try {
                            mHelper.consumeAsync(inventory.getPurchase(amountCode.get(spinnerAmount.getSelectedItemPosition())),
                                    mConsumeFinishedListener);
                        } catch (Exception e) {
                            e.printStackTrace();
                        }
                    }
                }
            }
        };
        try {
            mConsumeFinishedListener =
                    new IabHelper.OnConsumeFinishedListener() {
                        public void onConsumeFinished(Purchase purchase,
                                                      IabResult result) {
                            if (result.isSuccess()) {
                                Toast.makeText(MainActivity.this, "Purchased", Toast.LENGTH_SHORT).show();
//                                Functions.showToast(RewardsActivity.this, packageName + AppConstants.SPACE + getString(R.string.package_label) + AppConstants.SPACE + getString(R.string.msg_success_purchased));
//                                if (Functions.isConnected(context)) {
//                                    callPackageUpdateApi();
//                                } else {
//                                    Functions.showToast(context, getString(R.string.check_internet));
//                                }

                                postData(edtEmail.getText().toString().trim(), countries.get(spinnerCountry.getSelectedItemPosition()), edtName.getText().toString().trim(), paymentMode, amountList.get(spinnerAmount.getSelectedItemPosition()), edtData.getText().toString().trim(), DaysTxt, edtSuggestion.getText().toString().trim(), TransactionRefID);


                            } else {
//                                Log.e(TAG, "In-app Billing mConsumeFinishedListener failed: " +
//                                        result);
//                                Functions.showToast(RewardsActivity.this, getString(R.string.try_again));
                            }
                        }
                    };
        } catch (Exception e) {
            e.printStackTrace();
        }


        showbannerAds();
        loadInterstitialAds();
    }

    private void startStatusCheckupforInApp() {
        try {
            mHelper.startSetup(new
                                       IabHelper.OnIabSetupFinishedListener() {
                                           public void onIabSetupFinished(IabResult result) {
                                               if (!result.isSuccess()) {
//                                                   Log.e(TAG, "In-app Billing setup failed: " +
//                                                           result);
                                               } else {
//                                                   Log.e(TAG, "In-app Billing is set up OK");
                                                   //consumeItem();
                                               }
                                           }
                                       });
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void queryPurchasedItems() {
        if (mHelper.isSetupDone() && !mHelper.isAsyncInProgress()) {
            try {
                mHelper.queryInventoryAsync(mReceivedInventoryListener);
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }

    public void startPayment(View view) {
        if (edtEmail.getText().toString().trim().length() == 0) {
            Toast.makeText(MainActivity.this, "Enter your email id", Toast.LENGTH_SHORT).show();
            return;
        }

        if (edtName.getText().toString().trim().length() == 0) {
            Toast.makeText(MainActivity.this, "Enter your name", Toast.LENGTH_SHORT).show();
            return;
        }

        if (edtData.getText().toString().trim().length() == 0) {
            Toast.makeText(MainActivity.this, "Enter Mobile Number/ E-mail id/ BHIM UPI ID/ Bank Details", Toast.LENGTH_SHORT).show();
            return;
        }

        if (edtEmail.getText().toString().trim().length() == 0) {
            Toast.makeText(MainActivity.this, "", Toast.LENGTH_SHORT).show();
            return;
        }
        if (rgDay4.isChecked())
            DaysTxt = "4";

        if (rgDay5.isChecked())
            DaysTxt = "5";

        if (rgDay6.isChecked())
            DaysTxt = "6";

        if (rgDay7.isChecked())
            DaysTxt = "7";


        if (rbType1.isChecked())
            paymentMode = rbType1.getText().toString().trim();

        if (rbType2.isChecked())
            paymentMode = rbType2.getText().toString().trim();

        if (rbType3.isChecked())
            paymentMode = rbType3.getText().toString().trim();

        if (rbType4.isChecked())
            paymentMode = rbType4.getText().toString().trim();

        if (rbType5.isChecked())
            paymentMode = rbType5.getText().toString().trim();

        if (rbType6.isChecked())
            paymentMode = rbType6.getText().toString().trim();

        if (rbType7.isChecked())
            paymentMode = rbType7.getText().toString().trim();

        if (rbType8.isChecked())
            paymentMode = rbType8.getText().toString().trim();


//        Log.e("sku", amountCode.get(spinnerAmount.getSelectedItemPosition()));
        if (mHelper != null) mHelper.flagEndAsync();
        try {
            isInAppInProgree = true;
            mHelper.launchPurchaseFlow(MainActivity.this, amountCode.get(spinnerAmount.getSelectedItemPosition()), INAPP_SUCECESS_RESPONSE_CODE,
                    mPurchaseFinishedListener, "mypurchasetoken");
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Override
    public void onBackPressed() {
        if (!isInAppInProgree)
            finish();
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode,
                                    Intent data) {
        if (!mHelper.handleActivityResult(requestCode,
                resultCode, data)) {
            super.onActivityResult(requestCode, resultCode, data);
//            Log.e(TAG, "onActivityResult" + requestCode + data);
        } else {
            String purchaseData = data.getStringExtra(RESPONSE_INAPP_PURCHASE_DATA);
            String dataSignature = data.getStringExtra(RESPONSE_INAPP_SIGNATURE);
            if (purchaseData != null && purchaseData.length() > 0) {
                getPuchasedData(purchaseData);
            }
            Log.i(TAG, "onActivityResult handled by IABUtil." + purchaseData + dataSignature);
        }
    }

    private void getPuchasedData(String purchaseData) {
//        Log.e("check", purchaseData);
        try {
            JSONObject jsonObject = new JSONObject(purchaseData);
            if (jsonObject.has("orderId")) {
                TransactionRefID = String.valueOf(jsonObject.get("orderId"));
            }
            if (jsonObject.has("purchaseTime")) {
                TransactionDate = String.valueOf(jsonObject.get("purchaseTime"));
            }
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }


    public void postData(final String emailTxt,
                         final String countryTxt,
                         final String nameTxt,
                         final String payment_modeTxt,
                         final String amountTxt,
                         final String dataTxt,
                         final String daysTxt,
                         final String suggestionTxt,
                         final String transactionIdTxt) {

        StringRequest request = new StringRequest(
                Request.Method.POST,
                url,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
//                        Log.e("TAG", "Response: " + response);
                        if (response.length() > 0) {
                            Toast.makeText(MainActivity.this, "Successfully Posted", Toast.LENGTH_LONG).show();
                            edtName.setText("");
                            edtData.setText("");
                            edtEmail.setText("");
                            edtSuggestion.setText("");
                            spinnerAmount.setSelection(0);

                            showInterstitial();

                        } else {
                            Toast.makeText(MainActivity.this, "try again", Toast.LENGTH_LONG).show();
                        }
                    }
                }, new Response.ErrorListener() {

            @Override
            public void onErrorResponse(VolleyError error) {
            }
        }) {
            @Override
            protected Map<String, String> getParams() {
                Map<String, String> params = new HashMap<>();
                params.put(email, emailTxt);
                params.put(country, countryTxt);
                params.put(name, nameTxt);
                params.put(payment_mode, payment_modeTxt);
                params.put(amount, amountTxt);
                params.put(data, dataTxt);
                params.put(days, daysTxt);
                params.put(suggestion, suggestionTxt);
                params.put(transaction_id, transactionIdTxt);
                params.put(userId, PrefUtils.getEmailID(MainActivity.this));

                return params;
            }
        };
        request.setRetryPolicy(new DefaultRetryPolicy(
                50000,
                DefaultRetryPolicy.DEFAULT_MAX_RETRIES,
                DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));
        queue.add(request);
    }


    public void setUpDrawer() {
        mDrawerToggle = new ActionBarDrawerToggle(this, mDrawerLayout, toolbar, R.string.open, R.string.close) {
            @Override
            public void onDrawerOpened(View drawerView) {
                super.onDrawerOpened(drawerView);
                invalidateOptionsMenu();
            }

            @Override
            public void onDrawerClosed(View drawerView) {
                super.onDrawerClosed(drawerView);
                invalidateOptionsMenu();
            }

            @Override
            public void onDrawerSlide(View drawerView, float slideOffset) {
                if (slideOffset < 0.6) {
                    //      toolbar.setAlpha(1 - slideOffset);
                }
            }
        };

        mDrawerLayout.addDrawerListener(mDrawerToggle);

        mDrawerLayout.post(new Runnable() {
            @Override
            public void run() {
                mDrawerToggle.syncState();
            }
        });

        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (!mDrawerLayout.isDrawerOpen(Gravity.START)) {
                    mDrawerLayout.openDrawer(Gravity.START);
                }
            }
        });
    }


    private void loadInterstitialAds() {
        mInterstitialAd = new InterstitialAd(this);
        mInterstitialAd.setAdUnitId(getString(R.string.inter_ad));
        interAdRequest = new AdRequest.Builder().build();
        mInterstitialAd.loadAd(interAdRequest);
    }

    private void showInterstitial() {
        if (mInterstitialAd.isLoaded()) {
            mInterstitialAd.show();
        }
    }

    private void showbannerAds() {
        AdView mAdView = (AdView) findViewById(R.id.ad);
        AdRequest adRequest = new AdRequest.Builder().build();
        mAdView.loadAd(adRequest);
    }

}
