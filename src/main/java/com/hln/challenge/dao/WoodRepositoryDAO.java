package com.hln.challenge.dao;

import com.hln.challenge.entity.Wood;
import com.hln.challenge.entity.WoodKey;
import com.hln.challenge.entity.WoodType;
import org.springframework.data.jpa.repository.JpaRepository;

import java.math.BigDecimal;
import java.util.List;


public interface WoodRepositoryDAO extends JpaRepository<Wood, WoodKey> {

    List<Wood> findByWoodIdWoodType(WoodType type);

}
