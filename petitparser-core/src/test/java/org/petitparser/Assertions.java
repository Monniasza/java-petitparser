package org.petitparser;

import org.petitparser.context.ParseError;
import org.petitparser.context.Result;
import org.petitparser.parser.Parser;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertTrue;
import static org.junit.Assert.fail;

public class Assertions {

  public static <T> void assertSuccess(Parser parser, String input, T result) {
    assertSuccess(parser, input, result, input.length());
  }

  public static <T> void assertSuccess(
      Parser parser, String input, T expected, int position) {
    Result result = parser.parse(input);
    assertNotNull(result.toString());
    assertTrue("Expected parse success", result.isSuccess());
    assertEquals("Position", position, result.getPosition());
    assertEquals("Result", expected, result.get());
    assertNull("No message expected", result.getMessage());
    assertEquals("Fast parse", position, parser.fastParseOn(input, 0));
  }

  public static <T> void assertFailure(Parser parser, String input) {
    assertFailure(parser, input, 0);
  }

  public static <T> void assertFailure(
      Parser parser, String input, int position) {
    assertFailure(parser, input, position, null);
  }

  public static <T> void assertFailure(
      Parser parser, String input, String message) {
    assertFailure(parser, input, 0, message);
  }

  public static <T> void assertFailure(
      Parser parser, String input, int position, String message) {
    Result result = parser.parse(input);
    assertNotNull(result.toString());
    assertTrue("Expected parse failure", result.isFailure());
    assertEquals("Position", position, result.getPosition());
    if (message == null) {
      assertNotNull("Message expected", result.getMessage());
    } else {
      assertEquals("Message", message, result.getMessage());
    }
    assertEquals("Expected fast parse failure", -1,
        parser.fastParseOn(input, 0));
    try {
      result.get();
    } catch (ParseError error) {
      assertEquals(result, error.getFailure());
      assertEquals(result.getMessage(), error.getMessage());
      return;
    }
    fail("Result#get() did not throw a ParseError");
  }

}
