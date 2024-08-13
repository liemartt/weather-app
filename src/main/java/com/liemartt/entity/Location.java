package com.liemartt.entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.hibernate.annotations.OnDelete;
import org.hibernate.annotations.OnDeleteAction;

import java.math.BigDecimal;
import java.util.Objects;

@Entity
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Table(name = "locations", indexes = @Index(name = "nameIndex", columnList = "name"), uniqueConstraints = @UniqueConstraint(columnNames = {"user_id", "latitude", "longitude"}))
public class Location {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    
    private String name;
    
    
    @ManyToOne
    @JoinColumn(name = "user_id")
    @OnDelete(action = OnDeleteAction.CASCADE)
    private User user;
    
    private BigDecimal latitude;
    private BigDecimal longitude;
    
    public Location(String name, User user, BigDecimal latitude, BigDecimal longitude) {
        this.name = name;
        this.user = user;
        this.latitude = latitude;
        this.longitude = longitude;
    }
    
    public Location(BigDecimal longitude, BigDecimal latitude, String name) {
        this.longitude = longitude;
        this.latitude = latitude;
        this.name = name;
    }
    
    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Location location = (Location) o;
        return Objects.equals(user, location.user) && Objects.equals(latitude, location.latitude) && Objects.equals(longitude, location.longitude);
    }
    
    @Override
    public int hashCode() {
        return Objects.hash(user, latitude, longitude);
    }
    
    @Override
    public String toString() {
        return "Location{" +
                "id=" + id +
                ", name='" + name + '\'' +
                ", user=" + user +
                ", latitude=" + latitude +
                ", longitude=" + longitude +
                '}';
    }
}
