package com.cocoball.kakaoapipractice.pharmacy.repository;

import com.cocoball.kakaoapipractice.pharmacy.entity.Pharmacy;
import org.springframework.data.jpa.repository.JpaRepository;

public interface PharmacyRepository extends JpaRepository<Pharmacy, Long> {
}
