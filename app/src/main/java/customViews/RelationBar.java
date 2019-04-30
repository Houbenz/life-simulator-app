package customViews;

import android.content.Context;
import android.util.AttributeSet;
import android.widget.ProgressBar;

public class RelationBar extends ProgressBar {

    public RelationBar(Context context, AttributeSet attrs,OnMaxReachedListener mMaxReached) {
        super(context, attrs);

        this.mMaxReached=mMaxReached;

    }

    private OnMaxReachedListener mMaxReached;


    public interface OnMaxReachedListener{
        void onReachedMax();
    }

    public void setOnMaxReachedListener(OnMaxReachedListener mMaxReached){
        this.mMaxReached=mMaxReached;
    }


    @Override
    public synchronized void setProgress(int progress) {
        super.setProgress(progress);
        if(this.getProgress()==this.getMax())
            mMaxReached.onReachedMax();
    }
}
