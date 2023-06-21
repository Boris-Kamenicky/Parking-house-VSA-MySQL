package sk.stuba.fei.uim.vsa.pr1a.entities;

import javax.persistence.*;
import java.util.List;

@Entity
@Table(name = "CAR")
public class Car {


    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;

    //private Long userId;    //cudzi kluc
    @Column(nullable = false)
    private String brand;
    @Column(nullable = false)
    private String model;
    @Column(nullable = false)
    private String colour;
    @Column(unique = true,nullable = false)
    private String vehicleRegistrationPlate;

    @OneToOne(mappedBy = "car")
    @JoinColumn(unique = true)    //nic to nerobi
    private ParkingSpot parkingSpot;


    @ManyToOne
    //@JoinColumn(nullable = false)   lebo nejde vymazat
    private User user;

    public User getUser() {
        return user;
    }

    public void setUser(User user) {
        this.user = user;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getBrand() {
        return brand;
    }

    public void setBrand(String brand) {
        this.brand = brand;
    }

    public String getModel() {
        return model;
    }

    public void setModel(String model) {
        this.model = model;
    }

    public String getColour() {
        return colour;
    }

    public void setColour(String colour) {
        this.colour = colour;
    }

    public String getVehicleRegistrationPlate() {
        return vehicleRegistrationPlate;
    }

    public ParkingSpot getParkingSpot() {
        return parkingSpot;
    }

    public void setParkingSpot(ParkingSpot parkingSpot) {
        this.parkingSpot = parkingSpot;
    }

    public void setVehicleRegistrationPlate(String vehicleRegistrationPlate) {
        this.vehicleRegistrationPlate = vehicleRegistrationPlate;
    }

    @Override
    public String toString() {
        return "Name{" + "id=" + id + ", brand=" + brand + ", userIdr=" + user.getId() + ", colour="+  colour +", model="+  model + ", vehicleRegistrationPlate="+  vehicleRegistrationPlate +'}'+ "\n";
    }
}