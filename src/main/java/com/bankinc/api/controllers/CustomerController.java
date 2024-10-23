package com.bankinc.api.controllers;

import com.bankinc.api.models.entity.TblCustomer;
import com.bankinc.api.services.customer.CustomerService;
import io.swagger.v3.oas.annotations.OpenAPIDefinition;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/Customer")
@Tag(name ="Customer resources")
public class CustomerController {
    @Autowired
    private CustomerService customerService;

    @Operation(summary = "Listar clientes")
    @GetMapping("/all")
    public List<TblCustomer> listCustomer(){
        return customerService.finAll();
    }

    @PostMapping("/create")
    @Operation(summary = "crear clientes")
    public ResponseEntity<TblCustomer> createCustomer(@RequestBody TblCustomer tblCustomer){
        return ResponseEntity.ok(customerService.save(tblCustomer));
    }

    @GetMapping("/{id}")
    @Operation(summary = "consultar clientes")
    public ResponseEntity<Optional<TblCustomer>> findById(@PathVariable long id) {
        return ResponseEntity.ok(customerService.findById(id));
    }

    @Operation(summary = "eliminar clientes")
    @DeleteMapping("/delete/{id}")
    public ResponseEntity<String> deleteById(@PathVariable long id){
        return ResponseEntity.ok(customerService.deleteById(id));
    }
}
