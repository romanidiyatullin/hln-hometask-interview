package com.hln.challenge.service;

import com.hln.challenge.dao.WoodRepositoryDAO;
import com.hln.challenge.dao.WoodTypeRepositoryDAO;
import com.hln.challenge.entity.Wood;
import com.hln.challenge.entity.WoodKey;
import com.hln.challenge.entity.WoodType;
import com.hln.challenge.exception.ChallengeException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;


import java.io.IOException;
import java.math.BigDecimal;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.*;
import java.util.stream.Stream;

@Service
public class WoodServiceImpl implements WoodService{

    @Autowired
    WoodRepositoryDAO woodRepositoryDAO;

    @Autowired
    WoodTypeRepositoryDAO woodTypeRepositoryDAO;

    @Value("${tempFilePath}")
    private String tempFilePath;

    @Override
    public List<String> parseFileToStrings (MultipartFile file) throws IOException{

        Path tempFile = Paths.get(tempFilePath);
        file.transferTo(tempFile);

        Stream<String> streamOfLines = Files.lines(tempFile);

        return streamOfLines.filter(s -> s.contains("$") && s.contains("\t")).toList();
    }

    @Override
    public Wood getWoodObjectFromString(String source, WoodType woodType) throws ChallengeException {

            String[] keyValuePairArray = source.split("\\$");
            // Check if parsing was successful - normally we should have 2 array elements - id at [0] and it's value at [1]:
            if(keyValuePairArray.length!=2) {
                throw new ChallengeException("REASON: Cannot parse provided String into Wood object! Please check string ");
            }
            else {
                keyValuePairArray[0] = keyValuePairArray[0].trim();
                keyValuePairArray[1] = keyValuePairArray[1].trim();
                WoodKey woodKey = new WoodKey();
                woodKey.setId(Long.parseLong(keyValuePairArray[0]));
                woodKey.setWoodType(woodType);
                return new Wood(woodKey,BigDecimal.valueOf(Double.parseDouble(keyValuePairArray[1])));
            }
        }

    @Override
    public List<Wood> getWoodsFromFile(MultipartFile file, WoodType woodType) throws IOException, ChallengeException {
        List<String> stringListFromFile = parseFileToStrings(file);

        List<Wood> woodsList = new ArrayList<>();

        for(String string: stringListFromFile)
            woodsList.add(getWoodObjectFromString(string, woodType));

        return woodsList;
    }

    @Override
    public void saveWood(Wood wood){
        woodRepositoryDAO.save(wood);
    }

    @Override
    public void saveWoodType(WoodType woodType){
        woodTypeRepositoryDAO.save(woodType);
    }

    @Override
    public List<Wood> findAllByWoodType(WoodType woodType){
        return woodRepositoryDAO.findByWoodIdWoodType(woodType);
    }

    @Override
    public List<Wood> findAllByWoodTypeAndPriceLessThan(WoodType woodType, BigDecimal maxPrice){

        Comparator<Wood> sortingWoodsBasedOnPrice = (w1,w2)->w1.getPrice().compareTo(w2.getPrice());

        // Don't pick up wood object whose price is already more than maxPrice - first filtration step
        List<Wood> resultList = woodRepositoryDAO.findByWoodIdWoodType(woodType)
                                .stream().filter(wood->wood.getPrice().compareTo(maxPrice)<=0).sorted(sortingWoodsBasedOnPrice).toList();

        return resultList;
    }

    @Override
    public Map<String, Object> convertWoodToCustomFormat (Wood wood) {
        Map<String, Object> map = new LinkedHashMap<>();
        map.put("id", wood.getWoodId().getId());
        map.put("price", wood.getPrice());
        return map;
    }

}