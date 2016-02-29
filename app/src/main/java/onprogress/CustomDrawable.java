package onprogress;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.ColorFilter;
import android.graphics.Paint;
import android.graphics.drawable.Animatable;
import android.graphics.drawable.Drawable;
import android.os.SystemClock;
import android.view.animation.DecelerateInterpolator;
import android.view.animation.Interpolator;

/**
 * Created by kushagarlall on 29/02/16.
 */
public class CustomDrawable extends Drawable implements Animatable {
    private static final long FRAME_DURATION = 1000 / 60;
    private final static float OFFSET_PER_FRAME = 0.01f;
    private final int mStartSection;

    private Interpolator mInterpolator;
    private Paint mPaint;
    private int[] mColors;
    private int mColorsIndex;
    private boolean mRunning;
    private float mCurrentOffset;
    private int mSectionsCount;
    private float mSpeed;
    private boolean mNewTurn;
    private float mMaxOffset;
    private int mCurrentSections;
    private float mSeparatorLength;
    private float mFinishingOffset;

    private CustomDrawable(Interpolator interpolator, int sectionsCount, int[] colors, float speed) {
        mInterpolator = interpolator;
        mRunning = false;
        mSectionsCount = sectionsCount;
        mStartSection = 0;
        mCurrentSections = mSectionsCount;
        mSpeed = speed;
        mColors = colors;
        mColorsIndex = 0;

        mMaxOffset = 1f / mSectionsCount;

        mPaint = new Paint();
        mPaint.setStyle(Paint.Style.FILL);
        mPaint.setDither(false);
        mPaint.setAntiAlias(true);
    }

    @Override
    public void start() {
        if (isRunning())
            return;

        scheduleSelf(mUpdater, SystemClock.uptimeMillis() + FRAME_DURATION);
        invalidateSelf();
    }

    @Override
    public void scheduleSelf(Runnable what, long when) {
        mRunning = true;
        super.scheduleSelf(what, when);
    }

    @Override
    public void stop() {

    }

    @Override
    public boolean isRunning() {
        return mRunning;
    }

    private final Runnable mUpdater = new Runnable() {

        @Override
        public void run() {

            mCurrentOffset += (OFFSET_PER_FRAME * mSpeed);

            if (mCurrentOffset >= mMaxOffset) {
                mNewTurn = true;
                mCurrentOffset -= mMaxOffset;
            }

            if (isRunning())
                scheduleSelf(mUpdater, SystemClock.uptimeMillis() + FRAME_DURATION);

            invalidateSelf();
        }
    };

    @Override
    public void draw(Canvas canvas) {

        //new turn
        if (mNewTurn) {
            mColorsIndex = decrementColor(mColorsIndex);
            mNewTurn = false;
        }

        if (mCurrentSections < mSectionsCount) {
            mCurrentSections++;
        }
        drawBalls(canvas);
    }

    private void drawBalls(Canvas canvas) {
        float prevValue = 0f;
        int boundsWidth = canvas.getWidth() + 25;
        int width = boundsWidth + mSectionsCount;
        float xSectionWidth = 1f / mSectionsCount;

        float prev;
        float end;
        float spaceLength;
        float xOffset;
        float ratioSectionWidth;
        float sectionWidth;
        float drawLength;

        float xCenter = 0;
        float yCenter = getBounds().centerY();
        int currentIndexColor = mColorsIndex;

        for (int i = 0; i <= mCurrentSections; ++i) {

            xOffset = xSectionWidth * i + mCurrentOffset;
            prev = Math.max(0f, xOffset - xSectionWidth);
            ratioSectionWidth = Math.abs(mInterpolator.getInterpolation(prev) - mInterpolator.getInterpolation(Math.min(xOffset, 1f)));
            sectionWidth = (int) (width * ratioSectionWidth);

            if (sectionWidth + prev < width)
                spaceLength = Math.min(sectionWidth, mSeparatorLength);
            else
                spaceLength = 0f;

            drawLength = sectionWidth > spaceLength ? sectionWidth - spaceLength : 0;
            end = prevValue + drawLength;
            if (end > prevValue) {
                float xFinishingOffset = mInterpolator.getInterpolation(Math.min(mFinishingOffset, 1f));
                xCenter = Math.max(xFinishingOffset * width, Math.min(boundsWidth, prevValue));
                xCenter -= 12;

                drawBall(canvas, xCenter, yCenter, currentIndexColor);
            }

            prevValue = end + spaceLength;
            currentIndexColor = incrementColor(currentIndexColor);
        }
    }

    private void drawBall(Canvas canvas, float xCenter, float yCenter, int currentIndexColor) {
        mPaint.setColor(mColors[currentIndexColor]);
        canvas.drawCircle(xCenter, yCenter, 10, mPaint);
    }

    private int incrementColor(int colorIndex) {
        ++colorIndex;
        if (colorIndex >= mColors.length) colorIndex = 0;
        return colorIndex;
    }

    private int decrementColor(int colorIndex) {
        --colorIndex;
        if (colorIndex < 0) colorIndex = mColors.length - 1;
        return colorIndex;
    }

    @Override
    public void setAlpha(int alpha) {

    }

    @Override
    public void setColorFilter(ColorFilter colorFilter) {

    }

    @Override
    public int getOpacity() {
        return 0;
    }


    /*
    * Builder
    * */
    public static class Builder {
        private Interpolator mInterpolator;
        private int mSectionsCount;
        private int[] mColors;
        private float mSpeed;

        public Builder(Context context) {
            this(context, false);
        }

        public Builder(Context context, boolean editMode) {
            initValues(context, editMode);
        }

        public CustomDrawable build() {
            CustomDrawable ret = new CustomDrawable(mInterpolator, mSectionsCount, mColors, mSpeed);
            return ret;
        }

        private void initValues(Context context, boolean editMode) {
            mInterpolator = new DecelerateInterpolator();
            mSectionsCount = 5;
            mSpeed = 1f;
//            mColors = (context.getResources().getIntArray(R.array.pocket_bar_colors));
        }
    }
}
