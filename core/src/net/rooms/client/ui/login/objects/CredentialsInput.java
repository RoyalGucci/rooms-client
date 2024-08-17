package net.rooms.client.ui.login.objects;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.InputListener;
import com.badlogic.gdx.scenes.scene2d.ui.CheckBox;
import com.badlogic.gdx.scenes.scene2d.ui.Label;
import com.badlogic.gdx.scenes.scene2d.ui.Skin;
import com.badlogic.gdx.scenes.scene2d.ui.Table;
import com.badlogic.gdx.scenes.scene2d.ui.TextButton;
import com.badlogic.gdx.scenes.scene2d.ui.TextField;
import com.badlogic.gdx.scenes.scene2d.utils.ChangeListener;
import net.rooms.client.ui.login.LoginScreen;

class CredentialsInput extends Table {

	private final LoginScreen screen;
	private final TextField usernameField;
	private final TextField passwordField;
	private final CheckBox showPasswordCheckBox;
	private final Label errorMessage;

	public CredentialsInput(LoginScreen screen) {
		this.screen = screen;
		Skin skin = new Skin(Gdx.files.internal("skin-composer\\skin\\skin-composer-ui.json"));
		CredentialsInputListener credentialsInputListener = new CredentialsInputListener();

		usernameField = new TextField("", skin);
		usernameField.setMessageText("Username");
		usernameField.addListener(credentialsInputListener);

		passwordField = new TextField("", skin);
		passwordField.setMessageText("Password");
		passwordField.setPasswordMode(true);
		passwordField.setPasswordCharacter('*');
		passwordField.addListener(credentialsInputListener);

		showPasswordCheckBox = new CheckBox("Show Password", skin);

		TextButton login = new TextButton("Login", skin);
		TextButton signup = new TextButton("Need to sign up? click me", skin);

		errorMessage = new Label("", skin);
		errorMessage.setColor(1, 0, 0, 1);

		showPasswordCheckBox.addListener(event -> {
			passwordField.setPasswordMode(!showPasswordCheckBox.isChecked());
			return false;
		});

		login.addListener(new ChangeListener() {
			@Override
			public void changed(ChangeEvent event, Actor actor) {
				login();
			}
		});

		signup.addListener(new ChangeListener() {
			@Override
			public void changed(ChangeEvent event, Actor actor) {
				screen.getClient().getScreenManager().signup();
			}
		});

		add(usernameField).uniformX();
		row().pad(10, 0, 10, 0);
		add(passwordField).uniformX();
		row();
		add(showPasswordCheckBox);
		row();
		add(login).uniformX().pad(10);
		row();
		add(signup).uniformX().pad(10).padBottom(20);
		row().padTop(10);
		add(errorMessage).colspan(2);
	}

	private void login() {
		if (screen.getClient().getApiRequests().login(usernameField.getText(), passwordField.getText())) {
			screen.getClient().getScreenManager().dashboard();
			return;
		}
		errorMessage.setText("Invalid credentials");
	}

	public void resetContent() {
		usernameField.setText("");
		passwordField.setText("");
		passwordField.setPasswordMode(true);
		showPasswordCheckBox.setChecked(false);
		errorMessage.setText("");
	}

	private final class CredentialsInputListener extends InputListener {
		@Override
		public boolean keyDown(InputEvent event, int keycode) {
			if (keycode == Input.Keys.ENTER || keycode == Input.Keys.NUMPAD_ENTER) login();
			else if (keycode == Input.Keys.UP) getStage().setKeyboardFocus(usernameField);
			else if (keycode == Input.Keys.DOWN) getStage().setKeyboardFocus(passwordField);
			return true;
		}
	}
}
