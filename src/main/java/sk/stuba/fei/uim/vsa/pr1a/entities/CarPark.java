package sk.stuba.fei.uim.vsa.pr1a.entities;

import javax.persistence.*;
import java.io.Serializable;
import java.util.List;

@Entity
@Table(name = "CAR_PARK")
public class CarPark {


    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;

    @Column(unique = true,nullable = false)
    private String name;
    @Column(unique = true,nullable = false)
    private String address;
    @Column(nullable = false)
    private Integer pricePerHour;

    @OneToMany(orphanRemoval = true, cascade = CascadeType.PERSIST) //(orphanRemoval = true)   //tu si skoncil
    @JoinColumn(name = "CARPARK_ID")         //prida sa do konkretnej tabulky Do game to co je v Liste
    private List<CarParkFloor> carParkFloors;     // 1:N developer moze mat viac hier

    @OneToMany            //(mappedBy = "carpark")
    private List<ParkingSpot> parkingSpots;


    public void setCarParkFloors(List<CarParkFloor> carParkFloors) {
        this.carParkFloors = carParkFloors;
    }

    public List<CarParkFloor> getCarParkFloors() {
        return carParkFloors;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public int getPricePerHour() {
        return pricePerHour;
    }

    public void setPricePerHour(int pricePerHour) {
        this.pricePerHour = pricePerHour;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    @Override
    public String toString() {
        return "Name{" + "id=" + id + ", name=" + name + ", address=" + address + ", pricePerHour=" + pricePerHour + '}'+ "\n";
    }
}
