package com.cocoball.kakaoapipractice.direction.repository;

import com.cocoball.kakaoapipractice.direction.entity.Direction;
import org.springframework.data.jpa.repository.JpaRepository;

public interface DirectionRepository extends JpaRepository<Direction, Long> {
}
