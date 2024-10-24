package com.bankinc.api.controllers;

import com.bankinc.api.models.dto.ProductTypeDto;
import com.bankinc.api.models.entity.TblProductType;
import com.bankinc.api.services.productType.ProductTypeService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/ProductType")
@Tag(name ="Product Type resources" )
public class ProductTypeController {
    @Autowired
    private ProductTypeService productTypeService;

    @GetMapping("/all")
    @Operation(summary = "listar tipos de productos")
    public List<ProductTypeDto> listProductsType(){
        return productTypeService.findAll();
    }

    @PostMapping("/create")
    @Operation(summary = "crear tipos de productos")
    public ResponseEntity<ProductTypeDto> createProductsType(@RequestBody TblProductType tblProductType){
        return ResponseEntity.ok(productTypeService.save(tblProductType));
    }

    @GetMapping("/{id}")
    @Operation(summary = "consultar tipos de productos")
    public ResponseEntity<Optional<ProductTypeDto>> findById(@PathVariable int id) {
        return ResponseEntity.ok(productTypeService.findById(id));
    }

    @DeleteMapping("/delete/{id}")
    @Operation(summary = "eliminar tipos de productos")
    public ResponseEntity<String> deleteById(@PathVariable int id){
        return ResponseEntity.ok(productTypeService.deleteById(id));
    }

}
