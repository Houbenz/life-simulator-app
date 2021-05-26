package fragments;


import android.app.AlertDialog;
import android.app.Dialog;
import android.content.Context;
import android.content.SharedPreferences;
import android.net.Uri;
import android.os.Bundle;

import Workers.SaveToCloudWork;

import androidx.annotation.NonNull;
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
import com.google.android.gms.ads.AdError;
import com.google.android.gms.ads.AdRequest;
import com.google.android.gms.ads.FullScreenContentCallback;
import com.google.android.gms.ads.LoadAdError;
import com.google.android.gms.ads.rewarded.RewardedAd;
import com.google.android.gms.ads.rewarded.RewardedAdLoadCallback;
import com.google.android.gms.auth.api.signin.GoogleSignIn;
import com.google.android.gms.auth.api.signin.GoogleSignInAccount;
import com.google.android.gms.games.Games;
import com.houbenz.lifesimulator.MainMenu;

import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

import androidx.lifecycle.ViewModelProvider;
import androidx.work.OneTimeWorkRequest;
import androidx.work.WorkManager;
import conf.Params;
import customViews.RelationBar;
import database.Gift;
import database.Partner;
import database.Player;
import smartdevelop.ir.eram.showcaseviewlib.GuideView;
import smartdevelop.ir.eram.showcaseviewlib.config.DismissType;
import viewmodels.ViewModelPartner;



public class RelationFragment extends Fragment //implements RewardedVideoAdListener
{

   // private RewardedVideoAd mRewardVideoAd;

    private RewardedAd rewardAd;

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



    //this is the original FINDPARTNER AD VIDEO ID
    // public final static String AD_VIDEO_PARTNER_ID = "ca-app-pub-5859725902066144/8271930745";

    //this is the test for FINDPARTNER AD VIDEO ID
    public final static String AD_VIDEO_PARTNER_ID = "ca-app-pub-3940256099942544/5224354917";



    public void addFullScreenContentCallback(AdRequest adRequest){
        rewardAd.setFullScreenContentCallback(new FullScreenContentCallback() {

            @Override
            public void onAdShowedFullScreenContent() {
                super.onAdShowedFullScreenContent();
                Log.d("MyAd","ad was shown relationship");
                rewardAd=null;
                loadAd(adRequest);
            }

            @Override
            public void onAdFailedToShowFullScreenContent(@NonNull AdError adError) {
                super.onAdFailedToShowFullScreenContent(adError);
                Log.d("MyAd","ad failed to show relationship");

            }

            @Override
            public void onAdDismissedFullScreenContent() {
                super.onAdDismissedFullScreenContent();
                Log.d("MyAd","ad dismissed relationship");
                loadAd(adRequest);
            }

        });

    }

    public void loadAd(AdRequest adRequest){
       RewardedAd.load(requireActivity(), AD_VIDEO_PARTNER_ID, adRequest, new RewardedAdLoadCallback() {
            @Override
            public void onAdLoaded(@NonNull RewardedAd lRewardedAd) {
                super.onAdLoaded(lRewardedAd);
                rewardAd = lRewardedAd;
                addFullScreenContentCallback(adRequest);

                if(player1.getDating().equals("false")) {
                    adButtonFindPartner.setVisibility(View.VISIBLE);
                }

                Log.d("MyAd", "Ad loaded relationship");


            }
            @Override
            public void onAdFailedToLoad(@NonNull LoadAdError loadAdError) {
                super.onAdFailedToLoad(loadAdError);
                Log.d("MyAd", loadAdError.getMessage());
                rewardAd=null;
            }
        });

    }

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


        AdRequest adRequest = new AdRequest.Builder().build();
        loadAd(adRequest);



        viewmodel = new ViewModelProvider(requireActivity()).get(ViewModelPartner.class);

        slot = getArguments() != null ? getArguments().getInt("slot") : 0;

        player1 = MainMenu.myAppDataBase.myDao().getPlayer(slot);


        //initiate relation progress bar
        if(player1.getRelationBar() < 100){
            relationBar.setProgress(player1.getRelationBar());
            progressText.setText(String.format(Locale.ENGLISH,"%d/%d", relationBar.getProgress(), relationBar.getMax()));
            if(player1.getMarried().equals("true")) {

                mariagePhotos.setVisibility(View.VISIBLE);
            }
        }
        else {
            progressText.setText(R.string.hundred);
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
                rosesNumber.setText(String.format(Locale.ENGLISH,"0%d", roses.getGiftCount()));
            else
                rosesNumber.setText(String.format(Locale.ENGLISH,"%d", roses.getGiftCount()));

            if (jewelry.getGiftCount() < 10)
                jeweleryNumber.setText(String.format(Locale.ENGLISH,"0%d", jewelry.getGiftCount()));
            else
                jeweleryNumber.setText(String.format(Locale.ENGLISH,"%d", jewelry.getGiftCount()));

            if (chocolate.getGiftCount() < 10)
                chocolateNumber.setText(String.format(Locale.ENGLISH,"0%d", chocolate.getGiftCount()));
            else
                chocolateNumber.setText(String.format(Locale.ENGLISH,"%d", chocolate.getGiftCount()));

            //GiftRose Button
            giftRoses.setOnClickListener(view1 -> {

                //animation :DDDD

                    int newCount = roses.getGiftCount() - 1;
                    if (newCount >= 0) {
                        rosesNumber.animate().translationY(-50).alpha(0).setDuration(500).withEndAction(() ->{
                        roses.setGiftCount(newCount);
                        MainMenu.myAppDataBase.myDao().updateGift(roses);

                        if (roses.getGiftCount() < 10)
                            rosesNumber.setText(String.format(Locale.ENGLISH,"0%d", roses.getGiftCount()));
                        else
                            rosesNumber.setText(String.format(Locale.ENGLISH,"%d", roses.getGiftCount()));


                        relationBar.setProgress(relationBar.getProgress() + Params.ROSES_BONUS );
                        progressText.setText(String.format(Locale.ENGLISH,"%d/%d", relationBar.getProgress(), relationBar.getMax()));

                         viewmodel.setRelationBar(relationBar.getProgress());

                         player1.setRelationBar(relationBar.getProgress());
                        MainMenu.myAppDataBase.myDao().updatePlayer(player1);
                        rosesNumber.setTranslationY(rosesNumber.getTranslationY()+50);
                        rosesNumber.setAlpha(1f);

                        relationPlusText.setVisibility(View.VISIBLE);
                        relationPlusText.setText(String.format(Locale.ENGLISH,"+ %d %s", Params.ROSES_BONUS, getString(R.string.to_relation)));
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
                            chocolateNumber.setText(String.format(Locale.ENGLISH,"0%d", chocolate.getGiftCount()));
                        else
                            chocolateNumber.setText(String.format(Locale.ENGLISH,"%d", chocolate.getGiftCount()));

                        relationBar.setProgress(relationBar.getProgress() + Params.CHOCOLATE_BONUS );
                        progressText.setText(String.format(Locale.ENGLISH,"%d/%d", relationBar.getProgress(), relationBar.getMax()));
                        player1.setRelationBar(relationBar.getProgress());


                            viewmodel.setRelationBar(relationBar.getProgress());

                            MainMenu.myAppDataBase.myDao().updatePlayer(player1);

                        chocolateNumber.setTranslationY(chocolateNumber.getTranslationY()+50);
                        chocolateNumber.setAlpha(1f);

                        relationPlusText.setVisibility(View.VISIBLE);
                        relationPlusText.setText(String.format(Locale.ENGLISH,"+ %d %s", Params.CHOCOLATE_BONUS, getString(R.string.to_relation)));
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
                            jeweleryNumber.setText(String.format(Locale.ENGLISH,"0%d", jewelry.getGiftCount()));
                        else
                            jeweleryNumber.setText(String.format(Locale.ENGLISH,"%d", jewelry.getGiftCount()));

                        relationBar.setProgress(relationBar.getProgress() + Params.JEWELRY_BONUS );

                        progressText.setText(String.format(Locale.ENGLISH,"%d/%d", relationBar.getProgress(), relationBar.getMax()));
                        player1.setRelationBar(relationBar.getProgress());

                        viewmodel.setRelationBar(relationBar.getProgress());

                        MainMenu.myAppDataBase.myDao().updatePlayer(player1);

                        jeweleryNumber.setTranslationY(jeweleryNumber.getTranslationY()+50);
                        jeweleryNumber.setAlpha(1f);

                        relationPlusText.setVisibility(View.VISIBLE);
                        relationPlusText.setText(String.format(Locale.ENGLISH,"+ %d %s", Params.JEWELRY_BONUS, getString(R.string.to_relation)));
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
                            progressText.setText(String.format(Locale.ENGLISH,"%d/100", relationBar.getProgress()));

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

                            GoogleSignInAccount account=GoogleSignIn.getLastSignedInAccount(requireActivity());
                            if(account != null && GoogleSignIn.hasPermissions(account))
                                Games.getAchievementsClient(requireActivity(),account).unlock(getString(R.string.achievement_break_up));

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



                dialogTitle.setText(String.format(Locale.ENGLISH,"%s %d$, %s", getString(R.string.this_date_costs), randCost, getString(R.string.continue_n)));

                cofirm.setOnClickListener(v -> {
                    viewmodel.setGoDate(randCost);
                    dialog.dismiss();

                    dateNumber++;

                    relationBar.setProgress(relationBar.getProgress() + 10);

                    progressText.setText(String.format(Locale.ENGLISH,"%d/%d", relationBar.getProgress(), relationBar.getMax()));
                    player1.setRelationBar(relationBar.getProgress());


                    viewmodel.setRelationBar(relationBar.getProgress());

                    MainMenu.myAppDataBase.myDao().updatePlayer(player1);

                });


                watchad.setOnClickListener(v2 ->{
                    if(rewardAd != null){
                        dateAd=true;

                        countDownTimer.cancel();

                        rewardAd.show(requireActivity(),rewardItem -> {

                            if(!dateAd) {
                                List<Partner> partners = MainMenu.myAppDataBase.myDao().getPartners();

                                int min1 = 0;
                                int max1 = 3;
                                int random1 = (int) (Math.random() * max1) - min1;
                                foundPartner(partners.get(random1));
                            }
                            else{
                                //don't mind the 22 its just for the code to work properly
                                viewmodel.setGoDate(2000);

                                dateNumber++;

                                relationBar.setProgress(relationBar.getProgress() + 10);

                                progressText.setText(String.format(Locale.ENGLISH,"%d/%d", relationBar.getProgress(), relationBar.getMax()));
                                player1.setRelationBar(relationBar.getProgress());
                                viewmodel.setRelationBar(relationBar.getProgress());

                                MainMenu.myAppDataBase.myDao().updatePlayer(player1);
                            }
                        });
                    }

                    dialog.dismiss();
                });

                decline.setOnClickListener(v1 -> dialog.cancel());

                dialog.setOnDismissListener(dialog1 ->  dialog.cancel());
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

            if(rewardAd != null){
                countDownTimer.cancel();
                dateAd=false;
                rewardAd.show(requireActivity(),rewardItem -> {

                    if(!dateAd) {
                        List<Partner> partners = MainMenu.myAppDataBase.myDao().getPartners();

                        int min1 = 0;
                        int max1 = 3;
                        int random1 = (int) (Math.random() * max1) - min1;
                        foundPartner(partners.get(random1));
                    }
                    else{
                        //don't mind the 22 its just for the code to work properly
                        viewmodel.setGoDate(2000);

                        dateNumber++;

                        relationBar.setProgress(relationBar.getProgress() + 10);

                        progressText.setText(String.format(Locale.ENGLISH,"%d/%d", relationBar.getProgress(), relationBar.getMax()));
                        player1.setRelationBar(relationBar.getProgress());
                        viewmodel.setRelationBar(relationBar.getProgress());

                        MainMenu.myAppDataBase.myDao().updatePlayer(player1);
                    }

                });
            }

        });



        mariage.setOnClickListener(view -> {

                    Dialog dialog = new Dialog(getActivity());
                    dialog.setContentView(R.layout.mariage_dialog);
                    Button offerRingButton = dialog.findViewById(R.id.offerRingbutton);
                    TextView numberRing = dialog.findViewById(R.id.numberRing);


                    Gift jewelry = MainMenu.myAppDataBase.myDao().getJewelry();
                    numberRing.setText(String.format(Locale.ENGLISH,"%d", jewelry.getGiftCount()));

                    offerRingButton.setOnClickListener(view1 -> {

                        int newCount = jewelry.getGiftCount() - 1;

                        if (newCount >= 0) {

                            numberRing.animate().translationY(-50).alpha(0).setDuration(500).withEndAction(() ->{
                                jewelry.setGiftCount(newCount);
                                MainMenu.myAppDataBase.myDao().updateGift(jewelry);

                                if (jewelry.getGiftCount() < 10)
                                    numberRing.setText(String.format(Locale.ENGLISH,"0%d", jewelry.getGiftCount()));
                                else
                                    numberRing.setText(String.format(Locale.ENGLISH,"%d", jewelry.getGiftCount()));

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



                            GoogleSignInAccount account=GoogleSignIn.getLastSignedInAccount(requireActivity());
                            if(account != null && GoogleSignIn.hasPermissions(account))
                                Games.getAchievementsClient(requireActivity(),account).unlock(getString(R.string.achievement_married));
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
        if(getContext() != null)
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
                .setDismissType(DismissType.outside)
                .build()
                .show();
    }

    private void marriedDialog(){

        Dialog dialog = new Dialog(getActivity());
        dialog.setContentView(R.layout.congrat_married);
        TextView text = dialog.findViewById(R.id.cong_text);
        text.setText(String.format(Locale.ENGLISH,"%s %s !.", getString(R.string.married_to), partnerName.getText().toString()));
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
            title.setText(String.format(Locale.ENGLISH,"%s %s !", getString(R.string.met_someone), partner.getName()));

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

                    sharedPreferences.edit().putString("firstTimeDate","finished").apply();
                }

                dialog.cancel();
                dialog.dismiss();
                saveToCloud();
            });


            dialog.setOnDismissListener(dialog1 -> {

                lookPartner.setText(getString(R.string.start_looking_partner));
                lookPartner.setTextColor(getResources().getColor(R.color.white));
                visitText.setVisibility(View.GONE);
                dis = 0;
            });

            dialog.show();


        }

    @Override
    public void onDetach() {
        countDownTimer.cancel();
        countDownTimer=null;
        super.onDetach();
    }
    private void saveToCloud(){
        OneTimeWorkRequest oneTimeWorkRequest =new OneTimeWorkRequest.Builder(SaveToCloudWork.class)
                .build();
        WorkManager.getInstance(requireActivity()).enqueue(oneTimeWorkRequest);
        GoogleSignInAccount account =GoogleSignIn.getLastSignedInAccount(requireActivity());

    }

}


