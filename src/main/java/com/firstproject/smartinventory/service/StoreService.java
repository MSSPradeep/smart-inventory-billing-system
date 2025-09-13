package com.firstproject.smartinventory.service;

import com.firstproject.smartinventory.dto.StoreRequestDTO;
import com.firstproject.smartinventory.dto.StoreResponseDTO;
import com.firstproject.smartinventory.entity.Store;
import com.firstproject.smartinventory.entity.User;

import java.util.List;

public interface StoreService {

    StoreResponseDTO createStore(StoreRequestDTO storeRequestDTO );

    List<StoreResponseDTO> getStoresForUser();
}
