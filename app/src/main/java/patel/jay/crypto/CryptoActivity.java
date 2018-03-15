package patel.jay.crypto;

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

import com.rengwuxian.materialedittext.MaterialEditText;

import java.util.ArrayList;
import java.util.Arrays;

import static patel.jay.crypto.MyConstant.clearEdittext;
import static patel.jay.crypto.MyConstant.etBlank;
import static patel.jay.crypto.MyConstant.etBlankCheck;

public class CryptoActivity extends AppCompatActivity implements View.OnClickListener {

    Button btnClick;
    MaterialEditText etText, etIv, etKey;
    TextView tvAns;
    LinearLayout layout;

    Activity activity = CryptoActivity.this;

    TextWatcher watcher = new TextWatcher() {
        @Override
        public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {
        }

        @Override
        public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
            String iv = etIv.getText().toString().trim();
            String key = etKey.getText().toString().trim();

            if (iv.isEmpty()) {
                etIv.setText("0");
            } else if (key.isEmpty()) {
                etKey.setText("0");
            }
        }

        @Override
        public void afterTextChanged(Editable editable) {
        }
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_crypto);

        layout = findViewById(R.id.linear);

        etIv = findViewById(R.id.etIv);
        etKey = findViewById(R.id.etKey);
        etText = findViewById(R.id.etText);

        etKey.addTextChangedListener(watcher);
        etIv.addTextChangedListener(watcher);

        tvAns = findViewById(R.id.tvAns);

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

            case R.id.btnClick:
                try {
                    encDnc();

                } catch (Exception e) {
                    e.printStackTrace();
                    MyConstant.toast(activity, e.getMessage());
                    return;
                }
                break;
        }
    }

    private boolean checkIV(String text) {
        for (int j = 0; j < text.length(); j++) {
            int n = Integer.parseInt(text.charAt(j) + "");
            if (n > 1 || n < 0) {
                return true;
            }
        }
        return false;
    }

    private void encDnc() {
        String output = "",
                text = etText.getText().toString(),
                iv = etIv.getText().toString(),
                key = etKey.getText().toString();
        Algorithms a = new Algorithms();

        tvAns.setText(output);
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

/*
Copy To Clipboard
if (tvAns.getText().toString().trim().isEmpty()) {
                    MyConstant.toast(activity, "Data Is Not Available.");
                } else {
                    ClipboardManager clipboard = (ClipboardManager) getSystemService(CLIPBOARD_SERVICE);
                    ClipData clip = ClipData.newPlainText("", tvAns.getText().toString());
                    assert clipboard != null;
                    clipboard.setPrimaryClip(clip);
                }

 */