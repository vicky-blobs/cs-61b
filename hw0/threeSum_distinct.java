public class threeSum_distinct {
    public static void main(String[] args) {
        int[] boop = new int[] {5, 1, 0, 3, 6};
        int[] beep = new int[] {8, 2, -1, -1, 15};
        System.out.println("three sum possible?: " + threeSum_distinct(boop));
        System.out.println("three sum possible?: " + threeSum_distinct(beep));

    }
    public static boolean threeSum_distinct(int[] A) {
        //3sum9
        int arr_size = A.length;
        for (int i = 0; i < arr_size - 2; i++) {
            for (int j = i + 1; j < arr_size - 1; j++) {
                for (int k = j + 1; k <arr_size; k++) {
                    if (A[i] + A[j] + A[k] == 0) {
                        return true;
                    }
                }
            }
        }
        return false;
    }
}