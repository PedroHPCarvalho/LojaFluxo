package com.lojafluxo.infrastructure;

import jakarta.persistence.EntityManagerFactory;
import jakarta.persistence.Persistence;

public class JPAConnectionFactory {
    private static EntityManagerFactory instance;

    private JPAConnectionFactory() {
    }

    public static EntityManagerFactory getInstance() {
        if (instance == null) {
            instance = Persistence.createEntityManagerFactory("lojaFluxo");
        }
        return instance;
    }
}
