package fishfarm.gotech;

import android.os.Bundle;
import android.os.CountDownTimer;
import android.support.v7.app.AppCompatActivity;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.EditText;

import java.util.concurrent.TimeUnit;

import helperClass.CommonFunctions;
import helperClass.SessionManager;

public class TransporterTankInspActivity extends AppCompatActivity {
    View view;
    EditText iedt_Juveniles;
    EditText iedt_Body_Wt;
    EditText iedt_Tot_Wt;
    EditText iedt_Oxygen_Per;
    Button btnSubmit;
    long timer = 600000;
    private CommonFunctions commonFunctions;
    private SessionManager sessionManager;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.hatchery_transporter_tank_insp);
        setTitle("");
        getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_ADJUST_PAN);

        commonFunctions = new CommonFunctions(this);
        sessionManager = new SessionManager(this);
        btnSubmit=(Button)findViewById(R.id.btnSubmit);
        btnSubmit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v){
                CommonFunctions.hideSoftwareKeyboard(TransporterTankInspActivity.this);
                (TransporterTankInspActivity.this).finish();
            }
        });

    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_timer, menu);

        final MenuItem counter = menu.findItem(R.id.counter);
        new CountDownTimer(timer, 1000) {

            public void onTick(long millisUntilFinished) {
                long millis = millisUntilFinished;
                String  hms = (TimeUnit.MILLISECONDS.toMinutes(millis) -TimeUnit.HOURS.toMinutes(TimeUnit.MILLISECONDS.toHours(millis)))+":"+ (TimeUnit.MILLISECONDS.toSeconds(millis) - TimeUnit.MINUTES.toSeconds(TimeUnit.MILLISECONDS.toMinutes(millis)));

                counter.setTitle(hms);
                timer = millis;

            }

            public void onFinish() {
                counter.setTitle("done!");
            }
        }.start();

        return  true;
    }


    @Override
    public void onDestroy() {
        CommonFunctions.hideSoftwareKeyboard(this);
        super.onDestroy();
    }
    @Override
    public void onStop() {
        CommonFunctions.hideSoftwareKeyboard(this);
        super.onStop();
    }
}
