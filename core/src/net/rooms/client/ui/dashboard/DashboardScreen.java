package net.rooms.client.ui.dashboard;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.ui.*;
import com.badlogic.gdx.scenes.scene2d.utils.ClickListener;
import com.badlogic.gdx.scenes.scene2d.utils.TextureRegionDrawable;
import com.badlogic.gdx.utils.viewport.ScreenViewport;
import net.rooms.client.Client;
import net.rooms.client.connection.requests.Room;
import net.rooms.client.ui.dashboard.objects.DashboardList;

import java.util.List;

public class DashboardScreen implements Screen {

    private final Stage stage;
    private final Skin skin;
    private List<Room> roomsList;
    private Client client;
    private final Table roomsTable;
    private final Table sidebarTable;
    private final Table rightPanelTable;
    private Table mainTable;
    private Table imagesTable;

    //TODO check the constructor for the bug when I return to the screen (constructor/show)
    public DashboardScreen(Client client) {
        this.stage = new Stage(new ScreenViewport());
        this.client = client;
        skin = new Skin(Gdx.files.internal("skin-composer\\skin\\skin-composer-ui.json"));
        // Right panel
        rightPanelTable = new Table();
        rightPanelTable.top().left();
        rightPanelTable.setBackground(skin.newDrawable("white", 0.8f, 0.8f, 0.8f, 1));
        // New table for clickable images
        imagesTable = new Table();
        imagesTable.top().left();
        imagesTable.setBackground(skin.newDrawable("white", 0.3f, 0.3f, 0.3f, 1));
        // Add clickable images
        addImageButton(imagesTable, "settings-icon-size_32.png", 0, client);
        addImageButton(imagesTable, "settings-icon-size_32.png", 1, client);
        addImageButton(imagesTable, "settings-icon-size_32.png", 2, client);
        // Left top sidebar
        sidebarTable = new Table();
        sidebarTable.top().left();
        sidebarTable.setBackground(skin.newDrawable("white", 0.2f, 0.2f, 0.2f, 1));
        sidebarTable.add(new Label("Rooms", skin)).pad(10);
        addImageButton(sidebarTable, "settings-icon-size_32.png", 3, client);
        sidebarTable.add(new TextField("Search", skin)).pad(5).fillX();
        sidebarTable.add(new TextButton("ok", skin)).pad(5);
        sidebarTable.row();
        // Create a table for the list of rooms
        roomsTable = new Table();
        roomsTable.top().left();
        // Main table
        mainTable = new Table();
        mainTable.setFillParent(true);

        // adding all the tables together
        DashboardList dashboardList = new DashboardList();
        dashboardList.setBackground(skin.newDrawable("white", 0.8f, 0.8f, 0.8f, 1));
        mainTable.add(imagesTable).width(50).fillY().expandY();
        mainTable.add(sidebarTable).width(300).fillY().fillX();
        //mainTable.add(rightPanelTable).expand().fill();
        mainTable.add(dashboardList).expand().fill();
        stage.addActor(mainTable);

    }

    private void addRoomButton(Table table, Room room) {
        TextButton roomButton = new TextButton(room.title(), skin);
        roomButton.addListener(new ClickListener() {
            @Override
            public void clicked(InputEvent event, float x, float y) {
                rightPanelTable.clear();
                rightPanelTable.add(new Label(room.title(), skin)).pad(10).left();
                rightPanelTable.row();
                rightPanelTable.add(new Label(room.description(), skin)).pad(10).left();
                rightPanelTable.row();
            }
        });
        table.add(roomButton).pad(10).fillX().expandX().left();
        table.row();
    }

    private void addImageButton(Table table, String imagePath, int which, Client client) {
        Texture texture = new Texture(Gdx.files.internal(imagePath));
        ImageButton imageButton = new ImageButton(new TextureRegionDrawable(texture));
        imageButton.addListener(new ClickListener() {
            @Override
            public void clicked(InputEvent event, float x, float y) {
                switch (which) {
                    case 0:
                        client.getScreenManager().login();
                        break;
                    case 3:
                        createWindow();
                        break;
                }
            }
        });
        table.add(imageButton).pad(10).left();
        table.row();
    }

    private void createWindow() {
        Window window = new Window("New Room Customizer", skin);
        window.setSize(400, 400);
        window.setPosition(Gdx.graphics.getWidth() / 2f - window.getWidth() / 2f, Gdx.graphics.getHeight() / 2f - window.getHeight() / 2f);
        TextField titleField = new TextField("", skin);
        titleField.setMessageText("title");
        TextField descriptionField = new TextField("", skin);
        descriptionField.setMessageText("description");
        TextField passwordField = new TextField("", skin);
        passwordField.setMessageText("password");
        TextButton createButton = new TextButton("create", skin);
        passwordField.setDisabled(true);
        CheckBox disableCheckBox = new CheckBox("", skin);
        window.add(titleField).pad(10);
        window.row();
        window.add(descriptionField).pad(10).size(350, 28);
        window.row();
        window.add(passwordField).pad(10);
        window.row();
        window.add(new Label("private", skin));
        window.row();
        window.add(disableCheckBox);
        window.row();
        window.add(createButton).pad(10);


        disableCheckBox.addListener(event -> {
            passwordField.setDisabled(!disableCheckBox.isChecked());
            return false;
        });

        createButton.addListener(new ClickListener() {
            @Override
            public void clicked(InputEvent event, float x, float y) {
                Room room = client.getApiRequests().createRoom(titleField.getText(), descriptionField.getText(), disableCheckBox.isChecked(), passwordField.getText());
                addRoomButton(roomsTable, room);
                roomsTable.row();
                window.remove();
            }
        });

        stage.addActor(window);
    }

    @Override
    public void show() {
        Gdx.input.setInputProcessor(stage);
        roomsList = client.getApiRequests().getRooms();
        for (Room room : roomsList) {
            addRoomButton(roomsTable, room);
            roomsTable.row();
        }
        // Create a scroll pane for the rooms table
        ScrollPane scrollPane = new ScrollPane(roomsTable, skin);
        scrollPane.setFadeScrollBars(false); // Optional: Disable fading scroll bars

        // Add the scroll pane to the sidebar table
        sidebarTable.add(scrollPane).expand().fill();


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
