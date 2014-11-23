package edu.umich.seedforandroid.api;

import android.os.Handler;
import android.os.Looper;
import android.os.Message;

import com.appspot.umichseed.seed.SeedRequest;

import java.io.IOException;
import java.util.concurrent.BlockingDeque;
import java.util.concurrent.LinkedBlockingDeque;
import java.util.concurrent.TimeUnit;

/**
 * Created by Dominic on 10/8/2014.
 */
public class ApiThread {

    private static final int API_RESULT_MESSAGE = 100;
    private static final int API_ERROR_MESSAGE = -100;

    // we run API calls 1 at a time, these are the objects needed to do that
    // note that all BlockingQueue types are thread safe
    private BlockingDeque<ApiTask> mPendingTasks;
    private Handler mHandler;
    private Thread mApiThread;

    public ApiThread() {

        create(Looper.getMainLooper());
    }

    public ApiThread(Looper looper) {

        create(looper);
    }

    private void create(Looper looper) {

        mPendingTasks = new LinkedBlockingDeque<ApiTask>();
        mHandler = new ApiHandler(looper);
        mApiThread = new Thread(new ApiRunnable(mHandler, mPendingTasks));
    }

    // region start and stop

    /**
     * starts execution of the API Thread. Implicitly called when you make an API call.
     */
    public void start() {

        if (!isRunning()) {

            synchronized (ApiThread.this) {

                mApiThread.start();
            }
        }
    }

    /**
     * Interrupts the execution thread for this instance of the API. Currently executing API calls
     * will finish before the interruption occurs, however.
     */
    public void stop() {

        if (isRunning()) {

            synchronized (ApiThread.this) {

                mApiThread.interrupt();

                //can't restart a thread, so we need to re-instantiate this
                mApiThread = new Thread(new ApiRunnable(mHandler, mPendingTasks));
            }
        }
    }
    // endregion

    public boolean isRunning() {

        synchronized (ApiThread.this) {

            return mApiThread.isAlive() && !mApiThread.isInterrupted();
        }
    }

    // region listener management

    public static abstract class ApiResultAction {

        private Object mmResult;

        private void setResult(Object result) { mmResult = result; }
        public final Object getResult() { return mmResult; }

        private void dispatchResult() { onApiResult(getResult()); }
        private void dispatchError() { onApiError((Throwable)getResult()); }

        public Object doInBackground(Object result) { return result; }
        public abstract void onApiResult(Object result);
        public abstract void onApiError(Throwable error);
    }

    // endregion

    /**
     * Adds an Api Request to the execution queue. Api Requests execute in the order they are added
     * (just like synchronous calls). If the process thread isn't started when you call this method,
     * then it will be started (if the request is successfully added to the queue).
     * @param request the {@link com.appspot.umichseed.seed.SeedRequest SeedRequest} to process (all requests in the API extend {@link com.appspot.umichseed.seed.SeedRequest SeedRequest})
     * @param action the {@Link ApiResultListener} you'd like to receive this result. You may pass null if you don't need the result
     * @return true if the request is successfully added to the process queue. False otherwise.
     */
    public boolean enqueueRequest(SeedRequest request, ApiResultAction action) {

        ApiTask task = new ApiTask(request, action);
        boolean result = mPendingTasks.offer(task);

        if (result) {

            start();
        }

        return result;
    }

    // region internal classes

    private class ApiHandler extends Handler {

        public ApiHandler() { super(); }

        public ApiHandler(Looper looper) { super(looper); }

        @Override
        public void handleMessage(Message msg) {

            switch (msg.what){

                case API_RESULT_MESSAGE:

                    ((ApiResultAction)msg.obj)
                            .dispatchResult();
                    break;

                case API_ERROR_MESSAGE:

                    ((ApiResultAction)msg.obj)
                            .dispatchError();
                    break;
            }
        }
    }

    private class ApiRunnable implements Runnable {

        private final long mmPollTimeout = 1000L;
        private final TimeUnit mmPollTimeoutUnit = TimeUnit.MILLISECONDS;

        private Handler mmHandler;
        private BlockingDeque<ApiTask> mmPendingTasks;

        private ApiRunnable(Handler handler, BlockingDeque<ApiTask> pendingTasks) {

            mmHandler = handler;
            mmPendingTasks = pendingTasks;
        }

        private boolean isInterrupted() {

            synchronized (ApiThread.this) {

                return Thread.currentThread().isInterrupted();
            }
        }

        private void executeTask(ApiTask task) {

            Object result = null;

            try {

                result = task.getRequest().execute();
                ApiResultAction action = task.getResultAction();

                if (action != null) {

                    result = action.doInBackground(result);
                    action.setResult(result);
                    mmHandler.obtainMessage(API_RESULT_MESSAGE, action).sendToTarget();
                }
            }
            catch (IOException e) {

                ApiResultAction action = task.getResultAction();

                if (action != null) {

                    action.setResult(e);
                    mmHandler.obtainMessage(API_ERROR_MESSAGE, action).sendToTarget();
                }
            }
        }

        @Override
        public void run() {

            while (!isInterrupted()) {

                try {

                    ApiTask nextTask = mmPendingTasks.poll(mmPollTimeout, mmPollTimeoutUnit);

                    if (nextTask != null) {

                        if (isInterrupted()) {

                            // if we're interrupted, make sure the unfinished task is put back on
                            // the queue
                            mmPendingTasks.offerFirst(nextTask);
                        }
                        else {

                            executeTask(nextTask);
                        }
                    }
                }
                catch (InterruptedException ie) {/*continue*/}
            }
        }
    }

    private class ApiTask  {

        private final SeedRequest mmRequest;
        private final ApiResultAction mmAction;

        private ApiTask(SeedRequest request, ApiResultAction resultAction) {

            mmRequest = request;
            mmAction = resultAction;
        }

        private SeedRequest getRequest() { return mmRequest; }
        private ApiResultAction getResultAction() { return mmAction; }
    }
    // endregion
}
