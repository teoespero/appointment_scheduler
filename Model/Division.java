package teoespero.jappointment.Model;

import java.time.LocalDateTime;

public class Division {
    private int divisionID;
    private String divisionName;
    private LocalDateTime createDate;
    private String createdBy;
    private LocalDateTime lastUpdateTime;
    private String lastUpdatedBy;
    private int countryID;

    public Division() {}
    public Division(String divisionName, String createdBy, String lastUpdatedBy, int countryID) {
        this.divisionName = divisionName;
        this.createdBy = createdBy;
        this.lastUpdatedBy = lastUpdatedBy;
        this.countryID = countryID;
    }

    public int getDivisionID() {return divisionID;}
    public String getDivisionName() {return divisionName;}
    public LocalDateTime getCreateDate() {return createDate;}
    public String getCreatedBy() {return createdBy;}
    public LocalDateTime getLastUpdateTime() {return lastUpdateTime;}
    public String getLastUpdatedBy() {return lastUpdatedBy;}
    public int getCountryID() {return countryID;}

    public void setDivisionID(int divisionID) {this.divisionID = divisionID;}
    public void setDivisionName(String divisionName) {this.divisionName = divisionName;}
    public void setCreateDate(LocalDateTime createDate) {this.createDate = createDate;}
    public void setCreatedBy(String createdBy) {this.createdBy = createdBy;}
    public void setLastUpdateTime(LocalDateTime lastUpdateTime) {this.lastUpdateTime = lastUpdateTime;}
    public void setLastUpdatedBy(String lastUpdatedBy) {this.lastUpdatedBy = lastUpdatedBy;}
    public void setCountryID(int countryID) {this.countryID = countryID;}

    @Override public String toString() {return divisionName;}
}
