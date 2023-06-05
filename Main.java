package teoespero.jappointment;

import javafx.application.Application;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.geometry.Rectangle2D;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Screen;
import javafx.stage.Stage;
import teoespero.jappointment.DBHelper.DBConnection;

import java.io.IOException;
import java.net.URL;
import java.sql.Connection;
import java.sql.SQLException;
import java.util.ResourceBundle;
import java.util.TimeZone;

/**
 * <p>This is the Main Class defined in the application. This is the main entry point in the program.</p>
 * @author Teodulfo Espero (BS Software Development, WGU)
 * @since 01.05312023
 */
public class Main extends Application implements Initializable {

    public static Connection mysqlConn = null;


    public static TimeZone userTimeZone = TimeZone.getDefault();

    /**
     * <p>The Start method creates the Stage where the scene and the GUI elements are displayed.</p>
     * @param stage <p>The FXML stage.</p>
     * @throws IOException <p>The error thrown by JavaFX when it encounters an error.</p>
     */
    @FXML @Override public void start(Stage stage) throws IOException {
        Parent root = FXMLLoader.load(getClass().getResource("Screens/LoginScre.fxml"));
        stage.setTitle("Appointment Management System (AMS)");
        stage.setScene(new Scene(root));
        stage.show();

        //  center the form on the screen
        Rectangle2D screBoundary = Screen.getPrimary().getVisualBounds();
        stage.setX((screBoundary.getWidth() - stage.getWidth()) / 2);
        stage.setY((screBoundary.getHeight() - stage.getHeight()) / 2);
    }

    public static TimeZone getUserTimeZone() {return userTimeZone;}



    public static void main(String[] args) throws ClassNotFoundException, SQLException {

        mysqlConn = DBConnection.startConnection();
        launch(args);
    }

    @FXML @Override  public void initialize(URL url, ResourceBundle resourceBundle) {}
}