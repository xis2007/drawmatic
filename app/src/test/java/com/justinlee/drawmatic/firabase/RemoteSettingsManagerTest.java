package com.justinlee.drawmatic.firabase;

import com.justinlee.drawmatic.MainActivity;
import com.justinlee.drawmatic.MainPresenter;

import org.junit.Assert;
import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.mockito.Mock;
import org.mockito.junit.MockitoJUnit;
import org.mockito.junit.MockitoRule;

import static org.mockito.Mockito.when;

public class RemoteSettingsManagerTest {
    @Mock
    MainActivity mMainActivity;

    @Mock
    MainPresenter mMainPresenter;

    @Mock
    RemoteSettingsManager mRemoteSettingsManager;

    @Rule
    public MockitoRule mMockitoRule = MockitoJUnit.rule();

    @Before
    public void setup() {
        when(mRemoteSettingsManager.getCurrentAppVersionCode()).thenReturn(10);
    }

    @Test
    public void isUpdateRequired() {
        boolean isUpdateRequired = mRemoteSettingsManager.isUpdateRequired(true, 10);

        Assert.assertFalse(isUpdateRequired);
    }

}