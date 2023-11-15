package io.mafialike.other;

import net.dv8tion.jda.api.audio.AudioSendHandler;
import javax.sound.sampled.AudioInputStream;
import javax.sound.sampled.AudioSystem;
import java.io.File;
import java.io.IOException;
import java.nio.ByteBuffer;

public class MySendHandler implements AudioSendHandler {
    private final byte[] buffer = new byte[1024];
    private AudioInputStream audioStream;

    public MySendHandler() {
        try {
            audioStream = AudioSystem.getAudioInputStream(new File("src/main/resources/sounds/women_haha.mp3"));
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Override
    public boolean canProvide() {
        try {
            return audioStream.available() > 0;
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
    }

    @Override
    public ByteBuffer provide20MsAudio() {
        int amountToRead = 0;
        try {
            amountToRead = Math.min(buffer.length, audioStream.available());
        } catch (IOException e) {
            throw new RuntimeException(e);
        }

        try {
            int r = audioStream.read(buffer, 0, amountToRead);
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }

        return ByteBuffer.wrap(buffer, 0, amountToRead);
    }

    @Override
    public boolean isOpus() {
        return false;
    }
}
