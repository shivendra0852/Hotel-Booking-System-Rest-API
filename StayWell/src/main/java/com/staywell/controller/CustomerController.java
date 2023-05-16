package com.staywell.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.staywell.dto.CustomerDTO;
import com.staywell.model.Customer;
import com.staywell.service.CustomerService;

@RestController
@RequestMapping("/customer")
public class CustomerController {

    @Autowired
    private CustomerService cService;


    @PostMapping("/register")
    public ResponseEntity< Customer > createCustomer(@RequestBody Customer customer ) {

        Customer res = cService.createCustomer(customer);

        return new ResponseEntity<Customer>(res , HttpStatus.CREATED);
    }

    @PutMapping("/update")
    public  ResponseEntity<Customer> updateCustomer(@RequestBody CustomerDTO customerDto) {

            Customer res =  cService.updateCustomer(customerDto);

            return new ResponseEntity<Customer>(res , HttpStatus.OK);

    }

    @DeleteMapping("/delete")
    public ResponseEntity<Customer> deleteCustomer() {

        Customer res = cService.deleteCustomer();

        return new ResponseEntity<Customer>(res , HttpStatus.OK);
    }

    @GetMapping("/getAllCustomer")
    public ResponseEntity<List<Customer>> getAllCustomer() {

        List<Customer> res = cService.getAllCustomer();

        return new ResponseEntity<List<Customer>>(res , HttpStatus.OK);
    }

    @GetMapping("/getCustomerById/{id}")
    public ResponseEntity<Customer> getCustomerById(@PathVariable("id") Integer id) {

        Customer  res = cService.getCustomerById(id);

        return new ResponseEntity<Customer>(res , HttpStatus.OK);
    }

}
