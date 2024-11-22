package com.fmi.project_01_car_rent.repositories;

import com.fmi.project_01_car_rent.entities.Client;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;

@Repository
public class ClientRepository {



    private final JdbcTemplate db;

    public ClientRepository(JdbcTemplate db) {
        this.db = db;
    }

    public boolean createClient(Client client) {
        StringBuilder query = new StringBuilder();
        query.append("INSERT INTO td_clients (name, phone, age, address, has_accidents) ")
                .append("VALUES ('")
                .append(client.getName()).append("', '")
                .append(client.getPhone()).append("', ")
                .append(client.getAge()).append(", '")
                .append(client.getAddress()).append("', ")
                .append(client.hasAccidents() ? "TRUE" : "FALSE").append(")");

        this.db.execute(query.toString());
        return true;
    }

    public boolean existsByPhone(String phone) {
        String query = "SELECT COUNT(*) FROM td_clients WHERE phone = ?";
        Integer count = this.db.queryForObject(query, Integer.class, phone);
        return count != null && count > 0;
    }

    public Client findById(Long id) {
        String query = "SELECT * FROM td_clients WHERE id = ?";
        try {
            return this.db.queryForObject(query, new Object[]{id}, (rs, rowNum) -> {
                Client client = new Client();
                client.setId(rs.getInt("id"));
                client.setName(rs.getString("name"));
                client.setPhone(rs.getString("phone"));
                client.setAge(rs.getInt("age"));
                client.setAddress(rs.getString("address"));
                client.setHasAccidents(rs.getBoolean("has_accidents"));
                return client;
            });
        } catch (EmptyResultDataAccessException e) {
            return null;
        }
    }
}
