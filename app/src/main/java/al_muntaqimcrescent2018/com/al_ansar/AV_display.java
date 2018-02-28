package al_muntaqimcrescent2018.com.al_ansar;

import android.app.DownloadManager;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.media.MediaPlayer;
import android.net.Uri;
import android.os.Build;
import android.os.Environment;
import android.os.Vibrator;
import android.support.annotation.NonNull;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.ContextMenu;
import android.view.MenuItem;
import android.view.MotionEvent;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.airbnb.lottie.LottieAnimationView;
import com.facebook.FacebookActivity;
import com.github.florent37.viewanimator.ViewAnimator;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.sdsmdg.tastytoast.TastyToast;

public class AV_display extends AppCompatActivity {


    private boolean web = true , text = true;
    private String url;
    private LottieAnimationView lottieAnimationView;
    private int success = 0;
    private FirebaseDatabase firebaseDatabase;
    private DatabaseReference dbreference;
    private String vid;
    private FloatingActionButton fab;
    private WebSettings webSettings;
    private WebView webView ;
    TextView tv1;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
          this.getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN ,WindowManager.LayoutParams.FLAG_FULLSCREEN);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_HARDWARE_ACCELERATED ,WindowManager.LayoutParams.FLAG_HARDWARE_ACCELERATED);
        setContentView(R.layout.activity_av_display);
        getLotte();
        url = getIntent().getExtras().getString("link");

        SharedPreferences preferences =getApplicationContext().getSharedPreferences("NOTIFY",Context.MODE_PRIVATE);
        int notify = preferences.getInt("notify",1);
        if(notify == 1)
        {

            vid = getVid(url);
            getWebView(vid,notify);

            getFab();
        }

        else{

            fab = (FloatingActionButton) findViewById(R.id.fab_delete);
            fab.setVisibility(View.GONE);
            getWebView(url,notify);
        }
        getSupportActionBar().hide();


    }

    public void getWebView(String vid,int notify) {


        webView = (WebView) findViewById(R.id.my_web_view);
        webSettings = webView.getSettings();
        webView.setWebViewClient(new MyBrowser() {

            @Override
            public boolean shouldOverrideUrlLoading(WebView view, String url) {
                return false;

            }

            @Override
            public void onPageStarted(WebView view, String url, Bitmap favicon) {
                super.onPageStarted(view, url, favicon);
            }

            @Override
            public void onPageFinished(WebView view, String url) {
                super.onPageFinished(view, url);

                setCancel();
            }
        });

        if (notify == 1) {
            webView.setOnTouchListener(new View.OnTouchListener() {
                @Override
                public boolean onTouch(View view, MotionEvent motionEvent) {
                    return (motionEvent.getAction() == MotionEvent.ACTION_MOVE);
                }
            });

            webView.loadUrl("http://www.youtube.com/embed/" + vid + "?autoplay=1&vq=large" + "&rel=0");

            ViewAnimator
                    .animate( webView)
                    .thenAnimate(webView)
                    .scale(.1f, 1f, 1f)
                    .accelerate()
                    .duration(4000)
                    .start();

        } else {
            webView.loadUrl(vid);
        }

            webView.setVisibility(View.VISIBLE);
            webView.getSettings().setBuiltInZoomControls(true);
            webView.getSettings().setRenderPriority(WebSettings.RenderPriority.HIGH);
            webView.getSettings().setCacheMode(WebSettings.LOAD_NO_CACHE);
            webView.getSettings().setAppCacheEnabled(true);
            webView.setScrollBarStyle(View.SCROLLBARS_OUTSIDE_OVERLAY);
            webView.setScrollbarFadingEnabled(true);
            webView.getSettings().setDomStorageEnabled(true);
            webSettings.setLayoutAlgorithm(WebSettings.LayoutAlgorithm.SINGLE_COLUMN);
            webSettings.getUseWideViewPort();
            webSettings.setLoadWithOverviewMode(true);
            webSettings.setUseWideViewPort(true);
            webSettings.setSupportZoom(true);
            webSettings.getSaveFormData();
            webSettings.setEnableSmoothTransition(true);
            webView.getSettings().setJavaScriptEnabled(true);
            webView.setVisibility(View.VISIBLE);
            webView.getSettings().setBuiltInZoomControls(true);
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) {
                webView.setLayerType(View.LAYER_TYPE_HARDWARE, null);
            } else {
                webView.setLayerType(View.LAYER_TYPE_SOFTWARE, null);
            }

            this.registerForContextMenu(webView);



    }

    private void setCancel() {

        lottieAnimationView.cancelAnimation();
        lottieAnimationView.setVisibility(View.GONE);
    }

    public String getVid(String text) {

        String yt = ""+text;
        final String []ty ;

        String ret = "";
        if(yt.contains("=")) {
            final String[] you = yt.split("=");

            if (you[1].contains("&")) {
                ty = you[1].split("&");


                System.out.println("" + ty[0]);

                ret = ty[0];
            } else {


                System.out.println("" + you[1]);

                ret = you[1];
            }

            return  ret;
        }
        else {

            final String[] you = yt.split("https://youtu.be/");

            if (you[1].contains("&")) {
                ty = you[1].split("&");


                System.out.println("" + ty[0]);

                ret = ty[0];
            } else {

                System.out.println("" + you[1]);

                ret = you[1];
            }

            return  ret;

        }

    }



    public void getFab() {

        fab = (FloatingActionButton) findViewById(R.id.fab_delete);
        FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
        if(user.getEmail().equals(constants.EMAIL))
        {
            fab.setVisibility(View.VISIBLE);

        }
        else {

            fab.setVisibility(View.GONE);

        }



        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {


                getMedia();setVibrator();

                getLotte();
                String fireDb[] = new String[]{"video-downloads", "audio-downloads", "monthly-video-downloads", "monthly-audio-downloads"};

                int i = 0;
               do {

                   firebaseDatabase = FirebaseDatabase.getInstance();
                   dbreference = firebaseDatabase.getReference().child(fireDb[i]);
                   getRemove(fireDb[i],"uri");
                   if(i==3)
                   {
                       success =1;
                   }

                   System.out.print(" "+success);

                   i++;

               }while (success == 0);

            }
        });

    }



    private class MyBrowser extends WebViewClient {

        @Override
        public boolean shouldOverrideUrlLoading(WebView view, String url) {
            view.loadUrl(url);
            return true;
        }
    }

    private void getRemove(String s, String s1) {


        DatabaseReference reference = FirebaseDatabase.getInstance().getReference();
        Query apple = reference.child(s).orderByChild(s1).equalTo(url);

        apple.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {

                for (DataSnapshot uri : dataSnapshot.getChildren()) {
                    uri.getRef().removeValue().addOnSuccessListener(new OnSuccessListener<Void>() {
                        @Override
                        public void onSuccess(Void aVoid) {


                            setCancel();
                            finish();
                        }
                    }).addOnFailureListener(new OnFailureListener() {
                        @Override
                        public void onFailure(@NonNull Exception e) {


                            TastyToast.makeText(getApplicationContext(),"try after some time ",Toast.LENGTH_SHORT,TastyToast.ERROR).show();

                        }
                    });

                }

            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

                TastyToast.makeText(getApplicationContext(),"May be deleted !",Toast.LENGTH_SHORT,TastyToast.ERROR).show();
                setCancel();
                finish();
            }
        });
    }

    public void getMedia() {
        final MediaPlayer mp = MediaPlayer.create(getApplicationContext() ,R.raw.tweet);
        mp.start();
    }
    private void setVibrator() {
        Vibrator v =(Vibrator)getSystemService(VIBRATOR_SERVICE);
        v.vibrate(1000);
    }

    public void getLotte() {

        lottieAnimationView = (LottieAnimationView) findViewById(R.id.animation_view);
        lottieAnimationView.setAnimation("preloader.json");
        lottieAnimationView.setVisibility(View.VISIBLE);
        lottieAnimationView.playAnimation();
        lottieAnimationView.loop(true);

    }
}
