package teoespero.jappointment.Model;

import java.time.Month;

public class MonthlyTypeReport {

    private int year;
    private Month month;
    private String type;
    private int total;

    public MonthlyTypeReport() {}
    public MonthlyTypeReport(int year, Month month, String type, int total) {
        this.year = year;
        this.month = month;
        this.type = type;
        this.total = total;
    }

    public int getYear() {return year;}
    public Month getMonth() {return month;}
    public String getType() {return type;}
    public int getTotal() {return total;}

    public void setYear(int year) {this.year = year;}
    public void setMonth(Month month) {this.month = month;}
    public void setType(String type) {this.type = type;}
    public void setTotal(int total) {this.total = total;}
}
