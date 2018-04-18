package uk.gov.bis.lite.user.config;

import com.google.inject.AbstractModule;
import com.google.inject.Provides;
import ru.vyarus.dropwizard.guice.module.support.ConfigurationAwareModule;
import uk.gov.bis.lite.common.metrics.readiness.DefaultReadinessService;
import uk.gov.bis.lite.common.metrics.readiness.ReadinessService;
import uk.gov.bis.lite.common.spire.client.SpireClientConfig;
import uk.gov.bis.lite.common.spire.client.SpireRequestConfig;
import uk.gov.bis.lite.user.service.UserDetailsService;
import uk.gov.bis.lite.user.service.UserDetailsServiceImpl;
import uk.gov.bis.lite.user.service.UserPrivilegesService;
import uk.gov.bis.lite.user.service.UserPrivilegesServiceImpl;
import uk.gov.bis.lite.user.spire.spireuserdetails.SpireUserDetailsClient;
import uk.gov.bis.lite.user.spire.spireuserdetails.SpireUserDetailsErrorHandler;
import uk.gov.bis.lite.user.spire.spireuserdetails.SpireUserDetailsParser;
import uk.gov.bis.lite.user.spire.spireuserroles.SpireUserRolesClient;
import uk.gov.bis.lite.user.spire.spireuserroles.SpireUserRolesErrorHandler;
import uk.gov.bis.lite.user.spire.spireuserroles.SpireUserRolesParser;

public class GuiceModule extends AbstractModule implements ConfigurationAwareModule<UserServiceConfiguration> {

  private UserServiceConfiguration config;

  @Override
  protected void configure() {
    bind(ReadinessService.class).to(DefaultReadinessService.class);
    bind(UserPrivilegesService.class).to(UserPrivilegesServiceImpl.class);
    bind(UserDetailsService.class).to(UserDetailsServiceImpl.class);
  }

  @Override
  public void setConfiguration(UserServiceConfiguration config) {
    this.config = config;
  }

  @Provides
  public SpireUserRolesClient provideSpireUserRolesClient(SpireClientConfig clientConfig) {
    SpireRequestConfig requestConfig =
        new SpireRequestConfig("SPIRE_USER_ROLES", "getRoles", true);
    SpireUserRolesErrorHandler errorHandler = new SpireUserRolesErrorHandler();
    return new SpireUserRolesClient(new SpireUserRolesParser(), clientConfig, requestConfig, errorHandler);
  }

  @Provides
  public SpireUserDetailsClient provideSpireUserDetailsClient(SpireClientConfig clientConfig) {
    SpireRequestConfig requestConfig =
        new SpireRequestConfig("SPIRE_USER_DETAILS", "USER_DETAILS", false);
    return new SpireUserDetailsClient(new SpireUserDetailsParser(), clientConfig, requestConfig, new SpireUserDetailsErrorHandler());
  }

  @Provides
  public SpireClientConfig provideSpireClientConfig(UserServiceConfiguration config) {
    return new SpireClientConfig(config.getSpireClientUsername(), config.getSpireClientPassword(), config.getSpireClientUrl());
  }
}
