package com.codepath.apps.tumblrsnap.activities;

import javax.microedition.khronos.egl.EGLConfig;
import javax.microedition.khronos.opengles.GL10;

import android.graphics.Bitmap;
import android.graphics.Color;
import android.media.effect.Effect;
import android.media.effect.EffectContext;
import android.media.effect.EffectFactory;
import android.opengl.GLES20;
import android.opengl.GLSurfaceView;
import android.opengl.GLUtils;
import android.os.Bundle;
import android.support.v4.app.FragmentActivity;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;

import com.codepath.apps.tumblrsnap.GLToolbox;
import com.codepath.apps.tumblrsnap.R;
import com.codepath.apps.tumblrsnap.TextureRenderer;
import com.codepath.apps.tumblrsnap.TumblrClient;
import com.codepath.apps.tumblrsnap.models.User;
import com.codepath.libraries.androidviewhelpers.SimpleProgressDialog;
import com.loopj.android.http.AsyncHttpResponseHandler;

public class PreviewPhotoActivity extends FragmentActivity implements GLSurfaceView.Renderer {
	private Bitmap photoBitmap;
	private GLSurfaceView mEffectView;
	private int[] mTextures = new int[2];
	private EffectContext mEffectContext;
	private Effect mEffect;
	private TextureRenderer mTexRenderer = new TextureRenderer();
	private int mImageWidth;
	private int mImageHeight;
	private boolean mInitialized = false;
	int mCurrentEffect;
	private SimpleProgressDialog dialog;

	public void setCurrentEffect(int effect) {
		mCurrentEffect = effect;
	}

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_preview_photo);

		photoBitmap = getIntent().getParcelableExtra("photo_bitmap");

		/**
		 * Initialize the renderer and tell it to only render when
		 * explicity requested with the RENDERMODE_WHEN_DIRTY option
		 */
		mEffectView = (GLSurfaceView) findViewById(R.id.effectsview);
		mEffectView.setEGLContextClientVersion(2);
		mEffectView.setRenderer(this);
		mEffectView.setRenderMode(GLSurfaceView.RENDERMODE_WHEN_DIRTY);
		mCurrentEffect = R.id.none;
	}

	private void loadTextures() {
		// Generate textures
		GLES20.glGenTextures(2, mTextures, 0);

		// Load input bitmap
		Bitmap bitmap = photoBitmap;
		mImageWidth = bitmap.getWidth();
		mImageHeight = bitmap.getHeight();
		mTexRenderer.updateTextureSize(mImageWidth, mImageHeight);

		// Upload to texture
		GLES20.glBindTexture(GLES20.GL_TEXTURE_2D, mTextures[0]);
		GLUtils.texImage2D(GLES20.GL_TEXTURE_2D, 0, bitmap, 0);

		// Set texture parameters
		GLToolbox.initTexParams();
	}

	private void initEffect() {
		EffectFactory effectFactory = mEffectContext.getFactory();
		if (mEffect != null) {
			mEffect.release();
		}
		/**
		 * Initialize the correct effect based on the selected menu/action item
		 */
		switch (mCurrentEffect) {

		case R.id.none:
			break;

		case R.id.autofix:
			mEffect = effectFactory.createEffect(
					EffectFactory.EFFECT_AUTOFIX);
			mEffect.setParameter("scale", 0.5f);
			break;

		case R.id.bw:
			mEffect = effectFactory.createEffect(
					EffectFactory.EFFECT_BLACKWHITE);
			mEffect.setParameter("black", .1f);
			mEffect.setParameter("white", .7f);
			break;

		case R.id.brightness:
			mEffect = effectFactory.createEffect(
					EffectFactory.EFFECT_BRIGHTNESS);
			mEffect.setParameter("brightness", 2.0f);
			break;

		case R.id.contrast:
			mEffect = effectFactory.createEffect(
					EffectFactory.EFFECT_CONTRAST);
			mEffect.setParameter("contrast", 1.4f);
			break;

		case R.id.crossprocess:
			mEffect = effectFactory.createEffect(
					EffectFactory.EFFECT_CROSSPROCESS);
			break;

		case R.id.documentary:
			mEffect = effectFactory.createEffect(
					EffectFactory.EFFECT_DOCUMENTARY);
			break;

		case R.id.duotone:
			mEffect = effectFactory.createEffect(
					EffectFactory.EFFECT_DUOTONE);
			mEffect.setParameter("first_color", Color.YELLOW);
			mEffect.setParameter("second_color", Color.DKGRAY);
			break;

		case R.id.filllight:
			mEffect = effectFactory.createEffect(
					EffectFactory.EFFECT_FILLLIGHT);
			mEffect.setParameter("strength", .8f);
			break;

		case R.id.fisheye:
			mEffect = effectFactory.createEffect(
					EffectFactory.EFFECT_FISHEYE);
			mEffect.setParameter("scale", .5f);
			break;

		case R.id.flipvert:
			mEffect = effectFactory.createEffect(
					EffectFactory.EFFECT_FLIP);
			mEffect.setParameter("vertical", true);
			break;

		case R.id.fliphor:
			mEffect = effectFactory.createEffect(
					EffectFactory.EFFECT_FLIP);
			mEffect.setParameter("horizontal", true);
			break;

		case R.id.grain:
			mEffect = effectFactory.createEffect(
					EffectFactory.EFFECT_GRAIN);
			mEffect.setParameter("strength", 1.0f);
			break;

		case R.id.grayscale:
			mEffect = effectFactory.createEffect(
					EffectFactory.EFFECT_GRAYSCALE);
			break;

		case R.id.lomoish:
			mEffect = effectFactory.createEffect(
					EffectFactory.EFFECT_LOMOISH);
			break;

		case R.id.negative:
			mEffect = effectFactory.createEffect(
					EffectFactory.EFFECT_NEGATIVE);
			break;

		case R.id.posterize:
			mEffect = effectFactory.createEffect(
					EffectFactory.EFFECT_POSTERIZE);
			break;

		case R.id.rotate:
			mEffect = effectFactory.createEffect(
					EffectFactory.EFFECT_ROTATE);
			mEffect.setParameter("angle", 180);
			break;

		case R.id.saturate:
			mEffect = effectFactory.createEffect(
					EffectFactory.EFFECT_SATURATE);
			mEffect.setParameter("scale", .5f);
			break;

		case R.id.sepia:
			mEffect = effectFactory.createEffect(
					EffectFactory.EFFECT_SEPIA);
			break;

		case R.id.sharpen:
			mEffect = effectFactory.createEffect(
					EffectFactory.EFFECT_SHARPEN);
			break;

		case R.id.temperature:
			mEffect = effectFactory.createEffect(
					EffectFactory.EFFECT_TEMPERATURE);
			mEffect.setParameter("scale", .9f);
			break;

		case R.id.tint:
			mEffect = effectFactory.createEffect(
					EffectFactory.EFFECT_TINT);
			mEffect.setParameter("tint", Color.MAGENTA);
			break;

		case R.id.vignette:
			mEffect = effectFactory.createEffect(
					EffectFactory.EFFECT_VIGNETTE);
			mEffect.setParameter("scale", .5f);
			break;

		default:
			break;

		}
	}

	private void applyEffect() {
		mEffect.apply(mTextures[0], mImageWidth, mImageHeight, mTextures[1]);
	}

	private void renderResult() {
		if (mCurrentEffect != R.id.none) {
			// if no effect is chosen, just render the original bitmap
			mTexRenderer.renderTexture(mTextures[1]);
		}
		else {
			// render the result of applyEffect()
			mTexRenderer.renderTexture(mTextures[0]);
		}
	}

	@Override
	public void onDrawFrame(GL10 gl) {
		if (!mInitialized) {
			//Only need to do this once
			mEffectContext = EffectContext.createWithCurrentGlContext();
			mTexRenderer.init();
			loadTextures();
			mInitialized = true;
		}
		if (mCurrentEffect != R.id.none) {
			//if an effect is chosen initialize it and apply it to the texture
			initEffect();
			applyEffect();
		}
		renderResult();
	}

	@Override
	public void onSurfaceChanged(GL10 gl, int width, int height) {
		if (mTexRenderer != null) {
			mTexRenderer.updateViewSize(width, height);
		}
	}

	@Override
	public void onSurfaceCreated(GL10 gl, EGLConfig config) {
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.preview_photo, menu);
		return true;
	}

	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		int itemId = item.getItemId();
		if (itemId == R.id.more || itemId == R.id.action_save)
			return true;

		setCurrentEffect(itemId);
		mEffectView.requestRender();
		return true;
	}

	public void onSaveButton(MenuItem menuItem) {
		dialog = SimpleProgressDialog.build(this);
		dialog.show();
		
		TumblrClient client = ((TumblrClient) TumblrClient.getInstance(TumblrClient.class, this));
		client.createPhotoPost(User.currentUser().getBlogHostname(), photoBitmap, new AsyncHttpResponseHandler() {
			@Override
			public void onSuccess(int arg0, String arg1) {
				Log.d("DEBUG", String.valueOf(arg0));
				Log.d("DEBUG", arg1);
				dialog.dismiss();
				PreviewPhotoActivity.this.finish();
			}

			@Override
			public void onFailure(Throwable arg0, String arg1) {
				dialog.dismiss();
				Log.d("DEBUG", arg0.toString());
				Log.d("DEBUG", arg1);
			}
		});
	}
}
