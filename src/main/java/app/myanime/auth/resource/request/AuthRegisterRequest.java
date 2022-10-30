package app.myanime.auth.resource.request;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.validation.constraints.Email;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Size;

/**
 * Copyright (c) Maga, All Rights Reserved
 * Unauthorized copying of this file, via any medium is strictly prohibited
 * Proprietary and confidential
 * Written by Maga
 **/
@Data
@AllArgsConstructor
@NoArgsConstructor
public class AuthRegisterRequest {

    @NotBlank
    @Size(min = 4, max = 16, message = "should have size between [{min}, {max}]")
    private String name;

    @Email
    @NotBlank
    private String mail;

    @NotBlank
    private String password;
}
