package teoespero.jappointment.Model;

import java.time.LocalDateTime;

public class Customer {

    protected int customerID;
    protected String customerName;
    protected String address;
    protected String postalCode;
    protected String phone;
    protected LocalDateTime createDate;
    protected String createdBy;
    protected LocalDateTime lastUpdateTime;
    protected String lastUpdatedBy;
    protected int divisionID;
    public Customer() {}

    public Customer(String customerName, String address, String postalCode,
                    String phone, String createdBy, String lastUpdatedBy, int divisionID) {
        this.customerName = customerName;
        this.address = address;
        this.postalCode = postalCode;
        this.phone = phone;
        this.createdBy = createdBy;
        this.lastUpdatedBy = lastUpdatedBy;
        this.divisionID = divisionID;
    }

    public int getJAppointmentCustomerID() {return customerID;}
    public String getCustomerName() {return customerName;}
    public String getAddress() {return address;}
    public String getPostalCode() {return postalCode;}
    public String getPhone() {return phone;}
    public LocalDateTime getCreateDate() {return createDate;}
    public String getCreatedBy() {return createdBy;}
    public LocalDateTime getLastUpdateTime() {return lastUpdateTime;}
    public String getLastUpdatedBy() {return lastUpdatedBy;}
    public int getDivisionID() {return divisionID;}

    public void setCustomerID(int customerID) {this.customerID = customerID;}
    public void setCustomerName(String customerName) {this.customerName = customerName;}
    public void setAddress(String address) {this.address = address;}
    public void setPostalCode(String postalCode) {this.postalCode = postalCode;}
    public void setPhone(String phone) {this.phone = phone;}
    public void setCreateDate(LocalDateTime createDate) {this.createDate = createDate;}
    public void setCreatedBy(String createdBy) {this.createdBy = createdBy;}
    public void setLastUpdateTime(LocalDateTime lastUpdateTime) {this.lastUpdateTime = lastUpdateTime;}
    public void setLastUpdatedBy(String lastUpdatedBy) {this.lastUpdatedBy = lastUpdatedBy;}
    public void setDivisionID(int divisionID) {this.divisionID = divisionID;}

    @Override public String toString() {return customerName;}
}
