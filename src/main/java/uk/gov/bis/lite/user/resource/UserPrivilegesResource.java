package uk.gov.bis.lite.user.resource;

import com.google.inject.Inject;
import io.dropwizard.auth.Auth;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import uk.gov.bis.lite.common.jwt.LiteJwtUser;
import uk.gov.bis.lite.user.api.view.UserPrivilegesView;
import uk.gov.bis.lite.user.service.UserPrivilegesService;

import java.util.Optional;

import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;

@Path("/user-privileges")
public class UserPrivilegesResource {

  private static final Logger LOGGER = LoggerFactory.getLogger(UserPrivilegesResource.class);

  private final UserPrivilegesService userPrivilegesService;

  @Inject
  public UserPrivilegesResource(UserPrivilegesService userPrivilegesService) {
    this.userPrivilegesService = userPrivilegesService;
  }

  @GET
  @Produces({MediaType.APPLICATION_JSON})
  @Path("/{userId}")
  public Optional<UserPrivilegesView> viewUserPrivileges(@PathParam("userId") String userId, @Auth LiteJwtUser user) {
    return userPrivilegesService.getUserPrivileges(userId);
  }
}
