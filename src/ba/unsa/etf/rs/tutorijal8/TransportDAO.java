package ba.unsa.etf.rs.tutorijal8;

import javafx.beans.property.ObjectProperty;
import javafx.beans.property.SimpleObjectProperty;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;

import java.sql.*;
import java.time.LocalDate;
import java.util.ArrayList;

public class TransportDAO {

    private static TransportDAO instance = null;
    private Connection conn;
    private ObservableList<Bus> BUSlist = FXCollections.observableArrayList();
    private ObjectProperty<Bus> currentBus = null;
    private ObservableList<Driver> DRIVERlist = FXCollections.observableArrayList();
    private ObjectProperty<Driver> currentDriver = null;
    private PreparedStatement addDriverST;
    private PreparedStatement getDriverST;
    private PreparedStatement deleteDriverST;
    private PreparedStatement updateDriverST;
    private PreparedStatement addBusST;
    private PreparedStatement getBusST;
    private PreparedStatement deleteBusST;
    private PreparedStatement updateBusST;
    private PreparedStatement truncDriver;
    private PreparedStatement truncBus;
    private PreparedStatement DriverID;
    private PreparedStatement BusID;
    private PreparedStatement addDriverBusST;
    private PreparedStatement getAssignmentDriver;
    private PreparedStatement deleteAssignmentBus;
    private PreparedStatement deleteAssignmentDriver;
    private PreparedStatement truncAssignment; //dodjela
    private PreparedStatement resetAutoIncrementAssignment;
    private PreparedStatement resetAutoIncrementDrivers;
    private PreparedStatement resetAutoIncrementBuses;
    private PreparedStatement deleteAssignmentDriverBus;

    public static TransportDAO getInstance () {
        if (instance == null) {
            initialize();
        }
        return instance;
    }

    public TransportDAO() {
        prepareStatements();
        ucitajVozace();
        ucitajBuseve();
    }

    private static void initialize() {
        instance = new TransportDAO();
    }

    private void prepareStatements() {
        try {
            conn = DriverManager.getConnection("jdbc:sqlite:proba.db");
            Class.forName("org.sqlite.JDBC");
            DriverID = conn.prepareStatement("SELECT max(id) + 1 FROM drivers");
            BusID = conn.prepareStatement("SELECT max(id) + 1 FROM buses");
            addDriverST = conn.prepareStatement("INSERT INTO drivers(id, name, surname, jmb, birth, hire_date)" +
                    " VALUES(?,?,?,?,?,?)");
            addBusST = conn.prepareStatement("INSERT INTO buses(id, proizvodjac, serija, broj_sjedista)" +
                    " VALUES(?, ?, ?, ?)");
            getBusST = conn.prepareStatement("SELECT id, proizvodjac, serija, broj_sjedista" +
                    " FROM buses");
            getAssignmentDriver = conn.prepareStatement("SELECT DISTINCT dr.id, dr.name, dr.surname, dr.jmb, dr.birth, dr.hire_date" +
                    " FROM dodjela d INNER JOIN drivers dr ON (d.driver_id = dr.id) WHERE d.bus_id=?");
            getDriverST = conn.prepareStatement("SELECT id, name, surname, jmb, birth, hire_date" +
                    " FROM drivers");
            deleteAssignmentBus = conn.prepareStatement("DELETE FROM dodjela WHERE bus_id = ?; COMMIT;");
            deleteAssignmentDriver = conn.prepareStatement("DELETE FROM dodjela WHERE driver_id = ?; COMMIT;");
            deleteDriverST = conn.prepareStatement("DELETE FROM Drivers WHERE id = ?; COMMIT;");
            deleteBusST = conn.prepareStatement("DELETE FROM buses WHERE id = ?; COMMIT;");
            truncBus = conn.prepareStatement("DELETE FROM buses WHERE 1=1; COMMIT;");
            truncDriver = conn.prepareStatement("DELETE FROM drivers WHERE 1=1; COMMIT;");
            truncAssignment = conn.prepareStatement("DELETE FROM dodjela WHERE 1=1; COMMIT;");
            resetAutoIncrementAssignment = conn.prepareStatement("DELETE FROM SQLITE_SEQUENCE WHERE name='dodjela'; COMMIT;");
            resetAutoIncrementBuses = conn.prepareStatement("DELETE FROM SQLITE_SEQUENCE WHERE name='buses'; COMMIT;");
            resetAutoIncrementDrivers = conn.prepareStatement("DELETE FROM SQLITE_SEQUENCE WHERE name='drivers'; COMMIT;");
            addDriverBusST = conn.prepareStatement("INSERT OR REPLACE INTO dodjela(bus_id, driver_id)" +
                    " VALUES (?,?); COMMIT; ");
            updateDriverST = conn.prepareStatement("UPDATE drivers SET name = ?, surname = ?, jmb = ?, " +
                    "birth = ?, hire_date = ? WHERE id = ?; COMMIT; ");
            updateBusST = conn.prepareStatement("UPDATE buses SET proizvodjac = ?, serija = ?," +
                    " broj_sjedista = ? WHERE id = ?; COMMIT;");
            deleteAssignmentDriverBus = conn.prepareStatement("DELETE FROM dodjela WHERE bus_id = ? AND driver_id = ?; COMMIT;");
        } catch (SQLException e) {
            e.printStackTrace();
        } catch (ClassNotFoundException e) {
            System.out.println("Nije pronadjen driver za konekciju na bazu");
            e.printStackTrace();
        }
    }

    public void ucitajBuseve() {
        BUSlist =  FXCollections.observableArrayList(getBusses());
        if (BUSlist.size() > 0) {
            currentBus = new SimpleObjectProperty<>(BUSlist.get(0)) ;
        } else {
            currentBus = new SimpleObjectProperty<>(new Bus());
        }
    }

    public void ucitajVozace() {
        DRIVERlist = FXCollections.observableArrayList(getDrivers());
        if (DRIVERlist.size() > 0) {
            currentDriver = new SimpleObjectProperty<>(getDRIVERlist().get(0));
        } else {
            currentDriver = new SimpleObjectProperty<>(new Driver());
        }
    }

    public static void removeInsance() {
        if (instance != null) {
            try {
                instance.conn.close();
            } catch (SQLException e) {
                e.printStackTrace();
            }
        }
        instance = null;
    }

    public ArrayList<Bus> getBusses() {
        ArrayList<Bus> buses = new ArrayList<>();
        try {
            ResultSet result = getBusST.executeQuery();
            while(result.next()) {
                Integer id = result.getInt(1);
                String maker = result.getString(2);
                String series = result.getString(3);
                int seatNumber = result.getInt(4);
                getAssignmentDriver.setInt(1, id);
                ResultSet result2 = getAssignmentDriver.executeQuery();
                ArrayList<Driver> drivers = new ArrayList<Driver>();
                while (result2.next()) {
                    Integer idDriver = result2.getInt(1);
                    String name = result2.getString(2);
                    String surname = result2.getString(3);
                    String jmb = result2.getString(4);
                    Date birthDate = result2.getDate(5);
                    Date hireDate = result2.getDate(5);
                    drivers.add(new Driver(idDriver, name, surname, jmb, birthDate.toLocalDate(), hireDate.toLocalDate()));
                }
                if (drivers.size() == 0) {
                    buses.add(new Bus(id, maker, series, seatNumber, null, null));
                } else if (drivers.size() == 1) {
                    buses.add(new Bus(id, maker, series, seatNumber, drivers.get(0), null));
                } else {
                    buses.add(new Bus(id, maker, series, seatNumber, drivers.get(0), drivers.get(1)));
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return buses;

    }

    public ArrayList<Driver> getDrivers() {
        ArrayList<Driver> drivers = new ArrayList<>();
        try {
            ResultSet result = getDriverST.executeQuery();
            while (result.next()) {
                Integer idDriver = result.getInt(1);
                String name = result.getString(2);
                String surname = result.getString(3);
                String jmb = result.getString(4);
                Date birthDate = result.getDate(5);
                Date hireDate = result.getDate(6);
                drivers.add(new Driver(idDriver, name, surname, jmb, birthDate.toLocalDate(), hireDate.toLocalDate()));
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return drivers;
    }


    public void addNewDriver(String name, String surname, int jmb, LocalDate dateOfBirth, LocalDate hireDate) {
        try {
            ResultSet result = DriverID.executeQuery();
            result.next();
            Integer id = result.getInt(1);
            if (id == null) {
                id = 1;
            }
            addDriverST.setInt(1, id);
            addDriverST.setString(2, name);
            addDriverST.setString(3, surname);
            addDriverST.setInt(4, jmb);
            addDriverST.setDate(5, Date.valueOf(dateOfBirth));
            addDriverST.setDate(6, Date.valueOf(hireDate));
            addDriverST.executeUpdate();
        } catch (SQLException e) {
            throw new IllegalArgumentException();
        }
    }


    public void addBus(Bus bus) {
        try {
            ResultSet result = BusID.executeQuery();
            result.next();
            Integer id = result.getInt(1);
            if (id == null) {
                id = 1;
            }
            addBusST.setInt(1, id);
            addBusST.setString(2, bus.getMaker());
            addBusST.setString(3, bus.getSeries());
            addBusST.setInt(4, bus.getSeatNumber());
            addBusST.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }


    public void addDriver(Driver driver) {
        try {
            ResultSet result = DriverID.executeQuery();
            result.next();
            Integer id = result.getInt(1);
            System.out.println(id);
            if (id == null) {
                id = 0;
            }

            addDriverST.setInt(1, id);
            addDriverST.setString(2, driver.getName());
            addDriverST.setString(3, driver.getSurname());
            if(driver.getJMBG().equals("NULL") || driver.getJMBG().equals("")) {
                addDriverST.setString(4, "NULLid " + id);
            } else {
                addDriverST.setString(4, driver.getJMBG());
            }
            addDriverST.setDate(5, Date.valueOf(driver.getBirthdayDate()));
            addDriverST.setDate(6, Date.valueOf(driver.getEmploymentDate()));
            addDriverST.executeUpdate();
        } catch (SQLException e) {
            throw new IllegalArgumentException("Taj vozač već postoji!");
        }
    }

    public void deleteDriver(Driver driver) {
        try {
            deleteAssignmentDriver.setInt(1, driver.getId());
            deleteAssignmentDriver.executeUpdate();
            deleteDriverST.setInt(1, driver.getId());
            deleteDriverST.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public void deleteCurrentDriver() {
        try {
            if (currentDriver != null) {
                deleteAssignmentDriver.setInt(1, currentDriver.get().getId());
                deleteAssignmentDriver.executeUpdate();
                deleteDriverST.setInt(1, currentDriver.get().getId());
                deleteDriverST.executeUpdate();
                ucitajBuseve();
                ucitajVozace();
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }


    public void updateDriver (Driver driver) {
        try {
            updateDriverST.setString(1, driver.getName());
            updateDriverST.setString(2, driver.getSurname());
            updateDriverST.setString(3, driver.getJMBG());
            updateDriverST.setDate(4, Date.valueOf(driver.getBirthdayDate()));
            System.out.println(Date.valueOf(driver.getBirthdayDate()) + " " + Date.valueOf(driver.getEmploymentDate()));
            updateDriverST.setDate(5, Date.valueOf(driver.getEmploymentDate()));
            updateDriverST.setInt(6, driver.getId());
            updateDriverST.executeUpdate();
            currentDriver.set(driver);
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public void updateBus (Bus bus) {
        try {
            updateBusST.setString(1, bus.getMaker());
            updateBusST.setString(2, bus.getSeries());
            updateBusST.setInt(3, bus.getSeatNumber());
            updateBusST.setInt(4, bus.getId());
            updateBusST.executeUpdate();
            this.currentBus.set(bus);
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }



    public void deleteBus(Bus bus) {
        try {
            deleteAssignmentBus.setInt(1, bus.getId());
            deleteAssignmentBus.executeUpdate();
            deleteBusST.setInt(1, bus.getId());
            deleteBusST.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        }

    }

    public void deleteDodjela(Driver driver, Bus bus) {
        try {
            System.out.println("Bisanje busID:" + bus.getId());
            System.out.println("Bisanje driverID:" + driver.getId());
            deleteAssignmentDriverBus.setInt(1, bus.getId());
            deleteAssignmentDriverBus.setInt(2, driver.getId());
            deleteAssignmentDriverBus.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        } catch (NullPointerException e) {
            System.out.println("Nije nista obrisano jer nema takve dodjele");
        }
    }


    public void dodijeliVozacuAutobus(Driver driver, Bus bus, int which) {
        try {
            System.out.println("Dodjela busID:" + bus.getId());
            System.out.println("Dodjela driverID:" + driver.getId());
            addDriverBusST.setInt(1, bus.getId());
            addDriverBusST.setInt(2, driver.getId());
            addDriverBusST.executeUpdate();
            if (which == 1) {
                bus.setDriverOne(driver);
            } else {
                bus.setDriverTwo(driver);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        } catch (NullPointerException e) {
            System.out.println("Nije nista dodjeljeno");
        }
    }

    public void resetDatabase() {
        try {
            truncAssignment.executeUpdate();
            truncDriver.executeUpdate();
            truncBus.executeUpdate();
            resetAutoIncrementAssignment.executeUpdate();
            resetAutoIncrementDrivers.executeUpdate();
            resetAutoIncrementBuses.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public ObservableList<Bus> getBUSlist() {
        return BUSlist;
    }

    public void setBUSlist(ObservableList<Bus> BUSlist) {
        this.BUSlist = BUSlist;
    }

    public Bus getCurrentBus() {
        return currentBus.get();
    }

    public ObjectProperty<Bus> currentBusProperty() {
        return currentBus;
    }

    public void setCurrentBus(Bus currentBus) {
        this.currentBus.set(currentBus);
    }

    public ObservableList<Driver> getDRIVERlist() {
        return DRIVERlist;
    }

    public void setDRIVERlist(ObservableList<Driver> DRIVERlist) {
        this.DRIVERlist = DRIVERlist;
    }

    public Driver getCurrentDriver() {
        return currentDriver.get();
    }

    public ObjectProperty<Driver> currentDriverProperty() {
        return currentDriver;
    }

    public void setCurrentDriver(Driver currentDriver) {
        this.currentDriver.set(currentDriver);
    }
}