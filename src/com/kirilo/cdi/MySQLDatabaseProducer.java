package com.kirilo.cdi;

import javax.enterprise.inject.Alternative;
import javax.enterprise.inject.Produces;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;

@Alternative
public class MySQLDatabaseProducer {
    @Produces
    @PersistenceContext(unitName = "Book")
    private EntityManager em;
}
