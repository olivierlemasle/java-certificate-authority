package io.github.olivierlemasle.caweb.cli;

import static io.github.olivierlemasle.ca.CA.createCsr;
import static io.github.olivierlemasle.ca.CA.dn;
import static io.github.olivierlemasle.ca.CA.loadRootCertificate;

import io.dropwizard.cli.Command;
import io.dropwizard.setup.Bootstrap;
import io.github.olivierlemasle.ca.Certificate;
import io.github.olivierlemasle.ca.CertificateWithPrivateKey;
import io.github.olivierlemasle.ca.CsrWithPrivateKey;
import io.github.olivierlemasle.ca.DistinguishedName;
import io.github.olivierlemasle.ca.RootCertificate;
import net.sourceforge.argparse4j.inf.Namespace;
import net.sourceforge.argparse4j.inf.Subparser;

public class CreateCertificate extends Command {

  public CreateCertificate() {
    super("createcert", "Creates a signed certificate.");
  }

  @Override
  public void configure(final Subparser parser) {
    parser.addArgument("-s", "--subject")
        .dest("subject")
        .type(String.class)
        .required(true)
        .help("The subject CN");
    parser.addArgument("-r", "--signer")
        .dest("signer")
        .type(String.class)
        .required(true)
        .help("The CN of the root certificate.");
    parser.addArgument("-i", "--in")
        .dest("in")
        .type(String.class)
        .required(true)
        .help("The name of the root certificate keystore");
    parser.addArgument("-p", "--password")
        .dest("password")
        .type(String.class)
        .required(true)
        .help("The password of the keystore");
    parser.addArgument("-c", "--cert")
        .dest("cert")
        .type(String.class)
        .required(true)
        .help("The certificate file to be created");
    parser.addArgument("-k", "--key")
        .dest("key")
        .type(String.class)
        .required(true)
        .help("The key file to be created");
  }

  @Override
  public void run(final Bootstrap<?> bootstrap, final Namespace namespace) throws Exception {
    final String subject = namespace.getString("subject");
    final String signer = namespace.getString("signer");
    final String in = namespace.getString("in");
    final String password = namespace.getString("password");
    final String cert = namespace.getString("cert");
    final String key = namespace.getString("key");

    final RootCertificate root = loadRootCertificate(in, password.toCharArray(), signer);

    final DistinguishedName dn = dn().setCn(subject).build();
    final CsrWithPrivateKey csr = createCsr().generateRequest(dn);

    final Certificate signed = root.signCsr(csr)
        .setRandomSerialNumber()
        .sign();

    final CertificateWithPrivateKey result = signed.attachPrivateKey(csr.getPrivateKey());
    result.save(cert);
    result.saveKey(key);

    System.out.format("Certificate created for %s", subject);
  }

}
