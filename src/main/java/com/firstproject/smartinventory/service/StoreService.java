package com.firstproject.smartinventory.service;

import com.firstproject.smartinventory.entity.Store;
import com.firstproject.smartinventory.entity.User;

import java.util.List;

public interface StoreService {

    Store createStore(Store store, User currentUser);

    List<Store> getStoresForUser(User user);
}
