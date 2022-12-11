package com.hln.challenge.format;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.hln.challenge.entity.Wood;
import lombok.Data;

import java.math.BigDecimal;
import java.util.*;

@Data
public class Bundle {

    private String id = "";
    private List<Map<String, Object>> bundle;
    private BigDecimal price;

    @JsonIgnore
    private String delimiter = "-";

    public Bundle(List<Wood> woodList, BigDecimal price){

        bundle = new ArrayList<>();

        int length = woodList.size();
        int currentIteration = 0;

        for(Wood wood: woodList){

            currentIteration++;

            if(woodList.size()==1 ) {
                this.id = wood.getWoodId().getId().toString();
            }
            else {
                this.id += (currentIteration == length) ?
                        (wood.getWoodId().getId().toString()):
                        (wood.getWoodId().getId().toString() + delimiter);
            }
        }
        this.price = price;
        for(Wood wood: woodList){
            Map<String, Object> customMap = new LinkedHashMap<>();
            customMap.put("type", wood.getWoodId().getWoodType().toString());
            customMap.put("id", wood.getWoodId().getId());
            customMap.put("price", wood.getPrice());
            bundle.add(customMap);
        }
    }
}
