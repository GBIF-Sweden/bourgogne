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
import javax.persistence.UniqueConstraint;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;
import javax.xml.bind.annotation.XmlRootElement;

/**
 *
 * @author korbinus
 */
@Entity
@Table(name = "entities", uniqueConstraints = {
    @UniqueConstraint(columnNames = {"id"}),
    @UniqueConstraint(columnNames = {"originalUID"})})
@XmlRootElement
@NamedQueries({
    @NamedQuery(name = "Entities.findAll", query = "SELECT e FROM Entities e"),
    @NamedQuery(name = "Entities.findByAddedId", query = "SELECT e FROM Entities e WHERE e.addedId = :addedId"),
    @NamedQuery(name = "Entities.findByUpdated", query = "SELECT e FROM Entities e WHERE e.updated = :updated"),
    @NamedQuery(name = "Entities.findByDatasetid", query = "SELECT e FROM Entities e WHERE e.datasetid = :datasetid"),
    @NamedQuery(name = "Entities.findByOriginalUID", query = "SELECT e FROM Entities e WHERE e.originalUID = :originalUID"),
    @NamedQuery(name = "Entities.findByDeleted", query = "SELECT e FROM Entities e WHERE e.deleted = :deleted"),
    @NamedQuery(name = "Entities.findByProcessed", query = "SELECT e FROM Entities e WHERE e.processed = :processed")})
public class Entities implements Serializable {
    private static final long serialVersionUID = 1L;
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Basic(optional = false)
    @Column(name = "added_id", nullable = false)
    private Integer addedId;
    @Basic(optional = false)
    @NotNull
    @Lob
    @Column(name = "id", nullable = false)
    private byte[] id;
    @Basic(optional = false)
    @NotNull
    @Column(name = "updated", nullable = false)
    @Temporal(TemporalType.TIMESTAMP)
    private Date updated;
    @Lob
    @Column(name = "body")
    private byte[] body;
    @Column(name = "datasetid")
    private Integer datasetid;
    @Size(max = 255)
    @Column(name = "originalUID", length = 255)
    private String originalUID;
    @Column(name = "deleted")
    private Boolean deleted;
    @Column(name = "processed")
    private Boolean processed;

    public Entities() {
    }

    public Entities(Integer addedId) {
        this.addedId = addedId;
    }

    public Entities(Integer addedId, byte[] id, Date updated) {
        this.addedId = addedId;
        this.id = id;
        this.updated = updated;
    }

    public Integer getAddedId() {
        return addedId;
    }

    public void setAddedId(Integer addedId) {
        this.addedId = addedId;
    }

    public byte[] getId() {
        return id;
    }

    public void setId(byte[] id) {
        this.id = id;
    }

    public Date getUpdated() {
        return updated;
    }

    public void setUpdated(Date updated) {
        this.updated = updated;
    }

    public byte[] getBody() {
        return body;
    }

    public void setBody(byte[] body) {
        this.body = body;
    }

    public Integer getDatasetid() {
        return datasetid;
    }

    public void setDatasetid(Integer datasetid) {
        this.datasetid = datasetid;
    }

    public String getOriginalUID() {
        return originalUID;
    }

    public void setOriginalUID(String originalUID) {
        this.originalUID = originalUID;
    }

    public Boolean getDeleted() {
        return deleted;
    }

    public void setDeleted(Boolean deleted) {
        this.deleted = deleted;
    }

    public Boolean getProcessed() {
        return processed;
    }

    public void setProcessed(Boolean processed) {
        this.processed = processed;
    }

    @Override
    public int hashCode() {
        int hash = 0;
        hash += (addedId != null ? addedId.hashCode() : 0);
        return hash;
    }

    @Override
    public boolean equals(Object object) {
        // TODO: Warning - this method won't work in the case the id fields are not set
        if (!(object instanceof Entities)) {
            return false;
        }
        Entities other = (Entities) object;
        if ((this.addedId == null && other.addedId != null) || (this.addedId != null && !this.addedId.equals(other.addedId))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "se.gbif.bourgogne.models.Entities[ addedId=" + addedId + " ]";
    }
    
}
