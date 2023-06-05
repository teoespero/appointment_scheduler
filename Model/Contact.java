package teoespero.jappointment.Model;

/**
 * <p>The <b>Contact Class</b> provides the Data Object Model (DOM) for the contact information.</p>
 * @author Teodulfo Espero (BS Software Development, WGU)
 * @since 01.03212023
 */
public class Contact {

    ////////////////////////////////////////////////////////////////////////////////////////////////////////////
    //  Define the Members of the Class
    private int contactID; //   unique ID for the contact
    private String contactName; // the Contact Name
    private String email;   //  the contact Email Address

    ////////////////////////////////////////////////////////////////////////////////////////////////////////////
    //  Constructors

    /**
     * <p>Constructor that does not use any arguments.</p>
     */
    public Contact() {}

    /**
     * <p>Constructor that uses arguments in creating instances of the Contact object.</p>
     * @param contactName <p>The Contact Name (String)</p>
     * @param email <p>The Contact Email Address (String)</p>
     */
    public Contact(String contactName, String email) {
        this.contactName = contactName;
        this.email = email;
    }


    ////////////////////////////////////////////////////////////////////////////////////////////////////////////
    //  Getter

    /**
     * <p>Returns the Contact ID from the database.</p>
     * @return <p>The Contact ID (int).</p>
     */
    public int getJAppointmentContactID() {return contactID;}

    /**
     * <p>Returns the Contact Name from the database.</p>
     * @return <p>The Contact Name (String).</p>
     */
    public String getJAppointmentContactName() {return contactName;}

    ////////////////////////////////////////////////////////////////////////////////////////////////////////////
    //  Setter

    /**
     * <p>Sets the Contact ID.</p>
     * @param contactID <p>The Contact ID (int).</p>
     */
    public void setContactID(int contactID) {this.contactID = contactID;}

    /**
     * <p>Sets the Contact Name</p>
     * @param contactName <p>The Contact Name (String).</p>
     */
    public void setContactName(String contactName) {this.contactName = contactName;}

    /**
     * <p>Sets the Contact Email Address.</p>
     * @param email <p>The Email Address (String).</p>
     */
    public void setEmail(String email) {this.email = email;}

    /**
     * Override method, returns the Contact Name.
     * @return <p>The Contact Name (String).</p>
     */
    @Override public String toString() {return contactName;}
}
