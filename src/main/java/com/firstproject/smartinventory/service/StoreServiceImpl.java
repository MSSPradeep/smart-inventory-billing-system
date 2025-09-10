package com.firstproject.smartinventory.service;

import com.firstproject.smartinventory.entity.Store;
import com.firstproject.smartinventory.entity.User;
import com.firstproject.smartinventory.others.IDGenerator;
import com.firstproject.smartinventory.repository.StoreRepository;
import com.firstproject.smartinventory.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
@Service
public class StoreServiceImpl implements StoreService{

    @Autowired
    private StoreRepository storeRepository;

    @Autowired
    private UserRepository userRepository;

    @Override
    public Store createStore(Store store, User currentUser) {
        store.setStoreId(IDGenerator.idGenerator("STORE"));
        store.setOwner(currentUser);
        return storeRepository.save(store);
    }

    @Override
    public List<Store> getStoresForUser(User user) {
        return storeRepository.findByOwner(user);
    }
}
