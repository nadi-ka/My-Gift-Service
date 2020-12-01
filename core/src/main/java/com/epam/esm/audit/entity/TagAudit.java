package com.epam.esm.audit.entity;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;

import com.epam.esm.audit.AuditOperation;

@Entity
@Table(name = "Tag_audit")
public class TagAudit {
	
	@Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column(name = "Id")
    private long id;
	
	@Column(name = "TagId")
    private long tagId;
    
    @Column(name = "Name")
    private String name;
    
    @Enumerated(EnumType.STRING)
    @Column(name = "Operation")
    private AuditOperation operation;

	public TagAudit() {
	}

	public TagAudit(long id, long tagId, String name, AuditOperation operation) {
		this.id = id;
		this.tagId = tagId;
		this.name = name;
		this.operation = operation;
	}

	public long getId() {
		return id;
	}

	public void setId(long id) {
		this.id = id;
	}

	public long getEntityId() {
		return tagId;
	}

	public void setEntityId(long entityId) {
		this.tagId = entityId;
	}

	public AuditOperation getOperation() {
		return operation;
	}

	public void setOperation(AuditOperation operation) {
		this.operation = operation;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	@Override
	public String toString() {
		return "TagAudit [id=" + id + ", entityId=" + tagId + ", operation=" + operation + ", name=" + name + "]";
	}

}
