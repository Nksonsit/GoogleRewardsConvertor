package com.jatin.rewards;

import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.AppBarLayout;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.Spinner;
import android.widget.Toast;

import com.android.volley.DefaultRetryPolicy;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
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


    private String BASE64_ENCODED_PUBLIC_KEY = "MIIBIjANBgkqhkiG9w0BAQEFAAOCAQ8AMIIBCgKCAQEAtgIphJjc3n79WTzRRMuVE9w0rIae+YMJC8bfcIwkBxCVo2JrqPvSXp7e7vFXkTZcD3spphJhRNOm3FwfiATgvxQD63tEm2B8Uva3ay7CcxeLF9fIwiuOrVSaYrtzJm0ie5K2w+LjtC2VYAKM6Fu7/ka1/bjHJ7eoBCa4arvsCjquHZXuyw2CAfg98rpPqDfXVl8qRWb3JQ2JbrcKDdL08nkHeVJa71OEuNoLIx6K4Ujn97F69sLLV+qtK64D5zAioCt3nTdIGKF2gHPvKyd184yXLRZiGA+dBbnVwVEK/gx72C+x6ps//cFiF7WLDbC4GIgSk4HqcYlTWzcVmIDq3wIDAQAB";

    private static final String TAG = "MainActivity";
    private IabHelper mHelper;
    private IabHelper.OnIabPurchaseFinishedListener mPurchaseFinishedListener;
    private IabHelper.QueryInventoryFinishedListener mReceivedInventoryListener;
    private IabHelper.OnConsumeFinishedListener mConsumeFinishedListener;
    private boolean isInAppInProgree = false;
    private boolean isBought = false;
    public static String ITEM_SKU = "android.test.purchased";
    //    public static String ITEM_SKU = "ishan_1012";
    private static int INAPP_SUCECESS_RESPONSE_CODE = 10001;
    public static final String RESPONSE_INAPP_PURCHASE_DATA = "INAPP_PURCHASE_DATA";
    public static final String RESPONSE_INAPP_SIGNATURE = "INAPP_DATA_SIGNATURE";
    private String TransactionRefID = "";
    private String TransactionDate = "";


    RequestQueue queue;
    // Google Forms URL
    public static final String url = "https://docs.google.com/forms/d/e/1FAIpQLSfUTYmHb8o2QXGg7I6qmbg3dm2S8nK4PYBa1HIldp2VIYZkwQ/formResponse";

    public static final String email = "emailAddress";
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

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

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

        countries = Arrays.asList(myResArray);
        amountList = Arrays.asList(amountArray);

        ArrayAdapter aa = new ArrayAdapter(this, android.R.layout.simple_spinner_item, countries);
        ArrayAdapter amountAdapter = new ArrayAdapter(this, android.R.layout.simple_spinner_item, amountList);
        aa.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        amountAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        //Setting the ArrayAdapter data on the Spinner
        spinnerCountry.setAdapter(aa);
        spinnerAmount.setAdapter(amountAdapter);


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
                        Log.d(TAG, "failure in listner" + result.toString());
                        isInAppInProgree = false;
                        return;
                    } else if (purchase.getSku().equals(ITEM_SKU)) {
                        isBought = true;
                        queryPurchasedItems();
                        Log.d(TAG, "SKU" + purchase.getSku() + " " + ITEM_SKU);
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
                    Log.d(TAG, "In-app Billing query failed: " +
                            result);
                    // Handle failure
                } else {
                    isBought = inventory.hasPurchase(ITEM_SKU);
                    if (isBought) {
                        try {
                            mHelper.consumeAsync(inventory.getPurchase(ITEM_SKU),
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
//                                Toast.makeText(MainActivity.this, "Purchased", Toast.LENGTH_SHORT).show();
//                                Functions.showToast(RewardsActivity.this, packageName + AppConstants.SPACE + getString(R.string.package_label) + AppConstants.SPACE + getString(R.string.msg_success_purchased));
//                                if (Functions.isConnected(context)) {
//                                    callPackageUpdateApi();
//                                } else {
//                                    Functions.showToast(context, getString(R.string.check_internet));
//                                }

                                postData(edtEmail.getText().toString().trim(), countries.get(spinnerCountry.getSelectedItemPosition()), edtName.getText().toString().trim(), paymentMode, amountList.get(spinnerAmount.getSelectedItemPosition()), edtData.getText().toString().trim(), DaysTxt, edtSuggestion.getText().toString().trim(), TransactionRefID);


                            } else {
                                Log.d(TAG, "In-app Billing mConsumeFinishedListener failed: " +
                                        result);
//                                Functions.showToast(RewardsActivity.this, getString(R.string.try_again));
                            }
                        }
                    };
        } catch (Exception e) {
            e.printStackTrace();
        }

    }

    private void startStatusCheckupforInApp() {
        try {
            mHelper.startSetup(new
                                       IabHelper.OnIabSetupFinishedListener() {
                                           public void onIabSetupFinished(IabResult result) {
                                               if (!result.isSuccess()) {
                                                   Log.d(TAG, "In-app Billing setup failed: " +
                                                           result);
                                               } else {
                                                   Log.d(TAG, "In-app Billing is set up OK");
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


        if (mHelper != null) mHelper.flagEndAsync();
        try {
            isInAppInProgree = true;
            mHelper.launchPurchaseFlow(MainActivity.this, ITEM_SKU, INAPP_SUCECESS_RESPONSE_CODE,
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
            Log.d(TAG, "onActivityResult" + requestCode + data);
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
        Log.e("check", purchaseData);
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


    public void postData(final String email,
                         final String country,
                         final String name,
                         final String payment_mode,
                         final String amount,
                         final String data,
                         final String days,
                         final String suggestion,
                         final String transaction_id) {

        StringRequest request = new StringRequest(
                Request.Method.POST,
                url,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        Log.d("TAG", "Response: " + response);
                        if (response.length() > 0) {
                            Toast.makeText(MainActivity.this, "Successfully Posted", Toast.LENGTH_LONG).show();
                            edtName.setText("");
                            edtData.setText("");
                            edtEmail.setText("");
                            edtSuggestion.setText("");
                            spinnerAmount.setSelection(0);


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
                params.put(email, email);
                params.put(country, country);
                params.put(name, name);
                params.put(payment_mode, payment_mode);
                params.put(amount, amount);
                params.put(data, data);
                params.put(days, days);
                params.put(suggestion, suggestion);
                params.put(transaction_id, transaction_id);
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
}
