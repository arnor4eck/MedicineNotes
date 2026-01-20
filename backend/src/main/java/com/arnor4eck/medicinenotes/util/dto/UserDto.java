package com.arnor4eck.medicinenotes.util.dto;

import com.arnor4eck.medicinenotes.entity.User;

public record UserDto(long id,
                      String username,
                      String email) {
    public static UserDto fromEntity(User entity){
        return new UserDto(entity.getId(),
                entity.getUsername(),
                entity.getEmail());
    }
}