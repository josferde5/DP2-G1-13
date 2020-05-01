package org.springframework.samples.flatbook.util.assertj;

import org.springframework.samples.flatbook.model.Flat;

/**
 * {@link Flat} specific assertions - Generated by CustomAssertionGenerator.
 *
 * Although this class is not final to allow Soft assertions proxy, if you wish to extend it,
 * extend {@link AbstractFlatAssert} instead.
 */
@javax.annotation.Generated(value="assertj-assertions-generator")
public class FlatAssert extends AbstractFlatAssert<FlatAssert, Flat> {

  /**
   * Creates a new <code>{@link FlatAssert}</code> to make assertions on actual Flat.
   * @param actual the Flat we want to make assertions on.
   */
  public FlatAssert(Flat actual) {
    super(actual, FlatAssert.class);
  }

  /**
   * An entry point for FlatAssert to follow AssertJ standard <code>assertThat()</code> statements.<br>
   * With a static import, one can write directly: <code>assertThat(myFlat)</code> and get specific assertion with code completion.
   * @param actual the Flat we want to make assertions on.
   * @return a new <code>{@link FlatAssert}</code>
   */
  @org.assertj.core.util.CheckReturnValue
  public static FlatAssert assertThat(Flat actual) {
    return new FlatAssert(actual);
  }
}
