package sk.stuba.fei.uim.vsa.pr1a.entities;

import javax.persistence.*;

@Entity
@Table(name = "PARKING_SPOT",
        uniqueConstraints = {@UniqueConstraint(columnNames = {"CARPARK_ID", "SPOTIDENTIFIER"})})

public class ParkingSpot {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;

    @Column(nullable = false)
    private String floorIdentifier;

    @Column(nullable = false)
    private String spotIdentifier;    // musi byt unikatny v ramci parkovacieho domu

    @JoinColumn(name = "CARPARKFLOOR_ID")
    @ManyToOne
    private CarParkFloor carparkfloor;

    @JoinColumn(name = "CARPARK_ID")
    @ManyToOne
    private CarPark carpark;

    @OneToOne
    @JoinColumn(unique = true)   //toto ti osetri ze auto neni na 2 spotoch na raz
    private Car car;

    public Car getCar() {
        return car;
    }

    public void setCar(Car car) {
        this.car = car;
    }

    public String getSpotIdentifier() {
        return spotIdentifier;
    }

    public void setSpotIdentifier(String spotIdentifier) {
        this.spotIdentifier = spotIdentifier;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public CarPark getCarpark() {
        return carpark;
    }

    public void setCarpark(CarPark carpark) {
        this.carpark = carpark;
    }

    public CarParkFloor getCarparkfloor() {
        return carparkfloor;
    }

    public void setCarparkfloor(CarParkFloor carparkfloor) {
        this.carparkfloor = carparkfloor;
    }

    public String getFloorIdentifier() {
        return floorIdentifier;
    }

    public void setFloorIdentifier(String floorIdentifier) {
        this.floorIdentifier = floorIdentifier;
    }

    @Override
    public String toString() {
        return "Name{" + "id=" + id + ", floorIdentifier=" +  floorIdentifier + ", spotIdentifier="+  spotIdentifier + ", carParkId="+  carpark.getId() + ", carParkFloorId="+  carparkfloor.getId() +'}' + "\n";
    }
}

