package org.springframework.samples.flatbook.util.assertj;

import org.springframework.samples.flatbook.model.dtos.ReviewForm;

/**
 * {@link ReviewForm} specific assertions - Generated by CustomAssertionGenerator.
 *
 * Although this class is not final to allow Soft assertions proxy, if you wish to extend it,
 * extend {@link AbstractReviewFormAssert} instead.
 */
@javax.annotation.Generated(value="assertj-assertions-generator")
public class ReviewFormAssert extends AbstractReviewFormAssert<ReviewFormAssert, ReviewForm> {

  /**
   * Creates a new <code>{@link ReviewFormAssert}</code> to make assertions on actual ReviewForm.
   * @param actual the ReviewForm we want to make assertions on.
   */
  public ReviewFormAssert(ReviewForm actual) {
    super(actual, ReviewFormAssert.class);
  }

  /**
   * An entry point for ReviewFormAssert to follow AssertJ standard <code>assertThat()</code> statements.<br>
   * With a static import, one can write directly: <code>assertThat(myReviewForm)</code> and get specific assertion with code completion.
   * @param actual the ReviewForm we want to make assertions on.
   * @return a new <code>{@link ReviewFormAssert}</code>
   */
  @org.assertj.core.util.CheckReturnValue
  public static ReviewFormAssert assertThat(ReviewForm actual) {
    return new ReviewFormAssert(actual);
  }
}
