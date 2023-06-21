package sk.stuba.fei.uim.vsa.pr1a.entities;

import javax.persistence.*;

@Entity
@Table(name = "DISCOUNT_COUPON")
public class DiscountCoupon {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;

    @Column(nullable = false)
    private String name;
    @Column(nullable = false)
    private Integer discount;
    private Boolean validity;    //mozno aby vypis bol krajsi kukni cviko

    @ManyToOne
    private User user;

    public Boolean getValidity() {
        return validity;
    }

    public void setValidity(Boolean validity) {
        this.validity = validity;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Integer getDiscount() {
        return discount;
    }

    public void setDiscount(Integer discount) {
        this.discount = discount;
    }

    public User getUser() {
        return user;
    }

    public void setUser(User user) {
        this.user = user;
    }

    @Override
    public String toString() {
        return "Name{" + "id=" + id + ", name=" +  name + ", discount="+  discount +  ", validity="+  validity +  ", userId="+  user.getId() + '}' + "\n";
    }
}
