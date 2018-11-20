package com.jatin.rewards;

import android.app.Activity;
import android.app.Dialog;
import android.graphics.Bitmap;
import android.graphics.drawable.ColorDrawable;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.ProgressBar;
import android.widget.TextView;

public class WebViewDialog {

    // private variables.
    private Activity mActivity;
    private Dialog mDialog;
    private ProgressBar progressbar;

    public WebViewDialog(Activity baseActivity, String url, String title) {
        mActivity = baseActivity;
        createDialog(url, title);
    }

    private void createDialog(String url, String title) {
        mDialog = new Dialog(mActivity);
        mDialog.getWindow().setBackgroundDrawable(new ColorDrawable(0x00000000));
        mDialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        mDialog.setContentView(R.layout.dialog_web_view);
        WindowManager.LayoutParams lp = new WindowManager.LayoutParams();
        lp.copyFrom(mDialog.getWindow().getAttributes());
        lp.width = WindowManager.LayoutParams.MATCH_PARENT;
        lp.height = WindowManager.LayoutParams.WRAP_CONTENT;
        mDialog.getWindow().setAttributes(lp);
        ((TextView) mDialog.findViewById(R.id.dialog_tv_title)).setText(title);
        WebView webView = (WebView) mDialog.findViewById(R.id.fragment_setting_terms_and_conditions_wv);
        webView.getSettings().setJavaScriptEnabled(true);
        webView.loadUrl(url);
        webView.setWebViewClient(new WebViewClient() {
            @Override
            public void onPageFinished(WebView view, String url) {
                super.onPageFinished(view, url);
                try {

                    if (mDialog.isShowing() && mDialog != null) {
//                        mDialog.dismiss();
                    }
                    System.gc();
                    Runtime.getRuntime().gc();

                } catch (Exception e) {
                    e.printStackTrace();
                }
            }

            @Override
            public void onPageStarted(WebView view, String url, Bitmap favicon) {
                super.onPageStarted(view, url, favicon);
                if (mDialog != null) {
                    try {
                        if (mDialog.isShowing()) {
//                            mDialog.dismiss();
                        }
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                }

                mDialog.show();
            }
        });

        mDialog.findViewById(R.id.pop_window_cancel_tv).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
//                Functions.hideKeyPad(mActivity, v);
                mDialog.dismiss();
            }
        });
        show();
    }

    /**
     * Show.
     */
    public void show() {
        mDialog.show();
    }
}
