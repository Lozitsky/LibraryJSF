package com.kirilo.cdi;

import javax.enterprise.inject.Produces;
import javax.persistence.EntityManagerFactory;
import javax.persistence.PersistenceUnit;

public class DBProducer {
    @Produces
    @PersistenceUnit(unitName="Book")
//    @MySQLDatabase
    EntityManagerFactory emf;
}
