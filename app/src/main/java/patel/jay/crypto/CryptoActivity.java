package patel.jay.crypto;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.AlertDialog;
import android.content.ClipData;
import android.content.ClipboardManager;
import android.content.DialogInterface;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.text.Editable;
import android.text.InputFilter;
import android.text.TextWatcher;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.kyleduo.switchbutton.SwitchButton;
import com.rengwuxian.materialedittext.MaterialEditText;

import java.util.ArrayList;
import java.util.Arrays;

import static patel.jay.crypto.MyConstant.clearEdittext;
import static patel.jay.crypto.MyConstant.etBlank;
import static patel.jay.crypto.MyConstant.etBlankCheck;

public class CryptoActivity extends AppCompatActivity implements View.OnClickListener {

    Button btnClick;
    LinearLayout layout;
    SwitchButton sbtn;
    MaterialEditText etText, etIv, etKey;
    TextView tvECB, tvCBC, tvCFM, tvOFM, tvCTR;

    Activity activity = CryptoActivity.this;

    TextWatcher watcher = new TextWatcher() {
        @Override
        public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {
        }

        @Override
        public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
            String iv = etIv.getText().toString().trim();
            String key = etKey.getText().toString().trim();
            String[] non = {"2", "3", "4", "5", "6", "7", "8", "9"};

            if (iv.isEmpty()) {
                etIv.setText("0");
            } else if (key.isEmpty()) {
                etKey.setText("0");
            }

            for (String s : non) {
                if (iv.contains(s)) {
                    etIv.setText(iv.replace(s, ""));
                }
                if (key.contains(s)) {
                    etKey.setText(key.replace(s, ""));
                }
            }
        }

        @Override
        public void afterTextChanged(Editable editable) {
        }
    };

    @SuppressLint("SetTextI18n")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_crypto);

        sbtn = findViewById(R.id.sbtn);
        sbtn.setText("Enc", "Dec");
        sbtn.setChecked(true);

        layout = findViewById(R.id.linear);

        etIv = findViewById(R.id.etIv);
        etKey = findViewById(R.id.etKey);
        etText = findViewById(R.id.etText);

        etKey.addTextChangedListener(watcher);
        etIv.addTextChangedListener(watcher);

        tvECB = findViewById(R.id.tvECB);
        tvCBC = findViewById(R.id.tvCBC);
        tvCFM = findViewById(R.id.tvCFM);
        tvOFM = findViewById(R.id.tvOFM);
        tvCTR = findViewById(R.id.tvCTR);

        tvECB.setOnClickListener(this);
        tvCBC.setOnClickListener(this);
        tvCFM.setOnClickListener(this);
        tvOFM.setOnClickListener(this);
        tvCTR.setOnClickListener(this);

        btnClick = findViewById(R.id.btnClick);
        btnClick.setOnClickListener(this);

    }

    @Override
    protected void onStart() {
        super.onStart();

    }

    @Override
    public void onClick(View view) {

        switch (view.getId()) {
            case R.id.tvECB:
            case R.id.tvCBC:
            case R.id.tvCFM:
            case R.id.tvOFM:
            case R.id.tvCTR:
                TextView tv = (TextView) view;
                ClipboardManager clipboard = (ClipboardManager) getSystemService(CLIPBOARD_SERVICE);
                ClipData clip = ClipData.newPlainText("", tv.getText().toString());
                assert clipboard != null;
                clipboard.setPrimaryClip(clip);
                MyConstant.toast(activity, "Copied");
                break;

            case R.id.btnClick:
                try {
                    String text = etText.getText().toString(),
                            iv = etIv.getText().toString(),
                            key = etKey.getText().toString();
                    boolean isValid = true;

                    if (Integer.parseInt(key) == 0) {
                        etKey.setError("All 0 Not Allow");
                        isValid = false;
                    }

                    if (Integer.parseInt(iv) == 0) {
                        etIv.setError("All 0 Not Allow");
                        isValid = false;
                    }

                    if (iv.length() < 4) {
                        etIv.setError("More Than 3 Bits");
                        isValid = false;
                    }

                    if (key.length() < 4) {
                        etKey.setError("More Than 3 Bits");
                        isValid = false;
                    }

                    if (isValid) {
                        encDnc(text, iv, key);
                    }

                } catch (Exception e) {
                    e.printStackTrace();
                    MyConstant.toast(activity, e.getMessage());
                    return;
                }
                break;
        }

    }

    private void encDnc(String text, String iv, String key) {
        boolean isEnDn = sbtn.isChecked();

        Algorithms a = new Algorithms(iv, key, text);

        tvECB.setText(a.ECB());
        tvCBC.setText(a.CBC(isEnDn));
        tvCFM.setText(a.CFM(isEnDn));
        tvOFM.setText(a.OFM());
        tvCTR.setText(a.CTR());
    }

    //region Action Menu
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.dash_board, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {

        int id = item.getItemId();

        if (id == R.id.action_info) {
            alertView();
        }
        return super.onOptionsItemSelected(item);
    }

    private void alertView() {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setMessage(" If You Want To Any Changes Contact Me.\n"
                + "\n Email :- " + MyConstant.EMAILID + " \n");

        builder.setTitle("Contact Us")
                .setCancelable(true)
                .setNeutralButton("Follow On FB", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialoginterface, int i) {
                        Intent browserIntent = new Intent(Intent.ACTION_VIEW, Uri.parse(MyConstant.F_URL));
                        startActivity(browserIntent);
                    }
                })
                .setPositiveButton("Email", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        sendEmail();
                    }
                });

        AlertDialog alert = builder.create();
        alert.show();
    }

    protected void sendEmail() {
        String[] TO = {MyConstant.EMAILID};
        Intent emailIntent = new Intent(Intent.ACTION_SEND);

        emailIntent.setData(Uri.parse("mailto:"));
        emailIntent.setType("text/plain");
        emailIntent.putExtra(Intent.EXTRA_EMAIL, TO);
        emailIntent.putExtra(Intent.EXTRA_SUBJECT, "Subject:");
        emailIntent.putExtra(Intent.EXTRA_TEXT, "");

        try {
            startActivity(Intent.createChooser(emailIntent, "Send mail..."));
            finish();
        } catch (android.content.ActivityNotFoundException ex) {
            Toast.makeText(getApplicationContext(), "There is no email client installed.", Toast.LENGTH_SHORT).show();
        }
    }
    //endregion
}