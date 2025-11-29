package com.marv.taskify.services;

import com.marv.taskify.domain.entities.User;

import java.util.UUID;

public interface CurrentUserService {

    User getCurrentUser();

    UUID getCurrentUserId();
}
