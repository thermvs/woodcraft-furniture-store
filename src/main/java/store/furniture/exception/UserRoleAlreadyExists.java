package store.furniture.exception;

import store.furniture.model.Role;

/**
 * RoleAlreadyExists
 */
public class UserRoleAlreadyExists extends DatabaseConflictException {
  public UserRoleAlreadyExists(String login, Role role) {
    super(String.format(("role %s for user %s already exists"), role.name(), login));
  }

}
