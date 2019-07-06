package ba.unsa.etf.rs.tutorijal9.tutorijal8;


import javafx.beans.property.SimpleIntegerProperty;
import javafx.beans.property.SimpleStringProperty;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.Node;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.stage.Stage;
import javafx.util.converter.NumberStringConverter;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;

public class Controller {

    public TableView driverTable;
    public TableView busTable;
    public TableColumn columnName;
    public TableColumn columnSurname;
    public TableColumn columnPersonalIDNumber;
    public TableColumn columnEmploymentDate;

    public TextField nameDriver;
    public TextField surnameDriver;
    public TextField JMBGDriver;
    public DatePicker BirthdayDateDriver;
    public DatePicker EmploymentDateDriver;
    //bus
    public TableColumn columnMaker;
    public TableColumn columnSeries;
    public TableColumn columnSeatNumber;

    public TextField MakerBus;
    public TextField SeriesBus;
    public TextField SeatNumberBus;

    public Button addbusButton;
    public Button deletebusButton;
    public Button exitbusButton;

    public Button adddriverButton;
    public Button deletedriverButton;
    public Button exitdriverButton;

    public Controller(TransportDAO t) {
        transportModel = t;
    }
    private TransportDAO transportModel;

    @FXML
    public void initialize() {
        columnName.setCellValueFactory(new PropertyValueFactory<Driver, String>("Name"));
        columnSurname.setCellValueFactory(new PropertyValueFactory<Driver, String>("Surname"));
        columnPersonalIDNumber.setCellValueFactory(new PropertyValueFactory<Driver, String>("Personal ID number"));
        columnEmploymentDate.setCellValueFactory(new PropertyValueFactory<Driver, LocalDate>("Employment date"));

        columnMaker.setCellValueFactory(new PropertyValueFactory<Bus, String>("Maker"));
        columnSeries.setCellValueFactory(new PropertyValueFactory<Bus, String>("Series"));
        columnSeatNumber.setCellValueFactory(new PropertyValueFactory<Bus, Integer>("Seat number"));
        setTextPropetryBind();
        driverTable.setItems(transportModel.getDRIVERlist());
        busTable.setItems(transportModel.getBUSlist());

        BirthdayDateDriver.getEditor().focusedProperty().addListener(new ChangeListener<Boolean>() {
            @Override
            public void changed(ObservableValue<? extends Boolean> observableValue, Boolean aBoolean, Boolean t1) {
                if(!t1) {
                    LocalDate temp = LocalDate.parse(BirthdayDateDriver.getEditor().getText(), DateTimeFormatter.ofPattern("M/d/yyyy"));
                    BirthdayDateDriver.setValue(temp);
                }
            }
        });

        EmploymentDateDriver.getEditor().focusedProperty().addListener(new ChangeListener<Boolean>() {
            @Override
            public void changed(ObservableValue<? extends Boolean> observableValue, Boolean aBoolean, Boolean t1) {
                if(!t1) {
                    LocalDate temp = LocalDate.parse(EmploymentDateDriver.getEditor().getText(), DateTimeFormatter.ofPattern("M/d/yyyy"));
                    EmploymentDateDriver.setValue(temp);
                }
            }
        });

        driverTable.getSelectionModel().selectedItemProperty().addListener(new ChangeListener<Driver>() {
            @Override
            public void changed(ObservableValue<? extends Driver> observableValue, Driver oldPerson, Driver newPerson) {
                if (oldPerson != null) {
                    setTextPropetryUnbind();
                }
                if (newPerson == null) {
                    nameDriver.setText("");
                    surnameDriver.setText("");
                    JMBGDriver.setText("");
                    BirthdayDateDriver.setValue(LocalDate.of(1900,1,1));
                    EmploymentDateDriver.setValue(LocalDate.of(1900,1,1));
                } else {
                    updateSelectedDriver();
                }
                driverTable.refresh();
            }
        });
        driverTable.requestFocus();
        driverTable.getSelectionModel().selectFirst();

        busTable.getSelectionModel().selectedItemProperty().addListener(new ChangeListener() {
            @Override
            public void changed(ObservableValue observableValue, Object oldBus, Object newBus) {
                if (oldBus != null) {
                    setTextPropetryUnbind();
                }
                if (newBus == null) {
                    MakerBus.setText("");
                    SeriesBus.setText("");
                    SeatNumberBus.setText("");
                } else {
                    updateSelectedBus();
                }
                busTable.refresh();
            }
        });
        busTable.requestFocus();
        busTable.getSelectionModel().selectFirst();
    }

    @FXML
    private void updateSelectedBus() {
        if(transportModel.getCurrentBus() == null) {
            System.out.println("NULL driver");
        }
        Bus b = (Bus) busTable.getSelectionModel().getSelectedItem();
        setTextPropetryUnbind();
        transportModel.setCurrentBus(b);
        busTable.setItems(transportModel.getBUSlist());
        busTable.refresh();
        setTextPropetryBind();
    }

    @FXML
    private void updateSelectedDriver() {
        if(transportModel.getCurrentDriver() == null) {
            System.out.println("NULL driver");
        }
        Driver d = (Driver) driverTable.getSelectionModel().getSelectedItem();
        setTextPropetryUnbind();
        transportModel.setCurrentDriver(d);
        setTextPropetryBind();
        driverTable.setItems(transportModel.getDRIVERlist());
        driverTable.refresh();
    }

    private void updateTableView() {
        int index = driverTable.getSelectionModel().getSelectedIndex();
        driverTable.getItems().clear();
        transportModel.ucitajVozace();
        driverTable.setItems(transportModel.getDRIVERlist());
        driverTable.requestFocus();
        driverTable.getSelectionModel().select(index);

        index = busTable.getSelectionModel().getSelectedIndex();
        busTable.getItems().clear();
        transportModel.ucitajBuseve();
        busTable.setItems(transportModel.getBUSlist());
        busTable.requestFocus();
        busTable.getSelectionModel().select(index);
    }

    @FXML
    private void addNewDriver(javafx.event.ActionEvent mouseEvent) {
        transportModel.addDriver(new Driver());
        updateTableView();
        driverTable.getSelectionModel().selectLast();
    }

    @FXML
    private void deleteDriver(ActionEvent mouseEvent) {
        setTextPropetryUnbind();
        int index = driverTable.getSelectionModel().getSelectedIndex();
        if ( index != -1) {
            transportModel.deleteCurrentDriver();
            updateTableView();
            if (index == driverTable.getItems().size()) {
                driverTable.getSelectionModel().selectLast();
            }
        }
    }

    @FXML
    private void deleteBus(ActionEvent actionEvent) {
        setTextPropetryUnbind();
        int index = busTable.getSelectionModel().getSelectedIndex();
        if ( index != -1) {
            transportModel.deleteBus(transportModel.getCurrentBus());
            updateTableView();
            if (index == busTable.getItems().size()) {
                busTable.getSelectionModel().selectLast();
            }
        }
    }

    @FXML
    private void addNewBus(ActionEvent actionEvent) {
        transportModel.addBus(new Bus());
        updateTableView();
        busTable.getSelectionModel().selectLast();
    }

    private void setTextPropetryBind() {
        nameDriver.textProperty().bindBidirectional(new SimpleStringProperty(transportModel.getCurrentDriver().getName()));
        surnameDriver.textProperty().bindBidirectional(new SimpleStringProperty(transportModel.getCurrentDriver().getSurname()));
        JMBGDriver.textProperty().bindBidirectional(new SimpleStringProperty(transportModel.getCurrentDriver().getJMBG()));
        BirthdayDateDriver.valueProperty().bindBidirectional(transportModel.getCurrentDriver().birthdayDateProperty());
        EmploymentDateDriver.valueProperty().bindBidirectional(transportModel.getCurrentDriver().employmentDateProperty());

        MakerBus.textProperty().bindBidirectional(new SimpleStringProperty(transportModel.getCurrentBus().getMaker()));
        SeriesBus.textProperty().bindBidirectional(new SimpleStringProperty(transportModel.getCurrentBus().getSeries()));
        SeatNumberBus.textProperty().bindBidirectional(new SimpleIntegerProperty(transportModel.getCurrentBus().getSeatNumber()), new NumberStringConverter());
    }

    private void setTextPropetryUnbind() {
        nameDriver.textProperty().unbindBidirectional(transportModel.getCurrentDriver().getName());
        surnameDriver.textProperty().unbindBidirectional(transportModel.getCurrentDriver().getSurname());
        JMBGDriver.textProperty().unbindBidirectional(transportModel.getCurrentDriver().getJMBG());
        BirthdayDateDriver.valueProperty().unbindBidirectional(transportModel.getCurrentDriver().birthdayDateProperty());
        EmploymentDateDriver.valueProperty().unbindBidirectional(transportModel.getCurrentDriver().employmentDateProperty());

        MakerBus.textProperty().unbindBidirectional(transportModel.getCurrentBus().getMaker());
        SeriesBus.textProperty().unbindBidirectional(transportModel.getCurrentBus().getSeries());
        SeatNumberBus.textProperty().unbindBidirectional(transportModel.getCurrentBus().getSeatNumber());
    }

    public void exitDriver(ActionEvent actionEvent) {
        Node n = (Node) actionEvent.getSource();
        Stage stage = (Stage) n.getScene().getWindow();
        stage.close();
    }

    public void exitBus(ActionEvent actionEvent) {
        Node n = (Node) actionEvent.getSource();
        Stage stage = (Stage) n.getScene().getWindow();
        stage.close();
    }
}