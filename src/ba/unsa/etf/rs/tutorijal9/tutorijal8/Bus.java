package ba.unsa.etf.rs.tutorijal9.tutorijal8;

import javafx.beans.property.SimpleIntegerProperty;
import javafx.beans.property.SimpleStringProperty;

public class Bus {
    private SimpleIntegerProperty id = new SimpleIntegerProperty();
    private SimpleStringProperty proizvodjac = new SimpleStringProperty();
    private SimpleStringProperty serija = new SimpleStringProperty();
    private SimpleIntegerProperty numberOfSeats = new SimpleIntegerProperty();
    private SimpleIntegerProperty numberOfDrivers = new SimpleIntegerProperty();
    private SimpleIntegerProperty FirstDriver = new SimpleIntegerProperty();
    private SimpleIntegerProperty SecondDriver = new SimpleIntegerProperty();

    public Bus(Integer id, String proizvodjac, String serija, Integer numberOfSeats, Integer numberOfDrivers, Integer firstDriver, Integer secondDriver) {
        this.id.set(id);
        this.proizvodjac.set(proizvodjac);
        this.serija.set(serija);
        this.numberOfSeats.set(numberOfSeats);
        this.numberOfDrivers.set(numberOfDrivers);
        FirstDriver.set(firstDriver);
        SecondDriver.set(secondDriver);
    }

    public Bus(String proizvodjac, String serija, int brojSjedista) {
        this.proizvodjac.set(proizvodjac);
        this.serija.set(serija);
        this.numberOfSeats.set(brojSjedista);
    }

    public Bus() {

    }

    public int getId() {
        return id.get();
    }

    public SimpleIntegerProperty idProperty() {
        return id;
    }

    public void setId(int id) {
        this.id.set(id);
    }

    public String getProizvodjac() {
        return proizvodjac.get();
    }

    public SimpleStringProperty proizvodjacProperty() {
        return proizvodjac;
    }

    public void setProizvodjac(String proizvodjac) {
        this.proizvodjac.set(proizvodjac);
    }

    public String getSerija() {
        return serija.get();
    }

    public SimpleStringProperty serijaProperty() {
        return serija;
    }

    public void setSerija(String serija) {
        this.serija.set(serija);
    }

    public int getNumberOfSeats() {
        return numberOfSeats.get();
    }

    public SimpleIntegerProperty numberOfSeatsProperty() {
        return numberOfSeats;
    }

    public void setNumberOfSeats(int numberOfSeats) {
        this.numberOfSeats.set(numberOfSeats);
    }

    public int getNumberOfDrivers() {
        return numberOfDrivers.get();
    }

    public SimpleIntegerProperty numberOfDriversProperty() {
        return numberOfDrivers;
    }

    public void setNumberOfDrivers(int numberOfDrivers) {
        this.numberOfDrivers.set(numberOfDrivers);
    }

    public int getFirstDriver() {
        return FirstDriver.get();
    }

    public SimpleIntegerProperty firstDriverProperty() {
        return FirstDriver;
    }

    public void setFirstDriver(int firstDriver) {
        this.FirstDriver.set(firstDriver);
    }

    public int getSecondDriver() {
        return SecondDriver.get();
    }

    public SimpleIntegerProperty secondDriverProperty() {
        return SecondDriver;
    }

    public void setSecondDriver(int secondDriver) {
        this.SecondDriver.set(secondDriver);
    }


    @Override
    public String toString() {
        return proizvodjac+" "+serija ;
    }
}

