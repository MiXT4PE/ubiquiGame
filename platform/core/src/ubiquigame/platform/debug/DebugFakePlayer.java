package ubiquigame.platform.debug;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.scenes.scene2d.ui.Image;

import ubiquigame.platform.session.player.PlayerInternal;

public class DebugFakePlayer extends PlayerInternal {

	public DebugFakePlayer(int id) {
	super("FakePlayer " + id, 
			new DebugController(), 
			new Image(new Texture(Gdx.files.internal("avatars/avatar_player"+ id +".png"))));
	}
}
