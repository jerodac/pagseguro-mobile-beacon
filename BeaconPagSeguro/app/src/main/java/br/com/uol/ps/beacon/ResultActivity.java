package br.com.uol.ps.beacon;

import android.app.Activity;
import android.os.Bundle;
import android.widget.TextView;

import br.com.uol.ps.beacon.services.CommonConstants;

/**
 * Created by dario on 28/10/15.
 */
public class ResultActivity extends Activity {

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_result);
        String message = getIntent().getStringExtra(CommonConstants.EXTRA_MESSAGE);
        TextView text = (TextView) findViewById(R.id.result_message);
        text.setText(message);
    }
}
