package com.fmi.project_01_car_rent.repositories;


import com.fmi.project_01_car_rent.entities.Car;
import com.fmi.project_01_car_rent.entities.Offer;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;

import java.sql.Timestamp;
import java.time.DayOfWeek;
import java.time.LocalDate;
import java.util.List;

@Repository
public class OfferRepository {

    private final JdbcTemplate db;

    public OfferRepository(JdbcTemplate db) {
        this.db = db;
    }

    public boolean createOffer(Offer offer) {
        StringBuilder query = new StringBuilder();
        query.append("INSERT INTO td_offers (client_id, car_id, rental_days, total_price, is_accepted, is_deleted, created_at) ")
                .append("VALUES (")
                .append(offer.getClientId()).append(", ")
                .append(offer.getCarId()).append(", ")
                .append(offer.getRentalDays()).append(", ")
                .append(offer.getTotalPrice()).append(", ")
                .append(offer.isAccepted() ? "TRUE" : "FALSE").append(", ")
                .append(offer.isDeleted() ? "TRUE" : "FALSE").append(", '")
                .append(new Timestamp(System.currentTimeMillis())).append("')");

        this.db.execute(query.toString());
        return true;
    }


    public List<Offer> getOffersByClientId(int clientId) {
        String query = "SELECT * FROM td_offers WHERE client_id = ? AND is_deleted = FALSE";
        return db.query(query, new Object[]{clientId}, (rs, rowNum) -> {
            Offer offer = new Offer();
            offer.setClientId(rs.getInt("client_id"));
            offer.setCarId(rs.getInt("car_id"));
            offer.setRentalDays(rs.getInt("rental_days"));
            offer.setTotalPrice(rs.getDouble("total_price"));
            offer.setAccepted(rs.getBoolean("is_accepted"));
            offer.setDeleted(rs.getBoolean("is_deleted"));
            offer.setCreatedAt(rs.getTimestamp("created_at"));
            return offer;
        });
    }

    public Offer getOfferById(int id) {
        String query = "SELECT * FROM td_offers WHERE id = ? AND is_deleted = FALSE";
        try {
            return db.queryForObject(query, new Object[]{id}, (rs, rowNum) -> {
                Offer offer = new Offer();
                offer.setClientId(rs.getInt("client_id"));
                offer.setCarId(rs.getInt("car_id"));
                offer.setRentalDays(rs.getInt("rental_days"));
                offer.setTotalPrice(rs.getDouble("total_price"));
                offer.setAccepted(rs.getBoolean("is_accepted"));
                offer.setDeleted(rs.getBoolean("is_deleted"));
                offer.setCreatedAt(rs.getTimestamp("created_at"));
                return offer;
            });
        } catch (EmptyResultDataAccessException e) {
            return null;
        }
    }

    public boolean softDeleteOffer(int id) {
        String query = "UPDATE td_offers SET is_deleted = TRUE WHERE id = ?";
        int rowsAffected = db.update(query, id);
        return rowsAffected > 0; // Return true if at least one row was updated
    }

    public boolean acceptOffer(int id) {
        String query = "UPDATE td_offers SET is_accepted = TRUE WHERE id = ? AND is_deleted = FALSE";
        int rowsAffected = db.update(query, id);
        return rowsAffected > 0;
    }


}



