package database;

import android.content.Context;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;

public class Partner {

    private int id ;
    private String name ;
    private String image;
    private int likeness;

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getImage() {
        return image;
    }

    public void setImage(String image) {
        this.image = image;
    }

    public int getLikeness() {
        return likeness;
    }

    public void setLikeness(int likeness) {
        this.likeness = likeness;
    }

    public static ArrayList<Partner> initPartners(Context context){

        ArrayList<Partner> partners = new ArrayList<>();

        InputStream is ;
        String json ;

        try {

            is=context.getAssets().open("partner-female.json");

            byte[] buffer = new byte[is.available()];

            is.read(buffer);
            is.close();
            json=new String (buffer,"UTF-8");
            JSONArray jsonArray= new JSONArray(json);

            for (int i = 0 ; i<jsonArray.length();i++){
                JSONObject jsonObject = jsonArray.getJSONObject(i);
                Partner partner = new Partner();

                partner.setId(jsonObject.getInt("id"));
                partner.setImage(jsonObject.getString("image"));
                partner.setLikeness(jsonObject.getInt("likeness"));
                partner.setName(jsonObject.getString("name"));
                partners.add(partner);
            }

        } catch (IOException e) {
            e.printStackTrace();
        } catch (JSONException e) {
            e.printStackTrace();
        }


        return partners;
    }


}
