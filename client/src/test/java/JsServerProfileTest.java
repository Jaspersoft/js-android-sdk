import com.jaspersoft.android.sdk.client.JsServerProfile;
import org.junit.Test;

import static junit.framework.Assert.*;

/**
 * @author Ivan Gadzhega
 * @version $Id$
 * @since 1.0
 */
public class JsServerProfileTest {

    private final static Long id = 1L;
    private final static String alias = "Test Profile";
    private final static String serverUrl = "http://mobiledemo.jaspersoft.com/jasperserver-pro";
    private final static String organization = "organization";
    private final static String username = "user";
    private final static String password = "password";

    @Test
    public void test_getUsernameWithOrgId() {
        JsServerProfile serverProfile = new JsServerProfile(id, alias, serverUrl, organization, username, password);
        String actualResult = serverProfile.getUsernameWithOrgId();
        assertEquals(username + "|" + organization, actualResult);
    }

    @Test
    public void test_getUsernameWithOrgId_orgNull() {
        JsServerProfile serverProfile = new JsServerProfile(id, alias, serverUrl, null, username, password);
        String actualResult = serverProfile.getUsernameWithOrgId();
        assertEquals(username, actualResult);
    }

    @Test
    public void test_getUsernameWithOrgId_orgEmpty() {
        JsServerProfile serverProfile = new JsServerProfile(id, alias, serverUrl, "", username, password);
        String actualResult = serverProfile.getUsernameWithOrgId();
        assertEquals(username, actualResult);
    }
}
