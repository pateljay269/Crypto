package patel.jay.crypto;

import android.app.Activity;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.Toast;

import java.util.Random;

/**
 * Created by Jay on 19-Feb-18.
 */

public class MyConstant {

    public static final String F_URL = "https://www.facebook.com/pateljay269";
    public static final String EMAILID = "pateljay269@yahoo.com";

    public static void toast(Activity activity, String msg) {
        Toast.makeText(activity, msg, Toast.LENGTH_SHORT).show();
    }

    public static void etBlankCheck(EditText et) {
        if (etBlank(et))
            et.setError("Required");
    }

    public static boolean etBlank(EditText et) {
        return et.getText().toString().isEmpty();
    }

    public static void clearEdittext(ViewGroup group) {
        for (int i = 0; i < group.getChildCount(); ++i) {
            View view = group.getChildAt(i);
            if (view instanceof EditText) {
                ((EditText) view).setText("");
            } else if (view instanceof ViewGroup && (((ViewGroup) view).getChildCount() > 0))
                clearEdittext((ViewGroup) view);
        }
    }

    public static int randInt(int min, int max) {
        return new Random().nextInt((max - min) + 1) + min;
    }
}
