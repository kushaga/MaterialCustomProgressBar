package onprogress;

import android.app.Activity;
import android.content.Context;
import android.support.design.widget.CoordinatorLayout;
import android.util.AttributeSet;
import android.view.View;
import android.view.ViewGroup;
import android.view.ViewParent;
import android.view.inputmethod.InputMethodManager;
import android.widget.FrameLayout;
import android.widget.ProgressBar;

import com.example.akosha.sample1.materialcustomprogressbar.R;

/**
 * Created by kushagarlall on 29/02/16.
 */
public class CustomProgressBar extends ProgressBar {
    private Context mContext;
    private static ViewGroup mParent;
    private CircularProgressDrawable drawable;

    public CustomProgressBar(Context context) {
        super(context);
        mContext = context;
        init(context);
    }

    public CustomProgressBar(Context context, AttributeSet attrs) {
        super(context, attrs);
        mContext = context;
    }


    public CustomProgressBar(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        mContext = context;
    }



    public static CustomProgressBar create(Context context, View view) {
        CustomProgressBar bar = new CustomProgressBar(context);
        mParent = findSuitableParent(view);
        return bar;
    }

//    public CustomProgressBar(Context context, AttributeSet attrs, int defStyleAttr, int defStyleRes) {
//        super(context, attrs, defStyleAttr, defStyleRes);
//    }

    public void dismiss() {
        drawable.stop();
    }

    public void show() {
        if(this.getParent()==null){
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

    private static ViewGroup findSuitableParent(View view) {
        ViewGroup fallback = null;

        do {
            if (view instanceof CoordinatorLayout) {
                return (ViewGroup) view;
            }

            if (view instanceof FrameLayout) {
                if (view.getId() == 16908290) {
                    return (ViewGroup) view;
                }

                fallback = (ViewGroup) view;
            }

            if (view != null) {
                ViewParent parent = view.getParent();
                view = parent instanceof View ? (View) parent : null;
            }
        } while (view != null);

        return fallback;
    }
}
