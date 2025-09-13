package com.firstproject.smartinventory.mapper;

import com.firstproject.smartinventory.dto.StoreRequestDTO;
import com.firstproject.smartinventory.dto.StoreResponseDTO;
import com.firstproject.smartinventory.entity.Store;


public class StoreMapper {

    public static Store toEntity(StoreRequestDTO storeRequestDTO){
        Store store = new Store();
        store.setStoreAddress(storeRequestDTO.getStoreAddress());
        store.setStoreName(storeRequestDTO.getStoreName());
        return store;
    }

    public static StoreResponseDTO toDTO(Store store){
        StoreResponseDTO dto = new StoreResponseDTO();
        dto.setStoreId(store.getStoreId());
        dto.setStoreName(store.getStoreName());
        dto.setStoreAddress(store.getStoreAddress());
        dto.setOwnerId(store.getOwner().getId());
        dto.setOwnerName(store.getOwner().getUserName());
        return dto;
    }
}
