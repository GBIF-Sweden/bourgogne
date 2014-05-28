/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package se.gbif.bourgogne.models;

import java.io.Serializable;
import java.util.Date;
import javax.persistence.Basic;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Lob;
import javax.persistence.NamedQueries;
import javax.persistence.NamedQuery;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;
import javax.xml.bind.annotation.XmlRootElement;

/**
 *
 * @author korbinus
 */
@Entity
@Table(name = "transactions")
@XmlRootElement
@NamedQueries({
    @NamedQuery(name = "Transactions.findAll", query = "SELECT t FROM Transactions t"),
    @NamedQuery(name = "Transactions.findById", query = "SELECT t FROM Transactions t WHERE t.id = :id"),
    @NamedQuery(name = "Transactions.findByDt", query = "SELECT t FROM Transactions t WHERE t.dt = :dt"),
    @NamedQuery(name = "Transactions.findByType", query = "SELECT t FROM Transactions t WHERE t.type = :type"),
    @NamedQuery(name = "Transactions.findByDatasetId", query = "SELECT t FROM Transactions t WHERE t.datasetId = :datasetId"),
    @NamedQuery(name = "Transactions.findByCoordinates", query = "SELECT t FROM Transactions t WHERE t.coordinates = :coordinates"),
    @NamedQuery(name = "Transactions.findByCollectioncode", query = "SELECT t FROM Transactions t WHERE t.collectioncode = :collectioncode"),
    @NamedQuery(name = "Transactions.findByInstitutioncode", query = "SELECT t FROM Transactions t WHERE t.institutioncode = :institutioncode")})
public class Transactions implements Serializable {
    private static final long serialVersionUID = 1L;
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Basic(optional = false)
    @Column(name = "id", nullable = false)
    private Long id;
    @Basic(optional = false)
    @NotNull
    @Column(name = "dt", nullable = false)
    @Temporal(TemporalType.TIMESTAMP)
    private Date dt;
    @Size(max = 255)
    @Column(name = "type", length = 255)
    private String type;
    @Basic(optional = false)
    @NotNull
    @Lob
    @Column(name = "entityId", nullable = false)
    private byte[] entityId;
    @Column(name = "datasetId")
    private Integer datasetId;
    @Column(name = "coordinates")
    private Boolean coordinates;
    @Size(max = 255)
    @Column(name = "collectioncode", length = 255)
    private String collectioncode;
    @Size(max = 255)
    @Column(name = "institutioncode", length = 255)
    private String institutioncode;

    public Transactions() {
    }

    public Transactions(Long id) {
        this.id = id;
    }

    public Transactions(Long id, Date dt, byte[] entityId) {
        this.id = id;
        this.dt = dt;
        this.entityId = entityId;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Date getDt() {
        return dt;
    }

    public void setDt(Date dt) {
        this.dt = dt;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public byte[] getEntityId() {
        return entityId;
    }

    public void setEntityId(byte[] entityId) {
        this.entityId = entityId;
    }

    public Integer getDatasetId() {
        return datasetId;
    }

    public void setDatasetId(Integer datasetId) {
        this.datasetId = datasetId;
    }

    public Boolean getCoordinates() {
        return coordinates;
    }

    public void setCoordinates(Boolean coordinates) {
        this.coordinates = coordinates;
    }

    public String getCollectioncode() {
        return collectioncode;
    }

    public void setCollectioncode(String collectioncode) {
        this.collectioncode = collectioncode;
    }

    public String getInstitutioncode() {
        return institutioncode;
    }

    public void setInstitutioncode(String institutioncode) {
        this.institutioncode = institutioncode;
    }

    @Override
    public int hashCode() {
        int hash = 0;
        hash += (id != null ? id.hashCode() : 0);
        return hash;
    }

    @Override
    public boolean equals(Object object) {
        // TODO: Warning - this method won't work in the case the id fields are not set
        if (!(object instanceof Transactions)) {
            return false;
        }
        Transactions other = (Transactions) object;
        if ((this.id == null && other.id != null) || (this.id != null && !this.id.equals(other.id))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "se.gbif.bourgogne.models.Transactions[ id=" + id + " ]";
    }
    
}
