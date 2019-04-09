package ubiquigame.platform.menu;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.InputMultiplexer;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.ui.Cell;
import com.badlogic.gdx.scenes.scene2d.ui.Label;
import com.badlogic.gdx.scenes.scene2d.ui.Label.LabelStyle;
import com.badlogic.gdx.scenes.scene2d.utils.TextureRegionDrawable;
import com.badlogic.gdx.utils.Align;
import com.badlogic.gdx.scenes.scene2d.ui.Skin;
import com.badlogic.gdx.scenes.scene2d.ui.Table;
import com.badlogic.gdx.scenes.scene2d.ui.TextButton;
import com.badlogic.gdx.scenes.scene2d.ui.Value;
import com.badlogic.gdx.scenes.scene2d.ui.VerticalGroup;

import ubiquigame.platform.FontSuite;
import ubiquigame.platform.MenuManager;
import ubiquigame.platform.PlatformImpl;
import ubiquigame.platform.config.PlatformConfiguration;
import ubiquigame.platform.helpers.ColorUtil;
import ubiquigame.platform.helpers.Listener;
import ubiquigame.platform.helpers.StyleUtils;

/**
 * Contains a stage and a rootTable. You can overload isDebug to enable debug
 * mode for the rootTable
 */
public abstract class AbstractMenuScreen implements Screen {

	protected final Stage stage;
	protected final Table rootTable;

	protected static final Color BG = Color.valueOf("13A89Ecc");
	protected static final Color BG_BOX = Color.valueOf("262262cc");

	protected MenuManager menuManager;
	private Table footer;

	protected Skin skin = PlatformImpl.getInstance().getAssets().getSkin();
	protected Texture bgTexture = PlatformImpl.getInstance().getAssets().getMenuBackground();

	public AbstractMenuScreen() {
		menuManager = PlatformImpl.getInstance().getMenuManager();
		stage = new Stage();
		stage.setDebugAll(PlatformConfiguration.DEBUG_MODE);
		rootTable = new Table();
		rootTable.setFillParent(true);
		rootTable.top().left();
		rootTable.setBackground(new TextureRegionDrawable(new TextureRegion(bgTexture)));
		stage.addActor(rootTable);
		stage.addListener(Listener.onKeyDown(Input.Keys.ESCAPE, e -> onEscDown()));
		stage.addListener(Listener.onKeyDown(Input.Keys.F2, e -> toggleDebugMode()));
		
	}

	protected void toggleDebugMode() {
		PlatformConfiguration.DEBUG_MODE = !PlatformConfiguration.DEBUG_MODE;
		stage.setDebugAll(PlatformConfiguration.DEBUG_MODE);
	}

	@Override
	public void show() {
		Gdx.input.setInputProcessor(stage);
	}

	@Override
	public void render(float delta) {
		Gdx.gl.glEnable(GL20.GL_BLEND);
		Gdx.gl.glBlendFunc(GL20.GL_SRC_ALPHA, GL20.GL_ONE_MINUS_SRC_ALPHA);

		this.stage.act(delta);
		this.stage.draw();
	}

	@Override
	public void resize(int width, int height) {
		stage.getViewport().setWorldSize(width, height);
		this.stage.getViewport().update(width, height, true);
	}

	@Override
	public void pause() {
	}

	@Override
	public void resume() {
	}

	@Override
	public void hide() {
		Gdx.input.setInputProcessor(new InputMultiplexer());
	}

	@Override
	public void dispose() {
		this.stage.dispose();
	}

	public Stage getStage() {
		return stage;
	}

	public Table getRootTable() {
		return rootTable;
	}

	public Skin getSkin() {
		return skin;
	}

	public void createHeader(String title) {
		createHeader(title, null);
	}

	public void createHeader(String title, String subTitle) {
		Label titleLabel = new Label(title, skin);

		LabelStyle titleStyle = StyleUtils.createLabelStyleWithFont(titleLabel.getStyle(), FontSuite.OPEN_SANS_64);
		titleLabel.setStyle(titleStyle);
		if (subTitle != null) {
			VerticalGroup vg = new VerticalGroup();

			vg.addActor(titleLabel);
			Label subtitleLabel = new Label(subTitle, skin);
			LabelStyle subtitleStyle = StyleUtils.createLabelStyleWithFont(subtitleLabel.getStyle(),
					FontSuite.OPEN_SANS_32);
			subtitleLabel.setStyle(subtitleStyle);
			vg.addActor(subtitleLabel);
			vg.align(Align.left);
			vg.columnAlign(Align.left);
			createHeader(vg);
		} else {
			createHeader(titleLabel);
		}
	}

	public void createHeader(Actor actor) {
		Table header = new Table();

		header.setBackground(ColorUtil.getSolidColor(BG_BOX));
		Cell<Actor> headerCell = header.add(actor);
		headerCell.minHeight(100).padLeft(Value.percentHeight(.01F, rootTable)).growX();
		rootTable.add(header).expand(true, false).top().left().fill(false).growX();
		rootTable.row();

	}

	public static String localize(String key) {
		return PlatformImpl.getInstance().getLocalization().get(key);
	}

	public static String localize(String key, Object... args) {
		return PlatformImpl.getInstance().getLocalization().format(key, args);
	}

	protected Table createFooter() {
		footer = new Table();
		rootTable.row();
		rootTable.add(footer).growX().bottom().pad(20);
		rootTable.row();
		return footer;
	}

	/**
	 * 
	 * @return the created text button, which has been added to footer's button bar.
	 */
	protected Cell<TextButton> createFooterButton(String labelName, int alignment) {
		TextButton textButton = new TextButton(labelName, skin);
		Cell<TextButton> cell = footer.add(textButton).minWidth(100).align(alignment);
		if (alignment == Align.right) {
			cell.padLeft(10);
		} else {
			cell.padRight(10);
		}
		if (footer.getCells().size == 1)
			cell.expandX();
		return cell;
	}

	protected Table getFooter() {
		return footer;
	}

	protected void onEscDown() {
		if (PlatformConfiguration.DEBUG_MODE)
			menuManager.goBack();
	}

}
