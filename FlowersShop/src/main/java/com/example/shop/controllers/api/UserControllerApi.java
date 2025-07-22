/*package com.example.shop.controllers.api;

import com.example.shop.dto.*;
import com.example.shop.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.List;

@RestController
@RequestMapping("/user")
public class UserControllerApi {

    @Autowired
    private UserService userService;

    // Show all buyers: GET /product/all
    @GetMapping("/all")
    public AllUsersDto showAllUsers() {
        AllUsersDto allUsersDto = new AllUsersDto();
        List<UserDTO> buyersDTO = userService.findAllUsers();
        allUsersDto.setUsers(buyersDTO);
        return allUsersDto;
    }

 /*  @GetMapping("/{id}")
   public ResponseDTO showUserById(@PathVariable("id") int id) {
        ResponseDTO responseDTO = new ResponseDTO();
        UserDTO userDTO = userService.findById(id);
        responseDTO.setData(userDTO);
        responseDTO.setSuccess(userDTO !=null);
        return responseDTO;
    }


    @PostMapping("/")
    public ResponseEntity<Object> addUser(@RequestBody UserDTO userDTO) {
        HttpHeaders headers = new HttpHeaders();
        headers.add("Content-Type", "application/json");
        UserDTO savedUserDTO = userService.save(userDTO);
        return ResponseEntity.ok(savedUserDTO != null ? savedUserDTO : new HashMap<>());
    }


    @PutMapping("/{id}")
    public ResponseEntity<Object> updateUser(@PathVariable("id") int id, @RequestBody UserDTO userDTO) {
        if(userDTO.getId() != 0) {
            userDTO.setId(0);
        }
        UserDTO userDTOResponse = userService.update(userDTO,id);
        if(userDTOResponse == null) {
            return ResponseEntity.ok(new HashMap<>());
        }
        return ResponseEntity.ok(userDTOResponse);
    }

    // Delete buyer: DELETE /buyer/{id}
    @DeleteMapping("/{id}")
    public ResponseEntity<Object> deleteUser(@PathVariable("id") int id)
           {
        if (id < 0) {
            return ResponseEntity.badRequest().build();
        }
        boolean wasDeleted = userService.delete(id);
        if (wasDeleted) {
            return ResponseEntity.ok(new HashMap<>());
        }
        return ResponseEntity.badRequest().build();
    }

    @GetMapping("/sales")
    public List<UserDTO> showUsersAndSales() {
        return userService.findAllUsersAndSales();
    }

}
*/