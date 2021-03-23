package sn.isi.m2gl.domain;

import java.io.Serializable;
import java.time.LocalDate;
import javax.persistence.*;
import javax.validation.constraints.*;

/**
 * A Personne.
 */
@Entity
@Table(name = "personne")
public class Personne implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @NotNull
    @Column(name = "tests", nullable = false)
    private Long tests;

    @NotNull
    @Column(name = "importes", nullable = false)
    private Long importes;

    @NotNull
    @Column(name = "positifs", nullable = false)
    private Long positifs;

    @NotNull
    @Column(name = "deces", nullable = false)
    private Long deces;

    @NotNull
    @Column(name = "gueris", nullable = false)
    private Long gueris;

    @NotNull
    @Column(name = "date", nullable = false, unique = true)
    private LocalDate date;

    // jhipster-needle-entity-add-field - JHipster will add fields here
    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Personne id(Long id) {
        this.id = id;
        return this;
    }

    public Long getTests() {
        return this.tests;
    }

    public Personne tests(Long tests) {
        this.tests = tests;
        return this;
    }

    public void setTests(Long tests) {
        this.tests = tests;
    }

    public Long getImportes() {
        return this.importes;
    }

    public Personne importes(Long importes) {
        this.importes = importes;
        return this;
    }

    public void setImportes(Long importes) {
        this.importes = importes;
    }

    public Long getPositifs() {
        return this.positifs;
    }

    public Personne positifs(Long positifs) {
        this.positifs = positifs;
        return this;
    }

    public void setPositifs(Long positifs) {
        this.positifs = positifs;
    }

    public Long getDeces() {
        return this.deces;
    }

    public Personne deces(Long deces) {
        this.deces = deces;
        return this;
    }

    public void setDeces(Long deces) {
        this.deces = deces;
    }

    public Long getGueris() {
        return this.gueris;
    }

    public Personne gueris(Long gueris) {
        this.gueris = gueris;
        return this;
    }

    public void setGueris(Long gueris) {
        this.gueris = gueris;
    }

    public LocalDate getDate() {
        return this.date;
    }

    public Personne date(LocalDate date) {
        this.date = date;
        return this;
    }

    public void setDate(LocalDate date) {
        this.date = date;
    }

    // jhipster-needle-entity-add-getters-setters - JHipster will add getters and setters here

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof Personne)) {
            return false;
        }
        return id != null && id.equals(((Personne) o).id);
    }

    @Override
    public int hashCode() {
        // see https://vladmihalcea.com/how-to-implement-equals-and-hashcode-using-the-jpa-entity-identifier/
        return getClass().hashCode();
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "Personne{" +
            "id=" + getId() +
            ", tests=" + getTests() +
            ", importes=" + getImportes() +
            ", positifs=" + getPositifs() +
            ", deces=" + getDeces() +
            ", gueris=" + getGueris() +
            ", date='" + getDate() + "'" +
            "}";
    }
}
