package com.lojafluxo.infrastructure;

import com.lojafluxo.domain.Cliente;
import jakarta.persistence.EntityManager;
import jakarta.persistence.EntityManagerFactory;
import java.util.List;

public class ClienteRepository {
    private EntityManagerFactory entityManagerFactory;

    private ClienteRepository(EntityManagerFactory entityManagerFactory){
        this.entityManagerFactory = entityManagerFactory;
    }

    public static ClienteRepository newClienteRepository(EntityManagerFactory entityManagerFactory){
        return new ClienteRepository(entityManagerFactory);
    }

    //CREATE
    public void save(Cliente cliente){
        EntityManager entityManager = entityManagerFactory.createEntityManager();
        try {
            entityManager.getTransaction().begin();
            entityManager.persist(cliente);
            entityManager.getTransaction().commit();
        } catch (RuntimeException e) {
            entityManager.getTransaction().rollback();
            throw new RuntimeException(e);
        } finally {
            entityManager.close();
        }
    }

    //READ BY ID
    public Cliente findById(long id){
        try (EntityManager entityManager = entityManagerFactory.createEntityManager()){
            return entityManager.find(Cliente.class, id);
        } catch (RuntimeException e) {
            throw new RuntimeException(e);
        }
    }

    //READ ALL
    public List<Cliente> findAll(){
        try(EntityManager entityManager = entityManagerFactory.createEntityManager()){
            return entityManager.createQuery("SELECT c FROM Cliente c", Cliente.class).getResultList();
        } catch (RuntimeException e) {
            throw new RuntimeException(e);
        }
    }

    //UPDATE
    public Cliente update(Cliente updatedCliente){
        EntityManager entityManager = entityManagerFactory.createEntityManager();
        try{
            entityManager.getTransaction().begin();
            entityManager.merge(updatedCliente);
            entityManager.getTransaction().commit();
            return updatedCliente;
        } catch (RuntimeException e) {
            entityManager.getTransaction().rollback();
            throw new RuntimeException(e);
        } finally {
            entityManager.close();
        }
    }

    //DELETE BY ID
    public void delete(long id){
        EntityManager entityManager = entityManagerFactory.createEntityManager();
        try {
            entityManager.getTransaction().begin();
            Cliente clienteDeleted = entityManager.find(Cliente.class, id);
            entityManager.remove(clienteDeleted);
            entityManager.getTransaction().commit();
        } catch (RuntimeException e) {
            entityManager.getTransaction().rollback();
            throw new RuntimeException(e);
        } finally {
            entityManager.close();
        }
    }
}
