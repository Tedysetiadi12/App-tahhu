package com.tahhu.id;

import android.content.Context;
import android.net.wifi.ScanResult;
import android.net.wifi.WifiManager;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListView;

import androidx.fragment.app.Fragment;

import java.util.List;

public class NetworkListFragment extends Fragment {
    private ListView networkListView;
    private WifiManager wifiManager;
    private NetworkListAdapter adapter;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_network_list, container, false);
        networkListView = view.findViewById(R.id.network_list_view);
        wifiManager = (WifiManager) getActivity().getApplicationContext().getSystemService(Context.WIFI_SERVICE);

        scanWifiNetworks();

        return view;
    }

    private void scanWifiNetworks() {
        wifiManager.startScan();
        List<ScanResult> scanResults = wifiManager.getScanResults();
        adapter = new NetworkListAdapter(getContext(), scanResults);
        networkListView.setAdapter(adapter);
    }
}