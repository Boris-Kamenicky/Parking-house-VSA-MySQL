package sk.stuba.fei.uim.vsa.pr1a;

import sk.stuba.fei.uim.vsa.pr1a.entities.*;

import javax.persistence.*;
import java.util.*;

public class CarParkService extends AbstractCarParkService {
    @Override
    public Object createCarPark(String name, String address, Integer pricePerHour) {
        CarPark carpark = new CarPark();
        carpark.setName(name);
        carpark.setAddress(address);
        carpark.setPricePerHour(pricePerHour);
        persist(carpark);
        return carpark;
    }

    @Override
    public Object getCarPark(Long carParkId) {
        EntityManager em = emf.createEntityManager();
        CarPark p = em.find(CarPark.class, carParkId);
        em.close();
        return p;
    }

    @Override
    public Object getCarPark(String carParkName) {
        EntityManager em = emf.createEntityManager();
        Query parkingQuery = em.createNativeQuery("SELECT * from CAR_PARK where NAME = '" + carParkName + "'", CarPark.class);
        Optional<CarPark> park = parkingQuery.getResultStream().findFirst();
        CarPark topark = park.get();
        em.close();
        return topark;
    }

    @Override
    public List<Object> getCarParks() {
        EntityManager em = emf.createEntityManager();
        Query q = em.createNativeQuery("select * from CAR_PARK", CarPark.class);
        List<CarPark> carparks = q.getResultList();
        em.close();
        return Collections.singletonList(carparks);
    }

    @Override
    public Object updateCarPark(Object carPark){
        EntityManager em = emf.createEntityManager();
        CarPark newCp = (CarPark) carPark;
        CarPark k = em.find(CarPark.class, newCp.getId());
        em.getTransaction().begin();
        k.setPricePerHour(newCp.getPricePerHour());
        k.setName(newCp.getName());
        k.setAddress(newCp.getAddress());
        em.getTransaction().commit();
        return k;
    }

    @Override
    public Object deleteCarPark(Long carParkId) {    //dorobic
        EntityManager em = emf.createEntityManager();
        Query q = em.createNativeQuery ("SELECT * from CAR_PARK_FLOOR where CARPARK_ID = '" + carParkId +  "'" , CarParkFloor.class);
        List <CarParkFloor> floors =  q.getResultList();
        for (CarParkFloor f : floors){
            deleteCarParkFloor(carParkId,f.getFloorIdentifier());
        }
        CarPark p = em.getReference(CarPark.class, carParkId);
        em.getTransaction().begin();
        em.remove(p);
        em.getTransaction().commit();
        em.close();
        return p;
    }

    @Override
    public Object createCarParkFloor(Long carParkId, String floorIdentifier) {
        CarParkFloor p = new CarParkFloor();
        p.setCarParkId(carParkId);
        p.setFloorIdentifier(floorIdentifier);
        persist(p);
        return p;
    }

    @Override
    public Object getCarParkFloor(Long carParkId, String floorIdentifier) {
        EntityManager em = emf.createEntityManager();
        Query q = em.createNativeQuery ("SELECT * from CAR_PARK_FLOOR where FLOORIDENTIFIER = '" + floorIdentifier + "' AND CARPARK_ID = '" + carParkId + "'" , CarParkFloor.class);
        CarParkFloor floor = (CarParkFloor) q.getResultStream().findFirst().orElse(null);
        if (floor == null) {
            return null;
        }
        em.close();
        return floor;
    }

    @Override
    public Object getCarParkFloor(Long carParkFloorId) {
        EntityManager em = emf.createEntityManager();
        CarParkFloor p = em.find(CarParkFloor.class, carParkFloorId);
        em.close();
        return p;
    }

    @Override
    public List<Object> getCarParkFloors(Long carParkId) {
        EntityManager em = emf.createEntityManager();
        Query q = em.createNativeQuery ("SELECT * from CAR_PARK_FLOOR where CARPARK_ID = '" + carParkId +  "'" , CarParkFloor.class);
        List <CarParkFloor> floors =  q.getResultList();
        em.close();
        return Collections.singletonList(floors);
    }

    @Override
    public Object updateCarParkFloor(Object carParkFloor) {
        EntityManager em = emf.createEntityManager();
        CarParkFloor newCp = (CarParkFloor) carParkFloor;
        CarParkFloor k = em.find(CarParkFloor.class, newCp.getId());
        em.getTransaction().begin();
        k.setCarParkId(newCp.getCarParkId());
        k.setFloorIdentifier(newCp.getFloorIdentifier());
        em.getTransaction().commit();
        return k;
    }

    @Override
    public Object deleteCarParkFloor(Long carParkId, String floorIdentifier) {
        EntityManager em = emf.createEntityManager();

        CarParkFloor floor = (CarParkFloor) getCarParkFloor(carParkId, floorIdentifier);
        Query q = em.createNativeQuery ("SELECT * from PARKING_SPOT where FLOORIDENTIFIER = '" + floorIdentifier +  "' AND CARPARK_ID =  '" + carParkId +  "'", ParkingSpot.class);
        List <ParkingSpot> spots =  q.getResultList();
        for (ParkingSpot s : spots){
            deleteParkingSpot(s.getId());
        }
        em.getTransaction().begin();
        CarParkFloor stock = em.merge(floor);
        em.remove(stock);
        em.getTransaction().commit();
        em.close();
        return floor;
    }

    @Override
    public Object deleteCarParkFloor(Long carParkFloorId) {
        return null;
    }

    @Override
    public Object createParkingSpot(Long carParkId, String floorIdentifier, String spotIdentifier) {
        if (getCarParkFloor(carParkId,floorIdentifier)==null || getCarPark(carParkId)==null){
            return null;
        }
        ParkingSpot spot = new ParkingSpot();
        spot.setCarpark((CarPark) getCarPark(carParkId));
        spot.setSpotIdentifier(spotIdentifier);
        spot.setFloorIdentifier((floorIdentifier));
        spot.setCarparkfloor((CarParkFloor) getCarParkFloor(carParkId,floorIdentifier));
        spot.setCar((Car)getCar(20L));//tu si
        persist(spot);
        return spot;
    }

    @Override
    public Object getParkingSpot(Long parkingSpotId) {
        EntityManager em = emf.createEntityManager();
        ParkingSpot p = em.find(ParkingSpot.class, parkingSpotId);
        em.close();
        return p;
    }

    @Override
    public List<Object> getParkingSpots(Long carParkId, String floorIdentifier) {
        EntityManager em = emf.createEntityManager();

        Query q = em.createNativeQuery ("SELECT * from PARKING_SPOT where FLOORIDENTIFIER = '" + floorIdentifier +  "' AND CARPARK_ID =  '" + carParkId +  "'", ParkingSpot.class);
        List <ParkingSpot> spots =  q.getResultList();

        em.close();
        return Collections.singletonList(spots);
    }

    @Override
    public Map<String, List<Object>> getParkingSpots(Long carParkId) {
        EntityManager em = emf.createEntityManager();
        Map<String, List<Object>> mapka = new HashMap<String, List<Object>>();

        Query q = em.createNativeQuery ("SELECT * from CAR_PARK_FLOOR where CARPARK_ID = '" + carParkId +  "'" , CarParkFloor.class);
        List <CarParkFloor> floors =  q.getResultList();

        for (CarParkFloor floor :  floors) {
            mapka.put(floor.getFloorIdentifier(),getParkingSpots(carParkId,floor.getFloorIdentifier()));
        }

        em.close();
        return mapka;
    }

    @Override
    public Map<String, List<Object>> getAvailableParkingSpots(String carParkName) {
        EntityManager em = emf.createEntityManager();
        Map<String, List<Object>> mapka = new HashMap<String, List<Object>>();
        CarPark carpark = (CarPark) getCarPark(carParkName);
        Query q = em.createNativeQuery ("SELECT * from CAR_PARK_FLOOR where CARPARK_ID = '" + carpark.getId() +  "'" , CarParkFloor.class);
        List <CarParkFloor> floors =  q.getResultList();
        for (CarParkFloor floor :  floors) {
            mapka.put(floor.getFloorIdentifier(),getParkingSpotsFree(carpark.getId(),floor.getFloorIdentifier())); //tu si
        }
        em.close();
        return mapka;
    }

    @Override
    public Map<String, List<Object>> getOccupiedParkingSpots(String carParkName) {
        EntityManager em = emf.createEntityManager();
        Map<String, List<Object>> mapka = new HashMap<String, List<Object>>();
        CarPark carpark = (CarPark) getCarPark(carParkName);
        Query q = em.createNativeQuery ("SELECT * from CAR_PARK_FLOOR where CARPARK_ID = '" + carpark.getId() +  "'" , CarParkFloor.class);
        List <CarParkFloor> floors =  q.getResultList();
        for (CarParkFloor floor :  floors) {
            mapka.put(floor.getFloorIdentifier(),getParkingSpotsOccupied(carpark.getId(),floor.getFloorIdentifier())); //tu si
        }
        em.close();
        return mapka;
    }

    @Override
    public Object updateParkingSpot(Object parkingSpot) {
        EntityManager em = emf.createEntityManager();
        ParkingSpot newCp = (ParkingSpot) parkingSpot;
        ParkingSpot k = em.find(ParkingSpot.class, newCp.getId());

        em.getTransaction().begin();
        k.setSpotIdentifier(newCp.getSpotIdentifier());
        k.setFloorIdentifier(newCp.getFloorIdentifier());
        k.setCarpark(newCp.getCarpark());
        k.setCarparkfloor(newCp.getCarparkfloor());
        em.getTransaction().commit();
        return k;

    }

    @Override
    public Object deleteParkingSpot(Long parkingSpotId) {
        EntityManager em = emf.createEntityManager();
        ParkingSpot spot = em.find(ParkingSpot.class,parkingSpotId);

        em.getTransaction().begin();
        Query q = em.createNativeQuery ("SELECT * from RESERVATION where PARKINGSPOT_ID = '" + parkingSpotId +  "'", Reservation.class);
        List <Reservation> spots =  q.getResultList();
        for (Reservation s : spots){
            s.setParkingSpot(null);
            endReservation(s.getId());
        }

        ParkingSpot stock = em.merge(spot);
        em.remove(stock);
        em.getTransaction().commit();

        em.close();
        return spot;
    }

    @Override
    public Object createCar(Long userId, String brand, String model, String colour, String vehicleRegistrationPlate) {
        Car car = new Car();
        car.setUser((User) getUser(userId));
        car.setBrand(brand);
        car.setModel(model);
        car.setColour(colour);
        car.setVehicleRegistrationPlate(vehicleRegistrationPlate);
        persist(car);
        return car;
    }

    @Override
    public Object getCar(Long carId) {
        EntityManager em = emf.createEntityManager();
        Car p = em.find(Car.class, carId);
        em.close();
        return p;
    }

    @Override
    public Object getCar(String vehicleRegistrationPlate) {
        EntityManager em = emf.createEntityManager();
        Query carQuery = em.createNativeQuery("SELECT * from CAR where VEHICLEREGISTRATIONPLATE = '" + vehicleRegistrationPlate + "'", Car.class);
        Optional<Car> car = carQuery.getResultStream().findFirst();
        Car toCar = car.get();
        em.close();
        return toCar;
    }

    @Override
    public List<Object> getCars(Long userId) {
        EntityManager em = emf.createEntityManager();

        Query q = em.createNativeQuery ("SELECT * from CAR where USER_ID = '" + userId +  "'" , Car.class);
        List <Car> cars =  q.getResultList();

        em.close();
        return Collections.singletonList(cars);
    }

    @Override
    public Object updateCar(Object car) {
        EntityManager em = emf.createEntityManager();
        Car newCp = (Car) car;
        Car k = em.find(Car.class, newCp.getId());

        em.getTransaction().begin();
        k.setColour(newCp.getColour());
        k.setModel(newCp.getModel());
        k.setBrand(newCp.getBrand());
        k.setVehicleRegistrationPlate(k.getVehicleRegistrationPlate());
        k.setUser(newCp.getUser());
        em.getTransaction().commit();
        return k;
    }

    @Override
    public Object deleteCar(Long carId) {
        EntityManager em = emf.createEntityManager();
        Car car = em.find(Car.class,carId);

        em.getTransaction().begin();
        Query q = em.createNativeQuery ("SELECT * from RESERVATION where CAR_ID = '" + carId +  "'", Reservation.class);
        List <Reservation> reservations =  q.getResultList();
        for (Reservation s : reservations){
            s.setCar(null);
            endReservation(s.getId());
        }
        Query q2 = em.createNativeQuery ("SELECT * from PARKING_SPOT where CAR_ID = '" + carId +  "'", ParkingSpot.class);
        List <ParkingSpot> spots =  q2.getResultList();
        for (ParkingSpot s : spots){
            s.setCar(null);
        }
        Car stock = em.merge(car);
        em.remove(stock);
        em.getTransaction().commit();

        em.close();
        return car;
    }

    @Override
    public Object createUser(String firstname, String lastname, String email) {
        User user = new User();
        user.setEmail(email);
        user.setFirstname(firstname);
        user.setLastname(lastname);
        persist(user);
        return user;
    }

    @Override
    public Object getUser(Long userId) {
        EntityManager em = emf.createEntityManager();
        User p = em.find(User.class, userId);
        em.close();
        return p;
    }

    @Override
    public Object getUser(String email) {
        EntityManager em = emf.createEntityManager();
        Query userQuery = em.createNativeQuery("SELECT * from USER where EMAIL = '" + email + "'", User.class);
        Optional<User> park = userQuery.getResultStream().findFirst();
        User toUser = park.get();
        em.close();
        return toUser;
    }

    @Override
    public List<Object> getUsers() {
        EntityManager em = emf.createEntityManager();
        Query q = em.createNativeQuery("select * from USER", User.class);
        List<User> users = q.getResultList();
        em.close();
        return Collections.singletonList(users);
    }

    @Override
    public Object updateUser(Object user) {
        EntityManager em = emf.createEntityManager();
        User newCp = (User) user;
        User k = em.find(User.class, newCp.getId());

        em.getTransaction().begin();
        k.setFirstname(newCp.getFirstname());
        k.setLastname(newCp.getLastname());
        k.setEmail(newCp.getEmail());
        em.getTransaction().commit();
        return k;
    }

    @Override
    public Object deleteUser(Long userId) {
        EntityManager em = emf.createEntityManager();
        User user = (User) getUser(userId);
        Query q = em.createNativeQuery ("SELECT * from CAR where USER_ID = '" + userId +  "'", Car.class);
        List <Car> cars =  q.getResultList();
        for (Car s : cars){
            deleteCar(s.getId());
        }
        em.getTransaction().begin();
        Query q2 = em.createNativeQuery ("SELECT * from DISCOUNT_COUPON where USER_ID = '" + userId +  "'", DiscountCoupon.class);
        List <DiscountCoupon> coupons =  q2.getResultList();
        for (DiscountCoupon s : coupons){
            s.setUser(null);
        }
        User stock = em.merge(user);
        em.remove(stock);
        em.getTransaction().commit();
        em.close();
        return user;
    }

    @Override
    public Object createReservation(Long parkingSpotId, Long cardId) {
        EntityManager em = emf.createEntityManager();
        Query q = em.createNativeQuery ("SELECT * from RESERVATION where CAR_ID = '" + cardId +  "' AND FINISHDATE IS NULL ", Reservation.class);
        List <Reservation> reserves =  q.getResultList();
        if (reserves.size() > 0) {
            System.out.println("obsadene");
            return null;
        }
        Query q2 = em.createNativeQuery ("SELECT * from RESERVATION where PARKINGSPOT_ID = '" + parkingSpotId +  "' AND FINISHDATE IS NULL ", Reservation.class);
        List <Reservation> reserves2 =  q2.getResultList();
        if (reserves2.size() > 0) {
            System.out.println("obsadene");
            return null;
        }
        Reservation reservation = new Reservation();
        reservation.setCar((Car)getCar(cardId));
        reservation.setParkingSpot((ParkingSpot) getParkingSpot(parkingSpotId));
        reservation.setStartDate(new Date());
        em.getTransaction().begin();
        ParkingSpot spot = em.find(ParkingSpot.class, parkingSpotId);
        spot.setCar((Car) getCar(cardId));
        em.getTransaction().commit();
        em.close();
        persist(reservation);
        return reservation;
    }

    @Override
    public Object endReservation(Long reservationId) {
        EntityManager em = emf.createEntityManager();
        em.getTransaction().begin();
        Reservation reservation = em.find(Reservation.class, reservationId);
        if(reservation.getFinishDate() != null){
            System.out.println("Tato rezervacka uz skoncila");
            return null;
        }
        reservation.setFinishDate(new Date());
        reservation.setFinalPrice(countPrice(reservation));
        ParkingSpot spot = em.find(ParkingSpot.class, reservation.getParkingSpot().getId());
        spot.setCar(null);
        em.getTransaction().commit();
        em.close();
        return reservation;
    }

    @Override
    public List<Object> getReservations(Long parkingSpotId, Date date) {
        EntityManager em = emf.createEntityManager();
        ParkingSpot spot = (ParkingSpot) getParkingSpot(parkingSpotId);

        Query q = em.createNativeQuery ("SELECT * from RESERVATION where PARKINGSPOT_ID = '" + parkingSpotId + "'", Reservation.class);
        List <Reservation> spots =  q.getResultList();

        List<Reservation> list = new ArrayList<>();

        for (Reservation s : spots){
            if(s.getStartDate().getDay() == date.getDay()){
                list.add(s);
            }
        }
        em.close();
        return Collections.singletonList(list);
    }

    @Override
    public List<Object> getMyReservations(Long userId) {
        EntityManager em = emf.createEntityManager();
        Query q = em.createNativeQuery("select * from RESERVATION WHERE FINISHDATE IS NULL", Reservation.class);
        List<Reservation> reservations = q.getResultList();
        List<Reservation> list = new ArrayList<>();
        for (Reservation s : reservations){
            if(s.getCar().getUser().getId() == userId){
                list.add(s);
            }
        }
        em.close();
        return Collections.singletonList(list);
    }

    @Override
    public Object updateReservation(Object reservation) {
        EntityManager em = emf.createEntityManager();
        Reservation newCp = (Reservation) reservation;
        Reservation k = em.find(Reservation.class, newCp.getId());

        em.getTransaction().begin();

        k.setCar(newCp.getCar());
        k.setParkingSpot(newCp.getParkingSpot());
        k.setStartDate(newCp.getStartDate());
        k.setFinishDate(newCp.getFinishDate());
        k.setFinalPrice(newCp.getFinalPrice());

        em.getTransaction().commit();
        return k;
    }

    @Override
    public Object createDiscountCoupon(String name, Integer discount) {
        DiscountCoupon coupon = new DiscountCoupon();
        coupon.setName(name);
        coupon.setDiscount(discount);
        coupon.setValidity(true);

        persist(coupon);
        return coupon;
    }

    @Override
    public void giveCouponToUser(Long couponId, Long userId) {
        EntityManager em = emf.createEntityManager();
        User user = (User) getUser(userId);
        DiscountCoupon coupon = em.find(DiscountCoupon.class,couponId);

        em.getTransaction().begin();
        coupon.setUser(user);
        em.getTransaction().commit();
    }

    @Override
    public Object getCoupon(Long couponId) {
        EntityManager em = emf.createEntityManager();
        DiscountCoupon p = em.find(DiscountCoupon.class, couponId);
        em.close();
        return p;
    }

    @Override
    public List<Object> getCoupons(Long userId) {
        EntityManager em = emf.createEntityManager();

        Query q = em.createNativeQuery ("SELECT * from DISCOUNT_COUPON where USER_ID = '" + userId +  "'" , DiscountCoupon.class);
        List <DiscountCoupon> coupons =  q.getResultList();

        em.close();
        return Collections.singletonList(coupons);
    }

    @Override
    public Object endReservation(Long reservationId, Long couponId) {
        EntityManager em = emf.createEntityManager();
        em.getTransaction().begin();
        Reservation reservation = em.find(Reservation.class, reservationId);
        if(reservation.getFinishDate() != null){
            System.out.println("Tato rezervacka uz skoncila");
            return null;
        }
        DiscountCoupon coupon = em.find(DiscountCoupon.class, couponId);
        if (!coupon.getValidity()){
            System.out.println("neplatny kupon");
            return null;
        }

        reservation.setFinishDate(new Date());
        reservation.setFinalPrice(countPrice(reservation)-(countPrice(reservation)*(coupon.getDiscount()/100.00)));  //countPrice(reservation)-(countPrice(reservation)*(coupon.getDiscount()/100))
        reservation.setDiscountCoupon(coupon);
        coupon.setValidity(false);
        ParkingSpot spot = em.find(ParkingSpot.class, reservation.getParkingSpot().getId());
        spot.setCar(null);
        em.getTransaction().commit();
        em.close();

        return reservation;
    }

    @Override
    public Object deleteCoupon(Long couponId) {
        EntityManager em = emf.createEntityManager();
        DiscountCoupon coupon = em.find(DiscountCoupon.class,couponId);

        em.getTransaction().begin();
        DiscountCoupon stock = em.merge(coupon);
        em.remove(stock);
        em.getTransaction().commit();

        em.close();
        return coupon;
    }

    private void persist(Object object) {
        EntityManager em = this.emf.createEntityManager();
        em.getTransaction().begin();
        try {
            em.persist(object);
            em.getTransaction().commit();
        } catch (Exception e) {
            e.printStackTrace();
            em.getTransaction().rollback();
        } finally {
            em.close();
        }
    }

    public Object testt(Long carParkId, String floorIdentifier, String spotIdentifier) {
        if (getCarParkFloor(carParkId,floorIdentifier)==null || getCarPark(carParkId)==null){
            return null;
        }
        ParkingSpot spot = new ParkingSpot();
        spot.setCarpark((CarPark) getCarPark(carParkId));
        spot.setSpotIdentifier(spotIdentifier);
        spot.setFloorIdentifier((floorIdentifier));
        //set floor
        spot.setCarparkfloor((CarParkFloor) getCarParkFloor(carParkId,floorIdentifier));
        return spot;
    }

    public Object createCarTest(Long userId, String brand, String model, String colour, String vehicleRegistrationPlate) {
        Car car = new Car();
        car.setUser((User) getUser(userId));
        car.setBrand(brand);
        car.setModel(model);
        car.setColour(colour);
        car.setVehicleRegistrationPlate(vehicleRegistrationPlate);
        return car;
    }

    public List<Object> getParkingSpotsFree(Long carParkId, String floorIdentifier) {
        EntityManager em = emf.createEntityManager();

        Query q = em.createNativeQuery ("SELECT * from PARKING_SPOT where FLOORIDENTIFIER = '" + floorIdentifier +  "' AND CARPARK_ID =  '" + carParkId +  "' AND CAR_ID IS NULL  " , ParkingSpot.class);
        List <ParkingSpot> spots =  q.getResultList();

        em.close();

        return Collections.singletonList(spots);
    }

    public List<Object> getParkingSpotsOccupied(Long carParkId, String floorIdentifier) {
        EntityManager em = emf.createEntityManager();

        Query q = em.createNativeQuery ("SELECT * from PARKING_SPOT where FLOORIDENTIFIER = '" + floorIdentifier +  "' AND CARPARK_ID =  '" + carParkId +  "' AND CAR_ID IS NOT NULL  " , ParkingSpot.class);
        List <ParkingSpot> spots =  q.getResultList();

        em.close();

        return Collections.singletonList(spots);
    }

    public Double countPrice(Reservation reservation) {
        EntityManager em = emf.createEntityManager();
        Long time = reservation.getFinishDate().getTime() - reservation.getStartDate().getTime();
        time = time/1000;    //na sekundy

        ParkingSpot spot = em.find(ParkingSpot.class, reservation.getParkingSpot().getId());
        Integer timeInt = (int) (long) time;  //sekundy
        Integer price = ((timeInt/3600)+1)*spot.getCarpark().getPricePerHour();    //len na test kazdych 5 sekund, 3600 sekund = hodina
        em.close();
        return (double) price;
    }

    public Object createReservationTest(Long parkingSpotId, Long cardId) {

        Reservation reservation = new Reservation();
        reservation.setCar((Car)getCar(cardId));
        reservation.setParkingSpot((ParkingSpot) getParkingSpot(parkingSpotId));
        reservation.setStartDate(new Date());
        reservation.setId(19L);

        return reservation;
    }
}
