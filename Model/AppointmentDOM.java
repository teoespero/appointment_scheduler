package teoespero.jappointment.Model;

import teoespero.jappointment.DataAccessObjects.JContactDAO;

import java.sql.SQLException;
import java.time.*;
import java.time.format.DateTimeFormatter;

/**
 * <p>The <b>AppointmentDOM Class</b> is a wrapper class in the application created to convert primitives
 * into objects and vice-versa.</p>
 * @author Teodulfo Espero (BS Software Development, WGU)
 * @since 01.0427.2023
 */
public class AppointmentDOM extends Appointment {

    ////////////////////////////////////////////////////////////////////////////////////////////////////////////
    //  Defining the members
    private String contactName; //  The Contact Name that is referenced by an appointment
    private String startTimeString; //  The Appointment Start Time (String)
    private String endTimeString;   //  The Appointment End Time (String)


    ////////////////////////////////////////////////////////////////////////////////////////////////////////////
    //  Constructor
    /**
     * <p>Constructor that accepts arguments in creating the object instance.</p>
     * @param appointment <p>The Appointment that is to be wrapped.</p>
     * @throws SQLException <p>The error thrown when trying to locate the contact name.</p>
     */
    public AppointmentDOM(Appointment appointment) throws SQLException {

        this.appointmentID = appointment.appointmentID;
        this.title = appointment.title;
        this.description = appointment.description;
        this.location = appointment.location;
        this.type = appointment.type;
        this.startTime = appointment.startTime;
        this.endTime = appointment.endTime;
        this.createDate = appointment.createDate;
        this.createdBy = appointment.createdBy;
        this.lastUpdateTime = appointment.lastUpdateTime;
        this.lastUpdatedBy = appointment.lastUpdatedBy;
        this.customerID = appointment.customerID;
        this.userID = appointment.userID;
        this.contactID = appointment.contactID;

        try {
            this.contactName = JContactDAO.jContactSelectByID(appointment.getJAppointmentContactID()).getJAppointmentContactName();
        }
        catch (SQLException e) {
            System.out.println(e.getMessage());
        }

        DateTimeFormatter dateTimeFormat = DateTimeFormatter.ofPattern("MM-dd-YYYY HH:mm");

        this.startTimeString = convertFromUtc(appointment.getJClientAppointmentStartTime()).format(dateTimeFormat);
        this.endTimeString = convertFromUtc(appointment.getJAppointmentEndTime()).format(dateTimeFormat);

    }

    ////////////////////////////////////////////////////////////////////////////////////////////////////////////
    //  Getters

    /**
     * <p>Returns the Contact Name.</p>
     * @return <p>The Contact Name (String).</p>
     */
    public String getJAppointmentContactName() {return contactName;}

    /**
     * <p>Returns the Appointment Start Time.</p>
     * @return <p>The Appointment Start Time (String).</p>
     */
    public String getJAppointmentStartTimeString() {return startTimeString;}

    /**
     * <p>Returns the Appointment End Time.</p>
     * @return <p>The Appointment End Time (String).</p>
     */
    public String getJAppointmentEndTimeString() {return endTimeString;}


    ////////////////////////////////////////////////////////////////////////////////////////////////////////////
    //  Getters

    /**
     * <p>Sets the Appointment Contact Name.</p>
     * @param contactName <p>The Appointment Contact Name (String).</p>
     */
    public void setJAppointmentContactName(String contactName) {this.contactName = contactName;}

    /**
     * <p>Sets the Appointment Start Time.</p>
     * @param startTimeString <p>The Appointment Start Time (String).</p>
     */
    public void setJAppointmentStartTimeString(String startTimeString) {this.startTimeString = startTimeString;}

    LocalDateTime convertToUtc(LocalDateTime dateTime) {
        ZonedDateTime dateTimeInMyZone = ZonedDateTime.
                of(dateTime, ZoneId.systemDefault());

        return dateTimeInMyZone
                .withZoneSameInstant(ZoneOffset.UTC)
                .toLocalDateTime();

    }

    LocalDateTime convertFromUtc(LocalDateTime utcDateTime){
        return ZonedDateTime.
                of(utcDateTime, ZoneId.of("UTC"))
                .toOffsetDateTime()
                .atZoneSameInstant(ZoneId.systemDefault())
                .toLocalDateTime();
    }

    /**
     * <p>Sets the Appointment End Time.</p>
     * @param endTimeString <p>The Appointment End Time (String).</p>
     */
    public void setJAppointmentEndTimeString(String endTimeString) {this.endTimeString = endTimeString;}
}
