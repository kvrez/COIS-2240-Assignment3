import javafx.application.Application;
import javafx.geometry.Insets;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.layout.*;
import javafx.stage.Stage;
import java.time.LocalDate;
import java.util.List;

public class RentalSystemGUI extends Application {
   private RentalSystem rentalSystem;
   private TextArea outputArea;
   private ComboBox<String> rentVehicleBox;
   private ComboBox<String> rentCustomerBox;
   private ComboBox<String> returnVehicleBox;
   private ComboBox<String> returnCustomerBox;
   public static void main(String[] args) {
       launch(args);
   }
   @Override
   public void start(Stage primaryStage) {
       rentalSystem = RentalSystem.getInstance();
       VBox mainBox = new VBox(15);
       mainBox.setPadding(new Insets(20));
       // Add Vehicle
       Label vehicleTitle = new Label("Add Vehicle");
       vehicleTitle.setStyle("-fx-font-weight: bold;");
       TextField vType = new TextField();
       vType.setPromptText("Type: Car, Minibus, or PickupTruck (Select 1)");
       TextField vMake = new TextField();
       vMake.setPromptText("Make");
       TextField vModel = new TextField();
       vModel.setPromptText("Model");
       TextField vYear = new TextField();
       vYear.setPromptText("Year");
       TextField vPlate = new TextField();
       vPlate.setPromptText("License Plate (Ex. ABC123)");
       TextField vSpecific = new TextField();
       vSpecific.setPrefWidth(600);
       vSpecific.setPromptText("Car: # of seats | Minibus: (Accessible) true/false | PickupTruck: Cargo size, (Trailer) true/false");
       Button addVehicleBtn = new Button("Add Vehicle");
       addVehicleBtn.setOnAction(e -> {
           try {
               String type = vType.getText().trim();
               String make = vMake.getText().trim();
               String model = vModel.getText().trim();
               int year = Integer.parseInt(vYear.getText().trim());
               String plate = vPlate.getText().trim();
               String specific = vSpecific.getText().trim();
               Vehicle vehicle = null;
               if (type.equals("Car")) {
                   int seats = Integer.parseInt(specific);
                   vehicle = new Car(make, model, year, seats);
               } else if (type.equals("Minibus")) {
                   boolean accessible = Boolean.parseBoolean(specific);
                   vehicle = new Minibus(make, model, year, accessible);
               } else if (type.equals("PickupTruck")) {
                   String[] parts = specific.split(",");
                   double cargo = Double.parseDouble(parts[0]);
                   boolean trailer = Boolean.parseBoolean(parts[1]);
                   vehicle = new PickupTruck(make, model, year, cargo, trailer);
               } else {
                   outputArea.appendText("ERROR: Type must be Car, Minibus, or PickupTruck\n");
                   return;
               }
               vehicle.setLicensePlate(plate);
               rentalSystem.addVehicle(vehicle);
               outputArea.appendText("Vehicle added: " + plate + "\n");
               vType.clear(); vMake.clear(); vModel.clear(); vYear.clear(); vPlate.clear(); vSpecific.clear();
               refreshDropdowns();
           } catch (Exception ex) {
               outputArea.appendText("ERROR: " + ex.getMessage() + "\n");
           }
       });
       Separator sep1 = new Separator();
       // Add Customer
       Label custTitle = new Label("Add Customer");
       custTitle.setStyle("-fx-font-weight: bold;");
       TextField custId = new TextField();
       custId.setPromptText("Customer ID");
       TextField custName = new TextField();
       custName.setPromptText("Customer Name");
       Button addCustomerBtn = new Button("Add Customer");
       addCustomerBtn.setOnAction(e -> {
           try {
               int id = Integer.parseInt(custId.getText().trim());
               String name = custName.getText().trim();
               Customer customer = new Customer(id, name);
               rentalSystem.addCustomer(customer);
               outputArea.appendText("Customer added: " + name + "\n");
               custId.clear();
               custName.clear();
               refreshDropdowns();
           } catch (Exception ex) {
               outputArea.appendText("ERROR: " + ex.getMessage() + "\n");
           }
       });
       Separator sep2 = new Separator();
       // Rent Vehicle
       Label rentTitle = new Label("Rent Vehicle");
       rentTitle.setStyle("-fx-font-weight: bold;");
       rentVehicleBox = new ComboBox<>();
       rentVehicleBox.setPromptText("Select available vehicle");
       rentVehicleBox.setPrefWidth(300);
       rentCustomerBox = new ComboBox<>();
       rentCustomerBox.setPromptText("Select customer");
       rentCustomerBox.setPrefWidth(300);
       HBox rentSelectBox = new HBox(10,
               new VBox(new Label("Available Vehicle:"), rentVehicleBox),
               new VBox(new Label("Customer:"), rentCustomerBox));
       TextField rentAmount = new TextField();
       rentAmount.setPromptText("Rental Amount");
       Button rentBtn = new Button("Rent Vehicle");
       rentBtn.setOnAction(e -> {
           try {
               String vItem = rentVehicleBox.getValue();
               String cItem = rentCustomerBox.getValue();
               if (vItem == null || cItem == null) {
                   outputArea.appendText("ERROR: Please select a vehicle and a customer from the dropdowns.\n");
                   return;
               }
               double amount = Double.parseDouble(rentAmount.getText().trim());
               String plate = vItem.split(" ")[0];
               int customerId = Integer.parseInt(cItem.split(" ")[0]);
               Vehicle vehicle = rentalSystem.findVehicleByPlate(plate);
               Customer customer = rentalSystem.findCustomerById(customerId);
               if (vehicle == null) {
                   outputArea.appendText("ERROR: Vehicle not found\n");
                   return;
               }
               if (customer == null) {
                   outputArea.appendText("ERROR: Customer not found\n");
                   return;
               }
               if (rentalSystem.rentVehicle(vehicle, customer, LocalDate.now(), amount)) {
                   outputArea.appendText("SUCCESS: Vehicle rented - " + plate + "\n");
                   rentAmount.clear();
                   refreshDropdowns();
               } else {
                   outputArea.appendText("ERROR: Vehicle not available\n");
               }
           } catch (Exception ex) {
               outputArea.appendText("ERROR: " + ex.getMessage() + "\n");
           }
       });
       Separator sep3 = new Separator();
       // Return Vehicle
       Label returnTitle = new Label("Return Vehicle");
       returnTitle.setStyle("-fx-font-weight: bold;");
       returnVehicleBox = new ComboBox<>();
       returnVehicleBox.setPromptText("Select rented vehicle");
       returnVehicleBox.setPrefWidth(300);
       returnCustomerBox = new ComboBox<>();
       returnCustomerBox.setPromptText("Select customer");
       returnCustomerBox.setPrefWidth(300);
       HBox returnSelectBox = new HBox(10,
               new VBox(new Label("Rented Vehicle:"), returnVehicleBox),
               new VBox(new Label("Customer:"), returnCustomerBox));
       TextField returnFees = new TextField();
       returnFees.setPromptText("Extra Fees (Enter 0 if none)");
       returnFees.setText("0");
       Button returnBtn = new Button("Return Vehicle");
       returnBtn.setOnAction(e -> {
           try {
               String vItem = returnVehicleBox.getValue();
               String cItem = returnCustomerBox.getValue();
               if (vItem == null || cItem == null) {
                   outputArea.appendText("ERROR: Please select a vehicle and a customer from the dropdowns.\n");
                   return;
               }
               double fees = Double.parseDouble(returnFees.getText().trim());
               String plate = vItem.split(" ")[0];
               int customerId = Integer.parseInt(cItem.split(" ")[0]);
               Vehicle vehicle = rentalSystem.findVehicleByPlate(plate);
               Customer customer = rentalSystem.findCustomerById(customerId);
               if (vehicle == null) {
                   outputArea.appendText("ERROR: Vehicle not found\n");
                   return;
               }
               if (customer == null) {
                   outputArea.appendText("ERROR: Customer not found\n");
                   return;
               }
               if (rentalSystem.returnVehicle(vehicle, customer, LocalDate.now(), fees)) {
                   outputArea.appendText("SUCCESS: Vehicle returned - " + plate + "\n");
                   returnFees.setText("0");
                   refreshDropdowns();
               } else {
                   outputArea.appendText("ERROR: Vehicle not rented\n");
               }
           } catch (Exception ex) {
               outputArea.appendText("ERROR: " + ex.getMessage() + "\n");
           }
       });
       Separator sep4 = new Separator();
       // Display
       Label displayTitle = new Label("Display");
       displayTitle.setStyle("-fx-font-weight: bold;");
       Button showAvailableBtn = new Button("Show Available Vehicles");
       showAvailableBtn.setOnAction(e -> captureOutput(() -> rentalSystem.displayVehicles(Vehicle.VehicleStatus.Available)));
       Button showAllVehiclesBtn = new Button("Show All Vehicles");
       showAllVehiclesBtn.setOnAction(e -> captureOutput(() -> rentalSystem.displayVehicles(null)));
       Button showCustomersBtn = new Button("Show All Customers");
       showCustomersBtn.setOnAction(e -> captureOutput(() -> rentalSystem.displayAllCustomers()));
       Button showHistoryBtn = new Button("Show Rental History");
       showHistoryBtn.setOnAction(e -> captureOutput(() -> rentalSystem.displayRentalHistory()));
       outputArea = new TextArea();
       outputArea.setEditable(false);
       outputArea.setPrefHeight(400);
       outputArea.setPrefWidth(300);
       
       // Keep controls on the left side
       mainBox.getChildren().addAll(
           vehicleTitle, vType, vMake, vModel, vYear, vPlate, vSpecific, addVehicleBtn,
           sep1,
           custTitle, custId, custName, addCustomerBtn,
           sep2,
           rentTitle, rentSelectBox, new Label("Rental Amount:"), rentAmount, rentBtn,
           sep3,
           returnTitle, returnSelectBox, new Label("Extra Fees:"), returnFees, returnBtn,
           sep4,
           displayTitle, showAvailableBtn, showAllVehiclesBtn, showCustomersBtn, showHistoryBtn
       );
       
       // Keep output on the right side
       VBox rightBox = new VBox(5, new Label("Output:"), outputArea);
       rightBox.setPadding(new Insets(20, 20, 20, 0));
       ScrollPane scrollPane = new ScrollPane(mainBox);
       scrollPane.setFitToWidth(true);
       HBox root = new HBox(10, scrollPane, rightBox);
       Scene scene = new Scene(root, 1000, 700);
       primaryStage.setTitle("Vehicle Rental System");
       primaryStage.setScene(scene);
       primaryStage.show();
       refreshDropdowns();
   }
   private void refreshDropdowns() {
       if (rentVehicleBox == null) return;
       rentVehicleBox.getItems().clear();
       returnVehicleBox.getItems().clear();
       rentCustomerBox.getItems().clear();
       returnCustomerBox.getItems().clear();
       List<Vehicle> allVehicles = rentalSystem.getAllVehicles();
       for (Vehicle v : allVehicles) {
           String displayText = v.getLicensePlate() + " " + v.getMake() + " " + v.getModel();
           if (v.getStatus() == Vehicle.VehicleStatus.Available) {
               rentVehicleBox.getItems().add(displayText);
           } else if (v.getStatus() == Vehicle.VehicleStatus.Rented) {
               returnVehicleBox.getItems().add(displayText);
           }
       }
       List<Customer> allCustomers = rentalSystem.getAllCustomers();
       for (Customer c : allCustomers) {
           String displayText = c.getCustomerId() + " " + c.getCustomerName();
           rentCustomerBox.getItems().add(displayText);
           returnCustomerBox.getItems().add(displayText);
       }
   }
   private void captureOutput(Runnable action) {
       java.io.ByteArrayOutputStream baos = new java.io.ByteArrayOutputStream();
       java.io.PrintStream ps = new java.io.PrintStream(baos);
       java.io.PrintStream old = System.out;
       System.setOut(ps);
       action.run();
       System.out.flush();
       System.setOut(old);
       outputArea.setText(baos.toString());
   }
}
