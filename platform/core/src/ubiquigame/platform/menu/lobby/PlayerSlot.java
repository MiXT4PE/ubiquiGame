package ubiquigame.platform.menu.lobby;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.Pixmap;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Batch;
import com.badlogic.gdx.scenes.scene2d.ui.*;
import com.badlogic.gdx.utils.Align;
import ubiquigame.common.constants.PlayerIdentifier;
import ubiquigame.platform.debug.DebugFakePlayer;
import ubiquigame.platform.helpers.ColorUtil;
import ubiquigame.platform.helpers.Listener;
import ubiquigame.platform.session.ConnectionSession;
import ubiquigame.platform.session.GameSession;
import ubiquigame.platform.session.player.PlayerInternal;

import static ubiquigame.platform.menu.AbstractMenuScreen.localize;

public class PlayerSlot extends Table {

	private final ConnectionSession session;
	private Image avatarImage;
	private Texture texture;

	private Label nameLabel;
	private Label connectLabel;
	private Image checkImage;

	private Stack stack;

	private final int slotId;
	private final PlayerIdentifier playerIdentfier;

	public PlayerSlot(Skin skin, int slotId) {
		super();
		ConnectionSession session = GameSession.getCurrent().getConnectionSession();

		this.slotId = slotId;
		this.session = session;
		this.playerIdentfier = PlayerIdentifier.values()[slotId];

		this.texture = this.getColoredTexture(playerIdentfier.color);

		this.checkImage = new Image(new Texture("checkmark.png"));
		this.avatarImage = new Image();
		nameLabel = new Label("", skin);
		nameLabel.setStyle(new Label.LabelStyle(nameLabel.getStyle()));
		nameLabel.getStyle().background = new Image(ColorUtil.getSolidColor(new Color(0,0,0,0.5f))).getDrawable();
		connectLabel = new Label(localize("connect"), skin);

		this.stack = new Stack();

		this.stack.add(new Container<>(connectLabel));
		this.stack.add(new Container<>(avatarImage).width(Value.percentWidth(.6f, this))
				.height(Value.percentWidth(.6f, this)));
		this.stack.add(
				new Container<>(checkImage).width(Value.percentWidth(.2f, this)).height(Value.percentWidth(.2f, this)));
		this.stack.add(new Container<>(nameLabel).align(Align.bottom));

		this.add(new Container<>(this.stack));


		this.addListener(Listener.onClicked(event -> {
			this.onClick();
		}));

	}

	public void onClick() {
		if (!session.isPlayerConnected(slotId)) {
			session.addPlayer(new DebugFakePlayer(session.getPlayerCount()));
		} else if (session.isPlayerConnected(slotId) && !session.getPlayer(slotId).isReady()) {
			session.getPlayer(slotId).setReady(true);
		} else if (session.isPlayerConnected(slotId) && session.getPlayer(slotId).isReady()) {
			session.getPlayer(slotId).setReady(false);
		}
	}

	@Override
	public void draw(Batch batch, float parentAlpha) {
		Color tmp = batch.getColor();
		if (!GameSession.getCurrent().getConnectionSession().isPlayerConnected(slotId)) {
			batch.setColor(1, 1, 1, 0.3f);
		}
		batch.draw(texture, this.getX(), this.getY(), this.getWidth(), this.getHeight());
		batch.setColor(tmp);

		super.draw(batch, parentAlpha);
	}

	private Texture getColoredTexture(java.awt.Color color) {

		Color colorGdx = new Color(color.getRed()/255f, color.getGreen()/255f, color.getBlue()/255f, color.getAlpha()/255f);
		return getColoredTexture(colorGdx);
	}

	private Texture getColoredTexture(Color color) {
		Pixmap pixmap = new Pixmap(Gdx.graphics.getWidth() / 4, Gdx.graphics.getWidth() / 4, Pixmap.Format.RGBA8888);
		pixmap.setColor(color);
		pixmap.fillCircle(pixmap.getWidth() / 2, pixmap.getHeight() / 2, pixmap.getWidth() / 2);
		return new Texture(pixmap);
	}

	@Override
	public void act(float delta) {
		super.act(delta);
		ConnectionSession session = GameSession.getCurrent().getConnectionSession();
		if (session.isPlayerConnected(slotId)) {
			PlayerInternal player = session.getPlayer(slotId);
			this.nameLabel.setText(player.getName());
			this.connectLabel.setVisible(false);
			this.checkImage.setVisible(player.isReady());
			if(session.getPlayer(slotId).getAvatar() != null){
				this.avatarImage.setDrawable(session.getPlayer(slotId).getAvatar().getDrawable());
			}else{
				this.avatarImage.setDrawable(new Image(new Texture(Gdx.files.internal("avatars/avatar_player1.png"))).getDrawable());
			}

		} else {
			this.nameLabel.setText("");
			this.connectLabel.setVisible(true);
			this.checkImage.setVisible(false);
		}
	}
}
