package onprogress;

import android.content.Context;
import android.support.design.widget.CoordinatorLayout;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.ViewParent;
import android.widget.FrameLayout;
import android.widget.ProgressBar;

import com.example.akosha.sample1.materialcustomprogressbar.R;

/**
 * Created by kushagarlall on 29/02/16.
 */
public class CustomProgressDialog {
    private final View mView;
    private Context mContext;
    private final ViewGroup mParent;
    public final CustomProgressBar progressBar;

    public CustomProgressDialog(Context context, ViewGroup viewGroup){
        this.mContext = context;
        this.mParent = viewGroup;
        LayoutInflater inflater = LayoutInflater.from(this.mContext);
        this.mView = inflater.inflate(R.layout.progress_layout, this.mParent, false);
        progressBar =(CustomProgressBar) mView.findViewById(R.id.progress_bar);
        progressBar.mParent = viewGroup;
    }

    public static CustomProgressDialog createDialog(Context context , View view){
        CustomProgressDialog customProgressDialog = new CustomProgressDialog(context,findSuitableParent(view));
        return customProgressDialog;
    }

    /**
     * show on Progress
     */
    public void showProgress(){
        showView();
    }

    final void showView() {
        if (this.mView.getParent() == null) {
            this.mParent.addView(this.mView);
        }
    }
    /**
     * hide on Progress
     */
    public void hideProgress(){
        progressBar.dismiss();
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
