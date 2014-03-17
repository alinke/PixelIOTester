package com.ledpixelart.pixeliotester;

import ioio.lib.api.AnalogInput;
import ioio.lib.api.DigitalInput;
import ioio.lib.api.DigitalOutput;
import ioio.lib.api.IOIO;
import ioio.lib.api.PwmOutput;
import ioio.lib.api.exception.ConnectionLostException;
import ioio.lib.util.BaseIOIOLooper;
import ioio.lib.util.IOIOLooper;
import ioio.lib.util.android.IOIOActivity;
import android.os.Bundle;
import android.widget.SeekBar;
import android.widget.TextView;
import android.widget.ToggleButton;

public class IOIOSimpleApp extends IOIOActivity {
	private TextView textView_;
	private TextView buttonText_;
	private TextView ProxTextView_;
	private TextView AlcoholTextView_;
	private TextView i2cText_;
	private SeekBar seekBar_;
	private ToggleButton toggleButton_;

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.main);

		textView_ = (TextView) findViewById(R.id.TextView);
		buttonText_ = (TextView) findViewById(R.id.buttonText);
		toggleButton_ = (ToggleButton) findViewById(R.id.ToggleButton);
		ProxTextView_ = (TextView) findViewById(R.id.ProxTextView);
		AlcoholTextView_ = (TextView) findViewById(R.id.AlcoholTextView);
		enableUi(false);
	}

	class Looper extends BaseIOIOLooper {
		private AnalogInput input_;
		private AnalogInput ProxInput_;
		private AnalogInput AlcoholInput_;
		private DigitalOutput led_;
		private DigitalInput button_;
		private DigitalInput i2cButton_;
		private boolean buttonValue;

		@Override
		public void setup() throws ConnectionLostException {
			led_ = ioio_.openDigitalOutput(IOIO.LED_PIN, true);
			button_ = ioio_.openDigitalInput(4);
			input_ = ioio_.openAnalogInput(33);
			ProxInput_ = ioio_.openAnalogInput(32);
			AlcoholInput_ = ioio_.openAnalogInput(35);
			enableUi(true);
		}

		@Override
		public void loop() throws ConnectionLostException, InterruptedException {
			setNumber(input_.read());
			setProx(ProxInput_.read());
			setAlcohol(AlcoholInput_.read());
			
			buttonValue = button_.read();
			
			if (buttonValue == true) {
				setButtonText("2. Digital I/O Button is Pressed");
			}
			else {
				setButtonText("2. Digital I/O Button is not Pressed");
			}
			
			led_.write(!toggleButton_.isChecked());
			Thread.sleep(10);
		}

		@Override
		public void disconnected() {
			enableUi(false);
		}
	}

	@Override
	protected IOIOLooper createIOIOLooper() {
		return new Looper();
	}

	private void enableUi(final boolean enable) {
		runOnUiThread(new Runnable() {
			@Override
			public void run() {
				toggleButton_.setEnabled(enable);
			}
		});
	}

	private void setNumber(float f) {
		final String str = String.format("%.2f", f);
		runOnUiThread(new Runnable() {
			@Override
			public void run() {
				textView_.setText(str);
			}
		});
	}
	
	private void setProx(float f) {
		final String str = String.format("%.2f", f);
		runOnUiThread(new Runnable() {
			@Override
			public void run() {
				ProxTextView_.setText(str);
			}
		});
	}
	
	private void setAlcohol(float f) {
		final String str = String.format("%.2f", f);
		runOnUiThread(new Runnable() {
			@Override
			public void run() {
				AlcoholTextView_.setText(str);
			}
		});
	}
	
	private void setButtonText(final String str) {
		runOnUiThread(new Runnable() {
			@Override
			public void run() {
				buttonText_.setText(str);
			}
		});
	}
}