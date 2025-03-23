package com.example.zarooriyaat.repository;

import com.example.zarooriyaat.signupsellerobj.SellerSignupEntity;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface SellerSignupRepository extends JpaRepository<SellerSignupEntity, Long> {
     Optional<SellerSignupEntity> findByEmailAndPassword(String email, String password);
}
