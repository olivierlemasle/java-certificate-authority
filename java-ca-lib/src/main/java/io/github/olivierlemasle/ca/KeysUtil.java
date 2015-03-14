package io.github.olivierlemasle.ca;

import java.security.InvalidParameterException;
import java.security.KeyPair;
import java.security.KeyPairGenerator;
import java.security.NoSuchAlgorithmException;
import java.security.NoSuchProviderException;

final class KeysUtil {
  private KeysUtil() {
  }

  static KeyPair generateKeyPair() {
    return generateKeyPair(2048);
  }

  static KeyPair generateKeyPair(final int keySize) {
    try {
      final KeyPairGenerator gen = KeyPairGenerator.getInstance("RSA", "BC");
      gen.initialize(keySize);
      return gen.generateKeyPair();
    } catch (final NoSuchAlgorithmException | InvalidParameterException | NoSuchProviderException e) {
      throw new CaException(e);
    }
  }
}
