import java.io.*;
import java.net.URLDecoder;
import java.util.*;

public class Runner {

    private static List<HotsaleItem> list;
    private static final String RELATIVE_PATH = "src/dump_hotsale_ru.yml";

    public static void main(String[] args) throws IOException {
        list = new LinkedList<>();
        long currentTime = new Date().getTime();

        System.out.println("Script starting..., time = " + (new Date().getTime() - currentTime) / 1e3 + " sec");
        File dump = new File(RELATIVE_PATH);

        BufferedReader br = new BufferedReader(new FileReader(dump));
        String string = null;
        int itemNo = 0;
        try {
            for (int i = 0; i < 42; i++) {
                br.readLine();
            }
            String name, url;
            double commissionRate, price, salePrice;


            System.out.println("Adding items..., time = " + (new Date().getTime() - currentTime) / 1e3 + " sec");
            // Please God dont curse me 4 that
            while (true) {
                if (itemNo % 10000 == 0) {
                    System.out.println("Added " + itemNo + " items. Time = " + (new Date().getTime() - currentTime) / 1e3 + " sec");
                }
                itemNo++;
                br.readLine();
                br.readLine();
                string = br.readLine();
                try {
                    commissionRate = Double.parseDouble(string.substring(18, string.length() - 17));
                } catch (Exception e) {
                    commissionRate = 0.0;
                }

                string = br.readLine();
                name = string.substring(8, string.length() - 7);
                br.readLine();
                br.readLine();
                string = br.readLine();
                price = Double.parseDouble(string.substring(9, string.length() - 8));
                string = br.readLine();
                salePrice = Double.parseDouble(string.substring(14, string.length() - 13));
                br.readLine();
                string = br.readLine();
                url = URLDecoder.decode(string.substring(63, string.length() - 6), "utf-8");
                br.readLine();

                list.add(new HotsaleItem(name, commissionRate, price, salePrice, url));
            }
        } catch (Exception ex) {
            ex.printStackTrace();
        }

        System.out.println("Count of items = " + list.size() + ", time = " + (new Date().getTime() - currentTime) / 1e3 + " sec");

        System.out.println("Sorting list..., time = " + (new Date().getTime() - currentTime) / 1e3 + " sec");
        list.sort((o1, o2) -> (int) (o2.commissionRate * 10 - o1.commissionRate * 10));

        System.out.println("List sorted, time = " + (new Date().getTime() - currentTime) / 1e3 + " sec");


        int DEFAULT_RATE = 30;
        String containsSearch = "cable";

        File file0_20, file21_50, file51_100, file101_200, file201_500, file501_1000;
        BufferedWriter filewriter0_20, filewriter21_50, filewriter51_100, filewriter101_200, filewriter201_500, filewriter501_1000;
        file0_20 = new File("file0_20_rate" + DEFAULT_RATE);
        file21_50 = new File("file21_50_rate" + DEFAULT_RATE);
        file51_100 = new File("file51_100_rate" + DEFAULT_RATE);
        file101_200 = new File("file100_200_rate" + DEFAULT_RATE);
        file201_500 = new File("file201_500_rate" + DEFAULT_RATE);
        file501_1000 = new File("file501_1000_rate" + DEFAULT_RATE);
        filewriter0_20 = new BufferedWriter(new FileWriter(file0_20));
        filewriter21_50 = new BufferedWriter(new FileWriter(file21_50));
        filewriter51_100 = new BufferedWriter(new FileWriter(file51_100));
        filewriter101_200 = new BufferedWriter(new FileWriter(file101_200));
        filewriter201_500 = new BufferedWriter(new FileWriter(file201_500));
        filewriter501_1000 = new BufferedWriter(new FileWriter(file501_1000));

        int count20 = 0, count50 = 0, count100 = 0, count200 = 0, count500 = 0, count1000 = 0;
        int iterator = 0;
        System.out.println("Writing to file, time = " + (new Date().getTime() - currentTime) / 1e3 + " sec");
        for (HotsaleItem hotsaleItem : list) {
            iterator++;
            if (iterator % 100000 == 0) {
                System.out.println("Matched conditions - " + iterator + " items. Time = " + (new Date().getTime() - currentTime) / 1e3 + " sec");
            }
            if (hotsaleItem.salePrice > 0.0 && hotsaleItem.salePrice <= 0.20 && hotsaleItem.commissionRate > DEFAULT_RATE) {
                filewriter0_20.write(String.valueOf(hotsaleItem));
                count20++;
                continue;
            }
            if (hotsaleItem.salePrice > 0.21 && hotsaleItem.salePrice <= 0.50 && hotsaleItem.commissionRate > DEFAULT_RATE) {
                filewriter21_50.write(String.valueOf(hotsaleItem));
                count50++;
                continue;
            }
            if (hotsaleItem.salePrice > 0.51 && hotsaleItem.salePrice <= 1.00 && hotsaleItem.commissionRate > DEFAULT_RATE) {
                filewriter51_100.write(String.valueOf(hotsaleItem));
                count100++;
                continue;
            }
            if (hotsaleItem.salePrice > 1.01 && hotsaleItem.salePrice <= 2.00 && hotsaleItem.commissionRate > DEFAULT_RATE) {
                filewriter101_200.write(String.valueOf(hotsaleItem));
                count200++;
            }
            if (hotsaleItem.salePrice > 2.01 && hotsaleItem.salePrice <= 5.00 && hotsaleItem.commissionRate > DEFAULT_RATE) {
                filewriter201_500.write(String.valueOf(hotsaleItem));
                count500++;
            }
            if (hotsaleItem.salePrice > 5.01 && hotsaleItem.salePrice <= 10.00 && hotsaleItem.commissionRate > DEFAULT_RATE) {
                filewriter501_1000.write(String.valueOf(hotsaleItem));
                count1000++;
            }
        }
        System.out.println("Writing finished. Count = " + count20 + "__u20, " + count50 + "__u50, " + count100 + "__u100, " + count200 + "__u200 for COMMISSION-RATE = " + DEFAULT_RATE);
        filewriter0_20.close();
        filewriter21_50.close();
        filewriter51_100.close();
        filewriter101_200.close();
        filewriter201_500.close();
        filewriter501_1000.close();
        System.out.println("Done. Total time = " + (new Date().getTime() - currentTime) / 1e3 + " sec\n");

        //noinspection ConstantConditions
        if (!containsSearch.equals("")) {
//            list.sort((o1, o2) -> (int) (o1.salePrice * 100 - o2.salePrice * 100));

            containsSearch = containsSearch.toLowerCase();
            iterator = 0;
            int countOfAddedItems = 0;
            currentTime = 0;

            File file_Contains;
            BufferedWriter filewriter_contains;
            file_Contains = new File("fileContains.txt");
            filewriter_contains = new BufferedWriter(new FileWriter(file_Contains));

            System.out.println("Contains search enabled. Searching for \'" + containsSearch + "\'");
            for (HotsaleItem hotsaleItem : list) {
                iterator++;
                if (iterator % 10000 == 0) {
                    System.out.println("Matched conditions - " + iterator + " items. Time = " + (new Date().getTime() - currentTime) / 1e3 + " sec");
                }
                if (hotsaleItem.name.toLowerCase().contains(containsSearch)) {
                    filewriter_contains.write(String.valueOf(hotsaleItem));
                    countOfAddedItems++;
                }
            }
            System.out.println("Contains search finished. Count = " + countOfAddedItems + ", time = " + (new Date().getTime() - currentTime) / 1e3 + " sec");
            filewriter_contains.close();
        }
    }

    static class HotsaleItem {
        String name;
        double commissionRate;
        double price;
        double salePrice;
        String url;

        public HotsaleItem(String name, double commissionRate, double price, double salePrice, String url) {
            this.name = name;
            this.commissionRate = commissionRate;
            this.price = price;
            this.salePrice = salePrice;
            this.url = url;
        }

        @Override
        public String toString() {
            StringBuilder stringBuilder = new StringBuilder("Item");
            stringBuilder.append(name).append('\n')
                    .append("Commission ").append(commissionRate).append('\n')
                    .append("Price ").append(price).append("\tSale price ").append(salePrice).append('\n')
                    .append(url).append("\n\n");
            return stringBuilder.toString();
        }

        @Override
        public int hashCode() {
            return (int) (commissionRate * salePrice * price * url.length());
        }

        @Override
        public boolean equals(Object obj) {
            return this.hashCode() == obj.hashCode();
        }
    }
}
