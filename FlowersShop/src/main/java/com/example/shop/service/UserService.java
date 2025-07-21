package com.example.shop.service;

import com.example.shop.dto.UserDTO;
import com.example.shop.helpers.DTOManager;
import com.example.shop.model.User;
import com.example.shop.model.Sale;
import com.example.shop.repository.UsersRepositoryCrud;
import com.example.shop.repository.UsersRepositoryJpa;
import com.example.shop.repository.SaleRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Service
public class UserService {

    @Autowired
    UsersRepositoryCrud usersRepositoryCrud;
    @Autowired
    UsersRepositoryJpa usersRepositoryJpa;
    @Autowired
    private DTOManager dtoManager;
    @Autowired
    private SaleRepository saleRepository;

    private UserDTO userToDto(User user) {
        return dtoManager.userToDto(user);
    }

    private User userDtoToModel(UserDTO userDto) {
   User user = new User();
        String[] nameParts = userDto.getFullName().split(" ");
        user.setFirstName(nameParts[0]);
        user.setLastName(nameParts[1]);
        user.setEmail(userDto.getEmail());
        user.setPhone(userDto.getPhone());
        user.setAddress(userDto.getAddress());
        user.setCity(userDto.getCity());
        user.setPostalCode(userDto.getPostalCode());
        user.setPasswordHash(userDto.getPasswordHash());
        user.setRole(userDto.getRole());

        List<Sale> sales = userDto.getSales();
        if (sales != null && !sales.isEmpty()) {
            List<Sale>salesList = saleRepository.findAll();
            user.setSales(salesList);
        }

        return user;
    }

    public List<UserDTO> findAllUsers() {
        List<UserDTO> userDTOList = new ArrayList<>();
        Iterable<User> iterableUsers = usersRepositoryCrud.findAll();
        for (User user : iterableUsers) {
            UserDTO userDTO = userToDto(user);
            userDTOList.add(userDTO);
        }
        return userDTOList;
    }

    public UserDTO findById(int id) {
        Optional<User>userOptional = usersRepositoryCrud.findById(id);
        User user = userOptional.orElse(null);
        return userToDto(user);
    }

    public UserDTO findByEmail(String email) {
        Optional<User> userOptional = usersRepositoryCrud.findByEmail(email);
        User user = userOptional.orElse(null);
        return userToDto(user);
    }


    public UserDTO save(UserDTO userDTO) {
        User user = userDtoToModel(userDTO);
        usersRepositoryCrud.save(user);
        return userToDto(user);
    }

    public User register(User user) {
        return  usersRepositoryCrud.save(user);
    }


    public UserDTO update(UserDTO userDTO, int id) {
        User user = userDtoToModel(userDTO);
        user.setId(id);
        User bayerDB = usersRepositoryCrud.findById(id).orElse(null);
        if (bayerDB == null) {
            return null;
        }
        if(user.getFirstName()!=null) {
            bayerDB.setFirstName(user.getFirstName());
        }
        if(user.getLastName()!=null) {
            bayerDB.setLastName(user.getLastName());
        }
        if(user.getEmail()!=null) {
            bayerDB.setEmail(user.getEmail());
        }
        if(user.getPhone()!=null) {
            bayerDB.setPhone(user.getPhone());
        }
        if(user.getAddress()!=null) {
            bayerDB.setAddress(user.getAddress());
        }
        if(user.getCity()!=null) {
            bayerDB.setCity(user.getCity());
        }
        if(user.getPostalCode()!=null) {
            bayerDB.setPostalCode(user.getPostalCode());
        }
       user = usersRepositoryCrud.save(bayerDB);
        return userToDto(user);
        }

    public boolean delete(int id) {
        usersRepositoryCrud.deleteById(id);
        Optional<User> userOptional = usersRepositoryCrud.findById(id);
        return userOptional.isEmpty();
    }

    public List<UserDTO> findAllUsersAndSales() {
        List<UserDTO> userDTOList = new ArrayList<>();
        List<User> users = usersRepositoryJpa.findAllUsersAndSales();
        for (User user : users) {
            UserDTO userDTO = userToDto(user);
            userDTOList.add(userDTO);
        }
        return userDTOList;
    }

}



