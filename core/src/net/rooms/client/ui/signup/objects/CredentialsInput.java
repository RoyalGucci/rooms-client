package net.rooms.client.ui.signup.objects;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.InputListener;
import com.badlogic.gdx.scenes.scene2d.ui.Label;
import com.badlogic.gdx.scenes.scene2d.ui.Skin;
import com.badlogic.gdx.scenes.scene2d.ui.Table;
import com.badlogic.gdx.scenes.scene2d.ui.TextButton;
import com.badlogic.gdx.scenes.scene2d.ui.TextField;
import com.badlogic.gdx.scenes.scene2d.utils.ChangeListener;
import net.rooms.client.ui.signup.SignupScreen;

import java.util.List;

class CredentialsInput extends Table {

	private final SignupScreen screen;
	private final TextField nicknameField;
	private final TextField usernameField;
	private final TextField passwordField;
	private final TextField confirmPasswordField;
	private final Label errorMessage;

	public CredentialsInput(SignupScreen screen) {
		this.screen = screen;
		Skin skin = new Skin(Gdx.files.internal("skin-composer\\skin\\skin-composer-ui.json"));

		nicknameField = new TextField("", skin);
		nicknameField.setMessageText("Nickname");

		usernameField = new TextField("", skin);
		usernameField.setMessageText("Username");

		passwordField = new TextField("", skin);
		passwordField.setMessageText("Password");

		confirmPasswordField = new TextField("", skin);
		confirmPasswordField.setMessageText("Confirm password");

		CredentialsInputListener credentialsInputListener = new CredentialsInputListener();
		nicknameField.addListener(credentialsInputListener);
		usernameField.addListener(credentialsInputListener);
		passwordField.addListener(credentialsInputListener);
		confirmPasswordField.addListener(credentialsInputListener);

		TextButton signup = new TextButton("Signup", skin);
		TextButton login = new TextButton("Already signed up? click me", skin);

		errorMessage = new Label("", skin);
		errorMessage.setColor(1, 0, 0, 1);

		signup.addListener(new ChangeListener() {
			@Override
			public void changed(ChangeEvent event, Actor actor) {
				signup();
			}
		});

		login.addListener(new ChangeListener() {
			@Override
			public void changed(ChangeEvent event, Actor actor) {
				screen.getClient().getScreenManager().login();
			}
		});

		add(nicknameField).uniformX();
		row().pad(10, 0, 10, 0);
		add(usernameField).uniformX();
		row();
		add(passwordField).uniformX();
		row();
		add(confirmPasswordField).uniformX().pad(10, 0, 10, 0);
		row();
		add(signup).uniformX().pad(10).padBottom(20);
		row();
		add(login).uniformX().pad(10);
		row().padTop(10);
		add(errorMessage).colspan(2);
	}

	private void signup() {
		String nickname = nicknameField.getText();
		String username = usernameField.getText();
		String password = passwordField.getText();
		String confirmPassword = confirmPasswordField.getText();
		if (!fieldCheck(nickname, username, password, confirmPassword))
			return;
		if (!screen.getClient().getApiRequests().signup(nickname, username, password)) {
			errorMessage.setText("username already exists");
			return;
		}
		if (!screen.getClient().getApiRequests().login(username, password)) {
			screen.getClient().getScreenManager().login();
			return;
		}
		screen.getClient().getScreenManager().dashboard();
	}

	public void resetContent() {
		nicknameField.setText("");
		usernameField.setText("");
		passwordField.setText("");
		confirmPasswordField.setText("");
		errorMessage.setText("");
	}

	private boolean fieldCheck(String nickname, String username, String password, String password2) {
		if (!password.equals(password2)) {
			errorMessage.setText("Passwords do not match");
			return false;
		}
		if (password.length() < 8 || password.length() > 250) {
			errorMessage.setText("Password must be between 8 and 250 characters");
			return false;
		}
		if (nickname.isEmpty() || username.isEmpty()) {
			errorMessage.setText("All fields must be filled");
			return false;
		}
		return true;
	}

	private final class CredentialsInputListener extends InputListener {

		// Cannot be used before the below text fields are initialized
		List<Actor> fields = List.of(new Actor[]{nicknameField, usernameField, passwordField, confirmPasswordField});

		@Override
		public boolean keyDown(InputEvent event, int keycode) {
			if (keycode == Input.Keys.ENTER || keycode == Input.Keys.NUMPAD_ENTER) {
				signup();
				return true;
			}

			int currentFieldIndex = fields.indexOf(event.getListenerActor());
			if (keycode == Input.Keys.UP && currentFieldIndex > 0)
				getStage().setKeyboardFocus(fields.get(currentFieldIndex - 1));
			else if (keycode == Input.Keys.DOWN && currentFieldIndex < fields.size() - 1)
				getStage().setKeyboardFocus(fields.get(currentFieldIndex + 1));
			return true;
		}
	}
}
