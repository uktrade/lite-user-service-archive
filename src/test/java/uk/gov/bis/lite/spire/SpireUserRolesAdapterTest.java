package uk.gov.bis.lite.spire;

import static org.assertj.core.api.Assertions.assertThat;
import static uk.gov.bis.lite.spire.SpireUserRolesUtil.FULL_NAME;
import static uk.gov.bis.lite.spire.SpireUserRolesUtil.RES_TYPE_SPIRE_SAR_USERS;
import static uk.gov.bis.lite.spire.SpireUserRolesUtil.RES_TYPE_SPIRE_SITE_USERS;
import static uk.gov.bis.lite.spire.SpireUserRolesUtil.ROLE_SAR_ADMINISTRATOR;
import static uk.gov.bis.lite.spire.SpireUserRolesUtil.ROLE_SITE_ADMINISTRATOR;
import static uk.gov.bis.lite.spire.SpireUserRolesUtil.buildCustomerAdmin;
import static uk.gov.bis.lite.spire.SpireUserRolesUtil.buildCustomerPreparer;
import static uk.gov.bis.lite.spire.SpireUserRolesUtil.buildCustomerSubmitter;
import static uk.gov.bis.lite.spire.SpireUserRolesUtil.buildSiteAdmin;
import static uk.gov.bis.lite.spire.SpireUserRolesUtil.buildSitePreparer;
import static uk.gov.bis.lite.spire.SpireUserRolesUtil.buildSiteSubmitter;

import org.junit.Test;
import uk.gov.bis.lite.user.api.CustomerView;
import uk.gov.bis.lite.user.api.Role;
import uk.gov.bis.lite.user.api.SiteView;
import uk.gov.bis.lite.user.api.UserPrivilegesView;
import uk.gov.bis.lite.user.spire.SpireUserRole;
import uk.gov.bis.lite.user.spire.SpireUserRoles;
import uk.gov.bis.lite.user.spire.SpireUserRolesAdapter;

import java.util.Arrays;

public class SpireUserRolesAdapterTest {

  private static final String USER_ACCOUNT_TYPE = "REGULATOR";

  private static SpireUserRoles buildSpireUserRoles(SpireUserRole... userRole) {
    return new SpireUserRoles(USER_ACCOUNT_TYPE, Arrays.asList(userRole));
  }

  @Test
  public void customerIsAdminTest() throws Exception {
    SpireUserRoles sur = buildSpireUserRoles(buildCustomerAdmin("SAR123"));

    UserPrivilegesView userPrivs = SpireUserRolesAdapter.adapt(sur);
    assertThat(userPrivs.getCustomers().size()).isEqualTo(1);
    assertThat(userPrivs.getSites().isEmpty()).isTrue();

    CustomerView customer = userPrivs.getCustomers().get(0);
    assertThat(customer.getCustomerId()).isEqualTo("SAR123");
    assertThat(customer.getRole()).isEqualTo(Role.ADMIN);
  }

  @Test
  public void customerIsSubmitterTest() throws Exception {
    SpireUserRoles sur = buildSpireUserRoles(buildCustomerSubmitter("SAR123"));

    UserPrivilegesView userPrivs = SpireUserRolesAdapter.adapt(sur);
    assertThat(userPrivs.getCustomers().size()).isEqualTo(1);
    assertThat(userPrivs.getSites().isEmpty()).isTrue();

    CustomerView customer = userPrivs.getCustomers().get(0);
    assertThat(customer.getCustomerId()).isEqualTo("SAR123");
    assertThat(customer.getRole()).isEqualTo(Role.SUBMITTER);
  }

  @Test
  public void customerIsPreparerTest() throws Exception {
    SpireUserRoles sur = buildSpireUserRoles(buildCustomerPreparer("SAR123"));

    UserPrivilegesView userPrivs = SpireUserRolesAdapter.adapt(sur);
    assertThat(userPrivs.getCustomers().size()).isEqualTo(1);
    assertThat(userPrivs.getSites().isEmpty()).isTrue();

    CustomerView customer = userPrivs.getCustomers().get(0);
    assertThat(customer.getCustomerId()).isEqualTo("SAR123");
    assertThat(customer.getRole()).isEqualTo(Role.PREPARER);
  }

  @Test
  public void siteIsAdminTest() throws Exception {
    SpireUserRoles sur = buildSpireUserRoles(buildSiteAdmin("SAR123"));

    UserPrivilegesView userPrivs = SpireUserRolesAdapter.adapt(sur);
    assertThat(userPrivs.getSites().size()).isEqualTo(1);
    assertThat(userPrivs.getCustomers().isEmpty()).isTrue();

    SiteView site = userPrivs.getSites().get(0);
    assertThat(site.getSiteId()).isEqualTo("SAR123");
    assertThat(site.getRole()).isEqualTo(Role.ADMIN);
  }

  @Test
  public void siteIsSubmitterTest() throws Exception {
    SpireUserRoles sur = buildSpireUserRoles(buildSiteSubmitter("SITE123"));

    UserPrivilegesView userPrivs = SpireUserRolesAdapter.adapt(sur);
    assertThat(userPrivs.getSites().size()).isEqualTo(1);
    assertThat(userPrivs.getCustomers().isEmpty()).isTrue();

    SiteView site = userPrivs.getSites().get(0);
    assertThat(site.getSiteId()).isEqualTo("SITE123");
    assertThat(site.getRole()).isEqualTo(Role.SUBMITTER);
  }

  @Test
  public void siteIsPreparerTest() throws Exception {
    SpireUserRoles sur = buildSpireUserRoles(buildSitePreparer("SITE123"));

    UserPrivilegesView userPrivs = SpireUserRolesAdapter.adapt(sur);
    assertThat(userPrivs.getSites().size()).isEqualTo(1);
    assertThat(userPrivs.getCustomers().isEmpty()).isTrue();

    SiteView site = userPrivs.getSites().get(0);
    assertThat(site.getSiteId()).isEqualTo("SITE123");
    assertThat(site.getRole()).isEqualTo(Role.PREPARER);
  }

  @Test
  public void customerAndSiteTest() throws Exception {
    SpireUserRoles sur = buildSpireUserRoles(buildCustomerAdmin("SAR123"), buildSiteAdmin("SITE123"));

    UserPrivilegesView userPrivs = SpireUserRolesAdapter.adapt(sur);
    assertThat(userPrivs.getSites().size()).isEqualTo(1);
    assertThat(userPrivs.getCustomers().size()).isEqualTo(1);

    CustomerView customer = userPrivs.getCustomers().get(0);
    assertThat(customer.getCustomerId()).isEqualTo("SAR123");
    assertThat(customer.getRole()).isEqualTo(Role.ADMIN);

    SiteView site = userPrivs.getSites().get(0);
    assertThat(site.getSiteId()).isEqualTo("SITE123");
    assertThat(site.getRole()).isEqualTo(Role.ADMIN);
  }

  @Test
  public void multipleCustomersAndSitesTest() throws Exception {
    // Tests ordering of customers and sites list
    SpireUserRoles sur = buildSpireUserRoles(
        buildCustomerAdmin("SAR123"),
        buildCustomerAdmin("SAR456"),
        buildSiteAdmin("SITE123"),
        buildSiteAdmin("SITE456"));

    UserPrivilegesView userPrivs = SpireUserRolesAdapter.adapt(sur);
    assertThat(userPrivs.getCustomers().size()).isEqualTo(2);
    assertThat(userPrivs.getSites().size()).isEqualTo(2);

    CustomerView customer = userPrivs.getCustomers().get(0);
    assertThat(customer.getCustomerId()).isEqualTo("SAR123");
    assertThat(customer.getRole()).isEqualTo(Role.ADMIN);

    customer = userPrivs.getCustomers().get(1);
    assertThat(customer.getCustomerId()).isEqualTo("SAR456");
    assertThat(customer.getRole()).isEqualTo(Role.ADMIN);

    SiteView site = userPrivs.getSites().get(0);
    assertThat(site.getSiteId()).isEqualTo("SITE123");
    assertThat(site.getRole()).isEqualTo(Role.ADMIN);

    site = userPrivs.getSites().get(1);
    assertThat(site.getSiteId()).isEqualTo("SITE456");
    assertThat(site.getRole()).isEqualTo(Role.ADMIN);
  }

  @Test
  public void multipleCustomersAndSitesReversedTest() throws Exception {
    // Tests ordering of customers and sites list
    SpireUserRoles sur = buildSpireUserRoles(
        buildSiteAdmin("SITE456"),
        buildSiteAdmin("SITE123"),
        buildCustomerAdmin("SAR456"),
        buildCustomerAdmin("SAR123"));

    UserPrivilegesView userPrivs = SpireUserRolesAdapter.adapt(sur);
    assertThat(userPrivs.getCustomers().size()).isEqualTo(2);
    assertThat(userPrivs.getSites().size()).isEqualTo(2);

    CustomerView customer = userPrivs.getCustomers().get(0);
    assertThat(customer.getCustomerId()).isEqualTo("SAR123");
    assertThat(customer.getRole()).isEqualTo(Role.ADMIN);

    customer = userPrivs.getCustomers().get(1);
    assertThat(customer.getCustomerId()).isEqualTo("SAR456");
    assertThat(customer.getRole()).isEqualTo(Role.ADMIN);

    SiteView site = userPrivs.getSites().get(0);
    assertThat(site.getSiteId()).isEqualTo("SITE123");
    assertThat(site.getRole()).isEqualTo(Role.ADMIN);

    site = userPrivs.getSites().get(1);
    assertThat(site.getSiteId()).isEqualTo("SITE456");
    assertThat(site.getRole()).isEqualTo(Role.ADMIN);
  }

  @Test
  public void customerRoleAdminPriorityTest() throws Exception {
    SpireUserRoles sur = buildSpireUserRoles(
        buildCustomerAdmin("SAR123"),
        buildCustomerSubmitter("SAR123"),
        buildCustomerPreparer("SAR123"));

    UserPrivilegesView userPrivs = SpireUserRolesAdapter.adapt(sur);
    assertThat(userPrivs.getCustomers().size()).isEqualTo(1);
    assertThat(userPrivs.getSites().isEmpty()).isTrue();

    CustomerView customer = userPrivs.getCustomers().get(0);
    assertThat(customer.getCustomerId()).isEqualTo("SAR123");
    assertThat(customer.getRole()).isEqualTo(Role.ADMIN);
  }

  @Test
  public void customerRoleAdminPriorityReversedTest() throws Exception {
    SpireUserRoles sur = buildSpireUserRoles(
        buildCustomerPreparer("SAR123"),
        buildCustomerSubmitter("SAR123"),
        buildCustomerAdmin("SAR123"));

    UserPrivilegesView userPrivs = SpireUserRolesAdapter.adapt(sur);
    assertThat(userPrivs.getCustomers().size()).isEqualTo(1);
    assertThat(userPrivs.getSites().isEmpty()).isTrue();

    CustomerView customer = userPrivs.getCustomers().get(0);
    assertThat(customer.getCustomerId()).isEqualTo("SAR123");
    assertThat(customer.getRole()).isEqualTo(Role.ADMIN);
  }

  @Test
  public void siteRoleAdminPriorityTest() throws Exception {
    SpireUserRoles sur = buildSpireUserRoles(
        buildSiteAdmin("SITE123"),
        buildSiteSubmitter("SITE123"),
        buildSitePreparer("SITE123"));

    UserPrivilegesView userPrivs = SpireUserRolesAdapter.adapt(sur);
    assertThat(userPrivs.getSites().size()).isEqualTo(1);
    assertThat(userPrivs.getCustomers().isEmpty()).isTrue();

    SiteView site = userPrivs.getSites().get(0);
    assertThat(site.getSiteId()).isEqualTo("SITE123");
    assertThat(site.getRole()).isEqualTo(Role.ADMIN);
  }

  @Test
  public void siteRoleAdminPriorityReversedTest() throws Exception {
    SpireUserRoles sur = buildSpireUserRoles(
        buildSitePreparer("SITE123"),
        buildSiteSubmitter("SITE123"),
        buildSiteAdmin("SITE123"));

    UserPrivilegesView userPrivs = SpireUserRolesAdapter.adapt(sur);
    assertThat(userPrivs.getSites().size()).isEqualTo(1);
    assertThat(userPrivs.getCustomers().isEmpty()).isTrue();

    SiteView site = userPrivs.getSites().get(0);
    assertThat(site.getSiteId()).isEqualTo("SITE123");
    assertThat(site.getRole()).isEqualTo(Role.ADMIN);
  }

  @Test
  public void customerRoleSubmitterPriorityTest() throws Exception {
    SpireUserRoles sur = buildSpireUserRoles(
        buildCustomerSubmitter("SAR123"),
        buildCustomerPreparer("SAR123"));

    UserPrivilegesView userPrivs = SpireUserRolesAdapter.adapt(sur);
    assertThat(userPrivs.getCustomers().size()).isEqualTo(1);
    assertThat(userPrivs.getSites().isEmpty()).isTrue();

    CustomerView customer = userPrivs.getCustomers().get(0);
    assertThat(customer.getCustomerId()).isEqualTo("SAR123");
    assertThat(customer.getRole()).isEqualTo(Role.SUBMITTER);
  }

  @Test
  public void customerRoleSubmitterPriorityReversedTest() throws Exception {
    SpireUserRoles sur = buildSpireUserRoles(
        buildCustomerPreparer("SAR123"),
        buildCustomerSubmitter("SAR123"));

    UserPrivilegesView userPrivs = SpireUserRolesAdapter.adapt(sur);
    assertThat(userPrivs.getCustomers().size()).isEqualTo(1);
    assertThat(userPrivs.getSites().isEmpty()).isTrue();

    CustomerView customer = userPrivs.getCustomers().get(0);
    assertThat(customer.getCustomerId()).isEqualTo("SAR123");
    assertThat(customer.getRole()).isEqualTo(Role.SUBMITTER);
  }

  @Test
  public void siteRoleSubmitterPriorityTest() throws Exception {
    SpireUserRoles sur = buildSpireUserRoles(
        buildSiteSubmitter("SITE123"),
        buildSitePreparer("SITE123"));

    UserPrivilegesView userPrivs = SpireUserRolesAdapter.adapt(sur);
    assertThat(userPrivs.getSites().size()).isEqualTo(1);
    assertThat(userPrivs.getCustomers().isEmpty()).isTrue();

    SiteView site = userPrivs.getSites().get(0);
    assertThat(site.getSiteId()).isEqualTo("SITE123");
    assertThat(site.getRole()).isEqualTo(Role.SUBMITTER);
  }

  @Test
  public void siteRoleAdminSubmitterReversedTest() throws Exception {
    SpireUserRoles sur = buildSpireUserRoles(
        buildSitePreparer("SITE123"),
        buildSiteSubmitter("SITE123"));

    UserPrivilegesView userPrivs = SpireUserRolesAdapter.adapt(sur);
    assertThat(userPrivs.getSites().size()).isEqualTo(1);
    assertThat(userPrivs.getCustomers().isEmpty()).isTrue();

    SiteView site = userPrivs.getSites().get(0);
    assertThat(site.getSiteId()).isEqualTo("SITE123");
    assertThat(site.getRole()).isEqualTo(Role.SUBMITTER);
  }

  @Test
  public void customerIgnoreDuplicateAdminTest() throws Exception {
    SpireUserRoles sur = buildSpireUserRoles(
        buildCustomerAdmin("SAR123"),
        buildCustomerAdmin("SAR123"));

    UserPrivilegesView userPrivs = SpireUserRolesAdapter.adapt(sur);
    assertThat(userPrivs.getCustomers().size()).isEqualTo(1);
    assertThat(userPrivs.getSites().isEmpty()).isTrue();

    CustomerView customer = userPrivs.getCustomers().get(0);
    assertThat(customer.getCustomerId()).isEqualTo("SAR123");
    assertThat(customer.getRole()).isEqualTo(Role.ADMIN);
  }

  @Test
  public void siteIgnoreDuplicateAdminTest() throws Exception {
    SpireUserRoles sur = buildSpireUserRoles(
        buildSiteAdmin("SITE123"),
        buildSiteAdmin("SITE123"));

    UserPrivilegesView userPrivs = SpireUserRolesAdapter.adapt(sur);
    assertThat(userPrivs.getSites().size()).isEqualTo(1);
    assertThat(userPrivs.getCustomers().isEmpty()).isTrue();

    SiteView site = userPrivs.getSites().get(0);
    assertThat(site.getSiteId()).isEqualTo("SITE123");
    assertThat(site.getRole()).isEqualTo(Role.ADMIN);
  }

  @Test
  public void customerIgnoreDuplicateSubmitterTest() throws Exception {
    SpireUserRoles sur = buildSpireUserRoles(
        buildCustomerSubmitter("SAR123"),
        buildCustomerSubmitter("SAR123"));

    UserPrivilegesView userPrivs = SpireUserRolesAdapter.adapt(sur);
    assertThat(userPrivs.getCustomers().size()).isEqualTo(1);
    assertThat(userPrivs.getSites().isEmpty()).isTrue();

    CustomerView customer = userPrivs.getCustomers().get(0);
    assertThat(customer.getCustomerId()).isEqualTo("SAR123");
    assertThat(customer.getRole()).isEqualTo(Role.SUBMITTER);
  }

  @Test
  public void siteIgnoreDuplicateSubmitterTest() throws Exception {
    SpireUserRoles sur = buildSpireUserRoles(
        buildSiteSubmitter("SITE123"),
        buildSiteSubmitter("SITE123"));

    UserPrivilegesView userPrivs = SpireUserRolesAdapter.adapt(sur);
    assertThat(userPrivs.getSites().size()).isEqualTo(1);
    assertThat(userPrivs.getCustomers().isEmpty()).isTrue();

    SiteView site = userPrivs.getSites().get(0);
    assertThat(site.getSiteId()).isEqualTo("SITE123");
    assertThat(site.getRole()).isEqualTo(Role.SUBMITTER);
  }

  @Test
  public void customerIgnoreDuplicatePreparerTest() throws Exception {
    SpireUserRoles sur = buildSpireUserRoles(
        buildCustomerPreparer("SAR123"),
        buildCustomerPreparer("SAR123"));

    UserPrivilegesView userPrivs = SpireUserRolesAdapter.adapt(sur);
    assertThat(userPrivs.getCustomers().size()).isEqualTo(1);
    assertThat(userPrivs.getSites().isEmpty()).isTrue();

    CustomerView customer = userPrivs.getCustomers().get(0);
    assertThat(customer.getCustomerId()).isEqualTo("SAR123");
    assertThat(customer.getRole()).isEqualTo(Role.PREPARER);
  }

  @Test
  public void siteIgnoreDuplicatePreparerTest() throws Exception {
    SpireUserRoles sur = buildSpireUserRoles(
        buildSitePreparer("SITE123"),
        buildSitePreparer("SITE123"));

    UserPrivilegesView userPrivs = SpireUserRolesAdapter.adapt(sur);
    assertThat(userPrivs.getSites().size()).isEqualTo(1);
    assertThat(userPrivs.getCustomers().isEmpty()).isTrue();

    SiteView site = userPrivs.getSites().get(0);
    assertThat(site.getSiteId()).isEqualTo("SITE123");
    assertThat(site.getRole()).isEqualTo(Role.PREPARER);
  }

  @Test
  public void ignoreUnusedOrEmptyRolesTest() throws Exception {
    SpireUserRoles sur = buildSpireUserRoles(
        SpireUserRole.builder()
            .setResType(RES_TYPE_SPIRE_SAR_USERS)
            .setSarRef("SAR123")
            .setRoleName("SOME_OTHER_ROLE")
            .setIsAdmin("Y")
            .setIsApplicant("N")
            .setFullName(FULL_NAME)
            .build(),
        SpireUserRole.builder()
            .setResType(RES_TYPE_SPIRE_SAR_USERS)
            .setSarRef("SAR123")
            .setRoleName("")
            .setIsAdmin("Y")
            .setIsApplicant("N")
            .setFullName(FULL_NAME)
            .build(),
        SpireUserRole.builder()
            .setResType(RES_TYPE_SPIRE_SAR_USERS)
            .setSarRef("SAR123")
            .setRoleName(null)
            .setIsAdmin("Y")
            .setIsApplicant("N")
            .setFullName(FULL_NAME)
            .build(),
        SpireUserRole.builder()
            .setResType(RES_TYPE_SPIRE_SITE_USERS)
            .setSiteRef("SITE123")
            .setRoleName("SOME_OTHER_ROLE")
            .setIsAdmin("Y")
            .setIsApplicant("N")
            .setFullName(FULL_NAME)
            .build(),
        SpireUserRole.builder()
            .setResType(RES_TYPE_SPIRE_SITE_USERS)
            .setSiteRef("SITE123")
            .setRoleName("")
            .setIsAdmin("Y")
            .setIsApplicant("N")
            .setFullName(FULL_NAME)
            .build(),
        SpireUserRole.builder()
            .setResType(RES_TYPE_SPIRE_SITE_USERS)
            .setSiteRef("SITE123")
            .setRoleName(null)
            .setIsAdmin("Y")
            .setIsApplicant("N")
            .setFullName(FULL_NAME)
            .build()
        );

    UserPrivilegesView userPrivs = SpireUserRolesAdapter.adapt(sur);
    assertThat(userPrivs.getCustomers().isEmpty()).isTrue();
    assertThat(userPrivs.getSites().isEmpty()).isTrue();
  }

  @Test
  public void ignoreEmptySarRefs() throws Exception {
    SpireUserRoles sur = buildSpireUserRoles(
        buildCustomerAdmin(""),
        buildCustomerAdmin(null));

    UserPrivilegesView userPrivs = SpireUserRolesAdapter.adapt(sur);
    assertThat(userPrivs.getCustomers().isEmpty()).isTrue();
    assertThat(userPrivs.getSites().isEmpty()).isTrue();
  }

  @Test
  public void ignoreEmptySiteRefs() throws Exception {
    SpireUserRoles sur = buildSpireUserRoles(
        buildSiteAdmin(""),
        buildSiteAdmin(null));

    UserPrivilegesView userPrivs = SpireUserRolesAdapter.adapt(sur);
    assertThat(userPrivs.getCustomers().isEmpty()).isTrue();
    assertThat(userPrivs.getSites().isEmpty()).isTrue();
  }

  @Test
  public void ignoreEmptyResType() throws Exception {
    SpireUserRoles sur = buildSpireUserRoles(
        SpireUserRole.builder()
            .setResType("")
            .setSarRef("SAR123")
            .setRoleName(ROLE_SAR_ADMINISTRATOR)
            .setIsAdmin("Y")
            .setIsApplicant("N")
            .setFullName(FULL_NAME)
            .build(),
        SpireUserRole.builder()
            .setResType("")
            .setSiteRef("SITE123")
            .setRoleName(ROLE_SITE_ADMINISTRATOR)
            .setIsAdmin("Y")
            .setIsApplicant("N")
            .setFullName(FULL_NAME)
            .build(),
        SpireUserRole.builder()
            .setResType(null)
            .setSarRef("SAR123")
            .setRoleName(ROLE_SAR_ADMINISTRATOR)
            .setIsAdmin("Y")
            .setIsApplicant("N")
            .setFullName(FULL_NAME)
            .build(),
        SpireUserRole.builder()
            .setResType(null)
            .setSiteRef("SITE123")
            .setRoleName(ROLE_SITE_ADMINISTRATOR)
            .setIsAdmin("Y")
            .setIsApplicant("N")
            .setFullName(FULL_NAME)
            .build()
    );

    UserPrivilegesView userPrivs = SpireUserRolesAdapter.adapt(sur);
    assertThat(userPrivs.getCustomers().isEmpty()).isTrue();
    assertThat(userPrivs.getSites().isEmpty()).isTrue();
  }

  @Test
  public void tolerantOfUnusedFieldTest() throws Exception {
    SpireUserRoles sur = buildSpireUserRoles(
        SpireUserRole.builder()
            .setResType(RES_TYPE_SPIRE_SAR_USERS)
            .setSarRef("SAR123")
            .setRoleName(ROLE_SAR_ADMINISTRATOR)
            .build(),
        SpireUserRole.builder()
            .setResType(RES_TYPE_SPIRE_SITE_USERS)
            .setSiteRef("SITE123")
            .setRoleName(ROLE_SITE_ADMINISTRATOR)
            .build()
        );

    UserPrivilegesView userPrivs = SpireUserRolesAdapter.adapt(sur);
    assertThat(userPrivs.getCustomers().size()).isEqualTo(1);
    assertThat(userPrivs.getSites().size()).isEqualTo(1);
  }
}