package fragments;


import android.app.AlertDialog;
import android.app.Dialog;
import android.net.Uri;
import android.os.Bundle;
import androidx.fragment.app.Fragment;

import android.os.CountDownTimer;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.android.houbenz.lifesimulator.R;
import com.google.android.gms.ads.AdRequest;
import com.google.android.gms.ads.MobileAds;
import com.google.android.gms.ads.reward.RewardItem;
import com.google.android.gms.ads.reward.RewardedVideoAd;
import com.google.android.gms.ads.reward.RewardedVideoAdListener;
import com.houbenz.lifesimulator.GameScene;
import com.houbenz.lifesimulator.MainMenu;

import java.util.ArrayList;
import java.util.List;

import androidx.lifecycle.ViewModelProviders;
import conf.Params;
import customViews.RelationBar;
import database.Gift;
import database.Partner;
import database.Player;
import viewmodels.ViewModelPartner;

import static com.houbenz.lifesimulator.GameScene.APP_ADS_ID;


public class RelationFragment extends Fragment implements RewardedVideoAdListener {

    private RewardedVideoAd mRewardVideoAd;

    public RelationFragment() {

    }

    private RelationBar relationBar;
    private Button offerGift;
    private Button breakUp;
    private Button goDate;
    private Button mariage;

    private Button adButtonFindPartner;
    private TextView visitText;

    private TextView progressText;
    private Button lookPartner;
    private int dis = 2;
    private View foundPartnerConstraint;
    private Player player1;
    private int slot;
    private int minusRelation;
    private ViewModelPartner viewmodel;
    private CountDownTimer countDownTimer;

    private ImageView partnerImage;
    private TextView partnerName;

    private int dateNumber=0;


    private boolean cancelTimer =false;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View fragment = inflater.inflate(R.layout.fragment_relation, container, false);

        lookPartner = fragment.findViewById(R.id.lookPartner);
        foundPartnerConstraint = fragment.findViewById(R.id.foundPartnerConstraint);
        relationBar =fragment.findViewById(R.id.relationProgress);
        offerGift = fragment.findViewById(R.id.offerGift);
        breakUp = fragment.findViewById(R.id.breakUp);
        goDate = fragment.findViewById(R.id.goDate);
        progressText = fragment.findViewById(R.id.progressText);
        visitText=fragment.findViewById(R.id.visittext);
        adButtonFindPartner=fragment.findViewById(R.id.adButtonFindPartner);
        partnerImage=fragment.findViewById(R.id.partnerImage);
        partnerName=fragment.findViewById(R.id.partnerName);
        mariage=fragment.findViewById(R.id.mariage);

        try {
            MobileAds.initialize(getContext(), APP_ADS_ID);

        }catch (Exception e){
            Toast.makeText(getContext(),"not loading :/",Toast.LENGTH_LONG).show();
        }


        mRewardVideoAd =MobileAds.getRewardedVideoAdInstance(getActivity());
        mRewardVideoAd.setRewardedVideoAdListener(this);

        loadRewardAd();

        viewmodel = ViewModelProviders.of(getActivity()).get(ViewModelPartner.class);

        slot = getArguments().getInt("slot");

        player1 = MainMenu.myAppDataBase.myDao().getPlayer(slot);


        //initiate relation progress bar
        if(player1.getRelationBar() < 100){
            relationBar.setProgress(player1.getRelationBar());
            progressText.setText(relationBar.getProgress() + "/" + relationBar.getMax());
        }
        else {
            progressText.setText("100/100");
            relationBar.setProgress(99);
            goDate.setVisibility(View.GONE);
            offerGift.setVisibility(View.GONE);
            mariage.setVisibility(View.VISIBLE);
        }

        Log.i("Uoip",player1.getDating());
        if (player1.getDating().equals("true")) {
            foundPartnerConstraint.setVisibility(View.VISIBLE);
            lookPartner.setVisibility(View.GONE);
            visitText.setVisibility(View.GONE);
            Partner partner=MainMenu.myAppDataBase.myDao().getDatingPartner();

            partnerName.setText(partner.getName());
            partnerImage.setImageURI(Uri.parse(partner.getImage()));

        }


        int min =Params.MIN_TIME_LOOKING_FOR_PARTNER;
        int max = Params.MAX_TIME_LOOKING_FOR_PARTNER;

        int range = max - min + 1;
        int random = (int) (Math.random() * range) + min;

        ArrayList<String > strings =Params.getTexts();

        countDownTimer = new CountDownTimer(random, 3000) {
            @Override
            public void onTick(long millisUntilFinished) {

                int min = 0;
                int max = strings.size() - 1;
                int random = (int) (Math.random() * max) - min;
                visitText.setText(strings.get(random));

                if(cancelTimer) {
                    countDownTimer.cancel();

                    lookPartner.setText("Start looking for a partner");
                    lookPartner.setTextColor(getResources().getColor(R.color.white));
                    visitText.setVisibility(View.GONE);
                    cancelTimer=false;
                }
            }
            @Override
            public void onFinish() {

                List<Partner> partners =MainMenu.myAppDataBase.myDao().getPartners();

                int min = 0 ;
                int max = 3;
                int random = (int)(Math.random() * max) - min;

                foundPartner(partners.get(random));
            }
        };

        lookPartner.setOnClickListener(view -> {
            if (dis % 2 == 0) {
                lookPartner.setText("Stop looking for a partner");
                lookPartner.setTextColor(getResources().getColor(R.color.red));
                dis++;
                visitText.setVisibility(View.VISIBLE);

                countDownTimer.start();

            } else {
                lookPartner.setText("Start looking for a partner");
                lookPartner.setTextColor(getResources().getColor(R.color.white));
                visitText.setVisibility(View.GONE);
                countDownTimer.cancel();
                dis++;
            }
        });


        offerGift.setOnClickListener(view -> {

            Dialog dialog = new Dialog(getActivity());
            dialog.setContentView(R.layout.dialog_gift);

            Button giftRoses = dialog.findViewById(R.id.giftRoses);
            Button giftChocolate = dialog.findViewById(R.id.giftChocolate);
            Button giftJewelery = dialog.findViewById(R.id.giftJewlery);
            TextView rosesNumber = dialog.findViewById(R.id.rosesNumber);
            TextView chocolateNumber = dialog.findViewById(R.id.chocolateNumber);
            TextView jeweleryNumber = dialog.findViewById(R.id.jeweleryNumber);

            TextView relationPlusText=dialog.findViewById(R.id.relationPlusText);

            Gift roses = MainMenu.myAppDataBase.myDao().getRoses();
            Gift jewelry= MainMenu.myAppDataBase.myDao().getJewelry();
            Gift chocolate = MainMenu.myAppDataBase.myDao().getChocolate();

            if (roses.getGiftCount() < 10)
                rosesNumber.setText("0" + roses.getGiftCount());
            else
                rosesNumber.setText(roses.getGiftCount() + "");

            if (jewelry.getGiftCount() < 10)
                jeweleryNumber.setText("0" + jewelry.getGiftCount());
            else
                jeweleryNumber.setText(jewelry.getGiftCount() + "");

            if (chocolate.getGiftCount() < 10)
                chocolateNumber.setText("0" + chocolate.getGiftCount());
            else
                chocolateNumber.setText(chocolate.getGiftCount() + "");

            //GiftRose Button
            giftRoses.setOnClickListener(view1 -> {

                //animation :DDDD

                    int newCount = roses.getGiftCount() - 1;
                    if (newCount >= 0) {
                        rosesNumber.animate().translationY(-50).alpha(0).setDuration(500).withEndAction(() ->{
                        roses.setGiftCount(newCount);
                        MainMenu.myAppDataBase.myDao().updateGift(roses);

                        if (roses.getGiftCount() < 10)
                            rosesNumber.setText("0" + roses.getGiftCount());
                        else
                            rosesNumber.setText(roses.getGiftCount() + "");


                        relationBar.setProgress(relationBar.getProgress() + Params.ROSES_BONUS );
                        progressText.setText(relationBar.getProgress()+"/"+relationBar.getMax());

                         viewmodel.setRelationBar(relationBar.getProgress());

                         player1.setRelationBar(relationBar.getProgress());
                        MainMenu.myAppDataBase.myDao().updatePlayer(player1);
                        rosesNumber.setTranslationY(rosesNumber.getTranslationY()+50);
                        rosesNumber.setAlpha(1f);

                        relationPlusText.setVisibility(View.VISIBLE);
                        relationPlusText.setText("+ "+Params.ROSES_BONUS+" to Relation");
                        relationPlusText.animate().translationY(-100).alpha(0f).setDuration(1000).withEndAction(()->{
                            relationPlusText.setText("");
                            relationPlusText.setVisibility(View.INVISIBLE);
                            relationPlusText.setTranslationY(relationPlusText.getTranslationY()+100);
                            relationPlusText.setAlpha(1f);
                        });
                        });
                    }
            });

            //Gift Chocolate Button
            giftChocolate.setOnClickListener(view2 -> {


                    int newCount = chocolate.getGiftCount() - 1;

                    if (newCount >= 0) {
                        chocolateNumber.animate().translationY(-50).alpha(0).setDuration(500).withEndAction(() ->{
                        chocolate.setGiftCount(newCount);
                        MainMenu.myAppDataBase.myDao().updateGift(chocolate);

                        if (chocolate.getGiftCount() < 10)
                            chocolateNumber.setText("0" + chocolate.getGiftCount());
                        else
                            chocolateNumber.setText(chocolate.getGiftCount() + "");

                        relationBar.setProgress(relationBar.getProgress() + Params.CHOCOLATE_BONUS );
                        progressText.setText(relationBar.getProgress()+"/"+relationBar.getMax());
                        player1.setRelationBar(relationBar.getProgress());


                            viewmodel.setRelationBar(relationBar.getProgress());

                            MainMenu.myAppDataBase.myDao().updatePlayer(player1);

                        chocolateNumber.setTranslationY(chocolateNumber.getTranslationY()+50);
                        chocolateNumber.setAlpha(1f);

                        relationPlusText.setVisibility(View.VISIBLE);
                        relationPlusText.setText("+ "+Params.CHOCOLATE_BONUS+" to Relation");
                        relationPlusText.animate().translationY(-100).alpha(0f).setDuration(1000).withEndAction(()->{

                            relationPlusText.setText("");
                            relationPlusText.setVisibility(View.INVISIBLE);
                            relationPlusText.setTranslationY(relationPlusText.getTranslationY()+100);
                            relationPlusText.setAlpha(1f);
                        });
                        });
                    }
            });

            //Gift Jewelry Button
            giftJewelery.setOnClickListener(view3 -> {

                    int newCount = jewelry.getGiftCount() - 1;

                    if (newCount >= 0) {

                        jeweleryNumber.animate().translationY(-50).alpha(0).setDuration(500).withEndAction(() ->{
                        jewelry.setGiftCount(newCount);
                        MainMenu.myAppDataBase.myDao().updateGift(jewelry);

                        if (jewelry.getGiftCount() < 10)
                            jeweleryNumber.setText("0" + jewelry.getGiftCount());
                        else
                            jeweleryNumber.setText(jewelry.getGiftCount() + "");

                        relationBar.setProgress(relationBar.getProgress() + Params.JEWELRY_BONUS );

                        progressText.setText(relationBar.getProgress()+"/"+relationBar.getMax());
                        player1.setRelationBar(relationBar.getProgress());

                        viewmodel.setRelationBar(relationBar.getProgress());

                        MainMenu.myAppDataBase.myDao().updatePlayer(player1);

                        jeweleryNumber.setTranslationY(jeweleryNumber.getTranslationY()+50);
                        jeweleryNumber.setAlpha(1f);

                        relationPlusText.setVisibility(View.VISIBLE);
                        relationPlusText.setText("+ "+Params.JEWELRY_BONUS+" to Relation");
                        relationPlusText.animate().translationY(-100).alpha(0f).setDuration(1000).withEndAction(()->{

                            relationPlusText.setText("");
                            relationPlusText.setVisibility(View.INVISIBLE);
                            relationPlusText.setTranslationY(relationPlusText.getTranslationY()+100);
                            relationPlusText.setAlpha(1f);
                        });
                        });
                    }
            });

            if(relationBar.getProgress() == relationBar.getMax())
                dialog.dismiss();

            dialog.show();
        });

        breakUp.setOnClickListener(view -> {

            AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());

            Partner partner=MainMenu.myAppDataBase.myDao().getDatingPartner();

            if(partner !=null) {

                builder.setMessage("Do you really want to break up with "+partner.getName()+" ?")
                        .setPositiveButton("Yes", ((dialog, which) -> {

                            player1.setDating("false");
                            player1.setRelationBar(0);
                            relationBar.setProgress(0);
                            progressText.setText(relationBar.getProgress() + "/100");

                            viewmodel.setRelationBar(relationBar.getProgress());

                            MainMenu.myAppDataBase.myDao().updatePlayer(player1);

                            partner.setDating("false");
                            MainMenu.myAppDataBase.myDao().updatePartner(partner);

                            lookPartner.setVisibility(View.VISIBLE);
                            adButtonFindPartner.setVisibility(View.VISIBLE);
                            foundPartnerConstraint.setVisibility(View.GONE);
                            lookPartner.setText("Start looking for a partner");
                            lookPartner.setTextColor(getResources().getColor(R.color.white));
                            dis = 2;

                            viewmodel.setBreakUp(true);

                            loadRewardAd();
                        })).setNegativeButton("No", ((dialog, which) -> {
                    dialog.cancel();
                    dialog.dismiss();
                }));

                AlertDialog alertDialog = builder.create();
                alertDialog.show();
            }
        });


        goDate.setOnClickListener(view ->{

            if(dateNumber >2){

                Toast.makeText(getContext(),"You've gone in too much dates lately ",Toast.LENGTH_LONG).show();

            }
            else {
                Dialog dialog = new Dialog(getActivity());
                dialog.setContentView(R.layout.dialog);
                TextView dialogTitle = dialog.findViewById(R.id.dialogTitle);
                Button cofirm = dialog.findViewById(R.id.confirm);
                Button decline = dialog.findViewById(R.id.decline);

                int randCost = (int) (Math.random() * 250) + 50;

                dialogTitle.setText("this date will cost you " + randCost + "$, proceed ?");

                cofirm.setOnClickListener(v -> {
                    viewmodel.setGoDate(randCost);
                    dialog.dismiss();

                    dateNumber++;

                    relationBar.setProgress(relationBar.getProgress() + 10);

                    progressText.setText(relationBar.getProgress() + "/" + relationBar.getMax());
                    player1.setRelationBar(relationBar.getProgress());


                    viewmodel.setRelationBar(relationBar.getProgress());

                    MainMenu.myAppDataBase.myDao().updatePlayer(player1);

                });

                decline.setOnClickListener(v1 -> {
                    dialog.cancel();
                });

                dialog.setOnDismissListener(dialog1 -> {
                    dialog.cancel();
                });

                dialog.show();
            }
        });

        adButtonFindPartner.setOnClickListener(v -> {

            if(mRewardVideoAd.isLoaded()){
                mRewardVideoAd.show();
            }
        });



        mariage.setOnClickListener(view -> {

                    Dialog dialog = new Dialog(getActivity());
                    dialog.setContentView(R.layout.mariage_dialog);
                    Button offerRingButton = dialog.findViewById(R.id.offerRingbutton);
                    TextView numberRing = dialog.findViewById(R.id.numberRing);


                    Gift jewelry = MainMenu.myAppDataBase.myDao().getJewelry();
                    numberRing.setText(""+jewelry.getGiftCount());

                    offerRingButton.setOnClickListener(view1 -> {

                        int newCount = jewelry.getGiftCount() - 1;

                        if (newCount >= 0) {

                            numberRing.animate().translationY(-50).alpha(0).setDuration(500).withEndAction(() ->{
                                jewelry.setGiftCount(newCount);
                                MainMenu.myAppDataBase.myDao().updateGift(jewelry);

                                if (jewelry.getGiftCount() < 10)
                                    numberRing.setText("0" + jewelry.getGiftCount());
                                else
                                    numberRing.setText(jewelry.getGiftCount() + "");

                                MainMenu.myAppDataBase.myDao().updatePlayer(player1);

                                numberRing.setTranslationY(numberRing.getTranslationY()+50);
                                numberRing.setAlpha(1f);

                                dialog.dismiss();
                                marriedDialog();
                            });

                            viewmodel.setMarried(true);
                        }
                    });
                    dialog.show();
                });


        relationBar.setMaxReachedListener(()->{
            Toast.makeText(getContext(),"You Reached Max Congratulation !! " +
                    "progress ="+ relationBar.getProgress(),Toast.LENGTH_LONG).show();
        });


        return fragment;
    }


    private void marriedDialog(){

        Dialog dialog = new Dialog(getActivity());
        dialog.setContentView(R.layout.congrat_married);
        TextView text = dialog.findViewById(R.id.cong_text);
        text.setText("Congratulations You're now married !");
        dialog.show();

    }

    private void foundPartner(Partner partner){

        Log.i("Uoip","Im executed");
            Dialog dialog = new Dialog(getActivity());
            dialog.setContentView(R.layout.found_partner);
            Button confirm = dialog.findViewById(R.id.confirm);
            Button cancel = dialog.findViewById(R.id.cancel);
            ImageView partnerImage = dialog.findViewById(R.id.partnerImage);
            TextView title = dialog.findViewById(R.id.title);

            partnerImage.setImageURI(Uri.parse(partner.getImage()));
            title.setText("Congratulations you've met " + partner.getName() + " !");

            cancel.setOnClickListener(view -> {
                dialog.dismiss();
                dialog.cancel();

                lookPartner.setText("Start looking for a partner");
                lookPartner.setTextColor(getResources().getColor(R.color.white));
                visitText.setVisibility(View.GONE);
                dis = 0;
            });
            confirm.setOnClickListener(view -> {

                this.player1.setDating("true");
                MainMenu.myAppDataBase.myDao().updatePlayer(player1);

                partner.setDating("true");
                MainMenu.myAppDataBase.myDao().updatePartner(partner);

                foundPartnerConstraint.setVisibility(View.VISIBLE);
                lookPartner.setVisibility(View.GONE);
                visitText.setVisibility(View.GONE);
                adButtonFindPartner.setVisibility(View.GONE);
                partnerName.setText(partner.getName());
                this.partnerImage.setImageURI(Uri.parse(partner.getImage()));

                viewmodel.setFoundPartner(true);

                dialog.cancel();
                dialog.dismiss();
            });


            dialog.setOnDismissListener(dialog1 -> {

                lookPartner.setText("Start looking for a partner");
                lookPartner.setTextColor(getResources().getColor(R.color.white));
                visitText.setVisibility(View.GONE);
                dis = 0;
            });

            dialog.show();


        }


    private void loadRewardAd(){
        mRewardVideoAd.loadAd(GameScene.AD_VIDEO_PARTNER_ID, new AdRequest.Builder().build());
    }


    @Override
    public void onRewardedVideoAdLoaded() {
        if(player1.getDating().equals("false")) {
            adButtonFindPartner.setVisibility(View.VISIBLE);
        }
    }

    @Override
    public void onRewardedVideoAdOpened() {

    }

    @Override
    public void onRewardedVideoStarted() {
        countDownTimer.cancel();
    }

    @Override
    public void onRewardedVideoAdClosed() {

        loadRewardAd();
    }

    @Override
    public void onRewarded(RewardItem rewardItem) {

        Toast.makeText(getContext(),"Reward Delivered",Toast.LENGTH_SHORT).show();
      List<Partner> partners =MainMenu.myAppDataBase.myDao().getPartners();

      Log.i("Uoip","num : "+partners.size());
        int min = 0 ;
        int max = 3;
        int random = (int)(Math.random() * max) - min;
        foundPartner(partners.get(random));
    }

    @Override
    public void onRewardedVideoAdLeftApplication() {

    }

    @Override
    public void onRewardedVideoAdFailedToLoad(int i) {
        loadRewardAd();
    }

    @Override
    public void onRewardedVideoCompleted() {

    }


    @Override
    public void onDetach() {
        countDownTimer.cancel();
        countDownTimer=null;
        super.onDetach();
    }


}


