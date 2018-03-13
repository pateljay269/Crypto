package patel.jay.crypto;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.webkit.WebView;

public class WelcomeActivity extends Activity {

    private WebView wv;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_welcome);

        wv = findViewById(R.id.wv1);

    }

    @Override
    protected void onStart() {
        super.onStart();
        wv.loadUrl("file:///android_asset/crypto1.gif");
        wv.getSettings().setLoadWithOverviewMode(true);
        wv.getSettings().setUseWideViewPort(true);
        Animations.Scale(wv, 2000);

        new CountDownTimer(5000, 1000) {
            public void onFinish() {
                startActivity(new Intent(getApplicationContext(), CryptoActivity.class));
                finish();
            }

            public void onTick(long millisUntilFinished) {
            }

        }.start();
    }

}
