package com.hln.challenge.service;

import com.hln.challenge.exception.ChallengeException;
import com.hln.challenge.format.Bundle;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Map;

@Service
public interface BundleService {
    public List<Bundle> getBundleList(List<String> formatsList, Double minPrice, Double maxPrice) throws ChallengeException;
    public Map<String, Object> prepareResponse(List<Bundle> bundleList);
}
