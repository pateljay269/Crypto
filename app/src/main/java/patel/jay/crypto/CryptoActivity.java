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

public class CryptoActivity extends AppCompatActivity implements View.OnClickListener, AdapterView.OnItemSelectedListener {

    Button btnClick, btnCopy, btnClear;
    MaterialEditText etText, etIv, etKey;
    TextView tvAns;
    Spinner spnType;
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

    String[] str = {"ECB", "CFM", "OFM", "CBC", "CTR"};

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_crypto);

        layout = findViewById(R.id.linear);
        spnType = findViewById(R.id.spnType);

        etIv = findViewById(R.id.etIv);
        etKey = findViewById(R.id.etKey);
        etText = findViewById(R.id.etText);

        etKey.addTextChangedListener(watcher);
        etIv.addTextChangedListener(watcher);

        tvAns = findViewById(R.id.tvAns);

        btnClick = findViewById(R.id.btnClick);
        btnCopy = findViewById(R.id.btnCopy);
        btnClear = findViewById(R.id.btnClear);

        btnClick.setOnClickListener(this);
        btnCopy.setOnClickListener(this);
        btnClear.setOnClickListener(this);

        spnType.setOnItemSelectedListener(this);

    }

    @Override
    protected void onStart() {
        super.onStart();

        ArrayList<String> type = new ArrayList<>();
        type.add("Select Algorithm");
        type.addAll(Arrays.asList(str));

        ArrayAdapter<String> adapter = new ArrayAdapter<>(activity, android.R.layout.simple_list_item_1, type);
        spnType.setAdapter(adapter);

        Animations.Scale(spnType, 1000);
        Animations.Alpha(btnClear, 1000);
    }

    @Override
    public void onClick(View view) {

        switch (view.getId()) {
            case R.id.btnClear:
                clearEdittext(layout);
                break;

            case R.id.btnCopy:
                if (tvAns.getText().toString().trim().isEmpty()) {
                    MyConstant.toast(activity, "Data Is Not Available.");
                } else {
                    ClipboardManager clipboard = (ClipboardManager) getSystemService(CLIPBOARD_SERVICE);
                    ClipData clip = ClipData.newPlainText("", tvAns.getText().toString());
                    assert clipboard != null;
                    clipboard.setPrimaryClip(clip);
                }
                break;

            case R.id.btnClick:
                try {
                    boolean isValid = true;
                    String iv = etIv.getText().toString().trim();
                    String key = etKey.getText().toString().trim();
                    String temp = spnType.getSelectedItem().toString();

                    if (checkIV(iv) || checkIV(key) || etBlank(etText)) {
                        if (checkIV(iv)) {
                            etIv.setError("O & 1 Allow");
                        }

                        if (checkIV(key)) {
                            etKey.setError("O & 1 Allow");
                        }

                        etBlankCheck(etText);
                        isValid = false;
                    }

                    if (!temp.equals("ECB")) {

                        if (key.length() < 4) {
                            etKey.setError("Enter Bits More Than 3");
                            isValid = false;
                        }
                        if (Integer.parseInt(key) == 0) {
                            etKey.setError("All 0 Not Allow");
                            isValid = false;
                        }
                    }

                    if (iv.length() < 4) {
                        etIv.setError("Enter Bits More Than 3");
                        isValid = false;
                    }

                    if (temp.equals("CTR")) {
                        if (iv.length() != 4) {
                            etIv.setError("Enter 4 Bits");
                            isValid = false;
                        }
                    } else {
                        if (Integer.parseInt(iv) == 0) {
                            etIv.setError("All 0 Not Allow");
                            isValid = false;
                        }
                    }

                    if (isValid) {
                        encDnc();
                    }

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

        switch (spnType.getSelectedItem().toString()) {
            case "ECB":
                output = a.ECB(text, iv);
                break;

            case "CFM":
                output = a.CFM(text, iv, key);
                break;

            case "OFM":
                output = a.OFM(text, iv, key);
                break;

            case "CBC":
                output = a.CBC(text, iv, key);
                break;

            case "CTR":
                output = a.CTR(text, iv, key);
                break;

            default:
                output = "";
                break;

        }

        tvAns.setText(output);
    }

    @Override
    public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {

        int max = 10;
        String hint = "IV";

        if (i == 0) {
            layout.setVisibility(View.GONE);
        } else {
            layout.setVisibility(View.VISIBLE);

            //region Animations
            int random = MyConstant.randInt(1, 9);
            int time = MyConstant.randInt(500, 700);
            switch (random) {
                case 1:
                case 6:
                    Animations.Alpha(layout, time);
                    break;

                case 2:
                case 7:
                    Animations.Scale(layout, time);
                    break;

                case 3:
                case 8:
                    Animations.MultipleAnim(layout, time);
                    break;

                case 4:
                case 9:
                    Animations.Rotate(layout, time);
                    break;

                case 5:
                    Animations.MultipleAnim(layout, time);
                    break;
            }
            //endregion
        }

        if (adapterView.getSelectedItem().toString().equalsIgnoreCase("ECB")) {
            etKey.setVisibility(View.GONE);
        } else if (adapterView.getSelectedItem().toString().equalsIgnoreCase("CTR")) {
            max = 4;
            hint = "Nonce";
            etKey.setVisibility(View.VISIBLE);
        } else {
            etKey.setVisibility(View.VISIBLE);
        }

        tvAns.setText("");
        etIv.setHint("Enter " + hint);
        etIv.setFloatingLabelText("Enter " + hint);
        etIv.setFilters(new InputFilter[]{new InputFilter.LengthFilter(max)});
    }

    @Override
    public void onNothingSelected(AdapterView<?> adapterView) {
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
