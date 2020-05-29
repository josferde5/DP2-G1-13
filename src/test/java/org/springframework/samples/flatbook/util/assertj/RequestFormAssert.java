package org.springframework.samples.flatbook.util.assertj;

import org.springframework.samples.flatbook.model.dtos.RequestForm;

/**
 * {@link RequestForm} specific assertions - Generated by CustomAssertionGenerator.
 *
 * Although this class is not final to allow Soft assertions proxy, if you wish to extend it,
 * extend {@link AbstractRequestFormAssert} instead.
 */
@javax.annotation.Generated(value="assertj-assertions-generator")
public class RequestFormAssert extends AbstractRequestFormAssert<RequestFormAssert, RequestForm> {

  /**
   * Creates a new <code>{@link RequestFormAssert}</code> to make assertions on actual RequestForm.
   * @param actual the RequestForm we want to make assertions on.
   */
  public RequestFormAssert(RequestForm actual) {
    super(actual, RequestFormAssert.class);
  }

  /**
   * An entry point for RequestFormAssert to follow AssertJ standard <code>assertThat()</code> statements.<br>
   * With a static import, one can write directly: <code>assertThat(myRequestForm)</code> and get specific assertion with code completion.
   * @param actual the RequestForm we want to make assertions on.
   * @return a new <code>{@link RequestFormAssert}</code>
   */
  @org.assertj.core.util.CheckReturnValue
  public static RequestFormAssert assertThat(RequestForm actual) {
    return new RequestFormAssert(actual);
  }
}
