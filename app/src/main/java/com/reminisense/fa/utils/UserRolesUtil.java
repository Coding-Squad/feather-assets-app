package com.reminisense.fa.utils;

/**
 * Created by Nigs on 2016-05-25.
 */
public class UserRolesUtil {
    public static final String ROLE_USER = "ROLE_USER";
    public static final String ROLE_ADMIN = "ROLE_ADMIN";
    public static final String ROLE_GUARD = "ROLE_GUARD";

    public static boolean hasRole(String roles, String role) {
        if(roles == null || roles.isEmpty()) return false;
        return roles.contains(role);
    }
}
