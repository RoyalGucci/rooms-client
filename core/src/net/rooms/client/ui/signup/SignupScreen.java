package net.rooms.client.ui.signup;

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

public class SignupScreen implements Screen {

    private final Stage stage;
    private final Image logo;
    private Skin skin;
    private final Label errorMessage;

    public SignupScreen(final Client client) {
        this.stage = new Stage(new ScreenViewport());
        skin = new Skin(Gdx.files.internal("skin-composer\\skin\\skin-composer-ui.json"));

        logo = new Image(new Texture(Gdx.files.internal("logo.jpeg")));
        Table table = new Table();
        table.setFillParent(true);
        stage.addActor(logo);
        stage.addActor(table);
        table.setBackground(skin.newDrawable("white", 0.7f, 0.7f, 0.7f, 1));

        // Create UI elements
        TextField nicknameField = new TextField("", skin);
        nicknameField.setMessageText("Nickname");
        TextField usernameField = new TextField("", skin);
        usernameField.setMessageText("Username");
        TextField passwordField = new TextField("", skin);
        passwordField.setMessageText("Password");
        TextField passwordField2 = new TextField("", skin);
        passwordField2.setMessageText("type Password again");
        TextButton loginButton = new TextButton("already signed up? click me", skin);
        TextButton SignupButton = new TextButton("Signup", skin);
        errorMessage = new Label("", skin);
        errorMessage.setColor(1, 0, 0, 1);

        // Add elements to the table
        table.add(logo).size(500, 500).padBottom(20);
        table.row();
        table.add(nicknameField).uniformX();
        table.row().pad(10, 0, 10, 0);
        table.add(usernameField).uniformX();
        table.row();
        table.add(passwordField).uniformX();
        table.row();
        table.add(passwordField2).uniformX().pad(10, 0, 10, 0);
        table.row();
        table.add(SignupButton).uniformX().pad(10).padBottom(20);
        table.row();
        table.add(loginButton).uniformX().pad(10);
        table.row().padTop(10);
        table.add(errorMessage).colspan(2);

        SignupButton.addListener(new ChangeListener() {
            @Override
            public void changed(ChangeEvent event, Actor actor) {
                if (fieldcheck(nicknameField.getText(), usernameField.getText(), passwordField.getText(), passwordField2.getText())) {
                    if (!client.getApiRequests().signup(nicknameField.getText(), usernameField.getText(), passwordField.getText()))
                        errorMessage.setText("username already exists");
                    else
                        client.getScreenManager().login();
                }
            }
        });

        loginButton.addListener(new ChangeListener() {
            @Override
            public void changed(ChangeEvent event, Actor actor) {
                client.getScreenManager().login();
            }
        });
    }

    private boolean fieldcheck(String nickname, String username, String password, String password2) {
        if (!password.equals(password2)) {
            errorMessage.setText("passwords do not match");
            return false;
        }
        if (password.length() < 8 || password.length() > 250) {
            errorMessage.setText("passwords must be between 8 and 250 characters");
            return false;
        }
        if (nickname.isEmpty() || username.isEmpty()) {
            errorMessage.setText("all fields must be filled");
            return false;
        }
        return true;
    }


    @Override
    public void show() {
        Gdx.input.setInputProcessor(stage);
        logo.addAction(alpha(0f));
        logo.addAction(Actions.fadeIn(3f));

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
