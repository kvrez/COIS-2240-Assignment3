import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;
import java.time.LocalDate;
import java.lang.reflect.Constructor;
import java.lang.reflect.Modifier;

public class VehicleRentalTest {
    
    @Test
    public void testLicensePlate() {
        // Test valid plate AAA100
        Vehicle car1 = new Car("Toyota", "Camry", 2020, 5);
        car1.setLicensePlate("AAA100");
        assertTrue(car1.getLicensePlate().equals("AAA100"));

        // Test valid plate ABC567
        Vehicle car2 = new Car("Honda", "Civic", 2021, 5);
        car2.setLicensePlate("ABC567");
        assertTrue(car2.getLicensePlate().equals("ABC567"));

        // Test valid plate ZZZ999
        Vehicle car3 = new Car("Ford", "Focus", 2019, 5);
        car3.setLicensePlate("ZZZ999");
        assertTrue(car3.getLicensePlate().equals("ZZZ999"));
        
        // Test invalid plate empty string
        Vehicle car4 = new Car("Mazda", "3", 2022, 5);
        assertThrows(IllegalArgumentException.class, () -> {
            car4.setLicensePlate("");
        });
        assertFalse(car4.getLicensePlate() != null);

        // Test invalid plate null
        Vehicle car5 = new Car("Nissan", "Altima", 2020, 5);
        assertThrows(IllegalArgumentException.class, () -> {
            car5.setLicensePlate(null);
        });
        assertFalse(car5.getLicensePlate() != null);
        
        // Test invalid plate AAA1000
        Vehicle car6 = new Car("Chevy", "Malibu", 2021, 5);
        assertThrows(IllegalArgumentException.class, () -> {
            car6.setLicensePlate("AAA1000");
        });
        assertFalse(car6.getLicensePlate() != null);

        // Test invalid plate ZZZ99
        Vehicle car7 = new Car("Tesla", "Model 3", 2023, 5);
        assertThrows(IllegalArgumentException.class, () -> {
            car7.setLicensePlate("ZZZ99");
        });
        assertFalse(car7.getLicensePlate() != null);
    }
    
    @Test
    public void testRentAndReturnVehicle() {
        // Create a vehicle and customer
        Vehicle car = new Car("Toyota", "Camry", 2020, 5);
        car.setLicensePlate("GKD532");
        Customer customer = new Customer(003, "User");

        // Vehicle should start as available
        assertEquals(Vehicle.VehicleStatus.Available, car.getStatus());

        // Get RentalSystem
        RentalSystem rentalSystem = RentalSystem.getInstance();
        rentalSystem.addVehicle(car);
        rentalSystem.addCustomer(customer);

        // Rent the vehicle
        boolean rentSuccess = rentalSystem.rentVehicle(car, customer, LocalDate.now(), 0.0);
        assertTrue(rentSuccess);
        assertEquals(Vehicle.VehicleStatus.Rented, car.getStatus());

        // Renting again should fail
        boolean rentFailure = rentalSystem.rentVehicle(car, customer, LocalDate.now(), 0.0);
        assertFalse(rentFailure);

        // Return the vehicle
        boolean returnSuccess = rentalSystem.returnVehicle(car, customer, LocalDate.now(), 0.0);
        assertTrue(returnSuccess);
        assertEquals(Vehicle.VehicleStatus.Available, car.getStatus());

        // Returning again should fail
        boolean returnFailure = rentalSystem.returnVehicle(car, customer, LocalDate.now(), 0.0);
        assertFalse(returnFailure);
    }

    @Test
    public void testSingletonRentalSystem() throws Exception {
        // Tests if constructor enforces singleton by testing if its private
        Constructor<RentalSystem> constructor = RentalSystem.class.getDeclaredConstructor();
        int modifiers = constructor.getModifiers();
        assertEquals(Modifier.PRIVATE, modifiers & Modifier.PRIVATE);
        
        // Tests if getInstance() returns a valid instance
        RentalSystem instance = RentalSystem.getInstance();
        assertNotNull(instance);
    }
}