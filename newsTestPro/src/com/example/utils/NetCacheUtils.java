package com.example.utils;

import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;

import org.apache.http.HttpConnection;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.AsyncTask;
import android.widget.ImageView;

public class NetCacheUtils {

	class MyTaskBitmap extends AsyncTask<Object, Void, Bitmap> {
		ImageView mBitmap = null;
		String url = null;

		@Override
		protected Bitmap doInBackground(Object... params) {
			url = (String) params[0];
			mBitmap = (ImageView) params[1];
			mBitmap.setTag(url);// 因为gridView 和listview set图片不一样所以通过tag 来区分
			return DownLoadBitmap(url);
		}

		@Override
		protected void onPostExecute(Bitmap result) {
			String TagUrl = (String) mBitmap.getTag();
			if (TagUrl.equals(url)) {
				mBitmap.setImageBitmap(result);
			}
			super.onPostExecute(result);
		}

	}

	private Bitmap DownLoadBitmap(String url) {
		HttpURLConnection Urlconnection = null;
		try {
			Urlconnection = (HttpURLConnection) new URL(url).openConnection();
			Urlconnection.setConnectTimeout(5000);
			Urlconnection.setReadTimeout(5000);
			Urlconnection.setRequestMethod("GET");
			Urlconnection.connect();
			if (Urlconnection.getResponseCode() == 200) {
				InputStream stream = Urlconnection.getInputStream();
				Bitmap bitmap = BitmapFactory.decodeStream(stream);
				return bitmap;
			}

		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} finally {
			Urlconnection.disconnect();

		}
		return null;
	}
}
