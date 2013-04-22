package be.wishto;

import android.app.Activity;
import android.os.Bundle;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.webkit.WebViewClient;

public class WishWebView extends Activity {
	/** Called when the activity is first created. */
	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.wishwebview);
		WebView wv = (WebView) findViewById(R.id.webView1);
		wv.setWebViewClient(new Callback());
		WebSettings webSettings = wv.getSettings();
		webSettings.setBuiltInZoomControls(true);
		wv.loadUrl("file:///android_asset/mywish.html");
	}

	private class Callback extends WebViewClient {
		@Override
		public boolean shouldOverrideUrlLoading(WebView view, String url) {
			return (false);
		}
	}

}