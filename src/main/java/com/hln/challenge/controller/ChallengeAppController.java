package com.hln.challenge.controller;

import com.hln.challenge.entity.Wood;
import com.hln.challenge.entity.WoodType;
import com.hln.challenge.exception.ChallengeException;
import com.hln.challenge.format.Bundle;
import com.hln.challenge.service.BundleService;
import com.hln.challenge.service.WoodService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.server.ResponseStatusException;

import java.util.*;

@RestController
@RequestMapping("/api")
public class ChallengeAppController {

    @Autowired
    WoodService woodService;

    @Autowired
    BundleService bundleService;

    @PostMapping("/upload/{type}")
    public List<Map<String, Object>> fileUpload(@PathVariable("type") String woodTypeString, @RequestParam("file") MultipartFile file){
        try{

            WoodType woodType = new WoodType(woodTypeString);

            List<Wood> parsedListFromFile = woodService.getWoodsFromFile(file, woodType);
            woodService.saveWoodType(woodType);
            parsedListFromFile.forEach(woodService::saveWood);

            List<Wood> woodListResult = woodService.findAllByWoodType(woodType);

            woodListResult.sort(Comparator.comparing(Wood::getPrice).reversed()
                    .thenComparing(wood -> wood.getWoodId().getId()));

            return woodListResult.stream().map(woodService::convertWoodToCustomFormat).toList();

        }
        catch(Exception e){
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, e.getMessage());
        }
    }

    @GetMapping("/bundle")
    public Map<String, Object> getBundles(@RequestParam(name="format") List<String> formatsList,
                                          @RequestParam(name="minPrice", required = false) Double minPrice,
                                          @RequestParam(name="maxPrice", required = false) Double maxPrice) {

        try{
            if( formatsList.isEmpty() || new HashSet<>(formatsList).size()!=formatsList.size())
                throw new ChallengeException("REASON: No formats or duplicate formats detected in GET request");


            List<Bundle> bundleList = bundleService.getBundleList(formatsList, minPrice, maxPrice);

            bundleList.sort(Comparator.comparing(Bundle::getPrice).reversed()
                    .thenComparing(Bundle::getId));

            return bundleService.prepareResponse(bundleList);
        }
        catch(ChallengeException e){
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, e.getMessage());
        }
    }

}