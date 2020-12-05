package services.com.serviceexample;

import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.ServiceConnection;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.IBinder;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

public class MainActivity extends AppCompatActivity implements View.OnClickListener {

    private TextView buttonStart, buttonStop, buttonbindservices,
            buttonunbindservices, buttonrandomnumber, txt_setdata;
    private AmodService amodService;
    private boolean isserviceBound;
    private ServiceConnection serviceConnection;
    private Intent serviceintent;
    private boolean stoploop;
    int count = 0;
    private MyAsyncTask myAsyncTask;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        //getting buttons from xml
        buttonStart = (TextView) findViewById(R.id.buttonStart);
        buttonStop = (TextView) findViewById(R.id.buttonStop);
        buttonbindservices = (TextView) findViewById(R.id.buttonbindservices);
        buttonunbindservices = (TextView) findViewById(R.id.buttonunbindservices);
        buttonrandomnumber = (TextView) findViewById(R.id.buttonrandomnumber);
        txt_setdata = (TextView) findViewById(R.id.txt_setdata);
        buttonStart.setOnClickListener(this);
        buttonStop.setOnClickListener(this);
        buttonbindservices.setOnClickListener(this);
        buttonunbindservices.setOnClickListener(this);
        buttonrandomnumber.setOnClickListener(this);
        serviceintent = new Intent(getApplicationContext(), AmodService.class);
    }

    @Override
    public void onClick(View view) {
        if (view == buttonStart) {
            Toast.makeText(getApplicationContext(), "Service Start", Toast.LENGTH_LONG).show();
            //start the service here
//            startService(new Intent(this, AmodService.class));
            stoploop = true;
            myAsyncTask=new MyAsyncTask();
            myAsyncTask.execute(count);
            startService(serviceintent);
        } else if (view == buttonStop) {
            //stop the service here
            stoploop = false;
             myAsyncTask.cancel(true);
            stopService(new Intent(this, AmodService.class));
            Toast.makeText(getApplicationContext(), "Service Stop", Toast.LENGTH_LONG).show();
        } else if (view == buttonbindservices) {
            bindservice();
        } else if (view == buttonunbindservices) {
            unBindService(serviceConnection);
        } else if (view == buttonrandomnumber) {
            setRandomNumber();
        }
    }

    private void setRandomNumber() {
        if (isserviceBound) {
            txt_setdata.setText(amodService.getmRandomNumber());
        } else {
            txt_setdata.setText("Service Not Bound");
        }

    }

    private void unBindService(ServiceConnection serviceConnection) {
        if (isserviceBound) {
            unBindService(this.serviceConnection);
            isserviceBound = false;
        }
    }

    private void bindservice() {
        if (serviceConnection == null) {
            serviceConnection = new ServiceConnection() {
                @Override
                public void onServiceConnected(ComponentName name, IBinder iBinder) {
                    AmodService.MyServiceBinder myServiceBinder = (AmodService.MyServiceBinder) iBinder;
                    amodService = myServiceBinder.getService();
                    isserviceBound = true;
                }

                @Override
                public void onServiceDisconnected(ComponentName name) {
                    isserviceBound = false;
                }
            };
        }
        bindService(serviceintent, serviceConnection, Context.BIND_AUTO_CREATE);
    }

    private class MyAsyncTask extends AsyncTask<Integer, Integer, Integer> {

        private int customCounter;

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            customCounter = 0;
        }

        @Override
        protected Integer doInBackground(Integer... integers) {
            customCounter = integers[0];
            while (stoploop) {
                try {
                    Thread.sleep(1000);
                    customCounter++;
                    publishProgress(customCounter);
                } catch (InterruptedException e) {
                    Log.i("ASYNCTASK", e.getMessage());
                }

            }
            return customCounter;
        }

        @Override
        protected void onProgressUpdate(Integer... values) {
            super.onProgressUpdate(values);
            txt_setdata.setText("" + values[0]);
        }

        @Override
        protected void onPostExecute(Integer integer) {
            super.onPostExecute(integer);
            txt_setdata.setText("" + integer);
            count = integer;
        }
    }
}