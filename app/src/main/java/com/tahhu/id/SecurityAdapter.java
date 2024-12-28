package com.tahhu.id;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;
import java.util.List;

public class SecurityAdapter extends RecyclerView.Adapter<SecurityAdapter.SecurityViewHolder> {
    private Context context;
    private List<SecurityProfile> securityProfiles;
    private String securityType;
    private String period;
    private String price;

    public SecurityAdapter(Context context, List<SecurityProfile> securityProfiles, String securityType, String period, String price) {
        this.context = context;
        this.securityProfiles = securityProfiles;
        this.securityType = securityType;
        this.period = period;
        this.price = price;
    }

    @NonNull
    @Override
    public SecurityViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.item_security_profile, parent, false);
        return new SecurityViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull SecurityViewHolder holder, int position) {
        SecurityProfile profile = securityProfiles.get(position);
        holder.profileImage.setImageResource(profile.getImageResourceId());
        holder.nameTextView.setText(profile.getName());
        holder.experienceTextView.setText(profile.getExperience());
        holder.skillsTextView.setText(profile.getSkills());

        holder.hireButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(context, PaymentSecurityServicesActivity.class);
                intent.putExtra("securityName", profile.getName());
                intent.putExtra("securityType", securityType);
                intent.putExtra("period", period);
                intent.putExtra("price", price);
                context.startActivity(intent);
            }
        });
    }

    @Override
    public int getItemCount() {
        return securityProfiles.size();
    }

    static class SecurityViewHolder extends RecyclerView.ViewHolder {
        ImageView profileImage;
        TextView nameTextView, experienceTextView, skillsTextView;
        Button hireButton;

        SecurityViewHolder(@NonNull View itemView) {
            super(itemView);
            profileImage = itemView.findViewById(R.id.profileImage);
            nameTextView = itemView.findViewById(R.id.nameTextView);
            experienceTextView = itemView.findViewById(R.id.experienceTextView);
            skillsTextView = itemView.findViewById(R.id.skillsTextView);
            hireButton = itemView.findViewById(R.id.hireButton);
        }
    }
}
