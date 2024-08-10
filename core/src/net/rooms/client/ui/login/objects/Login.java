package net.rooms.client.ui.login.objects;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.scenes.scene2d.ui.Image;
import com.badlogic.gdx.scenes.scene2d.ui.ScrollPane;
import com.badlogic.gdx.scenes.scene2d.ui.Skin;
import com.badlogic.gdx.scenes.scene2d.ui.Table;
import net.rooms.client.ui.login.LoginScreen;

import static com.badlogic.gdx.scenes.scene2d.actions.Actions.alpha;
import static com.badlogic.gdx.scenes.scene2d.actions.Actions.fadeIn;

public class Login extends Table {

	private final Table scrollTable;
	private final CredentialsInput credentialsInput;

	public Login(LoginScreen screen) {
		Skin skin = new Skin(Gdx.files.internal("skin-composer\\skin\\skin-composer-ui.json"));
		setFillParent(true);
		setBackground(skin.newDrawable("white", 0.7f, 0.7f, 0.7f, 1));

		scrollTable = new Table();
		Image logo = new Image(new Texture(Gdx.files.internal("logo.jpeg")));
		scrollTable.add(logo).size(300, 300).padBottom(20);
		scrollTable.row();
		credentialsInput = new CredentialsInput(screen);
		scrollTable.add(credentialsInput);

		ScrollPane scrollPane = new ScrollPane(scrollTable, skin);
		scrollPane.setFadeScrollBars(false);
		add(scrollPane).expand().fill();
	}

	public void resetContent() {
		credentialsInput.resetContent();
	}

	public void onShow() {
		scrollTable.addAction(alpha(0));
		scrollTable.addAction(fadeIn(1));
	}
}
