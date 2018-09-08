package ads;

import android.content.Context;
import android.widget.Toast;

import com.google.android.gms.ads.AdRequest;
import com.google.android.gms.ads.MobileAds;
import com.google.android.gms.ads.reward.RewardItem;
import com.google.android.gms.ads.reward.RewardedVideoAd;
import com.google.android.gms.ads.reward.RewardedVideoAdListener;

/**
 * Created by Houbenz on 06/09/2018.
 */

public class AdListener implements RewardedVideoAdListener {


    private Context context ;
    private RewardedVideoAd rewardedVideoAd ;


    public AdListener(Context context,RewardedVideoAd rewardedVideoAd){
        this.context=context;
        this.rewardedVideoAd=rewardedVideoAd;
    }


    @Override
    public void onRewardedVideoAdLoaded() {
        Toast.makeText(context,"onRewardedVideoAdLoaded",Toast.LENGTH_SHORT).show();
    }

    @Override
    public void onRewardedVideoAdOpened() {
        Toast.makeText(context,"onRewardedVideoAdOpened",Toast.LENGTH_SHORT).show();

    }

    @Override
    public void onRewardedVideoStarted() {
        Toast.makeText(context,"onRewardedVideoStarted",Toast.LENGTH_SHORT).show();
    }

    @Override
    public void onRewardedVideoAdClosed() {
        Toast.makeText(context,"onRewardedVideoAdClosed",Toast.LENGTH_SHORT).show();
        loadRewardVideoAd();
    }

    @Override
    public void onRewarded(RewardItem rewardItem) {
        Toast.makeText(context,"reward : "+rewardItem.getAmount(),Toast.LENGTH_SHORT).show();

    }

    @Override
    public void onRewardedVideoAdLeftApplication() {
        Toast.makeText(context,"onRewardedVideoAdFailedToLoad",Toast.LENGTH_SHORT).show();
    }

    @Override
    public void onRewardedVideoAdFailedToLoad(int i) {
        Toast.makeText(context,"onRewardedVideoAdFailedToLoad",Toast.LENGTH_SHORT).show();
    }

    @Override
    public void onRewardedVideoCompleted() {
        Toast.makeText(context,"onRewardedVideoCompleted",Toast.LENGTH_SHORT).show();
    }


    public void loadRewardVideoAd(){
        rewardedVideoAd=MobileAds.getRewardedVideoAdInstance(context);
        rewardedVideoAd.setRewardedVideoAdListener(this);
        rewardedVideoAd.loadAd("ca-app-pub-3940256099942544/5224354917",new AdRequest.Builder().build());


    }
}
