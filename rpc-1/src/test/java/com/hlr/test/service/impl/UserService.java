package com.hlr.test.service.impl;

import com.hlr.test.service.IUserService;

public class UserService implements IUserService {
    @Override
    public String getUserName(String id) {
        return "name";
    }
}
