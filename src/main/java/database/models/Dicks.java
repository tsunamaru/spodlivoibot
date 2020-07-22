package database.models;

import org.glassfish.grizzly.http.util.TimeStamp;
import org.hibernate.annotations.Type;

import javax.persistence.*;
import java.io.Serializable;
import java.math.BigDecimal;
import java.sql.Timestamp;
import java.time.LocalDateTime;
import java.util.Calendar;
import java.util.Objects;

import static javax.persistence.GenerationType.IDENTITY;

@Entity
@Table(name = "dicks")
public class Dicks implements Serializable {
    private int id;
    private int size;
    //@Type(type = "org.hibernate.type.CalendarDateType")
    String lastMeasurement;
    private int user;
    private Users users;

    @Id
    @GeneratedValue(strategy = IDENTITY)
    @Column(name = "id", nullable = false)
    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    @Basic
    @Column(name = "size", nullable = false, precision = 2)
    public int getSize() {
        return size;
    }

    public void setSize(int size) {
        this.size = size;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Dicks dicks = (Dicks) o;
        return id == dicks.id &&
                Objects.equals(size, dicks.size);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, size);
    }

    @Basic
    @Column(name = "last_measurement", nullable = false)
    public String getLastMeasurement() {
        return lastMeasurement;
    }

    public void setLastMeasurement(String lastMeasurement) {
        this.lastMeasurement = lastMeasurement;
    }

    @Basic
    @Column(name = "user", nullable = false, updatable = false, insertable = false)
    public int getUser() {
        return user;
    }

    public void setUser(int user) {
        this.user = user;
    }

    @ManyToOne
    @JoinColumn(name = "user", referencedColumnName = "id", nullable = false)
    public Users getUsers() {
        return users;
    }

    public void setUsers(Users users) {
        this.users = users;
    }
}
