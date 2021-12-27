package com.example.pocketmark.util;

import java.util.List;
import java.util.stream.Collectors;

import javax.persistence.Entity;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;

import com.google.common.base.CaseFormat;

import org.springframework.beans.factory.InitializingBean;
import org.springframework.context.annotation.Profile;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Profile("ping9")
public class DataBaseCleanUpHelper implements InitializingBean {

    @PersistenceContext
    private EntityManager em;

    private List<String> dbTableNames;

    @Override
    public void afterPropertiesSet() throws Exception {
        // TODO Auto-generated method stub
        
        dbTableNames = em.getMetamodel().getEntities().stream()
            .filter(e-> e.getJavaType().getAnnotation(Entity.class)!=null)
            .map(e-> CaseFormat.UPPER_CAMEL.to(CaseFormat.LOWER_UNDERSCORE,e.getName()))
            .collect(Collectors.toList());
    }

    @Transactional
    public void execute(){
        //flush unsaved SQL
        em.flush();

        em.createNativeQuery("SET REFERENTIAL_INTEGRITY FALSE").executeUpdate();

        for(String table : dbTableNames){
            em.createNativeQuery("TRUNCATE TABLE "+table).executeUpdate();
            em.createNativeQuery("ALTER TABLE "+ table + " ALTER COLUMN "+"ID RESTART WITH 1").executeUpdate();
        }
        em.createNativeQuery("SET REFERENTIAL_INTEGRITY TRUE").executeUpdate();
    }
    
}
