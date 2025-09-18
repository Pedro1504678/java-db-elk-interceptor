//package com.example.teste.infra.repository;
//
//import com.example.teste.infra.entity.UserEntity;
//import jakarta.persistence.EntityManager;
//import jakarta.persistence.PersistenceContext;
//import jakarta.transaction.Transactional;
//import org.apache.catalina.User;
//import org.springframework.data.domain.Example;
//import org.springframework.stereotype.Component;
//
//import java.util.List;
//
//@Component
//public abstract class UserRepositoryImpl implements UserRepository {
//
//    @PersistenceContext
//    private EntityManager entityManager;
//
//    @Transactional
//    public void salvar(UserEntity userEntity) {
//        entityManager.persist(userEntity);
//    }
//
//    @Override
//    public  List<UserEntity> findAll(Example<S> example) {
//        return List.of();
//    }
//}
