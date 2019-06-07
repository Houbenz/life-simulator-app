package fragments;


import android.Manifest;
import android.app.Dialog;
import android.content.ContentValues;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.net.Uri;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.Fragment;
import database.Acquired_Cars;
import database.Acquired_Houses;

import android.os.CountDownTimer;
import android.os.Environment;
import android.provider.MediaStore;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.android.houbenz.lifesimulator.R;
import com.houbenz.lifesimulator.MainMenu;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.OutputStream;
import java.util.List;

import static fragments.Home2Fragment.savePicture;

/**
 * A simple {@link Fragment} subclass.
 */
public class OutsideHome2Fragment extends Fragment {




    private ImageView outsideView;
    private ImageView carPlace2;
    private View mainConsLayout;
    public OutsideHome2Fragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {


        View fragment;

        int slot =getArguments().getInt("slot");
        Acquired_Houses acquired_houses = MainMenu.myAppDataBase.myDao().getAcqHouse(slot,3);

        if(acquired_houses == null) {

            fragment = inflater.inflate(R.layout.fragment_home_not_owned, container, false);
        }
        else
            {
            fragment = inflater.inflate(R.layout.fragment_outside_home2, container, false);
            outsideView=fragment.findViewById(R.id.outsideView);
            carPlace2=fragment.findViewById(R.id.carPlace2);

                List<Acquired_Cars> acquired_cars=MainMenu.myAppDataBase.myDao().getAcquiredCars(slot);
                for(Acquired_Cars acqCar : acquired_cars){
                    if(acqCar != null){
                        if(acqCar.getAvailable().equals("true")){
                            carPlace2.setImageURI(Uri.parse(acqCar.getImgUrl()+"2"));
                        }
                    }
                }

                mainConsLayout = fragment.findViewById(R.id.mainConsLayout);

                CountDownTimer count = new CountDownTimer(500,250) {
                    @Override
                    public void onTick(long millisUntilFinished) {
                        outsideView.setImageBitmap(getBitmap(mainConsLayout));
                    }
                    @Override
                    public void onFinish() {
                        removeAllImages();
                    }
                };
                count.start();


                mainConsLayout.setOnLongClickListener(v -> {
                    if(ContextCompat.checkSelfPermission(getActivity(),Manifest.permission.WRITE_EXTERNAL_STORAGE)
                            == PackageManager.PERMISSION_GRANTED) {
                        Dialog dialog = new Dialog(getContext());

                        dialog.setContentView(R.layout.dialog);
                        TextView title = dialog.findViewById(R.id.dialogTitle);
                        Button confirm = dialog.findViewById(R.id.confirm);
                        Button cancel = dialog.findViewById(R.id.decline);
                        title.setText("would you like to save the image ?");

                        confirm.setOnClickListener(v1 -> {

                            //this is a static method from home2Fragment
                            addImageGallery(savePicture(mainConsLayout, "Home2 Outside.jpeg"));

                            Toast.makeText(getContext(), "Picture save Succesfully", Toast.LENGTH_SHORT).show();
                            dialog.dismiss();
                        });

                        cancel.setOnClickListener(v2 -> {
                            dialog.dismiss();
                        });
                        dialog.setOnDismissListener(dialog1 -> {

                            dialog.dismiss();
                        });

                        dialog.show();
                    }
                    return  false;
                });
        }



        return fragment;
    }



    private void removeAllImages(){
        carPlace2.setVisibility(View.GONE);
    }

    private Bitmap getBitmap(View v) {
        Bitmap bitmap = Bitmap.createBitmap(v.getWidth(),v.getHeight(),Bitmap.Config.ARGB_8888);
        Canvas canvas = new Canvas(bitmap);
        v.draw(canvas);
        return bitmap;
    }
    private void addImageGallery(@NonNull File file ) {
        ContentValues values = new ContentValues();
        values.put(MediaStore.Images.Media.DATA, file.getAbsolutePath());
        values.put(MediaStore.Images.Media.MIME_TYPE, "image/jpeg"); // or image/png
        getContext().getContentResolver().insert(MediaStore.Images.Media.EXTERNAL_CONTENT_URI, values);
    }
}
