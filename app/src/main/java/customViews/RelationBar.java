package customViews;

import android.content.Context;
import android.util.AttributeSet;
import android.widget.ProgressBar;

public class RelationBar extends ProgressBar {


    private OnMaxReachedListener mMaxReached;

    public RelationBar(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    public RelationBar(Context context, AttributeSet attrs,OnMaxReachedListener mMaxReached) {
        super(context, attrs);

        this.mMaxReached=mMaxReached;

    }

    public RelationBar(Context context, AttributeSet attrs, int defStyleAttr, OnMaxReachedListener mMaxReached) {
        super(context, attrs, defStyleAttr);
        this.mMaxReached = mMaxReached;
    }

    public RelationBar(Context context, AttributeSet attrs, int defStyleAttr, int defStyleRes, OnMaxReachedListener mMaxReached) {
        super(context, attrs, defStyleAttr, defStyleRes);
        this.mMaxReached = mMaxReached;
    }

    public RelationBar(Context context, OnMaxReachedListener mMaxReached) {
        super(context);
        this.mMaxReached = mMaxReached;
    }


    public interface OnMaxReachedListener{
        void onReachedMax();
    }


    public OnMaxReachedListener getmMaxReached() {
        return mMaxReached;
    }

    public void setMaxReachedListener(OnMaxReachedListener mMaxReached) {
        this.mMaxReached = mMaxReached;
    }




    @Override
    public synchronized void setProgress(int progress) {
        super.setProgress(progress);
        if(this.getProgress()==this.getMax())
            mMaxReached.onReachedMax();
    }
}
