package ba.unsa.etf.rs.tutorijal9.tutorijal8;


import org.junit.jupiter.api.*;


import java.time.LocalDate;
import java.util.ArrayList;

import static org.junit.jupiter.api.Assertions.*;


@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
class PripremaTest {

    private TransportDAO dao;

    @BeforeAll
    static void vratiNaPocetak(){
        TransportDAO dao = TransportDAO.getInstance();
        dao.resetDatabase();
    }

    @BeforeEach
    void setUp() {
        dao = TransportDAO.getInstance();
    }

    @Test
    @Order(1)
    void daLiJeSingleton() {
        TransportDAO dao2 = TransportDAO.getInstance();
        assertTrue(dao==dao2);
    }

    @Test
    @Order(2)
    void dodavanjeVozaca() {
        dao.addDriver(new Driver("Test","Testović","1111111111111", LocalDate.now().minusYears(20),LocalDate.now()));
        dao.addDriver(new Driver("Priprema","Pripremović","2222222222222",LocalDate.now().minusYears(23),LocalDate.now().minusYears(1)));
        assertEquals(dao.getDrivers().size(),2);
    }

    @Test
    @Order(3)
    void dodavanjeAutobusa() {
        dao.addBus(new Bus("Man","Serija",52));
        dao.addBus(new Bus("Mercedes-Benz","Serija",49));
        dao.addBus(new Bus("Iveco","Serija",54));
        assertEquals(dao.getBusses().size(),3);
    }

    @Test
    @Order(4)
    void uklanjanjeAutobusa() {
        Bus bus = dao.getBusses().get(1);
        dao.deleteBus(bus);
        assertEquals(dao.getBusses().size(),2);
    }

    @Test
    @Order(5)
    void daLiSeKoristiBaza() {
        try {
            ArrayList<Driver> drivers = dao.getDrivers();
            drivers.remove(0);
            drivers.remove(0);
            dao.getDrivers().get(0);
        }catch (Exception e){
            dao.addDriver(new Driver("Test","Testović","1111111111111", LocalDate.now().minusYears(20),LocalDate.now()));
            dao.addDriver(new Driver("Priprema","Pripremović","2222222222222",LocalDate.now().minusYears(23),LocalDate.now().minusYears(1)));
            assertFalse(true);
        }

    }

    @Test
    @Order(6)
    void otpustanjeVozaca() {
        Driver driver = dao.getDrivers().get(0);
        dao.deleteDriver(driver);
        assertEquals(dao.getDrivers().size(),1);
    }

    @Test
    @Order(7)
    void dodavanjeIstogVozaca() {
        assertAll(
                ()->{
                    assertThrows(IllegalArgumentException.class,()->dao.addDriver(new Driver("Isti","Istić","2222222222222",LocalDate.now().minusYears(40),LocalDate.now().minusYears(3))));
                    try{
                        dao.addDriver(new Driver("Isti","Istić","2222222222222",LocalDate.now().minusYears(40),LocalDate.now().minusYears(3)));
                    }catch (IllegalArgumentException e){
                        assertEquals("Taj vozač već postoji!",e.getMessage());
                    }
                }
        );

    }
}