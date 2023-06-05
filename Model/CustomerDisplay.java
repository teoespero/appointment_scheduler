package teoespero.jappointment.Model;

import teoespero.jappointment.DataAccessObjects.JDivisionDAO;

import java.sql.SQLException;

public class CustomerDisplay extends Customer {
    String divisionName;
    public CustomerDisplay(Customer customer) throws SQLException {

        this.customerID = customer.customerID;
        this.customerName = customer.customerName;
        this.address = customer.address;
        this.postalCode = customer.postalCode;
        this.phone = customer.phone;
        this.createDate = customer.createDate;
        this.createdBy = customer.createdBy;
        this.lastUpdateTime = customer.lastUpdateTime;
        this.lastUpdatedBy = customer.lastUpdatedBy;
        this.divisionID = customer.divisionID;

        try {
            this.divisionName = JDivisionDAO.JDivisionSelectByID(customer.divisionID).getDivisionName();
        }
        catch(SQLException e) {
            System.out.println(e.getMessage());
        }
    }

    public String getDivisionName() {return divisionName;}
    public void setDivisionName(String divisionName) {this.divisionName = divisionName;}
}
