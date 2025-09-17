package com.mybudget.DTOs;

import com.mybudget.Entity.Role;

import java.util.List;

public record RecoveryUserDto(
        Long id,
        String email,
        List<Role> roles
) {
}
