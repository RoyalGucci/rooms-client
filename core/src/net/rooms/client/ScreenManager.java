package net.rooms.client;

import net.rooms.client.ui.dashboard.DashboardScreen;
import net.rooms.client.ui.login.LoginScreen;
import net.rooms.client.ui.search.SearchScreen;
import net.rooms.client.ui.signup.SignupScreen;

public class ScreenManager {

	private final Client client;

	private final LoginScreen login;
	private final SignupScreen signup;
	private final DashboardScreen dashboard;
	private final SearchScreen search;

	public ScreenManager(Client client) {
		this.client = client;

		login = new LoginScreen(client);
		signup = new SignupScreen(client);
		dashboard = new DashboardScreen(client);
		search = new SearchScreen(client);
	}

	public void dispose() {
		login.dispose();
		signup.dispose();
		dashboard.dispose();
		search.dispose();
	}

	public void login() {
		if (dashboard.equals(client.getScreen()) || search.equals(client.getScreen())) {
			dashboard.unloadDashboard();
			search.hide();
		}
		client.setScreen(login);
	}

	public void signup() {
		client.setScreen(signup);
	}

	public void search() {
		client.setScreen(search);
	}

	public void dashboard() {
		if (login.equals(client.getScreen()) || signup.equals(client.getScreen())){
			login.resetContent();
			signup.resetContent();
			dashboard.loadDashboard();
		}
		client.setScreen(dashboard);
	}
}
