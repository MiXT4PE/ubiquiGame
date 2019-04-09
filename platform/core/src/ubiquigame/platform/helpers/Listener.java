package ubiquigame.platform.helpers;

import java.util.function.Consumer;

import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.scenes.scene2d.Event;
import com.badlogic.gdx.scenes.scene2d.EventListener;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.InputEvent.Type;
import com.badlogic.gdx.scenes.scene2d.InputListener;
import com.badlogic.gdx.scenes.scene2d.utils.ClickListener;
import com.badlogic.gdx.scenes.scene2d.utils.FocusListener;
import com.badlogic.gdx.scenes.scene2d.utils.FocusListener.FocusEvent;

public class Listener {

	public static EventListener on(Type eventType, Consumer<Event> callback) {
		return new EventListener() {
			@Override
			public boolean handle(Event event) {
				callback.accept(event);
				return false;
			}
		};
	}

	public static FocusListener onKeyboardFocusChanged(Consumer<FocusEvent> callback) {
		return new FocusListener() {
			@Override
			public void keyboardFocusChanged(FocusEvent event, Actor actor, boolean focused) {
				callback.accept(event);
			}
		};
	}
	
	public static FocusListener onKeyboardFocused(Consumer<FocusEvent> callback) {
		return new FocusListener() {
			@Override
			public void keyboardFocusChanged(FocusEvent event, Actor actor, boolean focused) {
				if(focused) {					
					callback.accept(event);
				}
			}
		};
	}

	public static FocusListener onScrollFocusChanged(Consumer<FocusEvent> callback) {
		return new FocusListener() {
			@Override
			public void scrollFocusChanged(FocusEvent event, com.badlogic.gdx.scenes.scene2d.Actor actor,
					boolean focused) {
				callback.accept(event);
			}

		};
	}

	public static ClickListener onClicked(Consumer<Event> callback) {
		return new ClickListener() {
			@Override
			public void clicked(InputEvent event, float x, float y) {
				callback.accept(event);
			}
		};
	}

	public static InputListener onKeyDown(int keycode, Consumer<InputEvent> callback) {
		return new InputListener() {
			private final int listenOnKeyCode = keycode;

			@Override
			public boolean keyDown(InputEvent event, int keycode) {
				if (listenOnKeyCode == keycode)
					callback.accept(event);
				return true;
			}
		};
	}
}
