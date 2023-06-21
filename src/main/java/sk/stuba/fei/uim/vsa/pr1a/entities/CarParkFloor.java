package sk.stuba.fei.uim.vsa.pr1a.entities;

import javax.persistence.*;
import java.util.List;

@Entity
@Table(name = "CAR_PARK_FLOOR",
                uniqueConstraints = {@UniqueConstraint(columnNames = {"CARPARK_ID", "FLOORIDENTIFIER"})}) //kombinacia je unique
public class CarParkFloor {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;

    @Column(name = "CARPARK_ID",nullable = false)
    private Long carParkId;

    @Column(nullable = false)
    private String floorIdentifier;

    @OneToMany
    private List<ParkingSpot> parkingSpots;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Long getCarParkId() {
        return carParkId;
    }

    public void setCarParkId(Long carParkId) {
        this.carParkId = carParkId;
    }

    public String getFloorIdentifier() {
        return floorIdentifier;
    }

    public void setFloorIdentifier(String floorIdentifier) {
        this.floorIdentifier = floorIdentifier;
    }

    @Override
    public String toString() {
        return "Name{" + "id=" + id + ", carParkId=" + carParkId + ", identifier=" + floorIdentifier +  '}' + "\n";
    }
}
