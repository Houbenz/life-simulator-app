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

import androidx.lifecycle.ViewModelProviders;
import conf.Params;
import database.Gift;
import database.Player;
import viewmodels.ViewModelPartner;


public class RelationFragment extends Fragment {


    public RelationFragment() {

    }

    private ProgressBar relationStatus;
    private Button offerGift;
    private Button breakUp;
    private Button goDate;

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
        relationStatus = fragment.findViewById(R.id.relationProgress);
        offerGift = fragment.findViewById(R.id.offerGift);
        breakUp = fragment.findViewById(R.id.breakUp);
        goDate = fragment.findViewById(R.id.goDate);
        progressText = fragment.findViewById(R.id.progressText);

        player1 = MainMenu.myAppDataBase.myDao().getPlayer(slot);

        if (player1.getDating().equals("true")) {
            foundPartnerConstraint.setVisibility(View.VISIBLE);
            lookPartner.setVisibility(View.GONE);
        }

        lookPartner.setOnClickListener(view -> {
            if (dis % 2 == 0) {
                lookPartner.setText("Stop looking for a partner");
                lookPartner.setTextColor(getResources().getColor(R.color.red));
                dis++;

                int min =Params.MIN_TIME_LOOKING_FOR_PARTNER;
                int max = Params.MAX_TIME_LOOKING_FOR_PARTNER;

                int range = max - min + 1;
                int random = (int) (Math.random() * range) + min;

                countDownTimer = new CountDownTimer(random, 1000) {
                    @Override
                    public void onTick(long millisUntilFinished) {

                        //put here the activities he's going to do like (going to club entering dating sites)
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

            Gift roses = MainMenu.myAppDataBase.myDao().getRoses();

            if (roses.getGiftCount() < 10)
                rosesNumber.setText("0" + roses.getGiftCount());
            else
                rosesNumber.setText(roses.getGiftCount() + "");

            giftRoses.setOnClickListener(view1 -> {

                int newCount = roses.getGiftCount() - 1;

                if (newCount >= 0) {
                    roses.setGiftCount(newCount);
                    MainMenu.myAppDataBase.myDao().updateGift(roses);

                    if (roses.getGiftCount() < 10)
                        rosesNumber.setText("0" + roses.getGiftCount());
                    else
                        rosesNumber.setText(roses.getGiftCount() + "");
                }
            });

            giftChocolate.setOnClickListener(view2 -> {
            });
            giftJewelery.setOnClickListener(view3 -> {
            });

            dialog.show();
        });

        breakUp.setOnClickListener(view -> {

            AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());

            builder.setMessage("Do you really want to break up ?")
                    .setPositiveButton("Yes", ((dialog, which) -> {


                        player1.setDating("false");

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
                dis =0 ;
            });
            confirm.setOnClickListener(view -> {

               this.player1.setDating("true");
                MainMenu.myAppDataBase.myDao().updatePlayer(player1);

                foundPartnerConstraint.setVisibility(View.VISIBLE);
                lookPartner.setVisibility(View.GONE);

                viewmodel.setFoundPartner(true);

                dialog.cancel();
                dialog.dismiss();
            });

            dialog.show();
    }
}
