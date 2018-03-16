package al_muntaqimcrescent2018.com.al_ansar;

import android.app.ProgressDialog;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.media.MediaPlayer;
import android.net.Uri;
import android.provider.MediaStore;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.Display;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.Toast;
import android.widget.VideoView;

import com.airbnb.lottie.LottieAnimationView;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;
import com.sdsmdg.tastytoast.TastyToast;

import java.net.URL;
import java.util.Calendar;

public class VideoCreator extends AppCompatActivity {

    private ImageView videoView;
    private EditText editText,tlink;
    private FirebaseDatabase firebaseDatabase;
    private DatabaseReference dbreference;
    private static final int RC_PHOTO_PICKER = 1;
    private static final int RESULT_OK = -1;
    private Uri photouri;
    int media;
    private LottieAnimationView lottieAnimationView;
    private RelativeLayout lotrel;
    private String fireDb ,fireStr;
    private FirebaseStorage firebaseStorage;
    private StorageReference storageReference;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_video_creator);

         SharedPreferences preferences = getSharedPreferences("chooser", MODE_PRIVATE);
         media = preferences.getInt("media",0);

        initialise();
        firbaseinitialise();

    }

    private void initialise() {
        tlink = (EditText) findViewById(R.id.youtube_link);
        editText  = (EditText)  findViewById(R.id.videodescription);
    }

    private void AndroidSystempusher() {

        final String descriptiontext = " "+editText.getText();

        final String ylink = ""+tlink.getText();

        final String datei = getSystemDate();

        boolean con = getHttp(ylink);

        if((con)&&(descriptiontext.length()>5)) {

            if(descriptiontext.length()<=115) {

                Video_Audio_Initialiser video_audio_initialiser = new Video_Audio_Initialiser("" + ylink.trim(), descriptiontext.replaceAll("\\s+"," "), datei);

                dbreference.push().setValue(video_audio_initialiser).addOnSuccessListener(new OnSuccessListener<Void>() {
                    @Override
                    public void onSuccess(Void aVoid) {

                        TastyToast.makeText(getApplicationContext(), "pushed", Toast.LENGTH_SHORT, TastyToast.SUCCESS).show();
                        cancelAnim();
                        finish();
                    }
                });


            }
            else {

                cancelAnim();
                TastyToast.makeText(getApplicationContext(), "description is too lengthy", Toast.LENGTH_SHORT,TastyToast.ERROR).show();
            }
        }
        else {

            cancelAnim();
            if(con == false) {
                TastyToast.makeText(getApplicationContext(), "give proper link", Toast.LENGTH_SHORT,TastyToast.ERROR).show();
            }
            else {

                TastyToast.makeText(getApplicationContext(), "give description", Toast.LENGTH_SHORT,TastyToast.ERROR).show();

            }
        }

    }

    private boolean getHttp(String url) {

        if(url.contains("https"))
        {

            return true;
        }
        else {
            return false;
        }
    }

      private void firbaseinitialise() {


        if(media == 0) {
             fireDb = corrector("video-downloads");
        }
        else if(media == 1){

             fireDb = corrector("audio-downloads");
        }
        else if(media == 2)
        {
            fireDb = corrector("monthly-video-downloads");
        }
        else if(media == 3) {
            fireDb = corrector("monthly-audio-downloads");
        }
          firebaseDatabase = FirebaseDatabase.getInstance();
          dbreference = firebaseDatabase.getReference().child(fireDb);

    }


    private String corrector(String master) {

        String fin = "";

        String mod = "" ,mod1 = "",mod2 = "",mod3 = "",mod4="",mod5 = "";



        if(master.contains("."))
        {
            mod = ""+master.replace(".","dot") ;
        }
        else{
            mod = master;
        }
        if(mod.contains("$"))
        {


            mod1 = ""+mod.replace("$","dollar");
        }
        else{
            mod1 = mod;
        }
        if(mod1.contains("["))
        {

            mod2 = ""+mod1.replace("[","lsb");
        }
        else{
            mod2 = mod1;
        }
        if(mod2.contains("]"))
        {

            mod3 = ""+mod2.replace("]","rsb");
        }
        else{
            mod3 = mod2;
        }
        if(mod3.contains("#"))
        {

            mod4 = ""+mod3.replace("#","hash");
        }
        else{
            mod4 = mod3;
        }
        if(mod4.contains("/"))
        {

            mod5 = ""+mod4.replace("/","fs");

            fin = mod5;
        }
        else{
            mod5 = mod4;

            fin = mod5;

        }


        System.out.println(""+fin);

        return   fin;
    }
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.push,menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {

        switch (item.getItemId())
        {
            case R.id.push :
                getMedia();
                getLotte();
                TastyToast.makeText(getApplicationContext(),"pushing",Toast.LENGTH_SHORT,TastyToast.INFO).show();
                AndroidSystempusher();
                break;
        }
        return true;
    }

    public String getSystemDate()
    {

        Calendar calendar =Calendar.getInstance();

        return ""+calendar.getTime();
    }

    public void getMedia() {
        final MediaPlayer mp = MediaPlayer.create(getApplicationContext(),R.raw.tweet);
        mp.start();
    }
    public void getLotte() {

        lottieAnimationView = (LottieAnimationView) findViewById(R.id.animation_view);
        lottieAnimationView.setAnimation("preloader.json");
        lottieAnimationView.setVisibility(View.VISIBLE);
        lottieAnimationView.playAnimation();
        lottieAnimationView.loop(true);
        lotrel = (RelativeLayout) findViewById(R.id.lotteRel);
        lotrel.setVisibility(View.VISIBLE);
    }
    private void cancelAnim() {

        lottieAnimationView.setVisibility(View.GONE);
        lottieAnimationView.cancelAnimation();
        lotrel.setVisibility(View.GONE);
    }
}
