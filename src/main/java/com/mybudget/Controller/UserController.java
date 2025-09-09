package com.mybudget.Controller;
import com.mybudget.Entity.User;
import com.mybudget.Repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Optional;


@RestController
@RequestMapping("/user")
public class UserController {

    @Autowired
    private UserRepository userRepository;

    @PostMapping
    public ResponseEntity<User> createUser(@RequestBody User user){
        
        if(user.getName() == null || user.getEmail() == null || user.getPassword() == null){
            return ResponseEntity.badRequest().build();
        }
        System.out.println(user);
        User savedUser = userRepository.save(user);
        return ResponseEntity.ok(savedUser);
    }

    @PutMapping("/{id}")
    public ResponseEntity<User> editUser(@PathVariable Long id, @RequestBody User updateUser){
        Optional<User> existingUser = userRepository.findById(id);

        if(existingUser.isPresent()){
            User user = existingUser.get();
            user.setName(updateUser.getName());
            user.setEmail(updateUser.getEmail());
            user.setPassword(updateUser.getPassword());
            User savedUser = userRepository.save(user);
            return ResponseEntity.ok(savedUser);
        }else{
            return ResponseEntity.notFound().build();
        }

    }

    @GetMapping("/{id}")
    public ResponseEntity<User> getUser (@PathVariable Long id){
        Optional<User>  existingUser = userRepository.findById(id);

        if (existingUser.isPresent()) {
            User user = existingUser.get();
            return ResponseEntity.ok(user);
        }else {
            return ResponseEntity.notFound().build();
        }

    }

    @DeleteMapping("/{id}")
    public ResponseEntity<User> deleteUser (@PathVariable Long id){
        Optional<User> existingUser = userRepository.findById(id);

        if (existingUser.isPresent()){
            userRepository.deleteById(id); ;
            return ResponseEntity.ok().build();
        }else{
            return  ResponseEntity.notFound().build();
        }


    }
}
