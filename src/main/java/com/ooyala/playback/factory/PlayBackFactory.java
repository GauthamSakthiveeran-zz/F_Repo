package com.ooyala.playback.factory;

import java.lang.reflect.Field;

import com.ooyala.playback.page.*;
import org.openqa.selenium.WebDriver;

import com.ooyala.playback.page.action.AutoplayAction;
import com.ooyala.playback.page.action.ClickDiscoveryButtonAction;
import com.ooyala.playback.page.action.FullScreenAction;
import com.ooyala.playback.page.action.LiveAction;
import com.ooyala.playback.page.action.PauseAction;
import com.ooyala.playback.page.action.PlayAction;
import com.ooyala.playback.page.action.PlayPauseAction;
import com.ooyala.playback.page.action.SeekAction;
import com.ooyala.playback.page.action.StateScreenAction;
import com.relevantcodes.extentreports.ExtentTest;

public class PlayBackFactory {

	private static PlayBackFactory playbackFactory;
	private WebDriver driver;
	private CCValidator ccValidator;
	private DiscoveryValidator discoveryValidator;
	private FullScreenValidator fullScreenValidator;
	private PauseValidator pauseValidator;
	private PlayValidator playValidator;
	private SeekValidator seekValidator;
	private UpNextValidator upNextValidator;
	private PauseAction pauseAction;
	private PlayAction playAction;
	private AutoplayAction autoplay;
	private EventValidator eventValidator;
	private AspectRatioValidator aspectRatioValidator;
	private LiveAction liveAction;
	private ShareTabValidator shareTabValidator;
	private WaterMarkValidator waterMarkValidator;
	private VolumeValidator volumeValidator;
	private PlayPauseAction playPauseAction;
	private ControlBarValidator controlBarValidator;
	private StartScreenValidator startScreenValidator;
	private EndScreenValidator endScreenValidator;
	private ReplayValidator replayValidator;
	private ClickDiscoveryButtonAction clickDiscoveryButtonAction;
	private Bitratevalidator bitratevalidator;
	private SocialScreenValidator socailScreenValidator;
	private ThumbnailValidator thumbnailValidator;
	private FullScreenAction fullScreenAction;
	private SeekAction seekAction;
	private SaasPortValidator saasPortValidator;
	private StateScreenAction stateScreenAction;
	private OverlayValidator overlayValidator;
	private AdSkipButtonValidator adSkipButtonValidator;
	private DifferentElementValidator differentElement;
	private IsAdPlayingValidator isAdPlaying;
	private EncodingValidator encodingValidator;
	private MultiplePlayerValidator multiplePlayerValidator;
	private AdClickThroughValidator adClickThroughValidator;
	private PoddedAdValidator poddedAdValidator;
	private OoyalaAPIValidator ooyalaAPIValidator;
	private PlaylistValidator playlistValidator;
	private AdFrequencyValidator adFrequencyValidator;
	private ThumbnailCarouselValidator thumbnailCarouselValidator;
	private InitalTimeValidator initalTimeValidator;
	private ExtentTest extentTest;
	private SetEmbedCodeValidator setEmbedCodeValidator;
    private ConcurrentStreamValidator concurrentStreamValidator;
	private DRMValidator drmValidator;
	private GeoValidator geoValidator;
	private StreamTypeValidator streamTypeValidator;
	private ErrorDescriptionValidator errorDescriptionValidator;
	private BitmovinTechnologyValidator bitmovinTechnologyValidator;
	private SyndicationRuleValidator syndicationRuleValidator;
	private LiveValidator liveValidator;

	public PlayBackFactory(WebDriver driver, ExtentTest extentTest) {
		this.driver = driver;
		this.extentTest = extentTest;
	}
	
	public SyndicationRuleValidator getSyndicationRuleValidator() {
        if (syndicationRuleValidator == null){
        	syndicationRuleValidator = new SyndicationRuleValidator(driver);
        	syndicationRuleValidator.setExtentTest(extentTest);
        }
        return syndicationRuleValidator;
    }
	
    public ConcurrentStreamValidator getConcurrentStreamValidator() {
        if (concurrentStreamValidator == null){
            concurrentStreamValidator = new ConcurrentStreamValidator(driver);
            concurrentStreamValidator.setExtentTest(extentTest);
        }
        return concurrentStreamValidator;
    }
    
    public BitmovinTechnologyValidator getBitmovinTechnologyValidator() {
        if (bitmovinTechnologyValidator == null){
        	bitmovinTechnologyValidator = new BitmovinTechnologyValidator(driver);
        	bitmovinTechnologyValidator.setExtentTest(extentTest);
        }
        return bitmovinTechnologyValidator;
    }
    
    public ErrorDescriptionValidator getErrorDescriptionValidator() {
        if (errorDescriptionValidator == null){
        	errorDescriptionValidator = new ErrorDescriptionValidator(driver);
        	errorDescriptionValidator.setExtentTest(extentTest);
        }
        return errorDescriptionValidator;
    }

	public InitalTimeValidator getInitalTimeValidator(){
		if (initalTimeValidator == null){
			initalTimeValidator = new InitalTimeValidator(driver);
			initalTimeValidator.setExtentTest(extentTest);
		}
		return initalTimeValidator;
	}

	public StreamTypeValidator getStreamTypeValidator() {
		if (streamTypeValidator == null){
			streamTypeValidator = new StreamTypeValidator(driver);
			streamTypeValidator.setExtentTest(extentTest);
		}
		return streamTypeValidator;
	}

	public GeoValidator getGeoValidator() {
		if (geoValidator == null){
			geoValidator = new GeoValidator(driver);
			geoValidator.setExtentTest(extentTest);
		}
		return geoValidator;
	}
	
	public DRMValidator getDRMValidator(){
		if (drmValidator == null){
			drmValidator = new DRMValidator(driver);
			drmValidator.setExtentTest(extentTest);
		}
		return drmValidator;
	}
	
	public AdFrequencyValidator getAdFrequencyValidator() {
		if (adFrequencyValidator == null){
			adFrequencyValidator = new AdFrequencyValidator(driver);
			adFrequencyValidator.setExtentTest(extentTest);
		}
		return adFrequencyValidator;
	}

	public OoyalaAPIValidator getOoyalaAPIValidator() {
		if (ooyalaAPIValidator == null){
			ooyalaAPIValidator = new OoyalaAPIValidator(driver);
			ooyalaAPIValidator.setExtentTest(extentTest);
		}
		return ooyalaAPIValidator;
	}

	public ThumbnailCarouselValidator getThumbnailCarouselValidator() {
		if (thumbnailCarouselValidator == null){
			thumbnailCarouselValidator = new ThumbnailCarouselValidator(driver);
			thumbnailCarouselValidator.setExtentTest(extentTest);
		}
		return thumbnailCarouselValidator;

	}

	public MultiplePlayerValidator getMultiplePlayerValidator() {
		if (multiplePlayerValidator == null){
			multiplePlayerValidator = new MultiplePlayerValidator(driver);
			multiplePlayerValidator.setExtentTest(extentTest);
		}
		return multiplePlayerValidator;

	}

	public SeekAction getSeekAction() {
		if (seekAction == null){
			seekAction = new SeekAction(driver);
			seekAction.setExtentTest(extentTest);
		}
		return seekAction;

	}

	public PoddedAdValidator getPoddedAdValidator() {
		if (poddedAdValidator == null){
			poddedAdValidator = new PoddedAdValidator(driver);
			poddedAdValidator.setExtentTest(extentTest);
		}
		return poddedAdValidator;
	}

	public AdClickThroughValidator getAdClickThroughValidator() {
		if (adClickThroughValidator == null){
			adClickThroughValidator = new AdClickThroughValidator(driver);
			adClickThroughValidator.setExtentTest(extentTest);
		}
		return adClickThroughValidator;

	}

	public EncodingValidator getEncodingValidator() {
		if (encodingValidator == null){
			encodingValidator = new EncodingValidator(driver);
			encodingValidator.setExtentTest(extentTest);
		}
		return encodingValidator;

	}

	public AdSkipButtonValidator getAdSkipButtonValidator() {
		if (adSkipButtonValidator == null){
			adSkipButtonValidator = new AdSkipButtonValidator(driver);
			adSkipButtonValidator.setExtentTest(extentTest);
		}
		return adSkipButtonValidator;

	}

	public OverlayValidator getOverlayValidator() {
		if (overlayValidator == null){
			overlayValidator = new OverlayValidator(driver);
			overlayValidator.setExtentTest(extentTest);
		}
		return overlayValidator;

	}

	public StateScreenAction getStateScreenAction() {
		if (stateScreenAction == null){
			stateScreenAction = new StateScreenAction(driver);
			stateScreenAction.setExtentTest(extentTest);
		}
		return stateScreenAction;
	}

	public FullScreenAction getFullScreenAction() {
		if (fullScreenAction == null){
			fullScreenAction = new FullScreenAction(driver);
			fullScreenAction.setExtentTest(extentTest);
		}
		return fullScreenAction;
	}

	public PauseAction getPauseAction() {
		if (pauseAction == null){
			pauseAction = new PauseAction(driver);
			pauseAction.setExtentTest(extentTest);
		}
		return pauseAction;
	}

	public PlayAction getPlayAction() {
		if (playAction == null){
			playAction = new PlayAction(driver);
			playAction.setExtentTest(extentTest);
		}
		return playAction;
	}

	public CCValidator getCCValidator() {
		if (ccValidator == null){
			ccValidator = new CCValidator(driver);
			ccValidator.setExtentTest(extentTest);
		}
		return ccValidator;
	}

	public UpNextValidator getUpNextValidator() {
		if (upNextValidator == null){
			upNextValidator = new UpNextValidator(driver);
			upNextValidator.setExtentTest(extentTest);
		}
		return upNextValidator;
	}

	public SeekValidator getSeekValidator() {
		if (seekValidator == null){
			seekValidator = new SeekValidator(driver);
			seekValidator.setExtentTest(extentTest);
		}
		return seekValidator;
	}

	public PlayValidator getPlayValidator() {
		if (playValidator == null){
			playValidator = new PlayValidator(driver);
			playValidator.setExtentTest(extentTest);
		}
		return playValidator;
	}

	public PauseValidator getPauseValidator() {
		if (pauseValidator == null){
			pauseValidator = new PauseValidator(driver);
			pauseValidator.setExtentTest(extentTest);
		}
		return pauseValidator;
	}

	public FullScreenValidator getFullScreenValidator() {
		if (fullScreenValidator == null){
			fullScreenValidator = new FullScreenValidator(driver);
			fullScreenValidator.setExtentTest(extentTest);
		}
		return fullScreenValidator;
	}

	public DiscoveryValidator getDiscoveryValidator() {
		if (discoveryValidator == null){
			discoveryValidator = new DiscoveryValidator(driver);
			discoveryValidator.setExtentTest(extentTest);
		}
		return discoveryValidator;
	}

	public EventValidator getEventValidator() {
		if (eventValidator == null){
			eventValidator = new EventValidator(driver);
			eventValidator.setExtentTest(extentTest);
		}
		return eventValidator;
	}

	public AspectRatioValidator getAspectRatioValidator() {
		if (aspectRatioValidator == null){
			aspectRatioValidator = new AspectRatioValidator(driver);
			aspectRatioValidator.setExtentTest(extentTest);
		}
		return aspectRatioValidator;
	}

	public ShareTabValidator getShareTabValidator() {
		if (shareTabValidator == null){
			shareTabValidator = new ShareTabValidator(driver);
			shareTabValidator.setExtentTest(extentTest);
		}
		return shareTabValidator;
	}

	public VolumeValidator getVolumeValidator() {
		if (volumeValidator == null){
			volumeValidator = new VolumeValidator(driver);
			volumeValidator.setExtentTest(extentTest);
		}
		return volumeValidator;
	}

	public WaterMarkValidator getWaterMarkValidator() {
		if (waterMarkValidator == null){
			waterMarkValidator = new WaterMarkValidator(driver);
			waterMarkValidator.setExtentTest(extentTest);
		}
		return waterMarkValidator;
	}

	public ControlBarValidator getControlBarValidator() {
		if (controlBarValidator == null){
			controlBarValidator = new ControlBarValidator(driver);
			controlBarValidator.setExtentTest(extentTest);
		}
		return controlBarValidator;
	}

	public StartScreenValidator getStartScreenValidator() {
		if (startScreenValidator == null){
			startScreenValidator = new StartScreenValidator(driver);
			startScreenValidator.setExtentTest(extentTest);
		}
		return startScreenValidator;
	}

	public ClickDiscoveryButtonAction getClickDiscoveryButtonAction() {
		if (clickDiscoveryButtonAction == null){
			clickDiscoveryButtonAction = new ClickDiscoveryButtonAction(driver);
			clickDiscoveryButtonAction.setExtentTest(extentTest);
		}
		return clickDiscoveryButtonAction;
	}

	public EndScreenValidator getEndScreenValidator() {
		if (endScreenValidator == null){
			endScreenValidator = new EndScreenValidator(driver);
			endScreenValidator.setExtentTest(extentTest);
		}
		return endScreenValidator;
	}

	public ReplayValidator getReplayValidator() {
		if (replayValidator == null){
			replayValidator = new ReplayValidator(driver);
			replayValidator.setExtentTest(extentTest);
		}
		return replayValidator;
	}

	public Bitratevalidator getBitratevalidator() {
		if (bitratevalidator == null){
			bitratevalidator = new Bitratevalidator(driver);
			bitratevalidator.setExtentTest(extentTest);
		}
		return bitratevalidator;
	}

	public ThumbnailValidator getThumbnailValidator() {
		if (thumbnailValidator == null){
			thumbnailValidator = new ThumbnailValidator(driver);
			thumbnailValidator.setExtentTest(extentTest);
		}
		return thumbnailValidator;
	}

	public SocialScreenValidator getSocailScreenValidator() {
		if (socailScreenValidator == null){
			socailScreenValidator = new SocialScreenValidator(driver);
			socailScreenValidator.setExtentTest(extentTest);
		}
		return socailScreenValidator;
	}

	public PlayPauseAction getPlayPauseAction() {
		if (playPauseAction == null){
			playPauseAction = new PlayPauseAction(driver);
			playPauseAction.setExtentTest(extentTest);
		}
		return playPauseAction;
	}

	public AutoplayAction getAutoplay() {
		if (autoplay == null){
			autoplay = new AutoplayAction(driver);
			autoplay.setExtentTest(extentTest);
		}
		return autoplay;
	}

	public LiveAction getLiveAction() {
		if (liveAction == null){
			liveAction = new LiveAction(driver);
			liveAction.setExtentTest(extentTest);
		}
		return liveAction;
	}


	public SaasPortValidator getSaasPortValidator() {
		if (saasPortValidator == null){
			saasPortValidator = new SaasPortValidator(driver);
			saasPortValidator.setExtentTest(extentTest);
		}
		return saasPortValidator;
	}

	public DifferentElementValidator getDifferentElements() {
		if (differentElement == null){
			differentElement = new DifferentElementValidator(driver);
			differentElement.setExtentTest(extentTest);
		}
		return differentElement;
	}

	public IsAdPlayingValidator isAdPlaying() {
		if (isAdPlaying == null){
			isAdPlaying = new IsAdPlayingValidator(driver);
			isAdPlaying.setExtentTest(extentTest);
		}
		return isAdPlaying;
	}

	public SetEmbedCodeValidator getSetEmbedCodeValidator() {
		if (setEmbedCodeValidator == null){
			setEmbedCodeValidator = new SetEmbedCodeValidator(driver);
			setEmbedCodeValidator.setExtentTest(extentTest);
		}
		return setEmbedCodeValidator;
	}

	public PlaylistValidator PlaylistValidator() {
		if (playlistValidator == null){
			playlistValidator = new PlaylistValidator(driver);
			playlistValidator.setExtentTest(extentTest);
		}
		return playlistValidator;
	}

	public LiveValidator getLiveValidator() {
		if (liveValidator == null){
			liveValidator = new LiveValidator(driver);
			liveValidator.setExtentTest(extentTest);
		}
		return liveValidator;
	}


	public void destroyInstance() {
		playbackFactory = null;
	}

	public WebDriver getDriver() {
		return driver;
	}

	public <T> T getObject(Class<T> validator) throws Exception {

		Field[] fs = this.getClass().getDeclaredFields();
		fs[0].setAccessible(true);
		for (Field property : fs) {
			if (property.getType().isAssignableFrom(validator)) {
				if (property.get(this) == null)
					property.set(this, validator
							.getConstructor(WebDriver.class)
							.newInstance(driver));
				return (T) property.get(this);
			}

		}
		return null;
	}



}
