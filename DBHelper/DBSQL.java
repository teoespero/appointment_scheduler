package teoespero.jappointment.DBHelper;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;

/**
 * <p>The <b>DBSQL Class</b> provides the mechanism that creates the query statements that allows access to
 * the database.</p>
 * @author Teodulfo Espero (BS Software Development, WGU)
 * @since 01.05212023
 */
public class DBSQL {

    private static PreparedStatement statement;

    /**
     * <p>Mechanism for creating the prepared SQL statements.</p>
     * @param conn <p>The database connection from the application.</p>
     * @param sqlStatement <p>The SQL statement passed on by the different functions of the application.</p>
     * @throws SQLException <p>The error thrown in preparing the SQL statement (prepareStatement).</p>
     */
    public static void setPreparedStatement(Connection conn, String sqlStatement) throws SQLException {

        statement = conn.prepareStatement(sqlStatement, PreparedStatement.RETURN_GENERATED_KEYS);
    }

    /**
     * <p>Returns the prepared SQL statement.</p>
     * @return The SQL statement prepared.
     */
    public static PreparedStatement getPreparedStatement() {

        return statement;
    }
}
