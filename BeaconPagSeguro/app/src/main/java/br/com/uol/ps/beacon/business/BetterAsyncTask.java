package br.com.uol.ps.beacon.business;

import android.os.AsyncTask;
import android.os.Handler;

import java.util.ArrayList;
import java.util.List;

import br.com.uol.ps.beacon.vo.BaseResponseVO;

public abstract class BetterAsyncTask<Result extends BaseResponseVO>
        extends AsyncTask<Void, Void, Result> {

    private List<Runnable> mExitListeners = new ArrayList<>();
    private Exception mError;

    protected abstract Result doIt();

    protected abstract void onResult(Result result);

    protected void onAbort() {
    }

    protected void onError(Exception ex) {
    }

    public void addExitListener(Runnable runnable) {
        mExitListeners.add(runnable);
    }

    @Override
    protected Result doInBackground(Void... params) {
        try {
            return doIt();
        } catch (Exception ex) {
            mError = ex;
            return null;
        }
    }

    @Override
    protected void onPostExecute(Result result) {
        if (!isCancelled()) {
            onExit();
            if (mError == null) {
                onResult(result);
            } else {
                error();
            }
        }
    }

    @Override
    protected void onCancelled() {
        onExit();
//        onAbort();
    }

    private void error() {
        new Handler().post(new Runnable() {
            @Override
            public void run() {
                onExit();
                onError(mError);
            }
        });
    }

    private void onExit() {
        for (Runnable r : mExitListeners) {
            r.run();
        }
    }

}