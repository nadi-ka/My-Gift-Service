package com.epam.esm.audit.listener;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.PostPersist;
import javax.persistence.PreRemove;
import javax.persistence.PreUpdate;
import javax.transaction.Transactional;

import org.hibernate.SessionFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.epam.esm.audit.AuditOperation;
import com.epam.esm.audit.entity.TagAudit;
import com.epam.esm.entity.Tag;

@Component
public class TagListener {
	
//	@PersistenceContext
//	private EntityManager entityManager;
	
	@Autowired
	private SessionFactory sessionFactory;
	
//	@Autowired
//	public TagListener(EntityManager entityManager) {
//		this.entityManager = entityManager;
//	}
	
	@PostPersist
    public void postPersist(Tag tag){
        insertIntoAuditTable(AuditOperation.INSERT, tag);
    }

    @PreUpdate
    public void preUpdate(Tag tag) {
        insertIntoAuditTable(AuditOperation.UPDATE, tag);
    }

    @PreRemove
    public void preRemove(Tag tag){
        insertIntoAuditTable(AuditOperation.DELETE, tag);
    }
	
//	private void insertIntoAuditTable(AuditOperation operation, Tag tag){
//        entityManager.persist(new TagAudit(0, tag.getId(), tag.getName(), operation));
//    }
    
    private void insertIntoAuditTable(AuditOperation operation, Tag tag){
        sessionFactory.getCurrentSession().persist(new TagAudit(0, tag.getId(), tag.getName(), operation));
    }

}
