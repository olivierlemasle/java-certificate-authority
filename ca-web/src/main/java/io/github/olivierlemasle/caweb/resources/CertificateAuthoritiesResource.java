package io.github.olivierlemasle.caweb.resources;

import static io.github.olivierlemasle.ca.CA.createSelfSignedCertificate;
import static io.github.olivierlemasle.ca.CA.dn;
import static io.github.olivierlemasle.ca.CA.loadRootCertificate;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.ws.rs.GET;
import javax.ws.rs.NotFoundException;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;

import com.codahale.metrics.annotation.Timed;

import io.github.olivierlemasle.ca.DistinguishedName;
import io.github.olivierlemasle.ca.KeyStoreReader;
import io.github.olivierlemasle.ca.RootCertificate;
import io.github.olivierlemasle.caweb.CaConfiguration.Keystore;
import io.github.olivierlemasle.caweb.api.CertificateAuthority;

@Path("/certificateAuthorities")
@Produces(MediaType.APPLICATION_JSON)
public class CertificateAuthoritiesResource {
  private final Map<String, CertificateAuthority> cas;
  private final String keystorePath;
  private final char[] keystorePassword;

  public CertificateAuthoritiesResource(Keystore keystore) {
    this.keystorePath = keystore.getPath();
    this.keystorePassword = keystore.getPassword().toCharArray();
    this.cas = new HashMap<>();

    final List<String> aliases = new KeyStoreReader().listAliases(keystorePath, keystorePassword);
    for (final String alias : aliases) {
      final RootCertificate rootCert = loadRootCertificate(keystorePath, keystorePassword, alias);
      final CertificateAuthority ca = new CertificateAuthority(rootCert);
      final String key = dn(alias).toString();
      cas.put(key, ca);
    }
  }

  @GET
  @Timed
  public Map<String, CertificateAuthority> get() {
    return cas;
  }

  @GET
  @Path("/{dn}")
  @Timed
  public CertificateAuthority get(@PathParam("dn") final String dn) {
    final CertificateAuthority ca = cas.get(dn);
    if (ca == null)
      throw new NotFoundException();
    return ca;
  }

  @POST
  @Path("/{dn}")
  @Timed
  public CertificateAuthority create(@PathParam("dn") final String subject) {
    final DistinguishedName dn = dn(subject);
    final RootCertificate caCertificate = createSelfSignedCertificate(dn).build();
    final CertificateAuthority ca = new CertificateAuthority(caCertificate);
    cas.put(dn.toString(), ca);
    caCertificate.exportPkcs12(keystorePath, keystorePassword, subject);
    return ca;
  }

}
