package com.bezkoder.spring.security.postgresql.service;

import com.bezkoder.spring.security.postgresql.models.Price;
import com.bezkoder.spring.security.postgresql.repository.PriceRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class PriceService {
    @Autowired
    private PriceRepository priceRepository;

    public List<Price> getAllPrices() {
        return priceRepository.findAll();
    }

    public List<Price> getAllPricesByIsTest(boolean isTest) {
        Sort sortById = Sort.by(Sort.Direction.ASC, "id");
        return priceRepository.findByIsTest(isTest, sortById);
    }
    public Optional<Price> getPriceById(Long id) {
        return priceRepository.findById(id);
    }

    public Price insertPrice(Price price) {
        return priceRepository.save(price);
    }

    public void updatePriceById(Long id, Price updatedPrice) {
        updatedPrice.setId(id);
        priceRepository.save(updatedPrice);
    }

    public void deleteAllPrices() {
        priceRepository.deleteAll();
    }

    public void deletePriceById(Long id) {
        priceRepository.deleteById(id);
    }
}
