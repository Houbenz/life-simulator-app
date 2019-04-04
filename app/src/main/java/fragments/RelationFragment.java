package fragments;


import android.app.AlertDialog;
import android.app.Dialog;
import android.os.Bundle;
import androidx.fragment.app.Fragment;

import android.os.CountDownTimer;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.android.houbenz.lifesimulator.R;
import com.houbenz.lifesimulator.MainMenu;

import java.util.ArrayList;

import androidx.lifecycle.ViewModelProviders;
import conf.Params;
import database.Gift;
import database.Player;
import viewmodels.ViewModelPartner;


public class RelationFragment extends Fragment {


    public RelationFragment() {

    }

    private ProgressBar relationBar;
    private Button offerGift;
    private Button breakUp;
    private Button goDate;

    private TextView visitText;

    private TextView progressText;
    private Button lookPartner;
    private int dis = 2;
    private View foundPartnerConstraint;
    private Player player1;
    private int slot;
    private ViewModelPartner viewmodel;
    private CountDownTimer countDownTimer;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View fragment = inflater.inflate(R.layout.fragment_relation, container, false);


        viewmodel = ViewModelProviders.of(getActivity()).get(ViewModelPartner.class);

        slot = getArguments().getInt("slot");

        lookPartner = fragment.findViewById(R.id.lookPartner);
        foundPartnerConstraint = fragment.findViewById(R.id.foundPartnerConstraint);
        relationBar = fragment.findViewById(R.id.relationProgress);
        offerGift = fragment.findViewById(R.id.offerGift);
        breakUp = fragment.findViewById(R.id.breakUp);
        goDate = fragment.findViewById(R.id.goDate);
        progressText = fragment.findViewById(R.id.progressText);
        visitText=fragment.findViewById(R.id.visittext);

        player1 = MainMenu.myAppDataBase.myDao().getPlayer(slot);


        Log.i("Youpi","rel : "+ player1.getRelationBar());

        //initiate relation progress bar
        relationBar.setProgress(player1.getRelationBar());
        progressText.setText(relationBar.getProgress()+"/"+relationBar.getMax());

        if (player1.getDating().equals("true")) {
            foundPartnerConstraint.setVisibility(View.VISIBLE);
            lookPartner.setVisibility(View.GONE);
            visitText.setVisibility(View.GONE);
        }

        lookPartner.setOnClickListener(view -> {
            if (dis % 2 == 0) {
                lookPartner.setText("Stop looking for a partner");
                lookPartner.setTextColor(getResources().getColor(R.color.red));
                dis++;
                visitText.setVisibility(View.VISIBLE);
                int min =Params.MIN_TIME_LOOKING_FOR_PARTNER;
                int max = Params.MAX_TIME_LOOKING_FOR_PARTNER;

                int range = max - min + 1;
                int random = (int) (Math.random() * range) + min;

                ArrayList<String > strings =Params.getTexts();
                countDownTimer = new CountDownTimer(random, 3000) {
                    @Override
                    public void onTick(long millisUntilFinished) {

                            int min = 0;
                            int max = 3;
                            int random = (int) (Math.random() * max) - min;
                            visitText.setText(strings.get(random));

                    }
                    @Override
                    public void onFinish() {
                        foundPartner();
                    }
                };
                countDownTimer.start();


            } else {
                lookPartner.setText("Start looking for a partner");
                lookPartner.setTextColor(getResources().getColor(R.color.white));
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
                        relationPlusText.setText("+ 5 to Relation");
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
                        relationPlusText.setText("+ 5 to Relation");
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
                        relationPlusText.setText("+ 5 to Relation");
                        relationPlusText.animate().translationY(-100).alpha(0f).setDuration(1000).withEndAction(()->{

                            relationPlusText.setText("");
                            relationPlusText.setVisibility(View.INVISIBLE);
                            relationPlusText.setTranslationY(relationPlusText.getTranslationY()+100);
                            relationPlusText.setAlpha(1f);
                        });
                        });
                    }
            });

            dialog.show();
        });

        breakUp.setOnClickListener(view -> {

            AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());

            builder.setMessage("Do you really want to break up ?")
                    .setPositiveButton("Yes", ((dialog, which) -> {

                        player1.setDating("false");
                        player1.setRelationBar(0);
                        relationBar.setProgress(0);
                        progressText.setText(relationBar.getProgress()+"/100");

                        viewmodel.setRelationBar(relationBar.getProgress());

                        MainMenu.myAppDataBase.myDao().updatePlayer(player1);
                        lookPartner.setVisibility(View.VISIBLE);
                        foundPartnerConstraint.setVisibility(View.GONE);
                        lookPartner.setText("Start looking for a partner");
                        lookPartner.setTextColor(getResources().getColor(R.color.white));
                        dis = 2;

                        viewmodel.setBreakUp(true);

                    })).setNegativeButton("No", ((dialog, which) -> {
                dialog.cancel();
                dialog.dismiss();
            }));

            AlertDialog alertDialog = builder.create();
            alertDialog.show();

        });
        return fragment;
    }

    public void foundPartner() {

            Dialog dialog = new Dialog(getActivity());
            dialog.setContentView(R.layout.found_partner);
            Button confirm = dialog.findViewById(R.id.confirm);
            Button cancel = dialog.findViewById(R.id.cancel);

            cancel.setOnClickListener(view -> {
                dialog.dismiss();
                dialog.cancel();

                lookPartner.setText("Start looking for a partner");
                lookPartner.setTextColor(getResources().getColor(R.color.white));
                visitText.setVisibility(View.GONE);
                dis =0 ;
            });
            confirm.setOnClickListener(view -> {

               this.player1.setDating("true");
                MainMenu.myAppDataBase.myDao().updatePlayer(player1);

                foundPartnerConstraint.setVisibility(View.VISIBLE);
                lookPartner.setVisibility(View.GONE);
                visitText.setVisibility(View.GONE);

                viewmodel.setFoundPartner(true);

                dialog.cancel();
                dialog.dismiss();
            });

            dialog.show();
    }





}


