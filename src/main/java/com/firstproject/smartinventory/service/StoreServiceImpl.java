package com.firstproject.smartinventory.service;

import com.firstproject.smartinventory.dto.StoreRequestDTO;
import com.firstproject.smartinventory.dto.StoreResponseDTO;
import com.firstproject.smartinventory.entity.Store;
import com.firstproject.smartinventory.entity.User;
import com.firstproject.smartinventory.mapper.StoreMapper;
import com.firstproject.smartinventory.others.IDGenerator;
import com.firstproject.smartinventory.repository.StoreRepository;
import com.firstproject.smartinventory.repository.UserRepository;
import com.firstproject.smartinventory.security.SecurityUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class StoreServiceImpl implements StoreService{

    @Autowired
    private StoreRepository storeRepository;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private SecurityUtil securityUtil;

    @Override
    public StoreResponseDTO createStore(StoreRequestDTO storeRequestDTO) {
        User currentUser = securityUtil.getCurrUser();
        Store store = StoreMapper.toEntity(storeRequestDTO);
        store.setStoreId(IDGenerator.idGenerator("STORE"));
        store.setOwner(currentUser);
        Store saved = storeRepository.save(store);
        return StoreMapper.toDTO(saved);
    }

    @Override
    public List<StoreResponseDTO> getStoresForUser() {
        User currUser = securityUtil.getCurrUser();
        return storeRepository.findByOwner(currUser).stream().map(StoreMapper::toDTO).toList();
    }
}
