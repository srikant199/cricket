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
 * A Team.
 */
@Entity
@Table(name = "team")
@Cache(usage = CacheConcurrencyStrategy.NONSTRICT_READ_WRITE)
@Document(indexName = "team")
public class Team implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "team_name")
    private String teamName;

    @Column(name = "team_rules")
    private String teamRules;

    @Column(name = "team_details")
    private String teamDetails;

    @ManyToMany
    @JsonIgnore
//    @JoinTable(name = "team_player", joinColumns = { @JoinColumn(name = "team_id") }, inverseJoinColumns = { @JoinColumn(name = "player_id") })
    @Cache(usage = CacheConcurrencyStrategy.NONSTRICT_READ_WRITE)
    private Set<Player> players = new HashSet<>();

    // jhipster-needle-entity-add-field - JHipster will add fields here, do not remove
    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getTeamName() {
        return teamName;
    }

    public Team teamName(String teamName) {
        this.teamName = teamName;
        return this;
    }

    public void setTeamName(String teamName) {
        this.teamName = teamName;
    }

    public String getTeamRules() {
        return teamRules;
    }

    public Team teamRules(String teamRules) {
        this.teamRules = teamRules;
        return this;
    }

    public void setTeamRules(String teamRules) {
        this.teamRules = teamRules;
    }

    public String getTeamDetails() {
        return teamDetails;
    }

    public Team teamDetails(String teamDetails) {
        this.teamDetails = teamDetails;
        return this;
    }

    public void setTeamDetails(String teamDetails) {
        this.teamDetails = teamDetails;
    }

    public Set<Player> getPlayers() {
        return players;
    }

    public Team players(Set<Player> players) {
        this.players = players;
        return this;
    }

    public Team addPlayer(Player player) {
        this.players.add(player);
        player.getTeams().add(this);
        return this;
    }

    public Team removePlayer(Player player) {
        this.players.remove(player);
        player.getTeams().remove(this);
        return this;
    }

    public void setPlayers(Set<Player> players) {
        this.players = players;
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
        Team team = (Team) o;
        if (team.getId() == null || getId() == null) {
            return false;
        }
        return Objects.equals(getId(), team.getId());
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(getId());
    }

    @Override
    public String toString() {
        return "Team{" +
            "id=" + getId() +
            ", teamName='" + getTeamName() + "'" +
            ", teamRules='" + getTeamRules() + "'" +
            ", teamDetails='" + getTeamDetails() + "'" +
            "}";
    }
}
