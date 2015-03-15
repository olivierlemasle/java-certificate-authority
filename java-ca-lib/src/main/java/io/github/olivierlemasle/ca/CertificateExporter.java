package io.github.olivierlemasle.ca;

import java.io.File;

public interface CertificateExporter {

  public String printCertificate();

  public void saveCertificate(File file);

  public void saveCertificate(String fileName);
}
