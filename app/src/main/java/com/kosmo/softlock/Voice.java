package com.kosmo.softlock;

import android.content.ActivityNotFoundException;
import android.content.Intent;
import android.speech.RecognitionListener;
import android.speech.RecognizerIntent;
import android.speech.SpeechRecognizer;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;

public class Voice extends AppCompatActivity implements RecognitionListener {

    private static final String TAG = "lecture";

    private static final int REQUEST_CODE = 1000;
    TextView textView1;
    String mem_idx = "";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_voice);

        mem_idx = getIntent().getExtras().getString("mem_idx");
        textView1 = (TextView) findViewById(R.id.textView1);

        try {
            // 음성 인식의 실행 (1)
            Intent intent = new Intent(RecognizerIntent.ACTION_RECOGNIZE_SPEECH);
            intent.putExtra(RecognizerIntent.EXTRA_CALLING_PACKAGE, getPackageName());
            intent.putExtra(RecognizerIntent.EXTRA_LANGUAGE, "ko-KR");
            intent.putExtra(RecognizerIntent.EXTRA_PROMPT, "음성 검색");
            intent.putExtra(RecognizerIntent.EXTRA_MAX_RESULTS, 1);

            startActivityForResult(intent, REQUEST_CODE);

        } catch (ActivityNotFoundException e) {
            Log.d(TAG, "ActivityNotFoundException");
        }
    }




    @Override
    public void onEndOfSpeech() {
    }

    @Override
    public void onReadyForSpeech(Bundle bundle) {
    }

    @Override
    public void onResults(Bundle results) {
        ArrayList<String> matches = results
                .getStringArrayList(SpeechRecognizer.RESULTS_RECOGNITION);

        String str = "";
        for(int i = 0; i < matches.size() ; i++){
            str = str + matches.get(i) + "\n";
            Log.d(TAG, "onResults text : " + matches.get(i));
        }


    }

    @Override
    public void onError(int errorCode) {
        String message;
        switch (errorCode) {
            case SpeechRecognizer.ERROR_AUDIO:
                message = "오디오 에러";
                break;
            case SpeechRecognizer.ERROR_CLIENT:
                message = "클라이언트 에러";
                break;
            case SpeechRecognizer.ERROR_INSUFFICIENT_PERMISSIONS:
                message = "퍼미션없음";
                break;
            case SpeechRecognizer.ERROR_NETWORK:
                message = "네트워크 에러";
                break;
            case SpeechRecognizer.ERROR_NETWORK_TIMEOUT:
                message = "네트워크 타임아웃";
                break;
            case SpeechRecognizer.ERROR_NO_MATCH:
                message = "찾을수 없음";;
                break;
            case SpeechRecognizer.ERROR_RECOGNIZER_BUSY:
                message = "BUSY";
                break;
            case SpeechRecognizer.ERROR_SERVER:
                message = "서버이상";;
                break;
            case SpeechRecognizer.ERROR_SPEECH_TIMEOUT:
                message = "시간초과";
                break;
            default:
                message = "알수없음";
                break;
        }

        Log.d(TAG, "SPEECH ERROR : " + message);
    }

    @Override
    public void onRmsChanged(float v) {
    }

    @Override
    public void onBeginningOfSpeech() {
    }

    @Override
    public void onEvent(int i, Bundle bundle) {
    }

    @Override
    public void onPartialResults(Bundle bundle) {
    }

    @Override
    public void onBufferReceived(byte[] bytes) {
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        switch (requestCode) {
            case REQUEST_CODE : {
                if (resultCode == RESULT_OK && null != data) {
                    ArrayList<String> text = data
                            .getStringArrayListExtra(RecognizerIntent.EXTRA_RESULTS);

//                    String str = "";
//                    for(int i = 0; i < text.size() ; i++){
//                        str = str + text.get(i) + "\n";
//                        Log.d(TAG, "onActivityResult text : " + text.get(i));
//                    }
//                    textView1.setText(str);

                    String[] rs = new String[text.size()];
                    text.toArray(rs);

                    Intent intent = new Intent(getApplicationContext(), HpSearchMap.class);
                    intent.putExtra("voiceStr",rs[0]);
                    intent.putExtra("mem_idx", mem_idx);
                    startActivity(intent);
                }

                break;
            }
        }
    }
}
