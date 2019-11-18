package com.doannganh.salesmobileassistant.Presenter;

import android.content.Context;
import android.content.SharedPreferences;

import com.doannganh.salesmobileassistant.Manager.DAO.AccountDAO;
import com.doannganh.salesmobileassistant.model.Account;

public class AccountPresenter {
    private static AccountPresenter instance;
    AccountDAO accountDAO;
    Context context;

    public static final String PREFS_NAME = "preferencesLogin";
    public static final String PREF_UNAME = "Username";
    public static final String PREF_PASSWORD = "Password";

    private AccountPresenter(){

    }

    public static AccountPresenter Instance(Context context)
    {
        if (instance == null){
            instance = new AccountPresenter();
        }

        instance.accountDAO = AccountDAO.Instance(context);
        instance.context = context;
        return AccountPresenter.instance;
    }

    public Account Login(Account account){
        try {
            return accountDAO.Login(account);
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }


    public void saveAccountPreferences(Context context, String us, String pa) {
        SharedPreferences settings = context.getSharedPreferences(PREFS_NAME,
                Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = settings.edit();

        // Edit and commit
        editor.putString(PREF_UNAME, us);
        editor.putString(PREF_PASSWORD, pa);
        editor.commit();
    }

    public String[] loadAccountPreferences(Context context) {
        String re[] = new String[2];
        SharedPreferences settings = context.getSharedPreferences(PREFS_NAME,
                Context.MODE_PRIVATE);

        // Get value
        re[0] = settings.getString(PREF_UNAME, "");
        re[1] = settings.getString(PREF_PASSWORD, "");
        return re;
    }
}
