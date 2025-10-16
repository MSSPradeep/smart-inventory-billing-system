package com.firstproject.smartinventory.service;

import com.firstproject.smartinventory.entity.Store;
import com.firstproject.smartinventory.entity.User;
import com.firstproject.smartinventory.exception.badRequest.InvalidInputException;
import com.firstproject.smartinventory.exception.notFound.StoreNotFoundException;
import com.firstproject.smartinventory.repository.StoreRepository;
import com.firstproject.smartinventory.security.CustomeUserDetails;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

@Service
public class StoreContextService  {

    @Autowired
    private StoreRepository storeRepository;

    public Store getCurrentStore(){
        Object principle = SecurityContextHolder.getContext().getAuthentication().getPrincipal();

        if(!(principle instanceof CustomeUserDetails userDetails)){
            throw new InvalidInputException("Invalid authentication details");
        }

        User user = userDetails.getUser();

        return storeRepository.findByOwner(user).stream().findFirst()
                .orElseThrow(()-> new StoreNotFoundException("No store found for user "+user.getUserName()));

    }
}

