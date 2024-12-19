package com.tahhu.id;

import android.app.AlertDialog;
import android.app.Dialog;
import android.content.Intent;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
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
import androidx.fragment.app.DialogFragment;

import com.google.android.material.floatingactionbutton.FloatingActionButton;

import java.text.NumberFormat;
import java.util.Locale;


public class finance extends AppCompatActivity {

    private ScrollView transactionScrollView;
    private double totalIncome = 0.0;
    private double totalExpense = 0.0;
    private TextView tvBalanceAmount;
    private TextView tvBalanceIncome;
    private TextView tvBalanceExpense;
    private ImageView homeButton, menuMarket, shortVideo, calculator;
    private FloatingActionButton market;

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

        homeButton = findViewById(R.id.homeButton);
        menuMarket = findViewById(R.id.menumarket);
        shortVideo = findViewById(R.id.shortvidio);
        calculator = findViewById(R.id.Kalkulator);
        market = findViewById(R.id.Market);

        // Set click listeners
        homeButton.setOnClickListener(v -> navigateTo("home"));
        menuMarket.setOnClickListener(v -> updateActiveIcon("market"));
        shortVideo.setOnClickListener(v -> navigateTo("videos"));
        calculator.setOnClickListener(v -> navigateTo("calculator"));
        market.setOnClickListener(v -> navigateTo("cart"));
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

    private void navigateTo(String destination) {
        Intent intent;
        switch (destination) {
            case "home":
                intent = new Intent(this, MainActivity.class);
                break;
            case "videos":
                intent = new Intent(this, ShortVidio.class);
                break;
            case "calculator":
                // Show calculator dialog
                DiscountCalculatorDialog dialog = new DiscountCalculatorDialog();
                dialog.show(getSupportFragmentManager(), "DiscountCalculatorDialog");
                return;
            case "cart":
                intent = new Intent(this, CartProductActivity.class);
                break;
            default:
                return;
        }
        startActivity(intent);
        if (!destination.equals("market")) {
            finish(); // Close this activity if navigating away
        }
    }
    private void updateActiveIcon(String activeIcon) {
        int activeColor = getResources().getColor(R.color.white);
        int inactiveColor = getResources().getColor(R.color.inactive_icon);

        homeButton.setColorFilter(activeIcon.equals("home") ? activeColor : inactiveColor);
        menuMarket.setColorFilter(activeIcon.equals("market") ? activeColor : inactiveColor);
        shortVideo.setColorFilter(activeIcon.equals("videos") ? activeColor : inactiveColor);
        calculator.setColorFilter(activeIcon.equals("calculator") ? activeColor : inactiveColor);
        // Note: We don't change the color of the FloatingActionButton (market) as it's always highlighted
    }

    public static class DiscountCalculatorDialog extends DialogFragment {
        private EditText etPrice, etDiscount;
        private TextView tvInitialPrice, tvDiscount, tvFinalPrice;
        private Button btnReset;
        private TextWatcher textWatcher;

        @Override
        public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
            View view = inflater.inflate(R.layout.dialog_diskon_kalkulator, container, false);

            // Initialize views
            etPrice = view.findViewById(R.id.etPrice);
            etDiscount = view.findViewById(R.id.etDiscount);
            tvInitialPrice = view.findViewById(R.id.tvInitialPrice);
            tvDiscount = view.findViewById(R.id.tvDiscount);
            tvFinalPrice = view.findViewById(R.id.tvFinalPrice);
            btnReset = view.findViewById(R.id.btnReset);

            // Set up text change listener
            textWatcher = new TextWatcher() {
                @Override
                public void beforeTextChanged(CharSequence s, int start, int count, int after) {}

                @Override
                public void onTextChanged(CharSequence s, int start, int before, int count) {}

                @Override
                public void afterTextChanged(Editable s) {
                    calculateDiscount();
                }
            };

            // Add text change listeners
            etPrice.addTextChangedListener(textWatcher);
            etDiscount.addTextChangedListener(textWatcher);

            // Set up reset button
            btnReset.setOnClickListener(v -> resetCalculator());

            ImageView closeButton = view.findViewById(R.id.ic_close);
            closeButton.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    dismiss();
                }
            });

            return view;
        }

        private void calculateDiscount() {
            try {
                double price = Double.parseDouble(etPrice.getText().toString().isEmpty() ? "0" : etPrice.getText().toString());
                double discount = Double.parseDouble(etDiscount.getText().toString().isEmpty() ? "0" : etDiscount.getText().toString());

                double discountAmount = (price * discount) / 100;
                double finalPrice = price - discountAmount;

                // Format currency
                NumberFormat formatter = NumberFormat.getCurrencyInstance(new Locale("id", "ID"));

                // Update TextViews
                tvInitialPrice.setText(formatter.format(price).replace("IDR", "Rp"));
                tvDiscount.setText("-" + formatter.format(discountAmount).replace("IDR", "Rp"));
                tvFinalPrice.setText(formatter.format(finalPrice).replace("IDR", "Rp"));
            } catch (NumberFormatException e) {
                // Handle invalid input
                tvInitialPrice.setText("Rp 0");
                tvDiscount.setText("-Rp 0");
                tvFinalPrice.setText("Rp 0");
            }
        }

        private void resetCalculator() {
            etPrice.setText("");
            etDiscount.setText("");
            tvInitialPrice.setText("Rp 0");
            tvDiscount.setText("-Rp 0");
            tvFinalPrice.setText("Rp 0");
        }

        @Override
        public void onStart() {
            super.onStart();
            Dialog dialog = getDialog();
            if (dialog != null) {
                dialog.getWindow().setLayout(
                        ViewGroup.LayoutParams.MATCH_PARENT,
                        ViewGroup.LayoutParams.WRAP_CONTENT
                );
            }
        }
    }
}
