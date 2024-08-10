package net.rooms.client.ui.login.objects;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.scenes.scene2d.ui.CheckBox;
import com.badlogic.gdx.scenes.scene2d.ui.Label;
import com.badlogic.gdx.scenes.scene2d.ui.Skin;
import com.badlogic.gdx.scenes.scene2d.ui.Table;
import com.badlogic.gdx.scenes.scene2d.ui.TextButton;
import com.badlogic.gdx.scenes.scene2d.ui.TextField;
import com.badlogic.gdx.scenes.scene2d.utils.ChangeListener;
import net.rooms.client.ui.login.LoginScreen;

class CredentialsInput extends Table {

	private final Label errorMessage;

	public CredentialsInput(LoginScreen screen) {
		Skin skin = new Skin(Gdx.files.internal("skin-composer\\skin\\skin-composer-ui.json"));
		TextField usernameField = new TextField("", skin);
		usernameField.setMessageText("Username");
		TextField passwordField = new TextField("", skin);
		passwordField.setMessageText("Password");
		passwordField.setPasswordMode(true);
		passwordField.setPasswordCharacter('*');
		CheckBox showPasswordCheckBox = new CheckBox("Show Password", skin);
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
				if (screen.getClient().getApiRequests().login(usernameField.getText(), passwordField.getText()))
					screen.getClient().getScreenManager().dashboard();
				else
					errorMessage.setText("username doesn't exists");
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
}
