package beans;

import android.content.Context;

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

    private float amount ;
    private float deposit;
    private int interest ;

    public Bank(float amount, float deposit,int interest) {
        this.amount = amount;
        this.deposit=deposit;
        this.interest = interest;
    }


    public float getAmount() {
        return amount;
    }

    public void setAmount(float amount) {
        this.amount = amount;
    }

    public float getDeposit() {
        return deposit;
    }

    public void setDeposit(float deposit) {
        this.deposit = deposit;
    }

    public int getInterest() {
        return interest;
    }

    public void setInterest(int interest) {
        this.interest = interest;
    }
}
