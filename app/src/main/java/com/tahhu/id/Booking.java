package com.tahhu.id;

public class Booking {
    private String id;
    private String securityName;
    private String securityType;
    private String period;
    private String price;
    private String paymentMethod;
    private String address;
    private boolean isActive;

    public Booking() {
        // Default constructor required for Firebase
    }

    public Booking(String id, String securityName, String securityType, String period, String price, String paymentMethod, String address) {
        this.id = id;
        this.securityName = securityName;
        this.securityType = securityType;
        this.period = period;
        this.price = price;
        this.paymentMethod = paymentMethod;
        this.address = address;
        this.isActive = true;
    }

    // Getters and setters
    public String getId() { return id; }
    public void setId(String id) { this.id = id; }

    public String getSecurityName() { return securityName; }
    public void setSecurityName(String securityName) { this.securityName = securityName; }

    public String getSecurityType() { return securityType; }
    public void setSecurityType(String securityType) { this.securityType = securityType; }

    public String getPeriod() { return period; }
    public void setPeriod(String period) { this.period = period; }

    public String getPrice() { return price; }
    public void setPrice(String price) { this.price = price; }

    public String getPaymentMethod() { return paymentMethod; }
    public void setPaymentMethod(String paymentMethod) { this.paymentMethod = paymentMethod; }

    public String getAddress() { return address; }
    public void setAddress(String address) { this.address = address; }

    public boolean isActive() { return isActive; }
    public void setActive(boolean active) { isActive = active; }
}

