/*
 * @(#) $(NAME).java    1.0     2/13/2025
 *
 * Copyright (c) 2025 IUH. All rights reserved.
 */

package iuh.fit.se.services.impl;

/*
 * @description
 * @author: Tran Tan Dat
 * @version: 1.0
 * @created: 13-February-2025 8:15 PM
 */

import iuh.fit.se.dtos.UserDTO;
import iuh.fit.se.entities.User;
import iuh.fit.se.exceptions.ItemNotFoundException;
import iuh.fit.se.repositories.UserRepository;
import iuh.fit.se.services.UserService;
import jakarta.transaction.Transactional;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class UserServiceImpl implements UserService {

    @Autowired
    UserRepository userRepository;

    @Autowired
    ModelMapper modelMapper;

    private UserDTO convertToDTO(User user) {
        UserDTO userDTO = modelMapper.map(user, UserDTO.class);
        return userDTO;
    }

    private User convertToEntity(UserDTO userDTO) {
        User user = modelMapper.map(userDTO, User.class);
        return user;
    }


    @Override
    public UserDTO findById(int id) {
        User user = userRepository.findById(id)
                .orElseThrow(()-> new ItemNotFoundException("Can not find Employee with id: " + id));

        return this.convertToDTO(user);
    }

    @Transactional
    @Override
    public List<UserDTO> findAll() {
        return userRepository.findAll().stream().map(this::convertToDTO).collect(Collectors.toList());
    }

    @Transactional
    @Override
    public UserDTO save(UserDTO userDTO) {
        User user = this.convertToEntity(userDTO);
        user = userRepository.save(user);
        return this.convertToDTO(user);
    }

    @Override
    public boolean delete(int id) {
        this.findById(id);
        userRepository.deleteById(id);
        return true;
    }
}
