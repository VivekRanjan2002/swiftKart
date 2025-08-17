package com.app.swiftKart.service;

import com.app.swiftKart.dto.AddressDTO;
import com.app.swiftKart.dto.UserRequest;
import com.app.swiftKart.dto.UserResponse;
import com.app.swiftKart.model.Address;
import com.app.swiftKart.repository.UserRepository;
import com.app.swiftKart.model.UserEntity;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Service
public class UserService {
    @Autowired
    private UserRepository userRepository;
    private List<UserEntity> users= new ArrayList<>();
    private long nextid=0;
    public List<UserResponse> getAllUsers(){
        //return users;
//        List<UserEntity> userList= userRepository.findAll();
//        List<UserResponse> response= new ArrayList<>();
//        for(UserEntity user: userList){
//            response.add(mapToUserResponse(user));
       // }
        return userRepository.findAll().stream()
                .map(this::mapToUserResponse)
                .toList();
    }
    public void addUser(UserRequest userRequest){
//        user.setId(++nextid);
//        users.add(user);
        userRepository.save(mapToUserEntity(userRequest));
    }
    public Optional<UserResponse> fetchUser(Long id){

//      return  users.stream()
//                .filter(user->user.getId().equals(id))
//                .findFirst();
        return userRepository.findById(id)
                .map(this::mapToUserResponse);

    }
    public Boolean updateUser(Long id,UserRequest updatedUserRequest){
//      return  users.stream()
//                .filter(user->user.getId().equals(id))
//                .findFirst()
//                .map(ExistingUser->{
//                    ExistingUser.setFirstname(updatedUser.getFirstname());
//                    ExistingUser.setLastname(updatedUser.getLastname());
//                    return true;
//                })
//                .orElse(false);
        UserEntity updatedUser =mapToUserEntity(updatedUserRequest);
        return userRepository.findById(id)
                .map(ExistingUser->{
                    ExistingUser.setFirstname(updatedUser.getFirstname());
                    ExistingUser.setLastname(updatedUser.getLastname());
                    ExistingUser.setEmail(updatedUser.getEmail());
                    ExistingUser.setPhoneNo(updatedUser.getPhoneNo());
                    ExistingUser.setRole(updatedUser.getRole());
                    Address address = updatedUser.getAddress();
                    ExistingUser.setAddress(address);
                    userRepository.save(ExistingUser);
                    return true;
                })
                .orElse(false);
    }
    private UserResponse mapToUserResponse(UserEntity user){
        UserResponse response = new UserResponse();
        response.setId(String.valueOf(user.getId()));
        response.setFirstName(user.getFirstname());
        response.setLastName(user.getLastname());
        response.setEmail(user.getEmail());
        response.setPhone(user.getPhoneNo());
        response.setRole(user.getRole());
        if(user.getAddress()!=null){
            AddressDTO addressDTO= new AddressDTO();
            addressDTO.setStreet(user.getAddress().getStreet());
            addressDTO.setCity(user.getAddress().getCity());
            addressDTO.setState(user.getAddress().getState());
            addressDTO.setCountry(user.getAddress().getCountry());
            addressDTO.setZipcode(user.getAddress().getZipCode());
            response.setAddress(addressDTO);
        }
        return response;
    }
    private UserEntity mapToUserEntity(UserRequest userRequest){
        UserEntity user= new UserEntity();
        user.setFirstname(userRequest.getFirstName());
        user.setLastname(userRequest.getLastName());
        user.setEmail(userRequest.getEmail());
        user.setPhoneNo(userRequest.getPhone());
        if(userRequest.getAddress()!=null){
            Address address= new Address();
            address.setStreet(userRequest.getAddress().getStreet());
            address.setCity(userRequest.getAddress().getCity());
            address.setState(userRequest.getAddress().getState());
            address.setCountry(userRequest.getAddress().getCountry());
            address.setZipCode(userRequest.getAddress().getZipcode());
            user.setAddress(address);
        }
        return user;
    }
}
