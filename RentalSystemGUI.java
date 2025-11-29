import javafx.application.Application;
import javafx.geometry.Insets;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.layout.*;
import javafx.stage.Stage;

public class RentalSystemGUI extends Application {

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
        });

        Separator sep4 = new Separator();

        // Display
        Label displayTitle = new Label("Display");
        displayTitle.setStyle("-fx-font-weight: bold;");

        Button showAvailableBtn = new Button("Show Available Vehicles");
        showAvailableBtn.setOnAction(e -> {
        });

        Button showAllVehiclesBtn = new Button("Show All Vehicles");
        showAllVehiclesBtn.setOnAction(e -> {
        });

        Button showCustomersBtn = new Button("Show All Customers");
        showCustomersBtn.setOnAction(e -> {
        });

        Button showHistoryBtn = new Button("Show Rental History");
        showHistoryBtn.setOnAction(e -> {
        });

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
    }
}
