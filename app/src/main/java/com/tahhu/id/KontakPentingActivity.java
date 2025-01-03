package com.tahhu.id;

import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.viewpager2.adapter.FragmentStateAdapter;
import androidx.viewpager2.widget.ViewPager2;
import com.google.android.material.dialog.MaterialAlertDialogBuilder;
import com.google.android.material.tabs.TabLayout;
import com.google.android.material.tabs.TabLayoutMediator;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import androidx.appcompat.widget.SearchView;


public class KontakPentingActivity extends AppCompatActivity {

    private SearchView searchView;
    private static ViewPager2 viewPager;
    private TabLayout tabLayout;
    private static KontakPagerAdapter pagerAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_kontak_penting);

        initializeViews();
        setupViewPager();
        setupTabLayout();
        setupSearchView();
    }

    private void initializeViews() {
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        Objects.requireNonNull(getSupportActionBar()).setDisplayHomeAsUpEnabled(true);

        viewPager = findViewById(R.id.viewPager);
        tabLayout = findViewById(R.id.tabLayout);
        searchView = findViewById(R.id.searchView);
    }

    private void setupViewPager() {
        pagerAdapter = new KontakPagerAdapter(this);
        viewPager.setAdapter(pagerAdapter);
    }

    private void setupTabLayout() {
        new TabLayoutMediator(tabLayout, viewPager,
                (tab, position) -> {
                    switch (position) {
                        case 0:
                            tab.setText("Darurat");
                            break;
                        case 1:
                            tab.setText("Keamanan");
                            break;
                        case 2:
                            tab.setText("Layanan");
                            break;
                    }
                }
        ).attach();
    }

    private void setupSearchView() {
        searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String query) {
                return false;
            }

            @Override
            public boolean onQueryTextChange(String newText) {
                Fragment currentFragment = getCurrentFragment();
                if (currentFragment instanceof KontakFragment) {
                    ((KontakFragment) currentFragment).filterContacts(newText);
                } else {
                    // Log untuk debugging
                    Log.e("KontakPentingActivity", "Current fragment is null or not a KontakFragment");
                }
                return true;
            }
        });

    }

    private Fragment getCurrentFragment() {
        int currentItem = viewPager.getCurrentItem();
        Fragment fragment = getSupportFragmentManager().findFragmentByTag("f" + currentItem);

        if (fragment instanceof KontakFragment) {
            return fragment; // Hanya kembalikan jika fragment adalah KontakFragment
        }
        return null; // Hindari NullPointerException
    }


    public static class Contact {
        String name;
        String category;
        String number;
        String description;
        int iconResource;

        public Contact(String name, String category, String number, String description, int iconResource) {
            this.name = name;
            this.category = category;
            this.number = number;
            this.description = description;
            this.iconResource = iconResource;
        }
    }

    // Fragment untuk menampilkan daftar kontak
    public static class KontakFragment extends Fragment {
        private RecyclerView recyclerView;
        private ContactAdapter adapter;
        private List<Contact> contacts;
        private List<Contact> filteredContacts;
        private String category;

        public static KontakFragment newInstance(String category) {
            KontakFragment fragment = new KontakFragment();
            Bundle args = new Bundle();
            args.putString("category", category);
            fragment.setArguments(args);
            return fragment;
        }

        @Override
        public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
            View view = inflater.inflate(R.layout.fragment_kontak, container, false);

            category = getArguments().getString("category");
            recyclerView = view.findViewById(R.id.recyclerView);

            setupRecyclerView();
            loadContacts();

            return view;
        }

        private void setupRecyclerView() {
            contacts = new ArrayList<>();
            filteredContacts = new ArrayList<>();
            adapter = new ContactAdapter(requireContext(), filteredContacts);
            recyclerView.setLayoutManager(new LinearLayoutManager(requireContext()));
            recyclerView.setAdapter(adapter);
        }

        private void loadContacts() {
            // Load contacts based on category
            contacts.clear();
            switch (category) {
                case "Darurat":
                    contacts.add(new Contact("Ambulans", "Darurat", "118",
                            "Layanan ambulans 24 jam", R.drawable.ic_user1));
                    contacts.add(new Contact("Pemadam Kebakaran", "Darurat", "113",
                            "Layanan pemadam kebakaran", R.drawable.ic_user1));
                    break;
                case "Keamanan":
                    contacts.add(new Contact("Pos Satpam", "Keamanan", "021-555123",
                            "Pos keamanan kompleks", R.drawable.ic_user1));
                    contacts.add(new Contact("Polisi", "Keamanan", "110",
                            "Kantor polisi terdekat", R.drawable.ic_user1));
                    break;
                case "Layanan":
                    contacts.add(new Contact("Admin Perumahan", "Layanan", "021-555789",
                            "Layanan administrasi", R.drawable.ic_user1));
                    contacts.add(new Contact("PLN", "Layanan", "123",
                            "Layanan listrik", R.drawable.ic_user1));
                    break;
            }
            filteredContacts.clear();
            filteredContacts.addAll(contacts);
            adapter.notifyDataSetChanged();
        }

        public void filterContacts(String query) {
            if (filteredContacts == null) {
                filteredContacts = new ArrayList<>(); // Inisialisasi jika null
            }

            filteredContacts.clear(); // Membersihkan daftar sebelumnya
            if (query.isEmpty()) {
                filteredContacts.addAll(contacts); // Salin semua kontak jika query kosong
            } else {
                String lowerQuery = query.toLowerCase();
                for (Contact contact : contacts) {
                    if (contact.name.toLowerCase().contains(lowerQuery) || contact.number.contains(lowerQuery)) {
                        filteredContacts.add(contact);
                    }
                }
            }
            adapter.notifyDataSetChanged();
        }

    }

    // Adapter untuk kontak
    private static class ContactAdapter extends RecyclerView.Adapter<ContactAdapter.ViewHolder> {
        private Context context;
        private List<Contact> contacts;

        public ContactAdapter(Context context, List<Contact> contacts) {
            this.context = context;
            this.contacts = contacts;
        }

        @Override
        public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
            View view = LayoutInflater.from(context).inflate(R.layout.item_contact, parent, false);
            return new ViewHolder(view);
        }

        @Override
        public void onBindViewHolder(ViewHolder holder, int position) {
            Contact contact = contacts.get(position);
            holder.contactName.setText(contact.name);
            holder.contactCategory.setText(contact.category);
            holder.contactNumber.setText(contact.number);
            holder.contactIcon.setImageResource(contact.iconResource);

            holder.callButton.setOnClickListener(v -> {
                Intent intent = new Intent(Intent.ACTION_DIAL);
                intent.setData(Uri.parse("tel:" + contact.number));
                context.startActivity(intent);
            });

            holder.messageButton.setOnClickListener(v -> {
                Intent intent = new Intent(Intent.ACTION_SENDTO);
                intent.setData(Uri.parse("smsto:" + contact.number));
                context.startActivity(intent);
            });

            holder.itemView.setOnClickListener(v -> {
                showContactDetail(contact);
            });
        }

        private void showContactDetail(Contact contact) {
            View dialogView = LayoutInflater.from(context)
                    .inflate(R.layout.dialog_contact_detail, null);

            TextView nameText = dialogView.findViewById(R.id.contactName);
            TextView categoryText = dialogView.findViewById(R.id.contactCategory);
            TextView numberText = dialogView.findViewById(R.id.contactNumber);
            TextView descriptionText = dialogView.findViewById(R.id.contactDescription);
            ImageView iconImage = dialogView.findViewById(R.id.contactIcon);

            nameText.setText(contact.name);
            categoryText.setText(contact.category);
            numberText.setText(contact.number);
            descriptionText.setText(contact.description);
            iconImage.setImageResource(contact.iconResource);

            new MaterialAlertDialogBuilder(context)
                    .setView(dialogView)
                    .setPositiveButton("Tutup", null)
                    .show();
        }

        @Override
        public int getItemCount() {
            return contacts.size();
        }

        static class ViewHolder extends RecyclerView.ViewHolder {
            ImageView contactIcon;
            TextView contactName, contactCategory, contactNumber;
            ImageButton callButton, messageButton;

            ViewHolder(View itemView) {
                super(itemView);
                contactIcon = itemView.findViewById(R.id.contactIcon);
                contactName = itemView.findViewById(R.id.contactName);
                contactCategory = itemView.findViewById(R.id.contactCategory);
                contactNumber = itemView.findViewById(R.id.contactNumber);
                callButton = itemView.findViewById(R.id.callButton);
                messageButton = itemView.findViewById(R.id.messageButton);
            }
        }
    }

    public static class KontakPagerAdapter extends FragmentStateAdapter {

        public KontakPagerAdapter(@NonNull FragmentActivity fragmentActivity) {
            super(fragmentActivity);
        }

        @NonNull
        @Override
        public Fragment createFragment(int position) {
            switch (position) {
                case 0:
                    return KontakFragment.newInstance("Darurat");
                case 1:
                    return KontakFragment.newInstance("Keamanan");
                case 2:
                    return KontakFragment.newInstance("Layanan");
                default:
                    return KontakFragment.newInstance("Darurat");
            }
        }

        @Override
        public int getItemCount() {
            return 3; // Tiga tab: Darurat, Keamanan, Layanan
        }

    }
}