import service.TRKService;

import java.io.IOException;

/**
 * Created by Jakub Filipiak on 25.02.2019.
 */
public class App {



    public static void main(String[] args) throws IOException {

        TRKService trkService = new TRKService();
        trkService.processFile();
    }
}
