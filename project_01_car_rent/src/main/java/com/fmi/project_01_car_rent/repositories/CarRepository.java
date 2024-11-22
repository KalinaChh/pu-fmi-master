package com.fmi.project_01_car_rent.repositories;

import com.fmi.project_01_car_rent.entities.Car;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public class CarRepository {

    private final JdbcTemplate db;

    public CarRepository(JdbcTemplate db){
        this.db=db;
    }

    public boolean createCar(Car car) {
        StringBuilder query = new StringBuilder();
        query.append("INSERT INTO td_cars")
                .append("(brand, model, location, price_per_day)")
                .append("VALUES")
                .append("('")
                .append(car.getBrand())
                .append("', '")
                .append(car.getModel())
                .append("', '")
                .append(car.getLocation())
                .append("', ")
                .append(car.getPricePerDay())
                .append(")");

        this.db.execute(query.toString());
        return true;
    }

    public List<Car> getAllCars() {
        StringBuilder query = new StringBuilder();
        query.append("SELECT * FROM td_cars WHERE is_deleted = FALSE");

        return this.db.query(query.toString(), (rs, rowNum) -> {
            Car car = new Car();
            car.setId(rs.getInt("id"));
            car.setBrand(rs.getString("brand"));
            car.setModel(rs.getString("model"));
            car.setPricePerDay(rs.getDouble("price_per_day"));
            car.setLocation(rs.getString("location"));
            car.setDeleted(rs.getBoolean("is_deleted"));
            return car;
        });
    }

    public Car getCarById(int id) {
        StringBuilder query = new StringBuilder();
        query.append("SELECT * FROM td_cars WHERE id = ? AND is_deleted = FALSE");

        return this.db.queryForObject(query.toString(), new Object[]{id}, (rs, rowNum) -> {
            Car car = new Car();
            car.setId(rs.getInt("id"));
            car.setBrand(rs.getString("brand"));
            car.setModel(rs.getString("model"));
            car.setPricePerDay(rs.getDouble("price_per_day"));
            car.setLocation(rs.getString("location"));
            car.setDeleted(rs.getBoolean("is_deleted"));
            return car;
        });
    }


    public List<Car> getCarByLocation(String location) {
        StringBuilder query = new StringBuilder();
        query.append("SELECT * FROM td_cars WHERE location = ? AND is_deleted = FALSE");

        return this.db.query(query.toString(), new Object[]{location}, (rs, rowNum) -> {
            Car car = new Car();
            car.setId(rs.getInt("id"));
            car.setBrand(rs.getString("brand"));
            car.setModel(rs.getString("model"));
            car.setPricePerDay(rs.getDouble("price_per_day"));
            car.setLocation(rs.getString("location"));
            car.setDeleted(rs.getBoolean("is_deleted"));
            return car;
        });
    }

    public List<Car> getCarsByClientLocation(int clientId) {
        StringBuilder query = new StringBuilder();
        query.append("SELECT c.* ")
                .append("FROM td_cars c ")
                .append("JOIN td_clients cl ON c.location = cl.address ")
                .append("WHERE cl.id = ? AND c.is_deleted = FALSE");

        return this.db.query(query.toString(), new Object[]{clientId}, (rs, rowNum) -> {
            Car car = new Car();
            car.setId(rs.getInt("id"));
            car.setBrand(rs.getString("brand"));
            car.setModel(rs.getString("model"));
            car.setPricePerDay(rs.getDouble("price_per_day"));
            car.setLocation(rs.getString("location"));
            car.setDeleted(rs.getBoolean("is_deleted"));
            return car;
        });
    }


    public boolean updateCar(Car car) {
        StringBuilder query = new StringBuilder();

        query.append("UPDATE td_cars ")
                .append("SET brand = ?, ")
                .append("model = ?, ")
                .append("price_per_day = ?, ")
                .append("location = ? ")
                .append("WHERE id = ? ")
                .append("AND is_deleted = FALSE");

        int resultCount = db.update(query.toString(),
                car.getBrand(),
                car.getModel(),
                car.getPricePerDay(),
                car.getLocation(),
                car.getId());

        if (resultCount > 1) {
            throw new RuntimeException("More than one car with the same id exists");
        }

        return resultCount == 1;
    }

    public boolean deleteCar(int id) {
        StringBuilder query = new StringBuilder();

        query.append("UPDATE td_cars ")
                .append("SET is_deleted = TRUE ")
                .append("WHERE id = ? ");

        int resultCount     = this.db.update(query.toString(),id);
        return resultCount  == 1;
    }


}
