package cz.cvut.fit.pinadani.cardgamear.utils;

import android.support.annotation.NonNull;

import java.util.UUID;

import rx.Observable;
import rx.subjects.PublishSubject;

/**
 * Helper class that takes care of retrying.
 * Created by Jan Stanek[jan.stanek@ackee.cz] on {29.6.16}
 **/
public class RetryHelper {
    public static final String TAG = RetryHelper.class.getName();

    private final PublishSubject<RetryAnswer> retrySubject;

    public RetryHelper() {
        this.retrySubject = PublishSubject.create();
    }

    /**
     * Accept retry option so that observable will be retried.
     *
     * @param uuid UUID of RetryAnswer to push to retrySubject.
     */
    public void onRetryAccepted(@NonNull UUID uuid) {
        retrySubject.onNext(new RetryAnswer(uuid));
    }

    /**
     * Get Observable with RetryAnswer that corresponds to given UUID.
     *
     * @param uuid UUID of RetryAnswer to observe.
     * @return RetryAnswer observable.
     */
    public Observable<RetryAnswer> getRetryObservable(UUID uuid) {
        return retrySubject.filter(retryAnswer -> retryAnswer.getUuid().equals(uuid));
    }

    /**
     * Holder class for retry answers.
     */
    public static class RetryAnswer {

        private final UUID uuid;

        public RetryAnswer(UUID uuid) {
            this.uuid = uuid;
        }

        public UUID getUuid() {
            return uuid;
        }
    }
}
