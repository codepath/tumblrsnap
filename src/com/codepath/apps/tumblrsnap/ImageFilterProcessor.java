package com.codepath.apps.tumblrsnap;

import android.graphics.Bitmap;
import android.graphics.Bitmap.Config;

import com.jabistudio.androidjhlabs.filter.BlurFilter;
import com.jabistudio.androidjhlabs.filter.ContrastFilter;
import com.jabistudio.androidjhlabs.filter.CrystallizeFilter;
import com.jabistudio.androidjhlabs.filter.GlowFilter;
import com.jabistudio.androidjhlabs.filter.GrayscaleFilter;
import com.jabistudio.androidjhlabs.filter.KaleidoscopeFilter;
import com.jabistudio.androidjhlabs.filter.LevelsFilter;
import com.jabistudio.androidjhlabs.filter.MarbleFilter;
import com.jabistudio.androidjhlabs.filter.PinchFilter;
import com.jabistudio.androidjhlabs.filter.SolarizeFilter;
import com.jabistudio.androidjhlabs.filter.TritoneFilter;
import com.jabistudio.androidjhlabs.filter.util.AndroidUtils;

public class ImageFilterProcessor {
	public static final int NONE = 0;
	public static final int CONTRAST = 1;
	public static final int GRAYSCALE = 2;
	public static final int SOLARIZE = 3;
	public static final int FRACTAL = 4;
	public static final int MARBLE = 5;
	public static final int PINCH = 6;
	public static final int WARP = 7;
	public static final int BLUR = 8;
	public static final int CRYSTALLIZE = 9;
	public static final int TRITONE = 10;
	public static final int GLOW = 11;

	Bitmap originalImage;
	int width;
	int height;

	public ImageFilterProcessor(Bitmap originalImage) {
		this.originalImage = originalImage;
		this.width = originalImage.getWidth();
		this.height = originalImage.getHeight();
	}

	public Bitmap applyFilter(int effectType) {
		int[] src = AndroidUtils.bitmapToIntArray(originalImage);
		int[] dest;
		switch (effectType) {
		case CONTRAST:
			ContrastFilter cFilter = new ContrastFilter();
			dest = cFilter.filter(src, width, height);
			break;
		case GRAYSCALE:
			GrayscaleFilter gFilter = new GrayscaleFilter();
			dest = gFilter.filter(src, width, height);
			break;
		case SOLARIZE:
			SolarizeFilter sFlter = new SolarizeFilter();
			dest = sFlter.filter(src, width, height);
			break;
		case FRACTAL:
			KaleidoscopeFilter kFilter = new KaleidoscopeFilter();
			dest = kFilter.filter(src, width, height);
			break;
		case MARBLE:
			MarbleFilter mFilter = new MarbleFilter();
			dest = mFilter.filter(src, width, height);
			break;
		case PINCH:
			PinchFilter pFilter = new PinchFilter();
			dest = pFilter.filter(src, width, height);
			break;
		case BLUR:
			BlurFilter bFilter = new BlurFilter();
			dest = bFilter.filter(src, width, height);
			break;
		case CRYSTALLIZE:
			CrystallizeFilter clFilter = new CrystallizeFilter();
			dest = clFilter.filter(src, width, height);
			break;
		case TRITONE:
			TritoneFilter ttFilter = new TritoneFilter();
			dest = ttFilter.filter(src, width, height);
			break;
		case GLOW:
			GlowFilter glFilter = new GlowFilter();
			dest = glFilter.filter(src, width, height);
			break;
		default:
			LevelsFilter lFilter = new LevelsFilter();
			dest = lFilter.filter(src, width, height);
			break;
		}
		
		if (effectType != NONE) {
			return Bitmap.createBitmap(dest, width, height, Config.ARGB_8888);
		} else {
			return originalImage;
		}
	}
}
