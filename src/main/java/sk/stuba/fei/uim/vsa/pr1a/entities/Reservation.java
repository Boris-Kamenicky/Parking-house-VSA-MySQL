package sk.stuba.fei.uim.vsa.pr1a.entities;

import javax.persistence.*;
import java.util.Date;

@Entity
@Table(name = "RESERVATION")
public class Reservation {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;


    @OneToOne    //nullable ale este uvidis
    private Car car;

    @OneToOne   //nullable
    private ParkingSpot parkingSpot;

    @OneToOne
    private DiscountCoupon discountCoupon;

    @Column(nullable = false)
    private Date startDate;

    private Date finishDate;
    private Double finalPrice;

    public DiscountCoupon getDiscountCoupon() {
        return discountCoupon;
    }

    public void setDiscountCoupon(DiscountCoupon discountCoupon) {
        this.discountCoupon = discountCoupon;
    }

    public Car getCar() {
        return car;
    }

    public void setCar(Car car) {
        this.car = car;
    }

    public ParkingSpot getParkingSpot() {
        return parkingSpot;
    }

    public void setParkingSpot(ParkingSpot parkingSpot) {
        this.parkingSpot = parkingSpot;
    }

    public Date getStartDate() {
        return startDate;
    }

    public void setStartDate(Date startDate) {
        this.startDate = startDate;
    }

    public Date getFinishDate() {
        return finishDate;
    }

    public void setFinishDate(Date finishDate) {
        this.finishDate = finishDate;
    }

    public Double getFinalPrice() {
        return finalPrice;
    }

    public void setFinalPrice(Double finalPrice) {
        this.finalPrice = finalPrice;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    @Override
    public String toString() {
        return "Name{" + "id=" + id + ", car=" + getCar().getId() + ", parkingSpotId=" + getParkingSpot().getId() + ", finalPrice=" + finalPrice +  ", startDate=" + startDate +  ", finishDate=" + finishDate + ", discountCoupon=" + discountCoupon +  '}' + "\n";
    }
}
