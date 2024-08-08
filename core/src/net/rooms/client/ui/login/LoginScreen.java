package net.rooms.client.ui.login;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.actions.Actions;
import com.badlogic.gdx.scenes.scene2d.ui.*;
import com.badlogic.gdx.scenes.scene2d.utils.ChangeListener;
import com.badlogic.gdx.utils.viewport.ScreenViewport;
import net.rooms.client.Client;

import static com.badlogic.gdx.scenes.scene2d.actions.Actions.alpha;

public class LoginScreen implements Screen {

    private final Stage stage;
    private final Image logo;
    private Skin skin;
    private final Label errorMessage;

    public LoginScreen(final Client client) {
        this.stage = new Stage(new ScreenViewport());
        skin = new Skin(Gdx.files.internal("skin-composer\\skin\\skin-composer-ui.json"));

        logo = new Image(new Texture(Gdx.files.internal("logo.jpeg")));
        Table table = new Table();
        table.setFillParent(true);
        stage.addActor(logo);
        stage.addActor(table);
        table.setBackground(skin.newDrawable("white", 0.7f, 0.7f, 0.7f, 1));

        // Create UI elements
        TextField usernameField = new TextField("", skin);
        usernameField.setMessageText("Username");
        TextField passwordField = new TextField("", skin);
        passwordField.setMessageText("Password");
        passwordField.setPasswordMode(true);
        passwordField.setPasswordCharacter('*');
        CheckBox showPasswordCheckBox = new CheckBox("Show Password", skin);
        TextButton loginButton = new TextButton("Login", skin);
        TextButton SignupButton = new TextButton("Need to sign up? click me", skin);
        errorMessage = new Label("", skin);
        errorMessage.setColor(1, 0, 0, 1);

        // Add elements to the table
        table.add(logo).size(500, 500).padBottom(20);
        table.row();
        table.add(usernameField).uniformX();
        table.row().pad(10, 0, 10, 0);
        table.add(passwordField).uniformX();
        table.row();
        table.add(showPasswordCheckBox);
        table.row();
        table.add(loginButton).uniformX().pad(10);
        table.row();
        table.add(SignupButton).uniformX().pad(10).padBottom(20);
        table.row().padTop(10);
        table.add(errorMessage).colspan(2);

        showPasswordCheckBox.addListener(event -> {
            passwordField.setPasswordMode(!showPasswordCheckBox.isChecked());
            return false;
        });

        loginButton.addListener(new ChangeListener() {
            @Override
            public void changed(ChangeEvent event, Actor actor) {
                if (!client.getApiRequests().login(usernameField.getText(), passwordField.getText()))
                    errorMessage.setText("username doesn't exists");
                else
                    client.getScreenManager().dashboard();
            }
        });

        SignupButton.addListener(new ChangeListener() {
            @Override
            public void changed(ChangeEvent event, Actor actor) {
                client.getScreenManager().signup();
            }
        });
    }

    @Override
    public void show() {
        logo.addAction(alpha(0f));
        logo.addAction(Actions.fadeIn(3f));
        Gdx.input.setInputProcessor(stage);
    }

    @Override
    public void render(float delta) {
        Gdx.gl.glClearColor(0.5f, 0.5f, 1, 1);
        Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);
        stage.act(delta);
        stage.draw();
    }

    @Override
    public void resize(int width, int height) {
        stage.getViewport().update(width, height, true);
    }

    @Override
    public void pause() {

    }

    @Override
    public void resume() {

    }

    @Override
    public void hide() {

    }

    @Override
    public void dispose() {
        stage.dispose();
    }
}
