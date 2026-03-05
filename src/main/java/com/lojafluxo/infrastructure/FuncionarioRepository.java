package com.lojafluxo.infrastructure;

import com.lojafluxo.domain.Funcionario;
import jakarta.persistence.EntityManager;
import jakarta.persistence.EntityManagerFactory;

import java.util.List;

public class FuncionarioRepository {
    private EntityManagerFactory entityManagerFactory;

    private FuncionarioRepository(EntityManagerFactory entityManagerFactory){
        this.entityManagerFactory = entityManagerFactory;
    }

    public static FuncionarioRepository newFuncionarioRepository(EntityManagerFactory entityManagerFactory){
        return new FuncionarioRepository(entityManagerFactory);
    }

    public void save(Funcionario funcionario){
        EntityManager entityManager = entityManagerFactory.createEntityManager();
        try{
            entityManager.getTransaction().begin();
            entityManager.persist(funcionario);
            entityManager.getTransaction().commit();
        } catch (RuntimeException e) {
            entityManager.getTransaction().rollback();
            throw new RuntimeException(e);
        } finally {
            entityManager.close();
        }
    }

    public Funcionario findById(long id){
        EntityManager entityManager = entityManagerFactory.createEntityManager();
        try {
            return entityManager.find(Funcionario.class, id);
        } catch (RuntimeException e) {
            throw new RuntimeException(e);
        } finally {
            entityManager.close();
        }
    }

    public List<Funcionario> findAll(){
        EntityManager entityManager = entityManagerFactory.createEntityManager();
    }
}
