package database;

import android.content.Context;
import android.util.Log;

import com.houbenz.lifesimulator.MainMenu;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.Locale;

import beans.Buy;
import beans.Learn;

import static com.houbenz.lifesimulator.MainMenu.myAppDataBase;

public  class DatabaseInit {



    public static void initWorkRows(boolean isUpdate,Context context){
        ArrayList<beans.Work> worksBeans = beans.Work.workInit(context) ;

        for(beans.Work bean : worksBeans){
            database.Work work = new database.Work();

            work.setId(bean.getId());

            work.setDegree_required(bean.getReqDegree());
            work.setImgPath(bean.getImagePath());
            work.setIncome(bean.getPay());
            work.setLvlToWork(bean.getLeveltoWork());
            work.setName(bean.getName());
            work.setWork_time(bean.getTimeOfWork());
            work.setDegree_id(bean.getDegree_id());

            if(!isUpdate)
                myAppDataBase.myDao().addWork(work);
            else
                myAppDataBase.myDao().updateWork(work);
        }

        int oldRows =myAppDataBase.myDao().workNumber();

        int newRows = worksBeans.size() - oldRows;

        Log.i("YUUP","number of new rows : "+ newRows);
        if(newRows >0) {
            for(int i =oldRows  ; i<worksBeans.size();i++){

                database.Work work = new database.Work();

                work.setId(worksBeans.get(i).getId());

                work.setDegree_required(worksBeans.get(i).getReqDegree());
                work.setImgPath(worksBeans.get(i).getImagePath());
                work.setIncome(worksBeans.get(i).getPay());
                work.setLvlToWork(worksBeans.get(i).getLeveltoWork());
                work.setName(worksBeans.get(i).getName());
                work.setWork_time(worksBeans.get(i).getTimeOfWork());
                work.setDegree_id(worksBeans.get(i).getDegree_id());

                myAppDataBase.myDao().addWork(work);
            }
        }


    }

    public static void initDegreeRows(boolean isUpdate,Context context){
        ArrayList<Learn> learns = Learn.initLearn(context);

        for(Learn learn : learns){
            Degree degree = new Degree();

            degree.setId(learn.getId());
            degree.setName(learn.getName());
            degree.setPrice(learn.getPrice());
            degree.setImgUrl(learn.getImgURL());
            degree.setProgress(learn.getProgress());

            if(!isUpdate)
                myAppDataBase.myDao().addDegree(degree);
            else
                myAppDataBase.myDao().updateDegree(degree);
        }

        int oldRows =myAppDataBase.myDao().degreeNumber();

        int newRows =learns.size() - oldRows;
        if (newRows > 0){
            for (int i = oldRows;i<learns.size();i++){

                Degree degree = new Degree();

                degree.setId(learns.get(i).getId());
                degree.setName(learns.get(i).getName());
                degree.setPrice(learns.get(i).getPrice());
                degree.setImgUrl(learns.get(i).getImgURL());
                degree.setProgress(learns.get(i).getProgress());

                myAppDataBase.myDao().addDegree(degree);
            }
        }
    }

    public static void initFood(boolean isUpdate,Context context){
        ArrayList<beans.Food>  foods= beans.Food.initFood(context);

        for(beans.Food food : foods){
            database.Food foodDb = new database.Food();

            foodDb.setId(food.getId());
            foodDb.setName(food.getName());
            foodDb.setPrice(food.getPrice());
            foodDb.setBenefit(food.getBenefit());
            foodDb.setDescription(food.getDescription());
            foodDb.setImgUrl(food.getImagePath());
            if(!isUpdate)
                myAppDataBase.myDao().addFood(foodDb);
            else
                myAppDataBase.myDao().updateFood(foodDb);
        }

        int oldRows =myAppDataBase.myDao().foodNumber();

        int newRows = foods.size()-oldRows;

        if (newRows > 0){
            for(int i = oldRows;i<foods.size();i++){

                database.Food foodDb = new database.Food();

                foodDb.setName(foods.get(i).getName());
                foodDb.setPrice(foods.get(i).getPrice());
                foodDb.setBenefit(foods.get(i).getBenefit());
                foodDb.setDescription(foods.get(i).getDescription());
                foodDb.setImgUrl(foods.get(i).getImagePath());

                myAppDataBase.myDao().addFood(foodDb);
            }

        }


    }

    public static void initMedicine(boolean isUpdate,Context context){
        ArrayList<beans.Medicine> medicines = beans.Medicine.initMedicine(context);


        for(beans.Medicine medicine : medicines) {
            database.Medicine medicineDb = new database.Medicine();

            medicineDb.setId(medicine.getId());
            medicineDb.setName(medicine.getName());
            medicineDb.setPrice(medicine.getPrice());
            medicineDb.setBenefit(medicine.getBenefit());
            medicineDb.setImgUrl(medicine.getImagePath());
            if (!isUpdate)
                myAppDataBase.myDao().addMedicine(medicineDb);
            else
                myAppDataBase.myDao().updateMedicine(medicineDb);
        }
        int oldRows =myAppDataBase.myDao().medicineNumber();
        int newRows = medicines.size()-oldRows;

        if(newRows > 0){
            for (int i=oldRows;i <medicines.size();i++){

                database.Medicine medicineDb = new database.Medicine();

                medicineDb.setName(medicines.get(i).getName());
                medicineDb.setPrice(medicines.get(i).getPrice());
                medicineDb.setBenefit(medicines.get(i).getBenefit());
                medicineDb.setImgUrl(medicines.get(i).getImagePath());

                myAppDataBase.myDao().addMedicine(medicineDb);
            }
        }

    }

    public static void initFragments(boolean isUpdate,Context context){
        ArrayList<Buy> buys = Buy.initBuy(context);


        for (Buy buy : buys){
            MainFragments mainFragments = new MainFragments();
            mainFragments.setId(buy.getId());
            mainFragments.setName(buy.getName());
            mainFragments.setColor(buy.getColor());
            mainFragments.setImage_Uri(buy.getImagePath());

            if(!isUpdate)
                myAppDataBase.myDao().addMainFragment(mainFragments);
            else
                myAppDataBase.myDao().updateMainFragment(mainFragments);
        }

        int oldRows = myAppDataBase.myDao().fragmentNumber();
        int newRows =buys.size()-oldRows;

        if(newRows > 0){

            for(int i=oldRows; i <buys.size();i++){

                MainFragments mainFragments = new MainFragments();
                mainFragments.setId(buys.get(i).getId());
                mainFragments.setName(buys.get(i).getName());
                mainFragments.setColor(buys.get(i).getColor());
                mainFragments.setImage_Uri(buys.get(i).getImagePath());

                myAppDataBase.myDao().addMainFragment(mainFragments);
            }
        }
    }

    public static void initStores(boolean isUpdate,Context context){
        ArrayList<beans.Store> stores = beans.Store.initStore(context);

        for (beans.Store store : stores) {
            database.Store storeDb = new database.Store();

            storeDb.setId(store.getId());
            storeDb.setName(store.getName());
            storeDb.setPrice(store.getPrice());
            storeDb.setImgUrl(store.getUri());
            storeDb.setIncome(store.getIncome());
            if (!isUpdate)
                myAppDataBase.myDao().addStore(storeDb);
            else
                myAppDataBase.myDao().updateStore(storeDb);


        }


        int oldRows =myAppDataBase.myDao().storeNumber();
        int newRows = stores.size() - oldRows;

        if(newRows > 0 ){
            for(int i = oldRows;i<stores.size(); i++){

                database.Store storeDb = new database.Store();
                storeDb.setName(stores.get(i).getName());
                storeDb.setPrice(stores.get(i).getPrice());
                storeDb.setImgUrl(stores.get(i).getUri());
                storeDb.setIncome(stores.get(i).getIncome());

                myAppDataBase.myDao().addStore(storeDb);
            }
        }




    }

    public static void initFurnitures(boolean isUpdate,Context context){
        ArrayList<beans.Furniture> furnitures = beans.Furniture.initFourniture(context);

        for (beans.Furniture furniture : furnitures){
            database.Furniture furnitureDb = new database.Furniture();

            furnitureDb.setId(furniture.getId());
            furnitureDb.setName(furniture.getName());
            furnitureDb.setPrice(furniture.getPrice());
            furnitureDb.setImgUrl(furniture.getUrl());
            furnitureDb.setFurnitureType(furniture.getFournitureType());

            if(!isUpdate)
                myAppDataBase.myDao().addFurnitures(furnitureDb);
            else
                myAppDataBase.myDao().updateFurniture(furnitureDb);
        }

        int oldRows =myAppDataBase.myDao().furnitureNumber();
        int newRows = furnitures.size() - oldRows;

        if(newRows > 0) {
            for(int i =oldRows ; i<furnitures.size() ; i++){

                database.Furniture furnitureDb = new database.Furniture();
                furnitureDb.setName(furnitures.get(i).getName());
                furnitureDb.setPrice(furnitures.get(i).getPrice());
                furnitureDb.setImgUrl(furnitures.get(i).getUrl());
                furnitureDb.setFurnitureType(furnitures.get(i).getFournitureType());

                myAppDataBase.myDao().addFurnitures(furnitureDb);
            }
        }


    }

    public static void initCars(boolean isUpdate,Context context){

        InputStream is ;
        String json ;
        try{

            if(Locale.getDefault().getLanguage().equals("fr"))
                is=context.getAssets().open("cars-fr.json");
            else
                is=context.getAssets().open("cars.json");

            int size =is.available();

            byte[] buffer = new byte[size];

            is.read(buffer);
            is.close();

            json = new String(buffer,"UTF-8");
            JSONArray jsonArray = new JSONArray(json);


            for (int i =0 ; i<jsonArray.length();i++){
                Car car = new Car();
                JSONObject jsonObject = jsonArray.getJSONObject(i);
                car.setId(jsonObject.getInt("id"));
                car.setName(jsonObject.getString("name"));
                car.setPrice(jsonObject.getDouble("price"));
                car.setImgUrl(jsonObject.getString("uri"));

                if(!isUpdate)
                    MainMenu.myAppDataBase.myDao().addCar(car);
                else
                    MainMenu.myAppDataBase.myDao().updateCar(car);
            }

            int oldRows = MainMenu.myAppDataBase.myDao().carsNumber();
            int newRows = jsonArray.length() - oldRows;


            //for when we update the file with a new row

            if(newRows > 0)
                for(int i = oldRows ; i<jsonArray.length();i++){

                    Car car = new Car();

                    JSONObject jsonObject = jsonArray.getJSONObject(i);
                    car.setId(jsonObject.getInt("id"));
                    car.setName(jsonObject.getString("name"));
                    car.setPrice(jsonObject.getDouble("price"));
                    car.setImgUrl(jsonObject.getString("uri"));

                    MainMenu.myAppDataBase.myDao().addCar(car);
                }

        } catch (IOException | JSONException e) {
            e.printStackTrace();
        }
    }

    public static void initGifts(boolean isUpdate,Context context){

        InputStream is=null ;
        String json ;
        try {

            if(Locale.getDefault().getLanguage().equals("fr"))
                is = context.getAssets().open("gifts-fr.json");
            else
                is = context.getAssets().open("gifts.json");


            int size = is.available();
            byte[] buffer = new byte[size];
            is.read(buffer);
            is.close();
            json=new String (buffer,"UTF-8");
            JSONArray jsonArray = new JSONArray(json);


            for(int i =0 ; i<jsonArray.length();i++){
                JSONObject jo=jsonArray.getJSONObject(i);
                Gift gift = new Gift();
                gift.setId(jo.getInt("id"));
                gift.setName(jo.getString("name"));
                gift.setImgUrl(jo.getString("uri"));
                gift.setPrice(jo.getInt("price"));
                if(!isUpdate)
                    gift.setGiftCount(jo.getInt("giftCount"));
                else {
                    Gift giftforCount =myAppDataBase.myDao().getGift(gift.getId());
                    gift.setGiftCount(giftforCount.getGiftCount());
                }

                if(!isUpdate)
                    myAppDataBase.myDao().addGift(gift);
                else
                    myAppDataBase.myDao().updateGift(gift);
            }

            int oldRows = myAppDataBase.myDao().giftNumber();
            int newRows = jsonArray.length() - oldRows;

            if(newRows > 0){

                for(int i =oldRows ; i<jsonArray.length();i++){

                    JSONObject jo=jsonArray.getJSONObject(i);

                    Gift gift = new Gift();

                    gift.setId(jo.getInt("id"));
                    gift.setName(jo.getString("name"));
                    gift.setImgUrl(jo.getString("uri"));
                    gift.setPrice(jo.getInt("price"));
                    if(!isUpdate)
                        gift.setGiftCount(jo.getInt("giftCount"));

                    myAppDataBase.myDao().addGift(gift);
                }
            }
        } catch (IOException | JSONException e) {
            e.printStackTrace();
        }
    }

    public static void initPartners(boolean isUpdate,Context context){

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

                if(!isUpdate)
                    myAppDataBase.myDao().addPartner(partner);
                else {
                    Partner par = myAppDataBase.myDao().getDatingPartner();

                    if (par != null && par.getId() == partner.getId())
                        partner.setDating("true");
                }
                myAppDataBase.myDao().updatePartner(partner);
            }

            int oldRows = myAppDataBase.myDao().partnerNumber();
            int newRows = jsonArray.length() - oldRows;

            if(newRows >0){

                for (int i = oldRows ; i<jsonArray.length();i++){
                    JSONObject jsonObject = jsonArray.getJSONObject(i);
                    Partner partner = new Partner();

                    partner.setId(jsonObject.getInt("id"));
                    partner.setImage(jsonObject.getString("image"));
                    partner.setLikeness(jsonObject.getInt("likeness"));
                    partner.setName(jsonObject.getString("name"));
                    partners.add(partner);
                    myAppDataBase.myDao().addPartner(partner);
                }
            }


        } catch (IOException | JSONException e) {
            e.printStackTrace();
        }
    }

    public static void initHouses(boolean isUpdate,Context context){

        ArrayList<House> houses = new ArrayList<>();

        InputStream is ;
        String json ;

        try {


            if(Locale.getDefault().getLanguage().equals("fr"))
                is=context.getAssets().open("house-fr.json");
            else
                is=context.getAssets().open("house.json");

            byte[] buffer = new byte[is.available()];

            is.read(buffer);
            is.close();
            json=new String (buffer,"UTF-8");
            JSONArray jsonArray= new JSONArray(json);
            for (int i = 0 ; i<jsonArray.length();i++){
                JSONObject jsonObject = jsonArray.getJSONObject(i);
                House house = new House();

                house.setId(jsonObject.getInt("id"));
                house.setName(jsonObject.getString("name"));
                house.setPrice(jsonObject.getInt("price"));
                house.setImgUrl(jsonObject.getString("uri"));
                house.setBonusEnergy(jsonObject.getInt("bonusE"));
                house.setBonusHealth(jsonObject.getInt("bonusH"));
                houses.add(house);

                if(!isUpdate)
                    myAppDataBase.myDao().addHouse(house);
                else
                    myAppDataBase.myDao().updateHouse(house);
            }


            int oldRows = myAppDataBase.myDao().houseNumber();
            int newRows = jsonArray.length() - oldRows;

            if(newRows >0){

                for (int i = oldRows ; i<jsonArray.length();i++){
                    JSONObject jsonObject = jsonArray.getJSONObject(i);
                    House house = new House();

                    house.setId(jsonObject.getInt("id"));
                    house.setName(jsonObject.getString("name"));
                    house.setPrice(jsonObject.getInt("price"));
                    house.setImgUrl(jsonObject.getString("uri"));
                    house.setBonusEnergy(jsonObject.getInt("bonusE"));
                    house.setBonusHealth(jsonObject.getInt("bonusH"));
                    houses.add(house);

                    myAppDataBase.myDao().addHouse(house);
                }
            }


        } catch (IOException | JSONException e) {
            e.printStackTrace();
        }
    }

    public static String getDatabaseVersion(Context context){

        InputStream is=null ;
        String db_version= "";
        String json ;

        try {
            is = context.getAssets().open("database-version.json");


            int size = is.available();

            byte[] buffer = new byte[size];

            is.read(buffer);

            is.close();

            json=new String(buffer,"UTF-8");

            JSONObject jsonObject = new JSONObject(json);

            db_version =jsonObject.getString("version");


        } catch (IOException e) {
            e.printStackTrace();
        } catch (JSONException e) {
            e.printStackTrace();
        }


        return db_version;
    }
}
