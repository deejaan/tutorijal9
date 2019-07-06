package ba.unsa.etf.rs.tutorijal9.tutorijal8;

import javafx.beans.property.SimpleStringProperty;
import org.sqlite.JDBC;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.sql.*;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Scanner;

public class TransportDAO {

    private Connection conn;
    private static PreparedStatement dajVozaceUpit,dajBusUpit,
            odrediIdDriveraUpit,truncVozaciBuseva, dodajVouzacaBusa , addDriver , obrisiBusUpit ,
            dodajBusUpit ,obrisiDriverUpit ,odrediIdBusaUpit, truncBus , truncDriver ,
            updateBus ,updateDriver,dajJMB , getDodjelaVozaci , busById , driverById;


    private static TransportDAO instance;
    private Driver driver;
    static {
        try {
            DriverManager.registerDriver(new JDBC());
        }
        catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public static TransportDAO getInstance() {
        if(instance == null) instance = new TransportDAO();
        return instance;
    }

    private TransportDAO(){
        try {
            conn = DriverManager.getConnection("jdbc:sqlite:Baza.db");
        } catch (SQLException e) {
            e.printStackTrace();
        }
        try {
            obrisiBusUpit = conn.prepareStatement("DELETE FROM Bus WHERE bus_id=?");
            obrisiDriverUpit = conn.prepareStatement("DELETE FROM Vozac WHERE  vozac_id=?");
            addDriver = conn.prepareStatement("INSERT INTO Vozac VALUES (?,?,?,?,?,?)");
            dajVozaceUpit = conn.prepareStatement("SELECT * FROM Vozac;");
            dajBusUpit = conn.prepareStatement("SELECT * FROM Bus");
            dodajBusUpit = conn.prepareStatement("INSERT INTO Bus VALUES(?,?,?,?,?)");
            // daj id
            odrediIdBusaUpit = conn.prepareStatement("SELECT MAX(bus_id)+1 FROM Bus");
            odrediIdDriveraUpit = conn.prepareStatement("SELECT MAX(vozac_id)+1 FROM Vozac");
            // Delete
            truncBus = conn.prepareStatement("DELETE FROM Bus");
            truncDriver = conn.prepareStatement("DELETE FROM Vozac");
            truncVozaciBuseva = conn.prepareStatement("DELETE FROM VozaciBuseva");
            // update
            updateDriver = conn.prepareStatement("UPDATE Vozac SET ime=?, prezime=?, JMB=?, datum_rodjenja=?, datum_zaposljenja=? WHERE vozac_id=?; commit;");
            updateBus = conn.prepareStatement("UPDATE Bus SET proizvodjac=?, serija=?, broj_sjedista=?, broj_vozaca=? WHERE bus_id=?; commit; ");
            // by Id
            busById = conn.prepareStatement("SELECT bus_id, proizvodjac, serija, broj_sjedista, broj_vozaca from Bus where bus_id =?");
            driverById = conn.prepareStatement("SELECT vozac_id, ime, prezime, JMB, datum_rodjenja, datum_zaposljenja  from Vozac where vozac_id =?");

        } catch (SQLException e) {
            regenerisiBazu();
            e.printStackTrace();
        }
    }
    //Rjesavamo slucaj ukoliko baza nije kreirana!
    public void regenerisiBazu() {
        Scanner ulaz = null;
        try {
            ulaz = new Scanner(new FileInputStream("Baza.db.sql"));
            String sqlUpit = "";
            while (ulaz.hasNext()){
                sqlUpit += ulaz.nextLine();
                if (sqlUpit.charAt(sqlUpit.length() - 1 ) == ';'){
                    try {
                        Statement stmt = conn.createStatement();
                        stmt.execute(sqlUpit);
                        sqlUpit = "";
                    } catch (SQLException e) {
                        e.printStackTrace();
                    }
                }
            }
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }
        ulaz.close();
    }
    public static void removeInstance() {
        if (instance == null) return;
        instance.close();
        instance = null;
    }
    public void close() {
        try {
            conn.close();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public Date convertToDateViaSqlDate(LocalDate dateToConvert) {
        return java.sql.Date.valueOf(dateToConvert);
    }

    public void addDriver(Driver driver){
        try {
            ResultSet rs = odrediIdDriveraUpit.executeQuery();
            int id = 1;
            if (rs.next()) {
                id = rs.getInt(1);
            }
            addDriver.setInt(1, id);
            addDriver.setString(2, driver.getIme());
            addDriver.setString(3, driver.getPrezime());
            addDriver.setString(4 , driver.getJMB());
            addDriver.setDate(5 , convertToDateViaSqlDate(driver.getBirthDate()));
            addDriver.setDate(6 , convertToDateViaSqlDate(driver.getWorkDate()));
            addDriver.executeUpdate();
        } catch (SQLException e) {
            throw new IllegalArgumentException("Taj vozač već postoji!");
        }
    }
    public void addBus(Bus bus) {
        try {
            ResultSet rs = odrediIdBusaUpit.executeQuery();
            int id = 1;
            if (rs.next()) {
                id = rs.getInt(1);
            }
            dodajBusUpit.setInt(1, id);
            dodajBusUpit.setString(2, bus.getProizvodjac());
            dodajBusUpit.setString(3, bus.getSerija());
            dodajBusUpit.setInt(4, bus.getNumberOfSeats());
            dodajBusUpit.setInt(5,bus.getNumberOfDrivers());
            dodajBusUpit.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public ArrayList<Driver> getDrivers() {
        ArrayList<Driver> drivers = new ArrayList<Driver>();
        ResultSet result = null;
        try {
            result = dajVozaceUpit.executeQuery();
            Driver driver;
            while (  ( driver = dajVozaceUpit(result) ) != null )
                drivers.add(driver);
            result.close();
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return drivers;
    }

    public ArrayList<Bus> getBusses() {
        ArrayList<Bus> busevi = new ArrayList<Bus>();
        ResultSet result = null;
        try {
            result = dajBusUpit.executeQuery();
            Bus bus;
            while ( ( bus = dajBusUpit(result) ) != null )
                busevi.add(bus);
            result.close();
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return busevi;
    }

    public LocalDate convertToLocalDateViaSqlDate(Date dateToConvert) {
        return new java.sql.Date(dateToConvert.getTime()).toLocalDate();
    }
    private Driver dajVozaceUpit(ResultSet result) {
        Driver driver = null;
        try {
            if (result.next() ){
                int id = result.getInt("vozac_id");
                String name = result.getString("ime");
                String surname = result.getString("prezime");
                String jmb = result.getString("JMB");
                LocalDate rodjendan = result.getDate("datum_rodjenja").toLocalDate();
                LocalDate datum_zap = result.getDate("datum_zaposljenja").toLocalDate();

                driver = new Driver( name , surname , jmb , rodjendan , datum_zap);
                driver.setId(id);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return driver;
    }


    private Bus dajBusUpit(ResultSet result) {
        Bus bus = null;
        try {
            if (result.next() ){
                int id = result.getInt("bus_id");
                String proizvodjac = result.getString("proizvodjac");
                String serija = result.getString("serija");
                int brojSjedista = result.getInt("broj_sjedista");

                bus = new Bus( proizvodjac , serija , brojSjedista);
                bus.setId(id);

            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return bus;
    }

    public void izmijeniDrivera (Driver driver) {
        try {
            driverById.clearParameters();
            driverById.setInt(1,driver.getId());
            ResultSet res = driverById.executeQuery();
            while(res.next()) {
                updateDriver.setString(1, driver.getIme());
                updateDriver.setString(2, driver.getPrezime());
                updateDriver.setString(3, driver.getJMB());
                updateDriver.setDate(4, Date.valueOf(driver.getBirthDate()));
                updateDriver.setDate(5, Date.valueOf(driver.getWorkDate()));
                updateDriver.executeUpdate();
            }
        } catch (SQLException e) {
            e.printStackTrace();
            System.out.println(e.getMessage());
        }
    }

    public void izmijeniBus(Bus bus) {
        try {
            /*busById.clearParameters();
            busById.setInt(1,bus.getId());
            ResultSet res = busById.executeQuery();
            while(res.next()) {
                updateBus.clearParameters();*/
            updateBus.setString(1, bus.getProizvodjac());
            updateBus.setString(2, bus.getSerija());
            updateBus.setInt(3, bus.getNumberOfSeats());
            updateBus.setInt(4, bus.getNumberOfDrivers());
            updateBus.executeUpdate();
            //}
        } catch (SQLException e) {
            e.printStackTrace();
            System.out.println(e.getMessage());
        }
    }


    public void deleteBus(Bus bus) {
        try {
            obrisiBusUpit.setInt(1, bus.getId());
            obrisiBusUpit.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public void deleteDriver(Driver driver) {
        try {
            obrisiDriverUpit.setInt(1, driver.getId());
            obrisiDriverUpit.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public void resetDatabase() {
        try {
            truncVozaciBuseva.executeUpdate();
            truncBus.executeUpdate();
            truncDriver.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }



}