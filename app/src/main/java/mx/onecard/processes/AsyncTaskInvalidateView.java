package mx.onecard.processes;

import android.os.CountDownTimer;
import android.view.View;
import android.widget.ProgressBar;

/**
 * Created by OneCardAdmon on 20/08/2015.
 * Deshabilita una vista, despliega el progreso en una progressbar y al final la vuelve activar
 */
public class AsyncTaskInvalidateView extends CountDownTimer{
    private View mView;
    private ProgressBar mProgressBar;
    private int progressBarStatus;
    private int progressBarStatusMax;

    AsyncTaskInvalidateView(View view, int miliseconds) {
        super(miliseconds,1000);
        setup(view,miliseconds);
    }

    AsyncTaskInvalidateView(View view, int miliseconds,int millisTickGap) {
        super(miliseconds, millisTickGap);
        setup(view,miliseconds);
    }

    AsyncTaskInvalidateView(View view, int miliseconds, ProgressBar progressBar) {
        super(miliseconds,1000);
        setup(view,miliseconds,progressBar);
    }

    AsyncTaskInvalidateView(View view, int miliseconds,int millisTickGap, ProgressBar progressBar) {
        super(miliseconds, millisTickGap);
        setup(view,miliseconds,progressBar);
    }

    private void setup(View view,int miliseconds){
        this.mView = view;
        mProgressBar = null;
        progressBarStatusMax = miliseconds;
    }

    private void setup(View view,int miliseconds, ProgressBar progressBar){
        setup(view, miliseconds);
        mProgressBar = progressBar;
    }

    public synchronized CountDownTimer starter(){
        mView.setEnabled(false);
        progressBarStatus = 0;
        mProgressBar.setProgress(progressBarStatus);
        mProgressBar.setMax(progressBarStatusMax);      // Milis maximus
        return super.start();
    }

    @Override
    public void onTick(long millisUntilFinished) {
        if(mProgressBar != null){
            mProgressBar.setProgress((int) (progressBarStatusMax - millisUntilFinished));
        }
    }

    @Override
    public void onFinish() {
        mView.setEnabled(true); // Habilitamos la vista
    }

    public void setProgressBar(ProgressBar progressBar) {
        this.mProgressBar = progressBar;
    }
}
