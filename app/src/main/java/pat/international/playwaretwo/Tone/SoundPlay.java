package pat.international.playwaretwo.Tone;

import android.media.AudioFormat;
import android.media.AudioManager;
import android.media.AudioTrack;
import android.media.SoundPool;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import android.os.Handler;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.Toast;

import java.util.Random;

import pat.international.playwaretwo.R;


public class SoundPlay extends Fragment {

    // originally from http://marblemice.blogspot.com/2010/04/generate-and-play-tone-in-android.html
    // and modified by Steve Pomeroy <steve@staticfree.info>

    private String note_list[] = {"a", "b", "c", "d", "e", "f", "g"};

    private String partition[] = {"e", "e", "e", "e",
            "e", "e", "e", "g", "c", "d", "e", "f", "f", "f", "f", "e", "e", "e", "e", "e", "d", "d", "e", "d", "g"};

    private Integer positions[] = {1, 3, 2, 3, 1, 3, 2, 1, 2, 3, 1, 3, 2, 1, 2, 3, 2, 1, 3, 1, 3, 2, 1, 3, 2};

    private Integer location1 = 0;
    private Integer location2 = 0;
    private Integer location3 = 0;
    private Integer location4 = 0;
    private Integer location5 = 0;


    ImageView iv_11, iv_12, iv_13,
            iv_21, iv_22, iv_23,
            iv_31, iv_32, iv_33,
            iv_41, iv_42, iv_43,
            iv_51, iv_52, iv_53;

    private Integer note_count = 0;

    private SoundPool soundPool;
    private Integer integerSoundIDa;
    private Integer integerSoundIDb;
    private Integer integerSoundIDc;
    private Integer integerSoundIDd;
    private Integer integerSoundIDe;
    private Integer integerSoundIDf;
    private Integer integerSoundIDg;

    private float floatSpeed = 1.0f;


    private final int duration = 3; // seconds
    private final int sampleRate = 8000;
    private final int numSamples = duration * sampleRate;
    private final double sample[] = new double[numSamples];
    private final double freqOfTone = 440; // hz


    private final byte generatedSnd[] = new byte[2 * numSamples];

    Handler handler = new Handler();
    View v;
    Button endGameBtn;
    Button oneRandomNote;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        v = inflater.inflate(R.layout.fragment_start_game_seven, container, false);

        endGameBtn = v.findViewById(R.id.button_end);
        oneRandomNote = v.findViewById(R.id.button_note);
        iv_11 = v.findViewById(R.id.iv_11);
        iv_12 = v.findViewById(R.id.iv_12);
        iv_13 = v.findViewById(R.id.iv_13);
        iv_21 = v.findViewById(R.id.iv_21);
        iv_22 = v.findViewById(R.id.iv_22);
        iv_23 = v.findViewById(R.id.iv_23);
        iv_31 = v.findViewById(R.id.iv_31);
        iv_32 = v.findViewById(R.id.iv_32);
        iv_33 = v.findViewById(R.id.iv_33);
        iv_41 = v.findViewById(R.id.iv_41);
        iv_42 = v.findViewById(R.id.iv_42);
        iv_43 = v.findViewById(R.id.iv_43);
        iv_51 = v.findViewById(R.id.iv_51);
        iv_52 = v.findViewById(R.id.iv_52);
        iv_53 = v.findViewById(R.id.iv_53);

        SoundPool.Builder builder = new SoundPool.Builder();
        soundPool = builder.build();


        integerSoundIDa = soundPool.load(getContext(), R.raw.a3, 1);
        integerSoundIDb = soundPool.load(getContext(), R.raw.b3, 1);
        integerSoundIDc = soundPool.load(getContext(), R.raw.c3, 1);
        integerSoundIDd = soundPool.load(getContext(), R.raw.d3, 1);
        integerSoundIDe = soundPool.load(getContext(), R.raw.e3, 1);
        integerSoundIDf = soundPool.load(getContext(), R.raw.f3, 1);
        integerSoundIDg = soundPool.load(getContext(), R.raw.g3, 1);

        return v;
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        oneRandomNote.setOnClickListener(v -> {
            button_play_note();
        });
    }

    public void button_play_note() {
        String randomNote = partition[note_count];
        note_count = note_count + 1;
        if (randomNote == "a") {
            soundPool.play(integerSoundIDa, 1, 1, 1, 0, floatSpeed);
        }
        if (randomNote == "b") {
            soundPool.play(integerSoundIDb, 1, 1, 1, 0, floatSpeed);
        }
        if (randomNote == "c") {
            soundPool.play(integerSoundIDc, 1, 1, 1, 0, floatSpeed);
        }
        if (randomNote == "d") {
            soundPool.play(integerSoundIDd, 1, 1, 1, 0, floatSpeed);
        }
        if (randomNote == "e") {
            soundPool.play(integerSoundIDe, 1, 1, 1, 0, floatSpeed);
        }
        if (randomNote == "f") {
            soundPool.play(integerSoundIDf, 1, 1, 1, 0, floatSpeed);
        }
        if (randomNote == "g") {
            soundPool.play(integerSoundIDg, 1, 1, 1, 0, floatSpeed);
        }
    }


}