package com.tstudioz.androidfirebaseitems;

import android.arch.core.executor.testing.InstantTaskExecutorRule;
import android.arch.lifecycle.Observer;
import android.support.test.runner.AndroidJUnit4;

import com.tstudioz.androidfirebaseitems.data.DataItem;
import com.tstudioz.androidfirebaseitems.data.IFirebaseDatabaseItemRepository;
import com.tstudioz.androidfirebaseitems.data.Resource;
import com.tstudioz.androidfirebaseitems.data.Status;
import com.tstudioz.androidfirebaseitems.mock.MockFirebaseDatabaseItemRepository;
import com.tstudioz.androidfirebaseitems.viewmodel.ItemViewModel;
import com.tstudioz.essentialuilibrary.viewmodel.LiveDataEvent;

import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.TestRule;
import org.junit.runner.RunWith;
import org.mockito.ArgumentCaptor;
import org.mockito.Captor;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.util.List;

import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.CoreMatchers.nullValue;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.mockito.Mockito.timeout;
import static org.mockito.Mockito.verify;

@RunWith(AndroidJUnit4.class)
public class ItemViewModelTest {

    private static final int TIMEOUT_LIVEDATA = 5000;

    @Rule
    public TestRule rule = new InstantTaskExecutorRule();

    @Mock
    private Observer<LiveDataEvent<Resource<DataItem>>> observer;

    @Captor
    private ArgumentCaptor<LiveDataEvent<Resource<DataItem>>> resCaptor;

    private ItemViewModel viewModel;

    @Before
    public void setup() {
        MockitoAnnotations.initMocks(this);

        final IFirebaseDatabaseItemRepository<DataItem> repo = new MockFirebaseDatabaseItemRepository();
        viewModel = new ItemViewModel(repo);
    }

    @Test
    public void loadItemIsSuccess() {
        String key = MockFirebaseDatabaseItemRepository.KEY_ITEM_01;
        DataItem item = new DataItem(key, "Item 1", 1);

        viewModel.loadItem(key).observeForever(observer);

        verify(observer, timeout(TIMEOUT_LIVEDATA).times(2)).onChanged(resCaptor.capture());
        List<LiveDataEvent<Resource<DataItem>>> resources = resCaptor.getAllValues();
        Resource<DataItem> dataItemResource = resources.get(0).peekContent();
        assertThat(dataItemResource.status.status, is(Status.WORKING));
        assertThat(dataItemResource.data, is(nullValue()));
        dataItemResource = resources.get(1).getContentIfNotHandled();
        assertThat(dataItemResource.status.status, is(Status.SUCCESS));
        assertThat(dataItemResource.data, is(item));
    }
}
