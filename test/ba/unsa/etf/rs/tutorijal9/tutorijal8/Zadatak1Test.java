package ba.unsa.etf.rs.tutorijal9.tutorijal8;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;


import java.io.ByteArrayInputStream;
import java.io.InputStream;
import java.time.LocalDate;
import java.util.ArrayList;

import static org.junit.jupiter.api.Assertions.*;

class Zadatak1Test {

    @BeforeEach
    void prepareDatabase(){
        TransportDAO dao = TransportDAO.getInstance();
        dao.resetDatabase();
    }

    @Test
    void dodajVozaca() {
        InputStream defaultSystemIn = setupCustomInputStreamAndReturnStandardInputStream(
                "dodaj vozaca\nTest\nTestovic\n1111111111111\n11.2.1997\n11.3.2018\n");

        String[] args= {"a","b"};
        Main.main(args);
        System.setIn(defaultSystemIn);
        TransportDAO dao = TransportDAO.getInstance();
        ArrayList<Driver> drivers = dao.getDrivers();
        assertAll(()->{
            assertEquals(1,drivers.size());
            assertEquals("Test",drivers.get(0).getName());
            assertEquals(LocalDate.of(1997,2,11),drivers.get(0).getBirthday());
        });
    }


    @Test
    void dodajIOtpustiVozaca() {
        InputStream defaultSystemIn = setupCustomInputStreamAndReturnStandardInputStream(
                "dodaj vozaca\nTest\nTestovic\n1111111111111\n11.2.1997\n11.3.2018\n" +
                "dodaj vozaca\nPriprema\nPripremovic\n2222222222222\n1.2.1990\n2.3.2017\n" + "otpusti vozaca\n1\n");
        String[] args= {"a","b"};
        Main.main(args);
        System.setIn(defaultSystemIn);
        TransportDAO dao = TransportDAO.getInstance();
        ArrayList<Driver> drivers = dao.getDrivers();
        assertAll(()->{
            assertEquals(1,drivers.size());
            assertEquals("Priprema",drivers.get(0).getName());
        });
    }

    @Test
    void dodajAutobus() {
        InputStream defaultSystemIn = setupCustomInputStreamAndReturnStandardInputStream(
                "dodaj autobus\nMan\nX534\n59\n" +
                "dodaj autobus\nIsuzu\nA77\n42\n" +
                "dodaj autobus\nIcarbus\nB856\n47\n");

        String[] args= {"a","b"};
        Main.main(args);
        System.setIn(defaultSystemIn);
        TransportDAO dao = TransportDAO.getInstance();
        ArrayList<Bus> busses = dao.getBusses();
        assertAll(()->{
            assertEquals(3,busses.size());
            assertEquals("Isuzu",busses.get(1).getMaker());
            assertEquals(59,busses.get(0).getSeatNumber());
        });
    }

    @Test
    void ukloniAutobus() {
        InputStream defaultSystemIn = setupCustomInputStreamAndReturnStandardInputStream(
                "dodaj autobus\nMan\nX534\n59\n" +
                "dodaj autobus\nIsuzu\nA77\n42\n" +
                "dodaj autobus\nIcarbus\nB856\n47\n" +
                "ukloni autobus\n2\n");

        String[] args= {"a","b"};
        Main.main(args);
        revertToStandardInputStream(defaultSystemIn);
        TransportDAO dao = TransportDAO.getInstance();
        ArrayList<Bus> busses = dao.getBusses();
        assertAll(()->{
            assertEquals(2,busses.size());
            assertEquals("Icarbus",busses.get(1).getMaker());
            assertEquals(47,busses.get(1).getSeatNumber());
        });
    }

    private void revertToStandardInputStream(InputStream defaultSystemIn) {
        System.setIn(defaultSystemIn);
    }

    private InputStream setupCustomInputStreamAndReturnStandardInputStream(String s) {
        String driverInput = s;
        byte[] input = new byte[driverInput.length()];
        for (int i = 0; i < driverInput.length(); i++) {
            input[i] = (byte) driverInput.charAt(i);
        }

        ByteArrayInputStream inputHolder = new ByteArrayInputStream(input);
        InputStream defaultSystemIn = System.in;
        System.setIn(inputHolder);
        return defaultSystemIn;
    }
}
