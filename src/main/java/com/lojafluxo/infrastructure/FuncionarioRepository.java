package com.lojafluxo.infrastructure;

import com.lojafluxo.domain.Funcionario;
import jakarta.persistence.EntityManager;
import jakarta.persistence.EntityManagerFactory;

import java.util.ArrayList;
import java.util.List;

public class FuncionarioRepository {
    private EntityManagerFactory entityManagerFactory;

    private FuncionarioRepository(EntityManagerFactory entityManagerFactory){
        this.entityManagerFactory = entityManagerFactory;
    }

    public static FuncionarioRepository newFuncionarioRepository(EntityManagerFactory entityManagerFactory){
        return new FuncionarioRepository(entityManagerFactory);
    }

    //CREATE
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

    // READ BY ID
    public Funcionario findById(long id){
        try (EntityManager entityManager = entityManagerFactory.createEntityManager()){
            return entityManager.find(Funcionario.class, id);
        } catch (RuntimeException e) {
            throw new RuntimeException(e);
        }
    }

    //READ ALL
    public List<Funcionario> findAll(){
        try (EntityManager entityManager = entityManagerFactory.createEntityManager()) {
            return entityManager.createQuery("Select f FROM Funcionario f", Funcionario.class).getResultList();
        } catch (RuntimeException e) {
            throw new RuntimeException(e);
        }
    }

    //UPDATE
    public Funcionario update(Funcionario funcionario){
        EntityManager entityManager = entityManagerFactory.createEntityManager();
        try {
            entityManager.getTransaction().begin();
            entityManager.merge(funcionario);
            entityManager.getTransaction().commit();
            return funcionario;
        } catch (RuntimeException e) {
            entityManager.getTransaction().rollback();
            throw new RuntimeException(e);
        } finally {
            entityManager.close();
        }
    }

    //DELETE
    public void delete(long id){
        EntityManager entityManager = entityManagerFactory.createEntityManager();
        try{
            entityManager.getTransaction().begin();
            Funcionario deletedFuncionario = entityManager.find(Funcionario.class, id);
            entityManager.remove(deletedFuncionario);
            entityManager.getTransaction().commit();
        } catch (RuntimeException e) {
            entityManager.getTransaction().rollback();
            throw new RuntimeException(e);
        } finally {
            entityManager.close();
        }
    }
}
