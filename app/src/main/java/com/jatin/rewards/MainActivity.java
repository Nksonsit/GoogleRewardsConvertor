package com.jatin.rewards;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
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

import java.util.HashMap;
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

    // Google Form's Column ID
    public static final String nameField = "entry.1317516008";
    public static final String phoneField = "entry.1563719254";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);


        // Initializing Queue for Volley
        queue = Volley.newRequestQueue(getApplicationContext());

//        postData("ishan", "soni");

        /*
        emailAddress
entry.1372464918  country
entry.1374722863  name
entry.403838916 payment mode
entry.1622831794 amount
entry.807570936 mobile num/bhim upi
entry.2019417577 days
entry.423242404 suggestion (optional)
entry.367219391 transaction id
        */

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
                                Toast.makeText(MainActivity.this, "Purchased", Toast.LENGTH_SHORT).show();
//                                Functions.showToast(RewardsActivity.this, packageName + AppConstants.SPACE + getString(R.string.package_label) + AppConstants.SPACE + getString(R.string.msg_success_purchased));
//                                if (Functions.isConnected(context)) {
//                                    callPackageUpdateApi();
//                                } else {
//                                    Functions.showToast(context, getString(R.string.check_internet));
//                                }
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


    public void postData(final String name, final String phone) {
        StringRequest request = new StringRequest(
                Request.Method.POST,
                url,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        Log.d("TAG", "Response: " + response);
                        if (response.length() > 0) {
                            Toast.makeText(MainActivity.this, "Successfully Posted", Toast.LENGTH_LONG).show();
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
                params.put(nameField, name);
                params.put(phoneField, phone);
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
