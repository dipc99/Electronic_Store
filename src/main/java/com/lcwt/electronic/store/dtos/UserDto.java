package com.lcwt.electronic.store.dtos;

import lombok.*;

import javax.validation.constraints.Email;
import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.Pattern;
import javax.validation.constraints.Size;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Data
@Builder
public class UserDto extends CustomFieldsDto{

    private Long userId;
    @NotEmpty       //OR  @NotBlank - 1 or more than 1 char
    @Size(min = 4,max =20,message = "Username Must be min of 4 and max 20 character...")
    private String userName;

   // @Email(message = "Email Address is not Valid..")
    @Pattern(regexp = "^[a-z0-9][-a-z0-9._]+@([-a-z0-9]+\\.)+[a-z]{2,5}$",message = "Invalid Email...")
    private String email;

    @NotEmpty(message = "Password is required...!")
    @Size(min = 4,max = 15,message = "password must be min 4 max 15 character..")
    private String password;

    @NotEmpty
    @Size(min=4,max = 6,message = "Invalid gender...")
    private String gender;

    @NotEmpty(message = "write something...")
    private String about;

    private String image_name;

}
