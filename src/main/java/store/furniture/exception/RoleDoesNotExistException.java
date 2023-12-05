package store.furniture.exception;

import store.furniture.model.Role;

/**
 * RoleDoesNotExistException
 */
public class RoleDoesNotExistException extends DoesNotExistException {
  public RoleDoesNotExistException(Role role) {
    super(String.format(("role %s does not exist"), role.name()));
  }
}
