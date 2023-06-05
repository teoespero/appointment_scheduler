package teoespero.jappointment.Model;

import java.time.LocalDateTime;

/**
 * <p>The <b>Country Class</b> provides the model for the country object.</p>
 * @author Teodulfo Espero (BS Software Development, WGU)
 * @since 01.05232023
 */
public class Country {

    ////////////////////////////////////////////////////////////////////////////////////////////////////////////
    //  Define the Class Members
    private int countryID;  //  The unique ID for the country
    private String countryName; //  The Name of the Country
    private LocalDateTime createDate;   //  The Date/Time the record was created
    private String createdBy;   //  Who created the record
    private LocalDateTime lastUpdateTime;   //  When was it last updated
    private String lastUpdatedBy;   //  Who last updated it

    ////////////////////////////////////////////////////////////////////////////////////////////////////////////
    //  Constructors

    /**
     * <p>Constructor that does not use any arguments.</p>
     */
    public Country(){}

    /**
     * <p>Constructor that uses arguments in creating instances of the Country object.</p>
     * @param countryName <p>The name of the country (String)</p>
     * @param createdBy <p>Who created it (String)</p>
     * @param lastUpdatedBy <p>Who last updated the record (String)</p>
     */
    public Country(String countryName, String createdBy, String lastUpdatedBy) {
        this.countryName = countryName;
        this.createdBy = createdBy;
        this.lastUpdatedBy = lastUpdatedBy;
    }


    ////////////////////////////////////////////////////////////////////////////////////////////////////////////
    //  Getters

    /**
     * <p>Getter for the Country ID, returns the Country ID from the database.</p>
     * @return <p>The Country ID (int).</p>
     */
    public int getCountryID() {return countryID;}

    /**
     * <p>Getter for the Country Name, returns the Country Name from the database.</p>
     * @return <p>The Country Name (String).</p>
     */
    public String getCountryName() {return countryName;}

    /**
     * <p>Getter for the Country record's creation date.</p>
     * @return <p>Creation date (LocalDateTime).</p>
     */
    public LocalDateTime getCreateDate() {return createDate;}

    /**
     * <p>Getter for the Country record's creator.</p>
     * @return <p>Creator Name (String).</p>
     */
    public String getCreatedBy() {return createdBy;}

    /**
     * <p>Getter for when the Country record was last updated (Time).</p>
     * @return <p>Time the record was last updated (LocalDateTime).</p>
     */
    public LocalDateTime getLastUpdateTime() {return lastUpdateTime;}

    /**
     * <p>Getter for when the Country record was last updated (Date).</p>
     * @return <p>Date the record was last updated (LocalDateTime).</p>
     */
    public String getLastUpdatedBy() {return lastUpdatedBy;}


    ////////////////////////////////////////////////////////////////////////////////////////////////////////////
    //  Setters

    /**
     * <p>Setter for the Country ID.</p>
     * @param countryID <p>The Country ID (int).</p>
     */
    public void setCountryID(int countryID) {this.countryID = countryID;}

    /**
     * <p>Setter for the Country Name.</p>
     * @param countryName <p>The Country Name (String).</p>
     */
    public void setCountryName(String countryName) {this.countryName = countryName;}

    /**
     * <p>Setter for the Country record create date.</p>
     * @param createDate <p>Create date (LocalDateTime).</p>
     */
    public void setCreateDate(LocalDateTime createDate) {this.createDate = createDate;}

    /**
     * <p>Setter for the Country record creator.</p>
     * @param createdBy <p>Creator Name (String).</p>
     */
    public void setCreatedBy(String createdBy) {this.createdBy = createdBy;}

    /**
     * <p>Setter for when the Country record was last updated (Time).</p>
     * @param lastUpdateTime <p>Time last updated (LocalDateTime).</p>
     */
    public void setLastUpdateTime(LocalDateTime lastUpdateTime) {this.lastUpdateTime = lastUpdateTime;}

    /**
     * <p>Setter for when the Country record was updated by a user.</p>
     * @param lastUpdatedBy <p>The user who updated the record (String).</p>
     */
    public void setLastUpdatedBy(String lastUpdatedBy) {this.lastUpdatedBy = lastUpdatedBy;}

    /**
     * <p>Getter for the Country Name.</p>
     * @return <p>The Country Name.</p>
     */
    @Override public String toString() {return countryName;}
}
