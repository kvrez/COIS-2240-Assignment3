public abstract class Vehicle {
    private String licensePlate;
    private String make;
    private String model;
    private int year;
    private VehicleStatus status;

    public enum VehicleStatus { Available, Held, Rented, UnderMaintenance, OutOfService }

    public Vehicle(String make, String model, int year) {
        this.make = capitalize(make);
        this.model = capitalize(model);
        this.year = year;
        this.status = VehicleStatus.Available;
        this.licensePlate = null;
    }

    public Vehicle() {
        this(null, null, 0);
    }
    
    private String capitalize(String input) {
        if (input == null || input.isEmpty()) {
            return null;
        }
        return input.substring(0, 1).toUpperCase() + input.substring(1).toLowerCase();
    }
    
    private boolean isValidPlate(String plate) {
        // Check if plate is null or empty
        if (plate == null || plate.isEmpty()) {
            return false;
        }
        
        // Ensures plate is 6 characters
        if (plate.length() != 6) {
            return false;
        }
        
        //  Ensures first 3 characters are letters
        if (Character.isLetter(plate.charAt(0)) == false) {
            return false;
        }
        if (Character.isLetter(plate.charAt(1)) == false) {
            return false;
        }
        if (Character.isLetter(plate.charAt(2)) == false) {
            return false;
        }
        
        // Ensures last 3 characters are digits
        if (Character.isDigit(plate.charAt(3)) == false) {
            return false;
        }
        if (Character.isDigit(plate.charAt(4)) == false) {
            return false;
        }
        if (Character.isDigit(plate.charAt(5)) == false) {
            return false;
        }
        
        return true;
    }

    public void setLicensePlate(String plate) {
        if (isValidPlate(plate) == false) {
            throw new IllegalArgumentException("Invalid license plate. Should be 3 letters and 3 numbers (Ex. ABC123).");
        }
        this.licensePlate = plate.toUpperCase();
    }

    public void setStatus(VehicleStatus status) {
    	this.status = status;
    }

    public String getLicensePlate() { return licensePlate; }

    public String getMake() { return make; }

    public String getModel() { return model;}

    public int getYear() { return year; }

    public VehicleStatus getStatus() { return status; }

    public String getInfo() {
        return "| " + licensePlate + " | " + make + " | " + model + " | " + year + " | " + status + " |";
    }

}
