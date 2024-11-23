package com.tahhu.coba;

import android.app.AlertDialog;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.PopupMenu;
import android.widget.RadioGroup;
import android.widget.ScrollView;
import android.widget.Toast;
import android.widget.TextView;
import android.widget.LinearLayout;
import androidx.appcompat.app.AppCompatActivity;

public class finance extends AppCompatActivity {

    private ScrollView transactionScrollView;
    private double totalIncome = 0.0;
    private double totalExpense = 0.0;
    private TextView tvBalanceAmount;
    private TextView tvBalanceIncome;
    private TextView tvBalanceExpense;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_finance);
        transactionScrollView = findViewById(R.id.scroll_view);
        LinearLayout transactionContainer = findViewById(R.id.transaction_container);

        tvBalanceAmount = findViewById(R.id.tv_balance_amount);
        tvBalanceIncome = findViewById(R.id.tv_balance_income);
        tvBalanceExpense = findViewById(R.id.tv_balance_expense);

        ImageView btnBack = findViewById(R.id.beranda);
        btnBack.setOnClickListener(v -> {
            finish();
        });

        ImageView kebabIcon = findViewById(R.id.btn_titiktiga);
        kebabIcon.setOnClickListener(this::showPopupMenu);

        Button btnAddMoney = findViewById(R.id.btn_add_money);
        btnAddMoney.setOnClickListener(v -> showAddMoneyDialog());

        // Tambahkan data statis
        addDefaultTransactions();
    }

    private void addDefaultTransactions() {
        // Tambahkan beberapa transaksi default
        addTransactionCard("Income", 50000);  // Pemasukan
        addTransactionCard("Expense", 20000); // Pengeluaran
        addTransactionCard("Income", 30000);  // Pemasukan
        addTransactionCard("Expense", 15000); // Pengeluaran
    }

    private void showAddMoneyDialog() {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        View dialogView = LayoutInflater.from(this).inflate(R.layout.dialog_add_money, null);
        builder.setView(dialogView);

        EditText etNominal = dialogView.findViewById(R.id.et_nominal);
        RadioGroup rgTransactionType = dialogView.findViewById(R.id.rg_transaction_type);
        Button btnSaveTransaction = dialogView.findViewById(R.id.btn_save_transaction);

        AlertDialog dialog = builder.create();

        btnSaveTransaction.setOnClickListener(v -> {
            String nominal = etNominal.getText().toString().trim();
            int selectedId = rgTransactionType.getCheckedRadioButtonId();

            if (nominal.isEmpty() || selectedId == -1) {
                Toast.makeText(this, "Please enter all fields", Toast.LENGTH_SHORT).show();
                return;
            }

            try {
                // Pastikan nominal bisa diubah menjadi angka
                double amount = Double.parseDouble(nominal);

                // Tentukan jenis transaksi
                String transactionType = selectedId == R.id.rb_income ? "Income" : "Expense";

                // Panggil fungsi untuk menambahkan transaksi
                addTransactionCard(transactionType, amount);

                // Tutup dialog setelah menyimpan transaksi
                dialog.dismiss();
            } catch (NumberFormatException e) {
                // Tangani kesalahan inputan jika nominal tidak valid
                Toast.makeText(this, "Invalid amount. Please enter a valid number.", Toast.LENGTH_SHORT).show();
            }
        });

        dialog.show();
    }

    private void addTransactionCard(String transactionType, double amount) {
        // Pastikan nominal valid
        if (amount <= 0) {
            Toast.makeText(this, "Amount must be greater than zero", Toast.LENGTH_SHORT).show();
            return;
        }

        // Buat View untuk kartu transaksi
        View transactionCard = LayoutInflater.from(this).inflate(R.layout.transaction_card, null);

        // Ambil referensi ke komponen dalam kartu transaksi
        TextView tvTransactionName = transactionCard.findViewById(R.id.tv_transaction_name);
        TextView tvTransactionAmount = transactionCard.findViewById(R.id.tv_transaction_amount);

        // Set nama dan nominal transaksi
        tvTransactionName.setText(transactionType);
        tvTransactionAmount.setText((transactionType.equals("Income") ? "+ Rp " : "- Rp ") + amount);

        // Update total balance, income, and expense
        totalIncome += transactionType.equals("Income") ? amount : 0;
        totalExpense += transactionType.equals("Expense") ? amount : 0;

        double totalBalance = totalIncome - totalExpense;
        tvBalanceAmount.setText("Rp " + totalBalance);
        tvBalanceIncome.setText("Rp " + totalIncome);
        tvBalanceExpense.setText("Rp " + totalExpense);

        // Pastikan kita menambahkan ke LinearLayout yang benar
        LinearLayout transactionContainer = findViewById(R.id.transaction_container);
        if (transactionContainer != null) {
            transactionContainer.addView(transactionCard);
        } else {
            Log.e("Finance", "Transaction container is null");
        }
    }


    private void showPopupMenu(View view) {
        PopupMenu popupMenu = new PopupMenu(this, view);
        popupMenu.inflate(R.menu.bottom_nav_menu);

        popupMenu.setOnMenuItemClickListener(item -> {
            if (item.getItemId() == R.id.nav_home) {
                Toast.makeText(finance.this, "Settings Selected", Toast.LENGTH_SHORT).show();
                return true;
            } else if (item.getItemId() == R.id.nav_transactions) {
                Toast.makeText(finance.this, "Help Selected", Toast.LENGTH_SHORT).show();
                return true;
            } else if (item.getItemId() == R.id.nav_profile) {
                Toast.makeText(finance.this, "Logout Selected", Toast.LENGTH_SHORT).show();
                return true;
            } else {
                return false;
            }
        });
        popupMenu.show();
    }
}
