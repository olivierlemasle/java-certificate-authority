package io.github.olivierlemasle.ca;

import java.io.BufferedWriter;
import java.io.File;
import java.io.IOException;
import java.io.StringWriter;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.StandardOpenOption;
import java.security.cert.X509Certificate;

import org.bouncycastle.openssl.jcajce.JcaPEMWriter;

class CertificateExporterImpl implements CertificateExporter {
  private final X509Certificate certificate;

  CertificateExporterImpl(final X509Certificate certificate) {
    this.certificate = certificate;
  }

  @Override
  public String printCertificate() {
    final StringWriter sw = new StringWriter();
    try {
      try (JcaPEMWriter writer = new JcaPEMWriter(sw)) {
        writer.writeObject(certificate);
        writer.flush();
        return sw.toString();
      }
    } catch (final IOException e) {
      throw new CaException(e);
    }
  }

  @Override
  public void saveCertificate(final File file) {
    try {
      try (BufferedWriter fw = Files.newBufferedWriter(file.toPath(), StandardCharsets.UTF_8,
          StandardOpenOption.CREATE)) {
        try (JcaPEMWriter writer = new JcaPEMWriter(fw)) {
          writer.writeObject(certificate);
          writer.flush();
        }
      }
    } catch (final IOException e) {
      throw new CaException(e);
    }
  }

  @Override
  public void saveCertificate(final String fileName) {
    final File file = new File(fileName);
    saveCertificate(file);
  }

}
