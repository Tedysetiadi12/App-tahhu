package com.tahhu.coba;

import android.content.Context;
import android.content.SharedPreferences;
import android.os.Build;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;

import com.android.volley.BuildConfig;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.List;

public class TransactionManager {
    private static final String PREFS_NAME = "TransactionPrefs";
    private static final String KEY_COMPLETED_TRANSACTIONS = "CompletedTransactions";
    private static final String KEY_APP_VERSION = "AppVersion";

    private SharedPreferences sharedPreferences;
    private Gson gson;

    private static TransactionManager instance;

    // Constructor private untuk menghindari pembuatan instansi langsung
    private TransactionManager(Context context) {
        sharedPreferences = context.getSharedPreferences(PREFS_NAME, Context.MODE_PRIVATE);
        gson = new Gson();

        // Periksa apakah versi aplikasi berbeda dengan yang tersimpan
        if (isAppVersionUpdated(context)) {
            clearTransactions();
            clearCart();
        }
    }

    public void clearCart() {
        // Menghapus cart dari SharedPreferences (atau sumber penyimpanan lainnya)
        sharedPreferences.edit().remove("CartProductList").apply();
    }

    // Metode statis untuk mendapatkan instance singleton
    public static synchronized TransactionManager getInstance(Context context) {
        if (instance == null) {
            instance = new TransactionManager(context.getApplicationContext());
        }
        return instance;
    }

    public void saveTransaction(Transaction transaction) {
        // Periksa apakah aplikasi sedang dalam mode debug
        if (isDebugMode()) {
            // Jangan simpan transaksi jika dalam mode debug
            return;
        }

        List<Transaction> transactions = getCompletedTransactions();
        if (transactions == null) {
            transactions = new ArrayList<>();
        }
        transactions.add(transaction);

        String json = gson.toJson(transactions);
        sharedPreferences.edit().putString(KEY_COMPLETED_TRANSACTIONS, json).apply();

        // Kosongkan cart setelah transaksi berhasil disimpan
        clearCart();
    }

    public List<Transaction> getCompletedTransactions() {
        String json = sharedPreferences.getString(KEY_COMPLETED_TRANSACTIONS, null);
        Type type = new TypeToken<List<Transaction>>() {}.getType();
        List<Transaction> transactions = gson.fromJson(json, type);
        return transactions == null ? new ArrayList<>() : transactions;
    }

    public void clearTransactions() {
        sharedPreferences.edit().remove(KEY_COMPLETED_TRANSACTIONS).apply();
    }

    // Metode untuk memeriksa apakah aplikasi sedang dalam mode debug
    private boolean isDebugMode() {
        // Cek apakah aplikasi sedang dalam mode debug
        return (BuildConfig.DEBUG);
    }

    // Metode untuk memeriksa apakah versi aplikasi berbeda dari versi yang tersimpan
    private boolean isAppVersionUpdated(Context context) {
        try {
            PackageInfo packageInfo = context.getPackageManager().getPackageInfo(context.getPackageName(), 0);
            int currentVersionCode = packageInfo.versionCode;

            int savedVersionCode = sharedPreferences.getInt(KEY_APP_VERSION, -1);
            if (currentVersionCode != savedVersionCode) {
                // Simpan versi aplikasi saat ini ke SharedPreferences
                sharedPreferences.edit().putInt(KEY_APP_VERSION, currentVersionCode).apply();
                return true; // Versi aplikasi baru, data perlu di-reset
            }
        } catch (PackageManager.NameNotFoundException e) {
            e.printStackTrace();
        }
        return false; // Versi sama, tidak perlu di-reset
    }
}
