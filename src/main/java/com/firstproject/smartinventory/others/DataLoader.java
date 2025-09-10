package com.firstproject.smartinventory.others;

import com.firstproject.smartinventory.entity.Role;
import com.firstproject.smartinventory.entity.User;
import com.firstproject.smartinventory.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.ApplicationArguments;
import org.springframework.boot.ApplicationRunner;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;

@Component
public class DataLoader implements ApplicationRunner {

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private PasswordEncoder passwordEncoder;

    @Override
    public void run(ApplicationArguments args) throws Exception {

//        //Creating the default Admin if database is empty
//        if(userRepository.findByUserNameIgnoreCase("admin").isEmpty()){
//            User admin = new User();
//            admin.setUserName("admin");
//            admin.setPassword(passwordEncoder.encode("admin@123"));
//            admin.setRole(Role.ADMIN);
//
//            userRepository.save(admin);
//        }
//
//        //Creating the default Casher if database is empty
//        if(userRepository.findByUserNameIgnoreCase("Cashier").isEmpty()){
//            User defaul = new User();
//            defaul.setUserName("cashier");
//            defaul.setRole(Role.STAFF);
//            defaul.setPassword(passwordEncoder.encode("cashier@123"));
//
//            userRepository.save(defaul);
//        }
    }
}
