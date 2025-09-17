package com.mybudget.DTOs;

import com.mybudget.model.RoleName;

public record CreateUserDto(
    String name,
    String email,
    String password,
    RoleName role
){

}
