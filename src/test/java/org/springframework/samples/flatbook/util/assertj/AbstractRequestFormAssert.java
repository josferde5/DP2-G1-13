package org.springframework.samples.flatbook.util.assertj;

import org.assertj.core.util.Objects;
import org.springframework.samples.flatbook.model.dtos.RequestForm;

/**
 * Abstract base class for {@link RequestForm} specific assertions - Generated by CustomAssertionGenerator.
 */
@javax.annotation.Generated(value="assertj-assertions-generator")
public abstract class AbstractRequestFormAssert<S extends AbstractRequestFormAssert<S, A>, A extends RequestForm> extends AbstractBaseEntityAssert<S, A> {

  /**
   * Creates a new <code>{@link AbstractRequestFormAssert}</code> to make assertions on actual RequestForm.
   * @param actual the RequestForm we want to make assertions on.
   */
  protected AbstractRequestFormAssert(A actual, Class<S> selfType) {
    super(actual, selfType);
  }

  /**
   * Verifies that the actual RequestForm's description is equal to the given one.
   * @param description the given description to compare the actual RequestForm's description to.
   * @return this assertion object.
   * @throws AssertionError - if the actual RequestForm's description is not equal to the given one.
   */
  public S hasDescription(String description) {
    // check that actual RequestForm we want to make assertions on is not null.
    isNotNull();

    // overrides the default error message with a more explicit one
    String assertjErrorMessage = "\nExpecting description of:\n  <%s>\nto be:\n  <%s>\nbut was:\n  <%s>";

    // null safe check
    String actualDescription = actual.getDescription();
    if (!Objects.areEqual(actualDescription, description)) {
      failWithMessage(assertjErrorMessage, actual, description, actualDescription);
    }

    // return the current assertion for method chaining
    return myself;
  }

  /**
   * Verifies that the actual RequestForm's finishDate is equal to the given one.
   * @param finishDate the given finishDate to compare the actual RequestForm's finishDate to.
   * @return this assertion object.
   * @throws AssertionError - if the actual RequestForm's finishDate is not equal to the given one.
   */
  public S hasFinishDate(java.time.LocalDate finishDate) {
    // check that actual RequestForm we want to make assertions on is not null.
    isNotNull();

    // overrides the default error message with a more explicit one
    String assertjErrorMessage = "\nExpecting finishDate of:\n  <%s>\nto be:\n  <%s>\nbut was:\n  <%s>";

    // null safe check
    java.time.LocalDate actualFinishDate = actual.getFinishDate();
    if (!Objects.areEqual(actualFinishDate, finishDate)) {
      failWithMessage(assertjErrorMessage, actual, finishDate, actualFinishDate);
    }

    // return the current assertion for method chaining
    return myself;
  }

  /**
   * Verifies that the actual RequestForm's startDate is equal to the given one.
   * @param startDate the given startDate to compare the actual RequestForm's startDate to.
   * @return this assertion object.
   * @throws AssertionError - if the actual RequestForm's startDate is not equal to the given one.
   */
  public S hasStartDate(java.time.LocalDate startDate) {
    // check that actual RequestForm we want to make assertions on is not null.
    isNotNull();

    // overrides the default error message with a more explicit one
    String assertjErrorMessage = "\nExpecting startDate of:\n  <%s>\nto be:\n  <%s>\nbut was:\n  <%s>";

    // null safe check
    java.time.LocalDate actualStartDate = actual.getStartDate();
    if (!Objects.areEqual(actualStartDate, startDate)) {
      failWithMessage(assertjErrorMessage, actual, startDate, actualStartDate);
    }

    // return the current assertion for method chaining
    return myself;
  }

}
