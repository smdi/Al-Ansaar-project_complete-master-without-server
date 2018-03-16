package al_muntaqimcrescent2018.com.al_ansar;

import android.app.Fragment;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.media.MediaPlayer;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.annotation.RequiresApi;
import android.support.design.widget.FloatingActionButton;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.RelativeLayout;
import android.widget.Toast;

import com.github.florent37.viewanimator.AnimationListener;
import com.github.florent37.viewanimator.ViewAnimator;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.sdsmdg.tastytoast.TastyToast;

/**
 * Created by Imran on 28-01-2018.
 */

public class MonthlyBayans extends Fragment {


    private FloatingActionButton fab;
  //  private Button video,audio;
  //  private RelativeLayout relativeLayout;
    android.app.FragmentTransaction fv,fa ;
    private Fragment fragmentVideo ,fragmentAudio;
    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, Bundle savedInstanceState) {
        return inflater.inflate(R.layout.downloads,container,false);
    }
    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        getActivity().setTitle("Monthly Bayans");
        initialise(view);
        fragmentInitialise();

    }

    synchronized private void  fragmentInitialise() {


        fragmentVideo = new Monthly_Video_Downloads();

        fv = getChildFragmentManager().beginTransaction();
        fv.add(R.id.downloadsfrag ,fragmentVideo).setCustomAnimations(android.R.animator.fade_in,android.R.animator.fade_out).show(fragmentVideo).commit();
    }


    private void initialise(View view) {

        fab = (FloatingActionButton) view.findViewById(R.id.fab_montly_downloads);

        fab.setImageResource(R.drawable.videocamera);
        getFab("video" ,view);
        FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();

        if(constants.EMAIL.equals(user.getEmail())) {

            fab.setVisibility(View.VISIBLE);
        }

    }


    private void getViewAnim(FloatingActionButton video, final View view) {

        ViewAnimator
                .animate(video)
                .thenAnimate(video)
                .scale(.1f,
                        1f, 1f)
                .accelerate()
                .duration(2000)
                .start().onStop(new AnimationListener.Stop() {
            @Override
            public void onStop() {

                TastyToast.makeText(getActivity(), "video", Toast.LENGTH_SHORT,TastyToast.INFO).show();

                Intent intent = new Intent(getActivity(),VideoCreator.class);
                startActivity(intent);


            }
        });

    }




    private void getFab(String s,View view) {

        SharedPreferences preferences = this.getActivity().getSharedPreferences("chooser", Context.MODE_PRIVATE);

        final SharedPreferences.Editor editor = preferences.edit();

        if(s.equals("video"))
        {


            fab.setOnClickListener(new View.OnClickListener() {
                @RequiresApi(api = Build.VERSION_CODES.M)
                @Override
                public void onClick(View view) {


                    getViewAnim(fab,view);
                    getMedia();
                    editor.putInt("media",2);
                    editor.commit();

                }
            });

        }



    }
    public void getMedia() {
        final MediaPlayer mp = MediaPlayer.create(getActivity(),R.raw.tweet);
        mp.start();
    }
}
