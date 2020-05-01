package org.springframework.samples.flatbook.util.assertj;

import org.assertj.core.util.Objects;
import org.springframework.samples.flatbook.model.Message;
import org.springframework.samples.flatbook.model.Person;

/**
 * Abstract base class for {@link Message} specific assertions - Generated by CustomAssertionGenerator.
 */
@javax.annotation.Generated(value="assertj-assertions-generator")
public abstract class AbstractMessageAssert<S extends AbstractMessageAssert<S, A>, A extends Message> extends AbstractBaseEntityAssert<S, A> {

  /**
   * Creates a new <code>{@link AbstractMessageAssert}</code> to make assertions on actual Message.
   * @param actual the Message we want to make assertions on.
   */
  protected AbstractMessageAssert(A actual, Class<S> selfType) {
    super(actual, selfType);
  }

  /**
   * Verifies that the actual Message's body is equal to the given one.
   * @param body the given body to compare the actual Message's body to.
   * @return this assertion object.
   * @throws AssertionError - if the actual Message's body is not equal to the given one.
   */
  public S hasBody(String body) {
    // check that actual Message we want to make assertions on is not null.
    isNotNull();

    // overrides the default error message with a more explicit one
    String assertjErrorMessage = "\nExpecting body of:\n  <%s>\nto be:\n  <%s>\nbut was:\n  <%s>";

    // null safe check
    String actualBody = actual.getBody();
    if (!Objects.areEqual(actualBody, body)) {
      failWithMessage(assertjErrorMessage, actual, body, actualBody);
    }

    // return the current assertion for method chaining
    return myself;
  }

  /**
   * Verifies that the actual Message's creationMoment is equal to the given one.
   * @param creationMoment the given creationMoment to compare the actual Message's creationMoment to.
   * @return this assertion object.
   * @throws AssertionError - if the actual Message's creationMoment is not equal to the given one.
   */
  public S hasCreationMoment(java.time.LocalDateTime creationMoment) {
    // check that actual Message we want to make assertions on is not null.
    isNotNull();

    // overrides the default error message with a more explicit one
    String assertjErrorMessage = "\nExpecting creationMoment of:\n  <%s>\nto be:\n  <%s>\nbut was:\n  <%s>";

    // null safe check
    java.time.LocalDateTime actualCreationMoment = actual.getCreationMoment();
    if (!Objects.areEqual(actualCreationMoment, creationMoment)) {
      failWithMessage(assertjErrorMessage, actual, creationMoment, actualCreationMoment);
    }

    // return the current assertion for method chaining
    return myself;
  }

  /**
   * Verifies that the actual Message's receiver is equal to the given one.
   * @param receiver the given receiver to compare the actual Message's receiver to.
   * @return this assertion object.
   * @throws AssertionError - if the actual Message's receiver is not equal to the given one.
   */
  public S hasReceiver(Person receiver) {
    // check that actual Message we want to make assertions on is not null.
    isNotNull();

    // overrides the default error message with a more explicit one
    String assertjErrorMessage = "\nExpecting receiver of:\n  <%s>\nto be:\n  <%s>\nbut was:\n  <%s>";

    // null safe check
    Person actualReceiver = actual.getReceiver();
    if (!Objects.areEqual(actualReceiver, receiver)) {
      failWithMessage(assertjErrorMessage, actual, receiver, actualReceiver);
    }

    // return the current assertion for method chaining
    return myself;
  }

  /**
   * Verifies that the actual Message's sender is equal to the given one.
   * @param sender the given sender to compare the actual Message's sender to.
   * @return this assertion object.
   * @throws AssertionError - if the actual Message's sender is not equal to the given one.
   */
  public S hasSender(Person sender) {
    // check that actual Message we want to make assertions on is not null.
    isNotNull();

    // overrides the default error message with a more explicit one
    String assertjErrorMessage = "\nExpecting sender of:\n  <%s>\nto be:\n  <%s>\nbut was:\n  <%s>";

    // null safe check
    Person actualSender = actual.getSender();
    if (!Objects.areEqual(actualSender, sender)) {
      failWithMessage(assertjErrorMessage, actual, sender, actualSender);
    }

    // return the current assertion for method chaining
    return myself;
  }

}
