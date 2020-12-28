package com.kirilo.db;

import javax.annotation.Resource;
import javax.ejb.SessionContext;
import javax.ejb.Stateless;
import javax.persistence.PersistenceContext;

@Stateless
@PersistenceContext(name = "jdbc/Library")
public class DataHelper {
    @Resource
    SessionContext context;


}
