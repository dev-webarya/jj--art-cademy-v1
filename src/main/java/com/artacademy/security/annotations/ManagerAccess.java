package com.artacademy.security.annotations;

import org.springframework.security.access.prepost.PreAuthorize;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * Security annotation for endpoints accessible by ADMIN or STORE_MANAGER roles.
 * Use this for product management, inventory, and other store operations.
 */
@Target({ ElementType.METHOD, ElementType.TYPE })
@Retention(RetentionPolicy.RUNTIME)
@PreAuthorize("hasAnyRole('ADMIN', 'STORE_MANAGER')")
public @interface ManagerAccess {
}
