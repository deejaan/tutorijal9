package ba.unsa.etf.rs.tutorijal9.tutorijal8;

import javafx.beans.property.SimpleIntegerProperty;
import javafx.beans.property.SimpleObjectProperty;
import javafx.beans.property.SimpleStringProperty;

import java.time.LocalDate;

public class Driver {
    private SimpleIntegerProperty id = new SimpleIntegerProperty();
    private SimpleStringProperty Ime = new SimpleStringProperty("");
    private SimpleStringProperty Prezime= new SimpleStringProperty("");;
    private SimpleStringProperty JMB= new SimpleStringProperty("");;
    private SimpleObjectProperty<LocalDate> BirthDate= new SimpleObjectProperty<>();
    private SimpleObjectProperty<LocalDate> WorkDate= new SimpleObjectProperty<>();

    public Driver(String ime, String prezime, String JMB, LocalDate birthDate, LocalDate workDate) {
        Ime.set(ime);
        Prezime.set(prezime);
        this.JMB.set(JMB);
        BirthDate.set(birthDate);
        WorkDate.set(workDate);
    }


    public Driver() { }



    public String getIme() {
        return Ime.get();
    }

    public SimpleStringProperty imeProperty() {
        return Ime;
    }

    public void setIme(String ime) {
        this.Ime.set(ime);
    }

    public String getPrezime() {
        return Prezime.get();
    }

    public SimpleStringProperty prezimeProperty() {
        return Prezime;
    }

    public void setPrezime(String prezime) {
        this.Prezime.set(prezime);
    }

    public String getJMB() {
        return JMB.get();
    }

    public SimpleStringProperty JMBProperty() {
        return JMB;
    }

    public void setJMB(String JMB) {
        this.JMB.set(JMB);
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

    public LocalDate getBirthDate() {
        return BirthDate.get();
    }

    public SimpleObjectProperty<LocalDate> birthDateProperty() {
        return BirthDate;
    }

    public void setBirthDate(LocalDate birthDate) {
        this.BirthDate.set(birthDate);
    }

    public LocalDate getWorkDate() {
        return WorkDate.get();
    }

    public SimpleObjectProperty<LocalDate> workDateProperty() {
        return WorkDate;
    }

    public void setWorkDate(LocalDate workDate) {
        this.WorkDate.set(workDate);
    }

    @Override
    public String toString() {
        return  Ime + " " + Prezime ;
    }


}