/*
 * Copyright (C) 2016 The Android Open Source Project
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package android.support.v4.media;

import android.media.browse.MediaBrowser;
import android.os.Bundle;
import android.os.Parcel;
import android.support.annotation.NonNull;

import java.util.List;

class MediaBrowserCompatApi24 {
    public static Object createSubscriptionCallback(SubscriptionCallback callback) {
        return new SubscriptionCallbackProxy<>(callback);
    }

    public static void subscribe(Object browserObj, String parentId, Bundle options,
            Object subscriptionCallbackObj) {
        ((MediaBrowser) browserObj).subscribe(parentId, options,
                (MediaBrowser.SubscriptionCallback) subscriptionCallbackObj);
    }

    public static void unsubscribe(Object browserObj, String parentId,
            Object subscriptionCallbackObj) {
        ((MediaBrowser) browserObj).unsubscribe(parentId,
                (MediaBrowser.SubscriptionCallback) subscriptionCallbackObj);
    }

    interface SubscriptionCallback extends MediaBrowserCompatApi21.SubscriptionCallback {
        void onChildrenLoaded(@NonNull String parentId, List<Parcel> children,
                @NonNull Bundle options);
        void onError(@NonNull String parentId, @NonNull  Bundle options);
    }

    static class SubscriptionCallbackProxy<T extends SubscriptionCallback>
            extends MediaBrowserCompatApi21.SubscriptionCallbackProxy<T> {
        public SubscriptionCallbackProxy(T callback) {
            super(callback);
        }

        @Override
        public void onChildrenLoaded(@NonNull String parentId,
                List<MediaBrowser.MediaItem> children, @NonNull Bundle options) {
            mSubscriptionCallback.onChildrenLoaded(
                    parentId, itemListToParcelList(children), options);
        }

        @Override
        public void onError(@NonNull String parentId, @NonNull Bundle options) {
            mSubscriptionCallback.onError(parentId, options);
        }
    }
}
