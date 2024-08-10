package net.rooms.client;

import net.rooms.client.ui.dashboard.DashboardScreen;
import net.rooms.client.ui.login.LoginScreen;
import net.rooms.client.ui.signup.SignupScreen;

public class ScreenManager {

	private final Client client;

	private final LoginScreen login;
	private final SignupScreen signup;
	private final DashboardScreen dashboard;

	public ScreenManager(Client client) {
		this.client = client;

		login = new LoginScreen(client);
		signup = new SignupScreen(client);
		dashboard = new DashboardScreen(client);
	}

	public void dispose() {
		login.dispose();
		signup.dispose();
		dashboard.dispose();
	}

	public void login() {
		client.setScreen(login);
	}

	public void signup() {
		client.setScreen(signup);
	}

	public void dashboard() {
		login.resetContent();
		signup.resetContent();
		client.setScreen(dashboard);
	}
}
