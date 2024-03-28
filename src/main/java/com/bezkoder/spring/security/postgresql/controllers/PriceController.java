package com.bezkoder.spring.security.postgresql.controllers;

import com.bezkoder.spring.security.postgresql.models.ERole;
import com.bezkoder.spring.security.postgresql.models.Price;
import com.bezkoder.spring.security.postgresql.models.Role;
import com.bezkoder.spring.security.postgresql.models.User;
import com.bezkoder.spring.security.postgresql.payload.response.MessageResponse;
import com.bezkoder.spring.security.postgresql.repository.UserRepository;
import com.bezkoder.spring.security.postgresql.security.jwt.JwtUtils;
import com.bezkoder.spring.security.postgresql.service.PriceService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;
import java.util.Set;

@RestController
@RequestMapping("/prices")
public class PriceController {
    @Autowired
    private PriceService priceService;

    @Autowired
    private JwtUtils jwtUtils;

    @Autowired
    private UserRepository userRepository;

    @GetMapping
    public ResponseEntity<List<Price>> getAllPrices() {
        List<Price> prices = priceService.getAllPrices();
        return new ResponseEntity<>(prices, HttpStatus.OK);
    }

    @GetMapping("/isTest")
    public ResponseEntity<List<Price>> getAllPricesByIsTest(@RequestParam("isTest") boolean isTest) {
        List<Price> prices = priceService.getAllPricesByIsTest(isTest);
        return new ResponseEntity<>(prices, HttpStatus.OK);
    }

    @GetMapping("/{id}")
    public ResponseEntity<Price> getPriceById(@PathVariable("id") Long id) {
        Optional<Price> price = priceService.getPriceById(id);
        return price.map(value -> new ResponseEntity<>(value, HttpStatus.OK))
                .orElseGet(() -> new ResponseEntity<>(HttpStatus.NOT_FOUND));
    }

    @PostMapping
    public ResponseEntity<?> insertPrice(@RequestBody Price price, @RequestHeader(name = "Authorization") String token) {
        String username = jwtUtils.getExistingUsername(token);

        Optional<User> existingUser = userRepository.findByUsername(username);

        if (existingUser.isEmpty()) {
            return ResponseEntity
                    .badRequest()
                    .body(new MessageResponse("The current user is unavailable!"));
        }
        User user = existingUser.get();

        Set<Role> roles = user.getRoles();
        final boolean[] isSuperAdmin = {false};
        roles.forEach(role -> {
            if (role.getName().equals(ERole.ROLE_SUPER_ADMIN)) {
                isSuperAdmin[0] = true;
            }
        });

        if (!isSuperAdmin[0]) {
            return ResponseEntity
                    .unprocessableEntity()
                    .body(new MessageResponse("You don't have a permission to add the price."));
        }
        Price createdPrice = priceService.insertPrice(price);
        return new ResponseEntity<>(createdPrice, HttpStatus.CREATED);
    }

    @PutMapping("/{id}")
    public ResponseEntity<Void> updatePriceById(@PathVariable("id") Long id, @RequestBody Price updatedPrice) {
        priceService.updatePriceById(id, updatedPrice);
        return new ResponseEntity<>(HttpStatus.NO_CONTENT);
    }

    @DeleteMapping
    public ResponseEntity<Void> deleteAllPrices() {
        priceService.deleteAllPrices();
        return new ResponseEntity<>(HttpStatus.NO_CONTENT);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deletePriceById(@PathVariable("id") Long id) {
        priceService.deletePriceById(id);
        return new ResponseEntity<>(HttpStatus.NO_CONTENT);
    }
}