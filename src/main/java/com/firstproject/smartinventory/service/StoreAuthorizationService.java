package com.firstproject.smartinventory.service;

import com.firstproject.smartinventory.entity.Store;
import com.firstproject.smartinventory.exception.auth.UnauthorizedAccessException;
import com.firstproject.smartinventory.exception.badRequest.InvalidInputException;
import com.firstproject.smartinventory.security.SecurityUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.stereotype.Service;

@Service
public class StoreAuthorizationService {

    @Autowired
    private  SecurityUtil securityUtil;


    public void verifyUserAccess(Store store){
        if (store == null)
            throw new InvalidInputException("Invalid Store Details");

        String currentUserId = securityUtil.getCurrentUserId();

        if(store.getOwner().getId().equals(currentUserId))
            return;

        boolean isEmployee = store.getEmployee()
                .stream()
                .anyMatch(user -> user.getId().equals(currentUserId));

        if(!isEmployee)
            throw new UnauthorizedAccessException("Not authorized to access the store");
    }
}
