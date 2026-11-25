package com.timstanford.bookmarkservice;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

class BookmarkServiceApplicationPasswordTests {

    @Test
    void generatePasswordIncludesAllCharacterTypesAndHasExpectedLength() {
        String password = BookmarkServiceApplication.generatePassword();

        assertEquals(16, password.length());
        assertTrue(password.chars().anyMatch(Character::isLowerCase));
        assertTrue(password.chars().anyMatch(Character::isUpperCase));
        assertTrue(password.chars().anyMatch(Character::isDigit));
        assertTrue(password.chars().anyMatch(character -> "!@#$%^&*()-_=+[]{};:,.?".indexOf(character) >= 0));
    }
}
