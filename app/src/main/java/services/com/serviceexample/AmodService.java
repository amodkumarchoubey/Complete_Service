package services.com.serviceexample;

import android.app.Service;
import android.content.Intent;
import android.media.MediaPlayer;
import android.os.Binder;
import android.os.IBinder;
import android.support.annotation.Nullable;
import android.util.Log;

import java.util.Random;

public class AmodService extends Service {
    //creating a mediaplayer object
    private MediaPlayer player;

    private int mRandomNumber;
    private boolean mIsRandomGenerate;
    private final  int MIN=1;
    private final int MAX=100;

    class  MyServiceBinder extends Binder{
        public AmodService getService(){
            return  AmodService.this;
        }
    }

    private IBinder iBinder=new MyServiceBinder();
    @Nullable
    @Override
    public IBinder onBind(Intent intent) {
        Log.e("Inside","onBind");
        return iBinder;
    }

    @Override
    public void onCreate() {
        super.onCreate();
        Log.e("Inside","onCreate");
    }

    @Override
    public boolean onUnbind(Intent intent) {
        Log.e("Inside","onUnbind");
        return super.onUnbind(intent);
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        Log.e("Inside","onStartCommand");
        //getting systems default ringtone
//        player = MediaPlayer.create(this,
//                Settings.System.DEFAULT_RINGTONE_URI);
//
//        player.setLooping(true);
//
//        //staring the player
//        player.start();

        mIsRandomGenerate=true;
        new  Thread(new Runnable() {
            @Override
            public void run() {
                startRandomNumberGenerator();
            }
        }).start();


        return START_STICKY;
    }


    private void startRandomNumberGenerator()
    {
        while (mIsRandomGenerate)
        {
            try {
                Thread.sleep(1000);
                if (mIsRandomGenerate)
                {
                    mRandomNumber=new Random().nextInt(MAX)+MIN;
                    Log.e("Random number ","is===>"+mRandomNumber);
                }
            }catch (Exception e)
            {
                e.getMessage();
            }
        }
    }

    private void stopRandomNumberGenerator()
    {
        mIsRandomGenerate=false;
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        Log.e("Inside","onDestroy");
        stopRandomNumberGenerator();
        //stopping the player when service is destroyed
//        player.stop();
    }

    public int getmRandomNumber()
    {
        return mRandomNumber;
    }
}
