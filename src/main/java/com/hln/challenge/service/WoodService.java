package com.hln.challenge.service;

import com.hln.challenge.entity.Wood;
import com.hln.challenge.entity.WoodType;
import com.hln.challenge.exception.ChallengeException;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.math.BigDecimal;
import java.util.List;
import java.util.Map;

@Service
public interface WoodService {
    public List<Wood> getWoodsFromFile(MultipartFile file, WoodType woodType) throws IOException, ChallengeException;
    public List<String> parseFileToStrings (MultipartFile file) throws IOException;
    public Wood getWoodObjectFromString(String source, WoodType woodType) throws ChallengeException;
    public void saveWood(Wood wood);
    public void saveWoodType(WoodType woodType);
    public List<Wood> findAllByWoodType(WoodType woodType);
    public List<Wood> findAllByWoodTypeAndPriceLessThan(WoodType woodType, BigDecimal maxPrice);
    public Map<String, Object> convertWoodToCustomFormat(Wood wood);
}
