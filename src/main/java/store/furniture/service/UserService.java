package store.furniture.service;

import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import store.furniture.dto.RoleDTO;
import store.furniture.dto.UserCreatedDTO;
import store.furniture.dto.UserDTO;
import store.furniture.exception.*;
import store.furniture.model.Role;
import store.furniture.model.RoleEntity;
import store.furniture.model.UserEntity;
import store.furniture.repository.RoleRepository;
import store.furniture.repository.UserRepository;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

/**
 * UserService
 */
@Service
@RequiredArgsConstructor
public class UserService {

  private final UserRepository userRepository;
  private final RoleRepository roleRepository;

  @Transactional
  public void addRole(String login, Role role)
    throws UserDoesNotExistException, RoleDoesNotExistException, UserRoleAlreadyExists {
    UserEntity userEntity = userRepository.findByLogin(login)
      .orElseThrow(() -> new UserDoesNotExistException(login));

    RoleEntity roleEntity = roleRepository.findByName(role.name())
      .orElseThrow(() -> new RoleDoesNotExistException(role));

    if (userEntity.getRoles().contains(roleEntity)) {
      throw new UserRoleAlreadyExists(login, role);
    }

    userEntity.getRoles().add(roleEntity);
    userRepository.save(userEntity);
  }

  @Transactional
  public void removeRole(String login, Role role)
    throws UserDoesNotExistException, RoleDoesNotExistException, UserRoleDoesNotExist {
    UserEntity userEntity = userRepository.findByLogin(login)
      .orElseThrow(() -> new UserDoesNotExistException(login));

    RoleEntity roleEntity = roleRepository.findByName(role.name())
      .orElseThrow(() -> new RoleDoesNotExistException(role));

    if (!userEntity.getRoles().contains(roleEntity)) {
      throw new UserRoleDoesNotExist(login, role);
    }

    userEntity.getRoles().remove(roleEntity);
    userRepository.save(userEntity);
  }

  public List<RoleDTO> getRoles(String login) throws UserDoesNotExistException {
    UserEntity userEntity = userRepository.findByLogin(login)
      .orElseThrow(() -> new UserDoesNotExistException(login));

    return userEntity.getRoles().stream()
      .map(role -> new RoleDTO(Role.valueOf(role.getName())))
      .collect(Collectors.toList());
  }

  private final PasswordEncoder passwordEncoder;

  @Transactional
  public UserCreatedDTO register(UserDTO req) throws RoleDoesNotExistException, UserAlreadyExists {
    if (userRepository.existsByLogin(req.getLogin())) {
      throw new UserAlreadyExists(req.getLogin());
    }

    var userEntity = UserEntity.builder()
      .login(req.getLogin())
      .password(passwordEncoder.encode(req.getPassword()))
      .roles(new ArrayList<>())
      .build();

    var roleEntity = roleRepository.findByName(Role.PUBLIC.name())
      .orElseThrow(() -> new RoleDoesNotExistException(Role.PUBLIC));

    userEntity.getRoles().add(roleEntity);

    userRepository.save(userEntity);
    return mapToUserCreatedDto(userEntity);
  }

  private UserCreatedDTO mapToUserCreatedDto(UserEntity user) {
    return new UserCreatedDTO(user.getLogin());
  }
}
