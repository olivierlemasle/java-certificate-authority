package io.github.olivierlemasle.caweb.resources;

import static io.github.olivierlemasle.ca.CA.createSelfSignedCertificate;

import java.util.ArrayList;
import java.util.List;

import javax.ws.rs.GET;
import javax.ws.rs.NotFoundException;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;

import com.codahale.metrics.annotation.Timed;

import io.github.olivierlemasle.ca.DistinguishedName;
import io.github.olivierlemasle.ca.RootCertificate;
import io.github.olivierlemasle.caweb.api.CertificateAuthority;

@Path("/certificateAuthorities")
@Produces(MediaType.APPLICATION_JSON)
public class CertificateAuthoritiesResource {

  public CertificateAuthoritiesResource() {
  }

  @GET
  @Timed
  public List<CertificateAuthority> get() {
    return new ArrayList<>();
  }

  @GET
  @Path("/{dn}")
  @Timed
  public CertificateAuthority get(@PathParam("dn") final String dn) {
    throw new NotFoundException();
  }

  @POST
  @Timed
  public CertificateAuthority create(final DistinguishedName dn) {
    final RootCertificate caCertificate = createSelfSignedCertificate(dn)
        .build();
    return new CertificateAuthority(caCertificate);
  }

}
