package com.hln.challenge.service;

import com.hln.challenge.entity.Wood;
import com.hln.challenge.entity.WoodType;
import com.hln.challenge.exception.ChallengeException;
import com.hln.challenge.format.Bundle;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.util.*;

@Service
public class BundleServiceImpl implements BundleService {

    @Autowired
    WoodService woodService;


    @Override
    public List<Bundle> getBundleList(List<String> formatsList, Double minPrice, Double maxPrice) throws ChallengeException {

        BigDecimal minPx;
        BigDecimal maxPx;

        if (minPrice == null)
            minPx = BigDecimal.ZERO;
        else
            minPx = BigDecimal.valueOf(minPrice);

        if (maxPrice == null)
            maxPx = BigDecimal.valueOf(Long.MAX_VALUE);
        else
            maxPx = BigDecimal.valueOf(maxPrice);


        int minListSize = Integer.MAX_VALUE;

        Map<String, List<Wood>> woodFormatsMap = new LinkedHashMap<>();

        for (String format : formatsList) {
            List<Wood> woodList = woodService.findAllByWoodTypeAndPriceLessThan(new WoodType(format), maxPx);

            if (woodList.isEmpty())
                throw new ChallengeException("REASON: No wood records with given woodType format(s), so cannot create bundles");


            if (minListSize > woodList.size())
                minListSize = woodList.size();

            woodFormatsMap.put(format, woodList);
        }

        List<Bundle> bundleList = new ArrayList<>();

        List<Wood>[] array = new List[formatsList.size()];

        for (int i = 0; i < formatsList.size(); i++)
                array[i] = woodFormatsMap.get(formatsList.get(i));

        for (int currentIteration = 0; currentIteration < minListSize; currentIteration++) {

            BigDecimal currentlyAchievedPrice = BigDecimal.ZERO;
            List<Wood> woodTuple = new ArrayList<>();

            for (int woodTypesPerBundleCounter = 0;
                 woodTypesPerBundleCounter < woodFormatsMap.keySet().size();
                 woodTypesPerBundleCounter++)
            {
                Wood wood = array[woodTypesPerBundleCounter].get(currentIteration);
                currentlyAchievedPrice = currentlyAchievedPrice.add(wood.getPrice());
                woodTuple.add(wood);
            }

            if( currentlyAchievedPrice.compareTo(minPx) >= 0 && currentlyAchievedPrice.compareTo(maxPx) <=0 ) {
                bundleList.add(new Bundle(woodTuple, currentlyAchievedPrice));
            }

    }

        return bundleList;
    }

    @Override
    public Map<String, Object> prepareResponse(List<Bundle> bundleList) {

        Map<String, Object> resultMap = new LinkedHashMap<>();

        resultMap.put("bundles", bundleList);
        double sum = bundleList.stream().mapToDouble(bundle->bundle.getPrice().doubleValue()).sum();
        resultMap.put("total",BigDecimal.valueOf(sum));

        return resultMap;
    }
}
