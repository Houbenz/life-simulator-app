package beans;

import android.content.ContentResolver;
import android.content.Context;
import android.util.Log;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;

/**
 * Created by Houbenz on 24/07/2018.
 */

public class Bank {

    private String loan ;
    private float amount ;
    private int interest ;

    public Bank(String loan, float amount, int interest) {
        this.loan = loan;
        this.amount = amount;
        this.interest = interest;
    }

    public String getLoan() {
        return loan;
    }

    public void setLoan(String loan) {
        this.loan = loan;
    }

    public float getAmount() {
        return amount;
    }

    public void setAmount(float amount) {
        this.amount = amount;
    }

    public int getInterest() {
        return interest;
    }

    public void setInterest(int interest) {
        this.interest = interest;
    }

    public static ArrayList<Bank> initBank(Context context){

        ArrayList<Bank> banks =new ArrayList<Bank>();
        String json ;
        try {

            InputStream is = context.getAssets().open("bankLoan.json");

            int size = is.available();
            byte[] buffer =new byte[size];

            is.read(buffer);
            is.close();

            json=new String (buffer,"UTF-8");

            JSONArray jsonArray =new JSONArray(json);

            for (int i=0;i<jsonArray.length();i++){



                JSONObject jsonObject= jsonArray.getJSONObject(i);



                String loan =jsonObject.getString("loan");
                float amount =jsonObject.getLong("amount");
                int interest=jsonObject.getInt("interest");


                Bank bank =new Bank(loan,amount,interest);
                banks.add(bank);
            }


        } catch (IOException e) {
            e.printStackTrace();
        } catch (JSONException e) {
            e.printStackTrace();
        }
        return banks;
    }
}
