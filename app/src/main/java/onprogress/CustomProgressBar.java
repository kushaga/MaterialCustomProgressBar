package onprogress;

import android.app.Activity;
import android.content.Context;
import android.util.AttributeSet;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.InputMethodManager;
import android.widget.LinearLayout;
import android.widget.ProgressBar;

import com.example.akosha.sample1.materialcustomprogressbar.R;

/**
 * Created by kushagarlall on 29/02/16.
 */
public class CustomProgressBar extends ProgressBar {
    private Context mContext;
    public static ViewGroup mParent;
    private CircularProgressDrawable drawable;

    public CustomProgressBar(Context context) {
        super(context);
        mContext = context;
        init(context);
    }


    public CustomProgressBar(Context context , View view){
        super(context);

    }

    public CustomProgressBar(Context context, AttributeSet attrs) {
        super(context, attrs);
        mContext = context;
        init(context);
    }


    public CustomProgressBar(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        mContext = context;
        init(context);
    }

    public void dismiss() {
        drawable.stop();
    }

    public void show() {
        if(this.getParent()==null){
            LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(300, 10);
            this.setLayoutParams(params);
            this.mParent.addView(this);
        }
        hideKeyboard(mContext);
        drawable.start();
    }


    private void init(Context context) {
        drawable = new CircularProgressDrawable(getResources().getColor(R.color.otp_state_verifying), 10);
        setIndeterminateDrawable(drawable);
    }

    public static void hideKeyboard(Context ctx) {
        InputMethodManager inputManager = (InputMethodManager) ctx
                .getSystemService(Context.INPUT_METHOD_SERVICE);

        if (!(ctx instanceof Activity)) {
            return;
        }
        // check if no view has focus:
        View v = ((Activity) ctx).getCurrentFocus();
        if (v == null)
            return;

        inputManager.hideSoftInputFromWindow(v.getWindowToken(), 0);
    }
}
