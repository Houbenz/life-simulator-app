package beans;

import android.content.Context;

import com.google.gson.reflect.TypeToken;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.io.InputStream;
import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;


public class Work {

    private String name ;
    private int timeOfWork;
    private float pay ;
    private int leveltoWork;
    private String imagePath;
    private String reqDegree;

    public Work (){

    }

    public Work(String name, float pay, int leveltoWork, int timeOfWork,String reqDegree,String imagePath) {

        this.name = name;
        this.timeOfWork = timeOfWork;
        this.pay = pay;
        this.leveltoWork = leveltoWork;
        this.reqDegree=reqDegree;
        this.imagePath=imagePath;

    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public int getTimeOfWork() {
        return timeOfWork;
    }

    public void setTimeOfWork(int timeOfWork) {
        this.timeOfWork = timeOfWork;
    }

    public float getPay() {
        return pay;
    }

    public void setPay(float pay) {
        this.pay = pay;
    }

    public int getLeveltoWork() {
        return leveltoWork;
    }

    public void setLeveltoWork(int leveltoWork) {
        this.leveltoWork = leveltoWork;
    }

    public String getImagePath() {
        return imagePath;
    }

    public void setImagePath(String imagePath) {
        this.imagePath = imagePath;
    }

    public String getReqDegree() {
        return reqDegree;
    }

    public void setReqDegree(String reqDegree) {
        this.reqDegree = reqDegree;
    }

    public static ArrayList<Work> workInit(Context context){

        ArrayList<Work> works = new ArrayList<Work>();

        String json ;

        InputStream is=null;


        try{

            //if(Locale.getDefault().getLanguage().equals("en"))
                is =context.getAssets().open("jobs.json");

            //if(Locale.getDefault().getLanguage().equals("fr"))
              //  is =context.getAssets().open("jobs-fr.json");

          //  if(Locale.getDefault().getLanguage().equals("ar"))
            //    is =context.getAssets().open("jobs-ar.json");

            int size = is.available();

            byte[] buffer = new byte[size];

            is.read(buffer);

            is.close();

            json=new String (buffer,"UTF-8");

            JSONArray jsonArray = new JSONArray(json);

            for (int i = 0 ; i<jsonArray.length();i++){

                JSONObject jsonObject= jsonArray.getJSONObject(i);

                String name =jsonObject.getString("name");
                float pay  =jsonObject.getLong("pay");
                int leveltoWork = jsonObject.getInt("leveltoWork");
                int timeOfWork =jsonObject.getInt("timeofWork");
                String imagePath = jsonObject.getString("uri");
                String reqDegree = jsonObject.getString("degree");

                Work work =new Work(name,pay,leveltoWork,timeOfWork,reqDegree,imagePath);
                works.add(work);

            }

        } catch (IOException |JSONException e) {
            e.printStackTrace();
        }


        return works ;
    }

}
