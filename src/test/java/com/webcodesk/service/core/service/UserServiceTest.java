package com.webcodesk.service.core.service;

import com.webcodesk.service.core.entity.User;
import com.webcodesk.service.core.entity.UserDao;
import com.webcodesk.service.core.entity.UserLicense;
import com.webcodesk.service.core.model.UserAccountBean;
import com.webcodesk.service.web.WebcodeskServiceApplication;
import com.webcodesk.service.web.security.LicenseTokenUtils;
import org.junit.Ignore;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

import static org.junit.Assert.*;

@RunWith(SpringRunner.class)
@SpringBootTest(classes = WebcodeskServiceApplication.class)
@Transactional(propagation = Propagation.REQUIRED)
public class UserServiceTest {

    public static final PasswordEncoder PASSWORD_ENCODER = new BCryptPasswordEncoder();
    private static final Logger log = LoggerFactory.getLogger(UserService.class);

    private String testEmail = "_new_test@email.com";
    private String testFirstName = "John";
    private String testLastName = "Dough";
    private String testPassword = "galamaga";
    private String testNewPassword = "weiuryeweiuy";

    @Autowired
    UserService userService;

    @Autowired
    LicenseTokenUtils licenseTokenUtils;

    @Ignore
    @Test
    public void addNewRegistration() {
        try {
            userService.addNewRegistration("apustovalov@gmail.com", true);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Ignore
    @Test
    public void addNewRestoring() {
        try {
            userService.addRestoreRegistration("apustovalov@gmail.com", true);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Test
    public void confirmNewRegistration() {
        String recordId = userService.addNewRegistration(testEmail, false);
        userService.confirmNewRegistration(recordId, testPassword, testFirstName, testLastName);
        try {
            userService.addNewRegistration(testEmail, false);
            assertTrue(false);
        } catch (Exception e) {
            assertTrue(true);
        }
    }

    @Test
    public void getUserAmount() {
        Long amount = userService.getUserAmount();
        assertTrue(amount > 0);
    }

    @Test
    public void createDefaultUserProfile() {
        try {
            String recordId = userService.addNewRegistration(testEmail, false);
            userService.confirmNewRegistration(recordId, testPassword, testFirstName, testLastName);
            User foundUser = userService.findUserByEmail(testEmail);
            assertNotNull(foundUser);
            List<UserLicense> foundLicenses = userService.getUserLicensesById(foundUser.getId());
            assertEquals(foundLicenses.size(), 1);
            assertFalse(foundLicenses.get(0).isExpired());
        } catch (Exception e) {
            e.printStackTrace();
            assertTrue(false);
        }
    }

    @Test
    public void restoreAccount() {
        try {
            String recordId = userService.addNewRegistration(testEmail, false);
            userService.confirmNewRegistration(recordId, testPassword, testFirstName, testLastName);
            User foundUser = userService.findUserByEmail(testEmail);
            assertNotNull(foundUser);
//            List<UserLicense> foundLicenses = userService.getUserLicensesById(foundUser.getId());
//            assertEquals(foundLicenses.size(), 1);
//            assertFalse(foundLicenses.get(0).isExpired());

            String restoringRecordId = userService.addRestoreRegistration(testEmail, false);
            userService.confirmRestoringRegistration(restoringRecordId, testNewPassword);

            User newUser = userService.findUserByEmail(testEmail);

            assertTrue(PASSWORD_ENCODER.matches(testNewPassword, newUser.getPassword()));

        } catch (Exception e) {
            e.printStackTrace();
            assertTrue(false);
        }
    }

    @Test
    public void activateTrialLicense() {
        try {
            String recordId = userService.addNewRegistration(testEmail, false);
            userService.confirmNewRegistration(recordId, testPassword, testFirstName, testLastName);
            User foundUser = userService.findUserByEmail(testEmail);
            assertNotNull(foundUser);

            UserLicense userLicense = userService.getAvailableLicense(foundUser.getId());
            assertNotNull(userLicense);

            String token = licenseTokenUtils.generateToken(foundUser, userLicense);
            System.out.println("Token: " + token);

            Long activationId = userService.activateLicenseByAccountLicenseId(userLicense.getAccountLicenseId());
            assertNotNull(activationId);

            List<UserLicense> foundLicenses = userService.getUserLicensesById(foundUser.getId());
            assertEquals(foundLicenses.size(), 1);
            UserLicense firstLicense = foundLicenses.get(0);
            assertEquals(firstLicense.getLicenseActivations().size(), 1);
            assertEquals(firstLicense.getLicenseActivations().get(0).getStatus(), "activated");

            userService.deactivateLicenseByActivationId(activationId);

            List<UserLicense> foundLicenses2 = userService.getUserLicensesById(foundUser.getId());
            assertEquals(foundLicenses2.size(), 1);
            UserLicense firstLicense2 = foundLicenses2.get(0);
            assertEquals(firstLicense2.getLicenseActivations().size(), 1);
            assertEquals(firstLicense2.getLicenseActivations().get(0).getStatus(), "deactivated");

        } catch (Exception e) {
            e.printStackTrace();
            assertTrue(true);
        }
    }
}