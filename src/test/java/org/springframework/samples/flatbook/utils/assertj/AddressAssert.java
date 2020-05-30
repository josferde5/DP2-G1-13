package org.springframework.samples.flatbook.utils.assertj;

import org.springframework.samples.flatbook.model.Address;

/**
 * {@link Address} specific assertions - Generated by CustomAssertionGenerator.
 *
 * Although this class is not final to allow Soft assertions proxy, if you wish to extend it,
 * extend {@link AbstractAddressAssert} instead.
 */
@javax.annotation.Generated(value="assertj-assertions-generator")
public class AddressAssert extends AbstractAddressAssert<AddressAssert, Address> {

  /**
   * Creates a new <code>{@link AddressAssert}</code> to make assertions on actual Address.
   * @param actual the Address we want to make assertions on.
   */
  public AddressAssert(Address actual) {
    super(actual, AddressAssert.class);
  }

  /**
   * An entry point for AddressAssert to follow AssertJ standard <code>assertThat()</code> statements.<br>
   * With a static import, one can write directly: <code>assertThat(myAddress)</code> and get specific assertion with code completion.
   * @param actual the Address we want to make assertions on.
   * @return a new <code>{@link AddressAssert}</code>
   */
  @org.assertj.core.util.CheckReturnValue
  public static AddressAssert assertThat(Address actual) {
    return new AddressAssert(actual);
  }
}