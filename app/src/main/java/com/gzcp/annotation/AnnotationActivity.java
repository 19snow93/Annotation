package com.gzcp.annotation;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.widget.Toast;

import com.gzcp.lib_annotations.Add;
import com.gzcp.lib_api.Util;

/**
 * Created by leo on 2018/3/28.
 */

public class AnnotationActivity extends AppCompatActivity {

    @Add(ele1 = 10f,ele2 = 1000f)
    public double ele;

    @Add(ele1 = 5f,ele2 = 5000f)
    public double total;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        InjectorProcessor injectorProcessor = new InjectorProcessor();
        injectorProcessor.process(this);
        Util.inject(this);
        Log.i("ele ==> ", ele + "");
        Log.i("total ==> ", total + "");
    }

}
