package io.github.olivierlemasle.tests;

import static io.github.olivierlemasle.ca.CA.dn;
import static org.junit.Assert.assertArrayEquals;
import static org.junit.Assert.assertEquals;

import javax.security.auth.x500.X500Principal;

import org.bouncycastle.asn1.x500.X500Name;
import org.junit.Test;

import io.github.olivierlemasle.ca.DistinguishedName;

public class DistinguishedNameTest {

  @Test
  public void testImplementations() {
    final DistinguishedName dn = dn("CN=test");
    assertEquals("CN=test", dn.getName());

    final X500Principal principal = dn.getX500Principal();
    final X500Name x500name = dn.getX500Name();

    final DistinguishedName fromPrincipal = dn(principal);
    assertArrayEquals(dn.getEncoded(), fromPrincipal.getEncoded());
    assertEquals(dn.getName(), fromPrincipal.getName());

    final DistinguishedName fromX500 = dn(x500name);
    assertArrayEquals(dn.getEncoded(), fromX500.getEncoded());
    assertEquals(dn.getName(), fromX500.getName());

    assertEquals(principal, fromX500.getX500Principal());
    assertEquals(x500name, fromPrincipal.getX500Name());
  }

  @Test
  public void testBuilder() {
    final DistinguishedName dn = dn(
        "CN=test,L=Paris,St=IdF,O=test-org,OU=my-OU,C=France,STREET=random");

    final DistinguishedName dn2 = dn()
        .setCn("test")
        .setL("Paris")
        .setSt("IdF")
        .setO("test-org")
        .setOu("my-OU")
        .setC("France")
        .setStreet("random")
        .build();

    assertEquals(dn.toString(), dn2.toString());
    assertArrayEquals(dn.getEncoded(), dn2.getEncoded());

    final DistinguishedName dn3 = dn()
        .setCommonName("test")
        .setLocalityName("Paris")
        .setStateOrProvinceName("IdF")
        .setOrganizationName("test-org")
        .setOrganizationalUnitName("my-OU")
        .setCountryName("France")
        .setStreet("random")
        .build();

    assertEquals(dn.toString(), dn3.toString());
    assertArrayEquals(dn.getEncoded(), dn3.getEncoded());
  }
}
