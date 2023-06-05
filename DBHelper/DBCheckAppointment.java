package teoespero.jappointment.DBHelper;

import javafx.fxml.FXML;
import javafx.scene.control.ButtonType;
import teoespero.jappointment.DataAccessObjects.JAppointmentDAO;
import teoespero.jappointment.Model.Appointment;
import javafx.collections.ObservableList;
import javafx.scene.control.Alert;

import java.sql.SQLException;
import java.time.*;
import java.util.Optional;

/**
 * <p>The <b>DBCheckAppointment Method</b> validates the appointment that the user created in the system if it
 * satisfies the following conditions:</p>
 * <ul>
 *     <li>Appointments are within business hours (Weekdays from 8:00 a.m. to 10:00 p.m. EST)</li>
 *     <li>No overlapping appointments for customers</li>
 * </ul>
 * @author Teodulfo Espero (BS Software Development, WGU)
 * @since 01.05302023
 */
public class DBCheckAppointment {

    /**
     * <p>The <b>fieldsBlank Method</b> checks if the fields in the appointment forms (new and modify screens)
     * have any blanks</p>
     * @param appointment <p>The appointment object that contains the details of the appointment.</p>
     * @return <p>TRUE or FALSE if any of the field are blank or empty.</p>
     */
    public static boolean fieldsBlank(Appointment appointment) {

        boolean fieldsEmpty = false;

        if ("".equals(appointment.getJAppointmentTitle())) {
            fieldsEmpty = true;
        } else if ("".equals(appointment.getJAppointmentDescription())) {
            fieldsEmpty = true;
        } else if ("".equals(appointment.getJAppointmentLocation())) {
            fieldsEmpty = true;
        } else if ("".equals(appointment.getJAppointmentType())) {
            fieldsEmpty = true;
        }

        if (fieldsEmpty) {
            //displayAlert("Error", "Error", "One or more text fields are empty.");
        }

        return fieldsEmpty;
    }

    /**
     * <p>The <b>startTimePastEndTime Method</b> checks if the appointment created has a start date or time
     * that is after the and date or time.</p>
     * @param appointment <p>The appointment object that contains the details of the appointment.</p>
     * @return <p>TRUE or FALSE if the start/end date/times are invalid.</p>
     */
    public static boolean startTimePastEndTime(Appointment appointment) {

        boolean startAfterEnd = false;
        LocalDateTime startTime = appointment.getJClientAppointmentStartTime();
        LocalDateTime endTime = appointment.getJAppointmentEndTime();

        if (!startTime.isAfter(endTime)) {
        } else {
            startAfterEnd = true;
        }

        if (!startAfterEnd) {
            return startAfterEnd;
        }
        displayAlert("Error", "Error", "The start time cannot occur after the end time.");

        return startAfterEnd;
    }

    /**
     * <p>The <b>appointmentSetAfterWorkHours Method</b> checks if the appointment created is
     * within business hours.</p>
     * @param appointment <p>The appointment object that contains the details of the appointment.</p>
     * @return <p>TRUE or FALSE if the start/end date/times are beyond business hours.</p>
     */
    public static boolean appointmentSetAfterWorkHours(Appointment appointment) {

        LocalDateTime startDateTime = appointment.getJClientAppointmentStartTime();
        LocalDate startDate = startDateTime.toLocalDate();
        ZonedDateTime startTimeZoned = startDateTime.atZone(ZoneId.systemDefault());
        ZonedDateTime startTimeAsEST = startTimeZoned.withZoneSameInstant(ZoneId.of("America/New_York"));

        LocalTime businessStartTime = LocalTime.parse("08:00:00");
        LocalDateTime businessStartDateTime = LocalDateTime.of(startDate, businessStartTime);
        ZonedDateTime businessStartTimeZoned = businessStartDateTime.atZone(ZoneId.of("America/New_York"));

        LocalDateTime endDateTime = appointment.getJAppointmentEndTime();
        ZonedDateTime endTimeZoned = endDateTime.atZone(ZoneId.systemDefault());
        ZonedDateTime endTimeAsEST = endTimeZoned.withZoneSameInstant(ZoneId.of("America/New_York"));

        LocalTime businessEndTime = LocalTime.parse("22:00:00");
        LocalDateTime businessEndDateTime = LocalDateTime.of(startDate, businessEndTime);
        ZonedDateTime businessEndTimeZoned = businessEndDateTime.atZone(ZoneId.of("America/New_York"));

        boolean outsideBusinessHours = startTimeAsEST.isBefore(businessStartTimeZoned) ||
                endTimeAsEST.isAfter(businessEndTimeZoned);

        if (!outsideBusinessHours) {
            return outsideBusinessHours;
        }
        displayAlert("Error", "Error",
                "Appointments can only be made within business hours (08:00 to 22:00).");

        return outsideBusinessHours;
    }

    /**
     * <p>The <b>displayAlert Method</b> provides the logic needed to display custom-defined messages to the user
     * when there is an error, or a confirmation is needed.</p>
     * @param alertType <p>Defines the type of alert (ERROR, CONFIRMATION).</p>
     * @param alertTitle <p>The alert window title.</p>
     * @param alertHeader <p>The message to be displayed.</p>
     * @return <p>An optional return value (button type).</p>
     */
    @FXML public static Optional<ButtonType> displayAlert(String alertType, String alertTitle, String alertHeader){
        Alert alert = null;
        if (alertType.equals("Information")){
            alert = new Alert(Alert.AlertType.INFORMATION);
        }
        if (alertType.equals("Error")){
            alert = new Alert(Alert.AlertType.ERROR);
        }
        if (alertType.equals("Confirmation")){
            alert = new Alert(Alert.AlertType.CONFIRMATION);
        }
        alert.setTitle(alertTitle);
        alert.setHeaderText(alertHeader);
        Optional<ButtonType> result = alert.showAndWait();
        return result;
    }

    /**
     * <p>The <b>appointmentOverlapChecks Method</b> checks if there are any overlapping appointments with the
     * new appointment being created or with the existing appointment being modified.</p>
     * @param newAppointment <p>The appointment object that contains the details of the appointment.</p>
     * @return <p>TRUE or FALSE if there are any overlaps.</p>
     * @throws SQLException <p>The error thrown by the JAppointmentDAO Class.</p>
     */
    public static boolean appointmentOverlapChecks(Appointment newAppointment) throws SQLException {

        ObservableList<Appointment> allAppointments = JAppointmentDAO.jApptSelectAll();

        boolean overlapping = false;


        for (int i = 0, allAppointmentsSize = allAppointments.size(); i < allAppointmentsSize; i++) {
            Appointment appointment = allAppointments.get(i);

            if (appointment.getJAppointmentID() == newAppointment.getJAppointmentID()) {
                continue;
            }

            int newApptCustID = newAppointment.getJAppointmentCustomerID();
            LocalDateTime newApptStart = newAppointment.getJClientAppointmentStartTime();
            LocalDateTime newApptEnd = newAppointment.getJAppointmentEndTime();

            int apptCustID = appointment.getJAppointmentCustomerID();
            LocalDateTime apptStart = appointment.getJClientAppointmentStartTime();
            LocalDateTime apptEnd = appointment.getJAppointmentEndTime();


            if ((newApptCustID != apptCustID) || !newApptStart.isAfter(apptStart) ||
                    !newApptStart.isBefore(apptEnd)) {
                if ((newApptCustID != apptCustID) || !newApptEnd.isAfter(apptStart) ||
                        !newApptEnd.isBefore(apptEnd)) {
                    if ((newApptCustID != apptCustID) || !newApptStart.isBefore(apptStart) ||
                            !newApptEnd.isAfter(apptEnd)) {
                        if ((newApptCustID == apptCustID) && newApptStart.isEqual(apptStart) &&
                                newApptEnd.isAfter(apptEnd)) {
                            overlapping = true;
                            break;
                        } else if ((newApptCustID == apptCustID) && newApptStart.isBefore(apptStart) &&
                                newApptEnd.isEqual(apptEnd)) {
                            overlapping = true;
                            break;
                        } else if ((newApptCustID == apptCustID) && newApptStart.isEqual(apptStart) &&
                                newApptEnd.isEqual(apptEnd)) {
                            overlapping = true;
                            break;
                        }
                    } else {
                overlapping = true;
                break;
            }
                } else {
            overlapping = true;
            break;
        }
            } else {
        overlapping = true;
        break;
    }
        }
        return overlapping;
    }
}