package ba.unsa.etf.rs.tutorijal9.tutorijal8;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.Node;
import javafx.scene.control.DatePicker;
import javafx.scene.control.TextField;
import javafx.stage.Stage;

public class DriverController {
    public TextField fldIme;
    public TextField fldPrezime;
    public TextField fldJMBG;
    public DatePicker pikerBirth;
    public DatePicker pikerWork;
    private Driver driver;

    public DriverController(Driver driver) {
        this.driver = driver;
    }

    @FXML
    public void initialize() {
        if (driver != null) {
            fldIme.setText(driver.getIme());
            fldPrezime.setText(driver.getPrezime());

        }
    }

    public void akcijaCancel(ActionEvent actionEvent) {
        driver = null;
        Node n = (Node) actionEvent.getSource();
        Stage stage = (Stage) n.getScene().getWindow();
        stage.close();
    }

    public void akcijaOk(ActionEvent actionEvent) {
        if (driver == null) driver = new Driver();
        driver.setIme(fldIme.getText());
        driver.setPrezime(fldPrezime.getText());
        driver.setJMB(fldJMBG.getText());
        driver.setBirthDate(pikerBirth.getValue());
        driver.setWorkDate(pikerWork.getValue());
        Stage stage = (Stage) fldIme.getScene().getWindow();
        stage.close();
    }




    public Driver getDriver() {
        return driver;
    }
}