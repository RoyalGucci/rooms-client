package net.rooms.client.ui;

import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.ui.Table;
import com.badlogic.gdx.scenes.scene2d.utils.ClickListener;

public class ScrollListener extends ClickListener {

	private final Table scrollTable;

	public ScrollListener(Table scrollTable) {
		this.scrollTable = scrollTable;
	}

	@Override
	public void enter(InputEvent event, float x, float y, int pointer, Actor fromActor) {
		event.getStage().setScrollFocus(scrollTable);
	}

	@Override
	public void exit(InputEvent event, float x, float y, int pointer, Actor toActor) {
		event.getStage().setScrollFocus(null);
	}

	@Override
	public boolean mouseMoved(InputEvent event, float x, float y) {
		if (!scrollTable.hasScrollFocus()) event.getStage().setScrollFocus(scrollTable);
		return true;
	}
}
