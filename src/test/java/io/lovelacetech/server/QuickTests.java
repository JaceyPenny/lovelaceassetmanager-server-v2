package io.lovelacetech.server;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.JUnit4;

import java.util.UUID;

@RunWith(JUnit4.class)
public class QuickTests {

  @Test
  public void testDeserializeUUID() {
    UUID uuid = UUID.fromString("37147b14-0633-4245-80a6-a28adebd001e");
    System.out.println(uuid);
  }
}
