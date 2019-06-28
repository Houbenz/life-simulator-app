package fragments;


import android.app.AlertDialog;
import android.app.Dialog;
import android.content.Context;
import android.content.SharedPreferences;
import android.net.Uri;
import android.os.Bundle;
import androidx.fragment.app.Fragment;

import android.os.CountDownTimer;
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
import smartdevelop.ir.eram.showcaseviewlib.GuideView;
import viewmodels.ViewModelPartner;

import static com.houbenz.lifesimulator.GameScene.APP_ADS_ID;


public class RelationFragment extends Fragment implements RewardedVideoAdListener {

    private RewardedVideoAd mRewardVideoAd;

    public RelationFragment() {

    }
    private SharedPreferences sharedPreferences;
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
    private ImageView mariagePhotos;
    private TextView partnerName;
    private boolean dateAd=false;

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
        mariagePhotos=fragment.findViewById(R.id.mariagephotos);


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
            if(player1.getMarried().equals("true")) {

                mariagePhotos.setVisibility(View.VISIBLE);
            }
        }
        else {
            progressText.setText("100/100");
            relationBar.setProgress(99);

            if(player1.getMarried().equals("false")) {
                mariage.setVisibility(View.VISIBLE);
                goDate.setVisibility(View.GONE);
                offerGift.setVisibility(View.GONE);
            }
            else{
                mariagePhotos.setVisibility(View.VISIBLE);
                mariage.setVisibility(View.GONE);
                goDate.setVisibility(View.VISIBLE);
                offerGift.setVisibility(View.VISIBLE);
            }
        }
        if (player1.getDating().equals("true") ) {
            adButtonFindPartner.setVisibility(View.GONE);
            Partner partner=MainMenu.myAppDataBase.myDao().getDatingPartner();
            if(partner != null) {

                foundPartnerConstraint.setVisibility(View.VISIBLE);
                lookPartner.setVisibility(View.GONE);
                visitText.setVisibility(View.GONE);
                partnerName.setText(partner.getName());
                partnerImage.setImageURI(Uri.parse(partner.getImage()));
            }
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

                    lookPartner.setText(getString(R.string.start_looking_partner));
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
                lookPartner.setText(getString(R.string.stop_looking_partner));
                lookPartner.setTextColor(getResources().getColor(R.color.red));
                dis++;
                visitText.setVisibility(View.VISIBLE);

                countDownTimer.start();

            } else {
                lookPartner.setText(getString(R.string.start_looking_partner));
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
                        relationPlusText.setText("+ "+Params.ROSES_BONUS+" "+getString(R.string.to_relation));
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
                        relationPlusText.setText("+ "+Params.CHOCOLATE_BONUS+" "+getString(R.string.to_relation));
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
                        relationPlusText.setText("+ "+Params.JEWELRY_BONUS+" "+getString(R.string.to_relation));
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

                builder.setMessage(getString(R.string.do_u_want_breakup) +" "+partner.getName()+" ?")
                        .setPositiveButton(getString(R.string.yes), ((dialog, which) -> {

                            player1.setDating("false");
                            player1.setMarried("false");
                            mariagePhotos.setVisibility(View.GONE);
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
                            lookPartner.setText(getString(R.string.start_looking_partner));
                            lookPartner.setTextColor(getResources().getColor(R.color.white));
                            dis = 2;

                            viewmodel.setBreakUp(true);

                            //loadRewardAd();

                        })).setNegativeButton(getString(R.string.no), ((dialog, which) -> {
                    dialog.cancel();
                    dialog.dismiss();
                }));

                AlertDialog alertDialog = builder.create();
                alertDialog.show();
            }
        });


        goDate.setOnClickListener(view ->{

            if(dateNumber >2){

                Toast.makeText(getContext(),getString(R.string.too_much_dates),Toast.LENGTH_LONG).show();

            }
            else {

                int randCost = (int) (Math.random() * 200) + 50;


                Dialog dialog = new Dialog(getActivity());
                dialog.setContentView(R.layout.dialog);
                TextView dialogTitle = dialog.findViewById(R.id.dialogTitle);
                Button cofirm = dialog.findViewById(R.id.confirm);
                Button decline = dialog.findViewById(R.id.decline);
                Button watchad = dialog.findViewById(R.id.gainmoneyAd);



                dialogTitle.setText(getString(R.string.this_date_costs)+" " + randCost + "$, "+getString(R.string.continue_n));

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

                watchad.setOnClickListener(v2 ->{
                    if(mRewardVideoAd.isLoaded()){
                        dateAd=true;
                        mRewardVideoAd.show();
                    }

                    dialog.dismiss();
                });

                decline.setOnClickListener(v1 -> {
                    dialog.cancel();
                });

                dialog.setOnDismissListener(dialog1 -> {
                    dialog.cancel();
                });
                dialog.show();

                if(player1.getBalance() < randCost){
                    cofirm.setEnabled(false);
                    watchad.setVisibility(View.VISIBLE);
                }else {
                    cofirm.setEnabled(true);
                    watchad.setVisibility(View.GONE);
                }
            }
        });

        adButtonFindPartner.setOnClickListener(v -> {

            if(mRewardVideoAd.isLoaded()){
                mRewardVideoAd.show();
                dateAd=false;
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


                                //for showing mariage congratulation
                                marriedDialog();
                            });

                            viewmodel.setMarried(true);
                            mariagePhotos.setVisibility(View.VISIBLE);
                            mariage.setVisibility(View.GONE);
                            offerGift.setVisibility(View.VISIBLE);
                            goDate.setVisibility(View.VISIBLE);
                        }
                    });
                    dialog.show();
                });


        relationBar.setMaxReachedListener(()->{

            if(player1.getMarried().equals("false")) {

                mariage.setVisibility(View.VISIBLE);
                offerGift.setVisibility(View.GONE);
                goDate.setVisibility(View.GONE);

            }
            });
        sharedPreferences=getContext().getSharedPreferences("myshared",Context.MODE_PRIVATE);

        String firstTime=sharedPreferences.getString("firstTimePartner","none");

        if(firstTime.equals("none")) {
            showTuto();
            sharedPreferences.edit().putString("firstTimePartner","finished").apply();
        }

        return fragment;
    }



    private void showTuto(){

        new GuideView.Builder(getContext())
                .setTitle(getString(R.string.dating))
                .setContentText(getString(R.string.dating_message_tuto))
                .setTargetView(lookPartner)
                .build()
                .show();
    }

    private void marriedDialog(){

        Dialog dialog = new Dialog(getActivity());
        dialog.setContentView(R.layout.congrat_married);
        TextView text = dialog.findViewById(R.id.cong_text);
        text.setText( getString(R.string.married_to)+" "+partnerName.getText().toString()+" !.");
        dialog.show();

    }

    private void foundPartner(Partner partner){

            Dialog dialog = new Dialog(getActivity());
            dialog.setContentView(R.layout.found_partner);
            Button confirm = dialog.findViewById(R.id.confirm);
            Button cancel = dialog.findViewById(R.id.cancel);
            ImageView partnerImage = dialog.findViewById(R.id.partnerImage);
            TextView title = dialog.findViewById(R.id.title);

            partnerImage.setImageURI(Uri.parse(partner.getImage()));
            title.setText(getString(R.string.met_someone)+" " + partner.getName() + " !");

            cancel.setOnClickListener(view -> {
                dialog.dismiss();
                dialog.cancel();

                lookPartner.setText(getString(R.string.start_looking_partner));
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


                String firstTime =sharedPreferences.getString("firstTimeDate","none");


                if(firstTime.equals("none")) {
                    new GuideView.Builder(getContext())
                            .setTitle(getString(R.string.dating))
                            .setContentText(getString(R.string.click_offer_gift))
                            .setTargetView(offerGift)
                            .setGuideListener(view1 ->
                                    new GuideView.Builder(getContext())
                                            .setTitle(getString(R.string.dating))
                                            .setContentText(getString(R.string.click_go_date))
                                            .setTargetView(goDate)
                                            .build()
                                            .show())
                            .build()
                            .show();

                    sharedPreferences.edit().putString("firstTimeDate","finished");
                }

                dialog.cancel();
                dialog.dismiss();
            });


            dialog.setOnDismissListener(dialog1 -> {

                lookPartner.setText(getString(R.string.start_looking_partner));
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
    public void onRewardedVideoAdOpened() { }

    @Override
    public void onRewardedVideoStarted() {
        countDownTimer.cancel();
    }

    @Override
    public void onRewardedVideoAdClosed() {
        loadRewardAd(); }

    @Override
    public void onRewarded(RewardItem rewardItem) {

        if(!dateAd) {
            List<Partner> partners = MainMenu.myAppDataBase.myDao().getPartners();

            int min = 0;
            int max = 3;
            int random = (int) (Math.random() * max) - min;
            foundPartner(partners.get(random));
        }
        else{
            //don't mind the 22 its just for the code to work properly
            viewmodel.setGoDate(2000);

            dateNumber++;

            relationBar.setProgress(relationBar.getProgress() + 10);

            progressText.setText(relationBar.getProgress() + "/" + relationBar.getMax());
            player1.setRelationBar(relationBar.getProgress());
            viewmodel.setRelationBar(relationBar.getProgress());

            MainMenu.myAppDataBase.myDao().updatePlayer(player1);
        } }
    @Override
    public void onRewardedVideoAdLeftApplication() { }
    @Override
    public void onRewardedVideoAdFailedToLoad(int i) {
        loadRewardAd(); }
    @Override
    public void onRewardedVideoCompleted() { }


    @Override
    public void onDetach() {
        countDownTimer.cancel();
        countDownTimer=null;
        super.onDetach();
    }


}


