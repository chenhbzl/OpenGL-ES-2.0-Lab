package com.jayway.gles20.renderer;

import javax.microedition.khronos.egl.EGLConfig;
import javax.microedition.khronos.opengles.GL10;

import com.jayway.gles20.util.Util;

import android.opengl.GLSurfaceView.Renderer;
import android.util.Log;

public abstract class CommonRenderer implements Renderer {

	private boolean mFirstDraw;
	private boolean mSurfaceCreated;
	private int mWidth;
	private int mHeight;
	private long mLastTime;
	private int mFPS;

	public CommonRenderer() {
		mFirstDraw = true;
		mSurfaceCreated = false;
		mWidth = -1;
		mHeight = -1;
		mLastTime = System.currentTimeMillis();
		mFPS = 0;
	}

	@Override
	public void onSurfaceCreated(GL10 notUsed, EGLConfig config) {
		if (Util.DEBUG) {
			Log.i(Util.LOG_TAG, "Surface created.");
		}
		mSurfaceCreated = true;
		mWidth = -1;
		mHeight = -1;
	}

	@Override
	public void onSurfaceChanged(GL10 notUsed, int width, int height) {
		if (width == mWidth && height == mHeight) {
			if (Util.DEBUG) {
				Log.i(Util.LOG_TAG, "Surface changed but already handled.");
			}
			return;
		}
		if (Util.DEBUG) {
			// Android honeycomb has an option to keep the context.
			String msg = "Surface changed width:" + width + " height:" + height;
			if (!mSurfaceCreated) {
				msg += " context lost.";
			} else {
				msg += ".";
			}
			Log.i(Util.LOG_TAG, msg);
		}

		mWidth = width;
		mHeight = height;

		init(mWidth, mHeight, !mSurfaceCreated);
	}

	@Override
	public void onDrawFrame(GL10 notUsed) {
		draw(mFirstDraw);

		if (Util.DEBUG) {
			mFPS++;
			long currentTime = System.currentTimeMillis();
			if (currentTime - mLastTime >= 1000) {
				// Log.d("Floaty", "FPS:" + mFPS);
				mFPS = 0;
				mLastTime = currentTime;
			}
		}

		if (mFirstDraw) {
			mFirstDraw = false;
		}
	}

	public int getFPS() {
		return mFPS;
	}

	public abstract void init(int width, int height, boolean contextLost);

	public abstract void draw(boolean firstDraw);
}