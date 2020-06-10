public class Max {
    public static void main(String[] args) {
        int[] numa = new int[] {0, -5, 2, 14, 10};
        int maxnum = max(numa);
        System.out.println("max is " + maxnum);
    }
    public static int max(int[] a) {
        int maximum = a[0];
        int length = a.length;
        for (int i=1; i < length ; i++) {
            if (a[i] > maximum) {
                maximum = a[i];
            }
        }
        return maximum;
    }
}