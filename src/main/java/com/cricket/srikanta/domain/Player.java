package com.cricket.srikanta.domain;

import com.fasterxml.jackson.annotation.JsonIgnore;
import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;

import javax.persistence.*;

import org.springframework.data.elasticsearch.annotations.Document;
import java.io.Serializable;
import java.util.HashSet;
import java.util.Set;
import java.util.Objects;

/**
 * A Player.
 */
@Entity
@Table(name = "player")
@Cache(usage = CacheConcurrencyStrategy.NONSTRICT_READ_WRITE)
@Document(indexName = "player")
public class Player implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "playername")
    private String playername;

    @Column(name = "player_number")
    private String playerNumber;

    @Column(name = "player_phone_number")
    private String playerPhoneNumber;

    @Column(name = "player_address")
    private String playerAddress;

    @ManyToMany
    @JsonIgnore
    @Cache(usage = CacheConcurrencyStrategy.NONSTRICT_READ_WRITE)
    private Set<Team> teams = new HashSet<>();

    // jhipster-needle-entity-add-field - JHipster will add fields here, do not remove
    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getPlayername() {
        return playername;
    }

    public Player playername(String playername) {
        this.playername = playername;
        return this;
    }

    public void setPlayername(String playername) {
        this.playername = playername;
    }

    public String getPlayerNumber() {
        return playerNumber;
    }

    public Player playerNumber(String playerNumber) {
        this.playerNumber = playerNumber;
        return this;
    }

    public void setPlayerNumber(String playerNumber) {
        this.playerNumber = playerNumber;
    }

    public String getPlayerPhoneNumber() {
        return playerPhoneNumber;
    }

    public Player playerPhoneNumber(String playerPhoneNumber) {
        this.playerPhoneNumber = playerPhoneNumber;
        return this;
    }

    public void setPlayerPhoneNumber(String playerPhoneNumber) {
        this.playerPhoneNumber = playerPhoneNumber;
    }

    public String getPlayerAddress() {
        return playerAddress;
    }

    public Player playerAddress(String playerAddress) {
        this.playerAddress = playerAddress;
        return this;
    }

    public void setPlayerAddress(String playerAddress) {
        this.playerAddress = playerAddress;
    }

    public Set<Team> getTeams() {
        return teams;
    }

    public Player teams(Set<Team> teams) {
        this.teams = teams;
        return this;
    }

    public Player addTeam(Team team) {
        this.teams.add(team);
        team.getPlayers().add(this);
        return this;
    }

    public Player removeTeam(Team team) {
        this.teams.remove(team);
        team.getPlayers().remove(this);
        return this;
    }

    public void setTeams(Set<Team> teams) {
        this.teams = teams;
    }
    // jhipster-needle-entity-add-getters-setters - JHipster will add getters and setters here, do not remove

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }
        Player player = (Player) o;
        if (player.getId() == null || getId() == null) {
            return false;
        }
        return Objects.equals(getId(), player.getId());
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(getId());
    }

    @Override
    public String toString() {
        return "Player{" +
            "id=" + getId() +
            ", playername='" + getPlayername() + "'" +
            ", playerNumber='" + getPlayerNumber() + "'" +
            ", playerPhoneNumber='" + getPlayerPhoneNumber() + "'" +
            ", playerAddress='" + getPlayerAddress() + "'" +
            "}";
    }
}
