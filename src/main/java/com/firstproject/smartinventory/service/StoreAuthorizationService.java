package com.firstproject.smartinventory.service;

import com.firstproject.smartinventory.entity.Store;
import com.firstproject.smartinventory.security.SecurityUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
public class StoreAuthorizationService {

    @Autowired
    private  SecurityUtil securityUtil;


    public void verifyUserAccess(Store store){
        if (store == null)
            throw new RuntimeException("Invalid Store Details");

        String currentUserId = securityUtil.getCurrentUserId();

        if(store.getOwner().getId().equals(currentUserId))
            return;

        boolean isEmployee = store.getEmployee()
                .stream()
                .anyMatch(user -> user.getId().equals(currentUserId));

        if(isEmployee)
            throw new RuntimeException("Not authorized to access the store");
    }
}
