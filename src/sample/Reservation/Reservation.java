package sample.Reservation;

import java.sql.Date;
import java.time.LocalDate;

public class Reservation {
    int idtable_reservation;
    int table_number;
    String reservator_name;
    Date time;

    public Reservation(int idtable_reservation, int table_number, String reservator_name, Date time) {
        this.idtable_reservation = idtable_reservation;
        this.table_number = table_number;
        this.reservator_name = reservator_name;
        this.time = time;
    }

    public int getIdtable_reservation() {
        return idtable_reservation;
    }

    public void setIdtable_reservation(int idtable_reservation) {
        this.idtable_reservation = idtable_reservation;
    }

    public int getTable_number() {
        return table_number;
    }

    public void setTable_number(int table_number) {
        this.table_number = table_number;
    }

    public String getReservator_name() {
        return reservator_name;
    }

    public void setReservator_name(String reservator_name) {
        this.reservator_name = reservator_name;
    }

    public Date getTime() {
        return time;
    }

    public void setTime(Date time) {
        this.time = time;
    }
}
