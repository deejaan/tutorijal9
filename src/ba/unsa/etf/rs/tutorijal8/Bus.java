package ba.unsa.etf.rs.tutorijal8;

import javafx.beans.property.SimpleObjectProperty;

public class Bus {

    private Integer id = null;
    private String maker = "NULL";
    private String series = "NULL";
    private int seatNumber = -1;
    private SimpleObjectProperty<Driver> driverOne = new SimpleObjectProperty<>();
    private SimpleObjectProperty<Driver> driverTwo = new SimpleObjectProperty<>();

    public Bus(String maker, String series, int seatNumber) {
        this.maker = maker;
        this.series = series;
        this.seatNumber = seatNumber;
    }

    public Bus(Integer id, String maker, String series, int seatNumber) {
        this.id = id;
        this.maker = maker;
        this.series = series;
        this.seatNumber = seatNumber;
    }

    public Bus(int id, String maker, String series, int seatNumber, Driver driverOne, Driver driverTwo) {
        this.id = id;
        this.maker = maker;
        this.series = series;
        this.seatNumber = seatNumber;
        setDriverOne(driverOne);
        setDriverTwo(driverTwo);
    }

    public Bus() {
    }


    public String getMaker() {
        return maker;
    }

    public void setMaker(String maker) {
        this.maker = maker;
    }

    public String getSeries() {
        return series;
    }

    public void setSeries(String series) {
        this.series = series;
    }

    public int getSeatNumber() {
        return seatNumber;
    }

    public void setSeatNumber(int seatNumber) {
        this.seatNumber = seatNumber;
    }

    public Integer getId() {
        return id;
    }

    private Driver getDriverOne() {
        return driverOne.get();
    }

    public SimpleObjectProperty<Driver> driverOneProperty() {
        return driverOne;
    }

    public void setDriverOne(Driver driverOne) {
        this.driverOne.set(driverOne);
    }

    private Driver getDriverTwo() {
        return driverTwo.get();
    }

    public SimpleObjectProperty<Driver> driverTwoProperty() {
        return driverTwo;
    }

    public void setDriverTwo(Driver driverTwo) {
        this.driverTwo.set(driverTwo);
    }


    @Override
    public String toString () {
        String name = "";
        name += this.maker + " " + this.series + " ( seats: " + this.getSeatNumber() + " )";
        if (driverOne != null) {
            name += driverOne.toString();
        }
        if (driverTwo != null) {
            name += driverTwo.toString();
        }
        return name;
    }

    public boolean equals(Bus bus) {
        if (id != null) {
            return (bus.getId().equals(this.getId()));
        }
        return false;
    }
}