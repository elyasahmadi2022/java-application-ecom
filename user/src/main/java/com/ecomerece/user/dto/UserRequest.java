package com.ecomerece.user.dto;
import com.ecomerece.user.model.UserRole;
import lombok.Data;
@Data
public class UserRequest {
    private String firstName;
    private String lastName;
    private String email;
    private String phone;
    private com.ecomerece.user.model.UserRole role;
    private AddressDTO address;
}
