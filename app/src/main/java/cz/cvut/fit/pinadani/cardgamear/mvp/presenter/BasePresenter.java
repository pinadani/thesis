package cz.cvut.fit.pinadani.cardgamear.mvp.presenter;

import android.support.annotation.NonNull;

import java.net.HttpURLConnection;
import java.util.UUID;

import cz.cvut.fit.pinadani.cardgamear.mvp.view.IShowReloginView;
import cz.cvut.fit.pinadani.cardgamear.utils.RetryHelper;
import nucleus.presenter.RxPresenter;
import retrofit2.adapter.rxjava.HttpException;
import rx.Observable;
import rx.functions.Func1;
import rx.schedulers.Schedulers;

/**
 * TODO add class description
 **/
public class BasePresenter<View> extends RxPresenter<View> {
    public static final String TAG = BasePresenter.class.getName();
    private final RetryHelper retryHelper;

    public BasePresenter() {
        this.retryHelper = new RetryHelper();
    }
    //    /**
//     * Wrap observable with retryWhen operator that retries observable when it fails due to some network/api related error.
//     * Presenter that wants to use this Transformer has to be attached to a view that implements {@link IShowRetryView}. Otherwise
//     * an exception will be thrown.
//     */
    public <T> Observable.Transformer<T, T> wrapWithUnauthorizedHandling() {
        return new Observable.Transformer<T, T>() {
            @Override
            public Observable<T> call(Observable<T> observable) {
                return observable.retryWhen(new Func1<Observable<? extends Throwable>, Observable<?>>() {
                    @Override
                    public Observable<?> call(Observable<? extends Throwable> errors) {
                        return errors.flatMap(new Func1<Throwable, Observable<?>>() {
                            @Override
                            public Observable<?> call(Throwable throwable) {
                                if (isUnAuthorizedError(throwable)) {
                                    final UUID uuid = UUID.randomUUID();

                                    // Show retry option.
                                    view().subscribe(view -> {
                                        if (view instanceof IShowReloginView) {
                                            ((IShowReloginView) view).showUnauthorizedDialog(uuid);
                                        } else {
                                            throw new UnsupportedOperationException("View has to implement IShowReloginView to be able to handle retrying.");
                                        }
                                    }, t -> {
                                    });

                                    // Depending on user's choice retry login.
                                    return retryHelper.getRetryObservable(uuid)
                                            .first()
                                            .flatMap(retryAnswer -> Observable.just(null));
                                } else {
                                    return Observable.error(throwable);
                                }
                            }
                        });
                    }
                }, Schedulers.io());
            }
        };
    }

    private boolean isUnAuthorizedError(Throwable error) {
        if (error instanceof HttpException) {
            if (((HttpException) error).code() == HttpURLConnection.HTTP_UNAUTHORIZED) {
                return true;
            }
        }
        return false;
    }

    /**
     * Accept retry option so that observable will be retried.
     */
    public void onRetryAccepted(@NonNull UUID uuid) {
        retryHelper.onRetryAccepted(uuid);
    }
}