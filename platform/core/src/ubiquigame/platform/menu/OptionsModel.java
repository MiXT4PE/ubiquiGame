package ubiquigame.platform.menu;

import ubiquigame.common.impl.ScreenDimension;
import ubiquigame.platform.PlatformImpl;
import ubiquigame.platform.config.PlatformConfiguration;
import java.util.Objects;

public class OptionsModel {

	// graphics
	private boolean fullscreen;
	private ScreenDimension resolution;

	// networking
	private int portUDPReceive;
	private int portUDPBroadcast;
	private int portTCPReceive;
	private String platformName;

	// audio
	private int masterVolume;
	private int musicVolume;
	private int soundVolume;

	// language
	private String languageCode;

	@Override
	public boolean equals(final Object other) {
		if (!(other instanceof OptionsModel)) {
			return false;
		}
		OptionsModel castOther = (OptionsModel) other;
		return Objects.equals(fullscreen, castOther.fullscreen) && Objects.equals(resolution, castOther.resolution)
				&& Objects.equals(portUDPReceive, castOther.portUDPReceive)
				&& Objects.equals(portUDPBroadcast, castOther.portUDPBroadcast)
				&& Objects.equals(portTCPReceive, castOther.portTCPReceive)
				&& Objects.equals(platformName, castOther.platformName)
				&& Objects.equals(masterVolume, castOther.masterVolume)
				&& Objects.equals(musicVolume, castOther.musicVolume)
				&& Objects.equals(soundVolume, castOther.soundVolume)
				&& Objects.equals(languageCode, castOther.languageCode);
	}

	@Override
	public int hashCode() {
		return Objects.hash(fullscreen, resolution, portUDPReceive, portUDPBroadcast, portTCPReceive, platformName,
				masterVolume, musicVolume, soundVolume, languageCode);
	}

	public boolean validate() {
		return checkPortRange(portTCPReceive) && checkPortRange(portUDPReceive) && checkPortRange(portUDPBroadcast);
	}

	private boolean checkPortRange(int port) {
		return port < 65536 && port > 0;
	}

	public boolean isFullscreen() {
		return fullscreen;
	}

	public void setFullscreen(boolean fullscreen) {
		this.fullscreen = fullscreen;
	}

	public ScreenDimension getResolution() {
		return resolution;
	}

	public void setResolution(ScreenDimension resolution) {
		this.resolution = resolution;
	}

	public int getPortUDPReceive() {
		return portUDPReceive;
	}

	public void setPortUDPReceive(int portUDPReceive) {
		this.portUDPReceive = portUDPReceive;
	}

	public int getPortUDPBroadcast() {
		return portUDPBroadcast;
	}

	public void setPortUDPBroadcast(int portUDPBroadcast) {
		this.portUDPBroadcast = portUDPBroadcast;
	}

	public int getPortTCPReceive() {
		return portTCPReceive;
	}

	public void setPortTCPReceive(int portTCPReceive) {
		this.portTCPReceive = portTCPReceive;
	}

	public String getPlatformName() {
		return platformName;
	}

	public void setPlatformName(String platformName) {
		this.platformName = platformName;
	}

	public int getMasterVolume() {
		return masterVolume;
	}

	public void setMasterVolume(int masterVolume) {
		this.masterVolume = masterVolume;
	}

	public int getMusicVolume() {
		return musicVolume;
	}

	public void setMusicVolume(int musicVolume) {
		this.musicVolume = musicVolume;
	}

	public int getSoundVolume() {
		return soundVolume;
	}

	public void setSoundVolume(int soundVolume) {
		this.soundVolume = soundVolume;
	}

	public String getLanguageCode() {
		return languageCode;
	}

	public void setLanguageCode(String languageCode) {
		this.languageCode = languageCode;
	}

	public static OptionsModel snapshot() {
		OptionsModel snapshot = new OptionsModel();
		snapshot.setFullscreen(PlatformConfiguration.WINDOW_FULLSCREEN);
		snapshot.setResolution(
				new ScreenDimension(PlatformConfiguration.WINDOW_WIDTH, PlatformConfiguration.WINDOW_HEIGHT));
		snapshot.setLanguageCode(PlatformConfiguration.LANGUAGE);
		snapshot.setMasterVolume(PlatformConfiguration.AUDIO_MASTER_VOLUME);
		snapshot.setMusicVolume(PlatformConfiguration.AUDIO_MUSIC_VOLUME);
		snapshot.setSoundVolume(PlatformConfiguration.AUDIO_SOUND_VOLUME);
		snapshot.setPlatformName(PlatformConfiguration.PLATFORM_NAME);
		snapshot.setPortTCPReceive(PlatformConfiguration.TCP_PORT);
		snapshot.setPortUDPBroadcast(PlatformConfiguration.UDP_BROADCAST_PORT);
		snapshot.setPortUDPReceive(PlatformConfiguration.UDP_PORT);
		return snapshot;
	}
public void apply() {
		PlatformConfiguration.UDP_PORT = getPortUDPReceive();
		PlatformConfiguration.UDP_BROADCAST_PORT = getPortUDPBroadcast();
		PlatformConfiguration.TCP_PORT = getPortTCPReceive();
		PlatformConfiguration.PLATFORM_NAME = getPlatformName();
		PlatformConfiguration.WINDOW_WIDTH = getResolution().getWidth();
		PlatformConfiguration.WINDOW_HEIGHT = getResolution().getHeight();
		PlatformConfiguration.WINDOW_FULLSCREEN = isFullscreen();
		PlatformConfiguration.AUDIO_MASTER_VOLUME = getMasterVolume();
		PlatformConfiguration.AUDIO_MUSIC_VOLUME = getMusicVolume();
		PlatformConfiguration.AUDIO_SOUND_VOLUME = getSoundVolume();
		PlatformConfiguration.LANGUAGE = getLanguageCode();
		PlatformImpl.getInstance().setLocalization(PlatformConfiguration.LANGUAGE);
	}

}
