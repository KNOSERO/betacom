package com.ravcube.betacom.database.audit;

import org.hibernate.envers.RevisionListener;

public class ItemRevisionListener implements RevisionListener {

    @Override
    public void newRevision(Object revisionEntity) {
        ((ItemRevisionEntity) revisionEntity).setCreatedBy(AuditRevisionContext.getCreatedBy());
    }
}
