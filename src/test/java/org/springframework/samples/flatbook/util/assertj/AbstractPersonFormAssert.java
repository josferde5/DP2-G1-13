package org.springframework.samples.flatbook.util.assertj;

import org.assertj.core.api.AbstractObjectAssert;
import org.assertj.core.util.Objects;
import org.springframework.samples.flatbook.model.dtos.PersonForm;

/**
 * Abstract base class for {@link PersonForm} specific assertions - Generated by CustomAssertionGenerator.
 */
@javax.annotation.Generated(value="assertj-assertions-generator")
public abstract class AbstractPersonFormAssert<S extends AbstractPersonFormAssert<S, A>, A extends PersonForm> extends AbstractObjectAssert<S, A> {

  /**
   * Creates a new <code>{@link AbstractPersonFormAssert}</code> to make assertions on actual PersonForm.
   * @param actual the PersonForm we want to make assertions on.
   */
  protected AbstractPersonFormAssert(A actual, Class<S> selfType) {
    super(actual, selfType);
  }

  /**
   * Verifies that the actual PersonForm's authority is equal to the given one.
   * @param authority the given authority to compare the actual PersonForm's authority to.
   * @return this assertion object.
   * @throws AssertionError - if the actual PersonForm's authority is not equal to the given one.
   */
  public S hasAuthority(org.springframework.samples.flatbook.model.enums.AuthoritiesType authority) {
    // check that actual PersonForm we want to make assertions on is not null.
    isNotNull();

    // overrides the default error message with a more explicit one
    String assertjErrorMessage = "\nExpecting authority of:\n  <%s>\nto be:\n  <%s>\nbut was:\n  <%s>";

    // null safe check
    org.springframework.samples.flatbook.model.enums.AuthoritiesType actualAuthority = actual.getAuthority();
    if (!Objects.areEqual(actualAuthority, authority)) {
      failWithMessage(assertjErrorMessage, actual, authority, actualAuthority);
    }

    // return the current assertion for method chaining
    return myself;
  }

  /**
   * Verifies that the actual PersonForm's confirmPassword is equal to the given one.
   * @param confirmPassword the given confirmPassword to compare the actual PersonForm's confirmPassword to.
   * @return this assertion object.
   * @throws AssertionError - if the actual PersonForm's confirmPassword is not equal to the given one.
   */
  public S hasConfirmPassword(String confirmPassword) {
    // check that actual PersonForm we want to make assertions on is not null.
    isNotNull();

    // overrides the default error message with a more explicit one
    String assertjErrorMessage = "\nExpecting confirmPassword of:\n  <%s>\nto be:\n  <%s>\nbut was:\n  <%s>";

    // null safe check
    String actualConfirmPassword = actual.getConfirmPassword();
    if (!Objects.areEqual(actualConfirmPassword, confirmPassword)) {
      failWithMessage(assertjErrorMessage, actual, confirmPassword, actualConfirmPassword);
    }

    // return the current assertion for method chaining
    return myself;
  }

  /**
   * Verifies that the actual PersonForm's dni is equal to the given one.
   * @param dni the given dni to compare the actual PersonForm's dni to.
   * @return this assertion object.
   * @throws AssertionError - if the actual PersonForm's dni is not equal to the given one.
   */
  public S hasDni(String dni) {
    // check that actual PersonForm we want to make assertions on is not null.
    isNotNull();

    // overrides the default error message with a more explicit one
    String assertjErrorMessage = "\nExpecting dni of:\n  <%s>\nto be:\n  <%s>\nbut was:\n  <%s>";

    // null safe check
    String actualDni = actual.getDni();
    if (!Objects.areEqual(actualDni, dni)) {
      failWithMessage(assertjErrorMessage, actual, dni, actualDni);
    }

    // return the current assertion for method chaining
    return myself;
  }

  /**
   * Verifies that the actual PersonForm's email is equal to the given one.
   * @param email the given email to compare the actual PersonForm's email to.
   * @return this assertion object.
   * @throws AssertionError - if the actual PersonForm's email is not equal to the given one.
   */
  public S hasEmail(String email) {
    // check that actual PersonForm we want to make assertions on is not null.
    isNotNull();

    // overrides the default error message with a more explicit one
    String assertjErrorMessage = "\nExpecting email of:\n  <%s>\nto be:\n  <%s>\nbut was:\n  <%s>";

    // null safe check
    String actualEmail = actual.getEmail();
    if (!Objects.areEqual(actualEmail, email)) {
      failWithMessage(assertjErrorMessage, actual, email, actualEmail);
    }

    // return the current assertion for method chaining
    return myself;
  }

  /**
   * Verifies that the actual PersonForm's firstName is equal to the given one.
   * @param firstName the given firstName to compare the actual PersonForm's firstName to.
   * @return this assertion object.
   * @throws AssertionError - if the actual PersonForm's firstName is not equal to the given one.
   */
  public S hasFirstName(String firstName) {
    // check that actual PersonForm we want to make assertions on is not null.
    isNotNull();

    // overrides the default error message with a more explicit one
    String assertjErrorMessage = "\nExpecting firstName of:\n  <%s>\nto be:\n  <%s>\nbut was:\n  <%s>";

    // null safe check
    String actualFirstName = actual.getFirstName();
    if (!Objects.areEqual(actualFirstName, firstName)) {
      failWithMessage(assertjErrorMessage, actual, firstName, actualFirstName);
    }

    // return the current assertion for method chaining
    return myself;
  }

  /**
   * Verifies that the actual PersonForm's lastName is equal to the given one.
   * @param lastName the given lastName to compare the actual PersonForm's lastName to.
   * @return this assertion object.
   * @throws AssertionError - if the actual PersonForm's lastName is not equal to the given one.
   */
  public S hasLastName(String lastName) {
    // check that actual PersonForm we want to make assertions on is not null.
    isNotNull();

    // overrides the default error message with a more explicit one
    String assertjErrorMessage = "\nExpecting lastName of:\n  <%s>\nto be:\n  <%s>\nbut was:\n  <%s>";

    // null safe check
    String actualLastName = actual.getLastName();
    if (!Objects.areEqual(actualLastName, lastName)) {
      failWithMessage(assertjErrorMessage, actual, lastName, actualLastName);
    }

    // return the current assertion for method chaining
    return myself;
  }

  /**
   * Verifies that the actual PersonForm's password is equal to the given one.
   * @param password the given password to compare the actual PersonForm's password to.
   * @return this assertion object.
   * @throws AssertionError - if the actual PersonForm's password is not equal to the given one.
   */
  public S hasPassword(String password) {
    // check that actual PersonForm we want to make assertions on is not null.
    isNotNull();

    // overrides the default error message with a more explicit one
    String assertjErrorMessage = "\nExpecting password of:\n  <%s>\nto be:\n  <%s>\nbut was:\n  <%s>";

    // null safe check
    String actualPassword = actual.getPassword();
    if (!Objects.areEqual(actualPassword, password)) {
      failWithMessage(assertjErrorMessage, actual, password, actualPassword);
    }

    // return the current assertion for method chaining
    return myself;
  }

  /**
   * Verifies that the actual PersonForm's phoneNumber is equal to the given one.
   * @param phoneNumber the given phoneNumber to compare the actual PersonForm's phoneNumber to.
   * @return this assertion object.
   * @throws AssertionError - if the actual PersonForm's phoneNumber is not equal to the given one.
   */
  public S hasPhoneNumber(String phoneNumber) {
    // check that actual PersonForm we want to make assertions on is not null.
    isNotNull();

    // overrides the default error message with a more explicit one
    String assertjErrorMessage = "\nExpecting phoneNumber of:\n  <%s>\nto be:\n  <%s>\nbut was:\n  <%s>";

    // null safe check
    String actualPhoneNumber = actual.getPhoneNumber();
    if (!Objects.areEqual(actualPhoneNumber, phoneNumber)) {
      failWithMessage(assertjErrorMessage, actual, phoneNumber, actualPhoneNumber);
    }

    // return the current assertion for method chaining
    return myself;
  }

  /**
   * Verifies that the actual PersonForm's previousPassword is equal to the given one.
   * @param previousPassword the given previousPassword to compare the actual PersonForm's previousPassword to.
   * @return this assertion object.
   * @throws AssertionError - if the actual PersonForm's previousPassword is not equal to the given one.
   */
  public S hasPreviousPassword(String previousPassword) {
    // check that actual PersonForm we want to make assertions on is not null.
    isNotNull();

    // overrides the default error message with a more explicit one
    String assertjErrorMessage = "\nExpecting previousPassword of:\n  <%s>\nto be:\n  <%s>\nbut was:\n  <%s>";

    // null safe check
    String actualPreviousPassword = actual.getPreviousPassword();
    if (!Objects.areEqual(actualPreviousPassword, previousPassword)) {
      failWithMessage(assertjErrorMessage, actual, previousPassword, actualPreviousPassword);
    }

    // return the current assertion for method chaining
    return myself;
  }

  /**
   * Verifies that the actual PersonForm's saveType is equal to the given one.
   * @param saveType the given saveType to compare the actual PersonForm's saveType to.
   * @return this assertion object.
   * @throws AssertionError - if the actual PersonForm's saveType is not equal to the given one.
   */
  public S hasSaveType(org.springframework.samples.flatbook.model.enums.SaveType saveType) {
    // check that actual PersonForm we want to make assertions on is not null.
    isNotNull();

    // overrides the default error message with a more explicit one
    String assertjErrorMessage = "\nExpecting saveType of:\n  <%s>\nto be:\n  <%s>\nbut was:\n  <%s>";

    // null safe check
    org.springframework.samples.flatbook.model.enums.SaveType actualSaveType = actual.getSaveType();
    if (!Objects.areEqual(actualSaveType, saveType)) {
      failWithMessage(assertjErrorMessage, actual, saveType, actualSaveType);
    }

    // return the current assertion for method chaining
    return myself;
  }

  /**
   * Verifies that the actual PersonForm's username is equal to the given one.
   * @param username the given username to compare the actual PersonForm's username to.
   * @return this assertion object.
   * @throws AssertionError - if the actual PersonForm's username is not equal to the given one.
   */
  public S hasUsername(String username) {
    // check that actual PersonForm we want to make assertions on is not null.
    isNotNull();

    // overrides the default error message with a more explicit one
    String assertjErrorMessage = "\nExpecting username of:\n  <%s>\nto be:\n  <%s>\nbut was:\n  <%s>";

    // null safe check
    String actualUsername = actual.getUsername();
    if (!Objects.areEqual(actualUsername, username)) {
      failWithMessage(assertjErrorMessage, actual, username, actualUsername);
    }

    // return the current assertion for method chaining
    return myself;
  }

}
