package com.bankinc.api.controllers;

import com.bankinc.api.models.dto.ProductDto;
import com.bankinc.api.models.request.ProductBalanceRequest;
import com.bankinc.api.models.request.ProductCreationRequest;
import com.bankinc.api.services.products.ProductsService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/card")
@Tag(name ="Product resources")
public class ProductController {
    @Autowired
    private ProductsService productsService;

    @GetMapping("/{productId}/number")
    @Operation(summary = "Generar numero de tarjeta")
    public ResponseEntity<String> generateCardNumber(@PathVariable String productId){
        return ResponseEntity.ok(productsService.generateCardNumber(productId));
    }

    @PostMapping("/create")
    @Operation(summary = "crear producto")
    public ResponseEntity<ProductDto> createProduct(@RequestBody ProductCreationRequest creationRequest){
        return ResponseEntity.ok(productsService.createProduct(creationRequest));
    }

    @PostMapping("/enroll")
    @Operation(summary = "activar producto")
    public ResponseEntity<String> activateCard(@RequestBody long cardId) {
        productsService.activateProduct(cardId);
        return ResponseEntity.ok().build();
    }

    @DeleteMapping("/{cardId}")
    @Operation(summary = "bloquear producto")
    public ResponseEntity<String> blockCard(@PathVariable Long cardId) {
        productsService.blockProduct(cardId);
        return ResponseEntity.ok().build();
    }

    @GetMapping("/balance/{cardId}")
    @Operation(summary = "consultar saldo")
    public ResponseEntity<String> balance(@PathVariable Long cardId){
        return ResponseEntity.ok(productsService.balance(cardId));
    }

    @PostMapping("/balance")
    @Operation(summary = "recargar producto")
    public ResponseEntity<ProductDto> rechargebalance(@RequestBody ProductBalanceRequest balanceRequest){
        return ResponseEntity.ok(productsService.rechargebalance(balanceRequest));
    }
}
