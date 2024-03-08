package cc.rabbitcontrol;

import org.rabbitcontrol.rcp.model.Packet;
import org.rabbitcontrol.rcp.model.RCPParser;
import org.rabbitcontrol.rcp.model.exceptions.*;

import java.io.*;

public class ParserTest {

    public static void main(final String[] args) {

        if (args.length == 0) {

            System.err.println("please provide a file");
            return;
        }

        final File file = new File(args[0]);

        if (!file.exists()) {
            System.err.println("file does not exist: " + file.getAbsolutePath());
            return;
        }

        try {

            // init array with file length
            final byte[] bytes_from_file = new byte[(int)file.length()];

            try (final FileInputStream fis = new FileInputStream(file)) {
                fis.read(bytes_from_file); //read file into bytes[]
            }

            // parse
            final Packet packet = RCPParser.fromFile(file.getAbsolutePath());

            // serialize packet
            final byte[] the_bytes = Packet.serialize(packet, true);

            // compare length
            if (bytes_from_file.length != the_bytes.length) {
                System.err.println("length missmatch");

                System.err.println("origin: " + bytesToHex(bytes_from_file));
                System.err.println("parsed: " + bytesToHex(the_bytes));

                return;
            }

            // compare byte by byte
            for (int i = 0; i < the_bytes.length; i++) {

                final byte fb = bytes_from_file[i];
                final byte b  = the_bytes[i];

                if (fb != b) {
                    System.err.println("byte missmatch at index: " + i);

                    System.err.println("origin: " + bytesToHex(bytes_from_file));
                    System.err.println("parsed: " + bytesToHex(the_bytes));
                    return;
                }
            }

            // all good
            System.out.println("bytematch!");
        }
        catch (final IOException | RCPDataErrorException | RCPUnsupportedFeatureException | RCPException _e) {
            _e.printStackTrace();
        }

    }

    public static String bytesToHex(final byte[] in) {

        final StringBuilder builder = new StringBuilder();
        for (final byte b : in) {
            builder.append(String.format("0x%02x ", b));
        }
        return builder.toString();
    }

}
