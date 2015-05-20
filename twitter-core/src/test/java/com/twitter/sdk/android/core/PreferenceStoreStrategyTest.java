/*
 * Copyright (C) 2015 Twitter, Inc.
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
 *
 */

package com.twitter.sdk.android.core;

import io.fabric.sdk.android.services.persistence.PreferenceStore;
import io.fabric.sdk.android.services.persistence.PreferenceStoreImpl;
import io.fabric.sdk.android.services.persistence.PreferenceStoreStrategy;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.robolectric.RobolectricGradleTestRunner;
import org.robolectric.annotation.Config;

@RunWith(RobolectricGradleTestRunner.class)
@Config(constants = BuildConfig.class, emulateSdk = 21)
public class PreferenceStoreStrategyTest extends TwitterCoreAndroidTestCase {
    private PreferenceStore preferenceStore;
    private PreferenceStoreStrategy<TwitterSession> preferenceStrategy;

    @Override
    public void setUp() throws Exception {
        super.setUp();
        preferenceStore = new PreferenceStoreImpl(getContext(), "testSession");
        preferenceStrategy = new PreferenceStoreStrategy<>(preferenceStore,
                new TwitterSession.Serializer(), "testSession");
    }

    @Override
    public void tearDown() throws Exception {
        super.tearDown();
        preferenceStrategy.clear();
    }

    @Test
    public void testRestore_emptyStore() throws Exception {
        assertEquals(null, preferenceStrategy.restore());
    }

    @Test
    public void testSaveAndRestore_nullSession() throws Exception {
        preferenceStrategy.save(null);
        final TwitterSession restoredSession = preferenceStrategy.restore();
        assertEquals(null, restoredSession);
    }

    @Test
    public void testSaveAndRestore_session() throws Exception {
        final TwitterSession session = new TwitterSession(new TwitterAuthToken
                (TestFixtures.TOKEN, TestFixtures.SECRET), TwitterSession.UNKNOWN_USER_ID,
                TwitterSession.UNKNOWN_USER_NAME);
        preferenceStrategy.save(session);
        final TwitterSession restoredSession = preferenceStrategy.restore();
        assertEquals(session, restoredSession);
    }
}