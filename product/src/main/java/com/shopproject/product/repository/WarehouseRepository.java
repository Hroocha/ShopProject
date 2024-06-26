package com.shopproject.product.repository;

import com.shopproject.product.entities.Warehouse;
import jakarta.persistence.LockModeType;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Lock;
import org.springframework.stereotype.Repository;

import java.util.Optional;
import java.util.UUID;

@Repository
public interface WarehouseRepository extends JpaRepository<Warehouse, UUID> {

    @Lock(LockModeType.PESSIMISTIC_WRITE)
    Optional<Warehouse> findById(UUID id);

}

