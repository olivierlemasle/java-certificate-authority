package io.github.olivierlemasle.caweb.cli;

import static io.github.olivierlemasle.ca.CA.createSelfSignedCertificate;
import static io.github.olivierlemasle.ca.CA.dn;

import io.dropwizard.cli.Command;
import io.dropwizard.setup.Bootstrap;
import io.github.olivierlemasle.ca.DistinguishedName;
import io.github.olivierlemasle.ca.RootCertificate;
import net.sourceforge.argparse4j.inf.Namespace;
import net.sourceforge.argparse4j.inf.Subparser;

public class CreateSelfSignedCertificate extends Command {

  public CreateSelfSignedCertificate() {
    super("simpleca", "Creates a simple self-signed root certificate and saves it in a keystore.");
  }

  @Override
  public void configure(final Subparser parser) {
    parser.addArgument("-s", "--subject")
        .dest("subject")
        .type(String.class)
        .required(true)
        .help("The subject CN");
    parser.addArgument("-o", "--out")
        .dest("out")
        .type(String.class)
        .required(true)
        .help("The name of the keystore to be created");
    parser.addArgument("-p", "--password")
        .dest("password")
        .type(String.class)
        .required(true)
        .help("The password of the keystore to be created");
    parser.addArgument("-c", "--cert")
        .dest("cert")
        .type(String.class)
        .required(true)
        .help("The certificate file to be created");
  }

  @Override
  public void run(final Bootstrap<?> bootstrap, final Namespace namespace) throws Exception {
    final String subject = namespace.getString("subject");
    final String out = namespace.getString("out");
    final String password = namespace.getString("password");
    final String cert = namespace.getString("cert");

    final DistinguishedName dn = dn().setCn(subject).build();
    final RootCertificate root = createSelfSignedCertificate(dn).build();
    root.exportPkcs12(out, password.toCharArray(), subject);
    root.save(cert);
    System.out.format("KeyStore %s created with certificate %s.", out, subject);
  }

}
