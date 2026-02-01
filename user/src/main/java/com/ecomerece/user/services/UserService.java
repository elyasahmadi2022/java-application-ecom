package com.ecomerece.user.services;


import com.ecomerece.user.dto.AddressDTO;
import com.ecomerece.user.dto.UserRequest;
import com.ecomerece.user.dto.UserResponse;
import com.ecomerece.user.model.Address;
import com.ecomerece.user.model.User;
import com.ecomerece.user.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class UserService {
   private final UserRepository userRepository;

    public List<UserResponse> getUsers() {
        return  userRepository.findAll().stream().map(this::mapToUserResponse).collect(Collectors.toList());
    }
    public UserResponse getUser(String id) {
        return userRepository.findById(id).map(this::mapToUserResponse).orElse(null);
    }
    public User createUser(UserRequest requestUser){
        User user = new User();
        updateUserFromRequest(user,requestUser);
       User createUser =  userRepository.save(user);
       return createUser;
    }
    public boolean updateUser(String id, UserRequest updatedRequestUser){
       return  userRepository.findById(id).map(currentUser -> {
            updateUserFromRequest(currentUser,updatedRequestUser);
            userRepository.save(currentUser);
            return true;
        }).orElse(false);

    }
    private UserResponse mapToUserResponse(User user){
        UserResponse userResponse = new UserResponse();
        userResponse.setId(user.getId());
        userResponse.setFirstName(user.getFirstName());
        userResponse.setLastName(user.getLastName());
        userResponse.setEmail(user.getEmail());
        userResponse.setPhone(user.getPhone());
        userResponse.setRole(user.getRole());
        userResponse.setRole(user.getRole());
        if (user.getAddress() != null) {
            AddressDTO addressDTO = new AddressDTO();
            addressDTO.setStreet(user.getAddress().getStreet());
            addressDTO.setCity(user.getAddress().getCity());
            addressDTO.setCountry(user.getAddress().getCountry());
            addressDTO.setStreet(user.getAddress().getStreet());
            addressDTO.setState(user.getAddress().getState());
            addressDTO.setZipCode(user.getAddress().getZipCode());
            userResponse.setAddress(addressDTO);
        }

        return userResponse;
    }
    private void  updateUserFromRequest(User user, UserRequest userRequest){
        user.setEmail(userRequest.getEmail());
        user.setFirstName(userRequest.getFirstName());
        user.setLastName(userRequest.getLastName());
        user.setPhone(userRequest.getPhone());
        user.setRole(userRequest.getRole());
        if (userRequest.getAddress() != null) {
            Address address = new  Address();
            address.setCity(userRequest.getAddress().getCity());
            address.setCountry(userRequest.getAddress().getCountry());
            address.setStreet(userRequest.getAddress().getStreet());
            address.setState(userRequest.getAddress().getState());
            address.setZipCode(userRequest.getAddress().getZipCode());
            user.setAddress(address);
        }
    }

}
