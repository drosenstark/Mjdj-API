package com.confusionists.mjdj.api.midiDevice;

import com.confusionists.mjdj.api.MessageWrapper;

public interface ReceiverDeviceWrapper extends DeviceWrapper {

    public void send(MessageWrapper message);

}
