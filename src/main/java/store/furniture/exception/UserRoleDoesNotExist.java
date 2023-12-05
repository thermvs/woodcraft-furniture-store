package store.furniture.exception;

import store.furniture.model.Role;

/**
 * UserRoleDoesNotExist
 */
public class UserRoleDoesNotExist extends DoesNotExistException {
  public UserRoleDoesNotExist(String login, Role role) {
    super(String.format(("role %s for user %s does not exists"), role.name(), login));
  }

}
