package com.trilogyed.rsvp.model;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import org.springframework.stereotype.Repository;

import javax.persistence.*;
import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Positive;
import java.io.Serializable;
import java.util.Objects;


@Entity
@JsonIgnoreProperties({"hibernateLazyInitalizer", "handler"})
@Table(name = "rsvp")
public class Rsvp {

    @Id
    @GeneratedValue(strategy= GenerationType.AUTO)
    private Integer id;
    @NotEmpty(message = "Guest name must be provided")
    @NotNull
    private String guestName;
    @NotNull
    @Positive(message = "Total attending must be a positive integer value")
    private Integer totalAttending;

    public Rsvp() { }

    public Rsvp(@NotEmpty(message = "Guest name must be provided") @NotNull String guestName, @NotNull @Positive(message = "Total attending must be a positive integer value") Integer totalAttending) {
        this.guestName = guestName;
        this.totalAttending = totalAttending;
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getGuestName() {
        return guestName;
    }

    public void setGuestName(String guestName) {
        this.guestName = guestName;
    }

    public Integer getTotalAttending() {
        return totalAttending;
    }

    public void setTotalAttending(Integer totalAttending) {
        this.totalAttending = totalAttending;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Rsvp rsvp = (Rsvp) o;
        return Objects.equals(getId(), rsvp.getId()) &&
                Objects.equals(getGuestName(), rsvp.getGuestName()) &&
                Objects.equals(getTotalAttending(), rsvp.getTotalAttending());
    }

    @Override
    public int hashCode() {
        return Objects.hash(getId(), getGuestName(), getTotalAttending());
    }

    @Override
    public String toString() {
        return "Rsvp{" +
                "id=" + id +
                ", guestName='" + guestName + '\'' +
                ", totalAttending=" + totalAttending +
                '}';
    }
}
