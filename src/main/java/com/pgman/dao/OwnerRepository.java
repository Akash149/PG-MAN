package com.pgman.dao;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import com.pgman.entities.Owner;

public interface OwnerRepository extends JpaRepository<Owner, String> {
    
    @Query("select o from Owner o where o.email = :email")
    public Owner findByEmail(@Param("email") String email);

    @Query("select o from Owner o where o.phoneNo = :phone")
    public Owner findByPhone(@Param("phone") String phone);

}
