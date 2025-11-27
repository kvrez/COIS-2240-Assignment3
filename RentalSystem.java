import java.util.List;
import java.time.LocalDate;
import java.util.ArrayList;
import java.io.FileWriter;
import java.io.IOException;
import java.io.BufferedReader;
import java.io.FileReader;
import java.io.File;

public class RentalSystem {
	
	private static RentalSystem instance = new RentalSystem();
	
    private List<Vehicle> vehicles = new ArrayList<>();
    private List<Customer> customers = new ArrayList<>();
    private RentalHistory rentalHistory = new RentalHistory();
    
    private RentalSystem() {
    	loadData();
    }

    public static RentalSystem getInstance() {
        return instance;
    }
    
    public void addVehicle(Vehicle vehicle) {
        vehicles.add(vehicle);
        saveVehicle(vehicle);
    }

    public void addCustomer(Customer customer) {
        customers.add(customer);
        saveCustomer(customer);
    }

    public void rentVehicle(Vehicle vehicle, Customer customer, LocalDate date, double amount) {
        if (vehicle.getStatus() == Vehicle.VehicleStatus.Available) {
            vehicle.setStatus(Vehicle.VehicleStatus.Rented);
            RentalRecord record = new RentalRecord(vehicle, customer, date, amount, "RENT");
            rentalHistory.addRecord(record);
            saveRecord(record);
            System.out.println("Vehicle rented to " + customer.getCustomerName());
        }
        else {
            System.out.println("Vehicle is not available for renting.");
        }
    }

    public void returnVehicle(Vehicle vehicle, Customer customer, LocalDate date, double extraFees) {
        if (vehicle.getStatus() == Vehicle.VehicleStatus.Rented) {
            vehicle.setStatus(Vehicle.VehicleStatus.Available);
            RentalRecord record = new RentalRecord(vehicle, customer, date, extraFees, "RETURN");
            rentalHistory.addRecord(record);
            saveRecord(record);
            System.out.println("Vehicle returned by " + customer.getCustomerName());
        }
        else {
            System.out.println("Vehicle is not rented.");
        }
    }   

    public void displayVehicles(Vehicle.VehicleStatus status) {
        // Display appropriate title based on status
        if (status == null) {
            System.out.println("\n=== All Vehicles ===");
        } else {
            System.out.println("\n=== " + status + " Vehicles ===");
        }
        
        // Header with proper column widths
        System.out.printf("|%-16s | %-12s | %-12s | %-12s | %-6s | %-18s |%n", 
            " Type", "Plate", "Make", "Model", "Year", "Status");
        System.out.println("|--------------------------------------------------------------------------------------------|");
    	  
        boolean found = false;
        for (Vehicle vehicle : vehicles) {
            if (status == null || vehicle.getStatus() == status) {
                found = true;
                String vehicleType;
                if (vehicle instanceof Car) {
                    vehicleType = "Car";
                } else if (vehicle instanceof Minibus) {
                    vehicleType = "Minibus";
                } else if (vehicle instanceof PickupTruck) {
                    vehicleType = "Pickup Truck";
                } else {
                    vehicleType = "Unknown";
                }
                System.out.printf("| %-15s | %-12s | %-12s | %-12s | %-6d | %-18s |%n", 
                    vehicleType, vehicle.getLicensePlate(), vehicle.getMake(), vehicle.getModel(), vehicle.getYear(), vehicle.getStatus().toString());
            }
        }
        if (!found) {
            if (status == null) {
                System.out.println("  No Vehicles found.");
            } else {
                System.out.println("  No vehicles with Status: " + status);
            }
        }
        System.out.println();
    }

    public void displayAllCustomers() {
        for (Customer c : customers) {
            System.out.println("  " + c.toString());
        }
    }
    
    public void displayRentalHistory() {
        if (rentalHistory.getRentalHistory().isEmpty()) {
            System.out.println("  No rental history found.");
        } else {
            // Header with proper column widths
            System.out.printf("|%-10s | %-12s | %-20s | %-12s | %-12s |%n", 
                " Type", "Plate", "Customer", "Date", "Amount");
            System.out.println("|-------------------------------------------------------------------------------|");
            
            for (RentalRecord record : rentalHistory.getRentalHistory()) {                
                System.out.printf("| %-9s | %-12s | %-20s | %-12s | $%-11.2f |%n", 
                    record.getRecordType(), 
                    record.getVehicle().getLicensePlate(),
                    record.getCustomer().getCustomerName(),
                    record.getRecordDate().toString(),
                    record.getTotalAmount()
                );
            }
            System.out.println();
        }
    }
    
    public Vehicle findVehicleByPlate(String plate) {
        for (Vehicle v : vehicles) {
            if (v.getLicensePlate().equalsIgnoreCase(plate)) {
                return v;
            }
        }
        return null;
    }
    
    public Customer findCustomerById(int id) {
        for (Customer c : customers)
            if (c.getCustomerId() == id)
                return c;
        return null;
    }
    
    private void saveVehicle(Vehicle vehicle) {
        try {
            FileWriter writer = new FileWriter("vehicles.txt", true);
            
            String vehicleType = "";
            String specificInfo = "";
            
            if (vehicle instanceof Car) {
                vehicleType = "Car";
                specificInfo = String.valueOf(((Car) vehicle).getNumSeats());
            } else if (vehicle instanceof Minibus) {
                vehicleType = "Minibus";
                specificInfo = String.valueOf(((Minibus) vehicle).isAccessible());
            } else if (vehicle instanceof PickupTruck) {
                vehicleType = "PickupTruck";
                PickupTruck truck = (PickupTruck) vehicle;
                specificInfo = truck.getCargoSize() + "," + truck.hasTrailer();
            }
            
            writer.write(vehicleType + "|" + vehicle.getLicensePlate() + "|" + 
                         vehicle.getMake() + "|" + vehicle.getModel() + "|" + 
                         vehicle.getYear() + "|" + vehicle.getStatus() + "|" + 
                         specificInfo + "\n");
            
            writer.close();
            
        } catch (IOException e) {
            System.out.println("Error saving vehicle: " + e.getMessage());
        }
    }
    
    private void saveCustomer(Customer customer) {
        try {
            FileWriter writer = new FileWriter("customers.txt", true);
            
            writer.write(customer.getCustomerId() + "|" + 
                         customer.getCustomerName() + "\n");
            
            writer.close();
            
        } catch (IOException e) {
            System.out.println("Error saving customer: " + e.getMessage());
        }
    }
    
    private void saveRecord(RentalRecord record) {
        try {
            FileWriter writer = new FileWriter("rental_records.txt", true);
            
            writer.write(record.getRecordType() + "|" + 
                         record.getVehicle().getLicensePlate() + "|" + 
                         record.getCustomer().getCustomerId() + "|" + 
                         record.getRecordDate() + "|" + 
                         record.getTotalAmount() + "\n");
            
            writer.close(); 
            
        } catch (IOException e) {
            System.out.println("Error saving rental record: " + e.getMessage());
        }
    }
    
    private void loadData() {
        // Load vehicles data
        File vehicleFile = new File("vehicles.txt");
        if (vehicleFile.exists()) {
            try {
                BufferedReader reader = new BufferedReader(new FileReader("vehicles.txt"));
                String line;
                
                while ((line = reader.readLine()) != null) {
                    String[] parts = line.split("\\|");
                    
                    String vehicleType = parts[0];
                    String plate = parts[1];
                    String make = parts[2];
                    String model = parts[3];
                    int year = Integer.parseInt(parts[4]);
                    String status = parts[5];
                    String specificInfo = parts[6];
                    
                    Vehicle vehicle = null;
                    
                    if (vehicleType.equals("Car")) {
                        int numSeats = Integer.parseInt(specificInfo);
                        vehicle = new Car(make, model, year, numSeats);
                    } else if (vehicleType.equals("Minibus")) {
                        boolean isAccessible = Boolean.parseBoolean(specificInfo);
                        vehicle = new Minibus(make, model, year, isAccessible);
                    } else if (vehicleType.equals("PickupTruck")) {
                        String[] truckInfo = specificInfo.split(",");
                        double cargoSize = Double.parseDouble(truckInfo[0]);
                        boolean hasTrailer = Boolean.parseBoolean(truckInfo[1]);
                        vehicle = new PickupTruck(make, model, year, cargoSize, hasTrailer);
                    }
                    
                    if (vehicle != null) {
                        vehicle.setLicensePlate(plate);
                        vehicle.setStatus(Vehicle.VehicleStatus.valueOf(status));
                        vehicles.add(vehicle);
                    }
                }
                
                reader.close();
                
            } catch (Exception e) {
                System.out.println("Error loading vehicles: " + e.getMessage());
            }
        }
        
        // Load customers data
        File customerFile = new File("customers.txt");
        if (customerFile.exists()) {
            try {
                BufferedReader reader = new BufferedReader(new FileReader("customers.txt"));
                String line;
                
                while ((line = reader.readLine()) != null) {
                    String[] parts = line.split("\\|");
                    
                    int customerId = Integer.parseInt(parts[0]);
                    String customerName = parts[1];
                    
                    Customer customer = new Customer(customerId, customerName);
                    customers.add(customer);
                }
                
                reader.close();
                
            } catch (Exception e) {
                System.out.println("Error loading customers: " + e.getMessage());
            }
        }
        
        // Load rental records data
        File recordFile = new File("rental_records.txt");
        if (recordFile.exists()) {
            try {
                BufferedReader reader = new BufferedReader(new FileReader("rental_records.txt"));
                String line;
                
                while ((line = reader.readLine()) != null) {
                    String[] parts = line.split("\\|");
                    
                    String recordType = parts[0];
                    String licensePlate = parts[1];
                    int customerId = Integer.parseInt(parts[2]);
                    LocalDate date = LocalDate.parse(parts[3]);
                    double amount = Double.parseDouble(parts[4]);
                    
                    Vehicle vehicle = findVehicleByPlate(licensePlate);
                    Customer customer = findCustomerById(customerId);
                    
                    if (vehicle != null && customer != null) {
                        RentalRecord record = new RentalRecord(vehicle, customer, date, amount, recordType);
                        rentalHistory.addRecord(record);
                    }
                }
                
                reader.close();
                
            } catch (Exception e) {
                System.out.println("Error loading rental records: " + e.getMessage());
            }
        }
    }
}