package com.moore.models.entities;

import javax.persistence.Column;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.MappedSuperclass;
import javax.persistence.PrePersist;
import javax.persistence.PreUpdate;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;
import java.util.Calendar;

@MappedSuperclass
public class BaseEntity {

    @Id
    @GeneratedValue(strategy= GenerationType.IDENTITY)
    @Column(name = "id")
    private Long id;

    @Temporal(TemporalType.TIMESTAMP)
    @Column(name = "created")
    private Calendar created;

    @Temporal(TemporalType.TIMESTAMP)
    @Column(name = "modified")
    private Calendar modified;

    @PrePersist
    protected void onCreate() {
        created = modified = Calendar.getInstance();
    }

    @PreUpdate
    protected void onUpdate() {
        modified = Calendar.getInstance();
    }

    public Calendar getCreated() {
        return created;
    }

    public Calendar getModified() {
        return modified;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public void setCreated(Calendar created) {
        this.created = created;
    }

    public void setModified(Calendar modified) {
        this.modified = modified;
    }
}
