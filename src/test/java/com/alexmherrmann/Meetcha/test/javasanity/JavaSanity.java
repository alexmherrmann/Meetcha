package com.alexmherrmann.Meetcha.test.javasanity;

import com.alexmherrmann.Meetcha.Jeet;
import org.hamcrest.Matchers;
import org.junit.jupiter.api.Test;

public class JavaSanity {

  @Test
  void meetchaTest() {
    var thing = Jeet.meet.betterContainsOnly(
      Matchers.equalToIgnoringCase("A")
    );
  }
}
