/*
 * Copyright 2014-2017 the original author or authors.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package de.codecentric.boot.admin.server.notify.filter;

import de.codecentric.boot.admin.server.event.ClientApplicationRegisteredEvent;
import de.codecentric.boot.admin.server.model.Application;
import de.codecentric.boot.admin.server.model.ApplicationId;
import de.codecentric.boot.admin.server.model.Registration;
import de.codecentric.boot.admin.server.notify.TestNotifier;
import de.codecentric.boot.admin.server.registry.store.ApplicationStore;

import org.junit.Before;
import org.junit.Test;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

public class FilteringNotifierTest {
    private final Application application = Application.create(ApplicationId.of("-"),
            Registration.create("foo", "http://health").build()).build();
    private final ClientApplicationRegisteredEvent event = new ClientApplicationRegisteredEvent(application.getId(),
            application.getRegistration());
    private ApplicationStore store;

    @Before
    public void setUp() throws Exception {
        store = mock(ApplicationStore.class);
        when(store.find(application.getId())).thenReturn(application);
    }

    @Test(expected = IllegalArgumentException.class)
    public void test_ctor_assert() {
        new FilteringNotifier(null, store);
    }

    @Test
    public void test_expired_removal() {
        FilteringNotifier notifier = new FilteringNotifier(new TestNotifier(), store);
        notifier.setCleanupInterval(0L);

        String id1 = notifier.addFilter(new ApplicationNameNotificationFilter("foo", 0L));
        String id2 = notifier.addFilter(new ApplicationNameNotificationFilter("bar", -1L));

        assertThat(notifier.getNotificationFilters()).containsKey(id1).containsKey(id2);

        notifier.notify(event);

        assertThat(notifier.getNotificationFilters()).doesNotContainKey(id1).containsKey(id2);

        notifier.removeFilter(id2);
        assertThat(notifier.getNotificationFilters()).doesNotContainKey(id2);
    }

    @Test
    public void test_filter() {
        TestNotifier delegate = new TestNotifier();
        FilteringNotifier notifier = new FilteringNotifier(delegate, store);

        String idTrue = notifier.addFilter((event, application) -> true);

        notifier.notify(event);

        assertThat(delegate.getEvents()).doesNotContain(event);

        notifier.removeFilter(idTrue);
        notifier.notify(event);

        assertThat(delegate.getEvents()).contains(event);
    }
}
