package br.usp.lti.cdds;

public class Main {

    public static void main(String[] args) {

        int size = 20;
        int type = 1;//1 construction 2 local search
        //double[] hs = {0.2, 0.4, 0.6, 0.8};
        double[] hs={0.8};

        if (args.length == 2) {
            size = Integer.parseInt(args[0]);
            type = Integer.parseInt(args[1]);
        }
        int[][] table = new int[hs.length][10];
        int[] sums = new int[10];
        for (int i = 0; i < hs.length; i++) {
            double h = hs[i];
            Scheduling sdh;
            if (type == 1) {
                sdh = new ConstructionHeuristic(size, h);
            } else {
                sdh = new LocalSearch(size, h);
            }
            sdh.readDataFromFile();
            int j = 0;
            while (sdh.readNextProblem()) {
                sums[j] = sdh.getSum_P();
                int d = (int) Math.round(sums[j] * h);
                //sdh.printStatus(sdh.getBaseJobs());
                sdh.method(d);
                sdh.printStatus(sdh.getOrderedSet());
                table[i][j] = (int) sdh.getPenalty(d, sdh.getBeginAt(), sdh.getOrderedSet());
                j++;
            }
        }

        //System.out.print("k;SUM;");
        //for (double h : hs) {
        //    System.out.print("h=" + h + ";");
        //}
        //System.out.println("");
        for (int i = 0; i < table[0].length; i++) {
            //System.out.print((i + 1) + ";" + sums[i] + ";");
            for (int j = 0; j < table.length; j++) {
                System.out.print(table[j][i] + ";");
            }
            System.out.println("");
        }

    }

}
