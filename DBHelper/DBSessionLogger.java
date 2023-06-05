package teoespero.jappointment.DBHelper;

import teoespero.jappointment.Controllers.LoginScreController;
import teoespero.jappointment.Model.User;

import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.time.ZonedDateTime;
import java.time.format.DateTimeFormatter;
import java.util.Optional;

/**
 * <p>The <b>DBSessionLogger Class</b> provides the data and methods that will handle the session logging
 * functions of the JAppointment application. </p>
 * @author Teodulfo Espero (BS Software Development, WGU)
 * @since 01.05252023
 */
public class DBSessionLogger {

    //  define the object resources that will allow us to track the application session
    static User currentSessionUser;
    static ZoneId localMachineSettingZoneID = ZoneId.of("UTC");
    static DateTimeFormatter patternDateTime = DateTimeFormatter.ofPattern("MM/dd/yyyy 'at' hh:mm:ssa z");

    /**
     * <p>Method provides the logic for tracking user login sessions to the JAppointment system.</p>
     * @param entryType <p>0, login was successful, and 1, if the user was denied access to the system.</p>
     * @throws IOException <p>FileWriter error triggered by accessing the session log file.</p>
     */
    public static void logJAppointmentSession(int entryType, String userNameUsed) throws IOException {

        LocalDateTime currentDateTimeUTC = LocalDateTime.now(localMachineSettingZoneID);
        ZonedDateTime zonedDateTimeUTC = currentDateTimeUTC.atZone(localMachineSettingZoneID);
        String dateTimeString = patternDateTime.format(zonedDateTimeUTC);

        //  attempt to write the file and catch any errors triggered
        try {
            FileWriter fileWriter = new FileWriter("login_activity.txt", true);
            PrintWriter printWriter = new PrintWriter(fileWriter);
            currentSessionUser = LoginScreController.getCurrentJAppointmentUser();
            String userName = currentSessionUser.getJAppointmentUserName();

            if (userName != null) {
            } else {
                userName = "Error: Unknown user, Invalid Username and/or password.";
            }

            //  create the session file
            switch (entryType){

                //  login was unsuccessful
                case 0:{
                    System.out.println(userName);
                    printWriter.println("Error: " + userName + " denied access (" + dateTimeString + ").");
                    printWriter.close();
                }

                //  login was successful
                case 1:{
                    printWriter.println(userName + " was granted access on " + dateTimeString);
                    printWriter.close();
                }

                //  login was unsuccessful
                case 2:{
                    printWriter.println("Error: Denied access. Username was blank. " + dateTimeString);
                    printWriter.close();
                }
                //  login was unsuccessful
                case 3:{
                    printWriter.println("Error: Denied access. Password was blank. " + dateTimeString);
                    printWriter.close();
                }
                case 4:{
                    printWriter.println("Error: Denied access. The username " + userNameUsed + " cannot be found." + dateTimeString);
                    printWriter.close();
                }
            }
        }
        //  catch any issues and exit gracefully
        catch(IOException e) {
            System.out.println("An error has occurred: " + e.getMessage());
        }
    }
}
