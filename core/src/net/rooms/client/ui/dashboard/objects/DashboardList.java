package net.rooms.client.ui.dashboard.objects;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.ui.*;
import com.badlogic.gdx.utils.Align;

public class DashboardList extends Table {

    private Stage stage;
    private Skin skin;
    private Table mainTable;
    private Table chatTable;
    private TextField inputField;

    public DashboardList() {
        skin = new Skin(Gdx.files.internal("skin-composer\\skin\\skin-composer-ui.json"));
        // Upper panel
        Table upperPanel = new Table();
        Label titleLabel = new Label("Chat Title", skin);
        ImageButton imageButton = new ImageButton(skin);
        upperPanel.add(titleLabel).expandX().left().pad(10);
        upperPanel.add(imageButton).right().pad(10);
        this.add(upperPanel).expandX().fillX().top();
        this.row();

        // Middle panel (chat area with scroll pane)
        chatTable = new Table();
        ScrollPane scrollPane = new ScrollPane(chatTable, skin);
        scrollPane.setFadeScrollBars(false);
        this.add(scrollPane).expand().fill().pad(10);
        this.row();

        // Lower panel (input field)
        Table lowerPanel = new Table();
        inputField = new TextField("", skin);
        inputField.setMessageText("Type a message...");
        inputField.setAlignment(Align.left);
        inputField.setTextFieldListener((textField, c) -> {
            if (c == '\n') {
                addMessage(inputField.getText(), true);
                inputField.setText("");
            }
        });
        lowerPanel.add(inputField).expandX().fillX().pad(10);
        this.add(lowerPanel).expandX().fillX().bottom();
    }

        public void addMessage(String message,boolean isUser) {
            Table messageTable = new Table();
            Label messageLabel = new Label(message, skin);
            TextButton actionButton = new TextButton("Action", skin);

            if (isUser) {
                messageTable.add(messageLabel).expandX().fillX().align(Align.right);
                messageTable.add(actionButton).align(Align.right);
            } else {
                messageTable.add(actionButton).align(Align.left);
                messageTable.add(messageLabel).expandX().fillX().align(Align.left);
            }

            chatTable.row();
            chatTable.add(messageTable).expandX().fillX().pad(5);
        }

}

