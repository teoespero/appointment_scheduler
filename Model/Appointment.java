package teoespero.jappointment.Model;


import java.time.LocalDateTime;

/**
 * <p>The <b>Appointment Class</b> defines the Data Object Model (DOM) of the Appointment data in the
 * application.</p>
 * @author Teodulfo Espero (BS Software Development, WGU)
 * @since 01.04272023
 */
public class Appointment {

    ////////////////////////////////////////////////////////////////////////////////////////////////////////////
    //  defining the fields, the member are defined as protected. This means that they can be accessed by
    //  any class in the same package and by subclasses even if they are in another package

    protected int appointmentID; // this is the unique number that identifies an appointment
    protected String title; // short descriptive summary of  what the appointment is about
    protected String description; // short narrative of  what the appointment is about
    protected String location; // the appointment venue
    protected String type; //   the appointment type that customers can book
    protected LocalDateTime startTime; // date/time the appointment will begin
    protected LocalDateTime endTime; // date/time the appointment will end
    protected LocalDateTime createDate; // when was the appointment created
    protected String createdBy; // who created it
    protected LocalDateTime lastUpdateTime; // when was it last updated
    protected String lastUpdatedBy; // who update it last
    protected int customerID; // who was the customer that booked it
    protected int userID; // who is logged into the system
    protected int contactID; // who is the appointment for

    ////////////////////////////////////////////////////////////////////////////////////////////////////////////
    //  Constructors
    public Appointment() {} // constructor

    /**
     * <p>A constructor that uses parameters in creating the object.</p>
     *
     * @param title Appointment Title.
     * @param description Appointment Description.
     * @param location Appointment Location.
     * @param type Appointment Type.
     * @param startTime Appointment Start Date/Tme.
     * @param endTime Appointment End Date/Time.
     * @param createdBy User who created the Appointment.
     * @param lastUpdatedBy User who last updated the Appointment.
     * @param customerID The Customer ID.
     * @param userID The User ID.
     * @param contactID The Contact ID.
     */
    public Appointment(String title,
                       String description,
                       String location,
                       String type,
                       LocalDateTime startTime,
                       LocalDateTime endTime,
                       String createdBy,
                       String lastUpdatedBy,
                       int customerID,
                       int userID,
                       int contactID) {
        this.title = title;
        this.description = description;
        this.location = location;
        this.type = type;
        this.startTime = startTime;
        this.endTime = endTime;
        this.createdBy = createdBy;
        this.lastUpdatedBy = lastUpdatedBy;
        this.customerID = customerID;
        this.userID = userID;
        this.contactID = contactID;
    }


    ////////////////////////////////////////////////////////////////////////////////////////////////////////////
    //  Setters

    /**
     * <p>Sets the Appointment ID which is a unique ID (auto increment, auto generated in the database)
     * that identifies every appointment in the system.</p>
     * @param appointmentID <p>The Appointment ID (int).</p>>
     */
    public void setJAppointmentID(int appointmentID) {this.appointmentID = appointmentID;}

    /**
     * <p>Sets the Appointment Title. The Appointment Title is a short descriptive summary of  what the
     * appointment is about</p>
     * @param title <p>A short title given to an appointment (String).</p>
     */
    public void setJAppointmentTitle(String title){this.title = title;}

    /**
     * <p>Sets the Appointment Description. The Appointment Description is a short narrative of  what
     * the appointment is about</p>
     * @param description <p>A short narrative of what the appointment is about (String).</p>
     */
    public void setJAppointmentDescription(String description) {this.description = description;}

    /**
     * <p>Sets the Appointment Location. The Appointment Location defines the appointment venue.</p>
     * @param location <p>Defines the appointment venue (String).</p>
     */
    public void setJAppointmentLocation(String location) {this.location = location;}

    /**
     * <p>Sets the Appointment Type. Defines the Type of Appointment that the Customer can book.</p>
     * @param type <p>The Type of Appointment that can be booked with a contact (String).</p>
     */
    public void setJAppointmentType(String type) {this.type = type;}

    /**
     * <p>Sets the Appointment Start Date/Time.</p>
     * @param startTime <p>The Appointment Start Date/Time (LocalDateTime).</p>
     */
    public void setJAppointmentStartTime(LocalDateTime startTime) {this.startTime = startTime;}

    /**
     * <p>Sets the Appointment End Date/Time.</p>
     * @param endTime <p>The Appointment End Date/Time (LocalDateTime).</p>
     */
    public void setJAppointmentEndTime(LocalDateTime endTime) {this.endTime = endTime;}

    /**
     * <p>Sets the date when the Appointment was created.</p>
     * @param createDate <p>The date when the Appointment was created (LocalDateTime).</p>
     */
    public void setJAppointmentCreateDate(LocalDateTime createDate) {this.createDate = createDate;}

    /**
     * <p>Sets the information as to who created the Appointment.</p>
     * @param createdBy <p>Who created the Appointment (String).</p>
     */
    public void setCreatedBy(String createdBy) {this.createdBy = createdBy;}

    /**
     * <p>Sets the date as to when the Appointment was last update.</p>
     * @param lastUpdateTime <p>Date when the Appointment was last updated (LocalDateTime).</p>
     */
    public void setJAppointmentLastUpdateTime(LocalDateTime lastUpdateTime) {this.lastUpdateTime = lastUpdateTime;}

    /**
     * <p>Sets the information as to who last updated the Appointment.</p>
     * @param lastUpdatedBy <p>Who updated the Appointment last (String).</p>
     */
    public void setJAppointmentLastUpdatedBy(String lastUpdatedBy) {this.lastUpdatedBy = lastUpdatedBy;}

    /**
     * <p>Sets the Customer ID for whom the Appointment is for.</p>
     * @param customerID <p>The Customer ID in the database (int).</p>
     */
    public void setJAppointmentCustomerID(int customerID) {this.customerID = customerID;}

    /**
     * <p>Sets the User ID. The User ID is the one currently logged-in to the application.</p>
     * @param userID <p>The User ID in the Database (int).</p>
     */
    public void setUserID(int userID) {this.userID = userID;}

    /**
     * <p>Sets the Appointment Contact ID. The Contact ID is the Contact that the Customer would like
     * to have an Appointment with.</p>
     * @param contactID <p>The Contact ID in the Database (int).</p>
     */
    public void setJAppointmentContactID(int contactID) {this.contactID = contactID;}

    ////////////////////////////////////////////////////////////////////////////////////////////////////////////
    //  Getters

    /**
     * <p>Returns the Appointment ID.</p>
     * @return <p>The Appointment ID (int).</p>
     */
    public int getJAppointmentID() {return appointmentID;}

    /**
     * <p>Returns the Appointment Title.</p>
     * @return <p>The Appointment Title (String)</p>
     */
    public String getJAppointmentTitle() {return title;}

    /**
     * <p>Returns the Appointment Description.</p>
     * @return <p>The Appointment Description (String).</p>
     */
    public String getJAppointmentDescription() {return description;}

    /**
     * <p>Returns the Appointment Location.</p>
     * @return <p>The Appointment Location (String).</p>
     */
    public String getJAppointmentLocation() {return location;}

    /**
     * <p>Returns the Appointment Type.</p>
     * @return <p>The Appointment Type (String).</p>
     */
    public String getJAppointmentType() {return type;}

    /**
     * <p>Returns the Appointment Start Date/Time</p>
     * @return <p>The Appointment Start Date/Time (LocalDateTime).</p>
     */
    public LocalDateTime getJClientAppointmentStartTime() {return startTime;}

    /**
     * <p>Returns the Appointment End Date/Time</p>
     * @return <p>The Appointment End Date/Time (LocalDateTime).</p>
     */
    public LocalDateTime getJAppointmentEndTime() {return endTime;}

    /**
     * <p>Returns who created the Appointment.</p>
     * @return <p>Who created the Appointment (String).</p>
     */
    public String getJAppointmentCreatedBy() {return createdBy;}

    /**
     * <p>Returns who last updated the Appointment.</p>
     * @return <p>Who last updated the Appointment (String).</p>
     */
    public String getJAppointmentLastUpdatedBy() {return lastUpdatedBy;}

    /**
     * <p>Returns who booked the Appointment (Customer ID).</p>
     * @return <p>The Customer ID of who booked the Appointment (int).</p>
     */
    public int getJAppointmentCustomerID() {return customerID;}

    /**
     * <p>Returns the User ID that's logged-in to the session.</p>
     * @return <p>The User ID in the Database (int).</p>
     */
    public int getJAppointmentUserID() {return userID;}

    /**
     * <p>Returns the Contact ID that the Customer would like to meet with.</p>
     * @return <p>The Contact ID in the Database.</p>
     */
    public int getJAppointmentContactID() {return contactID;}
}