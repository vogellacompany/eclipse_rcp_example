package com.vogella.tasks.ui.tests;

/**
 * Utility class to control login behavior for testing.
 * 
 * When running tests, set the system property "skipLogin" to "true" to bypass
 * any login dialogs:
 * 
 * -DskipLogin=true
 * 
 * This can also be used programmatically by setting:
 * System.setProperty("skipLogin", "true");
 */
public class LoginTestHelper {
	
	private static final String SKIP_LOGIN_PROPERTY = "skipLogin";
	
	/**
	 * Check if login should be skipped (for testing purposes).
	 * @return true if the skipLogin system property is set to "true"
	 */
	public static boolean shouldSkipLogin() {
		return "true".equalsIgnoreCase(System.getProperty(SKIP_LOGIN_PROPERTY));
	}
	
	/**
	 * Enable skipping login dialogs for testing.
	 */
	public static void enableSkipLogin() {
		System.setProperty(SKIP_LOGIN_PROPERTY, "true");
	}
	
	/**
	 * Disable skipping login dialogs (restore normal behavior).
	 */
	public static void disableSkipLogin() {
		System.clearProperty(SKIP_LOGIN_PROPERTY);
	}
}
