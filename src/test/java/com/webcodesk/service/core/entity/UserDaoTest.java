package com.webcodesk.service.core.entity;

import com.webcodesk.service.web.WebcodeskServiceApplication;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.annotation.Commit;
import org.springframework.test.context.jdbc.Sql;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.transaction.annotation.Transactional;

import static org.junit.Assert.*;

@RunWith(SpringRunner.class)
@SpringBootTest(classes = WebcodeskServiceApplication.class)
@Transactional(readOnly = false)
public class UserDaoTest {

    private static final Logger log = LoggerFactory.getLogger(UserDao.class);

    @Autowired
    UserDao userDao;

    private String existingTestEmail = "_test@email.com";
    private String existingTestUserName = "_test_user_name";
    private String existingTestPassword = "babagalamaga";
    private String testEmail = "_new_test@email.com";
    private String testFirstName = "John";
    private String testLastName = "Dough";
    private String testPassword = "galamaga";

    @Test
    @Commit
    public void validateNewUserRegistrationFailed() {
        User user = userDao.addNewUserRegistration(testEmail);
        assertNotNull(user);
        if (user != null) {
            try {
                userDao.validateNewUserRegistration(testEmail);
                assertFalse("Registration should not be valid", true);
            } catch (Exception e) {
                assertTrue(true);
            }
        }
    }

    @Test
    public void validateNewUserRegistrationSuccess() {
        try {
            userDao.validateNewUserRegistration(testEmail);
            assertTrue(true);
        } catch (Exception e) {
            assertTrue("Registration should be valid", false);
        }
    }

    @Test
    public void addNewUserRegistration() {
        User user = userDao.addNewUserRegistration(testEmail);
        assertNotNull(user);
        assertEquals(user.getRegistrationEmail(), testEmail);
        assertNotNull(user.getRegistrationRecordId());
    }

    @Test
    public void getNewUserRegistration() {
        User user = userDao.addNewUserRegistration(testEmail);
        User foundRecord = userDao.getNewUserRegistration(user.getRegistrationRecordId());
        assertEquals(testEmail, foundRecord.getRegistrationEmail());
        assertEquals(user.getRegistrationRecordId(), foundRecord.getRegistrationRecordId());
        assertEquals(user.getRegistrationId(), foundRecord.getRegistrationId());
    }

    @Test
    public void createUser() {
    }

    @Test
    public void validateEmailSuccess() {
        try {
            userDao.validateEmail(testEmail);
            assertTrue(true);
        } catch (Exception e) {
            assertTrue(false);
        }
    }

    @Test
    public void validateEmailFailed() {
        try {
            User userRegistration = userDao.addNewUserRegistration(testEmail);
            assertNotNull(userRegistration);
            userDao.createUser(
                    testPassword,
                    testFirstName,
                    testLastName,
                    userRegistration.getRegistrationRecordId()
            );
            try {
                userDao.validateEmail(testEmail);
                assertTrue(false);
            } catch (Exception e) {
                assertTrue(true);
            }
        } catch (Exception e) {
            log.error(e.getMessage());
            assertTrue(false);
        }
    }

    @Test
    public void findUserByEmail() {
        try {
            User userRegistration = userDao.addNewUserRegistration(testEmail);
            assertNotNull(userRegistration);
            userDao.createUser(
                    testPassword,
                    testFirstName,
                    testLastName,
                    userRegistration.getRegistrationRecordId()
            );
            try {
                User foundUser = userDao.findUserByEmail(testEmail);
                assertNotNull(foundUser);
            } catch (Exception e) {
                e.printStackTrace();
                assertTrue(false);
            }
        } catch (Exception e) {
            e.printStackTrace();
//            log.error(e.getMessage());
            assertTrue(false);
        }
    }

    @Test
    public void getUserById() {
    }
}