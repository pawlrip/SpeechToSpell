package net.pawlrip.speech;

import javax.sound.sampled.*;

/**
 * Handles a data-line that represents a microphone, which the {@link RecognitionThread} uses.
 */
public class MicrophoneHandler {
    private final TargetDataLine line;
    private final int cacheSize;

    public MicrophoneHandler(int sampleRate, int cacheSize) throws LineUnavailableException {
        this.cacheSize = cacheSize;
        AudioFormat format = new AudioFormat(sampleRate, 16, 1, true, false);
        this.line = (TargetDataLine) AudioSystem.getLine(new DataLine.Info(TargetDataLine.class, format));
        this.line.open(format);

        this.line.start();
        this.line.stop();
        this.line.flush();
    }

    public void startListening() {
        this.line.start();
    }

    public void stopListening() {
        this.line.stop();
        this.line.flush();
    }

    public void end() {
        this.line.close();
    }

    public byte[] readData() {
        byte[] buffer = new byte[this.cacheSize];
        int count = this.line.read(buffer, 0, buffer.length);

        if (count > 0) return buffer;
        else return new byte[0];
    }
}
