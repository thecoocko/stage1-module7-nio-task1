package com.epam.mjc.nio;

import java.io.File;
import java.io.IOException;
import java.io.RandomAccessFile;
import java.nio.ByteBuffer;
import java.nio.channels.FileChannel;
import java.util.logging.Logger;
import java.util.regex.Matcher;
import java.util.regex.Pattern;


public class FileReader {
    private static final Logger logger = Logger.getLogger(FileReader.class.getName());

    public Profile getDataFromFile(File file) {

        try(RandomAccessFile accessFile = new RandomAccessFile(file,"r");
            FileChannel fileChannel = accessFile.getChannel()){
            int size = (int) fileChannel.size()+1;
            ByteBuffer buffer = ByteBuffer.allocate(size);
            StringBuilder data = new StringBuilder(size);
            while(fileChannel.read(buffer)>0){
                buffer.flip();
                for (int i = 0; i < buffer.limit(); i++) {
                    data.append((char)buffer.get());
                }
                buffer.clear();
            }
            String [] userData = new String[4];

            Pattern pattern = Pattern.compile("(?<=:)(\\s*)(.+)");
            Matcher matcher = pattern.matcher(data.toString().replace(" ",""));

            int i = 0;
            while (matcher.find()) {
                userData[i] = matcher.group();
                i++;
            }

            Profile profile = new Profile();
            profile.setAge(Integer.valueOf(userData[1]));
            profile.setName(userData[0]);
            profile.setEmail(userData[2]);
            profile.setPhone(Long.valueOf(userData[3]));

            return profile;
        } catch (IOException e) {
            logger.severe(e.getMessage());
        }

        return null;
    }

}
