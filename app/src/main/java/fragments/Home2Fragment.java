package fragments;


import android.app.Dialog;
import android.content.ContentValues;
import android.content.DialogInterface;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.net.Uri;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import database.Acquired_Furnitures;
import database.Acquired_Houses;

import android.os.CountDownTimer;
import android.os.Environment;
import android.provider.MediaStore;
import android.util.Log;
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
import java.io.IOException;
import java.io.OutputStream;
import java.util.List;


public class Home2Fragment extends Fragment {


    private ImageView room2;
    private ImageView wardrobe;
    private ImageView computerTable;
    private ImageView bed;
    private View introLayout;

    public Home2Fragment() {
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {


        int slot = getArguments().getInt("slot");
        View fragment;


        Acquired_Houses acquired_houses = MainMenu.myAppDataBase.myDao().getAcqHouse(slot,3);

        if(acquired_houses == null){


            fragment =inflater.inflate(R.layout.fragment_home_not_owned, container, false);

        }else {

            fragment = inflater.inflate(R.layout.fragment_home2, container, false);

            room2 = fragment.findViewById(R.id.room2);
            wardrobe = fragment.findViewById(R.id.wardrobe);
            computerTable = fragment.findViewById(R.id.computer_table);
            bed = fragment.findViewById(R.id.bed);


            List<Acquired_Furnitures> acquired_furnituresList = MainMenu.myAppDataBase.myDao().getAcquiredFurnitures(slot);


            for(Acquired_Furnitures furn : acquired_furnituresList){
                Uri uri=null;
                switch (furn.getFurnitureType()){

                    case "wardrobe" :
                        if(furn.getAvailable().equals("true"))
                        uri = Uri.parse(furn.getImgurl()+"x");
                        wardrobe.setImageURI(uri);
                        break;


                    case "computer table" :

                        if(furn.getAvailable().equals("true"))
                            uri = Uri.parse(furn.getImgurl()+"x");
                        computerTable.setImageURI(uri);

                        break;
                    case "bed" :

                        if(furn.getAvailable().equals("true"))
                            uri = Uri.parse(furn.getImgurl()+"x");
                        bed.setImageURI(uri);
                        break;

                        default: ;
                }
            }



         introLayout = fragment.findViewById(R.id.introlayout);


        CountDownTimer count = new CountDownTimer(500,250) {
            @Override
            public void onTick(long millisUntilFinished) {
                room2.setImageBitmap(getBitmap(introLayout));
            }

            @Override
            public void onFinish() {

                removeAllImages();
            }
        };
        count.start();




        // for saving a picture
        introLayout.setOnLongClickListener(v -> {

            Dialog dialog = new Dialog(getContext());

            dialog.setContentView(R.layout.dialog);
            TextView title = dialog.findViewById(R.id.dialogTitle);
            Button confirm = dialog.findViewById(R.id.confirm);
            Button cancel = dialog.findViewById(R.id.decline);
            title.setText("would you like to save the image ?");

            confirm.setOnClickListener(v1->{
                addImageGallery(savePicture(introLayout,"Home2 inside.jpeg"));


                Toast.makeText(getContext(),"Picture save Succesfully",Toast.LENGTH_SHORT).show();
                dialog.dismiss();
            });

            cancel.setOnClickListener(v2->{
                dialog.dismiss();
            });
            dialog.setOnDismissListener(dialog1 -> {

                dialog.dismiss();
            });

            dialog.show();
            return  false;
        });
        }
        return fragment;
    }




    public static File savePicture(@NonNull View v,String filename){

        Bitmap bm =getBitmap(v);

        File pictureFile = null;
        FileOutputStream fOut=null;

        try {
        File root = new File(Environment.getExternalStorageDirectory().getAbsolutePath()+ File.separator +"Life Simulator" + File.separator);
        root.mkdirs();
         pictureFile = new File(root,filename);

        pictureFile.createNewFile();

        fOut=new FileOutputStream(pictureFile);

        bm.compress(Bitmap.CompressFormat.JPEG,100,fOut);

        fOut.flush();
        fOut.close();


            //MediaStore.Images.Media.insertImage(getContext().getContentResolver(),bm,"Home2 Outside","picture");

        } catch (IOException e) {
            e.printStackTrace();
        }

        return pictureFile;

    }

    private  void removeAllImages(){
        bed.setVisibility(View.GONE);
        wardrobe.setVisibility(View.GONE);
        computerTable.setVisibility(View.GONE);
    }

    public static Bitmap getBitmap(@NonNull View v) {
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
